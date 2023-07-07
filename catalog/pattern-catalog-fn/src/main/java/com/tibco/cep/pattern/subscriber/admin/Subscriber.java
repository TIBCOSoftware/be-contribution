package com.tibco.cep.pattern.subscriber.admin;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.Router;

/*
* Author: Ashwin Jayaprakash Date: Oct 8, 2009 Time: 1:10:32 PM
*/
public interface Subscriber {
    Router getRouter();

    //------------

    Collection<? extends EventSource> getEventSources();

    //------------

    EventDescriptor getEventDescriptor(Id id);

    Collection<? extends EventDescriptor> getEventDescriptors();
}
