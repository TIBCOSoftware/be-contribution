package com.tibco.cep.pattern.integ.admin;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.admin.Matcher;
import com.tibco.cep.pattern.subscriber.admin.SubscriberAdmin;

/*
* Author: Ashwin Jayaprakash Date: Aug 24, 2009 Time: 2:29:00 PM
*/
public interface Admin<S extends Session> extends SubscriberAdmin, Matcher {
    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //------------

    S create(Id id);

    void deploy(S session) throws LifecycleException;

    void undeploy(Id sessionId) throws LifecycleException;

    //------------

    void setMaxExecutorThreads(int numThreads);

    int getMaxExecutorThreads();

    void setMaxSchedulerThreads(int numThreads);

    int getMaxSchedulerThreads();

    //------------

    ResourceProvider getResourceProvider();
}
