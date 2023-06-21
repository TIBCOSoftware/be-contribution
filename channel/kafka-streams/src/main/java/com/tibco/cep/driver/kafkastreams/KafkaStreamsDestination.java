package com.tibco.cep.driver.kafkastreams;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.kernel.service.logging.Level;

/**
 * An instance of this class represents a Kafka Streams destination.
 * 
 * @author shivkumarchelwa
 */
public class KafkaStreamsDestination extends BaseDestination {

	private Properties channelAndDestProps;
	private BEKafkaStreams beKafkaStreams;

	/**
	 * Initialize the destination. Store a handle to the channel's resource - in
	 * this case the executor which is initialized during Channel.init(), and set
	 * some Kafka Streams destination related configurations.
	 */
	@Override
	public void init() throws Exception {
		this.channelAndDestProps = initChannelAndDestProps();
	}

	/**
	 * connect() is where we connect the destination to its transport.
	 */
	@Override
	public void connect() throws Exception {
		// Nothing to do here in this implementation.
	}

	@Override
	public void bind(EventProcessor ep) throws Exception {
		if (beKafkaStreams == null) {
			beKafkaStreams = new BEKafkaStreams(ep, this, channelAndDestProps);
			beKafkaStreams.init();
		}
	}

	/*
	 * The channel and destination properties. The properties set as GV are set to
	 * deployment configured values. Additional properties can be set here.
	 */
	private Properties initChannelAndDestProps() {
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

		props.put(KafkaStreamsProperties.INTERNAL_PROP_KEY_CHANNEL_URI, getChannel().getUri());
		props.put(KafkaStreamsProperties.INTERNAL_PROP_KEY_DESTINATION_URI, this.getUri());

		return props;
	}

	/**
	 * Start receiving Kafka messages on this destination. A Kafka receiver job is
	 * started. This job runs forever, polling the Kafka endpoint for messages.
	 */
	@Override
	public void start() throws Exception {
		if (beKafkaStreams != null) {
			beKafkaStreams.start();
			getLogger().log(Level.INFO, "Kafka Streams destination started - " + this.getUri() + ", Serializer:"
					+ this.getSerializer().getClass());
		}
	}

	/**
	 * Stop the consumers.
	 */
	@Override
	public void close() throws Exception {
		getLogger().log(Level.INFO, "Closing Kafka Streams destination - " + this.getUri());

		
		// Shutdown this KafkaStreams instance
		boolean success = this.beKafkaStreams != null? this.beKafkaStreams.close() : true;
		if (success) {
			getLogger().log(Level.INFO, "Kafka Streams destination closed - " + this.getUri());
		} else {
			getLogger().log(Level.INFO,
					"Kafka Streams destination closed but not all threads stopped - " + this.getUri());
		}
	}

	@Override
	public void send(EventWithId event, Map overrideData) throws Exception {
		// A Kafka destination should be used instead of "Kafka Streams"
		throw new Exception("Send is not supported with Kafka Streams Channel");
	}

	@Override
	public Event requestEvent(Event outevent, String responseEventURI, BaseEventSerializer serializer, long timeout,
			Map overrideData) throws Exception {
		// Request-reply paradigm not applicable for Kafka
		throw new Exception("requestEvent() is not supported with Kafka Channel");
	}

	@Override
	public void suspend() {
		try {
			close();
			this.getLogger().log(Level.INFO, "Destination Suspended : " + getUri());
		} catch (Exception e) {
			getLogger().log(Level.ERROR, "Error while suspending Kafka Streams destination " + this.getUri(), e);
		}
	}

	@Override
	public void resume() {
		try {
			start();
			this.getLogger().log(Level.INFO, "Destination Resumed : " + getUri());
		} catch (Exception e) {
			getLogger().log(Level.ERROR, "Error while resuming Kafka Streams destination " + this.getUri(), e);
		}
	}
}
