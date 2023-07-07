package com.tibco.cep.pattern.matcher.master;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.pattern.matcher.response.Success;
import com.tibco.cep.pattern.matcher.trace.Sequence;
import com.tibco.cep.util.annotation.Optional;
import com.tibco.cep.util.annotation.ReadOnly;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 5:40:09 PM
*/

/**
 * All calls except where marked "Atomic" (like all read-methods in {@link DriverView}) must be made
 * within the context of the {@link #lock()} and {@link #unlock()} methods.
 */
public interface Driver extends Recoverable<Driver>, DriverView {
    void lock();

    boolean tryLock();

    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    void unlock();

    //--------------

    Sequence getSequence();

    /**
     * @return Can be <code>null</code> if nothing is specified.
     */
    TransitionGuardSet getTransitionGuardSet();

    //--------------

    /**
     * Atomic operation. Does not require a {@link #lock()}.
     * <p/>
     * Should return <code>true</code> when the driver is between {@link #start()} and {@link
     * #stop()}. <code>false</code> all other times.
     *
     * @return
     */
    boolean isValid();

    void start();

    /**
     * @param source
     * @param input
     * @return {@link Success} or {@link Failure}.
     */
    Response onInput(Source source, Input input);

    /**
     * @param input
     * @return {@link Success} or {@link Failure}.
     */
    Response onTimeOut(TimeInput input);

    void stop();

    //-----------

    void recordExpectedInput(ExpectedInput expectedInput);

    void eraseExpectedInput(ExpectedInput expectedInput);

    @Optional
    @ReadOnly
    Collection<? extends ExpectedInput> getExpectedInputs();

    //-----------

    void recordExpectedTimeInput(ExpectedTimeInput expectedTimeInput);

    void eraseExpectedTimeInput(ExpectedTimeInput expectedTimeInput);

    @Optional
    @ReadOnly
    Collection<? extends ExpectedTimeInput> getExpectedTimeInputs();

    //-----------

    boolean isInExpectationTrail(Node node);

    void addToExpectationTrail(Node node);

    void clearExpectationTrail();
}
