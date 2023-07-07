package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;
import java.util.concurrent.Future;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 13, 2009 Time: 4:05:57 PM
*/
public interface SubscriptionListener extends Recoverable<SubscriptionListener> {
    void start(ResourceProvider resourceProvider, Id subscriptionId);

    void onMessage(Object messageId, Object message) throws Exception;

    /**
     * @param messageId
     * @param message
     * @param routingJobs If any nested jobs are created, then they get added to this list for the
     *                    caller to track.
     * @throws Exception
     */
    void onMessage(Object messageId, Object message,
                   @Optional Collection<? super Future<?>> routingJobs) throws Exception;

    void stop();
}
