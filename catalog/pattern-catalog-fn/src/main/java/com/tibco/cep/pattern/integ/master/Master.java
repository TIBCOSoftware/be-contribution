package com.tibco.cep.pattern.integ.master;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/*
* Author: Ashwin Jayaprakash Date: Sep 22, 2009 Time: 4:12:02 PM
*/
public interface Master {
    void start(RuleServiceProvider rsp) throws LifecycleException;

    Admin getAdmin();

    void stop() throws LifecycleException;
}
