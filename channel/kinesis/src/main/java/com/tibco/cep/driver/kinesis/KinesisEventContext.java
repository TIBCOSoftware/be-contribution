package com.tibco.cep.driver.kinesis;
import java.util.HashMap;
import java.util.Map;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventContext;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.custom.channel.ExtendedDefaultEventImpl;
import com.tibco.cep.runtime.model.event.SimpleEvent;

/**
 * EventContext for Kinesis events.
 *
 * @author
 */
public class KinesisEventContext implements EventContext {

	private Event event;
	private BaseDestination destination;
	private BaseChannel channel;

	public KinesisEventContext(Event event, BaseDestination destination, BaseChannel channel) {
		this.setEvent(event);
		this.destination = destination;
		this.channel = channel;

	}

	@Override
	public boolean reply(Event replyEvent) {
	    Map<String, String> userData = new HashMap<String, String>();
	    ExtendedDefaultEventImpl replyEvt = (ExtendedDefaultEventImpl)replyEvent;
        SimpleEvent reply = replyEvt.getUnderlyingSimpleEvent();
        KinesisDestination KinesisDest = (KinesisDestination) channel.getDestinations().get(reply.getDestinationURI());

		try {
			KinesisDest.send((EventWithId)replyEvent, userData);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
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
		return null;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}