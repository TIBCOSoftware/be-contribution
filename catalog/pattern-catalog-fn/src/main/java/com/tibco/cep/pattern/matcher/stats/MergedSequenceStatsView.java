package com.tibco.cep.pattern.matcher.stats;

import java.util.concurrent.ConcurrentMap;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 12:58:12 PM
*/
public interface MergedSequenceStatsView {
    Id getContainerId();

    ConcurrentMap<Id, ? extends GroupStatsView> getConsolidatedStats();

    /**
     * Returns <code>null</code> until there are some values in the {@link #getConsolidatedStats()
     * stats}.
     *
     * @return
     */
    Id getSuperGroupId();
}
