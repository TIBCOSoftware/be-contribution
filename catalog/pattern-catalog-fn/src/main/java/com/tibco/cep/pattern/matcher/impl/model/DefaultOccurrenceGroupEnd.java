package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.model.OccurrenceGroupEnd;

/*
* Author: Ashwin Jayaprakash Date: Jun 29, 2009 Time: 2:02:05 PM
*/
public class DefaultOccurrenceGroupEnd extends AbstractNode
        implements OccurrenceGroupEnd<Context, DefaultExpectedInput, Input> {
    protected InternalOccurrenceGroupStart groupStart;

    protected Id groupId;

    public DefaultOccurrenceGroupEnd() {
    }

    public InternalOccurrenceGroupStart getGroupStart() {
        return groupStart;
    }

    public void setGroupStart(InternalOccurrenceGroupStart groupStart) {
        this.groupStart = groupStart;
    }

    public Id getGroupId() {
        return groupId;
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    //----------------

    protected final Collection<DefaultExpectedInput> setupExpectationsUsingSuccessors(
            Context context) {
        //Note that this calls Super. 
        Collection<DefaultExpectedInput> nextExpectations = super.fetchNextExpectations(context);

        if (nextExpectations != null) {
            for (DefaultExpectedInput nextExpectation : nextExpectations) {
                nextExpectation.appendDestination(this);
            }
        }

        return nextExpectations;
    }

    /**
     * Calls {@link Node#fetchExpectations(Context)} on {@link #groupStart}.
     *
     * @param context
     * @return
     */
    @Override
    public final Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        //Some cyclic calls here. But all the logic is placed in the Start node - centralized.
        return groupStart.fetchGroupExpectations(context);
    }

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
