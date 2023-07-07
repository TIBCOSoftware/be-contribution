package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 2:48:54 PM
*/
public interface Subscribers extends Recoverable<Subscribers> {
    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //-----------

    void lockBeforeConcurrentUse();

    /**
     * @param id
     * @param listener
     * @return <code>true</code> if it succeeds. <code>false</code> if there was already another
     *         registration with the same Id.
     */
    boolean addSubscriber(Id id, SubscriptionListener listener);

    SubscriptionListener removeSubscriber(Id id);

    void unlockAfterConcurrentUse();

    //------------

    /**
     * @return <code>true</code> if the locking succeeds.
     */
    boolean tryLockBeforeExclusiveUse();

    /**
     * Call this only if {@link #tryLockBeforeExclusiveUse()} succeeds.
     */
    void unlockAfterExclusiveUse();

    //------------

    Collection<SubscriptionListener> getSubscriptionListeners();

    Collection<Id> getSubscriberIds();

    int getSubscriberCount();
}
