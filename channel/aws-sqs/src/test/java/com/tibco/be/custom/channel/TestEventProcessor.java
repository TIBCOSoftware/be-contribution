package com.tibco.be.custom.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestEventProcessor implements EventProcessor {
	private String payloadToCompare;
	private int msgsExpected; 
	private int msgsReceived;
	
	public TestEventProcessor(String eventPayloadToCompare, int msgsExpected) {
		this.payloadToCompare = eventPayloadToCompare;
		this.msgsExpected = msgsExpected;
	}

	public void processEvent(com.tibco.be.custom.channel.Event event) throws Exception {
		assertNotNull(event);
		assertEquals(new String(event.getPayload()), payloadToCompare);
		msgsReceived++;
	};
	
	@Override
	public String getRuleSessionName() {
		return null;
	}
	
	public boolean allMessagesReceived() {
		return msgsExpected == msgsReceived;
	}

}
