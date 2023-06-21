package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupMemberOwner;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 5:11:56 PM
*/
public class DefaultDynGroupMemberOwner
        implements DynamicGroupMemberOwner<Context, DefaultExpectedInput, Input> {
    protected DefaultDynGroupMemberStart start;

    protected DefaultDynGroupMemberEnd end;

    public DefaultDynGroupMemberOwner() {
    }

    public DefaultDynGroupMemberStart getStart() {
        return start;
    }

    public void setStart(DefaultDynGroupMemberStart start) {
        this.start = start;
    }

    public DefaultDynGroupMemberEnd getEnd() {
        return end;
    }

    public void setEnd(DefaultDynGroupMemberEnd end) {
        this.end = end;
    }

    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
        start.setContextualExpectedTimeInput(expectedTimeInput);
    }

    //--------------

    public DefaultDynGroupMemberOwner recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return this;
    }
}
