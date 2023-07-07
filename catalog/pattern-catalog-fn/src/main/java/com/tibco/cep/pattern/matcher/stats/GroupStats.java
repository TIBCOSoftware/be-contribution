package com.tibco.cep.pattern.matcher.stats;

/*
* Author: Ashwin Jayaprakash / Date: Oct 29, 2009 / Time: 1:17:04 PM
*/
public interface GroupStats extends GroupStatsView {
    void incrementInstanceCount();

    void addInstanceCount(int instanceCount);

    void incrementCompletedCount();

    void addCompletedCount(int completedCount);

    void merge(GroupStats givenStats);
}
