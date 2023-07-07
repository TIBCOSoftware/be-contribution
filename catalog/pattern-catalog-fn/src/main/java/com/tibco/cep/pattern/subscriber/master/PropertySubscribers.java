package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;
import java.util.Set;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 2:51:33 PM
*/
public interface PropertySubscribers extends Recoverable<PropertySubscribers> {
    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //-----------

    void addSubscriber(Comparable propertyValue, Id subscriberId, SubscriptionListener listener)
            throws LifecycleException;

    Set<Comparable> getPropertyValues();

    Collection<Id> getSubscriberIds(Comparable propertyValue);

    int getSubscriberCount();

    /**
     * @param propertyValue
     * @return <code>null</code> if no mapping exists.
     */
    Collection<SubscriptionListener> getSubscriptionListeners(Comparable propertyValue);

    void removeSubscriber(Comparable propertyValue, Id subscriberId);
}
