package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.EndSource;

/*
* Author: Ashwin Jayaprakash Date: Jun 25, 2009 Time: 4:58:36 PM
*/

/**
 * This is just an internal token-type. This input is not expected from any external source but from
 * the driver itself.
 */
public interface ExpectedEndInput extends ExpectedInput {
    EndSource getSource();

    ExpectedEndInput recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException;
}