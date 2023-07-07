package com.tibco.cep.pattern.common.store;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 4:41:09 PM
*/
public interface Handle<K, V> extends Storable<V> {
    K getId();

    K getParentId();

    int getChildCount();

    boolean isValid();
}
