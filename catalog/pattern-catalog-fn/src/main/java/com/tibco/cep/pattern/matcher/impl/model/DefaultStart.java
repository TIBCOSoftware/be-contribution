package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.model.Start;

/*
* Author: Ashwin Jayaprakash Date: Jun 29, 2009 Time: 10:50:08 AM
*/
public class DefaultStart extends AbstractNode
        implements Start<Context, DefaultExpectedInput, Input> {
    public DefaultStart() {
    }

    /**
     * No-op.
     *
     * @param inputDef
     */
    @Override
    public void setInputDef(DefaultInputDef inputDef) {
    }

    public void start(Context context) {
        fetchExpectations(context);
    }

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
    public DefaultStart recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return (DefaultStart) super.recover(resourceProvider, params);
    }
}
