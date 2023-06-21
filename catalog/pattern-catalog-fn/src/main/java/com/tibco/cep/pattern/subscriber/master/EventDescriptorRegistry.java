package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.service.Service;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 11:30:06 AM
*/
public interface EventDescriptorRegistry<E extends EventDescriptor> extends Service {
    /**
     * @param eventDescriptor
     * @return <code>false</code> if there is already an existing deployment.
     */
    boolean addEventDescriptor(E eventDescriptor);

    /**
     * @param id From {@link EventDescriptor#getResourceId()}.
     * @return
     */
    E getEventDescriptor(Id id);

    Collection<? extends E> getEventDescriptors();

    void removeEventDescriptor(E eventDescriptor);
}