package com.hanl.hpf.threadpool;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author: Hanl
 * @date :2020/4/13
 * @desc:
 */
public class DefaultThreadFactory {

    public static ThreadFactory build(String namePattern, boolean daemon, int priority) {

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern(namePattern)
                .daemon(daemon)
                .priority(priority)
                .build();
        return factory;
    }

    public static ThreadFactory build(String namePattern) {
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern(namePattern)
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build();
        return factory;
    }

}
