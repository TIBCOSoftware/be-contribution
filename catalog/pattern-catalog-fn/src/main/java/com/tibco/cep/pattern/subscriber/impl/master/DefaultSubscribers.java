package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.Subscribers;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 5:16:14 PM
*/
public class DefaultSubscribers extends ReentrantReadWriteLock implements Subscribers {
    protected ConcurrentHashMap<Id, SubscriptionListener> subscribers;

    protected transient ResourceProvider resourceProvider;

    public DefaultSubscribers(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        /*
        Try to keep this map as small as possible. If during its life there will be only 1
        subscription, then this will waste a lot of memory.
        */
        this.subscribers = new ConcurrentHashMap<Id, SubscriptionListener>(1, 0.75f, 2);
    }

    //--------------

    public void start() throws LifecycleException {
        for (Entry<Id, SubscriptionListener> entry : subscribers.entrySet()) {
            SubscriptionListener listener = entry.getValue();
            listener.start(resourceProvider, entry.getKey());
        }
    }

    public void stop() throws LifecycleException {
        for (SubscriptionListener listener : subscribers.values()) {
            listener.stop();
        }
    }

    //--------------

    public void lockBeforeConcurrentUse() {
        readLock().lock();
    }

    public boolean addSubscriber(Id id, SubscriptionListener listener) {
        Object existing = subscribers.putIfAbsent(id, listener);

        if (existing == null) {
            listener.start(resourceProvider, id);

            return true;
        }

        return false;
    }

    public SubscriptionListener removeSubscriber(Id id) {
        SubscriptionListener listener = subscribers.remove(id);
        if (listener != null) {
            listener.stop();
        }

        return listener;
    }

    public void unlockAfterConcurrentUse() {
        readLock().unlock();
    }

    public boolean tryLockBeforeExclusiveUse() {
        try {
            return writeLock().tryLock(0, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
        }

        return false;
    }

    public void unlockAfterExclusiveUse() {
        writeLock().unlock();
    }

    public Collection<SubscriptionListener> getSubscriptionListeners() {
        return subscribers.values();
    }

    public Collection<Id> getSubscriberIds() {
        return subscribers.keySet();
    }

    public int getSubscriberCount() {
        return subscribers.size();
    }

    //--------------

    public DefaultSubscribers recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        this.resourceProvider = resourceProvider;

        for (Entry<Id, SubscriptionListener> entry : subscribers.entrySet()) {
            SubscriptionListener listener = entry.getValue();

            listener = listener.recover(resourceProvider, params);

            entry.setValue(listener);
        }

        return this;
    }
}
