package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;

/*
* Author: Ashwin Jayaprakash Date: Oct 6, 2009 Time: 10:48:12 AM
*/
public class AbstractDriverOwnerSettings {
    protected Id ownerId;

    protected DefaultPlan plan;

    protected DriverCallbackCreator optionalDriverCallbackCreator;

    protected TransitionGuardSetCreator optionalTransitionGuardSetCreator;

    /**
     * <code>false</code> by default.
     */
    protected boolean optionalRecordDriverSequence;

    public AbstractDriverOwnerSettings() {
    }

    public Id getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Id ownerId) {
        this.ownerId = ownerId;
    }

    public DefaultPlan getPlan() {
        return plan;
    }

    public void setPlan(DefaultPlan plan) {
        this.plan = plan;
    }

    public DriverCallbackCreator getOptionalDriverCallbackCreator() {
        return optionalDriverCallbackCreator;
    }

    public void setOptionalDriverCallbackCreator(
            DriverCallbackCreator optionalDriverCallbackCreator) {
        this.optionalDriverCallbackCreator = optionalDriverCallbackCreator;
    }

    public TransitionGuardSetCreator getOptionalTransitionGuardSetCreator() {
        return optionalTransitionGuardSetCreator;
    }

    public void setOptionalTransitionGuardSetCreator(
            TransitionGuardSetCreator optionalTransitionGuardSetCreator) {
        this.optionalTransitionGuardSetCreator = optionalTransitionGuardSetCreator;
    }

    public boolean isOptionalRecordDriverSequence() {
        return optionalRecordDriverSequence;
    }

    public void setOptionalRecordDriverSequence(boolean optionalRecordDriverSequence) {
        this.optionalRecordDriverSequence = optionalRecordDriverSequence;
    }
}
