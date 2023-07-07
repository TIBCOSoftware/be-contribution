package com.tibco.cep.pattern.matcher.impl.dsl;

import java.util.LinkedList;
import java.util.List;

import com.tibco.cep.pattern.matcher.dsl.ListBuilder;

/*
* Author: Ashwin Jayaprakash Date: Jul 24, 2009 Time: 11:21:29 AM
*/
public class SimpleListBuilder<I> implements ListBuilder<I> {
    protected LinkedList<I> list;

    public SimpleListBuilder() {
        this.list = new LinkedList<I>();
    }

    public SimpleListBuilder(I firstItem) {
        this.list = new LinkedList<I>();
        this.list.add(firstItem);
    }

    public SimpleListBuilder<I> add(I i) {
        list.add(i);

        return this;
    }

    public List<I> toList() {
        return list;
    }
}
