package com.tibco.cep.driver.kafka.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KeyValue;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.util.XiSupport;
import com.tibco.cep.driver.kafka.KafkaEvent;
import com.tibco.cep.driver.kafka.KafkaProperties;
import com.tibco.cep.kernel.model.entity.Entity;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.jxpath.objects.XDateTime;
import com.tibco.xml.XiNodeUtilities;
import com.tibco.xml.data.primitive.ExpandedName;
import com.tibco.xml.datamodel.XiNode;
import com.tibco.xml.datamodel.helpers.XiSerializer;
import com.tibco.xml.datamodel.nodes.Element;

/**
 * 
 * @author moshaikh
 */
public class KafkaXmlSerializer extends BaseEventSerializer implements KafkaSerializer {

	private static final String PAYLOAD_TAG_NAME = "payload";
	private static final String EVENT_TAG_NAME = "Event";
	private String topic;

	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		this.topic = destinationProperties.getProperty(KafkaProperties.KEY_DESTINATION_TOPIC_NAME);
	}

	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		Event event = null;
		String val = null;
		if (message instanceof ConsumerRecord) {
			ConsumerRecord<String, String> kafkaMessage = (ConsumerRecord<String, String>) message;
			val = new String(kafkaMessage.value());
		} else if (message instanceof KeyValue<?, ?>) {
			// Reuse this serializer for Kafka Streams
			KeyValue kvPair = (KeyValue) message;
			if (kvPair.value != null && kvPair.value instanceof String)
				val = kvPair.value.toString();
		}

		if (val == null) {
			return null;
		}

		event = deserializeFromXML(val, properties);

		return event;
	}

	/**
	 * Returns Custom Event for the passed XML.
	 * 
	 * @param xml
	 * @param properties
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	private Event deserializeFromXML(String xml, Map<String, Object> properties) throws IOException, SAXException {
		Event event = new KafkaEvent();

		XiNode root = XiSupport.getParser().parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
//		XiNodeUtilities.cleanTextNodes(root);
		Iterator content = root.getChildren();

		while (content.hasNext()) {
			Object eventContent = content.next();
			/*
			 * if (!EVENT_TAG_NAME.equals(((Element)eventContent).getName().getLocalName()))
			 * { continue; }
			 */
			Iterator eventProps = ((Element) eventContent).getChildren();
			event.setExtId(
					((Element) eventContent).getAttributeStringValue(ExpandedName.makeName(Entity.ATTRIBUTE_EXTID)));

			String ns = ((Element) eventContent).getName().namespaceURI;
			String nm = ((Element) eventContent).getName().localName;
			while (eventProps.hasNext()) {
				Object p = eventProps.next();
				if (!(p instanceof Element)) {
					continue;
				}
				Element prop = (Element) p;
				if (PAYLOAD_TAG_NAME.equals(prop.getName().getLocalName())) {
					XiNodeUtilities.cleanTextNodes(prop);
					event.setPayload(XiSerializer.serialize(prop.getFirstChild()).getBytes());
				} else {
					event.setProperty(prop.getName().getLocalName(), prop.getStringValue());
				}
			}

			if (isIncludeEventType(properties) && ns != null) {
				event.setEventUri(ns);
			}

			break;
		}

		return event;
	}

	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		XiNode node = serializeToXiNode(event, properties);
		String eventXmlStr = XiSerializer.serialize(node);
		

		Object key = event.getPropertyValue(KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY);

		ProducerRecord kafkaMessage = null;

		String replyTopic = null;
		if (null != properties && properties.containsKey(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC)) {
			replyTopic = (String) properties.get(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC);
		}

		if (key != null) {
			kafkaMessage = new ProducerRecord(null == replyTopic ? topic : replyTopic, String.valueOf(key), eventXmlStr == null ? "" : eventXmlStr);
		} else {
			kafkaMessage = new ProducerRecord(null == replyTopic ? topic : replyTopic, event.getExtId(), eventXmlStr == null ? "" : eventXmlStr);
		}
		return kafkaMessage;
	}

	/**
	 * Creates and populates XiNode instance for the passed event.
	 * 
	 * @param event
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private XiNode serializeToXiNode(EventWithId event, Map<String, Object> properties) throws Exception {
		ExpandedName rootNm;
		if (isIncludeEventType(properties)) {
			if (event.getEventUri() == null || event.getEventUri().isEmpty()) {
				throw new Exception("Event Uri is required when Including Event Type.");
			}
			String eventNs;
			String name;
			if (event.getEventUri().startsWith("{")) {
				eventNs = event.getEventUri().substring(1, event.getEventUri().indexOf('}'));
				name = event.getEventUri().substring(event.getEventUri().lastIndexOf('}') + 1,
						event.getEventUri().length());
			} else {
				eventNs = event.getEventUri();
				name = eventNs.substring(eventNs.lastIndexOf("/") + 1, eventNs.length());
			}
			rootNm = ExpandedName.makeName(eventNs, name);
		} else if (event.getEventUri() != null) {
			rootNm = ExpandedName.makeName(event.getEventUri().substring(event.getEventUri().lastIndexOf('}') + 1));
		} else {
			rootNm = ExpandedName.makeName(EVENT_TAG_NAME);
		}

		XiNode node = XiSupport.getXiFactory().createElement(rootNm);
		node.setAttributeStringValue(ExpandedName.makeName(Entity.ATTRIBUTE_ID), String.valueOf(event.getId()));
		if (event.getExtId() != null) {
			node.setAttributeStringValue(ExpandedName.makeName(Entity.ATTRIBUTE_EXTID), event.getExtId());
		}

		for (String propName : event.getAllPropertyNames()) {
			if (KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY.equals(propName)) {
				continue;
			}
			Object val = event.getPropertyValue(propName);
			if (val instanceof Calendar) {
				XDateTime c = new XDateTime(((Calendar) val).getTime(), ((Calendar) val).getTimeZone());
				node.appendElement(ExpandedName.makeName(propName)).appendText(c.castAsString());
			} else if (val != null) {
				node.appendElement(ExpandedName.makeName(propName))
						.appendText(String.valueOf(event.getPropertyValue(propName)));
			}
		}
		if (event.getPayload() != null) {
			XiNode payload = XiSupport.getParser().parse(new InputSource(new ByteArrayInputStream(event.getPayload())));

			XiNodeUtilities.cleanTextNodes(payload);
			node.appendElement(ExpandedName.makeName(PAYLOAD_TAG_NAME)).appendChild(payload.getFirstChild());
		}

		return node;
	}

	/**
	 * By default IncludeEventType is true
	 * 
	 * @param properties
	 * @return
	 */
	private boolean isIncludeEventType(Map<String, Object> properties) {
		if (properties == null || !properties.containsKey(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE)) {
			return true;
		} else {
			return (boolean) properties.get(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE);
		}
	}

	@Override
	public String keySerializer() {
		return "org.apache.kafka.common.serialization.StringSerializer";
	}

	@Override
	public String valueSerializer() {
		return "org.apache.kafka.common.serialization.StringSerializer";
	}

	@Override
	public String keyDeserializer() {
		return "org.apache.kafka.common.serialization.StringDeserializer";
	}

	@Override
	public String valueDeserializer() {
		return "org.apache.kafka.common.serialization.StringDeserializer";
	}
}
