package com.tibco.cep.driver.kinesis.serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.custom.channel.framework.CustomEvent;
import com.tibco.cep.driver.kinesis.KinesisProperties;
import com.tibco.cep.kernel.service.logging.Logger;


public class KinesisJsonSerializer extends BaseEventSerializer {
	
    private String stream;
    private String application;
    private String region;
    private String eventprop;
    private static String ISO_8601_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	
	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.stream = destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_STREAM_NAME);
		this.setApplication(destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_APPLICATION_NAME));
		this.eventprop = destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_EVENT_PROPERTY);
		this.setRegion(destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_REGION_NAME));
	}
	
	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {	
		String partitionkey = (String) event.getPropertyValue(eventprop);
		String eventJsonStr = serializeToJSON(event, properties);
		byte[] bytes = eventJsonStr.getBytes();
		PutRecordRequest putRecord = new PutRecordRequest();
		putRecord.setStreamName(this.stream);
		putRecord.setPartitionKey(partitionkey);
		putRecord.setData(ByteBuffer.wrap(bytes));
		
		return putRecord;
	}
	
	private String serializeToJSON(EventWithId event, Map<String, Object> properties) throws IOException {
		Map<String, Object> jsonEntries = new LinkedHashMap<String, Object>();

		Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();
		jsonEntries.put("attributes", attributeMap);
		attributeMap.put("id", String.valueOf(event.getId()));
		if (event.getExtId() != null && !event.getExtId().isEmpty()) {
			attributeMap.put("extId", event.getExtId());
		}
		if (isIncludeEventType(properties) && event.getEventUri() != null) {
			if (event.getEventUri().startsWith("{")) {
				attributeMap.put("type", event.getEventUri().substring(1, event.getEventUri().indexOf('}')));
			} else {
				attributeMap.put("type", event.getEventUri());
			}
		}

		for (String propName : event.getAllPropertyNames()) {
			Object propValue = event.getPropertyValue(propName);

			if (propValue instanceof Calendar) propValue = toStringValue(propValue);
			
			if (propValue != null) {
				jsonEntries.put(propName, propValue);
			}
	
		}

		ObjectMapper mapper = new ObjectMapper();

		if (event.getPayload() != null) {
			try {
				Object payloadJS = mapper.readValue(new String(event.getPayload()), Object.class);
				jsonEntries.put(KinesisProperties.RESERVED_EVENT_PROP_PAYLOAD, payloadJS);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		jsonEntries.put(KinesisProperties.RESERVED_EVENT_CONTEXT, "KinesisEventContext");
		return mapper.writeValueAsString(jsonEntries);
	}
	
	private String toStringValue(Object value) {
		Calendar cal = (Calendar) value;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ISO_8601_FULL_FORMAT);
		ZonedDateTime zdt = ((GregorianCalendar) cal).toZonedDateTime();
		return dtf.format(zdt);
	}

	@Override
	public Event deserializeUserEvent(Object record, Map<String, Object> properties) throws Exception {
		Event event = null;
		String json = new String(((Record) record).getData().array());

		event = deserializeFromJSON(json, properties);

		return event;
	}
	
	public Event deserializeFromJSON(String json, Map<String, Object> properties) throws Exception {
		Event event = new CustomEvent();

		ObjectMapper mapper = new ObjectMapper();
		Object eventData = mapper.readValue(json, Object.class);
		if (eventData instanceof Map) {
			
			if (((Map) eventData).get("attributes") instanceof Map) {
				Map attributes = (Map)((Map) eventData).get("attributes");
				String eventUri = (String)attributes.get("type");
				String extId = (String)attributes.get("extId");
				if (extId != null && !extId.isEmpty()) {
					event.setExtId(extId);
				}
				if (isIncludeEventType(properties) && eventUri != null && eventUri.contains(KinesisProperties.ENTITY_NS)) {
					eventUri = eventUri.substring(KinesisProperties.ENTITY_NS.length());
					event.setEventUri(eventUri);
				}
			}
							
			if (event.getEventUri() != null && !event.getEventUri().equals(event.getEventUri())) setDesignTimeEvent(event.getEventUri());
				
			for (Entry<String, Object> entry : ((Map<String, Object>) eventData).entrySet()) {
				if (KinesisProperties.RESERVED_EVENT_PROP_PAYLOAD.equals(entry.getKey())) {
					event.setPayload(mapper.writeValueAsString(entry.getValue()).getBytes());// LinkedHashMap
				} else { 
					Object val = entry.getValue();
					if (val instanceof Integer && super.isLongType(entry.getKey())) {
						val = ((Integer) entry.getValue()).longValue();
					}
					event.setProperty(entry.getKey(), val);
			    }
			}
		}
		return event;
	}

	/**
	 * By default IncludeEventType is true
	 * @param properties
	 * @return
	 */
	private boolean isIncludeEventType(Map<String, Object> properties) {
		if (properties == null || !properties.containsKey(KinesisProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE)) {
			return true;
		} else {
			return (boolean) properties.get(KinesisProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE);
		}
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public boolean isJSONPayload() {
		return true;
	}
}
