package com.tibco.cep.pattern.integ.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.integ.master.SourceBridge;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.matcher.master.DriverOwnerRegistry;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Sep 2, 2009 Time: 4:07:59 PM
*/
public abstract class AbstractSubscriptionListener implements SubscriptionListener {
    protected DefaultSourceBridge<Object> sourceBridge;

    protected transient DriverOwner driverOwner;

    protected AbstractSubscriptionListener(DefaultSourceBridge<Object> sourceBridge) {
        this.sourceBridge = sourceBridge;
    }

    //-------------

    public void start(ResourceProvider resourceProvider, Id subscriptionId) {
        DriverOwnerRegistry driverOwnerRegistry =
                resourceProvider.fetchResource(DriverOwnerRegistry.class);

        driverOwner = driverOwnerRegistry.getDriverOwner(subscriptionId);
    }

    public void stop() {
        driverOwner = null;
    }

    //-------------

    public AbstractSubscriptionListener recover(ResourceProvider resourceProvider,
                                                Object... params)
            throws RecoveryException {
        sourceBridge = sourceBridge.recover(resourceProvider, params);

        return this;
    }


    public DriverOwner getDriverOwner()
    {
        return this.driverOwner;
    }


    public SourceBridge<Object> getSourceBridge()
    {
        return this.sourceBridge;
    }
}
