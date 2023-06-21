package com.tibco.cep.pattern.subscriber.impl.dsl;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionDeploymentDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 2:01:05 PM
*/
public abstract class AbstractSubscriptionDeploymentDef implements
        SubscriptionDeploymentDef<AbstractSubscriptionDeploymentDef, DefaultSubscriptionDef> {
    protected EventDescriptorRegistry eventDescriptorRegistry;

    protected DefaultSubscriptionDef subscription;

    protected Id id;

    protected AbstractSubscriptionDeploymentDef(EventDescriptorRegistry eventDescriptorRegistry,
                                                Id id) {
        this.eventDescriptorRegistry = eventDescriptorRegistry;
        this.id = id;
    }

    //------------

    public DefaultSubscriptionDef create() {
        return new DefaultSubscriptionDef(eventDescriptorRegistry);
    }

    public AbstractSubscriptionDeploymentDef setSubscription(DefaultSubscriptionDef subscription) {
        this.subscription = subscription;

        return this;
    }


    public Id getId() {
        return id;
    }

    public DefaultSubscriptionDef getSubscription() {
        return subscription;
    }
}
