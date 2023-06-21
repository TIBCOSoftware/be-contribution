package com.tibco.cep.driver.mqtt;

import java.util.HashMap;
import java.util.Map;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventContext;
import com.tibco.be.custom.channel.EventWithId;


/**
 * @author ssinghal
 *
 */
public class MqttMessageContext implements EventContext{
	
	Event event;
	BaseDestination destination;
	BaseChannel channel;
	

	public MqttMessageContext(Event event, BaseDestination destination, BaseChannel channel) {
		this.event = event;
		this.destination = destination;
		this.channel = channel;
		
	}


	@Override
	public boolean reply(Event replyEvent) {
		
		Map<String, String> userData = new HashMap<String, String>();
		userData.put(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD, (String)event.getPropertyValue(MqttProperties.MQTT_REQUEST_TOPIC_KEYWORD));
		
		MqttDestination mqttDest = (MqttDestination) channel.getDestinations().get(event.getDestinationURI());
		
		try {
			mqttDest.send((EventWithId)replyEvent, userData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	


	@Override
	public void acknowledge() {
		// TODO Auto-generated method stub
	}


	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}


	@Override
	public BaseDestination getDestination() {
		return this.destination;
	}


	@Override
	public Object getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
