package com.tibco.cep.pattern.matcher.exception;

/*
* Author: Ashwin Jayaprakash Date: Jul 9, 2009 Time: 5:25:22 PM
*/
public class CopyException extends Exception {
    public CopyException() {
    }

    public CopyException(String message) {
        super(message);
    }

    public CopyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CopyException(Throwable cause) {
        super(cause);
    }
}
