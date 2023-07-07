package com.tibco.cep.pattern.matcher.impl.master;

import java.util.Map;
import java.util.Set;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.TransitionGuard;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSet;

/*
* Author: Ashwin Jayaprakash Date: Sep 11, 2009 Time: 2:09:02 PM
*/

/**
 * All the {@link V} classes should have an empty constructor.
 */
public class ReflectionTransitionGuardSet<K extends TransitionGuardClosure, V extends TransitionGuard>
        implements TransitionGuardSet<K, V> {
    protected Map<K, Class<? extends V>> closuresAndGuardClasses;

    protected transient ResourceProvider resourceProvider;

    protected transient Driver driver;

    public ReflectionTransitionGuardSet(Map<K, Class<? extends V>> closuresAndGuardClasses) {
        this.closuresAndGuardClasses = closuresAndGuardClasses;
    }

    public Set<K> getTransitionGuardClosures() {
        return closuresAndGuardClasses.keySet();
    }

    public void start(ResourceProvider resourceProvider, Driver driver) {
        this.resourceProvider = resourceProvider;
        this.driver = driver;
    }

    /**
     * @param closure
     * @return
     * @throws RuntimeException If an error occurs during reflective creation of the {@link
     *                          TransitionGuard} class.
     */
    public V create(K closure) {
        Class<? extends V> clazz = closuresAndGuardClasses.get(closure);

        try {
            V transitionGuard = clazz.newInstance();

            transitionGuard.init(resourceProvider, driver, this);

            return transitionGuard;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
    }

    public ReflectionTransitionGuardSet<K, V> recover(ResourceProvider resourceProvider,
                                                      Object... params)
            throws RecoveryException {
        return this;
    }
}
