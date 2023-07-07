package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.trace.SequenceRecorder;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 5:59:26 PM
*/
public class DefaultContext implements Context {
    protected Id flowId;

    protected DefaultCorrIdGenerator corrIdGenerator;

    protected Driver driver;

    protected SequenceRecorder sequenceRecorder;

    protected ResourceProvider resourceProvider;

    /**
     * @param resourceProvider
     * @param flowId
     * @param corrIdGenerator
     * @param driver
     */
    public DefaultContext(ResourceProvider resourceProvider, Id flowId,
                          DefaultCorrIdGenerator corrIdGenerator,
                          Driver driver) {
        this.resourceProvider = resourceProvider;
        this.flowId = flowId;
        this.corrIdGenerator = corrIdGenerator;
        this.driver = driver;
        this.sequenceRecorder = driver.getSequence();
    }

    public final Id getFlowId() {
        return flowId;
    }

    public final ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public final DefaultCorrIdGenerator getCorrelationIdGenerator() {
        return corrIdGenerator;
    }

    public final SequenceRecorder getSequenceRecorder() {
        return sequenceRecorder;
    }

    public final Driver getDriver() {
        return driver;
    }
}
