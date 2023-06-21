package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 1:22:59 PM
*/
public interface TimeSource extends Source {
    TimeSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException;
}
