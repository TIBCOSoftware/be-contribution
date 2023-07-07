package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.exception.CopyException;
import com.tibco.cep.pattern.matcher.model.Start;

/*
* Author: Ashwin Jayaprakash Date: Jul 7, 2009 Time: 2:08:23 PM
*/
public interface Plan extends Recoverable<Plan> {
    Source[] getSources();

    TimeSource getTimeSource();

    EndSource getEndSource();

    Start createNewFlow(ResourceProvider resourceProvider) throws CopyException;
}
