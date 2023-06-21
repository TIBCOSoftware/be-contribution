package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 11:08:58 AM
*/
public interface GroupBoundary<C extends Context, E extends ExpectedInput, I extends Input>
        extends Node<C, E, I> {
    Id getGroupId();
}
