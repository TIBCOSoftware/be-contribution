package com.tibco.cep.pattern.matcher.admin;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.dsl.PatternDeploymentDef;
import com.tibco.cep.pattern.matcher.master.DriverOwner;

/*
* Author: Ashwin Jayaprakash Date: Jul 22, 2009 Time: 11:37:33 AM
*/
public interface MatcherAdmin<D extends PatternDeploymentDef> extends Matcher {
    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //------------

    D createDeployment(Id id);

    DriverOwner deploy(D patternDeployment) throws LifecycleException;

    void undeploy(DriverOwner driverOwner) throws LifecycleException;

    //------------

    ResourceProvider getResourceProvider();
}
