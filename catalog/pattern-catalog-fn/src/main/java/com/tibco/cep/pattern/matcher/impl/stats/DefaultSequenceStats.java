package com.tibco.cep.pattern.matcher.impl.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.stats.SequenceStats;
import com.tibco.cep.pattern.matcher.trace.Group;
import com.tibco.cep.pattern.matcher.trace.SequenceMember;
import com.tibco.cep.pattern.matcher.trace.SequenceView;

/*
* Author: Ashwin Jayaprakash Date: Jul 31, 2009 Time: 11:43:37 AM
*/
public class DefaultSequenceStats implements SequenceStats {
    protected HashMap<Id, DefaultGroupStats> stats;

    protected Id superGroupId;

    /**
     * Uses the full group sequence starting from {@link SequenceView#getSuperGroup()}. If these
     * details are not present then the getters {@link #getStats()} and {@link #getSuperGroupId()}
     * will return <code>null</code>.
     *
     * @param sequenceView
     */
    public DefaultSequenceStats(SequenceView sequenceView) {
        Group group = sequenceView.getSuperGroup();

        if (group != null) {
            this.stats = new HashMap<Id, DefaultGroupStats>(8);

            calculate(group);

            this.superGroupId = group.getGroupId();
        }
    }

    public Map<Id, DefaultGroupStats> getStats() {
        return stats;
    }

    public Id getSuperGroupId() {
        return superGroupId;
    }

    private void calculate(Group group) {
        increment(group);

        //--------------

        List<? extends SequenceMember> members = group.getMembers();
        if (members.isEmpty()) {
            return;
        }

        for (SequenceMember member : members) {
            if (member instanceof Group) {
                Group child = (Group) member;

                calculate(child);
            }
            else {
                //todo member? This has no Id.
            }
        }
    }

    private void increment(Group group) {
        Id id = group.getGroupId();

        DefaultGroupStats groupStats = stats.get(id);

        if (groupStats == null) {
            Group parent = group.getParent();

            groupStats = (parent == null) ?
                    new DefaultGroupStats(id) : new DefaultGroupStats(id, parent.getGroupId());

            stats.put(id, groupStats);
        }

        groupStats.incrementInstanceCount();

        if (group.hasCompleted()) {
            groupStats.incrementCompletedCount();
        }
    }
}