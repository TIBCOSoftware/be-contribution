package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Aug 6, 2009 Time: 10:59:26 AM
*/

/**
 * This pair (this and {@link DynamicGroupMemberEnd}) are free floating nodes. They do not connect
 * to any other nodes.
 */
public interface DynamicGroupMemberStart<C extends Context, E extends ExpectedInput, I extends Input>
        extends DynamicGroupMemberBoundary<C, E, I> {
}
