package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.response.Response;

/*
* Author: Ashwin Jayaprakash Date: Jul 27, 2009 Time: 3:25:42 PM
*/
public interface DriverCallback extends Recoverable<DriverCallback> {
    void start(ResourceProvider resourceProvider, DriverView driverView);

    /**
     * Method must return quickly.
     *
     * @param source
     * @param input
     * @param response
     */
    void afterInput(Source source, Input input, Response response);

    /**
     * Method must return quickly.
     *
     * @param input
     * @param response
     */
    void afterTimeOut(TimeInput input, Response response);

    void stop();
}
