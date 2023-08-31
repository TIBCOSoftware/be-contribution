package com.tibco.cep.driver.mqtt;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
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
public class BEMqttAsyncPublisher extends BEMqttPublisher{
	
	MqttAsyncClient mqttAsyncClient;
	
	public BEMqttAsyncPublisher(MqttAsyncClient mqttClient, Properties channelAndDestProps, BaseEventSerializer seriallizer, Logger logger) {
		
		super(channelAndDestProps, seriallizer, logger);
		this.mqttAsyncClient = mqttClient;
	}
	
	public void publish(String topic, MqttMessage message) throws MqttPersistenceException, MqttException{
		mqttAsyncClient.publish(topic, message);
	}
	
	public void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttPersistenceException, MqttException{
		mqttAsyncClient.publish(topic, payload, qos, retained);
	}
	
}
