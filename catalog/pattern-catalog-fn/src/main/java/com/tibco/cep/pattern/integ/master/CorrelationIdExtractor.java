package com.tibco.cep.pattern.integ.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 4:44:39 PM
*/
public interface CorrelationIdExtractor<T> extends Recoverable<CorrelationIdExtractor<T>> {
    void start(ResourceProvider resourceProvider, Id subscriptionId, SourceBridge<T> sourceBridge);

    Id extract(T source);

    void stop();
}
