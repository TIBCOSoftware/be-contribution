package com.tibco.cep.pattern.matcher.dsl;

import java.util.List;

/*
* Author: Ashwin Jayaprakash Date: Jul 24, 2009 Time: 10:56:57 AM
*/
public interface ListBuilder<I> {
    ListBuilder<I> add(I i);

    List<I> toList();
}
