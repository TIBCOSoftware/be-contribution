package com.tibco.cep.pattern.matcher.response;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:14:30 PM
*/
public interface Success extends Response {
    /**
     * Over a period of time several driver instances can have the same correlation Id. But never at
     * the same time.
     *
     * @return
     */
    Id getCorrelationId();

    /**
     * Uniquely identifies this driver instance unlike {@link #getCorrelationId()}.
     *
     * @return
     */
    Id getInstanceId();
}
