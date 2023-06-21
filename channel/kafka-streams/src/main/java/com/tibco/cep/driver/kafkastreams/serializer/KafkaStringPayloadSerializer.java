package com.tibco.cep.driver.kafka.serializer;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.kafka.KafkaEvent;
import com.tibco.cep.driver.kafka.KafkaProperties;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * 
 * @author moshaikh
 */
public class KafkaStringPayloadSerializer extends BaseEventSerializer implements KafkaSerializer {
	
	private String topic;

	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.topic = destinationProperties.getProperty(KafkaProperties.KEY_DESTINATION_TOPIC_NAME);
	}

	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		Event event = new KafkaEvent();
		if (message instanceof ConsumerRecord) {
			ConsumerRecord kafkaMessage = (ConsumerRecord) message;
			String val = null;
			if (kafkaMessage.value() instanceof String) {
				val = (String)kafkaMessage.value();
			} else if (kafkaMessage.value() instanceof byte[]) {
				val = new String((byte[])kafkaMessage.value());
			}
			//setting payload
			event.setPayload(val!=null?val.getBytes():new byte[0]);
		} else {
			return null;
		}
		return event;
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		Object key = event.getPropertyValue(KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY);
		ProducerRecord kafkaMessage = null;
		String replyTopic = null;
		if(null !=properties &&  properties.containsKey(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC)) {
			 replyTopic =(String) properties.get(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC);
		}
		
		if (key != null) {
			kafkaMessage = new ProducerRecord(null == replyTopic ? topic : replyTopic, String.valueOf(key), event.getPayload() == null ? "" : new String(event.getPayload()));
		} else {
			kafkaMessage = new ProducerRecord(null == replyTopic ? topic : replyTopic, event.getExtId(), event.getPayload() == null ? "" : new String(event.getPayload()));
		}
		return kafkaMessage;
	}

	@Override
	public String keySerializer() {
		return "org.apache.kafka.common.serialization.StringSerializer";
	}

	@Override
	public String valueSerializer() {
		return "org.apache.kafka.common.serialization.StringSerializer";
	}

	@Override
	public String keyDeserializer() {
		return "org.apache.kafka.common.serialization.StringDeserializer";
	}

	@Override
	public String valueDeserializer() {
		return "org.apache.kafka.common.serialization.StringDeserializer";
	}
}
