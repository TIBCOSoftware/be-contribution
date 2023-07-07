package com.tibco.cep.pattern.integ.impl.master;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInput;
import com.tibco.cep.pattern.subscriber.impl.master.router.DriverOwnerCallerJob;
import com.tibco.cep.service.AsyncExecutor;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 27, 2009 Time: 11:08:58 AM
*/
public class BroadcastingSubscriptionListener extends AbstractSubscriptionListener {
    protected transient AsyncExecutor asyncExecutor;

    public BroadcastingSubscriptionListener(DefaultSourceBridge<Object> sourceBridge) {
        super(sourceBridge);
    }

    @Override
    public void start(ResourceProvider resourceProvider, Id subscriptionId) {
        super.start(resourceProvider, subscriptionId);

//        asyncExecutor = resourceProvider.fetchResource(AsyncExecutor.class);
    }

    public void onMessage(Object messageId, Object message) throws Exception {
        onMessage(messageId, message, null);
    }

    public void onMessage(Object messageId, Object message,
                          @Optional Collection<? super Future<?>> routingJobs) throws Exception {
        DefaultInput input = new DefaultInput(messageId);

        DriverOwnerCallerJob callerJob = new DriverOwnerCallerJob(sourceBridge, driverOwner, input, messageId, message);

        if (routingJobs == null) {
            asyncExecutor.submitToAllThreads(callerJob);
        }
        else {
            FutureTask<?> futureTask = new FutureTask<Object>(callerJob);

            routingJobs.add(futureTask);

            asyncExecutor.submit(futureTask);
        }
    }

    @Override
    public void stop() {
        super.stop();

        asyncExecutor = null;
    }

    @Override
    public BroadcastingSubscriptionListener recover(ResourceProvider resourceProvider,
                                                    Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        return this;
    }

    //-------------

}