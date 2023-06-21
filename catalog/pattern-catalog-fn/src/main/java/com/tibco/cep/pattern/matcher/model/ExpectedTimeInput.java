package com.tibco.cep.pattern.matcher.model;

import java.util.Collection;
import java.util.Set;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.TimeSource;
import com.tibco.cep.util.annotation.Optional;
import com.tibco.cep.util.annotation.ReadOnly;

/*
* Author: Ashwin Jayaprakash Date: Jun 25, 2009 Time: 4:58:36 PM
*/
public interface ExpectedTimeInput extends ExpectedInput {
    TimeSource getSource();

    Id getUniqueId();

    /**
     * The absolute UTC time at which this expectation was created. To get the actual time at which
     * the input is expected, add this value with the one from {@link #getExpectedTimeOffsetMillis()}.
     *
     * @return
     */
    long getExpectationRecordedAtMillis();

    /**
     * @return
     * @see @getExpectationRecordedAtMillis
     */
    long getExpectedTimeOffsetMillis();

    //---------------

    void addContextualInput(ExpectedInput expectedInput);

    void removeContextualInput(ExpectedInput expectedInput);

    boolean hasContextualInputs();

    @ReadOnly
    @Optional
    Set<? extends ExpectedInput> getContextualInputs();

    @Optional
    Set<? extends ExpectedInput> removeContextualInputs();

    //---------------

    /**
     * The Driver sets this when the time out occurs, before notifying the node.
     *
     * @param hint
     */
    void setTimeOutHint(TimeOutHint hint);

    TimeOutHint getTimeOutHint();

    //---------------

    ExpectedTimeInput recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException;

    //---------------

    public static interface TimeOutHint {
        @Optional
        Collection<? extends ExpectedInput> getContextualInputs();

        @Optional
        Collection<? extends ExpectedInput> getNonContextualInputs();

        boolean hasContextualInputs();

        boolean hasNonContextualInputs();
    }
}
