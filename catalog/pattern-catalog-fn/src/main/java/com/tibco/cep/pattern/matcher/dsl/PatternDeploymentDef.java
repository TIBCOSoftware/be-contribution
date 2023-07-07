package com.tibco.cep.pattern.matcher.dsl;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.DriverView;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.stats.MergedSequenceStatsView;
import com.tibco.cep.pattern.matcher.trace.SequenceView;

/*
* Author: Ashwin Jayaprakash Date: Jul 23, 2009 Time: 2:03:36 PM
*/
public interface PatternDeploymentDef<D extends PatternDeploymentDef, P extends PatternDef<I, P, N>,
        I extends InputDef, N extends Node> {
    /**
     * All the inputs created can be retrieved in the end using {@link #getInputs()}.
     *
     * @param id
     * @return
     */
    I createInput(String id);

    P createPattern();

    /**
     * For adding multiple inputs.
     *
     * @param firstInput
     * @return
     */
    InputDefLB<I> list(I firstInput);

    /**
     * For adding multiple patterns.
     *
     * @param firstPattern
     * @return
     */
    PatternDefLB<P> list(P firstPattern);

    //--------------

    D setPattern(P pattern);

    /**
     * Optional.
     *
     * @param maxThreads 1 or higher.
     * @return
     */
    D useDedicatedTimer(int maxThreads);

    /**
     * Optional.
     *
     * @param driverCallbackCreator
     * @return
     */
    D useDriverCallbackCreator(DriverCallbackCreator driverCallbackCreator);

    /**
     * Optional. Used if any {@link PatternDef} is using a {@link TransitionGuardClosure}.
     *
     * @param transitionGuardSetCreator
     * @return
     */
    D useTransitionGuardSetCreator(TransitionGuardSetCreator transitionGuardSetCreator);

    /**
     * Optional. Sets up the {@link DriverView driver} such that the {@link SequenceView sequence}
     * is captured in detail and also {@link MergedSequenceStatsView} is available. Off by default.
     *
     * @return
     */
    D captureSequence();

    //--------------

    Id getId();

    I[] getInputs();

    P getPattern();

    boolean usesDedicatedTimer();

    /**
     * @return 0 if {@link #usesDedicatedTimer()} returns <code>false</code>.
     */
    int getDedicatedTimerMaxThreads();

    DriverCallbackCreator getDriverCallbackCreator();

    TransitionGuardSetCreator getTransitionGuardSetCreator();

    boolean isSequenceCaptureOn();
}
