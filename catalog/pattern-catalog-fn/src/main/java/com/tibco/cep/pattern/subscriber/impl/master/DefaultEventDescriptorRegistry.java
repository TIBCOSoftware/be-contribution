package com.tibco.cep.pattern.subscriber.impl.master;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;
import com.tibco.cep.service.Service;

/*
* Author: Ashwin Jayaprakash Date: Aug 20, 2009 Time: 11:01:24 AM
*/
public class DefaultEventDescriptorRegistry
        implements EventDescriptorRegistry<EventDescriptor<Object>> {
    protected ConcurrentHashMap<Id, EventDescriptor<Object>> eventDescriptors;

    protected Id resourceId;

    public DefaultEventDescriptorRegistry() {
        this.eventDescriptors = new ConcurrentHashMap<Id, EventDescriptor<Object>>();
        this.resourceId = new DefaultId(getClass());
    }

    public Id getResourceId() {
        return resourceId;
    }

    //-------------

    public void start() throws LifecycleException {
    }

    public void stop() throws LifecycleException {
    }

    //-------------

    public boolean addEventDescriptor(EventDescriptor<Object> eventDescriptor) {
        EventDescriptor existing =
                eventDescriptors.putIfAbsent(eventDescriptor.getResourceId(), eventDescriptor);

        return (existing == null);
    }

    public EventDescriptor<Object> getEventDescriptor(Id id) {
        return eventDescriptors.get(id);
    }

    public Collection<? extends EventDescriptor<Object>> getEventDescriptors() {
        return eventDescriptors.values();
    }

    public void removeEventDescriptor(EventDescriptor eventDescriptor) {
        eventDescriptors.remove(eventDescriptor.getResourceId());
    }

    //-------------

    public Service recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        ConcurrentHashMap<Id, EventDescriptor<Object>> newMap =
                new ConcurrentHashMap<Id, EventDescriptor<Object>>(eventDescriptors.size());

        for (Entry<Id, EventDescriptor<Object>> entry : eventDescriptors.entrySet()) {
            EventDescriptor<Object> ed = entry.getValue();

            ed = ed.recover(resourceProvider, params);

            newMap.put(ed.getResourceId(), ed);
        }

        eventDescriptors.clear();
        eventDescriptors = newMap;

        return this;
    }
}
