package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.response.TimeOut;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:34:13 PM
*/

/**
 * There cannot be any step(s) in-between the start and end. On time-out, a failure does not occur,
 * but merely prepares to move to the next step if any.
 */
public class OccursAfterTimedGroupEnd extends OccursDuringTimedGroupEnd {
    public OccursAfterTimedGroupEnd() {
    }

    /**
     * Calls {@link Node#fetchExpectations(Context)} and {@link Driver#recordExpectedInput(ExpectedInput)
     * records} the results. Also clears {@link #contextualExpectedTimeInput}.
     * <p/>
     * Also calls {@link Sequence#recordGroupStart(Id)} and {@link Sequence#recordGroupEnd(Id)}.
     *
     * @param context
     * @param expectedTimeInput
     * @param input             @return Always returns <code>null</code>.
     */
    @Override
    protected TimeOut handleTimeOut(Context context, DefaultExpectedTimeInput expectedTimeInput,
                                    Input input) {
        testWrongTimeInput(expectedTimeInput, input);

        startAndCloseGroupAsNeeded(context);

        contextualExpectedTimeInput = null;

        return null;
    }
}