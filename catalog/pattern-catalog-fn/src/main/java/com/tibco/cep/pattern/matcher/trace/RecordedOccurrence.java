package com.tibco.cep.pattern.matcher.trace;

import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 11:00:16 AM
*/

/**
 * Simple doubly linked sequence of input occurrences from the beginning to the end.
 */
public interface RecordedOccurrence extends SequenceMember {
    ExpectedInput getExpectedInput();

    Input getInput();

    /**
     * @return Can be <code>null</code>.
     */
    RecordedOccurrence getPrevious();

    /**
     * @return Can be <code>null</code>.
     */
    RecordedOccurrence getNext();
}
