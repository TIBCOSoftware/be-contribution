package com.tibco.cep.pattern.impl.util;

import java.util.concurrent.Callable;

/*
* Author: Ashwin Jayaprakash Date: Sep 1, 2009 Time: 6:00:46 PM
*/
public abstract class AsyncJobMember<T> implements Callable<T> {
    protected AsyncJobCompletionListener<T> optionalCompletionListener;

    protected AsyncJobMember() {
    }

    protected AsyncJobMember(AsyncJobCompletionListener<T> optionalCompletionListener) {
        this.optionalCompletionListener = optionalCompletionListener;
    }

    public AsyncJobCompletionListener<T> getOptionalCompletionListener() {
        return optionalCompletionListener;
    }

    /**
     * Set before {@link #call()}.
     *
     * @param optionalCompletionListener
     */
    public void setOptionalCompletionListener(
            AsyncJobCompletionListener<T> optionalCompletionListener) {
        this.optionalCompletionListener = optionalCompletionListener;
    }

    public final T call() throws Exception {
        try {
            T t = performJob();

            if (optionalCompletionListener != null) {
                optionalCompletionListener.onSuccess(this, t);
            }

            return t;
        }
        catch (Throwable throwable) {
            Exception e = new Exception(throwable);

            if (optionalCompletionListener != null) {
                optionalCompletionListener.onFailure(this, e);
            }

            throw e;
        }
    }

    /**
     * @return <code>null</code> to simulate <code>void</code> return type.
     * @throws Exception
     */
    protected abstract T performJob() throws Exception;

    protected void cleanup() {
        optionalCompletionListener = null;
    }
}
