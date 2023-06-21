package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.stats.MergedSequenceStats;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Jul 31, 2009 Time: 4:07:48 PM
*/
public interface AdvancedDriverOwner extends DriverOwner {
    Plan getPlan();

    ResourceProvider getResourceProvider();

    /**
     * @param id
     * @return <code>null</code> if the driver does not exist.
     */
    Driver getDriver(Id id);

    @Optional
    MergedSequenceStats getMergedSequenceStats();
}
