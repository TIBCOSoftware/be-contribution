package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupMemberStart;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 5:13:09 PM
*/

/**
 * This pair (this and {@link DefaultDynGroupMemberEnd}) are free floating nodes. They do not
 * connect to any other nodes.
 */
public class DefaultDynGroupMemberStart extends AbstractNode
        implements DynamicGroupMemberStart<Context, DefaultExpectedInput, Input> {
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

    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        Node node = expectedInput.tearOffDestination();

        node.onInput(context, expectedInput, input);
    }

    @Override
    public DefaultDynGroupMemberStart recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return (DefaultDynGroupMemberStart) super.recover(resourceProvider, params);
    }
}
