package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.TransitionGuard;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSet;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.model.Silent;

/*
* Author: Ashwin Jayaprakash Date: Sep 10, 2009 Time: 9:17:38 AM
*/
public class DefaultSilentNode extends AbstractNode
        implements Silent<Context, DefaultExpectedInput, Input> {
    protected TransitionGuardClosure transitionGuardClosure;

    protected transient TransitionGuard transitionGuard;

    public DefaultSilentNode(TransitionGuardClosure transitionGuardClosure) {
        this.transitionGuardClosure = transitionGuardClosure;
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

    @Override
    public void onInput(Context context, DefaultExpectedInput expectedInput, Input input) {
        Driver driver = context.getDriver();

        if (transitionGuard == null) {
            TransitionGuardSet transitionGuardSet = driver.getTransitionGuardSet();

            transitionGuard = transitionGuardSet.create(transitionGuardClosure);
        }

        //------------

        transitionGuard.onInput(expectedInput.getSource(), input);

        if (driver.hasFailures()) {
            return;
        }

        //------------

        Node node = expectedInput.tearOffDestination();
        node.onInput(context, expectedInput, input);
    }

    @Override
    public void recallExpectation(Context context,
                                  DefaultExpectedInput expectedInput) {
        Node node = expectedInput.tearOffDestination();
        node.recallExpectation(context, expectedInput);
    }

    //------------

    @Override
    public DefaultSilentNode recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        transitionGuardClosure = transitionGuardClosure.recover(resourceProvider, params);

        return this;
    }
}
