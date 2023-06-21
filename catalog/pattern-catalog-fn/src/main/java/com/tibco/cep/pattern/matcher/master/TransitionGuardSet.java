package com.tibco.cep.pattern.matcher.master;

import java.util.Set;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Sep 10, 2009 Time: 1:51:17 PM
*/
/**
 * One instance for each {@link Driver}.
 */
public interface TransitionGuardSet<K extends TransitionGuardClosure, V extends TransitionGuard>
        extends Recoverable<TransitionGuardSet<K, V>> {
    Set<K> getTransitionGuardClosures();

    void start(ResourceProvider resourceProvider, Driver driver);

    /**
     * @param closure Has to be one of the items returned by {@link #getTransitionGuardClosures()}.
     * @return
     */
    V create(K closure);

    void stop();
}