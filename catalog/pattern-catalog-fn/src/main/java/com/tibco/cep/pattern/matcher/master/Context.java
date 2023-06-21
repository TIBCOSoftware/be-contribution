package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.trace.SequenceRecorder;
import com.tibco.cep.service.IdGenerator;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 5:57:56 PM
*/
public interface Context {
    /**
     * The flow's correlation-id.
     *
     * @return
     */
    Id getFlowId();

    IdGenerator getCorrelationIdGenerator();

    SequenceRecorder getSequenceRecorder();

    Driver getDriver();

    ResourceProvider getResourceProvider();
}
