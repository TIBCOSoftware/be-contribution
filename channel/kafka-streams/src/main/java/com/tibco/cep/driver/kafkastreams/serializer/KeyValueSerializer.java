package com.tibco.cep.driver.kafkastreams.serializer;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.streams.KeyValue;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.kafka.KafkaEvent;
import com.tibco.cep.kernel.service.logging.Logger;

public class KeyValueSerializer extends BaseEventSerializer {
	
	private static final String KEY_PROPERTY_NAME = "RECORD_KEY";
	private static final String VALUE_PROPERTY_NAME = "RECORD_VALUE";
	
	private Logger logger;
	private String destinationName;
	private Properties destinationProperties;
	
	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.logger = logger;
		this.destinationName = destinationName;
		this.destinationProperties = destinationProperties;
		
	}

	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		
		if(message instanceof KeyValue) {
			
			KeyValue record = (KeyValue) message;
			Object key = record.key;
			Object value = record.value;
			
			Event event = new KafkaEvent();
			event.setProperty(KEY_PROPERTY_NAME, key);
			event.setProperty(VALUE_PROPERTY_NAME, value);
			
			return event;
		} else {
			return null;
		}
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
