package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.EndSource;

/*
* Author: Ashwin Jayaprakash Date: Jul 30, 2009 Time: 2:43:03 PM
*/
public class DefaultEndSource extends DefaultSource implements EndSource {
    public DefaultEndSource(Id resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public DefaultEndSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        return this;
    }
}
