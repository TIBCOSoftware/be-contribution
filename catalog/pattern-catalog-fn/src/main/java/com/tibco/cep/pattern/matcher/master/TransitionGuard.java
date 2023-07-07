package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.response.Failure;

/*
* Author: Ashwin Jayaprakash Date: Sep 9, 2009 Time: 5:48:37 PM
*/

/**
 * A new instance will be created (see {@link Plan#createNewFlow(ResourceProvider)}) for every
 * {@link PatternDef}. If there are multiple transition-guards defined in the pattern, then each one
 * of those in the pattern instance will be new.
 */
public interface TransitionGuard {
    public void init(ResourceProvider resourceProvider, DriverView driverView,
                     TransitionGuardSet guardSet);

    /**
     * Called before the input is delivered to the internal state handler.
     * <p/>
     * Mark {@link Driver#flag(Failure)} to stop the flow to the state handler. The {@link
     * DriverCallback} will be notified of the failure. Use {@link Driver#getSequence()} to obtain
     * the current state of the {@link Driver}.
     * <p/>
     * To continue with the normal flow, do nothing.
     * <p/>
     * Method must return quickly.
     *
     * @param source
     * @param input
     */
    void onInput(Source source, Input input);
}
