package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Jul 27, 2009 Time: 3:40:13 PM
*/
public interface DriverCallbackCreator extends Recoverable<DriverCallbackCreator> {
    DriverCallback create(ResourceProvider resourceProvider, AdvancedDriverOwner driverOwner);
}
