package com.tibco.cep.driver.sb.serializers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;

import com.streambase.sb.ByteArrayView;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.DataType;
import com.streambase.sb.Schema;
import com.streambase.sb.Schema.Field;
import com.streambase.sb.StreamBaseException;
import com.streambase.sb.Timestamp;
import com.streambase.sb.Tuple;
import com.streambase.sb.TupleJSONUtil;
import com.streambase.sb.TupleJSONUtil.Options;
import com.streambase.sb.client.StreamBaseClient;
import com.streambase.sb.util.Util;
import com.tibco.cep.driver.sb.SBConstants;
import com.tibco.cep.driver.sb.internal.ISBDestination;
import com.tibco.cep.driver.util.PayloadExceptionUtil;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.channel.Channel;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.channel.DestinationConfig;
import com.tibco.cep.runtime.channel.EventSerializer;
import com.tibco.cep.runtime.channel.SerializationContext;
import com.tibco.cep.runtime.model.event.EventPayload;
import com.tibco.cep.runtime.model.event.PayloadFactory;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.model.event.impl.ObjectPayload;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.xml.data.primitive.ExpandedName;
import com.tibco.xml.datamodel.XiNode;
import com.tibco.xml.schema.SmParticleTerm;

public class StreamBaseSerializer implements EventSerializer {

	private Logger logger;

	@Override
	public void init(ChannelManager channelManager, DestinationConfig config) {
		this.logger = LogManagerFactory.getLogManager().getLogger(StreamBaseSerializer.class);
	}

	@Override
	public SimpleEvent deserialize(Object message, SerializationContext context)
			throws Exception {
		ISBDestination dest = (ISBDestination) context.getDestination();
		RuleSession session = context.getRuleSession();
		DestinationConfig config = dest.getConfig();
		ExpandedName en = config.getDefaultEventURI();
		Tuple tuple = (Tuple) message;
		boolean hasNmField = tuple.getSchema().hasField(Channel.EVENT_NAME_PROPERTY);
		boolean hasNsField = tuple.getSchema().hasField(Channel.EVENT_NAMESPACE_PROPERTY);
		if (hasNmField && hasNsField) {
			String nm = (String) tuple.getField(Channel.EVENT_NAME_PROPERTY);
			String ns = (String) tuple.getField(Channel.EVENT_NAMESPACE_PROPERTY);
			en = ExpandedName.makeName(ns, nm);
			logger.log(Level.DEBUG, "Namespace for default event overridden to '%s'", en);
		}
		
		SimpleEvent event = (SimpleEvent)session.getRuleServiceProvider().getTypeManager().createEntity(en);

		populateEventFromTuple(event, tuple, session);
		return event;
	}

	private void populateEventFromTuple(SimpleEvent event, Tuple tuple, RuleSession session) throws Exception {
		Schema schema = tuple.getSchema();
		Field[] fields = schema.getFields();
		for (Field field : fields) {
			updateEvent(event, field, tuple, session);
		}
	}

	@Override
	public Object serialize(SimpleEvent event, SerializationContext context)
			throws Exception {
		ISBDestination dest = (ISBDestination) context.getDestination();
		StreamBaseClient client = dest.getClient();
		DestinationConfig config = dest.getConfig();
		Schema schema = client.getSchemaForStream(config.getProperties().getProperty("StreamName"));
		Tuple tuple = schema.createTuple();
		
		populateTupleFromEvent(tuple, event);
		
		return tuple;
	}

	private void populateTupleFromEvent(Tuple tuple, SimpleEvent event) throws NoSuchFieldException, StreamBaseException {
		Schema schema = tuple.getSchema();
		Field[] fields = schema.getFields();
		for (Field field : fields) {
			updateTuple(event, field, tuple);
		}
	}

	private void updateTuple(SimpleEvent event, Field field, Tuple tuple) throws NoSuchFieldException, StreamBaseException {
		if (null != tuple) {
			String[] props = event.getPropertyNames();
			boolean hasProp = false;

			String key = Util.unwrapExoticIdentifier(field.getName());
			
			//TODO can avoid this if generated setPropety function does not throw exception for invalid property name
			for (String prop : props) {
				if (prop.equals(key)) {
					hasProp = true;
					break;
				}
			}
			if (!hasProp && (SBConstants.PAYLOAD_FIELD.equals(key) || Channel.EVENT_NAME_PROPERTY.equals(key) || Channel.EVENT_NAMESPACE_PROPERTY.equals(key))) {
				hasProp = true;
			}
			if (hasProp) {
				DataType dataType = field.getDataType();
				switch (dataType) {
				case BOOL:
					tuple.setBoolean(field, (boolean) event.getPropertyValue(key));
					break;
				case DOUBLE:
					tuple.setDouble(field, (double) event.getPropertyValue(key));
					break;
				case INT:
					tuple.setInt(field, (int) event.getPropertyValue(key));
					break;
				case LONG:
					tuple.setLong(field, (long) event.getPropertyValue(key));
					break;
				case STRING:
					if (SBConstants.PAYLOAD_FIELD.equals(key)) {
						// special case for payload field, serialize the payload as string
						tuple.setString(field, event.getPayloadAsString());
					} else if (Channel.EVENT_NAME_PROPERTY.equals(key)) {
						// if the Tuple has a field '_nm_', then set the value based on the Event
						tuple.setString(field, (String) event.getExpandedName().getLocalName());
					} else if (Channel.EVENT_NAMESPACE_PROPERTY.equals(key)) {
						// if the Tuple has a field '_ns_', then set the value based on the Event
						tuple.setString(field, (String) event.getExpandedName().getNamespaceURI());
					} else {
						tuple.setString(field, (String) event.getPropertyValue(key));
					}
					break;
				case TIMESTAMP:
					tuple.setTimestamp(field, createTimeStamp((Calendar) event.getPropertyValue(key)));
					break;
				case BLOB:
					tuple.setBlobBuffer(field, ByteArrayView.makeView((String) event.getPropertyValue(key)));
					break;
				case LIST:
					tuple.setList(field, createListFromString(field, event.getPropertyValue(key)));
					break;
				case TUPLE:
					tuple.setTuple(field, createTupleFromString(field, event.getPropertyValue(key)));
					break;
				default:
					logger.log(Level.WARN, "Property Type '%s' unsupported in BE", dataType.getName());
					break;
				}
			} else {
				logger.log(Level.WARN, "Property '%s' not found in Event '%s'", field.getName(), event.getExpandedName());
			}
		}
	}

	private Tuple createTupleFromString(Field field, Object propertyValue) throws StreamBaseException {
		if (!(propertyValue instanceof String)) {
			return null;
		}
		Tuple tuple = field.getSchema().createTuple();

		// assume that propertyValue is the JSON representation of a List
		String strVal = (String) propertyValue;
		TupleJSONUtil.setTupleFromJSON(tuple, strVal);
		return tuple;
	}

	private List<?> createListFromString(Field field, Object propertyValue) throws StreamBaseException {
		if (!(propertyValue instanceof String)) {
			return null;
		}
		// assume that propertyValue is the toString representation of a List
		String strVal = (String) propertyValue;
		if (strVal.length() > 0 && strVal.charAt(0) == '[') {
			strVal = strVal.substring(1, strVal.length()-1);
		}
		String[] split = strVal.split(",");
		CompleteDataType compType = field.getCompleteDataType();
		DataType dataType = compType.getElementType().getDataType();
		
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < split.length; i++) {
			list.add(convertToDataType(dataType, split[i].trim()));
		}
		return list;
	}

	private Object convertToDataType(DataType dataType, String val) throws StreamBaseException {
		switch (dataType) {
		case BOOL:
			return Boolean.parseBoolean(val);
		case DOUBLE:
			return Double.parseDouble(val);
		case INT:
			return Integer.parseInt(val);
		case LONG:
			return Long.parseLong(val);
		case STRING:
			return val;
		case TIMESTAMP:
			return parseTimeStamp(val);
		case BLOB:
			return ByteArrayView.makeView(val);
		default:
			logger.log(Level.WARN, "Property Type '%s' unsupported in BE", dataType.getName());
			break;
		}
		return null;
	}

	private Timestamp parseTimeStamp(String val) throws StreamBaseException {
		return Timestamp.fromString(val);
	}

	private Timestamp createTimeStamp(Calendar cal) {
		if (cal == null) {
			return null;
		}
		return new Timestamp(cal.getTime());
	}

	/**
	 * 
	 * @param event
	 * @param session 
	 * @param key
	 * @param value
	 */
	public void updateEvent(SimpleEvent event, Field field, Tuple tuple, RuleSession session) throws Exception {
		if (null != tuple && !tuple.isNull(field)) {
			String[] props = event.getPropertyNames();
			boolean hasProp = false;

			String key = Util.unwrapExoticIdentifier(field.getName());
			if (SBConstants.PAYLOAD_FIELD.equals(key)) {
				deserializePayload(event, session, tuple.getString(field), "UTF-8");
				return;
			}
			if (Channel.EVENT_NAME_PROPERTY.equals(key) || Channel.EVENT_NAMESPACE_PROPERTY.equals(key)) {
				return; // special case for _ns_ and _nm_ fields
			}
			//TODO can avoid this if generated setPropety function does not throw exception for invalid property name
			for (String prop : props) {
				if (prop.equals(key)) {
					hasProp = true;
					break;
				}
			}

			if (hasProp) {
				DataType dataType = field.getDataType();
				switch (dataType) {
				case BOOL:
					event.setProperty(key, tuple.getBoolean(field));
					break;
				case DOUBLE:
					event.setProperty(key, tuple.getDouble(field));
					break;
				case INT:
					event.setProperty(key, tuple.getInt(field));
					break;
				case LONG:
					event.setProperty(key, tuple.getLong(field));
					break;
				case STRING:
					event.setProperty(key, tuple.getString(field));
					break;
				case TIMESTAMP:
					if (tuple.isNull(field)) {
						break;
					}
					event.setProperty(key, convertToDateTime(tuple.getTimestamp(field)));
					break;
				case LIST:
					// convert the list to a String
					String listAsStr = tuple.getList(field).toString();
					event.setProperty(key, listAsStr);
					break;
				case TUPLE:
					// convert the tuple to a String
					if (tuple.isNull(field)) {
						break;
					}
					Tuple t = tuple.getTuple(field);
			    	EnumSet<Options> options = EnumSet.of(Options.INCLUDE_NULL_FIELDS, Options.PREFER_MAP);
					String tupleAsStr = TupleJSONUtil.toJSONString(t, options);
					event.setProperty(key, tupleAsStr);
					break;
				case BLOB:
					// convert the blob to a String
					if (tuple.isNull(field)) {
						break;
					}
					String blobAsStr = tuple.getBlobBuffer(field).asString();
					event.setProperty(key, blobAsStr);
					break;
				case FUNCTION:
					// convert the function to a String
					if (tuple.isNull(field)) {
						break;
					}
					String funcAsStr = tuple.getFunction(field).getStringRep();
					event.setProperty(key, funcAsStr);
					break;
					
				default:
					logger.log(Level.WARN, "Property Type '%s' unsupported in BE", dataType.getName());
					break;
				}
			} else {
				logger.log(Level.WARN, "Property '%s' not found in Event '%s'", field.getName(), event.getExpandedName());
			}
		}
	}

	private Object convertToDateTime(Timestamp timestamp) {
		// does Timestamp contain TimeZone info?
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTimeInMillis(timestamp.toMsecs());
		return gregorianCalendar;
	}

    /**
     * @param event
     * @param session
     * @param message
     * @param encoding
     * @throws Exception
     */
    protected void deserializePayload(final SimpleEvent event,
                                      final RuleSession session,
                                      final Object message,
                                      final String encoding) throws Exception {
        //Get the RSP from this
        RuleServiceProvider rsp = session.getRuleServiceProvider();
        PayloadFactory payloadFactory = rsp.getTypeManager().getPayloadFactory();
        if (payloadFactory != null) {
            EventPayload payload = null;
            //Get the schema associated with payload
            SmParticleTerm payloadTerm = payloadFactory.getPayloadElement(event.getExpandedName());
            if (payloadTerm == null || payloadTerm.getSchema() == null) {
                if (message != null) {
                    payload = (message instanceof byte[]) ? new ObjectPayload(message, encoding) : new ObjectPayload(message);
                }
            } else {
                ExpandedName exName = event.getExpandedName();
                try {
                    if (message instanceof String) {
                        String msg = (String) message;
                        if (msg.length() > 0) {
                            //If XML is sent as string
                            payload = payloadFactory.createPayload(exName, (String) message);
                        }
                    }
                    if (message instanceof byte[]) {
                        payload = payloadFactory.createPayload(exName, (byte[]) message);
                        logger.log(Level.DEBUG, "Payload String conversion %s", payload.toString());
                    }
                    if (message instanceof XiNode) {
                        payload = payloadFactory.createPayload(exName, (XiNode) message);
                    }
                } catch (RuntimeException re) {
                    PayloadExceptionUtil.assertPayloadExceptionAdvisoryEvent(re, message, event, session);
                }
            }
            event.setPayload(payload);
        }
    }
    
}
