package com.tibco.cep.pattern.matcher.impl.master;

import java.util.HashMap;
import java.util.Map;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.AdvancedDriverOwner;
import com.tibco.cep.pattern.matcher.master.TransitionGuard;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;

/*
* Author: Ashwin Jayaprakash Date: Sep 11, 2009 Time: 2:08:40 PM
*/

/**
 * @see ReflectionTransitionGuardSet
 */
public class ReflectionTransitionGuardSetCreator<K extends TransitionGuardClosure>
        implements
        TransitionGuardSetCreator<ReflectionTransitionGuardSet<K, TransitionGuard>, K, TransitionGuard> {
    protected Map<K, Class<? extends TransitionGuard>> closuresAndGuardClasses;

    public ReflectionTransitionGuardSetCreator() {
        this.closuresAndGuardClasses = new HashMap<K, Class<? extends TransitionGuard>>(2);
    }

    public ReflectionTransitionGuardSetCreator<K> addClosureAndGuard(K closure,
                                                                     Class<? extends TransitionGuard> guardClass) {
        closuresAndGuardClasses.put(closure, guardClass);

        return this;
    }

    public Map<K, Class<? extends TransitionGuard>> getClosuresAndGuardClasses() {
        return closuresAndGuardClasses;
    }

    public ReflectionTransitionGuardSet<K, TransitionGuard> create(
            ResourceProvider resourceProvider, AdvancedDriverOwner driverOwner) {
        return new ReflectionTransitionGuardSet<K, TransitionGuard>(closuresAndGuardClasses);
    }

    public ReflectionTransitionGuardSetCreator<K> recover(
            ResourceProvider resourceProvider, Object... params) throws RecoveryException {
        return this;
    }
}
