package com.tibco.cep.pattern.common;

/*
* Author: Ashwin Jayaprakash / Date: Nov 2, 2009 / Time: 4:41:09 PM
*/
public interface AsyncJob<T> {
    boolean isDone();

    T get() throws Exception;
}
