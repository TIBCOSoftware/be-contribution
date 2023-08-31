package com.tibco.cep.driver.mqtt.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.mqtt.MqttEvent;
import com.tibco.cep.driver.mqtt.MqttProperties;

/**
 * @author ssinghal
 *
 */
public class MqttJsonSerializer extends MqttBaseSerializer{

	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		
		Event event = null;
		MqttMessage mqttMessage = (MqttMessage) message;
		String eventJsonStr = (String) deserialize(mqttMessage.getPayload());
		
		event = deserializeFromJSON(eventJsonStr, mqttMessage, properties);
		
		return event;
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		String eventJsonStr = serializeToJSON(event, properties, false);
		return serialize(eventJsonStr);
	}
	
	private Event deserializeFromJSON(String json, MqttMessage mqttMessage, Map<String, Object> properties) throws Exception {
		Event event = new MqttEvent();
		ObjectMapper mapper = new ObjectMapper();
		Object eventData = mapper.readValue(json, Object.class);
		
		if (eventData instanceof Map) {

			if (((Map) eventData).get("attributes") instanceof Map) {
				Map attributes = (Map)((Map) eventData).get("attributes");
				String eventUri = (String)attributes.get("type");
				String extId = (String)attributes.get("extId");
				if (extId != null && !extId.isEmpty()) {
					event.setExtId(extId);
				}
				if (isIncludeEventType(properties) && eventUri != null && eventUri.contains(MqttProperties.ENTITY_NS)) {
					eventUri = eventUri.substring(MqttProperties.ENTITY_NS.length());
					event.setEventUri(eventUri);
				}
			}
			
			if (event.getEventUri() != null && !event.getEventUri().equals(getDesignTimeEventUri())) setDesignTimeEvent(event.getEventUri());
						
			for (Entry<String, Object> entry : ((Map<String, Object>)eventData).entrySet()) {
				if (MqttProperties.RESERVED_EVENT_PROP_PAYLOAD.equals(entry.getKey())) {
					event.setPayload(mapper.writeValueAsString(entry.getValue()).getBytes());//LinkedHashMap
				} else { 
					Object val = entry.getValue();
					if (val instanceof Integer && super.isLongType(entry.getKey())) {
						val = ((Integer) entry.getValue()).longValue();
					}
					event.setProperty(entry.getKey(), val);
				}
			}

			
			((MqttEvent)event).setRequestTopic(((Map<String, Object>)eventData).get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD)!=null?
					(String) ((Map<String, Object>)eventData).get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD):null);
			
			//if event has below two properties, then respective values would be set.
			event.setProperty(MqttProperties.MQTT_DUPLICATE_MESSAGE_KEYWORD, mqttMessage.isDuplicate());
			event.setProperty(MqttProperties.MQTT_RETAINED_MESSAGE_KEYWORD, mqttMessage.isRetained());
		}
		
		return event;
	}
	
	private String serializeToJSON(EventWithId event, Map<String, Object> properties, boolean pretty) throws IOException {
		
		Map<String, Object> jsonEntries = new HashMap<String, Object>();
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
		
		for (String prop : event.getAllPropertyNames()) {
			jsonEntries.put(prop, event.getPropertyValue(prop));
		}
		
		ObjectMapper mapper = new ObjectMapper();
		if (pretty) mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		if (event.getPayload() != null) {
			Object payloadJS = mapper.readValue(new String(event.getPayload()), Object.class);
			jsonEntries.put(MqttProperties.RESERVED_EVENT_PROP_PAYLOAD, payloadJS);
		}
		
		includeRequestTopic(jsonEntries, properties);
		
		return mapper.writeValueAsString(jsonEntries);
	}
	
	private void includeRequestTopic(Map<String, Object> messageMap, Map<String, Object> properties) {
		if(properties!=null && properties.containsKey(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD)){
			messageMap.put(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD, properties.get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD));
		}
	}
}
