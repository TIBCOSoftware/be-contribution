package com.tibco.cep.pattern.matcher.model;

import java.util.List;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash / Date: Nov 12, 2009 / Time: 11:10:45 AM
*/
public interface GroupBoundaryStart<C extends Context, E extends ExpectedInput, I extends Input>
        extends GroupBoundary<C, E, I> {
    /**
     * Calls this method recursively on all {@link #getChildGroups() children}.
     *
     * @param context
     */
    void reset(C context);

    GroupBoundaryEnd<C, E, I> getGroupEnd();

    @Optional
    List<? extends GroupBoundaryStart<C, E, I>> getChildGroups();
}
