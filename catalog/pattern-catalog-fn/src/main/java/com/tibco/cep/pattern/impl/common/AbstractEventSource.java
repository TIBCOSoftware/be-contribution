package com.tibco.cep.pattern.impl.common;

import java.util.Arrays;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 4:27:22 PM
*/
public abstract class AbstractEventSource<T> implements EventSource<T> {
    protected EventDescriptor<T> eventDescriptor;

    @Optional
    protected EventDescriptor<T>[] additionalEventDescriptors;

    public AbstractEventSource(EventDescriptor<T> eventDescriptor) {
        this.eventDescriptor = eventDescriptor;
    }

    public AbstractEventSource(EventDescriptor<T> eventDescriptor, EventDescriptor<T>[] additionalEventDescriptors) {
        this.eventDescriptor = eventDescriptor;
        this.additionalEventDescriptors = additionalEventDescriptors;
    }

    public EventDescriptor<T> getEventDescriptor() {
        return eventDescriptor;
    }

    public EventDescriptor<T>[] getAdditionalEventDescriptors() {
        return additionalEventDescriptors;
    }

    //------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEventSource)) {
            return false;
        }

        AbstractEventSource that = (AbstractEventSource) o;

        if (!Arrays.equals(additionalEventDescriptors, that.additionalEventDescriptors)) {
            return false;
        }
        if (!eventDescriptor.equals(that.eventDescriptor)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventDescriptor.hashCode();
        result = 31 * result + (additionalEventDescriptors != null ? Arrays.hashCode(additionalEventDescriptors) : 0);

        return result;
    }

    //------------

    @SuppressWarnings({"unchecked"})
    public AbstractEventSource<T> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        EventDescriptorRegistry edRegistry = resourceProvider.fetchResource(EventDescriptorRegistry.class);

        eventDescriptor = edRegistry.getEventDescriptor(eventDescriptor.getResourceId());

        for (int i = 0; i < additionalEventDescriptors.length; i++) {
            additionalEventDescriptors[i] =
                    edRegistry.getEventDescriptor(additionalEventDescriptors[i].getResourceId());
        }

        return this;
    }
}
