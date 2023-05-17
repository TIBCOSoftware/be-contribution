package com.tibco.cep.driver.kinesis;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseDriver;

/**
 * Implementation of Channel API's BaseDriver for Kinesis.
 * 
 * 
 */
public class KinesisDriver extends BaseDriver {

	@Override
	public BaseChannel getChannel() {
		return new KinesisChannel();
	}

	@Override
	public BaseDestination getDestination() {
		return new KinesisDestination();
	}

}
