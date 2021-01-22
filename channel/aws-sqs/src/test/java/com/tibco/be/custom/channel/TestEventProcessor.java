package com.tibco.be.custom.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestEventProcessor implements EventProcessor {
	private String payloadToCompare;
	
	public TestEventProcessor(String eventPayloadToCompare) {
		this.payloadToCompare = eventPayloadToCompare;
	}

	public void processEvent(com.tibco.be.custom.channel.Event event) throws Exception {
		assertNotNull(event);
		assertEquals(event.getPayload(), payloadToCompare.getBytes());
	};
	
	@Override
	public String getRuleSessionName() {
		return null;
	}

}
