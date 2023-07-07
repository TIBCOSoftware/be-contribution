package com.tibco.cep.pattern.subscriber.dsl;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 5:11:46 PM
*/
public interface SubscriptionItemDef extends Recoverable<SubscriptionItemDef> {
    String getName();

    EventDescriptor getEventDescriptor();

    String getPropertyName();

    Comparable getPropertyValue();

    boolean isPropertyValueSet();

    SubscriptionListener getSubscriptionListener();
}
