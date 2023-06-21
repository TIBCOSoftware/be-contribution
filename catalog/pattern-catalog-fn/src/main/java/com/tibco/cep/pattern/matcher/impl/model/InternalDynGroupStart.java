package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupEnd;
import com.tibco.cep.pattern.matcher.model.DynamicGroupStart;

/*
* Author: Ashwin Jayaprakash Date: Aug 12, 2009 Time: 11:04:54 AM
*/
public interface InternalDynGroupStart
        extends DynamicGroupStart<Context, DefaultExpectedInput, Input> {
    /**
     * Not idempotent.
     * <p/>
     * This method is called by {@link DynamicGroupEnd} when it receives and forwards an input to
     * its next node. Before forwarding, if this method returns <code>true</code> which means that
     * the group was started and therefore marks the end of the group.
     *
     * @return
     */
    boolean shouldMarkGroupEnd();

    void onGroupMemberCompleted(Context context);
}
