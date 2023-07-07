package com.tibco.cep.pattern.matcher.impl.model;

import java.util.UUID;

import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 29, 2009 Time: 3:05:24 PM
*/
public class DefaultInput implements Input {
    protected Object key;

    /**
     * Generates a key automatically using {@link UUID#randomUUID()}.
     */
    public DefaultInput() {
        this.key = UUID.randomUUID();
    }

    public DefaultInput(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + " {Key: " + key + "}}";
    }
}
