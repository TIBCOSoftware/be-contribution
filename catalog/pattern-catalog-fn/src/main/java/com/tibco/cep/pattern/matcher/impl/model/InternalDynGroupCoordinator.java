package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.DynamicGroupMemberOwner;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash / Date: Nov 20, 2009 / Time: 2:23:51 PM
*/
public interface InternalDynGroupCoordinator extends Node<Context, DefaultExpectedInput, Input> {
    void onGroupMemberCompleted(Context context, DynamicGroupMemberOwner memberOwner);
}
