package com.tibco.cep.pattern.integ.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.integ.master.SourceBridge;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash Date: Aug 24, 2009 Time: 5:18:06 PM
*/
public class DefaultSourceBridge<T> implements SourceBridge<T> {
    protected Id parentId;

    protected Id sourceId;

    protected String alias;

    protected EventDescriptor<T> eventDescriptor;

    /**
     * @param parentId        The container's/owner's Id.
     * @param id              The Id of this instance. Usually - parentId & alias.
     * @param alias           The user given name for this instance.
     * @param eventDescriptor The actual source that this bridge is getting input from.
     */
    public DefaultSourceBridge(Id parentId, Id id, String alias,
                               EventDescriptor<T> eventDescriptor) {
        this.parentId = parentId;
        this.sourceId = id;
        this.alias = alias;
        this.eventDescriptor = eventDescriptor;
    }

    public Id getResourceId() {
        return sourceId;
    }

    public EventDescriptor<T> getEventDescriptor() {
        return eventDescriptor;
    }

    public String getAlias() {
        return alias;
    }

    public Id getParentId() {
        return parentId;
    }

    //-------------

    public DefaultSourceBridge<T> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        eventDescriptor = eventDescriptor.recover(resourceProvider, params);

        return this;
    }

    //-------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSourceBridge)) {
            return false;
        }

        DefaultSourceBridge that = (DefaultSourceBridge) o;

        if (!alias.equals(that.alias)) {
            return false;
        }
        if (!eventDescriptor.equals(that.eventDescriptor)) {
            return false;
        }
        if (!parentId.equals(that.parentId)) {
            return false;
        }
        if (!sourceId.equals(that.sourceId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = parentId.hashCode();
        result = 31 * result + sourceId.hashCode();
        result = 31 * result + alias.hashCode();
        result = 31 * result + eventDescriptor.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + "{ParentId: " + parentId + "}, SourceId: " +
                sourceId + "}, {Alias: " + alias + "}}";
    }
}
