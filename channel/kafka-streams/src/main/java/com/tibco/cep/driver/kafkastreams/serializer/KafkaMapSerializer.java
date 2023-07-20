package com.tibco.cep.driver.kafkastreams.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KeyValue;

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
public class KafkaMapSerializer extends BaseEventSerializer {

	private String topic;

	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.topic = destinationProperties.getProperty(KafkaProperties.KEY_DESTINATION_TOPIC_NAME);
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		Map<String, Object> messageMap = new HashMap<String, Object>();
		// setting ext id
		messageMap.put(KafkaProperties.RESERVED_EVENT_PROP_EXT_ID, event.getExtId());
		// setting event id
		messageMap.put(KafkaProperties.RESERVED_EVENT_PROP_EVENT_ID, event.getId());

		boolean includeEventType = isIncludeEventType(properties);
		if (includeEventType && event.getEventUri() != null) {
			messageMap.put(KafkaProperties.BE_NAMESPACE, event.getEventUri());
			messageMap.put(KafkaProperties.BE_NAME, event.getEventUri()
					.substring(event.getEventUri().lastIndexOf('}') + 1, event.getEventUri().length()));
		}

		// setting payload
		messageMap.put(KafkaProperties.RESERVED_EVENT_PROP_PAYLOAD, event.getPayload());

		for (String prop : event.getAllPropertyNames()) {
			messageMap.put(prop, event.getPropertyValue(prop));
		}
		Object key = event.getPropertyValue(KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY);
		ProducerRecord kafkaMessage = null;
		
		String replyTopic = null ;
		if(null !=properties &&  properties.containsKey(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC)) {
			 replyTopic =(String) properties.get(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC);
		}
      
		if (key != null) {
			kafkaMessage = new ProducerRecord( null ==  replyTopic  ? topic : replyTopic, String.valueOf(key), serializeMap(messageMap));
		} else {
			kafkaMessage = new ProducerRecord(null ==  replyTopic  ? topic : replyTopic, serializeMap(messageMap));
		}
		
		return kafkaMessage;
	}

	@Override
	public Event deserializeUserEvent(Object kafkaRecord, Map<String, Object> properties) throws Exception {
		Event event = new KafkaEvent();
		boolean includeEventType = isIncludeEventType(properties);

		byte[] value = null;
		if (kafkaRecord instanceof ConsumerRecord) {
			ConsumerRecord consumerRec = (ConsumerRecord) kafkaRecord;
			if (!(consumerRec.value() instanceof byte[])) {
				throw new Exception("Failed to deserialize value into an Event.");
			}
			value = (byte[]) consumerRec.value();
		} else if (kafkaRecord instanceof KeyValue<?, ?>) {
			// Reuse this serializer for Kafka Streams
			KeyValue kvPair = (KeyValue) kafkaRecord;
			if (kvPair.value != null && kvPair.value instanceof byte[])
				value = (byte[]) kvPair.value;
		}

		if (value == null) {
			return null;
		}

		Map<String, Object> valueMap = deserializeMap(value);
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			event.setProperty(entry.getKey(), entry.getValue());
		}
		if (includeEventType && valueMap.containsKey(KafkaProperties.BE_NAMESPACE)) {
			String eventUri = (String) valueMap.get(KafkaProperties.BE_NAMESPACE);
			if (eventUri != null && eventUri.contains(KafkaProperties.ENTITY_NS)) {
				eventUri = eventUri.substring(
						eventUri.indexOf(KafkaProperties.ENTITY_NS) + KafkaProperties.ENTITY_NS.length(),
						eventUri.lastIndexOf('}'));
			}
			event.setEventUri(eventUri);
		}
		// setting payload
		event.setPayload(valueMap.get(KafkaProperties.RESERVED_EVENT_PROP_PAYLOAD) != null
				? (byte[]) valueMap.get(KafkaProperties.RESERVED_EVENT_PROP_PAYLOAD)
				: new byte[0]);
		// setting extID
		event.setExtId(valueMap.get(KafkaProperties.RESERVED_EVENT_PROP_EXT_ID) != null
				? (String) valueMap.get(KafkaProperties.RESERVED_EVENT_PROP_EXT_ID)
				: null);

		return event;
	}

	private byte[] serializeMap(Map map) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(map);
		oos.close();
		return baos.toByteArray();
	}

	private Map deserializeMap(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (Map) ois.readObject();
	}

	/**
	 * By default IncludeEventType is true
	 * 
	 * @param properties
	 * @return
	 */
	private boolean isIncludeEventType(Map<String, Object> properties) {
		if (properties == null || !properties.containsKey(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE)) {
			return true;
		} else {
			return (boolean) properties.get(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE);
		}
	}

}
