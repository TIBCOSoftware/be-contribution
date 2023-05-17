package com.tibco.cep.driver.kinesis.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.Record;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;

import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.custom.channel.framework.CustomEvent;
import com.tibco.be.model.rdf.primitives.RDFBooleanTerm;
import com.tibco.be.model.rdf.primitives.RDFDateTimeTerm;
import com.tibco.be.model.rdf.primitives.RDFDoubleTerm;
import com.tibco.be.model.rdf.primitives.RDFIntegerTerm;
import com.tibco.be.model.rdf.primitives.RDFLongTerm;
import com.tibco.be.model.rdf.primitives.RDFPrimitiveTerm;
import com.tibco.be.model.rdf.primitives.RDFStringTerm;
import com.tibco.be.util.XiSupport;
import com.tibco.cep.driver.kinesis.KinesisProperties;
import com.tibco.cep.kernel.model.entity.Entity;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.TypeManager;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.xml.XiNodeUtilities;
import com.tibco.xml.data.primitive.ExpandedName;
import com.tibco.xml.data.primitive.values.XsString;
import com.tibco.xml.datamodel.XiNode;
import com.tibco.xml.datamodel.helpers.XiSerializer;
import com.tibco.xml.datamodel.nodes.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class KinesisXmlSerializer extends BaseEventSerializer {
	
    public String stream;
    public String application;
    public String region;
    public String eventprop;
    public String eventUri;
    private static final String EVENT_TAG_NAME = "Event";
    private static String ISO_8601_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	

	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.stream = destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_STREAM_NAME);
		this.application = destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_APPLICATION_NAME);
		this.eventprop = destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_EVENT_PROPERTY);
		this.region = destinationProperties.getProperty(KinesisProperties.KEY_DESTINATION_REGION_NAME);
		this.eventUri = destinationProperties.getProperty(KinesisProperties.DEFAULT_EVENT_URI);		
	}
	
	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		String partitionkey = (String) event.getPropertyValue(eventprop);
		XiNode node = serializeToXiNode(event, properties);
        String eventXmlStr = XiSerializer.serialize(node);
        byte[] bytes = eventXmlStr.getBytes();
		PutRecordRequest putRecord = new PutRecordRequest();
		putRecord.setStreamName(this.stream);
		putRecord.setPartitionKey(partitionkey);
		putRecord.setData(ByteBuffer.wrap(bytes));
		
		return putRecord;
	}
	
	private XiNode serializeToXiNode(EventWithId event, Map<String, Object> properties) throws SAXException, IOException {
		ExpandedName rootNm = ExpandedName.makeName(EVENT_TAG_NAME);
		XiNode node = XiSupport.getXiFactory().createElement(rootNm);
		if (isIncludeEventType(properties) && event.getEventUri() != null) {
			String eventNs;
			String name;
			if (event.getEventUri().startsWith("{")) {
				eventNs = event.getEventUri().substring(1, event.getEventUri().indexOf('}'));
				name = event.getEventUri().substring(event.getEventUri().lastIndexOf('}') + 1,event.getEventUri().length());
			} else {
				eventNs = event.getEventUri();
				name = eventNs.substring(eventNs.lastIndexOf("/") + 1, eventNs.length());
			}

			node.appendElement(ExpandedName.makeName(KinesisProperties.BE_NAMESPACE)).appendText(eventNs);
			node.appendElement(ExpandedName.makeName(KinesisProperties.BE_NAME)).appendText(name);
		}

		node.setAttributeStringValue(ExpandedName.makeName(Entity.ATTRIBUTE_ID), String.valueOf(event.getId()));
		if (event.getExtId() != null) {
			node.setAttributeStringValue(ExpandedName.makeName(Entity.ATTRIBUTE_EXTID), event.getExtId());
		}
		for (String propName : event.getAllPropertyNames()) {
			node.appendElement(ExpandedName.makeName(propName)).appendText(new XsString(toStringValue(event.getPropertyValue(propName))));
		}

		if (event.getPayload() != null) {
			XiNode payload = XiSupport.getParser().parse(new InputSource(new ByteArrayInputStream(event.getPayload())));

			XiNodeUtilities.cleanTextNodes(payload);
			node.appendElement(ExpandedName.makeName(KinesisProperties.RESERVED_EVENT_PROP_PAYLOAD)).appendChild(payload.getFirstChild());
		}

		return node;
	}

	@Override
	public Event deserializeUserEvent(Object record, Map<String, Object> properties) throws Exception {
		Event event = null;
		if (record instanceof Record) {
			String val = new String(((Record) record).getData().array());
			
			event = deserializeFromXML(val, properties);
		} else {
			return null;
		}
		
		return event;
	}
	
	/**
	 * Returns Custom Event for the passed XML.
	 * @param xml
	 * @param properties
	 * @return
	 * @throws Exception 
	 */
	private Event deserializeFromXML(String xml, Map<String, Object> properties) throws Exception {
		Event event = new CustomEvent();

		XiNode root = XiSupport.getParser().parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
		Iterator content = root.getChildren();

		while (content.hasNext()) {
			Object eventContent = content.next();

			com.tibco.cep.designtime.model.event.Event designTimeEvent = null;
			String e1 = eventUri.substring(TypeManager.DEFAULT_BE_NAMESPACE_URI.length());
			com.tibco.cep.designtime.model.Entity entity = getEntity(eventUri.substring(TypeManager.DEFAULT_BE_NAMESPACE_URI.length()));

			if (entity != null && entity instanceof com.tibco.cep.designtime.model.event.Event) {
				designTimeEvent = (com.tibco.cep.designtime.model.event.Event) entity;
			}

			Iterator eventProps = ((Element)eventContent).getChildren();
			event.setExtId(((Element)eventContent).getAttributeStringValue(ExpandedName.makeName(Entity.ATTRIBUTE_EXTID)));

			String ns = null;
			String nm = null;
			while (eventProps.hasNext()) {
				Object p = eventProps.next();
				if (!(p instanceof Element)) {
					continue;
				}
				Element prop = (Element) p;
				if (KinesisProperties.RESERVED_EVENT_PROP_PAYLOAD.equals(prop.getName().getLocalName())) {
					XiNodeUtilities.cleanTextNodes(prop);
					event.setPayload(XiSerializer.serialize(prop.getFirstChild()).getBytes());
				} else if (KinesisProperties.BE_NAMESPACE.equals(prop.getName().getLocalName())) {
					ns = prop.getStringValue();
				} else if (KinesisProperties.BE_NAME.equals(prop.getName().getLocalName())) {
					nm = prop.getStringValue();
				} else {
					event.setProperty(prop.getName().getLocalName(), getValueByType(designTimeEvent.getPropertyType(prop.getName().localName), prop.getStringValue()));
				}
			}

			if (isIncludeEventType(properties) && ns != null) {
				if (ns.contains(KinesisProperties.ENTITY_NS)) {
					ns = ns.substring(KinesisProperties.ENTITY_NS.length());
				}
				event.setEventUri(ns);
			}
			break;
		}

		return event;

	}
	
	private com.tibco.cep.designtime.model.Entity getEntity(String entityURI) throws ClassNotFoundException {
		RuleServiceProviderManager RSPM = RuleServiceProviderManager.getInstance();
		return RSPM.getDefaultProvider().getProject().getOntology().getEntity(entityURI);
	}
	
	private Object getValueByType(RDFPrimitiveTerm rdfType, String value) throws Exception {
		Object convertedValue = null;

		if (rdfType instanceof RDFStringTerm)
			convertedValue = value;
		else if (rdfType instanceof RDFIntegerTerm)
			convertedValue = Integer.parseInt(value);
		else if (rdfType instanceof RDFLongTerm)
			convertedValue = Long.parseLong(value);
		else if (rdfType instanceof RDFDoubleTerm)
			convertedValue = Double.parseDouble(value);
		else if (rdfType instanceof RDFBooleanTerm)
			convertedValue = Boolean.parseBoolean(value);
		else if (rdfType instanceof RDFDateTimeTerm) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ISO_8601_FULL_FORMAT);
			ZonedDateTime zdt = ZonedDateTime.parse(value, dtf);
			convertedValue = GregorianCalendar.from(zdt);
		}

		return convertedValue;
	}
	
	private String toStringValue(Object value) {
		if (value instanceof String)
			return (String) value;
		else if (value instanceof Calendar) {
			Calendar cal = (Calendar) value;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ISO_8601_FULL_FORMAT);
			ZonedDateTime zdt = ((GregorianCalendar) cal).toZonedDateTime();
			return dtf.format(zdt);
		} else
			return String.valueOf(value);
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
}
