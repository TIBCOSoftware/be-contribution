package com.tibco.cep.driver.kafkastreams;

import com.tibco.cep.driver.kafka.KafkaProperties;

public class KafkaStreamsProperties extends KafkaProperties {

	public static final String PROPERTY_KEY_KAFKA_STREAMS_PROPERTY_PREFIX = "be.channel.kafka.streams";

	public static final String KEY_DESTINATION_APPLICATION_ID = "application.id";
	public static final String KEY_DESTINATION_TOPIC_NAME = "topic.name";
	public static final String KEY_DESTINATION_IS_REGEX_PATTERN = "regex.pattern";
	public static final String KEY_DESTINATION_KEY_SERDES = "key.serdes";
	public static final String KEY_DESTINATION_VALUE_SERDES = "value.serdes";
	public static final String KEY_DESTINATION_AUTO_OFFSET_RESET = "auto.offset.reset";
	public static final String KEY_DESTINATION_PROCESSING_GUARANTEE = "processing.guarantee";
	public static final String KEY_DESTINATION_PROCESSOR_TOPOLOGY = "processor.topology";

}
