package com.tibco.cep.pattern.integ.impl.jmx;

import javax.management.ObjectName;

import com.tibco.cep.pattern.jmx.XSourceMBean;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 4:27:07 PM
*/
public class XSource implements XSourceMBean {
    protected String alias;

    protected ObjectName eventDescriptorRef;

    public XSource(String alias, ObjectName eventDescriptorRef) {
        this.alias = alias;
        this.eventDescriptorRef = eventDescriptorRef;
    }

    public String getAlias() {
        return alias;
    }

    public ObjectName getEventDescriptorRef() {
        return eventDescriptorRef;
    }
}
