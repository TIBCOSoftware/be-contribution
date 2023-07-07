package com.tibco.cep.pattern.integ.admin;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.integ.dsl.PatternSubscriptionDef;
import com.tibco.cep.pattern.matcher.dsl.InputDefLB;
import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.dsl.PatternDefLB;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Aug 24, 2009 Time: 2:29:00 PM
*/
public interface Session<P extends PatternDef<I, P, N>, I extends InputDef, N extends Node> {
    Id getId();

    //------------

    /**
     * Calling this method more than once will return the same instance.
     *
     * @return The single instance of pattern-source helper for this Session instance.
     */
    PatternSubscriptionDef definePatternSubscription();

    //------------

    /**
     * * Calling this method more than once using the same parameter will return the same instance.
     *
     * @param alias
     * @return
     */
    InputDef defineInput(String alias);


    /**
     * For adding multiple inputs.
     *
     * @param firstInput
     * @return
     */
    InputDefLB<I> createList(I firstInput);

    /**
     * For adding multiple patterns.
     *
     * @param firstPattern
     * @return
     */
    PatternDefLB<P> createList(P firstPattern);


    /**
     * The same method is to be used to create Sub-Patterns.
     *
     * @return
     */
    PatternDef createPattern();

    /**
     * @param mainPattern The final Pattern.
     */
    void setPattern(PatternDef mainPattern);

    void setDriverCallbackCreator(DriverCallbackCreator callbackCreator);

    void setTransitionGuardSetCreator(TransitionGuardSetCreator guardSetCreator);

    void captureSequence();

    //-------------

    /**
     * To be called only by the internal framework just before deployment.
     */
    void prepare();

    /**
     * To be called if this session is not to be deployed, but discarded.
     */
    void discard();
}
