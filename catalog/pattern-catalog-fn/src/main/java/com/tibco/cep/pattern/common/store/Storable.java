package com.tibco.cep.pattern.common.store;

import com.tibco.cep.pattern.common.AsyncJob;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 4:41:09 PM
*/
public interface Storable<T> {
    StoredItem<T> write(T t);

    AsyncJob<StoredItem<T>> writeAsync(T t);

    StoredItem<T> read();
}
