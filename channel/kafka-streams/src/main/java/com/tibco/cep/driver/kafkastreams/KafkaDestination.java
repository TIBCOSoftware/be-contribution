package com.tibco.cep.driver.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.tibco.be.custom.channel.BaseEventSerializer;

import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventContext;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.custom.channel.ExtendedDefaultEventImpl;
import com.tibco.cep.driver.kafka.serializer.KafkaSerializer;
import com.tibco.cep.impl.service.DefaultThreadFactory;
import com.tibco.cep.kernel.model.rule.RuleFunction;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.runtime.service.loader.BEClassLoader;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;

/**
 * An instance of this class represents a kafka destination.
 * 
 * @author moshaikh
 */
public class KafkaDestination extends BaseDestination {

	private Map<String, BEKafkaConsumer> consumers = new HashMap<String, BEKafkaConsumer>();
	private volatile BEKafkaProducer producer;
	private ExecutorService executor;
	private String messageKeyRFUri;
	private Properties channelAndDestProps;
	private KafkaListnerManager manager;

	private ConcurrentHashMap<String, KafkaRequestCallback> reqCallbackMap;

	/**
	 * Initialize the destination. Store a handle to the channel's resource - in
	 * this case the executor which is initialized during Channel.init(), and set
	 * some Kafka destination related configurations.
	 */
	@Override
	public void init() throws Exception {
		this.channelAndDestProps = getChannelAndDestProps();

		this.messageKeyRFUri = channelAndDestProps.getProperty(KafkaProperties.KEY_DESTINATION_MESSAGE_KEY_RF);
		this.messageKeyRFUri = this.messageKeyRFUri == null || this.messageKeyRFUri.trim().isEmpty() ? null
				: this.messageKeyRFUri.trim();
		reqCallbackMap = ((KafkaChannel) this.getChannel()).getKafkaRequestReply().getReqCallbackMap();

		manager = new KafkaListnerManager(channelAndDestProps, this.getChannel().getBeProperties(), reqCallbackMap,
				this, (KafkaChannel) this.getChannel());

	}

	/**
	 * connect() is where we connect the destination to its transport.
	 */
	@Override
	public void connect() throws Exception {
		// really nothing to do here in this implementation.
	}

	@Override
	public void bind(EventProcessor ep) throws Exception {
		int consumerThreads = Integer
				.parseInt(channelAndDestProps.getProperty(KafkaProperties.KEY_DESTINATION_CONSUMER_THREADS, "1"));
		Map<String, BEKafkaConsumer> newConsumers = new HashMap<String, BEKafkaConsumer>();
		for (int i = 0; i < consumerThreads; i++) {
			String clientId = channelAndDestProps.getProperty(KafkaProperties.KEY_DESTINATION_CLIENT_ID);
			if (clientId == null || clientId.trim().isEmpty()) {
				// Use generated clientIds
				clientId = ep.getRuleSessionName() + this.getUri().replace('/', '.') + ".beclient";
			}

			// BE-26500: avoid creating consumer on reconnect(mbean)
			clientId = clientId + "_" + i;
			if (!this.consumers.containsKey(clientId)) {
				BEKafkaConsumer consumer = new BEKafkaConsumer(ep, channelAndDestProps, getChannel().getBeProperties(),
						clientId, this);
				newConsumers.put(clientId, consumer);
			}
		} // Just created the listener(s), not submitted them to executor yet.

		this.consumers.putAll(newConsumers);// Keep account of all the listeners for this destination.
	}

	/**
	 * Start receiving Kafka messages on this destination. A Kafka receiver job is
	 * started. This job runs forever, polling the Kafka endpoint for messages.
	 */
	@Override
	public void start() throws Exception {
		if (!this.consumers.isEmpty() && executor == null) {
			executor = Executors.newFixedThreadPool(this.consumers.size(),
					new DefaultThreadFactory("KafkaConsumer-" + this.getUri()));
			for (BEKafkaConsumer consumer : consumers.values()) {
				this.executor.submit(consumer);
			}
		} else { // BE-26500: channel should be started in suspended mode
			for (BEKafkaConsumer consumer : consumers.values()) {
				getLogger().log(Level.DEBUG,
						"Starting KafkaConsumer (" + consumer.getClientId() + ") for destination - " + this.getUri());
				consumer.resumeConsumer();
			}
		}
		this.suspended = false;
		getLogger().log(Level.INFO,
				"Kafka destination started - " + this.getUri() + ", Serializer:" + this.getSerializer().getClass());
		// Producer would be lazily initialized.
	}

	/**
	 * Stop the consumers.
	 */
	@Override
	public void close() throws Exception {
		getLogger().log(Level.INFO, "Closing Kafka destination - " + this.getUri());
		// Consumers shutdown
		for (BEKafkaConsumer consumer : this.consumers.values()) {
			consumer.stopConsumer();
		}
		if (executor != null) {
			executor.shutdown();
		}

		// Producer shutdown
		if (producer != null) {
			producer.close();
		}

		// Check all consumers are actually stopped.
		if (executor != null && !executor.awaitTermination(30, TimeUnit.SECONDS)) {
			getLogger().log(Level.WARN,
					"Kafka consumers ExecutorService taking too long to shutdown, forcing shutdown.");
			executor.shutdownNow();
		}

		if (null != manager) {
			manager.shutdown();
		}
		getLogger().log(Level.INFO, "Kafka destination closed - " + this.getUri());
	}

	/**
	 * Send the event
	 * 
	 */
	@Override
	public void send(EventWithId event, Map overrideData) throws Exception {
		if (messageKeyRFUri != null) {
			Object key = invokeRF(getRuleFunctionInstance(messageKeyRFUri), event);
			if (key != null) {
				event.setProperty(KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY, key);
			}
		}
		getProducer().send(event, overrideData);
	}

	@Override
	public Event requestEvent(Event outevent, String responseEventURI, BaseEventSerializer serializer, long timeout,
			Map overrideData) throws Exception {
		// Request-reply paradigm not applicable for Kafka
		if (manager.isListenerManagerStarted() != Boolean.TRUE) {
			manager.start();
		}

		String CorrelationID = UUID.randomUUID().toString();
		String str = ((KafkaChannel) this.getChannel()).getKafkaRequestReply().getReplyTopic();

		if (CorrelationID != null) {
			Object key = invokeRF(getRuleFunctionInstance(CorrelationID), (EventWithId) outevent);
			if (key != null) {
				outevent.setProperty(KafkaProperties.RESERVED_EVENT_PROP_MESSAGE_KEY, key);
			}
		}

		if (null == overrideData)
			overrideData = new HashMap<>();

		overrideData.put(KafkaProperties.INTERNAL_PROP_KEY_CORRELATION_ID, CorrelationID);
		overrideData.put(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC, str);

		final Event[] receivedEvt = new Event[2];

		Object lock = new Object();

		Map<String, Object> serializationPropertise = null;

		if (consumers.size() > 0)
			serializationPropertise = consumers.get(consumers.keySet().toArray()[0]).getSerializationProperties();

		((KafkaChannel) this.getChannel()).getKafkaRequestReply().getResponseFromReplyTopic(receivedEvt, lock,
				CorrelationID, serializationPropertise, this.getSerializer());

		getProducer().send((EventWithId) outevent, overrideData);

		synchronized (lock) {
			if (timeout == -1) {
				lock.wait();
			} else {
				lock.wait(timeout);
			}
		}

		if (reqCallbackMap.containsKey(CorrelationID)) {
			getLogger().log(Level.DEBUG, "TimeOut for the response ");
			reqCallbackMap.remove(CorrelationID);
		}

		return receivedEvt[0];
	}

	/**
	 * Initializes Consumer props
	 */
	private Properties getChannelAndDestProps() {
		Properties props = new Properties();
		for (Entry<Object, Object> entry : getChannel().getChannelProperties().entrySet()) {
			if (entry.getValue() != null) {
				props.put(entry.getKey(), getChannel().getGlobalVariableValue((String) entry.getValue()).toString());
			}
		}
		for (Entry<Object, Object> entry : getDestinationProperties().entrySet()) {
			if (entry.getValue() != null) {
				props.put(entry.getKey(), getChannel().getGlobalVariableValue((String) entry.getValue()).toString());
			}
		}

		props.put(KafkaProperties.INTERNAL_PROP_KEY_CHANNEL_URI, getChannel().getUri());
		props.put(KafkaProperties.INTERNAL_PROP_KEY_DESTINATION_URI, this.getUri());

		if (this.getSerializer() instanceof KafkaSerializer) {
			props.put(KafkaProperties.INTERNAL_PROP_KEY_KEY_SERIALIZER,
					((KafkaSerializer) this.getSerializer()).keySerializer());
			props.put(KafkaProperties.INTERNAL_PROP_KEY_VALUE_SERIALIZER,
					((KafkaSerializer) this.getSerializer()).valueSerializer());

			props.put(KafkaProperties.INTERNAL_PROP_KEY_KEY_DESERIALIZER,
					((KafkaSerializer) this.getSerializer()).keyDeserializer());
			props.put(KafkaProperties.INTERNAL_PROP_KEY_VALUE_DESERIALIZER,
					((KafkaSerializer) this.getSerializer()).valueDeserializer());
		} else {// defaults
			props.put(KafkaProperties.INTERNAL_PROP_KEY_KEY_SERIALIZER,
					"org.apache.kafka.common.serialization.StringSerializer");
			props.put(KafkaProperties.INTERNAL_PROP_KEY_VALUE_SERIALIZER,
					"org.apache.kafka.common.serialization.ByteArraySerializer");

			props.put(KafkaProperties.INTERNAL_PROP_KEY_KEY_DESERIALIZER,
					"org.apache.kafka.common.serialization.StringDeserializer");
			props.put(KafkaProperties.INTERNAL_PROP_KEY_VALUE_DESERIALIZER,
					"org.apache.kafka.common.serialization.ByteArrayDeserializer");
		}

		return props;
	}

	BEKafkaProducer getProducer() throws Exception {
		if (producer == null) {
			synchronized (this) {
				if (producer == null) {
					getLogger().log(Level.INFO, "Creating KafkaProducer for destination - " + this.getUri());
					producer = new BEKafkaProducer(getSerializer(), channelAndDestProps,
							this.getChannel().getBeProperties(), getLogger());
				}
			}
		}
		return producer;
	}

	@Override
	public void suspend() {
		synchronized (this) {
			this.suspended = true;
			for (BEKafkaConsumer consumer : this.consumers.values()) {
				consumer.suspendConsumer();
			}
			this.getLogger().log(Level.INFO, "Destination Suspended : " + getUri());
		}
	}

	@Override
	public void resume() {
		synchronized (this) {
			this.suspended = false;
			for (BEKafkaConsumer consumer : this.consumers.values()) {
				consumer.resumeConsumer();
			}
			this.getLogger().log(Level.INFO, "Destination Resumed : " + getUri());
		}
	}

	@Override
	public EventContext getEventContext(Event event) {
		return new KafkaMessageContext(event, ((KafkaEvent) event).getBEKafkaConsumer());
	}

	/**
	 * Invokes the RuleFunction and returns the value received on invoking the RF.
	 * 
	 * @param rf
	 * @param event
	 * @return
	 * @throws Exception
	 */
	private Object invokeRF(RuleFunction rf, EventWithId event) {
		if (rf == null) {
			return null;
		}
		if (event instanceof ExtendedDefaultEventImpl) {
			return rf.invoke(new Object[] { ((ExtendedDefaultEventImpl) event).getUnderlyingSimpleEvent() });
		}
		return null;
	}

	private RuleFunction getRuleFunctionInstance(String rfUri) throws Exception {
		try {
			RuleSession session = RuleSessionManager.getCurrentRuleSession();
			if (session == null) {
				session = RuleServiceProviderManager.getInstance().getDefaultProvider().getRuleRuntime()
						.getRuleSessions()[0];
			}
			RuleFunction rfi = ((BEClassLoader) session.getRuleServiceProvider().getTypeManager())
					.getRuleFunctionInstance(rfUri);
			return rfi;
		} catch (Exception e) {
			getLogger().log(Level.ERROR, e, "Failed to get instance of RuleFunction " + rfUri);
			throw new Exception("Failed to get instance of RuleFunction " + rfUri, e);
		}
	}
}
