package com.tibco.cep.pattern.integ.impl.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Resource;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/*
* Author: Ashwin Jayaprakash Date: Sep 21, 2009 Time: 2:32:50 PM
*/
public class RSPResource implements Resource {
    protected Id resourceId;

    protected RuleServiceProvider rsp;

    public RSPResource(RuleServiceProvider rsp) {
        this.resourceId = new DefaultId(rsp.getName(), getClass().getName());
        this.rsp = rsp;
    }

    public Id getResourceId() {
        return resourceId;
    }

    public RuleServiceProvider getRsp() {
        return rsp;
    }
}
