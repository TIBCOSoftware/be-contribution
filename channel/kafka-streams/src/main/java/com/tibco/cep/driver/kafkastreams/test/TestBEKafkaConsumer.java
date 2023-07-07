package com.tibco.cep.driver.kafka.test;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.kafka.BEKafkaConsumer;
import com.tibco.cep.driver.kafka.KafkaDestination;
import com.tibco.cep.driver.kafka.KafkaEvent;
import com.tibco.cep.driver.kafka.KafkaProperties;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * Standalone class to test BEKafkaConsumer.
 * 
 * @author moshaikh
 */
public class TestBEKafkaConsumer {
	public static void main(String[] args) throws Exception {
		final String TEST_TOPIC_NAME = "be_kafka_topic";//"be.application.error.topic"
		
		System.setProperty(KafkaProperties.PROPERTY_KEY_KAFKA_ERROR_ENDPOINT_ENABLED, "false");
		System.setProperty(KafkaProperties.PROPERTY_KEY_KAFKA_PROCESS_EVENT_MAX_ATTEMPTS, "1");
		
		//Kafka consumer configuration settings
		Properties props = new Properties();
		props.put(KafkaProperties.KEY_CHANNEL_BOOTSTRAP_SERVER, "localhost:9092");
		props.put(KafkaProperties.KEY_DESTINATION_GROUP_ID, "be_test_group");
		props.put(KafkaProperties.KEY_DESTINATION_ENABLE_AUTOCOMMIT, "false");
		props.put(KafkaProperties.KEY_DESTINATION_POLL_INTERVAL, "2000");
		props.put(KafkaProperties.KEY_DESTINATION_HEARTBEAT_INTERVAL, "1000");
		props.put(KafkaProperties.KEY_DESTINATION_SESSION_TIMEOUT, "10000");
		props.put(KafkaProperties.KEY_DESTINATION_TOPIC_NAME, TEST_TOPIC_NAME);
		
		Properties beProps = new Properties();
//		beProps.put("be.channel.kafka.client.id", "clientId_custom");
//		beProps.put("be.channel.kafka.sasl.mechanism", "PLAIN");
//		beProps.put("be.channel.kafka.security.protocol", "SASL_PLAINTEXT");
//		beProps.put("be.channel.kafka.sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");

		String clientId = "be_test_client";
		
		EventProcessor eventProcessor = new EventProcessor() {
			public void processEvent(Event event) throws Exception {}
			public String getRuleSessionName(){return null;}
		};
		BaseEventSerializer testSerializer = new BaseEventSerializer() {
			public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {return null;}
			public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {}
			public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
				ConsumerRecord rec = (ConsumerRecord)message;
				System.out.printf(new Date() + "offset = %d, key = %s, value = %s\n", rec.offset(), rec.key(), new String((byte[])rec.value()));
				return new KafkaEvent();
			}
		};
		KafkaDestination testDest = new KafkaDestination() {
			@Override
			public Logger getLogger() {
				return new KafkaTestLogger();
			}
			@Override
			public BaseEventSerializer getSerializer() {
				return testSerializer;
			}
		};
		
		BEKafkaConsumer c = new BEKafkaConsumer(eventProcessor, props, beProps, clientId, testDest);
		Thread t = new Thread(c);
		t.start();
		c.resumeConsumer();
	}
}
