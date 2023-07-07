package com.tibco.cep.pattern.matcher.response;

import java.util.Collection;

import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jul 16, 2009 Time: 6:57:07 PM
*/
public class DriverNotFound extends UnexpectedOccurrence {
    public DriverNotFound(Input trigger, Node occurredAt, Exception trace,
                          Collection<? extends ExpectedInput> expectations) {
        super(trigger, occurredAt, trace, expectations);
    }

    public DriverNotFound(Input trigger, Node occurredAt, Exception trace,
                          ExpectedInput... expectations) {
        super(trigger, occurredAt, trace, expectations);
    }
}
