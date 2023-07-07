package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.InputDef;

/*
* Author: Ashwin Jayaprakash Date: Jul 9, 2009 Time: 3:00:09 PM
*/
public class DefaultInputDef<S extends Source> implements InputDef {
    protected S source;

    public DefaultInputDef() {
    }

    public S getSource() {
        return source;
    }

    public void setSource(S source) {
        this.source = source;
    }

    //-----------

    public DefaultInputDef recover(ResourceProvider serviceProvider, Object... params)
            throws RecoveryException {
        source = (S) source.recover(serviceProvider, params);

        return this;
    }

    //-----------

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultInputDef)) {
            return false;
        }

        DefaultInputDef that = (DefaultInputDef) o;

        if (source != null ? !source.equals(that.source) : that.source != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return (source != null ? source.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + "{Source: " + source + "}}";
    }
}
