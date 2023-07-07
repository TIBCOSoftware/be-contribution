package com.tibco.cep.pattern.matcher.impl.stats;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.matcher.stats.GroupStats;
import com.tibco.cep.pattern.matcher.stats.MergedSequenceStats;
import com.tibco.cep.pattern.matcher.stats.SequenceStats;

/*
* Author: Ashwin Jayaprakash / Date: Oct 28, 2009 / Time: 3:38:40 PM
*/

/**
 * Thread safe.
 */
public class DefaultMergedSequenceStats implements MergedSequenceStats {
    protected Id containerId;

    protected ConcurrentHashMap<Id, GroupStats> consolidatedStats;

    protected volatile Id superGroupId;

    public DefaultMergedSequenceStats(Id containerId) {
        this.containerId = containerId;
        this.consolidatedStats = new ConcurrentHashMap<Id, GroupStats>(8);
    }

    public Id getContainerId() {
        return containerId;
    }

    public ConcurrentMap<Id, ? extends GroupStats> getConsolidatedStats() {
        return consolidatedStats;
    }

    public Id getSuperGroupId() {
        return superGroupId;
    }

    public void merge(SequenceStats sequenceStats) {
        Map<Id, ? extends GroupStats> givenStats = sequenceStats.getStats();

        for (Entry<Id, ? extends GroupStats> entry : givenStats.entrySet()) {
            Id key = entry.getKey();
            GroupStats value = entry.getValue();

            GroupStats existingValue = consolidatedStats.get(key);

            if (existingValue == null) {
                existingValue = consolidatedStats.putIfAbsent(key, value);

                if (existingValue != null) {
                    doMerge(existingValue, value);
                }

                /*
                This will be checked the first few times only. After which, all the groups should
                be filled and will go to the else block.
                */
                if (superGroupId == null) {
                    superGroupId = sequenceStats.getSuperGroupId();
                }
            }
            else {
                doMerge(existingValue, value);
            }
        }
    }

    private static void doMerge(GroupStats existingValue, GroupStats value) {
        if (Flags.TEST_ILLEGAL_STATES) {
            test(existingValue, value);
        }

        existingValue.merge(value);
    }

    private static void test(GroupStats existingValue, GroupStats value) {
        Id thisParent = existingValue.getParentId();
        Id thatParent = value.getParentId();

        if ((thatParent != null && thisParent != null && thatParent.equals(thisParent) == false)
                || (thatParent == null && thatParent != thisParent)) {

            throw new AssertionError("Group parent Ids are not the same: ["
                    + thatParent + "] and [" + thisParent +
                    "] although the group Ids are.");
        }
    }
}
