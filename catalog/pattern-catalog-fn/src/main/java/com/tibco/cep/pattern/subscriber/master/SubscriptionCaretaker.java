package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionItemDef;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 2:33:55 PM
*/
public interface SubscriptionCaretaker extends Recoverable<SubscriptionCaretaker> {
    Id getId();

    //--------------

    void start();

    void stop();

    //--------------

    Collection<? extends SubscriptionItemDef> getSubscriptionItems();
}
