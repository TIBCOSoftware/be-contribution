package com.tibco.cep.pattern.matcher.stats;

import java.util.concurrent.ConcurrentMap;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.util.annotation.ThreadSafe;

/*
* Author: Ashwin Jayaprakash / Date: Oct 29, 2009 / Time: 1:09:30 PM
*/

@ThreadSafe
public interface MergedSequenceStats extends MergedSequenceStatsView {
    ConcurrentMap<Id, ? extends GroupStats> getConsolidatedStats();

    /**
     * @param sequenceStats The values may used by reference. Do not share these values after
     *                      calling this method.
     */
    void merge(SequenceStats sequenceStats);
}
