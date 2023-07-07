package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 24, 2009 Time: 4:24:28 PM
*/
public interface Start<C extends Context, E extends ExpectedInput, I extends Input>
        extends Node<C, E, I> {
    void start(C context);

    Start<C, E, I> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException;
}
