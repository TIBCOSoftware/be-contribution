package com.tibco.cep.pattern.integ.impl.jmx;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.jmx.XDriverOwnerMBean;
import com.tibco.cep.pattern.matcher.master.AdvancedDriverOwner;
import com.tibco.cep.pattern.matcher.stats.GroupStatsView;
import com.tibco.cep.pattern.matcher.stats.MergedSequenceStats;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 4:27:07 PM
*/
public class XDriverOwner implements XDriverOwnerMBean {
    protected AdvancedDriverOwner advancedDriverOwner;

    public XDriverOwner(AdvancedDriverOwner advancedDriverOwner) {
        this.advancedDriverOwner = advancedDriverOwner;
    }

    public String[] getDriverIds() {
        Set<Id> driverCorrelationIdSet = advancedDriverOwner.getDriverCorrelationIds();

        LinkedList<String> driverCorrelationIdList = new LinkedList<String>();
        for (Id id : driverCorrelationIdSet) {
            driverCorrelationIdList.add(id.toString());
        }

        String[] driverIds =
                driverCorrelationIdList.toArray(new String[driverCorrelationIdList.size()]);

        return driverIds;
    }

    @Override
    public String getOwnerId() {
        return advancedDriverOwner.getOwnerId().toString();
    }

    @Override
    public String getSequenceStats() {
        SequenceStats stats = fetchSequenceStats();

        return stats.toString();
    }

    @Override
    public SequenceStats fetchSequenceStats() {
        String ownerId = getOwnerId();
        SequenceStatsComponent[] components = getSequenceStatsComponents();

        return new SequenceStatsImpl(ownerId, components);
    }

    protected SequenceStatsComponent[] getSequenceStatsComponents() {
        MergedSequenceStats sequenceStats = advancedDriverOwner.getMergedSequenceStats();
        if (sequenceStats == null) {
            return new SequenceStatsComponent[]{};
        }

        //-------------

        LinkedList<SequenceStatsComponentImpl> statsComponents =
                new LinkedList<SequenceStatsComponentImpl>();

        ConcurrentMap<Id, ? extends GroupStatsView> allGsv = sequenceStats.getConsolidatedStats();

        Id superGroupId = sequenceStats.getSuperGroupId();
        GroupStatsView superGroupGSV = allGsv.get(superGroupId);
        SequenceStatsComponentImpl superGroupStatsComponent = null;

        for (Entry<Id, ? extends GroupStatsView> entry : allGsv.entrySet()) {
            GroupStatsView gsv = entry.getValue();

            String id = gsv.getId().toString();
            int instanceCount = gsv.getInstanceCount();
            int completedCount = gsv.getCompletedCount();

            SequenceStatsComponentImpl statsComponent =
                    new SequenceStatsComponentImpl(id, instanceCount, completedCount);

            if (gsv != superGroupGSV) {
                statsComponents.add(statsComponent);
            }
            else {
                superGroupStatsComponent = statsComponent;
            }
        }

        //The super group stats is always the first element.
        if (superGroupStatsComponent != null) {
            statsComponents.add(0, superGroupStatsComponent);
        }

        return statsComponents.toArray(new SequenceStatsComponent[statsComponents.size()]);
    }

    //-------------

    public static class SequenceStatsImpl implements SequenceStats {
        protected String ownerId;

        protected SequenceStatsComponent[] components;

        public SequenceStatsImpl(String ownerId, SequenceStatsComponent[] components) {
            this.ownerId = ownerId;
            this.components = components;
        }

        @Override
        public String getOwnerId() {
            return ownerId;
        }

        @Override
        public SequenceStatsComponent[] getComponents() {
            return components;
        }

        @Override
        public String toString() {
            String s = "Sequence stats:\n  ownerId=" + ownerId;

            if (components.length == 0) {
                s = s + "\n  <No component stats>";
            }
            for (SequenceStatsComponent component : components) {
                s = s + "\n  " + component;
            }

            return s;
        }
    }

    public static class SequenceStatsComponentImpl implements SequenceStatsComponent {
        protected String id;

        protected int instanceCount;

        protected int completedCount;

        public SequenceStatsComponentImpl(String id, int instanceCount, int completedCount) {
            this.id = id;
            this.instanceCount = instanceCount;
            this.completedCount = completedCount;
        }

        public String getId() {
            return id;
        }

        public int getInstanceCount() {
            return instanceCount;
        }

        public int getCompletedCount() {
            return completedCount;
        }

        @Override
        public String toString() {
            return "id=" + id + ", instanceCount=" + instanceCount + ", completedCount=" +
                    completedCount;
        }
    }
}
