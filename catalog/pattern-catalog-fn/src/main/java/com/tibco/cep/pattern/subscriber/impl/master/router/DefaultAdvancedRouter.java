package com.tibco.cep.pattern.subscriber.impl.master.router;

import static com.tibco.cep.pattern.impl.util.Helper.$logger;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.impl.service.HashAsyncExecutor;
import com.tibco.cep.pattern.integ.impl.master.AbstractSubscriptionListener;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInput;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.subscriber.exception.RoutingException;
import com.tibco.cep.pattern.subscriber.impl.master.DefaultRoutingTable;
import com.tibco.cep.pattern.subscriber.impl.master.DefaultSubscriptionRecord;
import com.tibco.cep.pattern.subscriber.master.AdvancedRouter;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;
import com.tibco.cep.service.AsyncExecutor;
import com.tibco.cep.util.annotation.LogCategory;

/*
* Author: Ashwin Jayaprakash Date: Aug 19, 2009 Time: 11:41:25 AM
*/

@LogCategory("pattern.core.subscriber.router")
public class DefaultAdvancedRouter
        implements AdvancedRouter<DefaultRoutingTable, DefaultSubscriptionRecord> {
    protected Id resourceId;

    protected DefaultRoutingTable routingTable;

    protected transient AsyncExecutor asyncExecutor;

    protected transient boolean weCreatedAsyncExecutor;

    protected transient ResourceProvider resourceProvider;

    protected static transient Logger LOGGER;

    public DefaultAdvancedRouter(ResourceProvider resourceProvider) {
        this.resourceId = new DefaultId($category());
        this.routingTable = new DefaultRoutingTable(resourceProvider);
        this.resourceProvider = resourceProvider;

        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        this.weCreatedAsyncExecutor = false;
    }

    private String $category() {
        LogCategory logCategory = getClass().getAnnotation(LogCategory.class);
        if (logCategory != null) {
            return logCategory.value();
        }

        return getClass().getName();
    }

    public Id getResourceId() {
        return resourceId;
    }

    //-------------

    /**
     * Called by {@link #start()} and {@link #recover(ResourceProvider, Object...)} after the {@link #resourceId} has
     * been set.
     */
    protected AsyncExecutor createAsyncExecutorIfAbsent() {
        HashAsyncExecutor asyncExecutor = this.resourceProvider.fetchResource(HashAsyncExecutor.class);
        if (asyncExecutor != null) {
            return asyncExecutor;
        }

        //-------------

        asyncExecutor = new HashAsyncExecutor();

        String poolName = $category() + ".threadpool";

        Id id = new DefaultId(poolName);
        asyncExecutor.setResourceId(id);
        asyncExecutor.setThreadFamilyName(id.toString());
        asyncExecutor.setHashProvider(new DefaultRoutingJobHashProvider());
        final int numThreads = Math.max(4, Runtime.getRuntime().availableProcessors() / 3);
        asyncExecutor.setMaxThreads(numThreads);

        weCreatedAsyncExecutor = true;

        LOGGER.log(Level.INFO,
                "Started " + poolName + " with [" + numThreads + "] max threads");

        return asyncExecutor;
    }

    public void start() throws LifecycleException {
        routingTable.start();

        asyncExecutor = createAsyncExecutorIfAbsent();
        if (weCreatedAsyncExecutor) {
            asyncExecutor.start();
        }
    }

    public void stop() throws LifecycleException {
        if (weCreatedAsyncExecutor) {
            asyncExecutor.stop();
        }

        routingTable.stop();
    }

    //-------------

    public DefaultRoutingTable getRoutingTable() {
        return routingTable;
    }

    public <T> void routeMessage(EventSource<T> source, T message) throws RoutingException {
        routeMessage(source, message, null);
    }

    public <T> void routeMessage(EventSource<T> source, T message,
                                 Collection<? super Future<?>> routingJobs)
            throws RoutingException {
        EventDescriptor mainED = source.getEventDescriptor();

        collectForRouting(source, message, routingJobs, mainED);

        //-------------

        /*
        The same parent properties will appear in all the EDs:
        
         Parent.propA
           ^
           |
         Child.propA
           ^
           |
         GrandChild.propA

         This is perfectly fine as there could be different patterns with subscriptions at
         different levels:

         Pattern ABC = using Parent.propA

         Pattern DEF = using Child.propA

         Pattern XYZ = using GrandChild.propA         
        */

        EventDescriptor[] eventDescriptors = source.getAdditionalEventDescriptors();

        if (eventDescriptors != null) {
            for (EventDescriptor ed : eventDescriptors) {
                collectForRouting(source, message, routingJobs, ed);
            }
        }
    }

    private <T> void collectForRouting(EventSource<T> source, T message, Collection<? super Future<?>> routingJobs,
                                       EventDescriptor eventDescriptor) throws RoutingException {
        DefaultSubscriptionRecord subRec = routingTable.getSubscriptionRecord(eventDescriptor);

        if (subRec == null) {
            return;
        }

        //-------------

        Object messageId = eventDescriptor.extractUniqueId(message);

        String[] propertyNames = eventDescriptor.getSortedPropertyNames();
        for (String propertyName : propertyNames) {
            Comparable propertyValue = eventDescriptor.extractPropertyValue(message, propertyName);

            //todo Cache collections as array?
            Collection<SubscriptionListener> propSL = subRec.getSubscriptionListeners(propertyName, propertyValue);

            if (propSL != null) {
                for(SubscriptionListener sl : propSL) {
                    if (sl instanceof AbstractSubscriptionListener) {
                        this.wrapAndSubmit(source, messageId, message, (AbstractSubscriptionListener) sl, routingJobs);
                    } else {
                        //TODO???
                    }
                }
            }
        }

        //-------------

        //todo Cache collections as array?
        Collection<SubscriptionListener> plainListeners = subRec.getPlainSubscriptionListeners();

        int plainListenersSize = plainListeners.size();

        if (plainListenersSize > 0) {
            routeMessage(source, messageId, message, plainListeners, routingJobs);
        }
    }

    //-------------

    private void wrapAndSubmit(EventSource source,
                               Object messageId, Object message,
                               Collection<SubscriptionListener> listeners,
                               Collection<? super Future<?>> optionalRoutingJobs) {
        for (SubscriptionListener listener : listeners) {
            if (optionalRoutingJobs == null) {
                DefaultRoutingJob routingJob =
                        new DefaultRoutingJob(source, messageId, message, listener);

                asyncExecutor.submit(routingJob);

                continue;
            }

            //-------------

            DefaultRoutingJob routingJob =
                    new SyncChildrenExecRoutingJob(source, messageId, message, listener);

            FutureTask<?> futureTask = new FutureTask<Object>(routingJob);

            optionalRoutingJobs.add(futureTask);

            asyncExecutor.submit(futureTask);
        }
    }

    private void wrapAndSubmit(
            EventSource source,
            Object messageId,
            Object message,
            AbstractSubscriptionListener listener,
            Collection<? super Future<?>> optionalRoutingJobs)
    {
        if (optionalRoutingJobs == null) {
            final DefaultInput input = new DefaultInput(messageId);
            final DriverOwner driverOwner = listener.getDriverOwner();
            final DriverOwnerCallerJob callerJob = new DriverOwnerCallerJob(
                    listener.getSourceBridge(),
                    driverOwner,
                    input,
                    messageId,
                    message);

            this.asyncExecutor.submitToAllThreads(callerJob);

        } else {
            //TODO ???
            DefaultRoutingJob routingJob =
                    new SyncChildrenExecRoutingJob(source, messageId, message, listener);
            FutureTask<?> futureTask = new FutureTask<Object>(routingJob);
            optionalRoutingJobs.add(futureTask);
            asyncExecutor.submit(futureTask);
        }
    }


    protected void routeMessage(EventSource source,
                                Object messageId, Object message,
                                String propertyName,
                                Comparable propertyValue,
                                Collection<SubscriptionListener> listeners,
                                Collection<? super Future<?>> optionalRoutingJobs)
            throws RoutingException {
        wrapAndSubmit(source, messageId, message, listeners, optionalRoutingJobs);
    }

    protected void routeMessage(EventSource source,
                                Object messageId, Object message,
                                Collection<SubscriptionListener> listeners,
                                Collection<? super Future<?>> optionalRoutingJobs)
            throws RoutingException {
        wrapAndSubmit(source, messageId, message, listeners, optionalRoutingJobs);
    }

    //-------------

    public DefaultAdvancedRouter recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        this.resourceProvider = resourceProvider;

        this.routingTable = this.routingTable.recover(resourceProvider, params);

        this.asyncExecutor = createAsyncExecutorIfAbsent();
        //todo Huh? Not starting it after creating?

        return this;
    }
}
