package com.tibco.cep.pattern.matcher.admin;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.DriverOwner;

/*
* Author: Ashwin Jayaprakash Date: Oct 8, 2009 Time: 1:12:26 PM
*/
public interface Matcher {
    DriverOwner getDriverOwner(Id id);

    Collection<? extends DriverOwner> getDriverOwners();
}
