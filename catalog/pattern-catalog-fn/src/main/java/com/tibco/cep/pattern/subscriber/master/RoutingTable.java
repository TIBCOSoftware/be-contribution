package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Recoverable;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 3:43:24 PM
*/

/**
 * {@link EventDescriptor} is the lookup key for both sender and receiver.
 */
public interface RoutingTable<E extends EventSource, S extends SubscriptionRecord> extends
        Recoverable<RoutingTable<E, S>> {
    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //-------------

    void addEventSource(E eventSource) throws LifecycleException;

    Collection<E> getEventSources();

    void removeEventSource(E eventSource) throws LifecycleException;

    //-------------

    Collection<S> getSubscriptionRecords();

    S getSubscriptionRecord(EventDescriptor eventDescriptor);
}
