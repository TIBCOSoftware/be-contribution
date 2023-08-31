package com.tibco.cep.driver.mqtt.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.mqtt.MqttEvent;
import com.tibco.cep.driver.mqtt.MqttProperties;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * @author ssinghal
 *
 */
public class MqttMapSerializer extends MqttBaseSerializer{
	
	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		Event event = new MqttEvent();
		boolean includeEventType = isIncludeEventType(properties);
		
		MqttMessage mqttMessage = (MqttMessage) message;
		Map<String, Object> valueMap = (Map)deserialize(mqttMessage.getPayload());
		
		for (Entry<String, Object> entry : valueMap.entrySet()) {
			event.setProperty(entry.getKey(), entry.getValue());
		}
		
		if (includeEventType && valueMap.containsKey(MqttProperties.BE_NAMESPACE)) {
			String eventUri = (String)valueMap.get(MqttProperties.BE_NAMESPACE);
			if (eventUri != null && eventUri.contains(MqttProperties.ENTITY_NS)) {
				eventUri = eventUri.substring(eventUri.indexOf(MqttProperties.ENTITY_NS) + MqttProperties.ENTITY_NS.length(), eventUri.lastIndexOf('}'));
			}
			event.setEventUri(eventUri);
		}
		//setting payload
		event.setPayload(valueMap.get(MqttProperties.RESERVED_EVENT_PROP_PAYLOAD)!=null?(byte[])valueMap.get(MqttProperties.RESERVED_EVENT_PROP_PAYLOAD):new byte[0]);
		//setting extID
		event.setExtId(valueMap.get(MqttProperties.RESERVED_EVENT_PROP_EXT_ID)!=null?(String) valueMap.get(MqttProperties.RESERVED_EVENT_PROP_EXT_ID):null);
		
		((MqttEvent)event).setRequestTopic(valueMap.get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD)!=null?(String) valueMap.get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD):null);
		
		//if event has below two properties, then respective values would be set.
		event.setProperty(MqttProperties.MQTT_DUPLICATE_MESSAGE_KEYWORD, mqttMessage.isDuplicate());
		event.setProperty(MqttProperties.MQTT_RETAINED_MESSAGE_KEYWORD, mqttMessage.isRetained());
		
		//event.setDestinationURI(destinationURI);
		
		return event;
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		
		Map<String, Object> messageMap = new HashMap<String, Object>();
		
		messageMap.put(MqttProperties.RESERVED_EVENT_PROP_EXT_ID, event.getExtId());
		
		boolean includeEventType = isIncludeEventType(properties);
		if (includeEventType && event.getEventUri() != null) {
			messageMap.put(MqttProperties.BE_NAMESPACE, event.getEventUri());
			messageMap.put(MqttProperties.BE_NAME, event.getEventUri().substring(event.getEventUri().lastIndexOf('}') + 1, event.getEventUri().length()));
		}
		
		messageMap.put(MqttProperties.RESERVED_EVENT_PROP_PAYLOAD, event.getPayload());
		
		for (String prop : event.getAllPropertyNames()) {
			messageMap.put(prop, event.getPropertyValue(prop));
		}
		
		includeRequestTopic(messageMap, properties);
		
		return serialize(messageMap);
	}

	
	private void includeRequestTopic(Map<String, Object> messageMap, Map<String, Object> properties) {
		if(properties!=null && properties.containsKey(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD)){
			messageMap.put(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD, properties.get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD));
		}
	}

	/*private byte[] serializeMap(Map map) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		try{
			oos.writeObject(map);
		}
		finally{
			oos.close();
			return baos.toByteArray();
		}
	}*/
	
	/*private Map deserializeMap(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (Map)ois.readObject();
	}*/

}
