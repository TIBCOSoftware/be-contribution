package com.tibco.cep.pattern.matcher.trace;

import com.tibco.cep.common.resource.Recoverable;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 10:59:19 AM
*/
public interface Sequence extends SequenceView, SequenceRecorder, Recoverable<Sequence> {
}
