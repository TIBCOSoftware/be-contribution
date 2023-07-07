package com.tibco.cep.pattern.subscriber.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Aug 19, 2009 Time: 11:59:02 AM
*/
public class DefaultRoutingTable extends AbstractRoutingTable {
    public DefaultRoutingTable(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }

    @Override
    public DefaultRoutingTable recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        return this;
    }
}
