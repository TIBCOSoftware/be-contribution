package com.tibco.cep.pattern.matcher.master;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.service.Service;

/*
* Author: Ashwin Jayaprakash Date: Jul 23, 2009 Time: 3:01:14 PM
*/
public interface DriverOwnerRegistry<D extends DriverOwner> extends Service {
    /**
     * @param driverOwner
     * @return <code>false</code> if there is already an existing driver-owner.
     */
    boolean addDriverOwner(D driverOwner);

    /**
     * @param driverOwnerId From {@link DriverOwner#getOwnerId()}.
     * @return
     */
    D getDriverOwner(Id driverOwnerId);

    Collection<D> getDriverOwners();

    void removeDriverOwner(D driverOwner);
}
