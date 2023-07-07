package com.tibco.cep.pattern.subscriber.master;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 3:46:23 PM
*/
public interface EventSource<T> extends Recoverable<EventSource<T>> {
    EventDescriptor<T> getEventDescriptor();

    @Optional
    EventDescriptor<T>[] getAdditionalEventDescriptors();

    void start(ResourceProvider resourceProvider) throws LifecycleException;

    void stop() throws LifecycleException;
}
