package com.hanl.etl.api;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;

import com.google.common.collect.Lists;
import com.hanl.etl.base.Configs;
import com.hanl.etl.base.Metrics;

import com.hanl.etl.exception.OperatorCompilationException;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2020/3/5
 * @desc:
 */
public abstract class AbstractOperator<T extends RecordWrapper> implements Operator<T> {

    private final Configs configs;

    private final Operator parent;

    private final Operator child;

    private FlowContext context;

    private String name;

    private final Config config;

    private final Meter numProcessCallsMeter;

    private final Meter numNotifyCallsMeter;

    private static final boolean IS_MEASURING_METRICS = "true".equals(System.getProperty("isMeasuringMetrics", "true"));

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected AbstractOperator(OperatorBuilder builder, Config config, Operator parent, Operator child, FlowContext context) {
        Preconditions.checkNotNull(builder);
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(child);
        Preconditions.checkNotNull(context);
        this.config = config;
        this.configs = new Configs();
        this.parent = parent;
        this.child = child;
        this.context = context;
        Preconditions.checkArgument(builder.getNames().size() > 0);
        this.name = "morphline." + builder.getNames().iterator().next();
        this.numProcessCallsMeter = getMeter(Metrics.NUM_PROCESS_CALLS);
        this.numNotifyCallsMeter = getMeter(Metrics.NUM_NOTIFY_CALLS);

    }

    @Override
    public final void notify(T notification) {
        beforeNotify(notification);
        doNotify(notification);
    }

    private void beforeNotify(T notification) {
        if (isMeasuringMetrics()) {
            numNotifyCallsMeter.mark();
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("beforeNotify: {}", notification);
        } else {
            LOG.debug("beforeNotify()");
        }

    }

    /**
     * Processes the given notification on the control plane of the subtree rooted at this command.
     */
    protected void doNotify(T notification) {
        getChild().notify(notification);
    }

    /**
     * Returns whether or not metrics should be measured.
     */
    protected final boolean isMeasuringMetrics() {
        return IS_MEASURING_METRICS;
    }

    protected FlowContext getContext() {
        return context;
    }

    protected Meter getMeter(String... names) {
        return getContext().getMetricRegistry().meter(getMetricName(names));
    }

    private String getMetricName(String... names) {
        return MetricRegistry.name(name, names);
    }

    private void beforeProcess(RecordWrapper record) {
        if (isMeasuringMetrics()) {
            numProcessCallsMeter.mark();
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("beforeProcess: {}", record);
        } else {
            LOG.debug("beforeProcess()");
        }
    }

    @Override
    public final boolean process(RecordWrapper record) {
        beforeProcess(record);
        long start = System.currentTimeMillis();
        boolean success = doProcess(record);
        long end = System.currentTimeMillis();
        if (end - start > 10) {
            LOG.warn("Command processing {}ms ,record={}", end - start, record);
        }
        if (!success) {
            LOG.debug("Command failed!");
        }
        System.out.println(end-start);
        return success;
    }

    /**
     * Processes the given record on the data plane of this command.
     *
     * @return true to indicate that processing shall continue, false to indicate that backtracking
     * shall be done
     */
    protected boolean doProcess(RecordWrapper record) {
        return getChild().process(record);
    }


    /**
     * Helper that checks if the user provided configuration parameters are valid.
     */
    protected void validateArguments() {
        getConfigs().validateArguments(getConfig());
    }

    /**
     * Returns the JSON configuration of this command.
     */
    protected Config getConfig() {
        return config;
    }


    protected List<Operator> buildOperatorChain(Config rootConfig, String configKey, Operator finalChild, boolean ignoreNotifications) {
        Preconditions.checkNotNull(rootConfig);
        Preconditions.checkNotNull(configKey);
        Preconditions.checkNotNull(finalChild);
        List<? extends Config> commandConfigs = new Configs().getConfigList(rootConfig, configKey, Collections.<Config>emptyList());
        List<Operator> commands = Lists.newArrayList();
        Operator currentParent = this;
        Connector lastConnector = null;
        for (int i = 0; i < commandConfigs.size(); i++) {
            boolean isLast = (i == commandConfigs.size() - 1);
            Connector connector = new Connector(ignoreNotifications && isLast);
            if (isLast) {
                connector.setChild(finalChild);
            }
            Config cmdConfig = commandConfigs.get(i);
            Operator cmd = buildOperator(cmdConfig, currentParent, connector);
            commands.add(cmd);
            if (i > 0) {
                lastConnector.setChild(cmd);
            }
            connector.setParent(cmd);
            currentParent = connector;
            lastConnector = connector;
        }
        return commands;
    }

    /**
     * Factory method to create a command rooted at the given cmdConfig. The command will feed records
     * into finalChild. The command will have currentParent as it's parent.
     */
    protected Operator<T> buildOperator(Config cmdConfig, Operator currentParent, Operator finalChild) {
        Preconditions.checkNotNull(cmdConfig);
        Preconditions.checkNotNull(currentParent);
        Preconditions.checkNotNull(finalChild);
        Set<Map.Entry<String, Object>> entries = cmdConfig.root().unwrapped().entrySet();
        if (entries.size() != 1) {
            throw new OperatorCompilationException("Illegal number of entries: " + entries.size(), cmdConfig);
        }
        Map.Entry<String, Object> entry = entries.iterator().next();
        String cmdName = entry.getKey();

        Class cmdClass;
        LOG.trace("Building command: {}", cmdName);
        if (!cmdName.contains(".") && !cmdName.contains("/")) {
            cmdClass = getContext().getOperatorBuilder(cmdName);
            if (cmdClass == null) {
                throw new OperatorCompilationException("No operator builder registered for name: " + cmdName, cmdConfig);
            }
        } else {
            String className = cmdName.replace('/', '.');
            try {
                cmdClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new OperatorCompilationException("Cannot find operator class: " + className, cmdConfig, e);
            }
        }
        Object obj;
        try {
            obj = cmdClass.newInstance();
        } catch (Exception e) {
            throw new OperatorCompilationException("Cannot instantiate command class: " + cmdClass.getName(), cmdConfig, e);
        }
        if (!(obj instanceof OperatorBuilder)) {
            throw new OperatorCompilationException("Type of command " + cmdName + " must be an instance of "
                    + OperatorBuilder.class.getName() + " but is: " + cmdClass.getName(), cmdConfig);
        }
        OperatorBuilder builder = (OperatorBuilder) obj;
        Operator operator = builder.build(cmdConfig.getConfig(cmdName), currentParent, finalChild, getContext());
        return operator;
    }

    /**
     * Returns a helper for convenient access to the JSON configuration of this command.
     */
    protected Configs getConfigs() {
        return configs;
    }

    @Override
    public Operator<T> getParent() {
        return parent;
    }

    /**
     * Returns the child of this command. The parent of a command A is the command B that passes
     * records to A. A is the child of B.
     */
    protected Operator<T> getChild() {
        return child;
    }
}
