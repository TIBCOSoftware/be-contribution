package com.tibco.cep.pattern.subscriber.admin;

import java.util.Collection;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionDeploymentDef;
import com.tibco.cep.pattern.subscriber.master.AdvancedRouter;
import com.tibco.cep.pattern.subscriber.master.RoutingTable;
import com.tibco.cep.pattern.subscriber.master.SubscriptionCaretaker;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 12:55:22 PM
*/
public interface AdvancedSubscriberAdmin<D extends SubscriptionDeploymentDef>
        extends SubscriberAdmin {
    AdvancedRouter<? extends RoutingTable, ?> getRouter();

    //------------

    D createDeployment(Id id);

    SubscriptionCaretaker deploy(D subscriptionDeployment) throws LifecycleException;

    Collection<? extends SubscriptionCaretaker> getCaretakers();

    /**
     * @param id From {@link SubscriptionCaretaker#getId()}.
     * @return
     */
    SubscriptionCaretaker getCaretaker(Id id);

    void undeploy(SubscriptionCaretaker caretaker) throws LifecycleException;
}
