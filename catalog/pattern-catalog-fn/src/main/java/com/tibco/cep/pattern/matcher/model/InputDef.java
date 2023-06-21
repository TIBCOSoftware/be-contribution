package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.matcher.master.Source;

/*
* Author: Ashwin Jayaprakash Date: Jun 24, 2009 Time: 11:37:17 AM
*/
public interface InputDef extends Recoverable<InputDef> {
    Source getSource();
}
