package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupEnd;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 3:38:20 PM
*/
public class DefaultDynGroupEnd extends AbstractNode
        implements DynamicGroupEnd<Context, DefaultExpectedInput, Input> {
    protected Id groupId;

    protected InternalDynGroupStart groupStart;

    public DefaultDynGroupEnd() {
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    public Id getGroupId() {
        return groupId;
    }

    public InternalDynGroupStart getGroupStart() {
        return groupStart;
    }

    public void setGroupStart(InternalDynGroupStart groupStart) {
        this.groupStart = groupStart;
    }

    //------------

    @Override
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        Collection<DefaultExpectedInput> nextExpectations = fetchNextExpectations(context);

        if (nextExpectations != null) {
            for (DefaultExpectedInput expectedInput : nextExpectations) {
                expectedInput.appendDestination(this);
            }
        }

        return nextExpectations;
    }

    @Override
    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        Node node = expectedInput.tearOffDestination();
        if (node != null) {
            if (groupStart.shouldMarkGroupEnd()) {
                context.getSequenceRecorder().recordGroupEnd(groupId);
            }

            node.onInput(context, expectedInput, input);
        }
    }
}