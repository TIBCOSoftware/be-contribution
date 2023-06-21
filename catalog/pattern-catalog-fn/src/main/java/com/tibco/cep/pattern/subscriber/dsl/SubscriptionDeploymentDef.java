package com.tibco.cep.pattern.subscriber.dsl;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash Date: Aug 12, 2009 Time: 6:41:30 PM
*/
public interface SubscriptionDeploymentDef<D extends SubscriptionDeploymentDef, S extends SubscriptionDef> {
    S create();

    //--------------

    D setSubscription(S subscription);

    //--------------

    Id getId();

    S getSubscription();
}
