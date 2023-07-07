package com.tibco.cep.pattern.jmx;

import javax.management.ObjectName;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 4:27:07 PM
*/
public interface XSourceMBean {
    String getAlias();

    ObjectName getEventDescriptorRef();
}
