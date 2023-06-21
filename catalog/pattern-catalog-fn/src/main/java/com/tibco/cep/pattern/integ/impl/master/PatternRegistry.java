package com.tibco.cep.pattern.integ.impl.master;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.util.annotation.ThreadSafe;

/*
* Author: Ashwin Jayaprakash / Date: Mar 9, 2010 / Time: 4:30:49 PM
*/

@ThreadSafe
public class PatternRegistry implements Recoverable<PatternRegistry> {
    protected ConcurrentHashMap<String,
            /*This is a reverse map of Instance name to Uri*/ ConcurrentHashMap<String, String>> patternUriAndInstances;

    public PatternRegistry() {
        this.patternUriAndInstances = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();

    }

    public void registerPatternUri(String uri) {
        ConcurrentHashMap<String, String> old =
                patternUriAndInstances.putIfAbsent(uri, new ConcurrentHashMap<String, String>());

        if (old != null) {
            throw new RuntimeException("URI [" + uri + "] already exists.");
        }
    }

    public void registerPatternInstanceName(String uri, String instanceName) {
        ConcurrentHashMap<String, String> instances = patternUriAndInstances.get(uri);

        if (instances == null) {
            throw new RuntimeException("URI [" + uri + "] does not exist.");
        }

        Object existing = instances.putIfAbsent(instanceName, uri);

        if (existing != null) {
            throw new RuntimeException("Instance [" + instanceName + "] already exists under the URI [" + uri + "].");
        }
    }

    /**
     * @return May be empty but never null.
     */
    public Collection<String> getPatternUris() {
        return new LinkedList<String>(patternUriAndInstances.keySet());
    }

    /**
     * @param uri
     * @return May be empty but never null.
     */
    public Collection<String> getPatternInstanceNames(String uri) {
        ConcurrentHashMap<String, String> instances = patternUriAndInstances.get(uri);

        if (instances == null) {
            return new LinkedList<String>();
        }

        return new LinkedList<String>(instances.keySet());
    }

    /**
     * @param patternInstanceName
     * @return null if there is no such instance.
     */
    public String getPatternUri(String patternInstanceName) {
        for (ConcurrentHashMap<String, String> reverseMap : patternUriAndInstances.values()) {
            String uri = reverseMap.get(patternInstanceName);

            if (uri != null) {
                return uri;
            }
        }

        return null;
    }

    public void unregisterPatternInstanceName(String uri, String instanceName) {
        ConcurrentHashMap<String, String> instances = patternUriAndInstances.get(uri);

        if (instances == null) {
            throw new RuntimeException("URI [" + uri + "] does not exist.");
        }

        instances.remove(instanceName);
    }

    public void unregisterPatternUri(String uri) {
        ConcurrentHashMap<String, ?> instances = patternUriAndInstances.remove(uri);

        if (instances != null) {
            instances.clear();
        }
    }

    //----------------

    @Override
    public PatternRegistry recover(ResourceProvider resourceProvider, Object... params) throws RecoveryException {
        return this;
    }
}
