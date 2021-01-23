package com.tibco.be.custom.channel;

import com.tibco.cep.runtime.model.event.EventDeserializer;
import com.tibco.cep.runtime.model.event.impl.SimpleEventImpl;
import com.tibco.xml.data.primitive.ExpandedName;

public class TestSimpleEvent extends SimpleEventImpl {
	private String eventUri;
	public TestSimpleEvent(long id, String extId, String destinationUri, String eventUri) {
		super(id, extId);
		setDestinationURI(destinationURI);
		this.eventUri = eventUri;
	}

	@Override
	public ExpandedName getExpandedName() {
		return ExpandedName.makeName(eventUri);
	}

	@Override
	public String[] getPropertyNames() {
		return null;
	}

	@Override
	public long getTTL() {
		return 0;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public void deserializeProperty(EventDeserializer arg0, int arg1) {

	}
}
