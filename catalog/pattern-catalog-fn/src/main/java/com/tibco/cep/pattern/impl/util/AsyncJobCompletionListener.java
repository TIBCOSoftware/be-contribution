package com.tibco.cep.pattern.impl.util;

/*
* Author: Ashwin Jayaprakash Date: Sep 1, 2009 Time: 6:02:17 PM
*/
public interface AsyncJobCompletionListener<T> {
    void onSuccess(AsyncJobMember<T> member, T t);

    void onFailure(AsyncJobMember<T> member, Exception exception);
}
