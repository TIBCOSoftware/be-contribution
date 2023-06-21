package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.EndSource;

/*
* Author: Ashwin Jayaprakash Date: Jul 22, 2009 Time: 5:39:01 PM
*/
public class DefaultEndInputDef extends DefaultInputDef<EndSource> {
    public DefaultEndInputDef() {
    }

    @Override
    public DefaultEndInputDef recover(ResourceProvider serviceProvider, Object... params)
            throws RecoveryException {
        super.recover(serviceProvider, params);

        return this;
    }
}