package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import com.tibco.be.model.rdf.primitives.RDFPrimitiveTerm;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.designtime.model.event.Event;
import com.tibco.cep.designtime.model.event.EventPropertyDefinition;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.impl.util.EventProperty;
import com.tibco.cep.pattern.integ.master.MessageId;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.runtime.model.event.SimpleEvent;

/*
* Author: Ashwin Jayaprakash Date: Sep 21, 2009 Time: 11:46:37 AM
*/

/**
 * Relies on methods annotated by {@link EventProperty}.
 * <p/>
 * {@link #equals(Object)} only on {@link #getResourceId()} - keyed using {@link #getClassName()}.
 */
public class SimpleEventDescriptor<T extends SimpleEvent> implements EventDescriptor<T> {
    protected Id id;

    protected String className;

    protected String[] sortedPropertyNames;

    protected Class[] sortedPropertyTypes;

    /**
     * @param className
     * @param event
     */
    public SimpleEventDescriptor(String className, Event event) {
        this.id = new DefaultId("Event", className);
        this.className = className;

        Object[] namesAndTypes = collectPropertyNames(event);
        this.sortedPropertyNames = (String[]) namesAndTypes[0];
        this.sortedPropertyTypes = (Class[]) namesAndTypes[1];
    }

    /**
     * @param event
     * @return [0] = String[] [1] = Class[]
     */
    private static Object[] collectPropertyNames(Event event) {
        TreeMap<String, Class> propertyNamesAndTypes = new TreeMap<String, Class>();

        List<? extends EventPropertyDefinition> userProperties = event.getAllUserProperties();
        for (EventPropertyDefinition propertyDefinition : userProperties) {
            String propertyName = propertyDefinition.getPropertyName();

            RDFPrimitiveTerm primitiveTerm = propertyDefinition.getType();
            Class propertyType = TypeHelper.convertRDFtoJavaType(primitiveTerm);

            propertyNamesAndTypes.put(propertyName, propertyType);
        }

        String[] propertyNames = new String[propertyNamesAndTypes.size()];
        Class[] propertyTypes = new Class[propertyNamesAndTypes.size()];
        int k = 0;
        for (String propertyName : propertyNamesAndTypes.keySet()) {
            Class propertyType = propertyNamesAndTypes.get(propertyName);

            propertyNames[k] = propertyName;
            propertyTypes[k] = propertyType;
            k++;
        }

        return new Object[]{propertyNames, propertyTypes};
    }

    public String getClassName() {
        return className;
    }

    public Id getResourceId() {
        return id;
    }

    public String[] getSortedPropertyNames() {
        return sortedPropertyNames;
    }

    public Class getPropertyType(String propertyName) {
        int position = Arrays.binarySearch(sortedPropertyNames, propertyName);

        if (position < 0 || position >= sortedPropertyNames.length) {
            return null;
        }

        return sortedPropertyTypes[position];
    }

    public Comparable extractPropertyValue(T source, String propertyName) {
        try {
            Object result = source.getProperty(propertyName);

            return (Comparable) result;
        }
        catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public MessageId extractUniqueId(T source) {
        com.tibco.cep.kernel.model.entity.Id id = source.getId();

        return new MessageId(id);
    }

    //-------------

    public SimpleEventDescriptor<T> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return this;
    }

    //-------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleEventDescriptor)) {
            return false;
        }

        SimpleEventDescriptor that = (SimpleEventDescriptor) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

