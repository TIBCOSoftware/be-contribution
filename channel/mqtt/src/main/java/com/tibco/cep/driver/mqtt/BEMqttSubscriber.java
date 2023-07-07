package com.tibco.cep.driver.mqtt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * @author ssinghal
 *
 */
public abstract class BEMqttSubscriber implements MqttCallbackExtended{
	
	Properties channelAndDestProps;
	EventProcessor eventProcessor;
	BaseEventSerializer seriallizer;
	final Map<String, Object> serializationProperties;
	String[] subscribeTopicName;
	int qos;
	boolean disconnectOnSuspend;
	BEMqttConnectionOptions beConnOptions;
	Logger logger;
	MqttBECallback beCallback;
	boolean started;
	
	public BEMqttSubscriber(Properties channelAndDestProps, EventProcessor eventProcessor, BaseEventSerializer seriallizer,
			BEMqttConnectionOptions beConnOptions, Logger logger, MqttBECallback callback, String[] requestTopic) {
		
		this.channelAndDestProps = channelAndDestProps;
		this.eventProcessor = eventProcessor;
		this.seriallizer = seriallizer;
		
		this.serializationProperties = new HashMap<String, Object>();
		boolean includeEventType = BEMqttUtil.isIncludeEventTypeWhileDeserialize(
				channelAndDestProps.getProperty(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, "ALWAYS"));
		this.serializationProperties.put(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, includeEventType);
		
		this.disconnectOnSuspend =  Boolean.parseBoolean(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_DISCONNECT_ON_SUSPEND, "false"));
		this.subscribeTopicName = (requestTopic != null) ? requestTopic : (channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_SUBSCRIBE_TOPIC_NAME, "")).split(",");
		this.qos = Integer.parseInt(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_QOS, "1"));
		this.beConnOptions = beConnOptions;
		this.logger = logger;
		this.beCallback = callback;
	}
	
	public void start() throws MqttException{
		started = true;
	}
	
	public void stop() throws MqttException{
		started = false;
	}
	
	protected boolean isStarted(){
		return started;
	}
	public abstract void suspend() throws MqttException;
	public abstract void resume(MqttConnectOptions connOpt) throws MqttSecurityException, MqttException;
	public abstract boolean isConnected();
	public abstract void connect(MqttConnectOptions conOpt) throws MqttSecurityException, MqttException;
	public abstract void reSubscribe() throws MqttException;
	
	protected int[] setQos(String[] subscribeTopicName) {
		int[] qosArray = new int[subscribeTopicName.length];
		Arrays.fill(qosArray, this.qos);
		return qosArray;
	}
	
	public void reconnect(){
		
		if(beConnOptions.getLock().tryLock()){
			if(!isConnected()){
				for(int i=1; i<=beConnOptions.getConnectionAttempts(); i++){
					try {
						connect(beConnOptions.getConnOpt());
						break;
					} catch (MqttException e) {
						logger.log(Level.INFO, "Failed to reconnect. Reconnect count = " + i);
						try {
							Thread.currentThread().sleep(beConnOptions.getAttemptDelay());
						} catch (InterruptedException e1) {}
					}
				}
				beConnOptions.getLock().unlock();
			}
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		logger.log(Level.INFO, cause.getMessage());
		if(beConnOptions.getConnectionAttempts()!=null){ //otherwise paho client's auto reconnect is on.
			reconnect();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try{
			
			if(topic.equals(beConnOptions.getWillTopicName())){
				logger.log(Level.INFO, new String(message.getPayload()) );
				return;
			}
			
			Event e = seriallizer.deserializeUserEvent(message, serializationProperties);
			if(beCallback==null){
				eventProcessor.processEvent(e);
			}else{
				beCallback.messageArrived(topic, e);
			}
		}catch(Exception e){
			logger.log(Level.ERROR, e, "Bad message - " + e.getMessage() );
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		logger.log(Level.INFO , "Connected to " + serverURI);
		try {
			reSubscribe();
		} catch (MqttException e) {
			logger.log(Level.ERROR, e, "Unable to Resubscribe mqtt topics" );
		}
		
	}

}
