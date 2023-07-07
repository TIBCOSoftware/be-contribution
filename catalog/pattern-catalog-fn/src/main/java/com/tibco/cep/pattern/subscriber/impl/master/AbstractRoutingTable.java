package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantLock;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.PropertySubscribers;
import com.tibco.cep.pattern.subscriber.master.RoutingTable;
import com.tibco.cep.util.annotation.Copy;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 4:24:00 PM
*/
public abstract class AbstractRoutingTable implements RoutingTable<EventSource, DefaultSubscriptionRecord> {
    protected ReentrantLock lock;

    protected ConcurrentHashMap<EventSource, Object> eventSources;

    protected ConcurrentHashMap<EventDescriptor, AtomicStampedReference<DefaultSubscriptionRecord>> subscriptionRecords;

    protected transient ResourceProvider resourceProvider;

    protected AbstractRoutingTable(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        this.lock = new ReentrantLock();

        this.eventSources = new ConcurrentHashMap<EventSource, Object>();

        this.subscriptionRecords =
                new ConcurrentHashMap<EventDescriptor, AtomicStampedReference<DefaultSubscriptionRecord>>();
    }

    //---------------

    @Override
    public void start() throws LifecycleException {
        for (AtomicStampedReference<DefaultSubscriptionRecord> subscriptionRecord : subscriptionRecords.values()) {
            subscriptionRecord.getReference().start();
        }

        for (EventSource eventSource : eventSources.keySet()) {
            eventSource.start(resourceProvider);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        for (AtomicStampedReference<DefaultSubscriptionRecord> subscriptionRecord : subscriptionRecords.values()) {
            subscriptionRecord.getReference().stop();
        }

        for (EventSource eventSource : eventSources.keySet()) {
            eventSource.stop();
        }
    }

    //---------------

    @Override
    public void addEventSource(EventSource eventSource) throws LifecycleException {
        lock.lock();
        try {
            EventDescriptor eventDescriptor = eventSource.getEventDescriptor();

            eventSources.put(eventSource, eventSource);

            AtomicStampedReference<DefaultSubscriptionRecord> ref = subscriptionRecords.get(eventDescriptor);
            if (ref == null) {
                DefaultSubscriptionRecord subscriptionRecord =
                        new DefaultSubscriptionRecord(resourceProvider, eventDescriptor);

                ref = new AtomicStampedReference<DefaultSubscriptionRecord>(subscriptionRecord, /*Ref count*/ 1);

                subscriptionRecords.put(eventDescriptor, ref);
            }
            else {
                //Same old object but incr ref count.
                ref.set(ref.getReference(), ref.getStamp() + 1);
            }

            eventSource.start(resourceProvider);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<EventSource> getEventSources() {
        return eventSources.keySet();
    }

    @Override
    public void removeEventSource(EventSource eventSource) throws LifecycleException {
        lock.lock();
        try {
            EventDescriptor eventDescriptor = eventSource.getEventDescriptor();

            AtomicStampedReference<DefaultSubscriptionRecord> ref = subscriptionRecords.get(eventDescriptor);

            Collection<? extends PropertySubscribers> allPS = ref.getReference().getSubscribers();
            int c = 0;
            for (PropertySubscribers ps : allPS) {
                c = c + ps.getSubscriberCount();
            }

            if (c > 0 && ref.getStamp() == 1) {
                throw new LifecycleException(
                        "The EventSource [" + eventSource + "] cannot be removed because" +
                                " there are still a few Subscribers present [" + allPS + "].");
            }

            //---------------

            eventSource.stop();

            if (ref.getStamp() == 1) {
                subscriptionRecords.remove(eventDescriptor);
            }
            else {
                //Same ref but decr the count.
                ref.set(ref.getReference(), ref.getStamp() - 1);
            }

            eventSources.remove(eventSource);
        }
        finally {
            lock.unlock();
        }
    }

    //---------------

    @Override
    @Copy
    public Collection<DefaultSubscriptionRecord> getSubscriptionRecords() {
        LinkedList<DefaultSubscriptionRecord> list = new LinkedList<DefaultSubscriptionRecord>();

        for (AtomicStampedReference<DefaultSubscriptionRecord> reference : subscriptionRecords.values()) {
            list.add(reference.getReference());
        }

        return list;
    }

    @Override
    public DefaultSubscriptionRecord getSubscriptionRecord(EventDescriptor eventDescriptor) {
        return subscriptionRecords.get(eventDescriptor).getReference();
    }

    //---------------

    @Override
    public AbstractRoutingTable recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        this.resourceProvider = resourceProvider;

        //---------------

        ConcurrentHashMap<EventSource, Object> newMap = new ConcurrentHashMap<EventSource, Object>();

        for (Entry<EventSource, Object> entry : eventSources.entrySet()) {
            EventSource eventSource = entry.getKey();

            eventSource = (EventSource) eventSource.recover(resourceProvider, params);

            newMap.put(eventSource, eventSource);

            //---------------

            EventDescriptor eventDescriptor = eventSource.getEventDescriptor();
            recoverED(resourceProvider, eventDescriptor, params);

            EventDescriptor[] eventDescriptors = eventSource.getAdditionalEventDescriptors();
            if (eventDescriptors != null) {
                for (EventDescriptor additionalED : eventDescriptors) {
                    recoverED(resourceProvider, additionalED, params);
                }
            }
        }

        eventSources.clear();
        eventSources = newMap;

        //---------------

        return this;
    }

    private void recoverED(ResourceProvider resourceProvider, EventDescriptor eventDescriptor, Object[] params)
            throws RecoveryException {
        AtomicStampedReference<DefaultSubscriptionRecord> reference = subscriptionRecords.remove(eventDescriptor);

        DefaultSubscriptionRecord subscriptionRecord = reference.getReference().recover(resourceProvider, params);
        reference.set(subscriptionRecord, /*Do not change the count*/ reference.getStamp());

        subscriptionRecords.put(eventDescriptor, reference);
    }
}
