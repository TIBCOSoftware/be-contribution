package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.TimeSource;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.service.AsyncScheduler;

/*
* Author: Ashwin Jayaprakash Date: Jul 16, 2009 Time: 4:55:14 PM
*/
public interface InternalTimeSource extends TimeSource {
    void recordExpectedTimeInput(Id driverCorrelationId, ExpectedTimeInput expectedTimeInput);

    //-----------

    boolean isSchedulerDedicated();

    AsyncScheduler getScheduler();

    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    InternalTimeSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException;
}
