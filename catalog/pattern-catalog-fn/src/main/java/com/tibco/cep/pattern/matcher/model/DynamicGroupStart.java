package com.tibco.cep.pattern.matcher.model;

import java.util.Collection;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 23, 2009 Time: 6:03:27 PM
*/
public interface DynamicGroupStart<C extends Context, E extends ExpectedInput, I extends Input>
        extends GroupBoundaryStart<C, E, I> {
    Collection<? extends DynamicGroupMemberOwner<C, E, I>> getGroupMemberOwners();
}