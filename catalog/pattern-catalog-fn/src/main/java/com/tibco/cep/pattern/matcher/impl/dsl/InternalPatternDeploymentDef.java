package com.tibco.cep.pattern.matcher.impl.dsl;

import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.dsl.PatternDeploymentDef;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jul 23, 2009 Time: 2:03:36 PM
*/
public interface InternalPatternDeploymentDef<D extends InternalPatternDeploymentDef, P extends PatternDef<I, P, N>,
        I extends InputDef, N extends Node> extends PatternDeploymentDef<D, P, I, N> {
    /**
     * All the inputs created can be retrieved in the end using {@link #getInputs()}.
     *
     * @param input
     * @return
     */
    void addInput(I input);
}