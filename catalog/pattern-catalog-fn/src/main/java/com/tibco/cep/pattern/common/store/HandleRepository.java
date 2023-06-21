package com.tibco.cep.pattern.common.store;

import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash / Date: Nov 3, 2009 / Time: 6:08:27 PM
*/
public interface HandleRepository<K> {
    /**
     * @param id
     * @return <code>null</code> if it does not exist.
     */
    InternalHandle<K, ?> getHandle(K id);

    /**
     * @param parentId
     * @param id
     * @return <code>null</code> if there already is an existing handle, otherwise the new handle
     *         that was created and registered.
     * @throws Exception
     * @see HandleCreator
     */
    InternalHandle<K, ?> createHandleAndRegister(@Optional K parentId, K id) throws Exception;

    /**
     * @param id
     * @return The handle that was successfully unregistered. Otherwise <code>null</code>.
     */
    InternalHandle<K, ?> unregister(K id);

    //--------------

    public static interface HandleCreator<K> {
        InternalHandle<K, ?> create(@Optional K parentId, K id);
    }
}
