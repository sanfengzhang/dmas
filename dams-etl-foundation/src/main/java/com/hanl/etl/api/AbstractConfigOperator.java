package com.hanl.etl.api;

import jdk.internal.org.objectweb.asm.Handle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/4/2
 * @desc:
 * 比如某ETL任务从Kafka集群拉取数据，那么需要初始化一个PullSourceData
 * 的节点。但需要兼容不同的环境,比如：Flink环境下如何创建Source？单机精简
 * 版无任何其他数据框架，怎么创建Source
 * 所以这里只是抽象一个Source的基本描述和需要的配置参数，然后具体的Source可以从改节点
 * 中去获取实现
 */
public class AbstractConfigOperator implements Operator {

    private Map<String,Object> configMap=new HashMap<>();


    public Map<String, Object> getConfigMap() {
        return configMap;
    }
}
