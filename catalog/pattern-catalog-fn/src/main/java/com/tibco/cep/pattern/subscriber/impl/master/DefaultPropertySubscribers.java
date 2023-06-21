package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.PropertySubscribers;
import com.tibco.cep.pattern.subscriber.master.Subscribers;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;
import com.tibco.cep.util.annotation.Copy;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 5:16:14 PM
*/

public class DefaultPropertySubscribers implements PropertySubscribers {
    //todo This will be very heavy-weight if each value will have only 1 subscriber

    protected ConcurrentHashMap<Comparable, DefaultSubscribers> valuesAndSubscribers;

    protected AtomicReference<DefaultSubscribers> nullValueSubscribers;

    protected transient ResourceProvider resourceProvider;

    public DefaultPropertySubscribers(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
        this.valuesAndSubscribers = new ConcurrentHashMap<Comparable, DefaultSubscribers>();
        this.nullValueSubscribers = new AtomicReference<DefaultSubscribers>();
    }

    //---------------

    public void start() throws LifecycleException {
        for (DefaultSubscribers subscribers : valuesAndSubscribers.values()) {
            subscribers.start();
        }

        DefaultSubscribers nvs = nullValueSubscribers.get();
        if (nvs != null) {
            nvs.start();
        }
    }

    public void stop() throws LifecycleException {
        for (DefaultSubscribers subscribers : valuesAndSubscribers.values()) {
            subscribers.stop();
        }

        DefaultSubscribers nvs = nullValueSubscribers.get();
        if (nvs != null) {
            nvs.stop();
        }
    }

    //---------------

    @Copy
    public Set<Comparable> getPropertyValues() {
        //todo Avoid creating a new hashset everytime.
        HashSet<Comparable> set = new HashSet<Comparable>(valuesAndSubscribers.keySet());

        DefaultSubscribers nvs = nullValueSubscribers.get();
        if (nvs != null) {
            set.add(null);

            return set;
        }

        return valuesAndSubscribers.keySet();
    }

    /**
     * @param propertyValue <code>null</code> is not supported.
     * @return
     */
    public Collection<Id> getSubscriberIds(Comparable propertyValue) {
        Subscribers subscribers = null;

        if (propertyValue == null) {
            subscribers = nullValueSubscribers.get();
        }
        else {
            subscribers = valuesAndSubscribers.get(propertyValue);
        }

        if (subscribers == null) {
            return null;
        }

        return subscribers.getSubscriberIds();
    }

    /**
     * @return Approximate count.
     */
    public int getSubscriberCount() {
        int c = 0;

        for (DefaultSubscribers subscribers : valuesAndSubscribers.values()) {
            c = c + subscribers.getSubscriberCount();
        }

        DefaultSubscribers nvs = nullValueSubscribers.get();
        if (nvs != null) {
            c++;
        }

        return c;
    }

    /**
     * @param propertyValue <code>null</code> is not supported.
     * @return
     */
    public Collection<SubscriptionListener> getSubscriptionListeners(Comparable propertyValue) {
        DefaultSubscribers subscribers = null;

        if (propertyValue == null) {
            subscribers = nullValueSubscribers.get();
        }
        else {
            subscribers = valuesAndSubscribers.get(propertyValue);
        }

        if (subscribers == null) {
            return null;
        }

        return subscribers.getSubscriptionListeners();
    }

    //---------------

    /**
     * @param propertyValue <code>null</code> is not supported.
     * @param subscriberId
     * @param listener
     * @throws LifecycleException
     */
    public void addSubscriber(Comparable propertyValue, Id subscriberId, SubscriptionListener listener)
            throws LifecycleException {

        DefaultSubscribers subscribers = null;

        //Partially exclusive-lock-free mechanism to register new listeners.
        for (; /*Keep trying until you succeed.*/ ;) {
            subscribers = (propertyValue == null)
                    ? nullValueSubscribers.get()
                    : valuesAndSubscribers.get(propertyValue);

            //There are other subscriptions. Let's race to add ours too.
            if (subscribers != null) {
                boolean success = tryAddingToExistingContainer(propertyValue, subscriberId, listener, subscribers);

                if (success) {
                    return;
                }

                continue;
            }

            //---------------

            subscribers = new DefaultSubscribers(resourceProvider);

            subscribers.lockBeforeConcurrentUse();
            try {
                Subscribers existingSubscribers = null;

                if (propertyValue == null) {
                    if (nullValueSubscribers.compareAndSet(null, subscribers) == false) {
                        existingSubscribers = nullValueSubscribers.get();
                    }
                }
                else {
                    existingSubscribers = valuesAndSubscribers.putIfAbsent(propertyValue, subscribers);
                }

                //Ahh..good. So, we were the first to register for this value.
                if (existingSubscribers == null) {
                    subscribers.addSubscriber(subscriberId, listener);

                    return;
                }

                //Oh..someone beat us to the punch. So, we discard ours and start again.
                continue;
            }
            finally {
                subscribers.unlockAfterConcurrentUse();
            }
        }
    }

    /**
     * @param propertyValue      <code>null</code> is not supported.
     * @param subscriberId
     * @param listener
     * @param givenSubsContainer
     * @return
     * @throws LifecycleException
     */
    private boolean tryAddingToExistingContainer(Comparable propertyValue, Id subscriberId,
                                                 SubscriptionListener listener,
                                                 Subscribers givenSubsContainer)
            throws LifecycleException {
        givenSubsContainer.lockBeforeConcurrentUse();

        try {
            Subscribers newSubscribers = null;
            if (propertyValue == null) {
                newSubscribers = nullValueSubscribers.get();
            }
            else {
                newSubscribers = valuesAndSubscribers.get(propertyValue);
            }

            /*
            Oh..so we acquired a concurrent-lock on a subscriber that got removed
            just before our lock method. So, we have to start all over with the new mapping.
            */
            if (newSubscribers != givenSubsContainer) {
                return false;
            }

            boolean success = givenSubsContainer.addSubscriber(subscriberId, listener);
            if (success) {
                return true;
            }

            throw new LifecycleException("Subscription for the Id [" + subscriberId +
                    "] failed because another identical subscription already exists.");
        }
        finally {
            givenSubsContainer.unlockAfterConcurrentUse();
        }
    }

    /**
     * @param propertyValue <code>null</code> is not supported.
     * @param subscriberId
     */
    public void removeSubscriber(Comparable propertyValue, Id subscriberId) {
        Subscribers subscribersContainer = null;
        if (propertyValue == null) {
            subscribersContainer = nullValueSubscribers.get();
        }
        else {
            subscribersContainer = valuesAndSubscribers.get(propertyValue);
        }

        if (subscribersContainer == null) {
            return;
        }

        //---------------

        subscribersContainer.lockBeforeConcurrentUse();
        try {
            subscribersContainer.removeSubscriber(subscriberId);
        }
        finally {
            subscribersContainer.unlockAfterConcurrentUse();
        }

        //---------------

        //Now, try to remove the object if its count is 0.

        subscribersContainer = (propertyValue == null)
                ? nullValueSubscribers.get()
                : valuesAndSubscribers.get(propertyValue);

        //Wow that was quick. Some other thread removed it while we were looking.
        if (subscribersContainer == null) {
            return;
        }

        //Ready for cleanup.
        if (subscribersContainer.getSubscriberCount() == 0
                && subscribersContainer.tryLockBeforeExclusiveUse()) {
            try {
                /*
                Still 0. Making sure that no one sneaked in and added something before the
                exclusive-lock operation.
                */
                if (subscribersContainer.getSubscriberCount() == 0) {
                    if (propertyValue == null) {
                        nullValueSubscribers.set(null);
                    }
                    else {
                        valuesAndSubscribers.remove(propertyValue);
                    }
                }
            }
            finally {
                subscribersContainer.unlockAfterExclusiveUse();
            }
        }
    }

    //-------------

    public DefaultPropertySubscribers recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        this.resourceProvider = resourceProvider;

        for (Entry<Comparable, DefaultSubscribers> entry : valuesAndSubscribers.entrySet()) {
            DefaultSubscribers subscribers = entry.getValue();

            subscribers = subscribers.recover(resourceProvider, params);

            entry.setValue(subscribers);
        }

        return this;
    }
}
