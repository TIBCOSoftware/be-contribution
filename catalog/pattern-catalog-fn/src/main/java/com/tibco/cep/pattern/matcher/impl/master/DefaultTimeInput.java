package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.TimeInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;

/*
* Author: Ashwin Jayaprakash Date: Jul 16, 2009 Time: 2:54:39 PM
*/
public class DefaultTimeInput implements TimeInput {
    protected ExpectedTimeInput expectedTimeInput;

    public DefaultTimeInput(ExpectedTimeInput expectedTimeInput) {
        this.expectedTimeInput = expectedTimeInput;
    }

    public Id getDriverInstanceId() {
        return expectedTimeInput.getDriverInstanceId();
    }

    public Id getKey() {
        return expectedTimeInput.getUniqueId();
    }

    //-----------

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + "{ExpectedTimeInput:" + expectedTimeInput + "}}";
    }
}
