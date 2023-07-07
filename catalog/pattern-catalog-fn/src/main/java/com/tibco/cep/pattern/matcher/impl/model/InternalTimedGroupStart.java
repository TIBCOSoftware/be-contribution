package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.TimedGroupStart;

/*
* Author: Ashwin Jayaprakash Date: Jul 30, 2009 Time: 11:49:28 AM
*/
public interface InternalTimedGroupStart
        extends TimedGroupStart<Context, DefaultExpectedInput, Input> {
    /**
     * @return <code>true</code> if the group started.
     */
    boolean hasGroupStarted();

    DefaultExpectedTimeInput getParentsContextualExpectedTimeInput();
}