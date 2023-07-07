package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;
import com.tibco.cep.pattern.subscriber.master.SubscriptionRecord;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 5:16:14 PM
*/
public class DefaultSubscriptionRecord implements SubscriptionRecord {
    protected HashMap<String, DefaultPropertySubscribers> propNamesAndSubscribers;

    protected DefaultSubscribers plainSubscribers;

    protected EventDescriptor eventDescriptor;

    protected transient ResourceProvider resourceProvider;

    public DefaultSubscriptionRecord(ResourceProvider resourceProvider, EventDescriptor eventDescriptor) {
        this.resourceProvider = resourceProvider;

        this.eventDescriptor = eventDescriptor;

        String[] propertyNames = eventDescriptor.getSortedPropertyNames();

        this.propNamesAndSubscribers = new HashMap<String, DefaultPropertySubscribers>(propertyNames.length);
        for (String propertyName : propertyNames) {
            this.propNamesAndSubscribers
                    .put(propertyName, new DefaultPropertySubscribers(this.resourceProvider));
        }

        this.plainSubscribers = new DefaultSubscribers(this.resourceProvider);
    }

    public EventDescriptor getEventDescriptor() {
        return eventDescriptor;
    }

    //--------------

    public void start() throws LifecycleException {
        for (DefaultPropertySubscribers propertySubscribers : propNamesAndSubscribers.values()) {
            propertySubscribers.start();
        }

        plainSubscribers.start();
    }

    public void stop() throws LifecycleException {
        plainSubscribers.stop();

        for (DefaultPropertySubscribers propertySubscribers : propNamesAndSubscribers.values()) {
            propertySubscribers.stop();
        }
    }

    //--------------

    public void addSubscriber(String propertyName, Comparable propertyValue,
                              Id subscriberId, SubscriptionListener listener)
            throws LifecycleException {
        DefaultPropertySubscribers subscribers = propNamesAndSubscribers.get(propertyName);

        subscribers.addSubscriber(propertyValue, subscriberId, listener);
    }

    public Collection<SubscriptionListener> getSubscriptionListeners(String propertyName,
                                                                     Comparable propertyValue) {
        DefaultPropertySubscribers subscribers = propNamesAndSubscribers.get(propertyName);

        return subscribers.getSubscriptionListeners(propertyValue);
    }

    public DefaultPropertySubscribers getSubscribers(String propertyName) {
        return propNamesAndSubscribers.get(propertyName);
    }

    public Collection<DefaultPropertySubscribers> getSubscribers() {
        return propNamesAndSubscribers.values();
    }

    public void removeSubscriber(String propertyName, Comparable propertyValue, Id subscriberId) {
        DefaultPropertySubscribers subscribers = propNamesAndSubscribers.get(propertyName);

        subscribers.removeSubscriber(propertyValue, subscriberId);
    }

    //--------------

    public void addPlainSubscriber(Id subscriberId, SubscriptionListener listener)
            throws LifecycleException {
        plainSubscribers.addSubscriber(subscriberId, listener);
    }

    public Collection<SubscriptionListener> getPlainSubscriptionListeners() {
        return plainSubscribers.getSubscriptionListeners();
    }

    public DefaultSubscribers getPlainSubscribers() {
        return plainSubscribers;
    }

    public void removePlainSubscriber(Id subscriberId) {
        plainSubscribers.removeSubscriber(subscriberId);
    }

    //--------------

    public DefaultSubscriptionRecord recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        this.resourceProvider = resourceProvider;

        EventDescriptorRegistry edRegistry =
                resourceProvider.fetchResource(EventDescriptorRegistry.class);
        eventDescriptor = edRegistry.getEventDescriptor(eventDescriptor.getResourceId());

        //--------------

        plainSubscribers = plainSubscribers.recover(resourceProvider, params);

        //--------------

        for (Entry<String, DefaultPropertySubscribers> entry : propNamesAndSubscribers.entrySet()) {
            DefaultPropertySubscribers subscribers = entry.getValue();

            subscribers = subscribers.recover(resourceProvider, params);

            entry.setValue(subscribers);
        }

        //--------------

        return this;
    }
}
