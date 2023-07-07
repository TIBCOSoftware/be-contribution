package com.tibco.cep.pattern.matcher.impl.trace;

import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.trace.RecordedOccurrence;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 11:00:16 AM
*/
public class DefaultRecordedOccurrence implements RecordedOccurrence {
    protected ExpectedInput expectedInput;

    protected Input input;

    protected DefaultRecordedOccurrence next;

    protected DefaultRecordedOccurrence previous;

    public DefaultRecordedOccurrence(ExpectedInput expectedInput, Input input) {
        this.expectedInput = expectedInput;
        this.input = input;
    }

    public ExpectedInput getExpectedInput() {
        return expectedInput;
    }

    public Input getInput() {
        return input;
    }

    public DefaultRecordedOccurrence getNext() {
        return next;
    }

    public DefaultRecordedOccurrence getPrevious() {
        return previous;
    }

    //-------------

    protected void setNext(DefaultRecordedOccurrence next) {
        this.next = next;
    }

    protected void setPrevious(DefaultRecordedOccurrence previous) {
        this.previous = previous;
    }

    //-------------

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName()
                + "{ExpectedInput: " + expectedInput
                + ", Input: " + input
                + "}}";
    }
}