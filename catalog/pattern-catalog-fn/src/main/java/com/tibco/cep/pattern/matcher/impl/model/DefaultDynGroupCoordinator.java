package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;
import java.util.HashMap;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupMemberOwner;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Aug 11, 2009 Time: 2:43:29 PM
*/
public class DefaultDynGroupCoordinator implements InternalDynGroupCoordinator {
    protected HashMap<DefaultDynGroupMemberStart, DefaultDynGroupMemberOwner> remainingGroupMemberOwners;

    protected InternalDynGroupStart groupStart;

    public DefaultDynGroupCoordinator(
            Collection<DefaultDynGroupMemberOwner> remainingGroupMemberOwners,
            InternalDynGroupStart groupStart) {
        reset(remainingGroupMemberOwners);

        this.groupStart = groupStart;
    }

    protected final void reset(Collection<DefaultDynGroupMemberOwner> collection) {
        if (remainingGroupMemberOwners == null) {
            remainingGroupMemberOwners =
                    new HashMap<DefaultDynGroupMemberStart, DefaultDynGroupMemberOwner>();
        }
        else {
            remainingGroupMemberOwners.clear();
        }

        for (DefaultDynGroupMemberOwner rgmo : collection) {
            remainingGroupMemberOwners.put(rgmo.getStart(), rgmo);

            DefaultDynGroupMemberEnd groupMemberEnd = rgmo.getEnd();
            groupMemberEnd.setEndReached(false);
            groupMemberEnd.setGroupCoordinator(this);
            groupMemberEnd.setGroupMemberOwner(rgmo);
        }
    }

    public int getRemainingGroupMemberCount() {
        return remainingGroupMemberOwners.size();
    }

    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
        for (DefaultDynGroupMemberOwner memberOwner : remainingGroupMemberOwners.values()) {
            memberOwner.setContextualExpectedTimeInput(expectedTimeInput);
        }
    }

    //-----------

    /**
     * @return <code>null</code>.
     */
    public InputDef getInputDef() {
        return null;
    }

    public DefaultDynGroupMemberStart[] getNext() {
        int size = remainingGroupMemberOwners.size();

        return (size == 0) ? null :
                remainingGroupMemberOwners.keySet().toArray(new DefaultDynGroupMemberStart[size]);
    }

    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        Collection<DefaultExpectedInput> list = null;

        for (DefaultDynGroupMemberStart memberStart : remainingGroupMemberOwners.keySet()) {
            Collection<DefaultExpectedInput> eis = memberStart.fetchExpectations(context);

            if (eis != null && eis.size() > 0) {
                if (list == null) {
                    for (DefaultExpectedInput ei : eis) {
                        ei.appendDestination(this);
                    }

                    list = eis;
                }
                else {
                    for (DefaultExpectedInput ei : eis) {
                        ei.appendDestination(this);

                        list.add(ei);
                    }
                }
            }
        }

        return list;
    }

    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        Node node = expectedInput.tearOffDestination();

        if (node != null) {
            DefaultDynGroupMemberOwner memberOwner = remainingGroupMemberOwners.get(node);

            node.onInput(context, expectedInput, input);

            /*
            Check to see if the sub-sequence has completed. If not, then it means that the
            sub-sequence is really a sub-pattern that is still in progress.
            */
            if (memberOwner.getEnd().isEndReached()) {
                remainingGroupMemberOwners.remove(node);
            }
        }
    }

    /**
     * When a sub-sequence that happens to be a long sub-pattern ends, we need to know so as to set
     * up the remaining sub-sequences.
     *
     * @param context
     * @param memberOwner
     */
    public void onGroupMemberCompleted(Context context, DynamicGroupMemberOwner memberOwner) {
        remainingGroupMemberOwners.remove(memberOwner.getStart());

        groupStart.onGroupMemberCompleted(context);
    }

    public void recallExpectation(Context context,
                                  DefaultExpectedInput expectedInput) {
        Node node = expectedInput.tearOffDestination();

        if (node != null) {
            node.recallExpectation(context, expectedInput);
        }
    }

    public DefaultDynGroupCoordinator recover(
            ResourceProvider resourceProvider, Object... params) throws RecoveryException {
        return this;
    }
}
