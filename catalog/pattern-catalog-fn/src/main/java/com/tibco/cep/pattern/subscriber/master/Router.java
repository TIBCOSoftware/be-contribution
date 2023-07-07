package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;
import java.util.concurrent.Future;

import com.tibco.cep.pattern.subscriber.exception.RoutingException;
import com.tibco.cep.service.Service;

/*
* Author: Ashwin Jayaprakash Date: Aug 19, 2009 Time: 11:50:40 AM
*/
public interface Router extends Service {
    /**
     * Asynchronous call.
     *
     * @param source
     * @param message
     * @throws RoutingException
     */
    <T> void routeMessage(EventSource<T> source, T message) throws RoutingException;

    /**
     * Asynchronous call.
     *
     * @param source
     * @param message
     * @param routingJobs The collection into which all routing jobs will be added for tracking.
     * @throws RoutingException
     */
    <T> void routeMessage(EventSource<T> source, T message,
                          Collection<? super Future<?>> routingJobs) throws RoutingException;
}
