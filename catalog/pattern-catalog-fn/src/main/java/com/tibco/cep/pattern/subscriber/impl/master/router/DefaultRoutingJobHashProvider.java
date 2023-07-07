package com.tibco.cep.pattern.subscriber.impl.master.router;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.impl.service.HashAsyncExecutor;
import com.tibco.cep.pattern.integ.impl.master.BroadcastingSubscriptionListener;
import com.tibco.cep.pattern.integ.impl.master.ForwardingSubscriptionListener;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;


/**
 * User: nicolas
 * Date: 8/22/14
 * Time: 12:39 PM
 */
public class DefaultRoutingJobHashProvider
    implements HashAsyncExecutor.HashProvider
{


    @Override
    public int getHash(
            Object task)
    {
        if (null == task) {
            return 0;
        }

        if (task instanceof DefaultRoutingJob) {
            final DefaultRoutingJob routingJob = (DefaultRoutingJob) task;
            final SubscriptionListener listener = routingJob.getListener();

            if (listener instanceof ForwardingSubscriptionListener) {
                final Id correlationId = ((ForwardingSubscriptionListener) listener)
                        .getCorrelationIdExtractor()
                        .extract(routingJob.getMessage());
                return correlationId.hashCode();
//            } else if (listener instanceof BroadcastingSubscriptionListener) {
//                final BroadcastingSubscriptionListener bsl = ((BroadcastingSubscriptionListener) listener);
            }

        }

        return task.hashCode();
    }

}
