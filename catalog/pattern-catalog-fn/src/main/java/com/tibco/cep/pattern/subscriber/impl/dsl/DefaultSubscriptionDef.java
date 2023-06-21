package com.tibco.cep.pattern.subscriber.impl.dsl;

import java.util.LinkedList;
import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 2:03:22 PM
*/
public class DefaultSubscriptionDef implements SubscriptionDef<DefaultSubscriptionDef> {
    protected LinkedList<DefaultSubscriptionItemDef> subscriptionItems;

    protected transient DefaultSubscriptionItemDef inProgressListener;

    protected transient EventDescriptorRegistry eventDescriptorRegistry;

    public DefaultSubscriptionDef(EventDescriptorRegistry eventDescriptorRegistry) {
        this.eventDescriptorRegistry = eventDescriptorRegistry;
        this.subscriptionItems = new LinkedList<DefaultSubscriptionItemDef>();
    }

    public DefaultSubscriptionDef listenTo(Id sourceId) {
        inProgressListener = new DefaultSubscriptionItemDef();

        EventDescriptor eventDescriptor = eventDescriptorRegistry.getEventDescriptor(sourceId);
        inProgressListener.setEventDescriptor(eventDescriptor);

        return this;
    }

    public DefaultSubscriptionDef as(String listenerName) {
        inProgressListener.setName(listenerName);

        return this;
    }

    public DefaultSubscriptionDef use(String fieldName) {
        inProgressListener.setPropertyName(fieldName);

        return this;
    }

    public DefaultSubscriptionDef whereMatches(String fieldName, Comparable fieldValue) {
        inProgressListener.setPropertyName(fieldName);
        inProgressListener.setPropertyValue(fieldValue);

        return this;
    }

    public DefaultSubscriptionDef forwardTo(SubscriptionListener listener) {
        inProgressListener.setSubscriptionListener(listener);

        subscriptionItems.add(inProgressListener);
        inProgressListener = null;

        return this;
    }

    public DefaultSubscriptionDef alsoListenTo(Id sourceId) {
        return listenTo(sourceId);
    }

    public List<DefaultSubscriptionItemDef> getSubscriptionItems() {
        return subscriptionItems;
    }
}
