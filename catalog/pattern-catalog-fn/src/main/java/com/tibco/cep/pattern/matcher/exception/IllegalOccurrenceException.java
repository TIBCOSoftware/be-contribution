package com.tibco.cep.pattern.matcher.exception;

/*
* Author: Ashwin Jayaprakash Date: Jun 30, 2009 Time: 5:17:14 PM
*/
public class IllegalOccurrenceException extends RuntimeException {
    public IllegalOccurrenceException() {
    }

    public IllegalOccurrenceException(String message) {
        super(message);
    }

    public IllegalOccurrenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOccurrenceException(Throwable cause) {
        super(cause);
    }
}
