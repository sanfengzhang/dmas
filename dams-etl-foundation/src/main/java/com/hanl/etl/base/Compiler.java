package com.hanl.etl.base;

import com.google.common.base.Preconditions;
import com.hanl.etl.api.FlowContext;
import com.hanl.etl.api.Operator;
import com.hanl.etl.api.RecordWrapper;
import com.hanl.etl.exception.OperatorCompilationException;
import com.hanl.etl.operator.DropRecordBuilder;
import com.hanl.etl.operator.Flow;
import com.hanl.etl.operator.builder.FlowBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Tool to parse and compile a morphline file or morphline config.
 */
public final class Compiler<T extends RecordWrapper> {

    private static final Object LOCK = new Object();

    private static final Logger logger = LoggerFactory.getLogger(Compiler.class);

    public Compiler() {

    }

    public Flow<T> compile(Map<String, Object> configMap, FlowContext flowContext, Operator finalChild) {
        Config config = ConfigFactory.parseMap(configMap);
        return compile(config, flowContext, finalChild);
    }

    /**
     * Parses the given morphlineFile, then finds the morphline with the given morphlineId within,
     * then compiles the morphline and returns the corresponding morphline command. The returned
     * command will feed records into finalChild.
     */
    public Operator compile(File morphlineFile, String morphlineId, FlowContext flowContext, Operator finalChild, Config... overrides) {
        Config config;
        try {
            config = parse(morphlineFile, overrides);
        } catch (Exception e) {
            throw new OperatorCompilationException("Cannot parse morphline file: " + morphlineFile, null, e);
        }
        Config morphlineConfig = find(morphlineId, config, morphlineFile.getPath());
        Operator morphlineOperator = compile(morphlineConfig, flowContext, finalChild);
        return morphlineOperator;
    }

    /**
     * Loads the given config file from the local file system
     */
    public Config parse(File file, Config... overrides) throws IOException {
        if (file == null || file.getPath().trim().length() == 0) {
            throw new OperatorCompilationException("Missing morphlineFile parameter", null);
        }
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file);
        }
        if (!file.canRead()) {
            throw new IOException("Insufficient permissions to read file: " + file);
        }
        Config config = ConfigFactory.parseFile(file);
        for (Config override : overrides) {
            config = override.withFallback(config);
        }

        synchronized (LOCK) {
            ConfigFactory.invalidateCaches();
            config = ConfigFactory.load(config);
            config.checkValid(ConfigFactory.defaultReference()); // eagerly validate aspects of tree config
        }
        return config;
    }

    /**
     * Finds the given morphline id within the given morphline config, using the given nameForErrorMsg
     * for error reporting.
     */
    public Config find(String morphlineId, Config config, String nameForErrorMsg) {
        List<? extends Config> morphlineConfigs = config.getConfigList("morphlines");
        if (morphlineConfigs.size() == 0) {
            throw new OperatorCompilationException(
                    "Morphline file must contain at least one morphline: " + nameForErrorMsg, null);
        }
        if (morphlineId != null) {
            morphlineId = morphlineId.trim();
        }
        if (morphlineId != null && morphlineId.length() == 0) {
            morphlineId = null;
        }
        Config morphlineConfig = null;
        if (morphlineId == null) {
            morphlineConfig = morphlineConfigs.get(0);
            Preconditions.checkNotNull(morphlineConfig);
        } else {
            for (Config candidate : morphlineConfigs) {
                if (morphlineId.equals(new Configs().getString(candidate, "id", null))) {
                    morphlineConfig = candidate;
                    break;
                }
            }
            if (morphlineConfig == null) {
                throw new OperatorCompilationException(
                        "Morphline id '" + morphlineId + "' not found in morphline file: " + nameForErrorMsg, null);
            }
        }
        return morphlineConfig;
    }

    /**
     * Compiles the given morphline config using the given morphline context. The returned command
     * will feed records into finalChild or into /dev/null if finalChild is null.
     */
    public Flow<T> compile(Config morphlineConfig, FlowContext flowContext, Operator finalChild) {
        if (finalChild == null) {
            finalChild = new DropRecordBuilder().build(null, null, null, flowContext);
        }
        Operator<T> operator = new FlowBuilder().build(morphlineConfig, null, finalChild, flowContext);
        Flow flow = null;
        if (operator instanceof Flow) {
            flow = (Flow) operator;
        }
        return flow;
    }

}
