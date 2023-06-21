package com.tibco.cep.pattern.matcher.impl.trace;

import java.util.LinkedList;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.trace.Group;
import com.tibco.cep.pattern.matcher.trace.SequenceMember;

/*
* Author: Ashwin Jayaprakash Date: Jul 8, 2009 Time: 2:08:21 PM
*/
public class DefaultGroup implements Group {
    protected Group parent;

    protected Id groupId;

    protected LinkedList<SequenceMember> members;

    protected boolean completed;

    public DefaultGroup() {
        this.members = new LinkedList<SequenceMember>();
    }

    public Group getParent() {
        return parent;
    }

    public Id getGroupId() {
        return groupId;
    }

    public LinkedList<SequenceMember> getMembers() {
        return members;
    }

    public boolean hasCompleted() {
        return completed;
    }

    //--------------

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public void setGroupId(Id groupId) {
        this.groupId = groupId;
    }

    public void addMember(SequenceMember member) {
        members.add(member);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //--------------

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + "{GroupId: " + groupId + "}}";
    }
}
