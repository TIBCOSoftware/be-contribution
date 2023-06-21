package com.tibco.cep.driver.mqtt;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * @author ssinghal
 *
 */
public abstract class BEMqttPublisher {
	
	Properties channelAndDestProps;
	BaseEventSerializer seriallizer;
	final Map<String, Object> serializationProperties;
	String publishTopicName;
	int qos;
	boolean retained;
	Logger logger;
	
	public BEMqttPublisher(Properties channelAndDestProps, BaseEventSerializer seriallizer, Logger logger) {
		
		this.channelAndDestProps = channelAndDestProps;
		this.seriallizer = seriallizer;
		
		this.serializationProperties = new HashMap<String, Object>();
		boolean includeEventType = BEMqttUtil.isIncludeEventTypeWhileSerialize(
				channelAndDestProps.getProperty(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, "ALWAYS"));
		this.serializationProperties.put(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, includeEventType);
		
		this.publishTopicName = channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_PUBLISH_TOPIC_NAME, "");
		this.qos = Integer.parseInt(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_QOS, "1"));
		this.retained = Boolean.parseBoolean(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_RETAIN, "false"));
		this.logger = logger;
	}
	
	
	public void publish(EventWithId event, Map userData) throws Exception{
		byte[] payload = (byte[]) seriallizer.serializeUserEvent(event, serializationProperties);
		publish(publishTopicName, payload, qos, retained);
	}
	
	public void publish(byte[] payload, Map userData) throws Exception{
		publish(publishTopicName, payload, qos, retained);
	}
	
	public void publish(byte[] payload, String topicName, Map userData) throws Exception{
		publish(topicName, payload, qos, retained);
	}
	
	public void publish(EventWithId event, String topicName, Map userData) throws Exception{
		byte[] payload = (byte[]) seriallizer.serializeUserEvent(event, serializationProperties);
		publish(topicName, payload, qos, retained);
	}
	
	protected abstract void publish(String topic, MqttMessage message) throws MqttPersistenceException, MqttException;
	
	protected abstract void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttPersistenceException, MqttException;
	
}
