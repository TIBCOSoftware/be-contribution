package com.tibco.cep.pattern.matcher.response;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash Date: Jul 7, 2009 Time: 4:20:45 PM
*/
public class DefaultSuccess implements Success {
    protected Id correlationId;

    protected Id instanceId;

    public DefaultSuccess(Id correlationId, Id instanceId) {
        this.correlationId = correlationId;
        this.instanceId = instanceId;
    }

    public Id getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Id correlationId) {
        this.correlationId = correlationId;
    }

    public Id getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Id instanceId) {
        this.instanceId = instanceId;
    }
}
