package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 10:59:26 AM
*/
public interface DynamicGroupMemberEnd<C extends Context, E extends ExpectedInput, I extends Input>
        extends DynamicGroupMemberBoundary<C, E, I> {
}