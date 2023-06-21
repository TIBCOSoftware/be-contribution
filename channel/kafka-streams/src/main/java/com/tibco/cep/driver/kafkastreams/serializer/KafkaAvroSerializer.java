package com.tibco.cep.driver.kafka.serializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.avro.Schema.Type;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.designtime.model.element.Concept;
import com.tibco.cep.designtime.model.element.PropertyDefinition;
import com.tibco.cep.designtime.model.event.EventPropertyDefinition;
import com.tibco.cep.driver.kafka.KafkaEvent;
import com.tibco.cep.driver.kafka.KafkaProperties;
import com.tibco.cep.driver.kafka.KafkaPropertiesHelper;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.channel.ChannelProperties;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.cep.designtime.model.Ontology;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import com.tibco.messaging.schema.registry.SchemaRegistryCache;
import com.tibco.xml.NamespaceImporter;
import com.tibco.xml.data.primitive.PrefixToNamespaceResolver.PrefixNotFoundException;

public class KafkaAvroSerializer extends BaseEventSerializer implements KafkaSerializer {

	private String topic;
	private String subject;
	private String schemaRegistryUrl;
	private String schemaRegistryPlatform;
	private String JsonSchema;
	private Logger logger;
	private boolean skipBEAttributes;
	private boolean updateSchemaRegistry;
	
	private String authType;
	private Schema schema;

	public Map<String, Object> props;
	
	private  Ontology ontology ;
	
	private HashMap<String,String> defaultMapVal ;
	private HashMap<String,Object> defaultAttrObjMapVal ;
	private ElementNode root=null;
	private NamespaceImporter nsImp1;
	
	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		// TODO Auto-generated method stub
		defaultMapVal = new HashMap<String,String>();
		defaultMapVal.put("Id", "attr");
		defaultMapVal.put("type", "uri");
		
		this.topic = destinationProperties.getProperty(KafkaProperties.KEY_DESTINATION_TOPIC_NAME);
		this.subject = destinationProperties.getProperty(KafkaProperties.KEY_DESTINATION_SCHEMA_SUBJECT);
		this.schemaRegistryUrl = destinationProperties.getProperty(KafkaProperties.SCHEMA_REGISTRY_URL);
		this.schemaRegistryPlatform = destinationProperties.getProperty(KafkaProperties.SCHEMA_REGISTRY_PLATFORM);
		this.JsonSchema = destinationProperties.getProperty(KafkaProperties.JSON_SCHEMA);
		this.logger = logger;
		this.skipBEAttributes = Boolean
				.parseBoolean(System.getProperty(KafkaProperties.KEY_SKIP_BE_ATTRIBUTES, "false"));
		String stringVal =  destinationProperties.getProperty(KafkaProperties.UPDATE_SCHEMA_REGISTRY);
		this.updateSchemaRegistry= stringVal.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
		this.authType = destinationProperties.getProperty(KafkaProperties.SCHEMA_REGISTRY_AUTH_TYPE);
		
		String eventURI  = destinationProperties.getProperty(ChannelProperties.DEFAULT_EVENT_URI);
		
		try {
		schema = null;
		
		Properties authenticationProps = new Properties();
		Map<String, Object> props = new HashMap<String, Object>();
		
		KafkaPropertiesHelper.initSchemaReistryAutheticationProperties(destinationProperties,authenticationProps);
		
		for(Map.Entry<Object, Object> entry : authenticationProps.entrySet()) {
			props.put((String)entry.getKey(), entry.getValue());
		}
		
		if(this.updateSchemaRegistry) {
			schema = createSchemaUsingEventData(eventURI);
			//Remove default attributes in all schema fields
		    JSONObject jsonObject = new JSONObject(schema.toString());
		    removeDefaultFieldFromJson(jsonObject);
			String schemaInString = jsonObject.toString();
			Schema.Parser parser = new Schema.Parser();
			schema = parser.parse(schemaInString);
			
			
			logger.log(Level.INFO,"Updated Schema to be registred in schema Registry:" + schema.toString());
		}
		
		if(!(null == this.subject || this.subject.isBlank()) && null == schema) {
			
			if (this.schemaRegistryPlatform.equals(KafkaProperties.CONFLUENT_PLATFORM)) {
				
				SchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(this.schemaRegistryUrl, 100,props);
				
				Collection<String> subjectList = schemaRegistryClient.getAllSubjects();
				boolean isPresent = false;
				for(String schemaSubject : subjectList) {
					if(schemaSubject.equalsIgnoreCase(this.subject)) {
						this.subject = schemaSubject ;
						isPresent = Boolean.TRUE;
					}
				}
				
				if(isPresent) {
					logger.log(Level.INFO, "Associated Schema Subject Registered in Schema Registry:" + this.subject);
					SchemaMetadata metadata = schemaRegistryClient.getLatestSchemaMetadata(this.subject);
					String schemaInString = metadata.getSchema();
					// parse the schema
					Schema.Parser parser = new Schema.Parser();
					schema = parser.parse(schemaInString);
					logger.log(Level.DEBUG, "Associated Schema  Registered in Schema Registry:" + schema.toString());
				}
			
			} else {
				
				String[] array = new String[1];
				array[0] = this.schemaRegistryUrl;
				try {
					String trustType = (String) props.getOrDefault(KafkaProperties.FTL_TRUST_TYPE, null);
					String trustFile = (String) props.getOrDefault(KafkaProperties.FTL_TRUST_FILE, null);
					String trustString = (String) props.getOrDefault("default", null);
					String ftlUserName = (String) props.getOrDefault(KafkaProperties.FTL_USERNAME, null);
					String ftlUserPassword = (String) props.getOrDefault(KafkaProperties.FTL_PASSWORD, null);
					
					SchemaRegistryCache schemaRegistryClient = new SchemaRegistryCache(array, trustType, trustFile, trustString, ftlUserName, ftlUserPassword);
					schema = schemaRegistryClient.getSchema(this.subject,-1);
					if(null != schema) {
						logger.log(Level.INFO, "Associated Schema Subject Registered in Schema Registry:" + this.subject);
					}
					
				} catch(Exception ex) {
					logger.log(Level.ERROR, "Error in extracting Schema:" + ex.getMessage());
				}
				
			}
		}
		
		if(!(null == this.JsonSchema || this.JsonSchema.isBlank()) && null == schema){
			logger.log(Level.INFO, "Associated Schema to be registered in Schema Registry:" + this.JsonSchema);
			String schemaInString = this.JsonSchema.replace("\\", "");
			Schema.Parser parser = new Schema.Parser();
			schema = parser.parse(schemaInString);
		} 
		
		if(null == schema ){
			Schema createdSchema  = createSchemaUsingEventData(eventURI);
			schema= createdSchema ;
			//Remove default attributes in all schema fields
		    JSONObject jsonObject = new JSONObject(schema.toString());
		    removeDefaultFieldFromJson(jsonObject);
			String schemaInString = jsonObject.toString();
			Schema.Parser parser = new Schema.Parser();
			schema = parser.parse(schemaInString);
			logger.log(Level.INFO, "Created Schema to be registered in Schema Registry:" + schema.toString());
		}
	  }catch(Exception ex) {
		  logger.log(Level.ERROR, ex,"Exception in extracting or creating schema:" + ex.getMessage());
		  schema=null;
	  }
 
	}
	
	@Override
	public Object serializeUserEvent(EventWithId event, Map<String, Object> properties) throws Exception {
		
		if (schema == null) {
			logger.log(Level.ERROR, "Avro Schema object is null . Exception in extacting or registering schema " );
			throw new Exception();
		}
		
		String eventJsonStr = serializeToJSON(event, properties, false);

		JSONObject eventJson = new JSONObject(eventJsonStr);
		GenericRecord avroRecord = createGenericRecordUsingEvent(schema, eventJson);

		if(null == avroRecord){
		 logger.log(Level.ERROR, "Error in Creating Avro Record" );
		  throw new Exception();
		}
				
		logger.log(Level.DEBUG, "Avro Record using event payload :" + avroRecord.toString());
		
		return new ProducerRecord<String, GenericRecord>(this.topic, null, avroRecord);
	}

	public GenericRecord createGenericRecordUsingEvent(Schema schema, JSONObject payloadFieldJson) {
		GenericRecord avroRecord = new GenericData.Record(schema);
		Object fieldObj;
		List<Field> list = avroRecord.getSchema().getFields();
		try {
		for (Field fd : list) {
			Type fieldType  = fd.schema().getType();
			GenericRecord itemrecord;
			switch(fieldType) {
			case STRING :
				if(payloadFieldJson.has(fd.name()))
				//avroRecord.put(fd.name(),payloadFieldJson.getString(fd.name()) ) ;
				avroRecord.put(fd.name(),payloadFieldJson.get(fd.name())) ;
				else {
					if(fd.name().equalsIgnoreCase("Id") || fd.name().equalsIgnoreCase("type") || fd.name().equalsIgnoreCase("extId") || fd.name().equalsIgnoreCase("ref"))
					avroRecord.put(fd.name(),"") ;
				}
				break;
			case INT :
				fieldObj = payloadFieldJson.get(fd.name());
				
				if((fieldObj instanceof  String) && checkInteger(fieldObj.toString())) {
				    avroRecord.put(fd.name(), payloadFieldJson.getInt(fd.name()));
				} else 
				avroRecord.put(fd.name(), payloadFieldJson.get(fd.name()));
				
			   break;
			case  DOUBLE :
				fieldObj = payloadFieldJson.get(fd.name());
				
				if((fieldObj instanceof  String) && checkDouble(fieldObj.toString())) {
				    avroRecord.put(fd.name(), payloadFieldJson.getDouble(fd.name()));
				} else 
				avroRecord.put(fd.name(), payloadFieldJson.get(fd.name()));
				break ;
			case LONG :
				fieldObj = payloadFieldJson.get(fd.name());
				if((fieldObj instanceof  String) && checkLong(fieldObj.toString())) {
				    avroRecord.put(fd.name(), payloadFieldJson.getLong(fd.name()));
				} else 
				avroRecord.put(fd.name(), payloadFieldJson.get(fd.name()));
				
				break ;
			case  BOOLEAN :
				fieldObj = payloadFieldJson.get(fd.name());
				if((fieldObj instanceof  String) && checkBoolean(fieldObj.toString())) {
				    avroRecord.put(fd.name(), payloadFieldJson.getBoolean(fd.name()));
				} else 
				avroRecord.put(fd.name(), payloadFieldJson.get(fd.name()));
				
				break;
			case RECORD :
				if(this.skipBEAttributes && fd.name().equals("attributes"))
					continue;
					itemrecord = createGenericRecordUsingEvent(fd.schema(),
							payloadFieldJson.getJSONObject(fd.name()));
					avroRecord.put(fd.name(), itemrecord);
				break ;
			case ARRAY :
				JSONArray array = payloadFieldJson.getJSONArray(fd.name());
				//List<GenericRecord> itemList = new ArrayList();
				
				Schema itemSchema = fd.schema().getElementType();
				Type type = itemSchema.getType();
				
				if(type == Type.STRING) {
					List<String> itemList = new ArrayList();
					for (int i = 0; i < array.length(); i++) {
						itemList.add((String) array.get(i));
					}
					avroRecord.put(fd.name(), itemList);
					break;
					
				} else if(type == Type.INT) {
					List<Object> itemList = new ArrayList();
					for (int i = 0; i < array.length(); i++) {
						fieldObj = array.get(i);
						
						if((fieldObj instanceof  String) && checkInteger(fieldObj.toString())) {
							itemList.add(Integer.parseInt(fieldObj.toString()));
						} else if(fieldObj instanceof Integer) {
							itemList.add((Integer) fieldObj);
						} else 
							itemList.add(fieldObj);
					}
					avroRecord.put(fd.name(), itemList);
					break;
				} else if(type == Type.LONG) {
					List<Object> itemList = new ArrayList();
					for (int i = 0; i < array.length(); i++) {
						fieldObj = array.get(i);
						
						if((fieldObj instanceof  String) && checkLong(fieldObj.toString())) {
							itemList.add(Long.parseLong(fieldObj.toString()));
						} else 
							itemList.add((Long) fieldObj);
					}
					avroRecord.put(fd.name(), itemList);
					break;
					
				} else if(type == Type.DOUBLE) {
					List<Object> itemList = new ArrayList();
					for (int i = 0; i < array.length(); i++) {
						fieldObj = array.get(i);
						
						if((fieldObj instanceof  String) && checkDouble(fieldObj.toString())) {
							itemList.add(Double.parseDouble(fieldObj.toString()));
						} else 
							itemList.add((Double) fieldObj);
					}
					avroRecord.put(fd.name(), itemList);
					break;
					
				}  else if(type == Type.BOOLEAN) {
					List<Object> itemList = new ArrayList();
					for (int i = 0; i < array.length(); i++) {
						fieldObj = array.get(i);
						
						if((fieldObj instanceof  String) && checkBoolean(fieldObj.toString())) {
							itemList.add(Boolean.parseBoolean(fieldObj.toString()));
						} else 
							itemList.add((Boolean) fieldObj);
					}
					avroRecord.put(fd.name(), itemList);
					break;
				} 
				
				List<GenericRecord> itemList = new ArrayList();
				for (int i = 0; i < array.length(); i++) {
					itemrecord = createGenericRecordUsingEvent(fd.schema().getElementType(),
							(JSONObject) array.get(i));
					itemList.add(itemrecord);
				}
				
				avroRecord.put(fd.name(), itemList);
				break;
			case UNION :
				if(payloadFieldJson.has(fd.name()))
				avroRecord.put(fd.name(),payloadFieldJson.get(fd.name())) ;
				else
					avroRecord.put(fd.name(),null) ;	
				break;
			default :
				avroRecord.put(fd.name(),payloadFieldJson.get(fd.name())) ;
			}
		 }
		}catch(JSONException ex) {
			logger.log(Level.ERROR, ex, "Error in creating avro record from event paylod:"+ ex.getMessage());
			return null;
		}
		return avroRecord;
	}
	
	@Override
	public Event deserializeUserEvent(Object message, Map<String, Object> properties) throws Exception {
		Event event = null;
		
		ConsumerRecord<String, GenericRecord> kafkaMessage = null;
		if (message instanceof ConsumerRecord) {
			kafkaMessage = (ConsumerRecord<String, GenericRecord>) message;
		}
		
		event = deserializeFromJSON(kafkaMessage, properties);

		return event;
	}

	private String serializeToJSON(EventWithId event, Map<String, Object> properties, boolean pretty)
			throws Exception {
		Map<String, Object> jsonEntries = new LinkedHashMap<String, Object>();

		Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();
		jsonEntries.put("attributes", attributeMap);
	
		  //will fix this harcoding
		attributeMap.put(Concept.BASE_ATTRIBUTE_NAMES[1], String.valueOf(event.getId()));
		if (event.getExtId() != null && !event.getExtId().isEmpty()) {
			attributeMap.put(Concept.BASE_ATTRIBUTE_NAMES[0], event.getExtId());
		}
		if (isIncludeEventType(properties) && event.getEventUri() != null) {
			if (event.getEventUri().startsWith("{")) {
				attributeMap.put("type", event.getEventUri().substring(1, event.getEventUri().indexOf('}')));
			} else {
				attributeMap.put("type", event.getEventUri());
			}
		}

		for (String propName : event.getAllPropertyNames()) {
			if (!KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY.equals(propName)) {
				jsonEntries.put(propName, event.getPropertyValue(propName));
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		if (pretty)
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_EMPTY);

		try {
			if (event.getPayload() != null) {
				Object payloadJS = mapper.readValue(new String(event.getPayload()), Object.class);
				jsonEntries.put(KafkaProperties.RESERVED_EVENT_PROP_PAYLOAD, payloadJS);
			}
		} catch (Exception ex) {
			logger.log(Level.ERROR, "Error in json serializer:"+ ex.getMessage());
			throw ex;
		}

		return mapper.writeValueAsString(jsonEntries);
	}

	private Event deserializeFromJSON(ConsumerRecord<String, GenericRecord> consRecord, Map<String, Object> properties)
			throws Exception {
		Event event = new KafkaEvent();

       try {
		if(null != consRecord.value()) {
			GenericRecord eventRecord = consRecord.value();
			//
			for (Field field : eventRecord.getSchema().getFields()) {
			  if(field.schema().getType() == Type.RECORD && field.name().equals("attributes")) {
				  GenericRecord attributeRecord = (GenericRecord) eventRecord.get(field.name());
				  String eventUri=null,extId=null;
					  eventUri = null != attributeRecord.getSchema().getField("type") ? attributeRecord.getSchema().getField("type").toString() : "";
					  extId = null != attributeRecord.getSchema().getField("extId") ? attributeRecord.getSchema().getField("extId").toString() : "";
				  if (isIncludeEventType(properties) && eventUri != null
							&& eventUri.contains(KafkaProperties.ENTITY_NS)) {
						eventUri = eventUri.substring(KafkaProperties.ENTITY_NS.length());
						event.setEventUri(eventUri);
				  }
				  if (extId != null && !extId.isEmpty()) {
						event.setExtId(extId);
				  }
				  
				  if (event.getEventUri() != null && !event.getEventUri().equals(getDesignTimeEventUri()))
						setDesignTimeEvent(event.getEventUri());
			  } else if(field.schema().getType() == Type.RECORD && field.name().equals(KafkaProperties.PAYLOAD)) {
				  JSONObject recordObj = createRecordPayload((GenericRecord)eventRecord.get(KafkaProperties.PAYLOAD));
				  JSONObject payload = new JSONObject();
				  payload.put(KafkaProperties.PAYLOAD, recordObj);
				  String payloadJsonString = payload.toString();
				  if (null != payloadJsonString)
					  event.setPayload(payloadJsonString.getBytes());
			  } else {
				    Schema fieldType = null;
				  	if(field.schema().getType() == Type.UNION) {
				  		Object obj = eventRecord.get(field.name());
				  		if(obj == null)
				  		continue;
				  		for(Schema type :field.schema().getTypes()) {
				  			if(null == type)
				  			continue;
				  			fieldType = type;
				  			break;
				  		}
				  	}
				  
					if (field.schema().getType() == Type.INT ) {
						int val = (Integer) eventRecord.get(field.name());
						event.setProperty(field.name(), val);
					} else if(field.schema().getType() == Type.STRING) {
						String val  = eventRecord.get(field.name()).toString();
						event.setProperty(field.name(), val);
					} else if(field.schema().getType() == Type.DOUBLE) {
						double val  = (Double)eventRecord.get(field.name());
						event.setProperty(field.name(), val);
					}else if(field.schema().getType() == Type.BOOLEAN) {
						Boolean val  = (Boolean)eventRecord.get(field.name());
						event.setProperty(field.name(), val);
					} else {
						event.setProperty(field.name(), eventRecord.get(field.name()));	
					}
			  }
			}
		}
       }catch(Exception ex) {
    	   logger.log(Level.ERROR, "Error in deserializing event from Generic Record:" + ex.getMessage());
       }
		
		return event;
	}

	public JSONObject createRecordPayload(GenericRecord record) {
		JSONObject recordObj = new JSONObject();

		for (Field field : record.getSchema().getFields()) {
			if (field.schema().getType() == Type.ARRAY) {
				JSONArray array = new JSONArray();
				
				Type type = field.schema().getElementType().getType();
				if(type == Type.INT) {
					List<Integer> list = (List<Integer>) record.get(field.name());
					for (int i = 0; i < list.size(); i++)
						array.put(list.get(i));  
				} else if(type == Type.STRING) {
					List<String> list = (List<String>) record.get(field.name());
					for (int i = 0; i < list.size(); i++)
						array.put(list.get(i));
				} else if(type == Type.LONG) {
					List<Long> list = (List<Long>) record.get(field.name());
					for (int i = 0; i < list.size(); i++)
						array.put(list.get(i));
				} else if(type == Type.DOUBLE) {
					List<Double> list = (List<Double>) record.get(field.name());
					for (int i = 0; i < list.size(); i++)
						array.put(list.get(i));
				} else if(type == Type.BOOLEAN) {
					List<Boolean> list = (List<Boolean>) record.get(field.name());
					for (int i = 0; i < list.size(); i++)
						array.put(list.get(i));
				}else if(type == type.RECORD) {
					List<GenericRecord> list = (List<GenericRecord>) record.get(field.name());
					for (int i = 0; i < list.size(); i++) {
						JSONObject arrayItem = createRecordPayload(list.get(i));
						array.put(arrayItem);
					}
				}
				recordObj.put(field.name(), array);
			} else if (field.schema().getType() == Type.RECORD) {
				JSONObject innerObj = createRecordPayload((GenericRecord) record.get(field.name()));
				recordObj.put(field.name(), innerObj);
			} else {
				recordObj.put(field.name(), record.get(field.name()));
			}
		}

		return recordObj;
	}

	private boolean isIncludeEventType(Map<String, Object> properties) {
		if (properties == null || !properties.containsKey(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE)) {
			return true;
		} else {
			return (boolean) properties.get(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE);
		}
	}

	@Override
	public boolean isJSONPayload() {
		return true;
	}

	@Override
	public String keySerializer() {
		return "org.apache.kafka.common.serialization.StringSerializer";
	}

	@Override
	public String valueSerializer() {
		return KafkaProperties.CONFLUENT_SCHEMA_REGISTRY_SERIALIZER;
	}

	@Override
	public String keyDeserializer() {
		return "org.apache.kafka.common.serialization.StringDeserializer";
		
	}

	@Override
	public String valueDeserializer() {
		return KafkaProperties.CONFLUENT_SCHEMA_REGISTRY_DESERIALIZER;
	}
	
	private void removeAttributes(Map<String, Object> map) {
		map.remove("attributes");
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				removeAttributes((Map) entry.getValue());
			} else if (entry.getValue() instanceof List) {
				for (Object attrObj : ((List) entry.getValue())) {
					if (attrObj instanceof Map) {
						removeAttributes((Map) attrObj);
					}
				}
			}
		}
	}
	public Schema createSchemaUsingEventData(String eventURI) throws Exception {

		Schema createdSchema = null;

		RuleServiceProvider RSP = RuleServiceProviderManager.getInstance().getDefaultProvider();
		Ontology ontology = RSP.getProject().getOntology();
		this.ontology = ontology;
		
		String eventPath=null;
		eventPath = eventURI.contains("/Events") ? eventURI.substring(eventURI.indexOf("/Events")) : eventURI;
						
		com.tibco.cep.designtime.model.event.Event event = ontology.getEvent(eventPath);

		try {
			if (event == null) {
				throw new Exception("Default Event field is not mapped to an Event.");
			}
			createdSchema = processEventForSchemaRegistry(eventURI, event);
		} catch (ParserConfigurationException | SAXException | IOException | PrefixNotFoundException ex) {
			// TODO Auto-generated catch block
			logger.log(Level.ERROR, ex,"Exception in creating schema:" + ex.getMessage());
			throw new Exception(ex.getMessage());
		} 

		return createdSchema;
	}

	private Schema processEventForSchemaRegistry(String eventURI, com.tibco.cep.designtime.model.event.Event event)
			throws Exception {

		ArrayList<Schema.Field> event_data_list = new ArrayList();
		Schema eventSchema = null;
		if (event != null) {
			Iterator propItr = event.getUserProperties();
			while (propItr.hasNext()) {
				EventPropertyDefinition eventProp = (EventPropertyDefinition) propItr.next();
				Field schemaField = null;
				switch (eventProp.getType().getTypeId()) {
				case 0:
					schemaField = new Schema.Field(eventProp.getPropertyName(), Schema.create(Type.STRING) , "","");
					break;
				case 1:
					schemaField = new Schema.Field(eventProp.getPropertyName(), Schema.create(Type.INT),"",0);
					break;
				case 2:
					schemaField = new Schema.Field(eventProp.getPropertyName(), Schema.create(Type.LONG),"",0);
					break;
				case 3:
					schemaField = new Schema.Field(eventProp.getPropertyName(), Schema.create(Type.DOUBLE),"",0);
					break;
				case 4:
					schemaField = new Schema.Field(eventProp.getPropertyName(), Schema.create(Type.BOOLEAN),"",Boolean.TRUE);
					break;
				case 5:
					schemaField = new Schema.Field(eventProp.getPropertyName(), Schema.create(Type.STRING),"","");
					break;
				}
				event_data_list.add(schemaField);
			}
		}

		if (!this.skipBEAttributes) {
			Schema.Field schemaField = null;
			ArrayList<Schema.Field> attr_field_list = new ArrayList<>();

			Schema.Field schemaField1 = new Schema.Field("Id", Schema.create(Type.STRING),"","");
			attr_field_list.add(schemaField1);

			Schema.Field schemaField2 = new Schema.Field("type", Schema.create(Type.STRING),"","");
			attr_field_list.add(schemaField2);
			
			schemaField = new Schema.Field("attributes",Schema.createRecord("eventBEAttributes", "", "", false, attr_field_list) ,"",defaultMapVal);
			
			event_data_list.add(schemaField);
		}

		if (event != null && event.getPayloadSchemaAsString() != null) {
			{// request body schema
				Schema.Field schemaField = null;
				NamespaceImporter nsImp = event.getPayloadNamespaceImporter();
				nsImp1 = nsImp;
				String payload = event.getPayloadSchemaAsString();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(payload));
				Document doc = db.parse(is);
				NodeList payloadNodeList = doc.getElementsByTagName("payload");
				Node refNode = payloadNodeList.item(0).getAttributes().getNamedItem("ref");
				String ref = refNode != null ? refNode.getNodeValue() : null;

				if (ref != null && !ref.isEmpty()) {
					String[] refArray = ref.split(":");
					String namespaceURIForPrefix = nsImp.getNamespaceURIForPrefix(refArray[0]);

					if (namespaceURIForPrefix.startsWith("www.tibco.com/be/ontology")) {
						Concept cept = null;
						for (Iterator iterator = ontology.getConcepts().iterator(); iterator.hasNext();) {
							cept = (Concept) iterator.next();
							if (cept.getName().equalsIgnoreCase(refArray[1])) {
								break;
							}
						}
						LinkedHashMap<String, Object> reqPlDef_props = new LinkedHashMap<>();
						Schema.Field schemaFieldret = createEventPayloadSchemaWithOntology(cept,cept.getName(),PropertyDefinition.PROPERTY_TYPE_CONCEPT);
						
						//Default values for field
						HashMap<String,Object> ceptSpecs = new HashMap<String,Object>();
						Collection<Field> coll = schemaFieldret.schema().getFields();
						for(Field fd :schemaFieldret.schema().getFields()) {
							ceptSpecs.put(fd.name(), fd.defaultVal());
						}
						HashMap<String,Object> dfaultPayloadAttributes = new HashMap<String,Object>();
						dfaultPayloadAttributes.put(cept.getName(), ceptSpecs);
						
						schemaField = new Schema.Field(cept.getName(),Schema.createRecord("payloadAttributes", "", "", false, Arrays.asList(schemaFieldret)) ,"",dfaultPayloadAttributes);
						Schema.Field schemaFieldPay = new Schema.Field("payload", schemaField.schema(),"",dfaultPayloadAttributes);
						event_data_list.add(schemaFieldPay);
						
					} else {
						throw new Exception("Event paylod schema reference or Element is empty ");
					}
				} else {
					NodeList list = payloadNodeList.item(0).getChildNodes();
					Node payloadChildNode = null;
					for(int i=0;i<list.getLength();i++) {
						String name  = list.item(i).getNodeName();
						if(name.equalsIgnoreCase("xs:element")) {
							payloadChildNode = list.item(i);
							break;
						}
					}
				
					traverseSchema(payloadChildNode,root);
				    Schema.Field schemaFieldret = createEventPayloadSchemaWithComplexType(root);
				    String name1 = payloadChildNode.getAttributes().getNamedItem("name").getNodeValue();
				   //Default values for field
						HashMap<String,Object> ceptSpecs = new HashMap<String,Object>();
						Collection<Field> coll = schemaFieldret.schema().getFields();
						for(Field fd :schemaFieldret.schema().getFields()) {
							ceptSpecs.put(fd.name(), fd.defaultVal());
						}
						HashMap<String,Object> dfaultPayloadAttributes = new HashMap<String,Object>();
						dfaultPayloadAttributes.put(name1, ceptSpecs);
						
				     
				     schemaField = new Schema.Field(name1,Schema.createRecord("payloadAttributes", "", "", false, Arrays.asList(schemaFieldret)) ,"",dfaultPayloadAttributes);
						Schema.Field schemaFieldPay = new Schema.Field("payload", schemaField.schema(),"",dfaultPayloadAttributes);
						event_data_list.add(schemaFieldPay);
					//throw new Exception("Associated Event paylod content type is not supported for Avro serializer");
				}
			}
		}
		
		eventSchema = Schema.createRecord(event.getName(), "EventData", "", false);
		eventSchema.setFields(event_data_list);
		logger.log(Level.DEBUG, "Schema to be Registered :" +  eventSchema.toString());
		
		return eventSchema;
	}
	
	public void traverseSchema(Node payloadChildNode , ElementNode parentNode ) {
		
		ElementNode node = null ;
		if(null == parentNode || null == root) {
			parentNode  = new ElementNode(payloadChildNode);
			root = parentNode;
			node = parentNode;
		}else {
			if(payloadChildNode.getNodeName().equalsIgnoreCase("xs:element")) {
				node =  new ElementNode(payloadChildNode);
				parentNode.list.add(node);
			}else if(payloadChildNode.getNodeName().equalsIgnoreCase("xs:complexType")) {
			  node = parentNode;
			  node.isComplexType=true;
			} else {
				node = parentNode;
			}
		}
		
		int len = payloadChildNode.getChildNodes().getLength();
		
		NodeList list = payloadChildNode.getChildNodes();
		for(int i=0;i<len;i++) {
			//String name  = list.item(i).getNodeName();
			payloadChildNode = list.item(i);
			if(payloadChildNode.hasChildNodes()) {
				traverseSchema(payloadChildNode,node);
			} else if(payloadChildNode.getNodeName().equalsIgnoreCase("xs:element")) {
				traverseSchema(payloadChildNode,node);
			}
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	private Schema.Field createEventPayloadSchemaWithComplexType(ElementNode treeNode) throws Exception {
		
		    if(null == treeNode )
		    return null;
		    
			ArrayList<Schema.Field> payload_data_list = new ArrayList();
			HashMap<String,Object> defaultMapPayloadVal  = new  HashMap<String,Object>();

			if (!this.skipBEAttributes) {
				Schema.Field schemaField = null;
				ArrayList<Schema.Field> attr_field_list = new ArrayList<>();

				Schema.Field schemaField_id = new Schema.Field("Id", Schema.create(Type.STRING),"","");
				attr_field_list.add(schemaField_id);

				Schema.Field schemaField_type = new Schema.Field("type", Schema.create(Type.STRING),"","");
				attr_field_list.add(schemaField_type);

				String name = treeNode.schemaNode.getNodeName().replace("xs:", "") + "Attributes" + "Record";
				
				schemaField = new Schema.Field("attributes",Schema.createRecord(name, "", "", false, attr_field_list) ,"",defaultMapVal);
				
				payload_data_list.add(schemaField);
			}
			
			int noOfChilds = treeNode.list.size();
			
			for(int i=0 ; i < noOfChilds ; i++) {
				ElementNode childNode = treeNode.list.get(i);
				Field schemaField = null;
				String  str = childNode.schemaNode.toString() ;
				if( null != childNode.schemaNode.getAttributes().getNamedItem("type")) {
					Node type  = childNode.schemaNode.getAttributes().getNamedItem("type");
					String name =  childNode.schemaNode.getAttributes().getNamedItem("name").getNodeValue();
					
					
					String typeName= type.getNodeValue();
					if(typeName.equalsIgnoreCase("xsd:string"))
					schemaField = new Schema.Field(name, Schema.create(Type.STRING) , "","");
					else if(typeName.equalsIgnoreCase("xsd:integer"))
					schemaField = new Schema.Field(name, Schema.create(Type.INT) , "","");
					else if(typeName.equalsIgnoreCase("xsd:boolean"))
					schemaField = new Schema.Field(name, Schema.create(Type.BOOLEAN) , "","");
					else if(typeName.equalsIgnoreCase("xsd:decimal"))
					schemaField = new Schema.Field(name, Schema.create(Type.DOUBLE) , "","");
					else if(typeName.contains("xs:datetime") || typeName.contains("xs:date") || typeName.contains("xs:duration")
							|| typeName.contains("xs:time")) {
					schemaField = new Schema.Field(name, Schema.create(Type.STRING) , "","");
					}
				} else {
						if(null != childNode.schemaNode.getAttributes().getNamedItem("ref")) {
						  NamespaceImporter nsImp = nsImp1;
						  
						  Node  refNode = childNode.schemaNode.getAttributes().getNamedItem("ref");
						  String ref = refNode != null ? refNode.getNodeValue() : null;
	
							if (ref != null && !ref.isEmpty()) {
								String[] refArray = ref.split(":");
								String namespaceURIForPrefix = nsImp.getNamespaceURIForPrefix(refArray[0]);
	
								if (namespaceURIForPrefix.startsWith("www.tibco.com/be/ontology")) {
									Concept cept = null;
									for (Iterator iterator = ontology.getConcepts().iterator(); iterator.hasNext();) {
										cept = (Concept) iterator.next();
										if (cept.getName().equalsIgnoreCase(refArray[1])) {
											break;
										}
									}
									LinkedHashMap<String, Object> reqPlDef_props = new LinkedHashMap<>();
									Schema.Field schemaFieldret = createEventPayloadSchemaWithOntology(cept,cept.getName(),PropertyDefinition.PROPERTY_TYPE_CONCEPT);
									
									schemaField = schemaFieldret;
										
								} else {
									throw new Exception("Event paylod schema reference or Element is empty ");
								}
							}
						} else if(childNode.isComplexType) {
							schemaField = createEventPayloadSchemaWithComplexType(childNode);
						} else {
							throw new Exception("Complex Type Content has not supported param type ");
						}
					}
					payload_data_list.add(schemaField);
			}

			String treeNodeName = treeNode.schemaNode.getAttributes().getNamedItem("name").getNodeValue();
			Schema.Field schemaField = new Schema.Field(treeNodeName,Schema.createRecord(treeNodeName + "Properties", "", "", false,payload_data_list) ,"",defaultMapPayloadVal);
			return schemaField;
	}

	@SuppressWarnings("rawtypes")
	private Schema.Field createEventPayloadSchemaWithOntology(Concept cept,String recordName, int conceptType) throws Exception {
			ArrayList<Schema.Field> payload_data_list = new ArrayList();
			HashMap<String,Object> defaultMapPayloadVal  = new  HashMap<String,Object>();

			if (!this.skipBEAttributes) {
				Schema.Field schemaField = null;
				ArrayList<Schema.Field> attr_field_list = new ArrayList<>();

				if(conceptType == PropertyDefinition.PROPERTY_TYPE_CONCEPTREFERENCE) {
					Schema.Field schemaField_ref = new Schema.Field("ref", Schema.create(Type.STRING),"","");
					attr_field_list.add(schemaField_ref);
				} else {
					Schema.Field schemaField_id = new Schema.Field("Id", Schema.create(Type.STRING),"","");
					attr_field_list.add(schemaField_id);
				}

				Schema.Field schemaField_type = new Schema.Field("type", Schema.create(Type.STRING),"","");
				attr_field_list.add(schemaField_type);

				String name = cept.getName() + "Attributes" + "Record";
				
				if(conceptType == PropertyDefinition.PROPERTY_TYPE_CONCEPT) 
					schemaField = new Schema.Field("attributes",Schema.createRecord(name, "", "", false, attr_field_list) ,"",defaultMapVal);
				else {
					HashMap<String,String>defaultMapValRef = new HashMap<String,String>();
					defaultMapValRef.put("ref", "attr");
					defaultMapValRef.put("type", "uri");
					
					schemaField = new Schema.Field("attributes",Schema.createRecord(name, "", "", false, attr_field_list) ,"",defaultMapValRef);
				}
				
				payload_data_list.add(schemaField);
			}

			Collection propDefs = cept.getLocalPropertyDefinitions();
			if(conceptType == PropertyDefinition.PROPERTY_TYPE_CONCEPT) {
			for (Iterator it = propDefs.iterator(); it.hasNext();) {
				PropertyDefinition pd = (PropertyDefinition) it.next();

				Schema.Field schemaField = null;
				String str  = pd.getName();
				switch (pd.getType()) {
				case PropertyDefinition.PROPERTY_TYPE_BOOLEAN:
					schemaField = new Schema.Field(pd.getName(), Schema.create(Type.BOOLEAN),"",true);
					break;
				case PropertyDefinition.PROPERTY_TYPE_INTEGER:
					if(!pd.isArray()) {
					schemaField = new Schema.Field(pd.getName(), Schema.create(Type.INT),"",0);
					} else {
						schemaField = new Schema.Field(pd.getName(), Schema.createArray(Schema.create(Type.INT)),"",new ArrayList<Integer>());
					}
					break;
				case PropertyDefinition.PROPERTY_TYPE_LONG:
					schemaField = new Schema.Field(pd.getName(), Schema.create(Type.LONG),"",0);
					break;
				case PropertyDefinition.PROPERTY_TYPE_REAL:
					schemaField = new Schema.Field(pd.getName(), Schema.create(Type.DOUBLE),"",0);
					break;
				case PropertyDefinition.PROPERTY_TYPE_DATETIME:
					schemaField = new Schema.Field(pd.getName(), Schema.create(Type.STRING),"","");
					break;
				case PropertyDefinition.PROPERTY_TYPE_STRING:
					
					if(!pd.isArray()) {
					schemaField = new Schema.Field(pd.getName(), Schema.create(Type.STRING),"","");
					}
					else {
					schemaField = new Schema.Field(pd.getName(), Schema.createArray(Schema.create(Type.STRING)),"",new ArrayList<String>());
					}
					break;
				case PropertyDefinition.PROPERTY_TYPE_CONCEPT:
					Concept contained = pd.getConceptType();
					Schema.Field schemaFieldRet = createEventPayloadSchemaWithOntology(contained,pd.getName(),PropertyDefinition.PROPERTY_TYPE_CONCEPT);
					
					//Default values for field
					List<Map<String,Object>> arrayDefault = new ArrayList<>();
					HashMap<String,Object> mapDefault = new HashMap<String,Object>();
					for(Field fd : schemaFieldRet.schema().getFields()) {
						mapDefault.put(fd.name(), fd.defaultVal());
					}
					arrayDefault.add(mapDefault);
					
					if (pd.isArray())
						schemaField = new Schema.Field(pd.getName(), Schema.createArray(schemaFieldRet.schema()),"",arrayDefault);
					else
						schemaField = schemaFieldRet;
					break;
				case PropertyDefinition.PROPERTY_TYPE_CONCEPTREFERENCE:
					Concept ceptRef = pd.getConceptType();
					
					Schema.Field schemaFieldRet1 = createEventPayloadSchemaWithOntology(ceptRef,pd.getName(),PropertyDefinition.PROPERTY_TYPE_CONCEPTREFERENCE);
					
					//Default values for field
					List<Map<String,Object>> arrayRefDefault = new ArrayList<>();
					HashMap<String,Object> mapRefDefault = new HashMap<String,Object>();
					for(Field fd : schemaFieldRet1.schema().getFields()) {
						mapRefDefault.put(fd.name(), fd.defaultVal());
					}
					arrayRefDefault.add(mapRefDefault);
					
					if (pd.isArray())
						schemaField = new Schema.Field(pd.getName(), Schema.createArray(schemaFieldRet1.schema()),"",arrayRefDefault);
					else
						schemaField = schemaFieldRet1;
					break;
				}
				payload_data_list.add(schemaField);
			 }
			}
			
			//Default values for field
			for(Field  field : payload_data_list) {
				defaultMapPayloadVal.put(field.name(), field.defaultVal());
			}
			
			String str  = cept.getName() + "Properties";
			//Schema.Field schemaField = new Schema.Field(cept.getName(),Schema.createRecord(cept.getName() + "Properties", "", "", false,payload_data_list) ,"",defaultMapVal);
			Schema.Field schemaField1 = new Schema.Field(recordName,Schema.createRecord(str, "", "", false,payload_data_list) ,"",defaultMapPayloadVal);
						// schemaField = new Schema.Field(cept.getName(),Schema.createRecord("payloadAttributes", "", "", false, Arrays.asList(schemaFieldret)) ,"",dfaultPayloadAttributes);
			return schemaField1;
	}

	private boolean checkDouble(String value) {

		try {
			Double val = Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean checkLong(String value) {

		try {
			Long val = Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean checkInteger(String value) {
		try {
			Integer val = Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean checkBoolean(String value) {
		try {
			boolean val = Boolean.parseBoolean(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	

	public void removeDefaultFieldFromJson(Object object) throws JSONException {
	    if (object instanceof JSONArray) {
	        JSONArray array = (JSONArray) object;
	        for (int i = 0; i < array.length(); ++i) removeDefaultFieldFromJson(array.get(i));
	    } else if (object instanceof JSONObject) {
	        JSONObject json = (JSONObject) object;
	        JSONArray names = json.names();
	        if (names == null) return;
	        for (int i = 0; i < names.length(); i++) {
	            String key = names.getString(i);
	            if (key.equalsIgnoreCase("default") || key.equalsIgnoreCase("doc")) {
	                json.remove(key);
	            } else {
	            	removeDefaultFieldFromJson(json.get(key));
	            }
	        }
	    }
	}
	
	class ElementNode {
		Node schemaNode;
		boolean isComplexType;
		List<ElementNode> list;
		ElementNode(){
			schemaNode =null;
			isComplexType =false;
			list = new ArrayList<ElementNode>();
		}
		ElementNode(Node node){
			schemaNode = node;
			isComplexType =false;
			list = new ArrayList<ElementNode>();
		}
	}
}
