package com.tibco.cep.pattern.matcher.impl.dsl;

import java.util.LinkedList;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.matcher.dsl.InputDefLB;
import com.tibco.cep.pattern.matcher.dsl.PatternDefLB;
import com.tibco.cep.pattern.matcher.impl.master.DefaultSource;
import com.tibco.cep.pattern.matcher.impl.model.AbstractNode;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInputDef;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;

/*
* Author: Ashwin Jayaprakash Date: Jul 22, 2009 Time: 3:56:58 PM
*/
public abstract class AbstractPatternDeploymentDef
        implements InternalPatternDeploymentDef<AbstractPatternDeploymentDef, DefaultPatternDef,
        DefaultInputDef, AbstractNode> {
    protected DefaultPatternDef finalPattern;

    protected int dedicatedTimerMaxThreads;

    protected DriverCallbackCreator driverCallbackCreator;

    protected TransitionGuardSetCreator transitionGuardSetCreator;

    protected boolean captureSequence;

    protected Id id;

    //-----------------

    protected LinkedList<DefaultInputDef> inputs;

    protected AbstractPatternDeploymentDef(Id id) {
        this.id = id;
        this.inputs = new LinkedList<DefaultInputDef>();
    }

    //-----------------

    public DefaultInputDef createInput(String id) {
        DefaultId sourceId = new DefaultId(this.id, id);

        DefaultSource source = new DefaultSource();
        source.setResourceId(sourceId);

        DefaultInputDef<Source> inputDef = new DefaultInputDef<Source>();
        inputDef.setSource(source);

        addInput(inputDef);

        return inputDef;
    }

    public void addInput(DefaultInputDef inputDef) {
        inputs.add(inputDef);
    }

    public DefaultPatternDef createPattern() {
        return new DefaultPatternDef();
    }

    public InputDefLB<DefaultInputDef> list(DefaultInputDef input) {
        return new DefaultInputDefLB(input);
    }

    public PatternDefLB<DefaultPatternDef> list(DefaultPatternDef pattern) {
        return new DefaultPatternDefLB(pattern);
    }

    //-----------------

    public AbstractPatternDeploymentDef setPattern(DefaultPatternDef finalPattern) {
        this.finalPattern = finalPattern;

        return this;
    }

    public AbstractPatternDeploymentDef useDedicatedTimer(int maxThreads) {
        if (maxThreads > 0) {
            this.dedicatedTimerMaxThreads = maxThreads;
        }
        else {
            this.dedicatedTimerMaxThreads = 0;
        }

        return this;
    }

    public AbstractPatternDeploymentDef useDriverCallbackCreator(
            DriverCallbackCreator driverCallbackCreator) {
        this.driverCallbackCreator = driverCallbackCreator;

        return this;
    }

    public AbstractPatternDeploymentDef useTransitionGuardSetCreator(
            TransitionGuardSetCreator transitionGuardSetCreator) {
        this.transitionGuardSetCreator = transitionGuardSetCreator;

        return this;
    }

    public AbstractPatternDeploymentDef captureSequence() {
        this.captureSequence = true;

        return this;
    }

    //-----------------

    public Id getId() {
        return id;
    }

    public DefaultInputDef[] getInputs() {
        int size = inputs.size();

        return (size == 0) ? null : inputs.toArray(new DefaultInputDef[size]);
    }

    public DefaultPatternDef getPattern() {
        return finalPattern;
    }

    public boolean usesDedicatedTimer() {
        return (dedicatedTimerMaxThreads > 0);
    }

    public int getDedicatedTimerMaxThreads() {
        return dedicatedTimerMaxThreads;
    }

    public DriverCallbackCreator getDriverCallbackCreator() {
        return driverCallbackCreator;
    }

    public TransitionGuardSetCreator getTransitionGuardSetCreator() {
        return transitionGuardSetCreator;
    }

    public boolean isSequenceCaptureOn() {
        return captureSequence;
    }
}
