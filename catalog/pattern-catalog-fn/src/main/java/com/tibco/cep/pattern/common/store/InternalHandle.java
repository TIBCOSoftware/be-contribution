package com.tibco.cep.pattern.common.store;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 4:41:09 PM
*/
public interface InternalHandle<K, V> extends Handle<K, V> {
    void incrementChildCount();

    void decrementChildCount();

    void lock();

    /**
     * Must be done under a {@link #lock()}.
     */
    void invalidate();

    void unlock();
}
