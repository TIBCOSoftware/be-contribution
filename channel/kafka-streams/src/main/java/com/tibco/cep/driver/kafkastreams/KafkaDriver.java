package com.tibco.cep.driver.kafka;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseDriver;

/**
 * Implementation of Channel API's BaseDriver for Kafka.
 * 
 * @author moshaikh
 */
public class KafkaDriver extends BaseDriver {

	@Override
	public BaseChannel getChannel() {
		return new KafkaChannel();
	}

	@Override
	public BaseDestination getDestination() {
		return new KafkaDestination();
	}

}
