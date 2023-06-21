package com.tibco.cep.pattern.subscriber.exception;

/*
* Author: Ashwin Jayaprakash Date: Aug 20, 2009 Time: 5:02:53 PM
*/
public class RoutingException extends Exception {
    public RoutingException() {
    }

    public RoutingException(String message) {
        super(message);
    }

    public RoutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoutingException(Throwable cause) {
        super(cause);
    }
}
