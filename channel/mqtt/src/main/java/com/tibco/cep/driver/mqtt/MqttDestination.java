package com.tibco.cep.driver.mqtt;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventContext;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.runtime.channel.Channel;
import com.tibco.cep.runtime.channel.Channel.State;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/**
 * @author ssinghal
 *
 */
public class MqttDestination extends BaseDestination{
	
	Object mqttClient;
	Object mqttRequestResClient;
	
	String[] brokerUrls;	
	MqttConnectOptions connOpt;
	BEMqttSubscriber beMqttSubscriber;
	private volatile BEMqttPublisher beMqttPublisher;
	private Properties channelAndDestProps;
	boolean asyncClient;
	Map<String, Object> serializationProperties;
	Map<String, Object> serializationPropertiesReqReply;
	
	BEMqttConnectionOptions beConnOptions;
	
	protected static final String MQTT_CLIENT_PERSISTENCE_PATH = System.getProperty("be.engine.channel.mqtt.client.persistence.path", System.getProperty("java.io.tmpdir"));
	protected static final String MQTT_CLIENT_CONNECT_ATTEMPTS = "be.mqtt.connect.attempts";
	protected static final String MQTT_CLIENT_CONNECTION_TIMEOUT = "be.mqtt.connection.timeout";
	protected static final String MQTT_CLIENT_CONNECT_KEEP_ALIVE_INTERVAL = "be.mqtt.connect.keep.alive.interval";
	protected static final String MQTT_CLIENT_WILL_TOPIC = "be.mqtt.will.topic";
	protected static final String MQTT_CLIENT_WILL_MESSAGE = "be.mqtt.will.message";

	@Override
	public void init() throws Exception {
		this.channelAndDestProps = getChannelAndDestProps();
		
		beConnOptions = new BEMqttConnectionOptions();
		
		serializationProperties = new HashMap<String, Object>();
		serializationPropertiesReqReply = new HashMap<String, Object>();
		boolean includeEventType = BEMqttUtil.isIncludeEventTypeWhileSerialize(
				channelAndDestProps.getProperty(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, "ALWAYS"));
		serializationProperties.put(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, includeEventType);
		serializationPropertiesReqReply.put(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, includeEventType);
		
		
		String connectAttempts = ((RuleServiceProvider)getChannel().getRuleServiceProvider()).getProperties().getProperty(MQTT_CLIENT_CONNECT_ATTEMPTS);
		if(connectAttempts==null){
			beConnOptions.setConnectionAttempts(null);
		}else{
			String[] attemptsNDelay = connectAttempts.split(",");
			if(attemptsNDelay.length==0){
				logger.log(Level.WARN, "Invalid property, falling to default behavior to keep retrying.");
				beConnOptions.setConnectionAttempts(new Integer(Integer.MAX_VALUE));
				beConnOptions.setAttemptDelay(500);
			}else {
				beConnOptions.setConnectionAttempts(Integer.parseInt(attemptsNDelay[0]));
				if(beConnOptions.getConnectionAttempts() == -1) beConnOptions.setConnectionAttempts(new Integer(Integer.MAX_VALUE));
				beConnOptions.setAttemptDelay(500);
	            if (attemptsNDelay.length > 1) beConnOptions.setAttemptDelay(Long.parseLong(attemptsNDelay[1]));
			}
		}
		
	}
	
	private Properties getChannelAndDestProps() {
		Properties props = new Properties();
		/*for (Entry<Object, Object> entry : getChannel().getChannelProperties().entrySet()) {
			if (entry.getValue() != null) {
				props.put(entry.getKey(), getChannel().getGlobalVariableValue((String)entry.getValue()).toString());
			}
		}*/
		
		props.put(MqttProperties.MQTT_CHANNEL_PROPERTY_BROKER_URLS, ((MqttChannel)getChannel()).getBrokerUrl());
		props.put(MqttProperties.MQTT_CHANNEL_PROPERTY_USERNAME, ((MqttChannel)getChannel()).getUser());
		props.put(MqttProperties.MQTT_CHANNEL_PROPERTY_PASSWORD, ((MqttChannel)getChannel()).getPassword());
		
		for (Entry<Object, Object> entry : getDestinationProperties().entrySet()) {
			if (entry.getValue() != null) {
				props.put(entry.getKey(), getChannel().getGlobalVariableValue((String)entry.getValue()).toString());
			}
		}
		
		return props;
	}

	@Override
	public void connect() throws Exception {
		
		String brokerUrl = channelAndDestProps.getProperty(MqttProperties.MQTT_CHANNEL_PROPERTY_BROKER_URLS, "");
		brokerUrls = brokerUrl.split(",");
		
		String user = channelAndDestProps.getProperty(MqttProperties.MQTT_CHANNEL_PROPERTY_USERNAME, "");
		String password = channelAndDestProps.getProperty(MqttProperties.MQTT_CHANNEL_PROPERTY_PASSWORD, "");
		
		String clientId = channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_CLIENT_ID, "");
		asyncClient = Boolean.parseBoolean(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_ASYNC_CLIENT, "true"));
		int maxInflight = Integer.parseInt(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_MAX_INFLIGHT, "10"));
		boolean willAndTestament = Boolean.parseBoolean(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_WILL_TESTAMENT, "false"));
		boolean cleanSession =  Boolean.parseBoolean(channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_CLEAN_SESSION, "false"));
		String publishTopicName = channelAndDestProps.getProperty(MqttProperties.MQTT_DEST_PROPERTY_PUBLISH_TOPIC_NAME, "");
		
		
		if(asyncClient){
			mqttClient = new MqttAsyncClient(brokerUrls[0], clientId, new MqttDefaultFilePersistence(MQTT_CLIENT_PERSISTENCE_PATH) );
		}else{
			mqttClient = new MqttClient(brokerUrls[0], clientId, new MqttDefaultFilePersistence(MQTT_CLIENT_PERSISTENCE_PATH));
		}
		connOpt = new MqttConnectOptions();
		
		if(beConnOptions.getConnectionAttempts()==null){
			connOpt.setAutomaticReconnect(true);
		}
		
		connOpt.setConnectionTimeout(Integer.parseInt(((RuleServiceProvider)getChannel().getRuleServiceProvider()).getProperties().getProperty(MQTT_CLIENT_CONNECTION_TIMEOUT, "30")));
		connOpt.setKeepAliveInterval(Integer.parseInt(((RuleServiceProvider)getChannel().getRuleServiceProvider()).getProperties().getProperty(MQTT_CLIENT_CONNECT_KEEP_ALIVE_INTERVAL, "60")));
		
		connOpt.setMaxInflight(maxInflight);
		connOpt.setServerURIs(brokerUrls);
		
		if(user!=null && !user.equals(""))
			connOpt.setUserName(user);
		if(password!=null && !password.equals(""))
			connOpt.setPassword(password.toCharArray());
		
		if(willAndTestament){
			String willTopic = ((RuleServiceProvider)getChannel().getRuleServiceProvider()).getProperties().getProperty(MQTT_CLIENT_WILL_TOPIC, "/will");
			String willMessage = ((RuleServiceProvider)getChannel().getRuleServiceProvider()).getProperties().getProperty(MQTT_CLIENT_WILL_MESSAGE, "Client offline") + clientId ; 
			beConnOptions.setWillTopicName(willTopic);
			connOpt.setWill(willTopic, willMessage.getBytes(), 1, false);
		}
		
		connOpt.setCleanSession(cleanSession);
		
		MqttChannel mqttChannel = (MqttChannel)getChannel();
		if(mqttChannel.isUseSsl()){
			connOpt.setSSLProperties(mqttChannel.getSslProperties());
		}
		
		beConnOptions.setConnOpt(connOpt);
		
		getLogger().log(Level.DEBUG, "Mqtt connection URI - " + this.getUri());
		if(asyncClient){
			((MqttAsyncClient)mqttClient).connect(connOpt).waitForCompletion();
		}
		else{
			((MqttClient)mqttClient).connect(connOpt);
		}
	}
	
	@Override
	public void bind(EventProcessor eventProcessor) throws Exception {
		if(beMqttSubscriber==null){
			if(asyncClient){
				beMqttSubscriber = new BEMqttAsyncSubscriber((MqttAsyncClient)mqttClient, channelAndDestProps, eventProcessor, this.getSerializer(), beConnOptions, getLogger(), null, null);
			}else{
				beMqttSubscriber = new BEMqttSyncSubscriber((MqttClient)mqttClient, channelAndDestProps, eventProcessor, this.getSerializer(), beConnOptions, getLogger(), null, null);
			}
		}
	}

	@Override
	public void start() throws Exception {
		if(beMqttSubscriber!=null && beMqttSubscriber.isStarted()==false){
			beMqttSubscriber.start();
			//suspend(); ///this has been done as mode has been lost in custom channel api impl, and default mode is always suspend.
		}
		
	}

	@Override
	public void close() throws Exception {
		getLogger().log(Level.INFO, "Closing Mqtt destination - " + this.getUri());
		
		if(mqttClient!=null){
			if(asyncClient){
				if(((MqttAsyncClient)mqttClient).isConnected()){
					((MqttAsyncClient)mqttClient).disconnect();
					((MqttAsyncClient)mqttClient).close();
				}
			}
			else{
				if(((MqttClient)mqttClient).isConnected()){
					((MqttClient)mqttClient).disconnect();
					((MqttClient)mqttClient).close();
				}
			}
			getLogger().log(Level.INFO, "Mqtt destination closed - " + this.getUri());
		}
	}

	@Override
	public void send(EventWithId event, Map userData) throws Exception {
		
		State channelState = ((MqttChannel)getChannel()).getState();
		
		if ((channelState == Channel.State.CONNECTED)
                || (channelState == Channel.State.STARTED)) {
			if(userData!=null){
				if(userData.containsKey(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD)){
					getPublisher().publish(event, (String)userData.get(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD), userData);
				}else if(userData.containsKey("name")){
					getPublisher().publish(event, (String)userData.get("name"), userData);
				}else{
					throw new Exception("Publish topic name not properly configured");
				}
				
			}else{
				getPublisher().publish(event, userData);
			}
		}else{
			this.getLogger().log(Level.ERROR, "Channel in an invalid state: %s", channelState);
			throw new Exception("Channel in an invalid state");
		}
	}
	
	@Override
	public void suspend() {
		synchronized (this) {
			try{
				if(beMqttSubscriber!=null && beMqttSubscriber.isConnected()){
					beMqttSubscriber.suspend();
					this.suspended = true;
					this.getLogger().log(Level.INFO, "Destination Suspended : " + getUri());
				}
			}catch(MqttException mex){
				this.getLogger().log(Level.ERROR, mex, "Unable to suspend Destination : " + getUri() );
			}
		}
	}
	
	@Override
	public void resume() {
		synchronized (this) {
			try{
				if(beMqttSubscriber!=null && this.suspended==true){
					beMqttSubscriber.resume(connOpt);
					this.suspended = false;
					this.getLogger().log(Level.INFO, "Destination Resumed : " + getUri());
				}
			}catch(MqttException mex){
				this.getLogger().log(Level.ERROR, mex, "Unable to resume Destination : " + getUri() );
			}
		}
	}

	@Override
	public Event requestEvent(Event outevent, String responseEventURI, BaseEventSerializer serializer, long timeout, Map userData) throws Exception {
		
		String requestTopic = UUID.randomUUID().toString(); //this would act as temporary topic and also client id
		final Event[] receivedEvt= new Event[1];
		
		Object mqttRequestResClient = initReqResConnection(requestTopic);
		
		Object lock = new Object();
		MqttBECallback callback = new MqttBECallback() {
			
			@Override
			public void messageArrived(String arg0, Event event) throws Exception {
				
				synchronized (lock) {
					receivedEvt[0] = event;
					lock.notify();
				}
			}
		};
		
		if(asyncClient){
			beMqttSubscriber = new BEMqttAsyncSubscriber((MqttAsyncClient)mqttRequestResClient, channelAndDestProps, eventProcessor, 
					this.getSerializer(), beConnOptions, getLogger(), callback, new String[]{requestTopic});
		}else{
			beMqttSubscriber = new BEMqttSyncSubscriber((MqttClient)mqttRequestResClient, channelAndDestProps, eventProcessor, 
					this.getSerializer(), beConnOptions, getLogger(), callback, new String[]{requestTopic});
		}
		beMqttSubscriber.start();
		
		serializationPropertiesReqReply.put(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD, requestTopic);
		byte[] payload = (byte[]) this.getSerializer().serializeUserEvent((EventWithId)outevent, serializationPropertiesReqReply);
		getPublisher().publish(payload, userData);
		
		synchronized (lock) {
			if(timeout == -1){
				lock.wait();
			}else{
				lock.wait(timeout + 50000);
			}
		}
		
		if(asyncClient){
			((MqttAsyncClient)mqttRequestResClient).unsubscribe(requestTopic);
			((MqttAsyncClient)mqttRequestResClient).disconnect();
		}else{
			((MqttClient)mqttRequestResClient).unsubscribe(requestTopic);
			((MqttClient)mqttRequestResClient).disconnect();
		}
		
		return receivedEvt[0];
		
	}
	
	private Object initReqResConnection(String clientId) throws Exception{
		
		Object mqttRequestResClient = null;
		if(asyncClient){
			mqttRequestResClient = new MqttAsyncClient(brokerUrls[0], clientId, new MqttDefaultFilePersistence(MQTT_CLIENT_PERSISTENCE_PATH));
			IMqttToken token = ((MqttAsyncClient)mqttRequestResClient).connect(connOpt);
			token.waitForCompletion();
		}else{
			mqttRequestResClient = new MqttClient(brokerUrls[0], clientId, new MqttDefaultFilePersistence(MQTT_CLIENT_PERSISTENCE_PATH));
			((MqttClient)mqttRequestResClient).connect(connOpt);
		}
		return mqttRequestResClient;
	}
	

	@Override
	public EventContext getEventContext(Event event) {
		return new MqttMessageContext(event, this, getChannel());
	}
	
	private BEMqttPublisher getPublisher(){
		if (beMqttPublisher == null) {
			synchronized (this) {
				if (beMqttPublisher == null) {
					getLogger().log(Level.DEBUG, "Creating MqttPublisher for destination - " + this.getUri());
					if(asyncClient){
						beMqttPublisher = new BEMqttAsyncPublisher((MqttAsyncClient)mqttClient, channelAndDestProps, this.getSerializer(), getLogger());
					}else{
						beMqttPublisher = new BEMqttSyncPublisher((MqttClient)mqttClient, channelAndDestProps, this.getSerializer(), getLogger());
					}
					
				}
			}
		}
		return beMqttPublisher;
	}
	

}
