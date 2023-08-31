/**
 * 
 */
package com.tibco.cep.driver.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.tibco.be.custom.channel.Event;

/**
 * @author ssinghal
 *
 */
public interface MqttBECallback {
	
	public void messageArrived(String topic, Event event) throws Exception ;

}
