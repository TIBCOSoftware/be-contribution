package com.tibco.cep.pattern.subscriber.admin;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;

/*
* Author: Ashwin Jayaprakash Date: Aug 27, 2009 Time: 6:39:16 PM
*/
public interface SubscriberAdmin extends Subscriber {
    void start() throws LifecycleException;

    void stop() throws LifecycleException;

    //------------

    void deploy(EventSource eventSource) throws LifecycleException;

    void undeploy(EventSource eventSource) throws LifecycleException;

    //------------

    void register(EventDescriptor eventDescriptor) throws LifecycleException;

    void unregister(EventDescriptor eventDescriptor) throws LifecycleException;

    //------------

    ResourceProvider getResourceProvider();
}
