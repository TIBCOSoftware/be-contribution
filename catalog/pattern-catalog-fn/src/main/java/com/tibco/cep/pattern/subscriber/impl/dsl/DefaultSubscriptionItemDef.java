package com.tibco.cep.pattern.subscriber.impl.dsl;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionItemDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 3:44:51 PM
*/

/**
 * The {@link #getName()} has to be unique for each instance in a set.
 */
public class DefaultSubscriptionItemDef implements SubscriptionItemDef {
    protected String name;

    protected EventDescriptor<?> eventDescriptor;

    protected String propertyName;

    protected Comparable propertyValue;

    protected boolean propertyValueSet;

    protected SubscriptionListener subscriptionListener;

    public DefaultSubscriptionItemDef() {
    }

    public String getName() {
        return name;
    }

    public EventDescriptor getEventDescriptor() {
        return eventDescriptor;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Comparable getPropertyValue() {
        return propertyValue;
    }

    public boolean isPropertyValueSet() {
        return propertyValueSet;
    }

    public SubscriptionListener getSubscriptionListener() {
        return subscriptionListener;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEventDescriptor(EventDescriptor eventDescriptor) {
        this.eventDescriptor = eventDescriptor;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPropertyValue(Comparable propertyValue) {
        this.propertyValue = propertyValue;
        this.propertyValueSet = true;
    }

    public void setSubscriptionListener(SubscriptionListener subscriptionListener) {
        this.subscriptionListener = subscriptionListener;
    }

    //------------

    public DefaultSubscriptionItemDef recover(ResourceProvider resourceProvider,
                                              Object... params)
            throws RecoveryException {
        eventDescriptor = eventDescriptor.recover(resourceProvider, params);
        subscriptionListener = subscriptionListener.recover(resourceProvider, params);

        return this;
    }

    //------------

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSubscriptionItemDef)) {
            return false;
        }

        DefaultSubscriptionItemDef that = (DefaultSubscriptionItemDef) o;

        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
