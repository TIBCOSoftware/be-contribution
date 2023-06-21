package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.matcher.exception.IllegalOccurrenceException;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput.TimeOutHint;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.model.TimedGroupEnd;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.response.TimeOut;
import com.tibco.cep.pattern.matcher.trace.Sequence;
import com.tibco.cep.pattern.matcher.trace.SequenceRecorder;
import com.tibco.cep.service.Clock;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:34:13 PM
*/

/**
 * Proceeds to the next step as soon as the step between start and end succeeds.
 * <p/>
 * Therefore, there has to be a step(s) in-between the start and end.
 */
public class OccursWithinTimedGroupEnd extends AbstractNode
        implements TimedGroupEnd<Context, DefaultExpectedInput, Input> {
    protected Id groupId;

    protected InternalTimedGroupStart groupStart;

    protected boolean groupEnded;

    protected boolean fetchNextCalled;

    //-------------

    protected transient Clock cachedClock;

    public OccursWithinTimedGroupEnd() {
    }

    public Id getGroupId() {
        return groupId;
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    public InternalTimedGroupStart getGroupStart() {
        return groupStart;
    }

    public void setGroupStart(InternalTimedGroupStart groupStart) {
        this.groupStart = groupStart;
    }

    //-------------

    /**
     * No-op. We make our own.
     *
     * @param expectedTimeInput
     */
    @Override
    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
    }

    /**
     * This returns the parent's contextual input and not this group's - {@link
     * InternalTimedGroupStart#getParentsContextualExpectedTimeInput()}.
     *
     * @return
     */
    @Override
    public DefaultExpectedTimeInput getContextualExpectedTimeInput() {
        return groupStart.getParentsContextualExpectedTimeInput();
    }

    //-------------

    /**
     * Resets {@link #fetchNextCalled}.
     *
     * @param context
     */
    protected void reset(Context context) {
        fetchNextCalled = false;
    }

    protected DefaultExpectedTimeInput recordExpectedTimeInput(Context context) {
        Driver driver = context.getDriver();

        Id newCorrId = context.getCorrelationIdGenerator().generateNew();

        DefaultExpectedTimeInput registeredExpectation = new DefaultExpectedTimeInput();
        registeredExpectation.setDriverInstanceId(driver.getDriverInstanceId());
        registeredExpectation.setSource(inputDef.getSource());
        registeredExpectation.setUniqueId(newCorrId);

        if (cachedClock == null) {
            cachedClock = context.getResourceProvider().fetchResource(Clock.class);
        }
        registeredExpectation.setExpectationRecordedAtMillis(cachedClock.getCurrentTimeMillis());
        registeredExpectation.setExpectedTimeOffsetMillis(groupStart.getDurationMillis());

        registeredExpectation.appendDestination(this);

        contextualExpectedTimeInput = registeredExpectation;

        driver.recordExpectedTimeInput(registeredExpectation);

        groupEnded = false;

        return registeredExpectation;
    }

    /**
     * Calls {@link AbstractNode#fetchNextExpectations(Context)}.
     * <p/>
     * Sets {@link #fetchNextCalled}.
     *
     * @param context
     * @return
     */
    @Override
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        Driver driver = context.getDriver();

        if (driver.isInExpectationTrail(this)) {
            return null;
        }

        //--------------

        /*
        An example to ponder over:  (  ( ( .. ){0,n} )within.x  ){0,m}

        The innermost {0,n} block will reach out to fetchNext of the within.x because the min
        is 0.
        */

        //---------------

        Collection<DefaultExpectedInput> eisBeyondGroup = super.fetchNextExpectations(context);

        if (eisBeyondGroup != null) {
            for (DefaultExpectedInput expectedInput : eisBeyondGroup) {
                expectedInput.appendDestination(this);
            }
        }

        //--------------

        //We added something so don't cycle back here again.
        driver.addToExpectationTrail(this);

        fetchNextCalled = true;

        return eisBeyondGroup;
    }

    /**
     * Calls {@link #handleTimeOut(Context,  DefaultExpectedTimeInput, Input)} if the expectedInput
     * does not have another destination in its {@link ExpectedInput#tearOffDestination() chain}.
     *
     * @param context
     * @param expectedInput
     * @param input
     */
    public void onInput(Context context, DefaultExpectedInput expectedInput,
                        Input input) {
        Node node = expectedInput.tearOffDestination();

        //Node is not null. So, it means that the input is not meant for us.
        if (node != null) {
            startAndCloseGroupAsNeeded(context);

            node.onInput(context, expectedInput, input);

            return;
        }

        //------------

        if (Flags.TEST_ILLEGAL_STATES &&
                (expectedInput instanceof DefaultExpectedTimeInput) == false) {
            throw new IllegalOccurrenceException("The received ExpectedInput [" +
                    expectedInput + "] should have been an instance of [" +
                    DefaultExpectedTimeInput.class.getName() + "] but is not.");
        }

        DefaultExpectedTimeInput expectedTimeInput = (DefaultExpectedTimeInput) expectedInput;

        TimeOut timeOut = handleTimeOut(context, expectedTimeInput, input);

        //No failure, so fetch next.
        if (timeOut == null) {
            handleNoTimeOutFailure(context, expectedInput, input);
        }
    }

    /**
     * Called by {@link #onInput(Context, DefaultExpectedInput, Input)} after all timeout tests have
     * been performed and no errors were found.
     * <p/>
     * Calls {@link #fetchExpectations(Context)} if {@link #fetchNextCalled} is <code>false</code>.
     *
     * @param context
     * @param expectedInput
     * @param input
     */
    protected void handleNoTimeOutFailure(Context context, DefaultExpectedInput expectedInput,
                                          Input input) {
        if (fetchNextCalled == false) {
            fetchExpectations(context);
        }
    }

    /**
     * {@link Driver#flag(Failure) Flags} a {@link TimeOut} if and only if the expectedInput matches
     * the {@link #contextualExpectedTimeInput}, which then gets cleared.
     * <p/>
     * Also calls {@link Sequence#recordGroupEnd(Id)} if the {@link #contextualExpectedTimeInput} is
     * not <code>null</code>.
     *
     * @param context
     * @param expectedTimeInput
     * @param input
     * @return The time-out instance that was flagged in the driver. Or, <code>null</code> if it
     *         wasn't.
     */
    protected TimeOut handleTimeOut(Context context, DefaultExpectedTimeInput expectedTimeInput,
                                    Input input) {
        testWrongTimeInput(expectedTimeInput, input);

        startAndCloseGroupAsNeeded(context);

        contextualExpectedTimeInput = null;

        //-------------

        TimeOutHint timeOutHint = expectedTimeInput.getTimeOutHint();

        /*
        There were other external choices. So, this is not a failure.
        Even if this were within(x minutes, repeat(c|d|b){0, x } ) as the last segment in the
        pattern, you would still have "End" as a possible non-contextual input.
        */
        if (timeOutHint.hasNonContextualInputs()) {
            return null;
        }
        /*
        There were no external choices but there were no internal choices either - because all the
        internal inputs arrived as expected.

        So, this is not a failure.
        */
        else if (timeOutHint.hasContextualInputs() == false) {
            return null;
        }

        //-------------

        //Cannot be ignored which means this is a failure.

        TimeOut failure = Flags.DEBUG ?
                new TimeOut(input, this, new Exception("Failure due to time out"),
                        timeOutHint.getContextualInputs()) :
                new TimeOut(input, this, null, timeOutHint.getContextualInputs());

        context.getDriver().flag(failure);

        return failure;
    }

    protected void startAndCloseGroupAsNeeded(Context context) {
        SequenceRecorder sr = context.getSequenceRecorder();

        if (groupEnded == false) {
            if (groupStart.hasGroupStarted() == false) {
                sr.recordGroupStart(groupId);
            }

            sr.recordGroupEnd(groupId);

            groupEnded = true;
        }
    }

    protected final void testWrongTimeInput(DefaultExpectedTimeInput expectedTimeInput,
                                            Input input) {
        if (Flags.TEST_ILLEGAL_STATES) {
            if (contextualExpectedTimeInput != null &&
                    contextualExpectedTimeInput.equals(expectedTimeInput) == false) {

                throw new IllegalOccurrenceException(
                        "Error occurred while handling a time-out. The received ExpectedInput [" +
                                "] is not the same as the one that was registered [" +
                                contextualExpectedTimeInput + "]. Input [" + input + "].");
            }
        }
    }

    //--------------

    @Override
    public OccursWithinTimedGroupEnd recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        //---------------

        if (contextualExpectedTimeInput != null) {
            contextualExpectedTimeInput =
                    contextualExpectedTimeInput.recover(resourceProvider, params);
        }

        return this;
    }
}
