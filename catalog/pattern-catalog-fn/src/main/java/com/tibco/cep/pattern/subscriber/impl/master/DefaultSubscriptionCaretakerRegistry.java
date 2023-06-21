package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.subscriber.master.SubscriptionCaretakerRegistry;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 1:03:20 PM
*/
public class DefaultSubscriptionCaretakerRegistry
        implements SubscriptionCaretakerRegistry<AbstractSubscriptionCaretaker> {
    protected ConcurrentHashMap<Id, AbstractSubscriptionCaretaker> deployments;

    protected Id resourceId;

    public DefaultSubscriptionCaretakerRegistry() {
        this.deployments = new ConcurrentHashMap<Id, AbstractSubscriptionCaretaker>();
        this.resourceId = new DefaultId(getClass().getName());
    }

    public Id getResourceId() {
        return resourceId;
    }

    //-------------

    public void start() throws LifecycleException {
    }

    public void stop() throws LifecycleException {
    }

    //-------------

    public boolean addCaretaker(AbstractSubscriptionCaretaker caretaker) {
        Object existing = deployments.putIfAbsent(caretaker.getId(), caretaker);

        if (existing == null) {
            caretaker.start();

            return true;
        }

        return false;
    }

    public AbstractSubscriptionCaretaker getCaretaker(Id id) {
        return deployments.get(id);
    }

    public Collection<AbstractSubscriptionCaretaker> getCaretakers() {
        return new LinkedList<AbstractSubscriptionCaretaker>(deployments.values());
    }

    public void removeCaretaker(AbstractSubscriptionCaretaker caretaker) {
        AbstractSubscriptionCaretaker registeredCT = deployments.remove(caretaker.getId());

        if (registeredCT != null) {
            registeredCT.stop();
        }
    }

    //-------------

    public DefaultSubscriptionCaretakerRegistry recover(ResourceProvider resourceProvider,
                                                        Object... params)
            throws RecoveryException {
        for (Entry<Id, AbstractSubscriptionCaretaker> entry : deployments.entrySet()) {
            AbstractSubscriptionCaretaker caretaker = entry.getValue();

            caretaker = caretaker.recover(resourceProvider, params);

            entry.setValue(caretaker);
        }

        return this;
    }
}
