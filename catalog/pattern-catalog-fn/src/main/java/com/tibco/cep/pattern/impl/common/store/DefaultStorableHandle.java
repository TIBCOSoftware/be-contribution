package com.tibco.cep.pattern.impl.common.store;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.tibco.cep.pattern.common.AsyncJob;
import com.tibco.cep.pattern.common.store.InternalHandle;
import com.tibco.cep.pattern.common.store.Storable;
import com.tibco.cep.pattern.common.store.StoredItem;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash / Date: Nov 3, 2009 / Time: 3:14:02 PM
*/
public class DefaultStorableHandle<K, V> extends ReentrantLock
        implements InternalHandle<K, V>, Storable<V> {
    @Optional
    protected K parentId;

    protected K id;

    protected volatile boolean valid;

    protected AtomicInteger childCount;

    public DefaultStorableHandle(@Optional K parentId, K id) {
        this.parentId = parentId;
        this.id = id;

        this.valid = true;
        this.childCount = new AtomicInteger();
    }

    public boolean isValid() {
        return valid;
    }

    public int getChildCount() {
        return childCount.get();
    }

    public K getParentId() {
        return parentId;
    }

    public K getId() {
        return id;
    }

    //--------------

    public void incrementChildCount() {
        childCount.incrementAndGet();
    }

    public void decrementChildCount() {
        childCount.decrementAndGet();
    }

    public void invalidate() {
        valid = false;
    }

    //--------------

    public StoredItem<V> write(V v) {
        //todo
        return null;
    }

    public AsyncJob<StoredItem<V>> writeAsync(V v) {
        //todo
        return null;
    }

    public StoredItem<V> read() {
        //todo
        return null;
    }

    public void delete() {
        //todo

    }

    public AsyncJob<?> deleteAsync() {
        //todo
        return null;
    }
}
