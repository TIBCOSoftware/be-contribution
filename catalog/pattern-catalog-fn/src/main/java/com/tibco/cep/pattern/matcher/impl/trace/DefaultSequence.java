package com.tibco.cep.pattern.matcher.impl.trace;

import java.util.LinkedList;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.matcher.exception.IllegalOccurrenceException;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.trace.RecordedOccurrence;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 2:08:09 PM
*/
public class DefaultSequence implements Sequence {
    protected DefaultRecordedOccurrence firstRecordedOccurrence;

    protected DefaultRecordedOccurrence recentRecordedOccurrence;

    protected DefaultGroup superGroup;

    protected LinkedList<DefaultGroup> openGroupStack;

    protected boolean completed;

    public DefaultSequence() {
        this.superGroup = new DefaultGroup();
        this.superGroup.setGroupId(new DefaultId("SuperGroup"));

        this.openGroupStack = new LinkedList<DefaultGroup>();
        this.openGroupStack.add(this.superGroup);
    }

    public RecordedOccurrence getFirstRecordedOccurrence() {
        return firstRecordedOccurrence;
    }

    public RecordedOccurrence getRecentRecordedOccurrence() {
        return recentRecordedOccurrence;
    }

    public DefaultGroup getSuperGroup() {
        return superGroup;
    }

    public LinkedList<DefaultGroup> getOpenGroupStack() {
        return openGroupStack;
    }

    public boolean hasCompleted() {
        return completed;
    }

    //---------------

    public void recordGroupStart(Id groupId) {
        DefaultGroup group = new DefaultGroup();
        group.setGroupId(groupId);

        //This is a top level group.
        if (openGroupStack.isEmpty()) {
            superGroup.addMember(group);
            group.setParent(superGroup);
        }
        else {
            DefaultGroup parentGroup = openGroupStack.getLast();

            parentGroup.addMember(group);
            group.setParent(parentGroup);
        }

        openGroupStack.addLast(group);
    }

    public void recordOccurrence(ExpectedInput expectedInput, Input input) {
        DefaultRecordedOccurrence occurrence = new DefaultRecordedOccurrence(expectedInput, input);

        if (firstRecordedOccurrence == null) {
            firstRecordedOccurrence = occurrence;
            recentRecordedOccurrence = occurrence;
        }
        else {
            recentRecordedOccurrence.setNext(occurrence);
            occurrence.setPrevious(recentRecordedOccurrence);

            recentRecordedOccurrence = occurrence;
        }

        if (Flags.TEST_ILLEGAL_STATES) {
            if (openGroupStack.isEmpty()) {
                throw new IllegalOccurrenceException(
                        "Occurrence being recorded is incorrect. There are no open groups left.");
            }
        }

        DefaultGroup expectedGroup = openGroupStack.getLast();
        expectedGroup.addMember(occurrence);
    }

    public void recordGroupEnd(Id groupId) {
        if (Flags.TEST_ILLEGAL_STATES) {
            if (openGroupStack.isEmpty()) {
                throw new IllegalOccurrenceException(
                        "Group end being recorded is out of sequence [" + groupId +
                                "]. There are no open groups left.");
            }
        }

        DefaultGroup expectedGroup = openGroupStack.removeLast();

        if (Flags.TEST_ILLEGAL_STATES) {
            Id actualGroupId = (expectedGroup == null) ? null : expectedGroup.getGroupId();

            if (expectedGroup == null || groupId.equals(actualGroupId) == false) {
                throw new IllegalOccurrenceException(
                        "Group end being recorded is in a wrong sequence." +
                                " Expecting Group Id [" + actualGroupId + "]." +
                                " Received Group Id [" + groupId + "]." +
                                " List of open Group Ids " + openGroupStack + ".");
            }
        }

        expectedGroup.setCompleted(true);
    }

    public void markCompleted() {
        if (Flags.TEST_ILLEGAL_STATES) {
            if (openGroupStack.isEmpty()) {
                throw new IllegalOccurrenceException(
                        "Sequence end completion is incorrect. There are no open groups left.");
            }
        }

        DefaultGroup lastGroup = openGroupStack.removeLast();

        if (Flags.TEST_ILLEGAL_STATES) {
            if (lastGroup.equals(superGroup) == false) {
                //Put it back.
                openGroupStack.addLast(lastGroup);

                throw new IllegalOccurrenceException(
                        "Sequence end completion is incorrect. There are still a few open groups left " +
                                openGroupStack + ".");
            }
        }

        lastGroup.setCompleted(true);
        this.completed = true;
    }

    //------------

    public DefaultSequence recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return this;
    }
}
