package com.tibco.cep.driver.mqtt;

import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
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
public class BEMqttAsyncSubscriber extends BEMqttSubscriber{
	
	MqttAsyncClient mqttAsyncClient;
		
	public BEMqttAsyncSubscriber(MqttAsyncClient mqttClient, Properties channelAndDestProps, EventProcessor eventProcessor, BaseEventSerializer seriallizer,
			BEMqttConnectionOptions beConnOptions, Logger logger, MqttBECallback callback, String[] requestTopic) {
		super(channelAndDestProps, eventProcessor, seriallizer, beConnOptions, logger, callback, requestTopic);
		this.mqttAsyncClient = mqttClient;
	}
	
	
	public void start() throws MqttException{
		mqttAsyncClient.subscribe(subscribeTopicName, setQos(subscribeTopicName));
		mqttAsyncClient.setCallback(this);
		super.start();
	}
	
	public void suspend() throws MqttException{
		if(disconnectOnSuspend){
			mqttAsyncClient.disconnect();
		}else{
			mqttAsyncClient.unsubscribe(subscribeTopicName);
		}
		super.stop();
	}
	
	public void resume(MqttConnectOptions connOpt) throws MqttSecurityException, MqttException{
		if(disconnectOnSuspend){
			mqttAsyncClient.connect(connOpt);
		}else{
			mqttAsyncClient.subscribe(subscribeTopicName, setQos(subscribeTopicName));
		}
	}

	@Override
	public boolean isConnected() {
		return this.mqttAsyncClient.isConnected();
	}

	@Override
	public void connect(MqttConnectOptions conOpt) throws MqttSecurityException, MqttException {
		this.mqttAsyncClient.connect(conOpt).waitForCompletion();
	}


	@Override
	public void reSubscribe() throws MqttException {
		mqttAsyncClient.subscribe(subscribeTopicName, setQos(subscribeTopicName));
	}
	
}
