/**
 * 
 */
package com.tibco.cep.driver.kafkastreams;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.Topology.AutoOffsetReset;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.model.rdf.RDFTypes;
import com.tibco.cep.driver.kafka.KafkaPropertiesHelper;
import com.tibco.cep.kernel.model.rule.RuleFunction;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.TypeManager;
import com.tibco.cep.runtime.service.loader.BEClassLoader;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;

/**
 * @author shivkumarchelwa
 *
 */
public class BEKafkaStreams {

	private String applicationId;

	private EventProcessor eventProcessor;
	private KafkaStreamsDestination ownerDestination;
	private Properties channelAndDestProps;
	private RuleSession ruleSession;
	private RuleServiceProvider rsp;

	private Consumed consumedWith;
	private KStream kStream;
	private KTable kTable;
	private KGroupedStream kGroupedStream;

	private Topology topology;
	private StreamsBuilder streamBuilder;
	private KafkaStreams kafkaStreams;

	public BEKafkaStreams(EventProcessor eventProcesor, KafkaStreamsDestination ownerDestination,
			Properties channelAndDestProps) {
		this.eventProcessor = eventProcesor;
		this.ownerDestination = ownerDestination;
		this.channelAndDestProps = channelAndDestProps;
		this.ruleSession = (RuleSession) ownerDestination.getRuleSession();
		this.rsp = ((RuleSession) ownerDestination.getRuleSession()).getRuleServiceProvider();
	}

	public void init() throws Exception {

		streamBuilder = new StreamsBuilder();
		applicationId = channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_APPLICATION_ID);

		buildStreamsTopology();
	}

	public String getApplicationId() {
		return applicationId;
	}

	private Properties initStreamsProperties() {

		Properties streamsProperties = new Properties();

		streamsProperties.put(StreamsConfig.APPLICATION_ID_CONFIG,
				channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_APPLICATION_ID));
		streamsProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
				channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_CHANNEL_BOOTSTRAP_SERVER));

		streamsProperties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG,
				channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_PROCESSING_GUARANTEE));

		KafkaPropertiesHelper.loadOverridenProperties(KafkaStreamsProperties.PROPERTY_KEY_KAFKA_STREAMS_PROPERTY_PREFIX,
				ownerDestination.getChannel().getBeProperties(), streamsProperties,
				ownerDestination.getChannel().getUri(), ownerDestination.getUri());

		String keySerdes = channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_KEY_SERDES);
		String valueSerdes = channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_VALUE_SERDES);

		streamsProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, getSerde(keySerdes).getClass());
		streamsProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, getSerde(valueSerdes).getClass());

		KafkaPropertiesHelper.initSslProperties(channelAndDestProps, streamsProperties);

		return streamsProperties;
	}

	private Topology buildStreamsTopology() throws JsonParseException, JsonMappingException, IOException {

		// At present, create a KStreams. Later based on input, create KTable or
		// KGlobalTable
		createKStream();

		String topologyJson = channelAndDestProps
				.getProperty(KafkaStreamsProperties.KEY_DESTINATION_PROCESSOR_TOPOLOGY);
		if (topologyJson != null && !topologyJson.isEmpty()) {

			ObjectMapper objMapper = new ObjectMapper();
			BETopologyModel topologyModel = objMapper.readValue(topologyJson, BETopologyModel.class);

			for (BETransformationModel transformation : topologyModel.getTransformations()) {
				addStreamProcessor(transformation);
			}
			if (kTable != null) {
				kStream = kTable.toStream();
			}
		}

		kStream.foreach(new ForEachRecordProcessor(eventProcessor, ownerDestination, this));
		topology = streamBuilder.build();

		ownerDestination.getLogger().log(Level.INFO, topology.describe().toString());

		return topology;
	}

	private KStream createKStream() {
		String topicName = channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_TOPIC_NAME);
		boolean isRegex = "true"
				.equals(channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_IS_REGEX_PATTERN));

		String keySerdes = channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_KEY_SERDES);
		String valueSerdes = channelAndDestProps.getProperty(KafkaStreamsProperties.KEY_DESTINATION_VALUE_SERDES);
		String autoOffsetReset = channelAndDestProps
				.getProperty(KafkaStreamsProperties.KEY_DESTINATION_AUTO_OFFSET_RESET);

		consumedWith = Consumed.with(BEKafkaStreams.getSerde(keySerdes), BEKafkaStreams.getSerde(valueSerdes))
				.withOffsetResetPolicy(AutoOffsetReset.valueOf(autoOffsetReset.toUpperCase()));

		if (isRegex) {
			Pattern pattern = Pattern.compile(topicName);
			kStream = streamBuilder.stream(pattern, consumedWith);
		} else {
			kStream = streamBuilder.stream(topicName, consumedWith);
		}
		return kStream;
	}

	private void addStreamProcessor(BETransformationModel t) {
		Map<String, Object> inputs = t.getInputs();
		String transformationType = t.getType();
		switch (transformationType) {
		case BEKafkaStreamsConstants.KSTREAM_FILTER:
			String rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_PREDICATE).toString();
			RuleFunction rf = getRuleFunctionInstance(rfUri);
			kStream = kStream.filter((key, value) -> (boolean) invokeRF(rf, new Object[] { key, value }));
			kGroupedStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KSTREAM_FILTER_NOT:
			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_PREDICATE).toString();
			rf = getRuleFunctionInstance(rfUri);
			kStream = kStream.filterNot((key, value) -> (boolean) invokeRF(rf, new Object[] { key, value }));
			kGroupedStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KSTREAM_MAP_VALUES:
			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_VALUE_MAPPER).toString();
			rf = getRuleFunctionInstance(rfUri);
			RuleFunction.ParameterDescriptor[] params = rf.getParameterDescriptors();
			if (params.length == 3) {
				kStream = kStream.mapValues(
						(key, value) -> invokeRF(getRuleFunctionInstance(rfUri), new Object[] { key, value }));
			} else {
				kStream = kStream
						.mapValues((value) -> invokeRF(getRuleFunctionInstance(rfUri), new Object[] { value }));
			}
			kGroupedStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KSTREAM_FLAT_MAP_VALUES:
			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_VALUE_MAPPER).toString();
			rf = getRuleFunctionInstance(rfUri);
			params = rf.getParameterDescriptors();
			if (params.length == 3) {
				kStream = kStream
						.flatMapValues((key, value) -> (Iterable<Object>) invokeRF(getRuleFunctionInstance(rfUri),
								new Object[] { key, value }));
			} else {
				kStream = kStream.flatMapValues(
						(value) -> (Iterable<Object>) invokeRF(getRuleFunctionInstance(rfUri), new Object[] { value }));
			}
			kGroupedStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KSTREAM_SELECT_KEY:
			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_KEY_VALUE_MAPPER).toString();
			rf = getRuleFunctionInstance(rfUri);
			kStream = kStream
					.selectKey((key, value) -> invokeRF(getRuleFunctionInstance(rfUri), new Object[] { key, value }));
			kGroupedStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KSTREAM_GROUP_BY_KEY:
			kGroupedStream = kStream.groupByKey();
			kStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KGROUPEDSTREAM_COUNT:
			kTable = kGroupedStream.count();
			kGroupedStream = null;
			kStream = null;
			break;
		case BEKafkaStreamsConstants.KGROUPEDSTREAM_AGGREGATE:
			String initializerRfUri = inputs.get(BEKafkaStreamsConstants.INPUT_INITIALIZER).toString();
			String aggregatorRfUri = inputs.get(BEKafkaStreamsConstants.INPUT_AGGREGATOR).toString();
			RuleFunction initializerRf = getRuleFunctionInstance(initializerRfUri);
			RuleFunction aggregatorRf = getRuleFunctionInstance(aggregatorRfUri);
			kTable = kGroupedStream.aggregate(() -> invokeRF(initializerRf, new Object[] {}),
					(key, value, aggregate) -> invokeRF(aggregatorRf, new Object[] { key, value, aggregate }));

			kGroupedStream = null;
			kStream = null;
			break;
		case BEKafkaStreamsConstants.KGROUPEDSTREAM_REDUCE:
			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_REDUCER).toString();
			rf = getRuleFunctionInstance(rfUri);
			kTable = kGroupedStream.reduce((value1, value2) -> invokeRF(rf, new Object[] { value1, value2 }));
			kGroupedStream = null;
			kStream = null;
			break;
		case BEKafkaStreamsConstants.KSTREAM_INNER_JOIN:
		case BEKafkaStreamsConstants.KSTREAM_LEFT_JOIN:
		case BEKafkaStreamsConstants.KSTREAM_OUTER_JOIN:
			String joinTopic = inputs.get(BEKafkaStreamsConstants.INPUT_JOIN_TOPIC).toString();
			joinTopic = ownerDestination.getChannel().getGlobalVariableValue(joinTopic).toString();
			KStream otherStream = streamBuilder.stream(joinTopic);

			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_VALUE_JOINER).toString();
			rf = getRuleFunctionInstance(rfUri);

			JoinWindows joinWindows = buildJoinWindows(inputs);

			if (BEKafkaStreamsConstants.KSTREAM_LEFT_JOIN.equals(transformationType)) {
				kStream = kStream.leftJoin(otherStream, (key, value) -> invokeRF(rf, new Object[] { key, value }),
						joinWindows);
			} else if (BEKafkaStreamsConstants.KSTREAM_OUTER_JOIN.equals(transformationType)) {
				kStream = kStream.outerJoin(otherStream, (key, value) -> invokeRF(rf, new Object[] { key, value }),
						joinWindows);
			} else {
				kStream = kStream.join(otherStream, (key, value) -> invokeRF(rf, new Object[] { key, value }),
						joinWindows);
			}
			kGroupedStream = null;
			kTable = null;
			break;
		case BEKafkaStreamsConstants.KTABLE_TO_STREAM:
			rfUri = inputs.get(BEKafkaStreamsConstants.INPUT_KEY_VALUE_MAPPER) != null
					? inputs.get(BEKafkaStreamsConstants.INPUT_KEY_VALUE_MAPPER).toString()
					: null;
			if (rfUri != null && !rfUri.isEmpty()) {
				rf = getRuleFunctionInstance(rfUri);
				kStream = kTable.toStream((key, value) -> invokeRF(rf, new Object[] { key, value }));
			} else {
				kStream = kTable.toStream();
			}
			kGroupedStream = null;
			kTable = null;
			break;
		default:
			break;
		}
	}

	/*
	 * A workaround of Long.value(String.valueOf(object)) is used to convert
	 * possible integer during deserialization
	 */
	private JoinWindows buildJoinWindows(Map<String, Object> inputs) {

		Map<String, Object> joinDurations = (Map<String, Object>) inputs.get(BEKafkaStreamsConstants.INPUT_JOIN_WINDOWS);
		Map<String, String> joinDurationUnits = (Map<String, String>) inputs
				.get(BEKafkaStreamsConstants.INPUT_JOIN_WINDOW_UNITS);

		String windowString = String.valueOf(joinDurations.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_WINDOW));
		long window = Long.valueOf(ownerDestination.getChannel().getGlobalVariableValue(windowString).toString());
		Duration windowDuration = convertToMillis(window,
				joinDurationUnits.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_WINDOW));
		JoinWindows joinWindow = JoinWindows.of(windowDuration);

		String afterValue = String.valueOf(joinDurations.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_AFTER));
		long after = Long.valueOf(ownerDestination.getChannel().getGlobalVariableValue(afterValue).toString());
		if (after > 0) {
			Duration duration = convertToMillis(after,
					joinDurationUnits.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_AFTER));
			joinWindow.after(duration);
		}

		String beforeValue = String.valueOf(joinDurations.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_BEFORE));
		long before = Long.valueOf(ownerDestination.getChannel().getGlobalVariableValue(beforeValue).toString());
		if (before > 0) {
			Duration duration = convertToMillis(before,
					joinDurationUnits.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_BEFORE));
			joinWindow.before(duration);
		}

		String graceValue = String.valueOf(joinDurations.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_GRACE));
		long grace = Long.valueOf(ownerDestination.getChannel().getGlobalVariableValue(graceValue).toString());
		if (grace > 0) {
			Duration duration = convertToMillis(grace,
					joinDurationUnits.get(BEKafkaStreamsConstants.INPUT_JOINWINDOWS_GRACE));
			joinWindow.grace(duration);
		}

		return joinWindow;
	}

	public void start() {
		// The start function is expected to be called only once during the life cycle
		// of KafkaStreams
		if (this.kafkaStreams == null) {
			Properties props = initStreamsProperties();
			this.kafkaStreams = new KafkaStreams(topology, props);
		}
		if (this.kafkaStreams.state().equals(KafkaStreams.State.CREATED)
				|| this.kafkaStreams.state().equals(KafkaStreams.State.NOT_RUNNING)) {
			this.kafkaStreams.start();
		}
	}

	/**
	 * In case of Kafka Streams, api does not support suspend. A suspend-resume is
	 * same as close-start. As BE suspends a custom channel before closing, this
	 * method may get called twice.
	 */
	public boolean close() {
		if (this.kafkaStreams == null) {
			// destination is already closed
			return true;
		}

		boolean success = this.kafkaStreams.close(Duration.ofSeconds(30));
		if (success) {
			this.kafkaStreams = null;
		}
		return success;
	}

	private Object invokeRF(RuleFunction rf, Object[] params) {
		if (rf == null) {
			return null;
		}
		RuleSessionManager.currentRuleSessions.set(ruleSession);
		return rf.invoke(params);
	}

	private RuleFunction getRuleFunctionInstance(String rfUri) {
		TypeManager typeManager = rsp.getTypeManager();
		RuleFunction rf = ((BEClassLoader) typeManager).getRuleFunctionInstance(rfUri);
		return rf;
	}

	public static Serde<?> getSerde(String serdeType) {

		if (RDFTypes.STRING.getName().equalsIgnoreCase(serdeType)) {
			return Serdes.String();
		} else if (RDFTypes.DOUBLE.getName().equalsIgnoreCase(serdeType)) {
			return Serdes.Double();
		} else if (RDFTypes.INTEGER.getName().equalsIgnoreCase(serdeType)) {
			return Serdes.Integer();
		} else if (RDFTypes.LONG.getName().equalsIgnoreCase(serdeType)) {
			return Serdes.Long();
		} else if (BEKafkaStreamsConstants.KEY_VALUE_SERDES_TYPE_BYTEARRAY.equalsIgnoreCase(serdeType)) {
			return Serdes.ByteArray();
		}

		return Serdes.String();
	}

	private Duration convertToMillis(long duration, String durationType) {
		switch (durationType) {
		case BEKafkaStreamsConstants.DURATION_UNIT_NANOS:
			return Duration.ofNanos(duration);
		case BEKafkaStreamsConstants.DURATION_UNIT_MILLIS:
			return Duration.ofMillis(duration);
		case BEKafkaStreamsConstants.DURATION_UNIT_SECONDS:
			return Duration.ofSeconds(duration);
		case BEKafkaStreamsConstants.DURATION_UNIT_MINUTES:
			return Duration.ofMinutes(duration);
		case BEKafkaStreamsConstants.DURATION_UNIT_HOURS:
			return Duration.ofHours(duration);
		case BEKafkaStreamsConstants.DURATION_UNIT_DAYS:
			return Duration.ofDays(duration);
		default:
			return Duration.ofMillis(duration);
		}
	}

	class ForEachRecordProcessor implements ForeachAction<Object, Object> {

		EventProcessor eventProcessor;
		BEKafkaStreams beKafkaStreams;
		private final BaseEventSerializer serializer;
		private final Logger logger;
		private KafkaStreamsDestination ownerDestination;
		private final Map<String, Object> serializationProperties;

		/**
		 * 
		 */
		public ForEachRecordProcessor(EventProcessor eventProcessor, KafkaStreamsDestination ownerDestination,
				BEKafkaStreams beKafkaConsumer) {
			this.eventProcessor = eventProcessor;
			this.beKafkaStreams = beKafkaConsumer;
			this.ownerDestination = ownerDestination;
			this.serializer = ownerDestination.getSerializer();
			this.logger = ownerDestination.getLogger();

			this.serializationProperties = new HashMap<String, Object>();
			boolean includeEventType = isIncludeEventTypeWhileDeserialize(ownerDestination.getDestinationProperties()
					.getProperty(KafkaStreamsProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, "ALWAYS"));
			this.serializationProperties.put(KafkaStreamsProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE,
					includeEventType);
		}

		private boolean isIncludeEventTypeWhileDeserialize(String includeEventTypeValue) {
			return "ALWAYS".equalsIgnoreCase(includeEventTypeValue)
					|| "ON_DESERIALIZE".equalsIgnoreCase(includeEventTypeValue);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.apache.kafka.streams.kstream.ForeachAction#apply(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public void apply(Object key, Object value) {
			try {
				logger.log(Level.DEBUG, "Received message on " + beKafkaStreams.getApplicationId() + "\nKey: " + key
						+ "\nValue:" + value);

				KeyValue<Object, Object> kvPair = KeyValue.pair(key, value);
				Event e = serializer.deserializeUserEvent(kvPair, serializationProperties);
				if (e != null) {
					eventProcessor.processEvent(e);
				} else {
					logger.log(Level.ERROR, "Deserializer returned null event, destination:" + ownerDestination.getUri()
							+ " record-key:" + key);
				}
			} catch (Exception e) {
				logger.log(Level.ERROR, e, "Error while processing event.");
			}
		}
	}
}
