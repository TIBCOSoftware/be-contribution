package com.tibco.cep.pattern.matcher.trace;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 10:59:19 AM
*/
public interface SequenceRecorder {
    void recordGroupStart(Id groupId);

    void recordOccurrence(ExpectedInput expectedInput, Input input);

    void recordGroupEnd(Id groupId);

    void markCompleted();
}