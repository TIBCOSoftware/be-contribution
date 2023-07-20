package com.tibco.cep.driver.kafkastreams.serializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KeyValue;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.kafka.KafkaEvent;
import com.tibco.cep.driver.kafka.KafkaProperties;
import com.tibco.cep.driver.kafka.serializer.KafkaSerializer;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * 
 * @author moshaikh
 */
public class KafkaJsonSerializer extends BaseEventSerializer implements KafkaSerializer {

	private String topic;
	private Logger logger;
	private boolean skipBEAttributes;

	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.topic = destinationProperties.getProperty(KafkaProperties.KEY_DESTINATION_TOPIC_NAME);
		this.logger = logger;
		this.skipBEAttributes = Boolean
				.parseBoolean(System.getProperty(KafkaProperties.KEY_SKIP_BE_ATTRIBUTES, "false"));
	}

	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		Event event = null;
		String val = null;
		if (message instanceof ConsumerRecord) {
			ConsumerRecord<String, String> kafkaMessage = (ConsumerRecord<String, String>) message;
			val = new String(kafkaMessage.value());
		} else if (message instanceof KeyValue<?, ?>) {
			// Reuse this serializer for Kafka Streams
			KeyValue kvPair = (KeyValue) message;
			if (kvPair.value != null && kvPair.value instanceof String)
				val = kvPair.value.toString();
		}

		if (val == null) {
			return null;
		}

		event = deserializeFromJSON(val, properties);

		return event;
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		Object key = event.getPropertyValue(KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY);

		String eventJsonStr = serializeToJSON(event, properties, false);
		ProducerRecord kafkaMessage = null;
		
		String replyTopic = null ; 
		if(null !=properties &&  properties.containsKey(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC)) {
			 replyTopic =(String) properties.get(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC);
		}
		
		if (key != null) {
			kafkaMessage = new ProducerRecord(null == replyTopic ? topic : replyTopic, String.valueOf(key), eventJsonStr == null ? "" : eventJsonStr);
		} else {
			kafkaMessage = new ProducerRecord(null == replyTopic ? topic : replyTopic, event.getExtId(), eventJsonStr == null ? "" : eventJsonStr);
		}
		return kafkaMessage;
	}

	private String serializeToJSON(EventWithId event, Map<String, Object> properties, boolean pretty)
			throws IOException {
		Map<String, Object> jsonEntries = new LinkedHashMap<String, Object>();

		Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();
		jsonEntries.put("attributes", attributeMap);
		attributeMap.put("Id", String.valueOf(event.getId()));
		if (event.getExtId() != null && !event.getExtId().isEmpty()) {
			attributeMap.put("extId", event.getExtId());
		}
		if (isIncludeEventType(properties) && event.getEventUri() != null) {
			if (event.getEventUri().startsWith("{")) {
				attributeMap.put("type", event.getEventUri().substring(1, event.getEventUri().indexOf('}')));
			} else {
				attributeMap.put("type", event.getEventUri());
			}
		}

		for (String propName : event.getAllPropertyNames()) {
			if (!KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY.equals(propName)) {
				jsonEntries.put(propName, event.getPropertyValue(propName));
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		if (pretty)
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_EMPTY);

	try {
		if (event.getPayload() != null) {
			String str = new String(event.getPayload());
			
			Object payloadJS = mapper.readValue(new String(event.getPayload()), Object.class);
			jsonEntries.put(KafkaProperties.RESERVED_EVENT_PROP_PAYLOAD, payloadJS);
		}
	} catch(Exception e) {
		System.out.println("ERROr in json serializer  :"  );
		e.printStackTrace();
	}

		if (skipBEAttributes) {
			removeAttributes(jsonEntries);
		}

		return mapper.writeValueAsString(jsonEntries);
	}

	private void removeAttributes(Map<String, Object> map) {
		map.remove("attributes");
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				removeAttributes((Map) entry.getValue());
			} else if (entry.getValue() instanceof List) {
				for (Object o : ((List) entry.getValue())) {
					if (o instanceof Map) {
						removeAttributes((Map) o);
					}
				}
			}
		}
	}

	private Event deserializeFromJSON(String json, Map<String, Object> properties) throws Exception {
		Event event = new KafkaEvent();

		ObjectMapper mapper = new ObjectMapper();
		Object eventData = mapper.readValue(json, Object.class);
		if (eventData instanceof Map) {
			
			if (((Map) eventData).get("attributes") instanceof Map) {
				Map attributes = (Map) ((Map) eventData).get("attributes");
				String eventUri = (String) attributes.get("type");
				String extId = (String) attributes.get("extId");
				if (isIncludeEventType(properties) && eventUri != null
						&& eventUri.contains(KafkaProperties.ENTITY_NS)) {
					eventUri = eventUri.substring(KafkaProperties.ENTITY_NS.length());
					event.setEventUri(eventUri);
				}
				if (extId != null && !extId.isEmpty()) {
					event.setExtId(extId);
				}
			}
			if (event.getEventUri() != null && !event.getEventUri().equals(getDesignTimeEventUri())) setDesignTimeEvent(event.getEventUri());
						
			for (Entry<String,Object> entry : ((Map<String,Object>)eventData).entrySet()) {
				if (KafkaProperties.RESERVED_EVENT_PROP_PAYLOAD.equals(entry.getKey())) {
					event.setPayload(mapper.writeValueAsString(entry.getValue()).getBytes());//LinkedHashMap
				} else { 
					Object val = entry.getValue();
					if (val instanceof Integer && super.isLongType(entry.getKey())) {
						val = ((Integer) entry.getValue()).longValue();
					}
					event.setProperty(entry.getKey(), val);
				}
			}
		}
		return event;
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

	@Override
	public boolean isJSONPayload() {
		return true;
	}
}
