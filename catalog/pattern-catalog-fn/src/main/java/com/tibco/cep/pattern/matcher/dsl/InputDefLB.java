package com.tibco.cep.pattern.matcher.dsl;

import com.tibco.cep.pattern.matcher.model.InputDef;

/*
* Author: Ashwin Jayaprakash Date: Jul 24, 2009 Time: 11:06:56 AM
*/
public interface InputDefLB<I extends InputDef> extends ListBuilder<I> {
    InputDefLB<I> add(I i);
}
