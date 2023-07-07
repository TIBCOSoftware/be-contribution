package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 1:31:10 PM
*/
public interface TimeInput extends Input {
    /**
     * The correlation Id.
     *
     * @return
     */
    Id getKey();

    /**
     * The driver's instance Id. Not the same as {@link #getKey()} or the driver's correlation Id.
     *
     * @return
     */
    Id getDriverInstanceId();
}
