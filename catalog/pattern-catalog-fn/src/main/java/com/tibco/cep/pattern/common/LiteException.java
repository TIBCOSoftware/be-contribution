package com.tibco.cep.pattern.common;

/*
* Author: Ashwin Jayaprakash / Date: Nov 3, 2009 / Time: 2:10:53 PM
*/

/**
 * Does not produce the {@link #fillInStackTrace() StackTrace} by default.
 */
public class LiteException extends Exception {
    public LiteException() {
    }

    public LiteException(String message) {
        super(message);
    }

    public LiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiteException(Throwable cause) {
        super(cause);
    }

    /**
     * No-op.
     *
     * @return
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
