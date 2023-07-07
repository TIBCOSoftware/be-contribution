package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.End;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Jul 9, 2009 Time: 1:56:37 PM
*/
public class DefaultEnd extends AbstractNode
        implements End<Context, DefaultExpectedInput, Input> {
    public DefaultEnd() {
    }

    @Override
    protected DefaultExpectedEndInput createExpectedInput() {
        return new DefaultExpectedEndInput();
    }

    @Override
    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
    }

    /**
     * Calls {@link Sequence#markCompleted()}.
     *
     * @param context
     * @param expectedInput
     * @param input
     */
    @Override
    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        context.getSequenceRecorder().markCompleted();
    }
}
