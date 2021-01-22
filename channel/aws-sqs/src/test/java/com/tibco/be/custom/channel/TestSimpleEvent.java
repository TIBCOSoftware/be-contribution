package com.tibco.be.custom.channel;

import com.tibco.cep.runtime.model.event.EventDeserializer;
import com.tibco.cep.runtime.model.event.impl.SimpleEventImpl;
import com.tibco.xml.data.primitive.ExpandedName;

public class TestSimpleEvent extends SimpleEventImpl {
	
	public TestSimpleEvent(long id, String extId) {
		super(id, extId);
	}

	@Override
	public ExpandedName getExpandedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTTL() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserializeProperty(EventDeserializer arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
