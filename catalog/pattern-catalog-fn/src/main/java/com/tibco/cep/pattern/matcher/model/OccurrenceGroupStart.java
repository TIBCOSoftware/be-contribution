package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 23, 2009 Time: 6:03:27 PM
*/
public interface OccurrenceGroupStart<C extends Context, E extends ExpectedInput, I extends Input>
        extends GroupBoundaryStart<C, E, I> {
    int getMinOccurrence();

    int getMaxOccurrence();
}
