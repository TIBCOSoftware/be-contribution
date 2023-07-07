package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.model.OccurrenceGroupEnd;
import com.tibco.cep.pattern.matcher.model.OccurrenceGroupStart;

/*
* Author: Ashwin Jayaprakash Date: Jul 30, 2009 Time: 11:49:28 AM
*/
public interface InternalOccurrenceGroupStart
        extends OccurrenceGroupStart<Context, DefaultExpectedInput, Input> {
    /**
     * Functionally should be same as {@link Node#fetchExpectations(Context)}.
     * <p/>
     * This method is to be used by {@link OccurrenceGroupEnd} and so avoidiing cyclic-deps.
     *
     * @param context
     * @return
     */
    Collection<DefaultExpectedInput> fetchGroupExpectations(Context context);

    /**
     * Not idempotent.
     * <p/>
     * This method is called by {@link OccurrenceGroupEnd} when it receives and forwards an input to
     * its next node. Before forwarding, if this method returns <code>true</code> which means that
     * the group was started and therefore marks the end of the group.
     *
     * @return
     */
    boolean shouldMarkGroupEnd();
}
