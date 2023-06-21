package com.tibco.cep.pattern.common.store;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 4:41:09 PM
*/
public interface StoredItem<T> {
    long getVersion();

    long getTimestamp();

    T getActual();
}
