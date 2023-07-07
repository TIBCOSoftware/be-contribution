package com.tibco.cep.pattern.matcher.impl.util;

import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.trace.Group;
import com.tibco.cep.pattern.matcher.trace.SequenceMember;
import com.tibco.cep.pattern.matcher.trace.SequenceView;

/*
* Author: Ashwin Jayaprakash Date: Jul 31, 2009 Time: 11:43:37 AM
*/
public class SequenceFormatter {
    public static StringBuilder print(Id sequenceId, SequenceView sequenceView) {
        StringBuilder builder = new StringBuilder();

        builder.append("Sequence: ");
        builder.append(sequenceId);
        builder.append('\n');

        builder.append("Completed: ");
        builder.append(sequenceView.hasCompleted() ? "Yes\n" : "No\n");

        Group group = sequenceView.getSuperGroup();
        if (group != null) {
            print(group, builder, 1);
        }

        builder.append('\n');

        return builder;
    }

    protected static void print(Group group, StringBuilder builder, int level) {
        for (int i = 0; i < level; i++) {
            builder.append(' ');
        }
        builder.append(group.getGroupId());
        builder.append('\n');

        for (int i = 0; i < level; i++) {
            builder.append(' ');
        }
        builder.append('{');
        builder.append('\n');

        //--------------

        List<? extends SequenceMember> members = group.getMembers();
        for (SequenceMember member : members) {
            if (member instanceof Group) {
                print((Group) member, builder, level + 1);
            }
            else {
                for (int i = 0; i <= level; i++) {
                    builder.append(' ');
                }
                builder.append(member);
                builder.append('\n');
            }
        }

        //--------------

        for (int i = 0; i < level; i++) {
            builder.append(' ');
        }
        builder.append('}');
        builder.append('\n');
    }
}
