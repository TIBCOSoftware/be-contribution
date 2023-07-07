package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.GroupBoundaryStart;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jun 29, 2009 Time: 1:54:05 PM
*/
public class DefaultOccurrenceGroupStart extends AbstractNode
        implements InternalOccurrenceGroupStart {
    protected int minOccurrence;

    protected int maxOccurrence;

    protected DefaultOccurrenceGroupEnd groupEnd;

    protected LinkedList<GroupBoundaryStart<Context, DefaultExpectedInput, Input>> childGroups;

    protected Id groupId;

    //-------------

    protected boolean expectingGroupEnd;

    protected int occurrencesSoFar;

    public DefaultOccurrenceGroupStart() {
    }

    public int getMinOccurrence() {
        return minOccurrence;
    }

    public void setMinOccurrence(int minOccurrence) {
        this.minOccurrence = minOccurrence;
    }

    public int getMaxOccurrence() {
        return maxOccurrence;
    }

    public void setMaxOccurrence(int maxOccurrence) {
        this.maxOccurrence = maxOccurrence;
    }

    public DefaultOccurrenceGroupEnd getGroupEnd() {
        return groupEnd;
    }

    public void setGroupEnd(DefaultOccurrenceGroupEnd groupEnd) {
        this.groupEnd = groupEnd;
    }

    public Id getGroupId() {
        return groupId;
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    public void reset(Context context) {
        Driver driver = context.getDriver();

        /*
        Cannot reset if this node has already placed an expectation in the list.

        Ex:
        ( (a)0-2  (b)0-1 )0-2

        If we have: a and then setting expactations, b will put 1 and then go back up 1 level and
        the parent will put a=1 and then reach b which has already put its expectation. So, we
        cannot reset it until its expectations are fulfilled.   
        */
        if (driver.isInExpectationTrail(this)) {
            return;
        }

        //--------------

        occurrencesSoFar = 0;

        resetChildGroups(context);
    }

    private void resetChildGroups(Context context) {
        if (childGroups == null) {
            return;
        }

        for (GroupBoundaryStart<Context, DefaultExpectedInput, Input> childGroup : childGroups) {
            childGroup.reset(context);
        }
    }

    public void addChildGroups(List<? extends GroupBoundaryStart> boundaryStarts) {
        if (boundaryStarts == null) {
            return;
        }

        if (childGroups == null) {
            childGroups =
                    new LinkedList<GroupBoundaryStart<Context, DefaultExpectedInput, Input>>();
        }

        for (GroupBoundaryStart boundaryStart : boundaryStarts) {
            childGroups.add(boundaryStart);
        }
    }

    public List<? extends GroupBoundaryStart<Context, DefaultExpectedInput, Input>> getChildGroups() {
        return childGroups;
    }

    public boolean shouldMarkGroupEnd() {
        boolean b = expectingGroupEnd;

        expectingGroupEnd = false;

        return b;
    }

    protected final Collection<DefaultExpectedInput> setupExpectationsFromBeyondGroupEnd(
            Context context) {
        if ((minOccurrence == 0)
                || (minOccurrence == maxOccurrence && occurrencesSoFar == maxOccurrence)
                || (minOccurrence < maxOccurrence && occurrencesSoFar >= minOccurrence)) {
            groupEnd.setContextualExpectedTimeInput(getContextualExpectedTimeInput());

            return groupEnd.setupExpectationsUsingSuccessors(context);
        }

        return null;
    }

    protected final Collection<DefaultExpectedInput> setupExpectationsForTheGroup(Context context) {
        Collection<DefaultExpectedInput> eisInsideGroup = null;

        if (occurrencesSoFar < maxOccurrence) {
            resetChildGroups(context);

            eisInsideGroup = super.fetchNextExpectations(context);

            if (eisInsideGroup != null) {
                for (DefaultExpectedInput expectedInput : eisInsideGroup) {
                    expectedInput.appendDestination(this);
                }
            }
        }

        return eisInsideGroup;
    }

    /**
     * Fetches from both {@link #setupExpectationsFromBeyondGroupEnd(Context)} and {@link
     * #setupExpectationsForTheGroup(Context)}.
     *
     * @param context
     * @return
     */
    @Override
    public final Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        Driver driver = context.getDriver();

        if (driver.isInExpectationTrail(this)) {
            return null;
        }

        //--------------

        /*
        Since this method gets called by the group-end which could originally have been called
        by this start node, we mark such calls with a flag to avoid stackoverflow.

        Ex: (b (a)*)*

        Here, occurenceStart->b->a->occurenceEnd.fetchGroupExpectations(..)->occurenceStart.setupExpectationsForTheGroup->CYCLE!
        */

        Collection<DefaultExpectedInput> eisInsideGroup = null;

        eisInsideGroup = setupExpectationsForTheGroup(context);

        if (eisInsideGroup != null) {
            //We added something so don't cycle back here again.
            driver.addToExpectationTrail(this);
        }

        Collection<DefaultExpectedInput> eisBeyondGroup =
                setupExpectationsFromBeyondGroupEnd(context);

        return combine(eisInsideGroup, eisBeyondGroup);
    }

    public Collection<DefaultExpectedInput> fetchGroupExpectations(Context context) {
        return fetchExpectations(context);
    }

    public void onInput(Context context, DefaultExpectedInput expectedInput, Input input) {
        if (occurrencesSoFar == 0) {
            context.getSequenceRecorder().recordGroupStart(groupId);

            expectingGroupEnd = true;
        }

        occurrencesSoFar++;

        Node node = expectedInput.tearOffDestination();
        if (node != null) {
            node.onInput(context, expectedInput, input);
        }
    }
}
