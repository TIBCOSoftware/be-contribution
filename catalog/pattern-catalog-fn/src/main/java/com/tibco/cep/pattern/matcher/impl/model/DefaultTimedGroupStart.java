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
* Author: Ashwin Jayaprakash Date: Jun 29, 2009 Time: 6:51:17 PM
*/
public class DefaultTimedGroupStart extends AbstractNode implements InternalTimedGroupStart {
    protected long durationMillis;

    protected Id groupId;

    protected OccursWithinTimedGroupEnd groupEnd;

    protected LinkedList<GroupBoundaryStart<Context, DefaultExpectedInput, Input>> childGroups;

    //-----------

    protected DefaultExpectedTimeInput parentsContextualExpectedTimeInput;

    protected boolean groupStarted;

    public DefaultTimedGroupStart() {
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public OccursWithinTimedGroupEnd getGroupEnd() {
        return groupEnd;
    }

    public void setGroupEnd(OccursWithinTimedGroupEnd groupEnd) {
        this.groupEnd = groupEnd;
    }

    public Id getGroupId() {
        return groupId;
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    /**
     * No-op.
     *
     * @param inputDef
     */
    @Override
    public void setInputDef(DefaultInputDef inputDef) {
    }

    /**
     * Sets to a different field that can be accessed using {@link #getParentsContextualExpectedTimeInput()}.
     *
     * @param expectedTimeInput
     */
    @Override
    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
        parentsContextualExpectedTimeInput = expectedTimeInput;
    }

    public DefaultExpectedTimeInput getParentsContextualExpectedTimeInput() {
        return parentsContextualExpectedTimeInput;
    }

    //-------------

    public void reset(Context context) {
        groupEnd.reset(context);

        if (childGroups != null) {
            for (GroupBoundaryStart<Context, ?, ?> childGroup : childGroups) {
                childGroup.reset(context);
            }
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

    @Override
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        Driver driver = context.getDriver();

        if (driver.isInExpectationTrail(this)) {
            return null;
        }

        //--------------

        Collection<DefaultExpectedInput> eisInsideGroup = null;

        contextualExpectedTimeInput = groupEnd.recordExpectedTimeInput(context);
        contextualExpectedTimeInput.appendDestination(this);

        //---------------

        eisInsideGroup = super.fetchNextExpectations(context);

        if (eisInsideGroup != null) {
            for (DefaultExpectedInput expectedInput : eisInsideGroup) {
                expectedInput.appendDestination(this);
            }
        }
        else {
            eisInsideGroup = new LinkedList<DefaultExpectedInput>();
        }

        eisInsideGroup.add(contextualExpectedTimeInput);

        //---------------

        //We added something so don't cycle back here again.
        driver.addToExpectationTrail(this);

        groupStarted = false;

        return eisInsideGroup;
    }

    public boolean hasGroupStarted() {
        return groupStarted;
    }

    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        if (groupStarted == false) {
            context.getSequenceRecorder().recordGroupStart(groupId);

            groupStarted = true;
        }

        Node node = expectedInput.tearOffDestination();
        if (node != null) {
            node.onInput(context, expectedInput, input);
        }
    }
}
