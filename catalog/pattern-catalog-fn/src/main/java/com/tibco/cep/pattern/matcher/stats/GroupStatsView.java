package com.tibco.cep.pattern.matcher.stats;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 12:57:32 PM
*/
public interface GroupStatsView {
    Id getId();

    Id getParentId();

    int getCompletedCount();

    int getInstanceCount();
}
