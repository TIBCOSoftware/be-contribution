package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.pattern.matcher.master.DriverCallback;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSet;

/*
* Author: Ashwin Jayaprakash Date: Oct 6, 2009 Time: 10:54:21 AM
*/
public class DefaultDriverSettings extends AbstractDriverSettings {
    protected DriverCallback optionalDriverCallback;

    protected TransitionGuardSet optionalTransitionGuardSet;

    protected boolean recordSequence;

    public DefaultDriverSettings() {
    }

    public DriverCallback getOptionalDriverCallback() {
        return optionalDriverCallback;
    }

    public void setOptionalDriverCallback(DriverCallback optionalDriverCallback) {
        this.optionalDriverCallback = optionalDriverCallback;
    }

    public TransitionGuardSet getOptionalTransitionGuardSet() {
        return optionalTransitionGuardSet;
    }

    public void setOptionalTransitionGuardSet(TransitionGuardSet optionalTransitionGuardSet) {
        this.optionalTransitionGuardSet = optionalTransitionGuardSet;
    }

    public boolean isRecordSequence() {
        return recordSequence;
    }

    public void setRecordSequence(boolean recordSequence) {
        this.recordSequence = recordSequence;
    }
}
