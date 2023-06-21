package com.tibco.cep.pattern.integ.impl.jmx;

import static com.tibco.cep.pattern.impl.util.HashGenerator.toMD5Hash;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.util.InternalObjectName;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 4:27:07 PM
*/
public class MBeanManager {
    protected XEventDescriptorGroup xEventDescriptorGroup;

    protected XDriverOwnerGroup xDriverOwnerGroup;

    public MBeanManager() {
    }

    //----------------

    public static String getDomain() {
        return "Pattern";
    }

    public static String generateAdminKV(Admin admin) {
        return "typeAdmin=Admin__" + toMD5Hash(System.identityHashCode(admin));
    }

    public static String generateEDGKV() {
        return "typeEventDescriptors=EventDescriptors";
    }

    public static String generateEDKV(EventDescriptor eventDescriptor) {
        return "typeEventDescriptor=EventDescriptor__" +
                toMD5Hash(eventDescriptor.getResourceId());
    }

    public static String generateDOGKV() {
        return "typeDriverOwners=DriverOwners";
    }

    public static String generateDOKV(DriverOwner driverOwner) {
        return "typeDriverOwner=DriverOwner__" + toMD5Hash(driverOwner.getOwnerId());
    }

    public static String generateSKV(Source source) {
        return "typeSource=Source__" + toMD5Hash(source.getResourceId());
    }

    //----------------

    public static InternalObjectName generateRootON(Admin admin)
            throws MalformedObjectNameException {
        return new InternalObjectName(getDomain(), generateAdminKV(admin));
    }

    protected static InternalObjectName generateEDGON(Admin admin)
            throws MalformedObjectNameException {
        return new InternalObjectName(getDomain(), generateAdminKV(admin), generateEDGKV());
    }

    protected static InternalObjectName generateON(InternalObjectName parent,
                                                   EventDescriptor eventDescriptor)
            throws MalformedObjectNameException {
        return new InternalObjectName(parent, generateEDKV(eventDescriptor));
    }

    protected static InternalObjectName generateDOGON(Admin admin)
            throws MalformedObjectNameException {
        return new InternalObjectName(getDomain(), generateAdminKV(admin), generateDOGKV());
    }

    protected static InternalObjectName generateON(InternalObjectName parent,
                                                   DriverOwner driverOwner)
            throws MalformedObjectNameException {
        return new InternalObjectName(parent, generateDOKV(driverOwner));
    }

    protected static InternalObjectName generateON(InternalObjectName parent, Source source)
            throws MalformedObjectNameException {
        return new InternalObjectName(parent, generateSKV(source));
    }

    //----------------

    /**
     * @param platform
     * @param objectName
     * @param mbeanInterface
     * @param mbeanInstance
     * @return <code>true</code> if the registration succeeded. <code>false</code> if it already
     *         exists.
     * @throws NotCompliantMBeanException
     * @throws MalformedObjectNameException
     * @throws MBeanRegistrationException
     * @throws InstanceAlreadyExistsException
     */
    public static boolean registerIfNotExists(MBeanServer platform, InternalObjectName objectName,
                                              Class mbeanInterface, Object mbeanInstance)
            throws NotCompliantMBeanException, MalformedObjectNameException,
            MBeanRegistrationException, InstanceAlreadyExistsException {
        ObjectName on = objectName.toObjectName();

        if (platform.isRegistered(on)) {
            return false;
        }

        StandardMBean standardMBean = new StandardMBean(mbeanInstance, mbeanInterface);

        platform.registerMBean(standardMBean, on);

        return true;
    }

    /**
     * If an MBean exists then that instance is unregistered and then this instance is registered in
     * its place.
     *
     * @param platform
     * @param objectName
     * @param mbeanInterface
     * @param mbeanInstance
     * @throws MBeanRegistrationException
     * @throws InstanceAlreadyExistsException
     * @throws NotCompliantMBeanException
     * @throws MalformedObjectNameException
     */
    public static void register(MBeanServer platform, InternalObjectName objectName,
                                Class mbeanInterface, Object mbeanInstance)
            throws MBeanRegistrationException, InstanceAlreadyExistsException,
            NotCompliantMBeanException, MalformedObjectNameException {
        ObjectName on = objectName.toObjectName();

        if (platform.isRegistered(on)) {
            try {
                platform.unregisterMBean(on);
            }
            catch (InstanceNotFoundException e) {
                //Ignore.
            }
        }

        StandardMBean standardMBean = new StandardMBean(mbeanInstance, mbeanInterface);

        platform.registerMBean(standardMBean, on);
    }

    public static void unregister(MBeanServer platform, InternalObjectName objectName)
            throws InstanceNotFoundException, MBeanRegistrationException,
            MalformedObjectNameException {
        platform.unregisterMBean(objectName.toObjectName());
    }

    //----------------

    public void init(Admin admin) throws MalformedObjectNameException, MBeanRegistrationException,
            InstanceAlreadyExistsException, NotCompliantMBeanException {
        xEventDescriptorGroup = new XEventDescriptorGroup(admin);
        xEventDescriptorGroup.refresh();

        xDriverOwnerGroup = new XDriverOwnerGroup(admin);
        xDriverOwnerGroup.refresh();
    }

    public void discard() throws MalformedObjectNameException, MBeanRegistrationException {
        if (xDriverOwnerGroup != null) {
            xDriverOwnerGroup.discard();
            xDriverOwnerGroup = null;
        }

        if (xEventDescriptorGroup != null) {
            xEventDescriptorGroup.discard();
            xEventDescriptorGroup = null;
        }
    }
}
