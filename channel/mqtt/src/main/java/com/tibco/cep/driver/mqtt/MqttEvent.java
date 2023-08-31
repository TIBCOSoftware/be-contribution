package com.tibco.cep.driver.mqtt;

import com.tibco.be.custom.channel.framework.CustomEvent;

/**
 * @author ssinghal
 *
 */
public class MqttEvent extends CustomEvent {

	private String requestTopic;

	public String getRequestTopic() {
		return requestTopic;
	}

	public void setRequestTopic(String requestTopic) {
		this.requestTopic = requestTopic;
	}
}
