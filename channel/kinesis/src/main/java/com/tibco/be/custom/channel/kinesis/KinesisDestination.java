package com.tibco.be.custom.channel.kinesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.ListStreamsRequest;
import com.amazonaws.services.kinesis.model.ListStreamsResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventContext;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.impl.service.DefaultThreadFactory;
import com.tibco.cep.kernel.service.logging.Level;

public class KinesisDestination extends BaseDestination {

	private BEKinesisConsumer consumer;
	private ExecutorService executor;

	private Map<String, Object> serializationProperties;
	private AmazonKinesis kinesisClient;

	@Override
	public void init() throws Exception {
		serializationProperties = new HashMap<String, Object>();
		serializationProperties.put(KinesisProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, true);
	}

	@Override
	public void bind(EventProcessor ep) throws Exception {
		consumer = new BEKinesisConsumer(this);
	}

	/**
	 * Start receiving Kinesis messages on this destination. A Kinesis receiver
	 * job is started.
	 */
	@Override
	public void start() throws Exception {
		
		this.createClient();
		if(!this.isStreamExisiting()) {
			getLogger().log(Level.DEBUG, "Kinesis Data Stream not found. - " + this.getUri());
			throw new RuntimeException("Kinesis Data Stream is not available. Failed to start the channel.");
		}
		
		if (consumer != null) {
			executor = Executors.newFixedThreadPool(1, new DefaultThreadFactory("KinesisConsumer-" + this.getUri()));
			this.executor.submit(consumer);
			getLogger().log(Level.DEBUG, "Starting KinesisConsumer for destination - " + this.getUri());
		}

		getLogger().log(Level.INFO,
				"Kinesis destination started - " + this.getUri() + ", Serializer:" + this.getSerializer().getClass());
	}

	/**
	 * Stop the consumers.
	 */
	@Override
	public void close() throws Exception {
		getLogger().log(Level.INFO, "Closing Kinesis destination - " + this.getUri());
		if (executor != null) {
			executor.shutdown();
		}

		// Check all consumers are actually stopped.
		if (executor != null && !executor.awaitTermination(30, TimeUnit.SECONDS)) {
			getLogger().log(Level.WARN,
					"Kinesis consumers ExecutorService taking too long to shutdown, forcing shutdown.");
			executor.shutdownNow();
		}
		getLogger().log(Level.INFO, "Kinesis destination closed - " + this.getUri());
	}

	public Map<String, Object> getSerializationProperties() {
		return serializationProperties;
	}

	@Override
	public void connect() throws Exception {
	}

	@Override
	public Event requestEvent(Event outevent, String responseEventURI, BaseEventSerializer serializer, long timeout,
			Map userData) throws Exception {
		return null;
	}

	/**
	 * Send the event
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void send(EventWithId event, Map overrideData) throws Exception {

		this.serializationProperties.put(KinesisProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, true);
		Object record = serializer.serializeUserEvent(event, serializationProperties);
		
		if (kinesisClient == null) {
			this.createClient();
		}

		if (record instanceof PutRecordRequest) {
			kinesisClient.putRecord((PutRecordRequest) record);
		}
	}

	@Override
	public EventContext getEventContext(Event event) {
		return new KinesisEventContext(event, this, getChannel());
	}

	@Override
	public void suspend() {
		if (consumer != null) {
			consumer.suspend();
			executor.shutdown();
			executor = null;
			this.suspended = true;
		}
	}

	@Override
	public void resume() {
		if (this.suspended) {
			try {
				this.suspended = false;
				this.start();
			} catch (Exception e) {
				logger.log(Level.ERROR, e, "Error while resuming destination");
				throw new RuntimeException(e);
			}
			this.getLogger().log(Level.INFO, "Destination Resumed : " + getUri());

		}
	}
	

	private void createClient() {
		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
		clientBuilder.setRegion((String) getChannel().getGlobalVariableValue(getDestinationProperties().getProperty(KinesisProperties.KEY_DESTINATION_REGION_NAME)));
		KinesisChannel kinesischannel = (KinesisChannel) getChannel();
		AWSCredentialsProvider credentialsProvider = kinesischannel.getCredentialsProvider();

		clientBuilder.setCredentials(credentialsProvider);
		kinesisClient = clientBuilder.build();
	}

	private boolean isStreamExisiting() {
		String streamName = (String) getChannel().getGlobalVariableValue(getDestinationProperties().getProperty(KinesisProperties.KEY_DESTINATION_STREAM_NAME));
		ListStreamsRequest listStreamsRequest = new ListStreamsRequest();
		ListStreamsResult listStreamsResult = kinesisClient.listStreams(listStreamsRequest);
		List<String> streamNames = new ArrayList<String>();
		streamNames.addAll(listStreamsResult.getStreamNames());
		while (listStreamsResult.getHasMoreStreams()) 
		{
			if (streamNames.size() > 0) {
				listStreamsRequest.setExclusiveStartStreamName(streamNames.get(streamNames.size() - 1));
			}
			listStreamsResult = kinesisClient.listStreams(listStreamsRequest);
			streamNames.addAll(listStreamsResult.getStreamNames());
		}
		return streamNames.stream().anyMatch(name -> streamName.equals(name));
	}
}
