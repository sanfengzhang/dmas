package com.hanl.etl.api;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.hanl.etl.exception.ExceptionHandler;
import com.hanl.etl.exception.FlowRuntimeException;
import com.hanl.etl.shaded.com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author: Hanl
 * @date :2020/3/5
 * @desc:
 * 在做任何系统的时候要抓住系统关心的、敏感的、和便于维护排查的系统。比如ETL Flow
 * 一般我们比较关心Flow实现后的性能、性能问题排查、Flow运行过程中故障处理策略。
 */
public class FlowContext {

    private Map<String, Object> settings;//可以为Flow上下文加入一些参数

    private ExceptionHandler exceptionHandler;//Flow故障时候的处理策略

    private MetricRegistry metricRegistry;//指标埋点注册

    private HealthCheckRegistry healthCheckRegistry;//健康检查埋点

    private Map<String, Class<OperatorBuilder>> commandBuilders = Collections.emptyMap();//Flow中包含的Node信息

    public static final String COMMAND_CLASS_EXPORT_ID = "command.class.export.dir";

    private static final Logger LOG = LoggerFactory.getLogger(FlowContext.class);

    /**
     * For public access use {@link Builder#build()} instead
     */
    protected FlowContext() {
    }


    public Map<String, Object> getSettings() {
        assert settings != null;
        return settings;
    }

    public TypedSettings getTypedSettings() {
        return new TypedSettings(getSettings());
    }

    public ExceptionHandler getExceptionHandler() {
        assert exceptionHandler != null;
        return exceptionHandler;
    }

    public MetricRegistry getMetricRegistry() {
        assert metricRegistry != null;
        return metricRegistry;
    }

    public HealthCheckRegistry getHealthCheckRegistry() {
        assert healthCheckRegistry != null;
        return healthCheckRegistry;
    }

    public Class<OperatorBuilder> getOperatorBuilder(String builderName) {
        return commandBuilders.get(builderName);
    }

    public void importOperatorBuilders(Collection<String> importSpecs) {
        if (commandBuilders == Collections.EMPTY_MAP) { // intentionally effective no more than once
            commandBuilders = Maps.newHashMap();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Importing commands from Java classpath: {}", System.getProperty("java.class.path"));
            } else {
                LOG.info("Importing commands");
            }
            Collection<Class<OperatorBuilder>> builderClasses = getTopLevelClasses(importSpecs, OperatorBuilder.class);
            for (Class<OperatorBuilder> builderClass : builderClasses) {
                try {
                    OperatorBuilder builder = builderClass.newInstance();
                    for (String builderName : builder.getNames()) {
                        LOG.debug("Importing command: {} from class: {}", builderName, builderClass.getName());
                        if (builderName.contains(".")) {
                            LOG.warn("Command name should not contain a period character: " + builderName);
                        }
                        commandBuilders.put(builderName, builderClass);
                    }
                } catch (Exception e) {
                    throw new FlowRuntimeException(e);
                }
            }
            LOG.info("Done importing commands");
        }
    }

    /**
     * Returns all classes that implement the given interface and are contained in a Java package or
     * its subpackages (importSpec ends with ".**"), or are contained in the given Java package
     * (importSpec ends with ".*"), or are a Java class with the given fully qualified class name
     * (importSpec ends otherwise).
     * <p>
     * Uses a shaded version of com.google.guava.reflect-14.0.1 to enable running with prior versions
     * of guava without issues.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    <T> Collection<Class<T>> getTopLevelClasses(Collection<String> importSpecs, Class<T> iface) {
        // count number of FQCNs in importSpecs
        int fqcnCount = 0;
        for (String importSpec : importSpecs) {
            if (!(importSpec.endsWith(".*") || importSpec.endsWith(".**"))) {
                fqcnCount++;
            }
        }

        HashMap<String, Class<T>> classes = Maps.newLinkedHashMap();
        for (ClassLoader loader : getClassLoaders()) {
            if (importSpecs.size() == fqcnCount) {
                // importSpecs consists solely of FQCNs!
                // Thus, we can omit the expensive ClassPath.from(loader) scan of the classpath.
                for (String importSpec : importSpecs) {
                    Class clazz;
                    try {
                        //clazz = Class.forName(importSpec, true, loader);
                        clazz = loader.loadClass(importSpec);
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                    addClass(clazz, classes, iface);
                }
            } else {
                // Need to scan the classpath via ClassPath.from(loader)
                ClassPath classPath;
                try {
                    classPath = ClassPath.from(loader);
                } catch (IOException e) {
                    continue;
                }
                for (String importSpec : importSpecs) {
                    Set<ClassPath.ClassInfo> classInfos = null;
                    if (importSpec.endsWith(".**")) {
                        String packageName = importSpec.substring(0, importSpec.length() - ".**".length());
                        classInfos = classPath.getTopLevelClassesRecursive(packageName);
                    } else if (importSpec.endsWith(".*")) {
                        String packageName = importSpec.substring(0, importSpec.length() - ".*".length());
                        classInfos = classPath.getTopLevelClasses(packageName);
                    } else { // importSpec is assumed to be a fully qualified class name
                        Class clazz;
                        try {
                            //clazz = Class.forName(importSpec, true, loader);
                            clazz = loader.loadClass(importSpec);
                        } catch (ClassNotFoundException e) {
                            continue;
                        }
                        addClass(clazz, classes, iface);
                        continue;
                    }

                    for (ClassPath.ClassInfo info : classInfos) {
                        Class clazz;
                        try {
                            clazz = info.load();
                            //            clazz = Class.forName(info.getName());
                        } catch (NoClassDefFoundError e) {
                            continue;
                        } catch (ExceptionInInitializerError e) {
                            continue;
                        } catch (UnsatisfiedLinkError e) {
                            continue;
                        }
                        addClass(clazz, classes, iface);
                    }
                }
            }
        }
        return classes.values();
    }

    private <T> void addClass(Class<T> clazz, HashMap<String, Class<T>> classes, Class<T> iface) {
        if (!classes.containsKey(clazz.getName())
                && iface.isAssignableFrom(clazz)
                && !clazz.isInterface()
                && !Modifier.isAbstract(clazz.getModifiers())) {
            for (Constructor ctor : clazz.getConstructors()) { // all public ctors
                if (ctor.getParameterTypes().length == 0) { // is public zero-arg ctor?
                    classes.put(clazz.getName(), clazz);
                }
            }
        }
    }

    private ClassLoader[] getClassLoaders() {
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader myLoader = getClass().getClassLoader();
        if (contextLoader == null) {
            return new ClassLoader[]{myLoader};
        } else if (contextLoader == myLoader || myLoader == null) {
            return new ClassLoader[]{contextLoader};
        } else {
            return new ClassLoader[]{contextLoader, myLoader};
        }
    }



    /**
     * Helper to construct a {@link FlowContext} instance.
     * <p>
     * Example usage:
     *
     * <pre>
     * MorphlineContext context = new FlowContext.Builder().setMetricRegistry(new MetricRegistry()).build();
     * </pre>
     */
    public static class Builder {

        protected FlowContext context = create();
        private Map<String, Object> settings = Maps.newHashMap();
        private ExceptionHandler exceptionHandler = new DefaultExceptionHandler();
        private MetricRegistry metricRegistry = new MetricRegistry();
        private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

        public Builder() {
        }

        public Builder setSettings(Map<String, Object> settings) {
            Preconditions.checkNotNull(settings);
            this.settings = settings;
            return this;
        }

        public Builder setExceptionHandler(ExceptionHandler exceptionHandler) {
            Preconditions.checkNotNull(exceptionHandler);
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public Builder setMetricRegistry(MetricRegistry metricRegistry) {
            Preconditions.checkNotNull(metricRegistry);
            this.metricRegistry = metricRegistry;
            return this;
        }

        public Builder setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
            Preconditions.checkNotNull(healthCheckRegistry);
            this.healthCheckRegistry = healthCheckRegistry;
            return this;
        }

        public void loadExportJar() {
            String jarFileDirPath = null;
            try {
                Object obj = settings.get(COMMAND_CLASS_EXPORT_ID);
                if (obj != null) {
                    jarFileDirPath = obj.toString();
                    File jarFileDir = new File(jarFileDirPath);
                    if (jarFileDir.isDirectory()) {
                        File jars[] = jarFileDir.listFiles();
                        if (jars.length > 0) {
                            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                            add.setAccessible(true);
                            int size = jars.length;
                            Object[] urlArray = new Object[size];
                            for (int i = 0; i < size; i++) {
                                urlArray[i] = jars[0].toURI().toURL();
                                LOG.info("add export jar url={}", urlArray[i]);
                            }
                            add.invoke(classLoader, urlArray);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Load export jars failed jarFileDirPath" + jarFileDirPath, e);
            }
        }

        public FlowContext build() {
            context.settings = settings;
            context.exceptionHandler = exceptionHandler;
            context.metricRegistry = metricRegistry;
            context.healthCheckRegistry = healthCheckRegistry;
            loadExportJar();
            return context;
        }

        protected FlowContext create() {
            return new FlowContext();
        }

    }


    private static final class DefaultExceptionHandler<T> implements ExceptionHandler<T> {

        @Override
        public void handleException(Throwable t, T record) {
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new FlowRuntimeException(t);
        }

    }
}
