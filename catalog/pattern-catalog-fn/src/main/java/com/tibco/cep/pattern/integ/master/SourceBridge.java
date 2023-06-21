package com.tibco.cep.pattern.integ.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash Date: Aug 31, 2009 Time: 3:52:02 PM
*/
public interface SourceBridge<T> extends Source {
    EventDescriptor<T> getEventDescriptor();

    String getAlias();

    Id getParentId();
}
