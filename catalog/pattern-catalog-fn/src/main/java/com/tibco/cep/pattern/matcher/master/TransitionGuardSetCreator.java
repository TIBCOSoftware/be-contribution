package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Sep 10, 2009 Time: 1:51:17 PM
*/
public interface TransitionGuardSetCreator<S extends TransitionGuardSet<K, V>,
        K extends TransitionGuardClosure, V extends TransitionGuard>
        extends Recoverable<TransitionGuardSetCreator<S, K, V>> {
    S create(ResourceProvider resourceProvider, AdvancedDriverOwner driverOwner);
}