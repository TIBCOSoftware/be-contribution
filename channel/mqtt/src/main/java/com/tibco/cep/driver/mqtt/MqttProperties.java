package com.tibco.cep.driver.mqtt;

import com.tibco.cep.runtime.channel.ChannelProperties;
import com.tibco.xml.data.primitive.ExpandedName;

/**
 * @author ssinghal
 *
 */
public class MqttProperties implements ChannelProperties {
	public static final String MQTT_CHANNEL_NAME 							= "MQTT";
	
	public static final String MQTT_CHANNEL_PROPERTY_BROKER_URLS 			= "mqtt.broker.urls";
	public static final String MQTT_CHANNEL_PROPERTY_BROKER_URLS_LABEL 		= "MQTT Broker URLs";
	public static final String MQTT_CHANNEL_PROPERTY_USERNAME 				= "UserName";
	public static final String MQTT_CHANNEL_PROPERTY_USERNAME_LABEL			= "User Name";
	public static final String MQTT_CHANNEL_PROPERTY_PASSWORD				= "Password";
	public static final String MQTT_CHANNEL_PROPERTY_DESCRIPTION 			= "Description";
	public static final String MQTT_CHANNEL_PROPERTY_USESSL 				= "useSsl";
	public static final String MQTT_CHANNEL_PROPERTY_SSL_NODE 				= "ssl";
	public static final String MQTT_CHANNEL_PROPERTY_TRUSTSTORE_FOLDER 		= "cert";
	public static final String MQTT_CHANNEL_PROPERTY_TRUSTSTORE_PASSWORD 	= "trustStorePassword";
	public static final String MQTT_CHANNEL_PROPERTY_KEYSTORE_IDENTITY 		= "identity";
	public static final String MQTT_CHANNEL_PROPERTY_REQUIRES_CLIENT_AUTH 	= "requiresClientAuthentication";
	
	public static final String MQTT_DEST_PROPERTY_PUBLISH_TOPIC_NAME		= "publish.topic.name";
	public static final String MQTT_DEST_PROPERTY_SUBSCRIBE_TOPIC_NAME		= "subscribe.topic.name";
	public static final String MQTT_DEST_PROPERTY_CLIENT_ID 				= "clientId";
	public static final String MQTT_DEST_PROPERTY_ASYNC_CLIENT 				= "asynsClient";
	public static final String MQTT_DEST_PROPERTY_AUTO_RECONNECT			= "autoReconnect";
	public static final String MQTT_DEST_PROPERTY_CONN_TIMEOUT 				= "connection.timeout.sec";
	public static final String MQTT_DEST_PROPERTY_KEEP_ALIVE_INTERVAL 		= "keep.alive.interval.sec";
	public static final String MQTT_DEST_PROPERTY_MAX_INFLIGHT 				= "maxInflight";
	public static final String MQTT_DEST_PROPERTY_WILL_TESTAMENT 			= "willAndTestament";
	public static final String MQTT_DEST_PROPERTY_CLEAN_SESSION 			= "cleanSession";
	public static final String MQTT_DEST_PROPERTY_QOS 						= "qos";
	public static final String MQTT_DEST_PROPERTY_RETAIN					= "retain";
	public static final String MQTT_DEST_PROPERTY_DISCONNECT_ON_SUSPEND		= "disconnectOnSuspend";
	
	public static final String MQTT_DUPLICATE_MESSAGE_KEYWORD				= "mqttDuplicateMessage";
	public static final String MQTT_RETAINED_MESSAGE_KEYWORD				= "mqttRetainedMessage";
	public static final String MQTT_REQUEST_TOPIC_KEYWORD					= "requestTopicName";
	
	public final static ExpandedName SHARED_CONFIG_CHANNEL_PROPERTY_SSL     = ExpandedName.makeName(AEMETA_SERVICES_2002_NS, "ssl");	
}
