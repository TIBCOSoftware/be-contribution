package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.GroupBoundaryStart;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 3:38:20 PM
*/
public class DefaultDynGroupStart extends AbstractNode implements InternalDynGroupStart {
    protected Id groupId;

    //-------------

    protected Collection<DefaultDynGroupMemberOwner> groupMemberOwners;

    protected DefaultDynGroupCoordinator coordinator;

    protected DefaultDynGroupEnd groupEnd;

    protected LinkedList<GroupBoundaryStart<Context, DefaultExpectedInput, Input>> childGroups;

    protected boolean expectingGroupEnd;

    public DefaultDynGroupStart() {
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    public Id getGroupId() {
        return groupId;
    }

    public Collection<DefaultDynGroupMemberOwner> getGroupMemberOwners() {
        return groupMemberOwners;
    }

    public void setGroupMemberOwners(Collection<DefaultDynGroupMemberOwner> groupMemberOwners) {
        this.groupMemberOwners = groupMemberOwners;

        this.coordinator = new DefaultDynGroupCoordinator(groupMemberOwners, this);
    }

    @Override
    public void setInputDef(DefaultInputDef inputDef) {
        //No-op.
    }

    @Override
    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
        super.setContextualExpectedTimeInput(expectedTimeInput);

        coordinator.setContextualExpectedTimeInput(expectedTimeInput);
    }

    //-----------

    public void reset(Context context) {
        Driver driver = context.getDriver();

        //Cannot reset if this node has already placed an expectation in the list.
        if (driver.isInExpectationTrail(this)) {
            return;
        }

        //--------------

        coordinator.reset(groupMemberOwners);

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

    public DefaultDynGroupEnd getGroupEnd() {
        return groupEnd;
    }

    public void setGroupEnd(DefaultDynGroupEnd groupEnd) {
        this.groupEnd = groupEnd;
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

    /**
     * Fetches from the {@link #coordinator} if the {@link DefaultDynGroupCoordinator#getRemainingGroupMemberCount()}
     * is > 0. Otherwise, returns {@link #fetchNextExpectations(Context)}.
     *
     * @param context
     * @return
     */
    @Override
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        Driver driver = context.getDriver();

        if (driver.isInExpectationTrail(this)) {
            return null;
        }

        //--------------

        Collection<DefaultExpectedInput> eis = coordinator.fetchExpectations(context);

        //There are still some expected group-members.
        if (eis != null) {
            for (DefaultExpectedInput ei : eis) {
                ei.appendDestination(this);
            }

            //We added something so don't cycle back here again.
            driver.addToExpectationTrail(this);

            return eis;
        }

        //Group is complete. Move forward.
        return fetchNextExpectations(context);
    }

    @Override
    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        int remainingGroupMemberCount = coordinator.getRemainingGroupMemberCount();

        if (remainingGroupMemberCount == groupMemberOwners.size()) {
            context.getSequenceRecorder().recordGroupStart(groupId);

            expectingGroupEnd = true;
        }

        //-----------

        Node node = expectedInput.tearOffDestination();
        node.onInput(context, expectedInput, input);

        //-----------

        //Set up for next only of the co-ordinator has successfully completed/removed a sub-sequence.
        if (coordinator.getRemainingGroupMemberCount() < remainingGroupMemberCount) {
            fetchExpectations(context);
        }
    }

    public void onGroupMemberCompleted(Context context) {
        fetchExpectations(context);
    }

    @Override
    public DefaultDynGroupStart recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        return this;
    }
}
