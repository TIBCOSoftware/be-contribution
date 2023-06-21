package com.tibco.cep.pattern.matcher.master;

import java.util.Set;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.pattern.matcher.stats.MergedSequenceStatsView;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Jul 6, 2009 Time: 5:00:58 PM
*/

public interface DriverOwner extends Recoverable<DriverOwner> {
    Id getOwnerId();

    Set<Id> getDriverCorrelationIds();
    
    Set<Id> getThreadLocalDriverCorrelationIds();

    boolean checkDriverCorrelationIdExists(Id id);

    /**
     * @param id
     * @return <code>null</code> if the driver does not exist.
     */
    DriverView getDriver(Id id);

    @Optional
    MergedSequenceStatsView getMergedSequenceStats();

    //--------------

    void start() throws LifecycleException;

    Response onInput(Source source, Id driverCorrelationId, Input input);

    Response onTimeOut(Id driverCorrelationId, TimeInput input);

    void stop() throws LifecycleException;
}
