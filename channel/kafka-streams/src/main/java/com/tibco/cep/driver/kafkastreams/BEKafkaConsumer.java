package com.tibco.cep.driver.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * Each instance of this Runnable class encloses a KafkaConsumer that reads
 * messages from one or more partitions and submits the deserialized events for
 * processing.
 * 
 * @author moshaikh
 */
public class BEKafkaConsumer implements Runnable {

	private final String topic;
	private final EventProcessor eventProcessor;
	private final BaseEventSerializer serializer;
	private final int pollInterval;
	private final boolean autoCommitEnabled;
	private final String clientId;
	private Properties consumerProperties;
	private ConsumerState requestedState;
	private KafkaDestination ownerDestination;

	private final Map<String, Object> serializationProperties;
	private final KafkaConsumer consumer;
	private final Logger logger;
	private boolean currentEventConsumed;

	private enum ConsumerState {
		ACTIVE, SUSPENDED, STOPPED
	}

	public KafkaConsumer getConsumer() {
		return consumer;
	}

	public KafkaDestination getOwnerDestination() {
		return ownerDestination;
	}

	public BaseEventSerializer getSerializer() {
		return serializer;
	}

	public Map<String, Object> getSerializationProperties() {
		return serializationProperties;
	}

	public Logger getLogger() {
		return logger;
	}

	/**
	 * 
	 * @param eventProcessor
	 * @param ser
	 * @param destinationProps
	 * @param consumerNum
	 * @param logger
	 * @throws Exception
	 */
	public BEKafkaConsumer(EventProcessor eventProcessor, Properties destinationProps, Properties beProperties,
			String clientId, KafkaDestination ownerDestination) throws Exception {
		this.ownerDestination = ownerDestination;
		this.logger = ownerDestination.getLogger();
		this.topic = destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_TOPIC_NAME);
		this.requestedState = ConsumerState.SUSPENDED;
		this.eventProcessor = eventProcessor;
		this.serializer = ownerDestination.getSerializer();
		this.clientId = clientId;
		this.pollInterval = Integer
				.parseInt(destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_POLL_INTERVAL, "1000"));
		
		this.autoCommitEnabled = Boolean
				.parseBoolean(destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_ENABLE_AUTOCOMMIT));
		initConsumerProperties(destinationProps, beProperties);

		this.serializationProperties = new HashMap<String, Object>();
		boolean includeEventType = BEKafkaProducer.isIncludeEventTypeWhileDeserialize(
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, "ALWAYS"));
		this.serializationProperties.put(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, includeEventType);

		consumer = new KafkaConsumer(consumerProperties);
		
	}

	private void initConsumerProperties(Properties destinationProps, Properties beProperties) throws Exception {
		consumerProperties = new Properties();

		// Kafka consumer configuration settings
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_BOOTSTRAP_SERVER), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.GROUP_ID_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_GROUP_ID), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommitEnabled,
				consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_AUTOCOMMIT_INTERVAL), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_HEARTBEAT_INTERVAL), consumerProperties);
		// On consumer down waits for this timeout before reassigning partitions amongst
		// available consumers.
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_SESSION_TIMEOUT), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_KEY_DESERIALIZER), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_VALUE_DESERIALIZER), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.CLIENT_ID_CONFIG, clientId, consumerProperties);

		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_AUTO_OFFSET_RESET), consumerProperties);
		
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_MAX_POLL_INTERVAL), consumerProperties);
		
		if(serializer instanceof com.tibco.cep.driver.kafka.serializer.KafkaAvroSerializer)
			loadSchemaRegistrationProperties(destinationProps,beProperties);
		
		KafkaPropertiesHelper.initSslProperties(destinationProps, consumerProperties);
		
		
		String channelUri = destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_CHANNEL_URI);
		String destinationUri = destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_DESTINATION_URI);

		KafkaPropertiesHelper.loadOverridenProperties(KafkaProperties.PROPERTY_KEY_KAFKA_CLIENT_PROPERTY_PREFIX,
				beProperties, consumerProperties, channelUri, destinationUri);

		logger.log(Level.INFO,
				"Initializing KafkaConsumer for Destination '%s' with properties - TopicName:%s, GroupID:%s, ClientID:%s, HeartBeat Interval:%s,"
						+ " SessionTimeout:%s, AutoCommit:%s, AutoCommit Interval:%s",
				destinationUri, topic, consumerProperties.get(ConsumerConfig.GROUP_ID_CONFIG),
				consumerProperties.get(ConsumerConfig.CLIENT_ID_CONFIG),
				consumerProperties.get(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG),
				consumerProperties.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG),
				consumerProperties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG),
				consumerProperties.get(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
	}
	
	public void loadSchemaRegistrationProperties(Properties destinationProps, Properties beProperties) throws Exception{
		
		String schemaRegistryPlatform = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_PLATFORM);
		if(schemaRegistryPlatform.equals(KafkaProperties.CONFLUENT_PLATFORM))
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				KafkaProperties.CONFLUENT_SCHEMA_REGISTRY_DESERIALIZER, consumerProperties);
		else {
			KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
					KafkaProperties.TIBCO_SCHEMA_REGISTRY_DESERIALIZER, consumerProperties);
			consumerProperties.setProperty("ftl.realmservers", destinationProps.getProperty("ftl.real.server"));
		}
		
		KafkaPropertiesHelper.initSchemaReistryAutheticationProperties(destinationProps,consumerProperties);
	}

	@Override
	public void run() {
		subscribe();

		while (true) {
			if (requestedState == ConsumerState.SUSPENDED) {
				try {
					Thread.sleep(1000);// TODO: make configurable
				} catch (InterruptedException e) {
					logger.log(Level.ERROR, e, e.getMessage());
				}
				continue;
			} else if (requestedState == ConsumerState.STOPPED) {
				logger.log(Level.INFO, "Stopping KafkaConsumer - " + this);
				consumer.unsubscribe();
				consumer.close();
				return;
			}
		
			Duration timeoutMs = Duration.ofMillis(this.pollInterval);
			ConsumerRecords records = consumer.poll(timeoutMs);
					
			for (Object record : records) {
				try {
					ConsumerRecord consumerRecord = (ConsumerRecord) record;
					logger.log(Level.DEBUG, "Received message on " + this + "\nKey: " + consumerRecord.key()
							+ "\nValue:" + consumerRecord.value());

					if (autoCommitEnabled) {
						processMsgWithAutoCommit(consumerRecord);
					} else {
						processMsgWithExplicitCommit(consumerRecord);
					}
				} catch (Exception ex) {
					logger.log(Level.ERROR, ex, "Error while processing event.:" + ex.getMessage());
				}
			}
		}
	}
	
	/**
	 * This method processes the event and returns without waiting checking whether
	 * event got acknowledged/consumer or rolled back.
	 * 
	 * @param consumerRecord
	 * @throws Exception
	 */
	private void processMsgWithAutoCommit(ConsumerRecord consumerRecord) throws Exception {
		Event e = deserializeMessage(consumerRecord);
		if (e != null) {
			((KafkaEvent) e).setBEKafkaConsumer(this);
			eventProcessor.processEvent(e);
		}
	}

	/**
	 * This method processes the event redelivering it configured number of times in
	 * case of rollbacks.
	 * 
	 * @param consumerRecord
	 * @throws Exception
	 */
	private void processMsgWithExplicitCommit(ConsumerRecord consumerRecord) throws Exception {
		this.currentEventConsumed = false;
		int attempts = Integer.getInteger(KafkaProperties.PROPERTY_KEY_KAFKA_PROCESS_EVENT_MAX_ATTEMPTS,
				Integer.MAX_VALUE);
		if (attempts == 0) {
			throw new Exception("Invalid value '" + attempts + "' for property '"
					+ KafkaProperties.PROPERTY_KEY_KAFKA_PROCESS_EVENT_MAX_ATTEMPTS + "'");
		} else if (attempts < 0) {
			attempts = Integer.MAX_VALUE;
		}
		while (attempts-- > 0) {
			Event e = deserializeMessage(consumerRecord);
			if (e == null) {
				return;
			} else {
				((KafkaEvent) e).setBEKafkaConsumer(this);
				
				if(!this.currentEventConsumed)
				eventProcessor.processEvent(e);
				
				if (this.currentEventConsumed) {
					  //this.consumer.commitSync();// Commit offset and return
					  Map<TopicPartition, OffsetAndMetadata> partitionAndOffset = new HashMap<>();
					  partitionAndOffset.put(new TopicPartition(consumerRecord.topic(), consumerRecord.partition()), new OffsetAndMetadata(consumerRecord.offset()+1));
					  this.consumer.commitSync(partitionAndOffset);
					return;
				} else {// Event was not consumed try to process it again after a delay.
					if (attempts > 0) {// do not wait if this is the last execution of loop.
						Thread.sleep(Integer
								.getInteger(KafkaProperties.PROPERTY_KEY_KAFKA_PROCESS_EVENT_ATTEMPTS_INTERVAL, 1000));
					}
				}
			}
		}
	}

	private Event deserializeMessage(ConsumerRecord consumerRecord) throws Exception {
		Event event = null;
		try {
			event = serializer.deserializeUserEvent(consumerRecord, serializationProperties);
			((KafkaEvent) event).setConsumerRecord(consumerRecord);
			if (consumerRecord.key() != null) {
				event.setProperty(KafkaProperties.EVENT_FIELD_MSG_KEY, consumerRecord.key().toString());
			}
			event.setProperty(KafkaProperties.EVENT_FIELD_MSG_OFFSET, consumerRecord.offset());
			event.setProperty(KafkaProperties.EVENT_FIELD_MSG_PARTITION, consumerRecord.partition());
			event.setProperty(KafkaProperties.EVENT_FIELD_MSG_TIMESTAMP, consumerRecord.timestamp());
			if (consumerRecord.timestampType() != null) {
				event.setProperty(KafkaProperties.EVENT_FIELD_MSG_TIMESTAMPTYPE, consumerRecord.timestampType().name);
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, "Failed to deserialize message, destination:" + ownerDestination.getUri()
					+ " message-key:" + consumerRecord.key() + " error:" + e.getMessage());
			KafkaErrorEndpointHelper.forwardMessageToErrorEndpoint(ownerDestination, consumerRecord);
			return null;
		}
		if (event == null) {
			logger.log(Level.ERROR, "Deserializer returned null event, destination:" + ownerDestination.getUri()
					+ " message-key:" + consumerRecord.key());
			KafkaErrorEndpointHelper.forwardMessageToErrorEndpoint(ownerDestination, consumerRecord);
		}
		
		return event;
	}

	private void subscribe() {
		consumer.subscribe(Arrays.asList(this.topic));
		logger.log(Level.DEBUG, "Subscribed to topic " + this.topic);
	}

	public void stopConsumer() {
		suspendConsumer();
		requestedState = ConsumerState.STOPPED;
	}

	public void suspendConsumer() {
		this.requestedState = ConsumerState.SUSPENDED;
	}

	public void resumeConsumer() {
		this.requestedState = ConsumerState.ACTIVE;
	}

	public String getClientId() {
		return consumerProperties.getProperty(ConsumerConfig.CLIENT_ID_CONFIG);
	}

	public void setCurrentEventAcked() {
		this.currentEventConsumed = true;
	}

	public void setCurrentEventRolledback() {
		this.currentEventConsumed = false;
	}

	@Override
	public String toString() {
		return "ClientId:" + consumerProperties.getProperty(ConsumerConfig.CLIENT_ID_CONFIG);
	}

}
