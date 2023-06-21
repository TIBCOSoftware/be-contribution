package com.tibco.cep.pattern.matcher.dsl;

/*
* Author: Ashwin Jayaprakash Date: Jul 24, 2009 Time: 11:07:22 AM
*/
public interface PatternDefLB<P extends PatternDef> extends ListBuilder<P> {
    PatternDefLB<P> add(P p);
}
