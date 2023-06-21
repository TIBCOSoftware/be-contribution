package com.tibco.cep.driver.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.tibco.cep.kernel.service.logging.Level;

/**
 * Enabling error end-point allows applications to collect unsupported type messages to a separate error topic hence making it available to be handled externally.
 * 
 * @author moshaikh
 */
public class KafkaErrorEndpointHelper {
	
    private static final String DEFAULT_ERROR_TOPIC_NAME = "be.application.error.topic";
    
    public static boolean isErrorEndpointEnabled() {
		return Boolean.parseBoolean(System.getProperty(KafkaProperties.PROPERTY_KEY_KAFKA_ERROR_ENDPOINT_ENABLED, Boolean.FALSE.toString()));
	}
    
    /**
     * Identifies the error destination and forwards the message to that destination.
     * Also acks the original message.
     * 
     * @param kafkaDestination
     * @param consumerRecord
     * @return 
     */
    public static boolean forwardMessageToErrorEndpoint(KafkaDestination kafkaDestination, ConsumerRecord consumerRecord) {
		try {
			if (isErrorEndpointEnabled()) {
				String kafkaErrTopicName = System.getProperty(KafkaProperties.PROPERTY_KEY_KAFKA_DEFAULT_ERROR_TOPIC_NAME, DEFAULT_ERROR_TOPIC_NAME);
				kafkaDestination.getLogger().log(Level.WARN, "Forwarding message to error topic, message-key:" + consumerRecord.key() + " error-topic:" + kafkaErrTopicName);
				
				BEKafkaProducer beKafkaProducer = kafkaDestination.getProducer();//Use producer of same destination to send out this error record.
				ProducerRecord producerRecord = new ProducerRecord(kafkaErrTopicName, consumerRecord.key(), consumerRecord.value());
				beKafkaProducer.sendProducerRecord(producerRecord);
				
				return true;
			}
		} catch(Exception e) {
			kafkaDestination.getLogger().log(Level.ERROR, "Error while forwarding message to error topic, message-key:" + consumerRecord.key() + " error:" + e.getMessage());
		}
		return false;
    }
}
