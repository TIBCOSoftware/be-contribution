package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 2:54:54 PM
*/
public interface SubscriptionRecord extends Recoverable<SubscriptionRecord> {
    EventDescriptor getEventDescriptor();

    //------------

    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //------------

    void addSubscriber(String propertyName, Comparable propertyValue,
                       Id subscriberId, SubscriptionListener listener)
            throws LifecycleException;

    Collection<SubscriptionListener> getSubscriptionListeners(String propertyName,
                                                              Comparable propertyValue);

    PropertySubscribers getSubscribers(String propertyName);

    Collection<? extends PropertySubscribers> getSubscribers();

    void removeSubscriber(String propertyName, Comparable propertyValue, Id subscriberId);

    //------------

    void addPlainSubscriber(Id subscriberId, SubscriptionListener listener)
            throws LifecycleException;

    Collection<SubscriptionListener> getPlainSubscriptionListeners();

    Subscribers getPlainSubscribers();

    void removePlainSubscriber(Id subscriberId);
}
