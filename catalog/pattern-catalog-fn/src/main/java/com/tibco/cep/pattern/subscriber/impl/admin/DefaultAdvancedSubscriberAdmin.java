package com.tibco.cep.pattern.subscriber.impl.admin;

import java.util.Collection;
import java.util.LinkedList;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.log.LoggerService;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.log.DefaultLoggerService;
import com.tibco.cep.impl.common.resource.DefaultResourceProvider;
import com.tibco.cep.pattern.subscriber.admin.AdvancedSubscriberAdmin;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionItemDef;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionDeploymentDef;
import com.tibco.cep.pattern.subscriber.impl.master.AbstractRoutingTable;
import com.tibco.cep.pattern.subscriber.impl.master.AbstractSubscriptionCaretaker;
import com.tibco.cep.pattern.subscriber.impl.master.DefaultEventDescriptorRegistry;
import com.tibco.cep.pattern.subscriber.impl.master.DefaultSubscriptionCaretakerRegistry;
import com.tibco.cep.pattern.subscriber.impl.master.DefaultSubscriptionRecord;
import com.tibco.cep.pattern.subscriber.impl.master.router.DefaultAdvancedRouter;
import com.tibco.cep.pattern.subscriber.master.AdvancedRouter;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.Router;
import com.tibco.cep.pattern.subscriber.master.RoutingTable;
import com.tibco.cep.pattern.subscriber.master.SubscriptionCaretaker;
import com.tibco.cep.pattern.subscriber.master.SubscriptionCaretakerRegistry;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 5:17:56 PM
*/

public class DefaultAdvancedSubscriberAdmin
        implements AdvancedSubscriberAdmin<DefaultSubscriptionDeploymentDef> {
    protected DefaultSubscriptionCaretakerRegistry caretakerRegistry;

    protected DefaultAdvancedRouter router;

    protected DefaultEventDescriptorRegistry eventDescriptorRegistry;

    protected DefaultResourceProvider resourceProvider;

    @Optional
    protected LoggerService loggerServiceWeCreated;

    /**
     * Creates it's own {@link DefaultResourceProvider}.
     */
    public DefaultAdvancedSubscriberAdmin() {
        this.resourceProvider = new DefaultResourceProvider();
    }

    public DefaultAdvancedSubscriberAdmin(DefaultResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    //------------

    public void start() throws LifecycleException {
        LoggerService loggerService = resourceProvider.fetchResource(LoggerService.class);
        if (loggerService == null) {
            loggerService = new DefaultLoggerService();
            loggerService.start();

            resourceProvider.registerResource(LoggerService.class, loggerService);

            loggerServiceWeCreated = loggerService;
        }

        eventDescriptorRegistry = new DefaultEventDescriptorRegistry();
        eventDescriptorRegistry.start();
        resourceProvider.registerResource(EventDescriptorRegistry.class, eventDescriptorRegistry);

        caretakerRegistry = new DefaultSubscriptionCaretakerRegistry();
        caretakerRegistry.start();
        resourceProvider.registerResource(SubscriptionCaretakerRegistry.class, caretakerRegistry);

        router = new DefaultAdvancedRouter(resourceProvider);
        router.start();
        resourceProvider.registerResource(Router.class, router);
    }

    public void stop() throws LifecycleException {
        router.stop();

        caretakerRegistry.stop();

        eventDescriptorRegistry.stop();

        if (loggerServiceWeCreated != null) {
            loggerServiceWeCreated.stop();
        }
    }

    //---------------

    public AdvancedRouter<? extends RoutingTable, ?> getRouter() {
        return router;
    }

    //---------------

    public DefaultSubscriptionDeploymentDef createDeployment(Id id) {
        return new DefaultSubscriptionDeploymentDef(eventDescriptorRegistry, id);
    }

    public SubscriptionCaretaker deploy(DefaultSubscriptionDeploymentDef subscriptionDeployment)
            throws LifecycleException {
        AbstractSubscriptionCaretaker caretaker = subscriptionDeployment.build();

        boolean success = caretakerRegistry.addCaretaker(caretaker);

        if (success == false) {
            throw new LifecycleException(
                    "The Subscription Id [" + subscriptionDeployment.getId() +
                            "] is already in use.");
        }

        //------------

        try {
            addToRoutingTable(caretaker);
        }
        catch (LifecycleException e) {
            caretakerRegistry.removeCaretaker(caretaker);

            throw e;
        }

        //------------

        return caretaker;
    }

    protected void addToRoutingTable(AbstractSubscriptionCaretaker caretaker)
            throws LifecycleException {
        AbstractRoutingTable routingTable = router.getRoutingTable();

        Id caretakerId = caretaker.getId();

        Collection<? extends SubscriptionItemDef> subscriptionItems = caretaker.getSubscriptionItems();

        //------------

        LinkedList<SubscriptionItemDef> rollbackList = new LinkedList<SubscriptionItemDef>();
        LifecycleException exception = null;

        for (SubscriptionItemDef item : subscriptionItems) {
            //Use the main ED for the source.
            EventDescriptor ed = item.getEventDescriptor();
            DefaultSubscriptionRecord record = routingTable.getSubscriptionRecord(ed);

            if (record == null) {
                exception = new LifecycleException("The SubscriptionItem with the EventDescriptor ["
                        + item.getEventDescriptor() + "] could not be registered because" +
                        " it has no EventSource associated with it.");

                break;
            }

            if (item.isPropertyValueSet()) {
                record.addSubscriber(item.getPropertyName(), item.getPropertyValue(),
                        caretakerId, item.getSubscriptionListener());
            }
            else {
                record.addPlainSubscriber(caretakerId, item.getSubscriptionListener());
            }

            rollbackList.add(item);
        }

        //------------

        try {
            if (exception != null) {
                removeFromRoutingTable(caretakerId, rollbackList);

                throw exception;
            }
        }
        finally {
            rollbackList.clear();
        }
    }

    public Collection<? extends SubscriptionCaretaker> getCaretakers() {
        return caretakerRegistry.getCaretakers();
    }

    public SubscriptionCaretaker getCaretaker(Id id) {
        return caretakerRegistry.getCaretaker(id);
    }

    public void undeploy(SubscriptionCaretaker caretaker) {
        Id caretakerId = caretaker.getId();

        AbstractSubscriptionCaretaker registeredC =
                caretakerRegistry.getCaretaker(caretakerId);
        if (registeredC == null) {
            return;
        }

        removeFromRoutingTable(caretakerId, caretaker.getSubscriptionItems());

        caretakerRegistry.removeCaretaker(registeredC);
    }

    protected void removeFromRoutingTable(Id caretakerId,
                                          Collection<? extends SubscriptionItemDef> subscriptionItems) {
        AbstractRoutingTable routingTable = router.getRoutingTable();

        for (SubscriptionItemDef item : subscriptionItems) {
            DefaultSubscriptionRecord record =
                    routingTable.getSubscriptionRecord(item.getEventDescriptor());

            if (item.isPropertyValueSet()) {
                record.removeSubscriber(item.getPropertyName(), item.getPropertyValue(),
                        caretakerId);
            }
            else {
                record.removePlainSubscriber(caretakerId);
            }
        }
    }

    //---------------

    public void deploy(EventSource eventSource) throws LifecycleException {
        AbstractRoutingTable routingTable = router.getRoutingTable();

        routingTable.addEventSource(eventSource);
    }

    public Collection<? extends EventSource> getEventSources() {
        AbstractRoutingTable routingTable = router.getRoutingTable();

        return routingTable.getEventSources();
    }

    public void undeploy(EventSource eventSource) throws LifecycleException {
        AbstractRoutingTable routingTable = router.getRoutingTable();

        routingTable.removeEventSource(eventSource);
    }

    //---------------

    public void register(EventDescriptor eventDescriptor) throws LifecycleException {
        boolean success = eventDescriptorRegistry.addEventDescriptor(eventDescriptor);

        if (success == false) {
            throw new LifecycleException(
                    "The EventDescriptor [" + eventDescriptor +
                            "] could not be registered as an identical one already exists.");
        }
    }

    public EventDescriptor getEventDescriptor(Id id) {
        return eventDescriptorRegistry.getEventDescriptor(id);
    }

    public Collection<? extends EventDescriptor> getEventDescriptors() {
        return eventDescriptorRegistry.getEventDescriptors();
    }

    public void unregister(EventDescriptor eventDescriptor) throws LifecycleException {
        eventDescriptorRegistry.removeEventDescriptor(eventDescriptor);
    }

    //---------------

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }
}
