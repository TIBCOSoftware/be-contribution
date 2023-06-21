package com.tibco.cep.pattern.matcher.impl.trace;

import java.util.List;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.trace.Group;
import com.tibco.cep.pattern.matcher.trace.RecordedOccurrence;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Oct 6, 2009 Time: 11:40:12 AM
*/

/**
 * Most methods here will return <code>null</code> and are no-ops except {@link #hasCompleted()} and
 * {@link #markCompleted()}.
 */
public class LiteSequence implements Sequence {
    protected boolean completed;

    public LiteSequence() {
    }

    public RecordedOccurrence getFirstRecordedOccurrence() {
        return null;
    }

    public RecordedOccurrence getRecentRecordedOccurrence() {
        return null;
    }

    public Group getSuperGroup() {
        return null;
    }

    public List<? extends Group> getOpenGroupStack() {
        return null;
    }

    public boolean hasCompleted() {
        return completed;
    }

    public void recordGroupStart(Id groupId) {
    }

    public void recordOccurrence(ExpectedInput expectedInput, Input input) {
    }

    public void recordGroupEnd(Id groupId) {
    }

    public void markCompleted() {
        completed = true;
    }

    public LiteSequence recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return this;
    }
}
