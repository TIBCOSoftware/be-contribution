package com.tibco.cep.pattern.integ.impl.jmx;

import static com.tibco.cep.pattern.integ.impl.jmx.MBeanManager.generateDOGON;
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
import com.tibco.cep.pattern.integ.master.SourceBridge;
import com.tibco.cep.pattern.jmx.XDriverOwnerGroupMBean;
import com.tibco.cep.pattern.jmx.XDriverOwnerMBean;
import com.tibco.cep.pattern.jmx.XSourceMBean;
import com.tibco.cep.pattern.matcher.master.AdvancedDriverOwner;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.matcher.master.Plan;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.util.InternalObjectName;

/*
* Author: Ashwin Jayaprakash / Date: Oct 19, 2009 / Time: 3:18:56 PM
*/
public class XDriverOwnerGroup implements XDriverOwnerGroupMBean {
    protected Admin admin;

    protected LinkedList<InternalObjectName> selfAndChildren;

    public XDriverOwnerGroup(Admin admin) {
        this.admin = admin;
        this.selfAndChildren = new LinkedList<InternalObjectName>();
    }

    public void refresh() throws MalformedObjectNameException, MBeanRegistrationException,
            InstanceAlreadyExistsException, NotCompliantMBeanException {
        discard();

        //-------------

        MBeanServer platform = ManagementFactory.getPlatformMBeanServer();

        InternalObjectName doGroupName = generateDOGON(admin);

        register(platform, doGroupName, XDriverOwnerGroupMBean.class, this);
        selfAndChildren.add(doGroupName);

        //-------------

        refreshDriverOwners(doGroupName, platform, admin.getDriverOwners());
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

    protected void refreshDriverOwners(InternalObjectName parent, MBeanServer platform,
                                       Collection<? extends DriverOwner> driverOwners)
            throws MalformedObjectNameException, MBeanRegistrationException,
            InstanceAlreadyExistsException, NotCompliantMBeanException {
        for (DriverOwner driverOwner : driverOwners) {
            refreshDriverOwner(parent, platform, driverOwner);
        }
    }

    protected void refreshDriverOwner(InternalObjectName parent, MBeanServer platform,
                                      DriverOwner driverOwner) throws MalformedObjectNameException,
            InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        AdvancedDriverOwner advancedDriverOwner = (AdvancedDriverOwner) driverOwner;

        Plan plan = advancedDriverOwner.getPlan();
        Source[] sources = plan.getSources();

        XDriverOwner xDriverOwner = new XDriverOwner(advancedDriverOwner);

        InternalObjectName xdoName = generateON(parent, driverOwner);

        register(platform, xdoName, XDriverOwnerMBean.class, xDriverOwner);

        for (Source source : sources) {
            SourceBridge sourceBridge = (SourceBridge) source;

            String alias = sourceBridge.getAlias();
            EventDescriptor eventDescriptor = sourceBridge.getEventDescriptor();
            InternalObjectName eventDescriptorRef = generateON(parent, eventDescriptor);

            XSource xSource = new XSource(alias, eventDescriptorRef.toObjectName());

            InternalObjectName xsName = generateON(xdoName, source);

            register(platform, xsName, XSourceMBean.class, xSource);

            selfAndChildren.add(xsName);
        }

        selfAndChildren.add(xdoName);
    }
}
