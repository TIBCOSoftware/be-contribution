package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 10:59:26 AM
*/
public interface DynamicGroupMemberOwner<C extends Context, E extends ExpectedInput, I extends Input>
        extends Recoverable<DynamicGroupMemberOwner<C, E, I>> {
    DynamicGroupMemberStart<C, E, I> getStart();

    DynamicGroupMemberEnd<C, E, I> getEnd();
}