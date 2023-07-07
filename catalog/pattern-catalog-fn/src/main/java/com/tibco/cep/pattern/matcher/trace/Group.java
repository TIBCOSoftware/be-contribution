package com.tibco.cep.pattern.matcher.trace;

import java.util.List;

import com.tibco.cep.common.resource.Id;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 10:58:57 AM
*/
public interface Group extends SequenceMember {
    /**
     * @return Can be <code>null</code>.
     */
    Group getParent();

    Id getGroupId();

    /**
     * @return List of sequence-members that occurred inside this group.
     */
    List<? extends SequenceMember> getMembers();

    boolean hasCompleted();
}
