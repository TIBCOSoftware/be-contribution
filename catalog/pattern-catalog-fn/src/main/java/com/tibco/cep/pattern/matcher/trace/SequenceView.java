package com.tibco.cep.pattern.matcher.trace;

import java.util.List;

import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 10:59:19 AM
*/
public interface SequenceView {
    boolean hasCompleted();

    @Optional
    RecordedOccurrence getFirstRecordedOccurrence();

    @Optional
    RecordedOccurrence getRecentRecordedOccurrence();

    /**
     * @return The default group that is always there and all sequence-members are part of - either
     *         directly or as some nested child level.
     */
    @Optional
    Group getSuperGroup();

    /**
     * @return The "stack" of groups that are currently open. The outer most group will be the first
     *         element and the current nested group will be the last element in the list.
     */
    @Optional
    List<? extends Group> getOpenGroupStack();
}