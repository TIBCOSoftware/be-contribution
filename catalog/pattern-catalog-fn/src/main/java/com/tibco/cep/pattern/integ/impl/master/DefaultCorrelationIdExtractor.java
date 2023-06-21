package com.tibco.cep.pattern.integ.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.integ.master.CorrelationIdExtractor;
import com.tibco.cep.pattern.integ.master.SourceBridge;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash Date: Aug 31, 2009 Time: 4:50:02 PM
*/

/**
 * Uses the {@link EventDescriptor#extractPropertyValue(Object, String)} to extract the value and
 * then wraps the value in {@link DefaultId}.
 * <p/>
 * Gets hold of the {@link EventDescriptor} by means of the {@link SourceBridge} provided in the
 * {@link #start(ResourceProvider, Id , SourceBridge)} method.
 */
public class DefaultCorrelationIdExtractor implements CorrelationIdExtractor<Object> {
    protected String propertyName;

    protected transient EventDescriptor<Object> eventDescriptor;

    public DefaultCorrelationIdExtractor(String propertyName) {
        this.propertyName = propertyName;
    }

    public void start(ResourceProvider resourceProvider, Id subscriptionId,
                      SourceBridge<Object> sourceBridge) {
        eventDescriptor = sourceBridge.getEventDescriptor();
    }

    public Id extract(Object source) {
        Comparable value = eventDescriptor.extractPropertyValue(source, propertyName);

        return new DefaultId(value);
    }

    public void stop() {
    }

    //-------------

    public DefaultCorrelationIdExtractor recover(ResourceProvider resourceProvider,
                                                 Object... params)
            throws RecoveryException {
        return this;
    }
}
