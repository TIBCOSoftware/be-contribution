package com.tibco.cep.pattern.impl.util;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.impl.common.AbstractEventSource;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash Date: Aug 20, 2009 Time: 2:21:31 PM
*/
public class SimpleEventSource<T> extends AbstractEventSource<T> {
    public SimpleEventSource(EventDescriptor<T> eventDescriptor) {
        super(eventDescriptor);
    }

    public SimpleEventSource(EventDescriptor<T> eventDescriptor, EventDescriptor<T>[] additionalEventDescriptors) {
        super(eventDescriptor, additionalEventDescriptors);
    }

    public void start(ResourceProvider resourceProvider) throws LifecycleException {
    }

    public void stop() throws LifecycleException {
    }

    //-----------

    public SimpleEventSource<T> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        return this;
    }
}
