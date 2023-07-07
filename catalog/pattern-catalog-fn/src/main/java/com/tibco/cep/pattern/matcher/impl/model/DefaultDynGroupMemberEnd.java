package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupMemberEnd;
import com.tibco.cep.pattern.matcher.model.DynamicGroupMemberOwner;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 5:13:09 PM
*/

/**
 * @see DefaultDynGroupMemberStart
 */
public class DefaultDynGroupMemberEnd extends AbstractNode
        implements DynamicGroupMemberEnd<Context, DefaultExpectedInput, Input> {
    protected boolean endReached;

    protected InternalDynGroupCoordinator groupCoordinator;

    protected DynamicGroupMemberOwner groupMemberOwner;

    public boolean isEndReached() {
        return endReached;
    }

    public void setEndReached(boolean endReached) {
        this.endReached = endReached;
    }

    public InternalDynGroupCoordinator getGroupCoordinator() {
        return groupCoordinator;
    }

    public void setGroupCoordinator(InternalDynGroupCoordinator groupCoordinator) {
        this.groupCoordinator = groupCoordinator;
    }

    public DynamicGroupMemberOwner getGroupMemberOwner() {
        return groupMemberOwner;
    }

    public void setGroupMemberOwner(DynamicGroupMemberOwner groupMemberOwner) {
        this.groupMemberOwner = groupMemberOwner;
    }

    @Override
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        endReached = true;

        groupCoordinator.onGroupMemberCompleted(context, groupMemberOwner);

        return null;
    }

    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        //Nothing.
        if (Flags.DEBUG) {
            new Exception("onInput(..) was not expected for ExpectedInput ["
                    + expectedInput + "] and Input [" + input + "]")
                    .printStackTrace();
        }
    }

    @Override
    public DefaultDynGroupMemberEnd recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return (DefaultDynGroupMemberEnd) super.recover(resourceProvider, params);
    }
}