package com.tibco.cep.pattern.subscriber.impl.dsl;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.subscriber.impl.master.AbstractSubscriptionCaretaker;
import com.tibco.cep.pattern.subscriber.impl.master.DefaultSubscriptionCaretaker;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 4:10:16 PM
*/
public class DefaultSubscriptionDeploymentDef extends AbstractSubscriptionDeploymentDef {
    public DefaultSubscriptionDeploymentDef(EventDescriptorRegistry eventDescriptorRegistry,
                                            Id id) {
        super(eventDescriptorRegistry, id);
    }

    /**
     * Just creates a caretaker.
     *
     * @return
     * @throws LifecycleException
     */
    public AbstractSubscriptionCaretaker build() throws LifecycleException {
        DefaultSubscriptionCaretaker caretaker = new DefaultSubscriptionCaretaker();

        caretaker.setId(id);
        caretaker.setSubscriptionItems(subscription.getSubscriptionItems());

        return caretaker;
    }
}
