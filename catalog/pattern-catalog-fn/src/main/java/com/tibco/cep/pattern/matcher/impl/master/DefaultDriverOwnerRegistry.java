package com.tibco.cep.pattern.matcher.impl.master;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.matcher.master.DriverOwnerRegistry;

/*
* Author: Ashwin Jayaprakash Date: Jul 16, 2009 Time: 6:13:54 PM
*/
public class DefaultDriverOwnerRegistry implements DriverOwnerRegistry<AbstractDriverOwner> {
    protected ConcurrentHashMap<Id, AbstractDriverOwner> driverOwners;

    protected Id resourceId;

    public DefaultDriverOwnerRegistry() {
        this.driverOwners = new ConcurrentHashMap<Id, AbstractDriverOwner>();
        this.resourceId = new DefaultId(getClass().getName());
    }

    public Id getResourceId() {
        return resourceId;
    }

    //------------

    /**
     * Starts all the driver-owners.
     */
    public void start() throws LifecycleException {
        for (AbstractDriverOwner driverOwner : driverOwners.values()) {
            driverOwner.start();
        }
    }

    /**
     * Stops all the driver-owners.
     */
    public void stop() throws LifecycleException {
        for (AbstractDriverOwner driverOwner : driverOwners.values()) {
            driverOwner.stop();
        }
    }

    public DefaultDriverOwnerRegistry recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        for (Entry<Id, AbstractDriverOwner> entry : driverOwners.entrySet()) {
            AbstractDriverOwner driverOwner = entry.getValue();

            driverOwner = driverOwner.recover(resourceProvider, params);

            entry.setValue(driverOwner);
        }

        return this;
    }

    //------------

    public boolean addDriverOwner(AbstractDriverOwner driverOwner) {
        Object existing = driverOwners.putIfAbsent(driverOwner.getOwnerId(), driverOwner);

        if (existing == null) {
            try {
                driverOwner.start();
            }
            catch (LifecycleException e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        return false;
    }

    public AbstractDriverOwner getDriverOwner(Id driverOwnerId) {
        return driverOwners.get(driverOwnerId);
    }

    public Collection<AbstractDriverOwner> getDriverOwners() {
        return new LinkedList<AbstractDriverOwner>(driverOwners.values());
    }

    public void removeDriverOwner(AbstractDriverOwner driverOwner) {
        try {
            driverOwner.stop();
        }
        catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

        driverOwners.remove(driverOwner.getOwnerId());
    }
}
