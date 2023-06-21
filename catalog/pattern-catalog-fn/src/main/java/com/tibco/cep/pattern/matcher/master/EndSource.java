package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;

/*
* Author: Ashwin Jayaprakash Date: Jul 30, 2009 Time: 2:13:12 PM
*/
public interface EndSource extends Source {
    EndSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException;
}
