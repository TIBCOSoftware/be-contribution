package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;

import com.tibco.cep.pattern.matcher.master.Context;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:34:13 PM
*/

/**
 * Proceeds to the next step only after the specified time has passed and if there is no time-out
 * failure.
 * <p/>
 * There has to be a step(s) in-between the start and end.
 */
public class OccursDuringTimedGroupEnd extends OccursWithinTimedGroupEnd {
    public OccursDuringTimedGroupEnd() {
    }

    @Override
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        //Timeout has not occurred yet. So, cannot allow anything beyond this group.
        if (contextualExpectedTimeInput != null) {
            return null;
        }

        return super.fetchExpectations(context);
    }
}