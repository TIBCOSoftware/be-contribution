package com.tibco.cep.driver.kinesis;

import com.tibco.cep.runtime.channel.ChannelProperties;

public class KinesisProperties implements ChannelProperties {
	
	public static final String KEY_CHANNEL_ACCESS_KEY = "access_key";
	public static final String KEY_CHANNEL_SECRET_KEY = "secret_key";
	
	public static final String KEY_DESTINATION_MAX_RECORDS = "max.records";
	public static final String KEY_DESTINATION_STREAM_NAME = "stream.name";
	public static final String KEY_DESTINATION_APPLICATION_NAME = "application.name";
	public static final String KEY_DESTINATION_REGION_NAME = "region.name";
	public static final String KEY_DESTINATION_EVENT_PROPERTY = "EventProperty";

	public static final String RESERVED_EVENT_CONTEXT = "context";
	
	public static final String INTERNAL_PROP_KEY_CHANNEL_URI = "channel.uri";
	public static final String INTERNAL_PROP_KEY_DESTINATION_URI = "destination.uri";
	
	public static final String PROPERTY_KEY_KINESIS_CLIENT_PROPERTY_PREFIX = "be.channel.kinesis";
	
}
