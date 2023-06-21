package com.tibco.cep.driver.sb;

import com.tibco.xml.data.primitive.ExpandedName;

public interface SBConstants {

	public static final ExpandedName XML_NODE_CONFIG = ExpandedName.makeName("sharedsb");

	public static final ExpandedName CHANNEL_PROPERTY_AUTH_USERNAME = ExpandedName.makeName("UserName");
	public static final ExpandedName CHANNEL_PROPERTY_AUTH_PASSWORD = ExpandedName.makeName("Password");
	public static final ExpandedName CHANNEL_PROPERTY_SERVER_URI = ExpandedName.makeName("StreamBaseServerURI");
	public static final ExpandedName CHANNEL_PROPERTY_TRUST_STORE = ExpandedName.makeName("TrustStore");
	public static final ExpandedName CHANNEL_PROPERTY_KEY_STORE = ExpandedName.makeName("KeyStore");
	public static final ExpandedName CHANNEL_PROPERTY_KEY_STORE_PASSWORD = ExpandedName.makeName("KeyStorePassword");
	public static final ExpandedName CHANNEL_PROPERTY_TRUST_STORE_PASSWORD = ExpandedName.makeName("TrustStorePassword");
	public static final ExpandedName CHANNEL_PROPERTY_KEY_PASSWORD = ExpandedName.makeName("KeyPassword");

	public final static String SB_DRIVER_NAME    			= "StreamBase(Deprecated)";
	public final static String SB_DEST_PROP_STREAM_NAME    	= "StreamName";
	public final static String SB_DEST_PROP_CLIENT_TYPE    	= "ClientType";
	public final static String SB_DEST_PROP_FILTER_PREDICATE= "Predicate";
	public static final String SB_DEST_BUFFERING_ENABLED 	= "EnableBuffering";
	public static final String SB_DEST_BUFFER_SIZE 			= "BufferSize";
	public static final String SB_DEST_BUFFER_INTERVAL 		= "FlushInterval";
	
	public static final String SB_DEST_CLIENT_TYPE_DEQUEUER = "DEQUEUER";
	public static final String SB_DEST_CLIENT_TYPE_ENQUEUER = "ENQUEUER";

	public static final String PAYLOAD_FIELD = "_payload_";

	public static final String SB_RECONNECT_ATTEMPTS_PROP = "com.tibco.sbchannel.reconnect.attempts";
	public static final String SB_RECONNECT_INTERVAL_PROP = "com.tibco.sbchannel.reconnect.interval";
}
