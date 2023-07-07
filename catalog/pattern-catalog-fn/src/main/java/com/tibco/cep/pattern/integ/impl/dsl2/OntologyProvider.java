package com.tibco.cep.pattern.integ.impl.dsl2;

import java.util.Collection;

import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Event;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash / Date: Nov 17, 2009 / Time: 7:11:17 PM
*/
public interface OntologyProvider {
    EventDescriptor getEventDescriptor(Event event);

    Collection<? extends EventDescriptor> getEventDescriptors();
}
