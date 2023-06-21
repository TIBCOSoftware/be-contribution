package com.tibco.cep.driver.mqtt;

import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * @author ssinghal
 *
 */
public class BEMqttSyncSubscriber extends BEMqttSubscriber{
	
	MqttClient mqttClient;
		
	public BEMqttSyncSubscriber(MqttClient mqttClient, Properties channelAndDestProps, EventProcessor eventProcessor, BaseEventSerializer seriallizer,
			BEMqttConnectionOptions beConnOptions, Logger logger, MqttBECallback callback, String[] requestTopic) {
		super(channelAndDestProps, eventProcessor, seriallizer, beConnOptions, logger, callback, requestTopic);
		this.mqttClient = mqttClient;
	}
	
	
	public void start() throws MqttException{
		mqttClient.subscribe(subscribeTopicName, setQos(subscribeTopicName));
		mqttClient.setCallback(this);
		super.start();
	}
	
	public void suspend() throws MqttException{
		if(disconnectOnSuspend){
			mqttClient.disconnect(); //messages retained 
		}else{
			mqttClient.unsubscribe(subscribeTopicName); //all messages lost
		}
		super.stop();
	}
	
	public void resume(MqttConnectOptions connOpt) throws MqttSecurityException, MqttException{
		if(disconnectOnSuspend){
			mqttClient.connect(connOpt);
		}else{
			mqttClient.subscribe(subscribeTopicName, setQos(subscribeTopicName));
		}
	}
	
	@Override
	public boolean isConnected() {
		return this.mqttClient.isConnected();
	}

	@Override
	public void connect(MqttConnectOptions conOpt) throws MqttSecurityException, MqttException {
		this.mqttClient.connect(conOpt);
	}


	@Override
	public void reSubscribe() throws MqttException {
		mqttClient.subscribe(subscribeTopicName, setQos(subscribeTopicName));
	}

}
