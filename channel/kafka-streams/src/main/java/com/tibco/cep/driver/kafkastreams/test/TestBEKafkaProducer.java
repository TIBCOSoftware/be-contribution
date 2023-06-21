package com.tibco.cep.driver.kafka.test;

import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.tibco.cep.driver.kafka.BEKafkaProducer;
import com.tibco.cep.driver.kafka.KafkaProperties;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * Standalone class to test BEKafkaProducer.
 * 
 * @author moshaikh
 */
public class TestBEKafkaProducer {
	public static void main(String[] args) throws Exception {
		final String TEST_TOPIC_NAME = "be_kafka_topic";
		
		Logger testLogger = new KafkaTestLogger();
		
		//Kafka consumer configuration settings
		Properties props = new Properties();
		props.put(KafkaProperties.KEY_CHANNEL_BOOTSTRAP_SERVER, "localhost:9092");
		props.put(KafkaProperties.KEY_DESTINATION_CLIENT_ID, "be_test_client");
		props.put(KafkaProperties.KEY_DESTINATION_COMPRESSION_TYPE, "none");
		props.put(KafkaProperties.KEY_DESTINATION_TOPIC_NAME, TEST_TOPIC_NAME);
		props.put(KafkaProperties.KEY_DESTINATION_SYNC_SENDER, "true");
		props.put(KafkaProperties.KEY_DESTINATION_SYNC_SENDER_MAX_WAIT, "15000");
		
		Properties beProps = new Properties();
//		beProps.put("be.channel.kafka.batch.size", "1048576");
//		beProps.put("be.channel.kafka.linger.ms", "60000");
//		beProps.put("be.channel.kafka.max.block.ms", "10000");
//		beProps.put("be.channel.kafka.sasl.mechanism", "PLAIN");
//		beProps.put("be.channel.kafka.security.protocol", "SASL_PLAINTEXT");
//		beProps.put("be.channel.kafka.sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");

		BEKafkaProducer p = new BEKafkaProducer(null, props, beProps, testLogger);
		
		for (int i=0;i<1000;i++) {
			ProducerRecord producerRecord = new ProducerRecord(TEST_TOPIC_NAME, String.valueOf(i), "test message".getBytes());
			try {
				p.sendProducerRecord(producerRecord);
				testLogger.log(Level.DEBUG, new Date() + " Message sent " + producerRecord);
			} catch (Exception e) {
				testLogger.log(Level.ERROR, e, new Date() + " Sending Failed : " + e.getMessage());
			}
			Thread.sleep(1000);
		}
		p.close();
	}
}
