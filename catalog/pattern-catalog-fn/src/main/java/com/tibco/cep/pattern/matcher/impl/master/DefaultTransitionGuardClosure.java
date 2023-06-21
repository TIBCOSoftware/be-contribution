package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;

/*
* Author: Ashwin Jayaprakash Date: Sep 10, 2009 Time: 3:18:01 PM
*/
public class DefaultTransitionGuardClosure implements TransitionGuardClosure {
    protected String id;

    public DefaultTransitionGuardClosure(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public DefaultTransitionGuardClosure recover(ResourceProvider resourceProvider,
                                                 Object... params)
            throws RecoveryException {
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultTransitionGuardClosure)) {
            return false;
        }

        DefaultTransitionGuardClosure that = (DefaultTransitionGuardClosure) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }
}
