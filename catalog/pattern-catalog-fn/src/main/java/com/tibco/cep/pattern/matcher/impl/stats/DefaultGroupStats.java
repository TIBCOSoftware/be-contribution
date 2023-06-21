package com.tibco.cep.pattern.matcher.impl.stats;

import java.util.concurrent.atomic.AtomicInteger;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.stats.GroupStats;
import com.tibco.cep.util.annotation.ThreadSafe;

/*
* Author: Ashwin Jayaprakash / Date: Oct 28, 2009 / Time: 3:55:41 PM
*/

@ThreadSafe
public class DefaultGroupStats implements GroupStats {
    protected Id id;

    protected Id parentId;

    protected AtomicInteger completedCount;

    protected AtomicInteger instanceCount;

    public DefaultGroupStats(Id id) {
        this(id, null);
    }

    public DefaultGroupStats(Id id, Id parentId) {
        this.id = id;
        this.parentId = parentId;

        this.completedCount = new AtomicInteger();
        this.instanceCount = new AtomicInteger();
    }

    public Id getId() {
        return id;
    }

    public Id getParentId() {
        return parentId;
    }

    public int getCompletedCount() {
        return completedCount.get();
    }

    public int getInstanceCount() {
        return instanceCount.get();
    }

    public void incrementInstanceCount() {
        this.instanceCount.incrementAndGet();
    }

    public void addInstanceCount(int instanceCount) {
        this.instanceCount.addAndGet(instanceCount);
    }

    public void incrementCompletedCount() {
        this.completedCount.incrementAndGet();
    }

    public void addCompletedCount(int completedCount) {
        this.completedCount.addAndGet(completedCount);
    }

    public void merge(GroupStats givenStats) {
        this.instanceCount.addAndGet(givenStats.getInstanceCount());

        this.completedCount.addAndGet(givenStats.getCompletedCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultGroupStats)) {
            return false;
        }

        DefaultGroupStats that = (DefaultGroupStats) o;

        if (!completedCount.equals(that.completedCount)) {
            return false;
        }
        if (!id.equals(that.id)) {
            return false;
        }
        if (!instanceCount.equals(that.instanceCount)) {
            return false;
        }
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + completedCount.hashCode();
        result = 31 * result + instanceCount.hashCode();
        return result;
    }
}
