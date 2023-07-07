package com.tibco.cep.pattern.matcher.master;

import java.io.Serializable;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 1:22:03 PM
*/
public interface Input extends Serializable {
    /**
     * Some unique identifier.
     *
     * @return
     */
    Object getKey();
}
