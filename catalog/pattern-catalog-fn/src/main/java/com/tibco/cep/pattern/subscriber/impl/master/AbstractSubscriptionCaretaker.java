package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.LinkedList;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionItemDef;
import com.tibco.cep.pattern.subscriber.master.SubscriptionCaretaker;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 3:43:23 PM
*/
public abstract class AbstractSubscriptionCaretaker implements SubscriptionCaretaker {
    protected Id id;

    protected Collection<? extends SubscriptionItemDef> subscriptionItems;

    public AbstractSubscriptionCaretaker() {
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public void setSubscriptionItems(Collection<? extends SubscriptionItemDef> subscriptionItems) {
        this.subscriptionItems = subscriptionItems;
    }

    public Collection<? extends SubscriptionItemDef> getSubscriptionItems() {
        return subscriptionItems;
    }

    //------------

    public void start() {
    }

    public void stop() {
    }

    //------------

    public AbstractSubscriptionCaretaker recover(ResourceProvider resourceProvider,
                                                 Object... params)
            throws RecoveryException {
        LinkedList<SubscriptionItemDef> newList = new LinkedList<SubscriptionItemDef>();

        for (SubscriptionItemDef subscriptionItem : subscriptionItems) {
            subscriptionItem = subscriptionItem.recover(resourceProvider, params);

            newList.add(subscriptionItem);
        }

        subscriptionItems.clear();
        subscriptionItems = newList;

        return this;
    }
}
