package com.tibco.cep.pattern.matcher.response;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash Date: Jul 7, 2009 Time: 4:21:30 PM
*/
public class DefaultComplete extends DefaultSuccess implements Complete {
    public DefaultComplete(Id correlationId, Id instanceId) {
        super(correlationId, instanceId);
    }
}
