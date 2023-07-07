package com.tibco.cep.pattern.matcher.impl.master;

import java.util.concurrent.atomic.AtomicLong;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.service.IdGenerator;

/*
* Author: Ashwin Jayaprakash Date: Jun 30, 2009 Time: 11:13:20 AM
*/

public class DefaultCorrIdGenerator implements IdGenerator {
    protected Object family;

    protected AtomicLong counter;

    /**
     * First generated is 0.
     *
     * @param family
     */
    public DefaultCorrIdGenerator(Object family) {
        this.family = family;
        this.counter = new AtomicLong(0);
    }

    public DefaultCorrIdGenerator(String family, AtomicLong counter) {
        this.family = family;
        this.counter = counter;
    }

    public final DefaultId generateNew() {
        long number = counter.incrementAndGet();

        return new DefaultId(family, number);
    }

    //------------

    public DefaultCorrIdGenerator recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return this;
    }
}
