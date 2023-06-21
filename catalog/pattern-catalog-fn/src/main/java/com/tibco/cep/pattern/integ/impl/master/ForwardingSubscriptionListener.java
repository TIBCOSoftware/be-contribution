package com.tibco.cep.pattern.integ.impl.master;

import java.util.Collection;
import java.util.concurrent.Future;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.integ.master.CorrelationIdExtractor;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInput;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 27, 2009 Time: 11:08:58 AM
*/
public class ForwardingSubscriptionListener extends AbstractSubscriptionListener {
    protected CorrelationIdExtractor<Object> correlationIdExtractor;

    public ForwardingSubscriptionListener(DefaultSourceBridge<Object> sourceBridge,
                                          CorrelationIdExtractor<Object> correlationIdExtractor) {
        super(sourceBridge);

        this.correlationIdExtractor = correlationIdExtractor;
    }

    public CorrelationIdExtractor<Object> getCorrelationIdExtractor() {
        return correlationIdExtractor;
    }

    public void start(ResourceProvider resourceProvider, Id subscriptionId) {
        super.start(resourceProvider, subscriptionId);

        correlationIdExtractor.start(resourceProvider, subscriptionId, sourceBridge);
    }

    public void onMessage(Object messageId, Object message) {
        Id driverCorrelationId = correlationIdExtractor.extract(message);

        Response response =
                driverOwner.onInput(sourceBridge, driverCorrelationId, new DefaultInput(messageId));

        //-------------

        if (Flags.DEBUG) {
            System.out.println("Message [" + messageId + "] to alias [" + sourceBridge.getAlias() +
                    "] with subscription [" + driverOwner.getOwnerId() + "] received response [" +
                    response + "].");
        }
    }

    /**
     * Just calls {@link #onMessage(Object, Object)} because there are no nested jobs.
     *
     * @param messageId
     * @param message
     * @param routingJobs
     * @throws Exception
     */
    public void onMessage(Object messageId, Object message,
                          @Optional Collection<? super Future<?>> routingJobs) throws Exception {
        onMessage(messageId, message);
    }

    public void stop() {
        correlationIdExtractor.stop();

        super.stop();
    }

    //-------------

    public ForwardingSubscriptionListener recover(ResourceProvider resourceProvider,
                                                  Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        correlationIdExtractor = correlationIdExtractor.recover(resourceProvider, params);

        return this;
    }
}
