package com.tibco.cep.pattern.matcher.response;

import java.util.Collection;

import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:27:08 PM
*/
public class TimeOut extends UnexpectedOccurrence {
    public TimeOut(Input trigger, Node occurredAt, Exception trace,
                   Collection<? extends ExpectedInput> expectations) {
        super(trigger, occurredAt, trace, expectations);
    }

    public TimeOut(Input trigger, Node occurredAt, Exception trace,
                   ExpectedInput... expectations) {
        super(trigger, occurredAt, trace, expectations);
    }
}
