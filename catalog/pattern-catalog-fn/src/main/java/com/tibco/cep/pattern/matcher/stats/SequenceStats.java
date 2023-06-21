package com.tibco.cep.pattern.matcher.stats;

import java.util.Map;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash / Date: Oct 29, 2009 / Time: 1:12:56 PM
*/
public interface SequenceStats {
    Map<Id, ? extends GroupStats> getStats();

    Id getSuperGroupId();
}
