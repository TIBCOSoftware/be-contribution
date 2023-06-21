package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Source;

/*
* Author: Ashwin Jayaprakash Date: Jul 9, 2009 Time: 3:24:03 PM
*/
public class DefaultSource implements Source {
    protected Id resourceId;

    public DefaultSource() {
    }

    public Id getResourceId() {
        return resourceId;
    }

    public void setResourceId(Id resourceId) {
        this.resourceId = resourceId;
    }

    //-----------------

    public DefaultSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return this;
    }

    //-----------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DefaultSource)) {
            return false;
        }

        DefaultSource that = (DefaultSource) o;

        if (resourceId != null ? !resourceId.equals(that.resourceId) : that.resourceId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (resourceId != null ? resourceId.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + "{SourceId: " + resourceId + "}}";
    }
}
