package com.tibco.cep.driver.kafkastreams;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseDriver;
import com.tibco.cep.driver.kafka.KafkaChannel;

/**
 * Implementation of Channel API's BaseDriver for Kafka.
 * 
 * @author shivkumarchelwa
 */
public class KafkaStreamsDriver extends BaseDriver {

	@Override
	public BaseChannel getChannel() {
		return new KafkaChannel();
	}

	@Override
	public BaseDestination getDestination() {
		return new KafkaStreamsDestination();
	}

}
