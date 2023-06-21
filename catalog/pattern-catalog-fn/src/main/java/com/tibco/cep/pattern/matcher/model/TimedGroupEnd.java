package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 23, 2009 Time: 5:57:36 PM
*/
public interface TimedGroupEnd<C extends Context, E extends ExpectedInput, I extends Input>
        extends GroupBoundaryEnd<C, E, I> {
}