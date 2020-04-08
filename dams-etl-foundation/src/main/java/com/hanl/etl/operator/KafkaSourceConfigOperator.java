package com.hanl.etl.operator;

import com.hanl.etl.api.AbstractConfigOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/4/2
 * @desc:
 */
public class KafkaSourceConfigOperator extends AbstractConfigOperator {

    private Map<String, Object> kafkaSourceConfig = new HashMap<>();

    public KafkaSourceConfigOperator() {

    }

    public KafkaSourceConfigOperator(Map<String, Object> kafkaSourceConfig) {

        this.kafkaSourceConfig = kafkaSourceConfig;
    }


    public Map<String, Object> getKafkaSourceConfig() {

        return kafkaSourceConfig;
    }

    @Override
    public String toString() {
        return "KafkaSourceConfigOperator{" +
                "kafkaSourceConfig=" + kafkaSourceConfig.toString() +
                '}';
    }
}
