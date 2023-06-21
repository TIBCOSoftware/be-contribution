package com.tibco.cep.pattern.impl.common.store;

import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.pattern.common.LiteException;
import com.tibco.cep.pattern.common.store.HandleRepository;
import com.tibco.cep.pattern.common.store.InternalHandle;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash / Date: Nov 3, 2009 / Time: 11:45:39 AM
*/
public class DefaultHandleRepository<K> implements HandleRepository<K> {
    protected ConcurrentHashMap<K, InternalHandle<K, ?>> handles;

    protected HandleCreator<K> handleCreator;

    public DefaultHandleRepository(HandleCreator<K> handleCreator) {
        this.handles = new ConcurrentHashMap<K, InternalHandle<K, ?>>();
        this.handleCreator = handleCreator;
    }

    public InternalHandle<K, ?> getHandle(K id) {
        return handles.get(id);
    }

    public InternalHandle<K, ?> createHandleAndRegister(@Optional K parentId, K id)
            throws ParentIsInvalidException, ParentDoesNotExistException {
        if (handles.containsKey(id)) {
            return null;
        }

        InternalHandle<K, ?> parentHandle = null;
        if (parentId != null) {
            parentHandle = handles.get(parentId);
            if (parentHandle == null) {
                throw new ParentDoesNotExistException();
            }

            if (parentHandle.isValid() == false) {
                throw new ParentIsInvalidException();
            }
        }

        InternalHandle<K, ?> handle = handleCreator.create(parentId, id);

        InternalHandle<K, ?> existingHandle = handles.putIfAbsent(id, handle);
        if (existingHandle != null) {
            return null;
        }

        if (parentHandle != null) {
            parentHandle.incrementChildCount();
        }

        return handle;
    }

    public InternalHandle<K, ?> unregister(K id) {
        InternalHandle<K, ?> handle = handles.get(id);
        if (handle == null) {
            return null;
        }

        handle.lock();
        try {
            //Someone already beat us. Oh well..
            if (handle.isValid() == false) {
                return null;
            }

            handle.invalidate();

            K parentId = handle.getParentId();
            if (parentId != null) {
                InternalHandle<K, ?> parentHandle = handles.get(parentId);
                if (parentHandle != null) {
                    parentHandle.decrementChildCount();
                }
            }

            handles.remove(id);
        }
        finally {
            handle.unlock();
        }

        return handle;
    }

    //--------------

    public static class ParentDoesNotExistException extends LiteException {
    }

    public static class ParentIsInvalidException extends LiteException {
    }
}
