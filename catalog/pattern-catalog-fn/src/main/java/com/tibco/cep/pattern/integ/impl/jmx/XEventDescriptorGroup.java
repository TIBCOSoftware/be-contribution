package com.tibco.cep.pattern.integ.impl.jmx;

import static com.tibco.cep.pattern.integ.impl.jmx.MBeanManager.generateEDGON;
import static com.tibco.cep.pattern.integ.impl.jmx.MBeanManager.generateON;
import static com.tibco.cep.pattern.integ.impl.jmx.MBeanManager.register;
import static com.tibco.cep.pattern.integ.impl.jmx.MBeanManager.unregister;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.LinkedList;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.jmx.XEventDescriptorGroupMBean;
import com.tibco.cep.pattern.jmx.XEventDescriptorMBean;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.util.InternalObjectName;

/*
* Author: Ashwin Jayaprakash / Date: Oct 19, 2009 / Time: 3:19:08 PM
*/
public class XEventDescriptorGroup implements XEventDescriptorGroupMBean {
    protected Admin admin;

    protected LinkedList<InternalObjectName> selfAndChildren;

    public XEventDescriptorGroup(Admin admin) {
        this.admin = admin;
        this.selfAndChildren = new LinkedList<InternalObjectName>();
    }

    public void refresh() throws MalformedObjectNameException, MBeanRegistrationException,
            InstanceAlreadyExistsException, NotCompliantMBeanException {
        discard();

        //-------------

        MBeanServer platform = ManagementFactory.getPlatformMBeanServer();

        InternalObjectName edGroupName = generateEDGON(admin);

        register(platform, edGroupName, XEventDescriptorGroupMBean.class, this);

        selfAndChildren.add(edGroupName);

        //-------------

        refreshEventDescriptors(edGroupName, platform, admin.getEventDescriptors());
    }

    public void discard() throws MalformedObjectNameException, MBeanRegistrationException {
        MBeanServer platform = ManagementFactory.getPlatformMBeanServer();

        for (InternalObjectName child : selfAndChildren) {
            try {
                unregister(platform, child);
            }
            catch (InstanceNotFoundException e) {
                //Ignore.
            }
        }

        selfAndChildren.clear();
    }

    protected void refreshEventDescriptors(InternalObjectName parent, MBeanServer platform,
                                           Collection<? extends EventDescriptor> eventDescriptors)
            throws MalformedObjectNameException, MBeanRegistrationException,
            InstanceAlreadyExistsException, NotCompliantMBeanException {
        for (EventDescriptor eventDescriptor : eventDescriptors) {
            XEventDescriptor xEventDescriptor =
                    new XEventDescriptor(eventDescriptor.getSortedPropertyNames());

            InternalObjectName xedName = generateON(parent, eventDescriptor);

            register(platform, xedName, XEventDescriptorMBean.class, xEventDescriptor);

            selfAndChildren.add(xedName);
        }
    }
}
