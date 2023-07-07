package com.tibco.cep.pattern.integ.impl.jmx;

import com.tibco.cep.pattern.jmx.XEventDescriptorMBean;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 4:27:07 PM
*/
public class XEventDescriptor implements XEventDescriptorMBean {
    protected String[] propertyNames;

    public XEventDescriptor(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    public String[] getPropertyNames() {
        return propertyNames;
    }
}
