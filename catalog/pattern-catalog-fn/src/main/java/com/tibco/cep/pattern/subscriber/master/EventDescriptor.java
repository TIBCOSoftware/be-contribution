package com.tibco.cep.pattern.subscriber.master;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.Resource;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 4:44:39 PM
*/

public interface EventDescriptor<T> extends Resource, Recoverable<EventDescriptor<T>> {
    /**
     * @return Sorted in ascending order.
     */
    String[] getSortedPropertyNames();

    /**
     * @param propertyName One of the types from {@link #getSortedPropertyNames()}
     * @return The data type of the requested property or <code>null</code> if the propertyName does
     *         not exist.
     */
    Class getPropertyType(String propertyName);

    Comparable extractPropertyValue(T source, String propertyName);

    Object extractUniqueId(T source);
}
