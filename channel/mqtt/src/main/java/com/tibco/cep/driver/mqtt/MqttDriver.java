package com.tibco.cep.driver.mqtt;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.BaseDriver;

/**
 * @author ssinghal
 *
 */
public class MqttDriver extends BaseDriver {

	@Override
	public BaseChannel getChannel() {
		return new MqttChannel();
	}

	@Override
	public BaseDestination getDestination() {
		return new MqttDestination();
	}

}
