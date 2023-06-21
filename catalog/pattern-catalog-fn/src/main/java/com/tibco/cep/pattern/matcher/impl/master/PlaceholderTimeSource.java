package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.TimeSource;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:21:18 PM
*/

/**
 * A light-weight time-source to be used during design-time.
 */
public class PlaceholderTimeSource extends DefaultSource implements TimeSource {
    public PlaceholderTimeSource(Id resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public PlaceholderTimeSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        return this;
    }
}