package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 24, 2009 Time: 4:24:33 PM
*/
public interface End<C extends Context, E extends ExpectedInput, I extends Input>
        extends Node<C, E, I> {
}
