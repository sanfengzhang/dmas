package com.hanl.dams.common.config.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/5/22
 * @desc:
 */
public class KafkaConfig {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "bootstrap.servers";

    public static final String KAFKA_ZOOKEEPER_LIST = "zookeeper.list";

    public static final String KAFKA_DEFAULT_TOPIC_PARTITION_NUM = "default.topic.partition.num";

    public static final String KAFKA_DEFAULT_TOPIC_REPLICATION_FACTOR = "default.topic.replication.factor";

    public static final String KAFKA_KEY_DESERIALIZER = "key.deserializer";

    public static final String KAFKA_VALUE_DESERIALIZER = "value.deserializer";

    public static final String KAFKA_KEY_SERIALIZER = "key.serializer";

    public static final String KAFKA_VALUE_SERIALIZER = "value.serializer";

    public static final String KAFKA_MAX_POLL_RECORDS = "max.poll.records";

    public static final String KAFKA_CONSUMER_GROUP_ID = "group.id";

    public static final String KAFKA_CONSUMER_CLIENT_ID = "client.id";

    public static final String KAFKA_AUTO_OFFSET_RESET = "auto.offset.reset";

    public static final String KAFKA_ENABLE_AUTO_COMMIT = "enable.auto.commit";

    public static final String KAFKA_SESSION_TIMEOUT_MS = "session.timeout.ms";

    public static final String KAFKA_REQUEST_TIMEOUT_MS = "request.timeout.ms";

    public static final String KAFKA_ENABLE_KERBEROS = "enable.kerberos";

    public static final String KAFKA_SASL_KERBEROS_SERVICE_NAME = "sasl.kerberos.service.name";

    public static final String KAFKA_LDAP_OPEN = "kafka.ldap.open";

    public static final String KAFKA_SASL_MECHANISM = "sasl.mechanism";

    public static final String KAFKA_SECURITY_PROTOCOL = "security.protocol";

    public static final String KAFKA_SASL_JAAS_CONFIG = "sasl.jaas.config";

    public static final String KAFKA_CONSUME_DATA_TYPE = "serializer_tool";

    public static final int KAFKA_CLOSE_WAIT_SECONDS = 6;

    public static final int KAFKA_RECONN_WAIT_MS = 30000;

    private Map<String, Object> consumerConfig = new HashMap<>();

    private Map<String, Object> produceConfig = new HashMap<>();

    public Map<String, Object> getConsumerConfig() {

        return consumerConfig;
    }

    public Map<String, Object> getProduceConfig() {

        return produceConfig;
    }
}
