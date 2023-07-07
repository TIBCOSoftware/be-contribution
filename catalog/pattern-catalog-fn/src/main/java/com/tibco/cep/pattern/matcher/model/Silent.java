package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 23, 2009 Time: 6:10:59 PM
*/
public interface Silent<C extends Context, E extends ExpectedInput, I extends Input>
        extends Node<C, E, I> {
}
