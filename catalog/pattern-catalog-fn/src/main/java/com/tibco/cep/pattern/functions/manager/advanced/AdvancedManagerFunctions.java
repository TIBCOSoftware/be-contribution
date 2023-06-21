package com.tibco.cep.pattern.functions.manager.advanced;

import static com.tibco.be.model.functions.FunctionDomain.*;

import com.tibco.cep.kernel.model.entity.Id;
import com.tibco.cep.pattern.integ.master.MessageId;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.TimeInput;
import com.tibco.cep.pattern.matcher.trace.RecordedOccurrence;
import com.tibco.cep.pattern.matcher.trace.Sequence;

import java.util.ArrayList;

/*
* Author: Ashwin Jayaprakash / Date: Dec 15, 2009 / Time: 2:42:31 PM
*/

@com.tibco.be.model.functions.BEPackage(
		catalog = "CEP Pattern",
        category = "Pattern.Manager.Advanced",
        synopsis = "Advanced pattern management functions")
public abstract class AdvancedManagerFunctions {
    @com.tibco.be.model.functions.BEFunction(
        name = "getEventIds",
        signature = "Object[] getEventIds(Object opaque)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "advancedOpaque", type = "Object", desc = "Service when it invoked the callback RuleFunction.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "Object[]", desc = "An empty array if there was nothing."),
        version = "5.2",
        see = "void setCompletionListener(Object patternInstance, String ruleFunctionURI)",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns an array of Event Ids that have been observed by the pattern instance so far - i.e that ones\nthat did not cause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter\nprovided by the Pattern Service when it invokes the listener rule function. An empty array is returned if there\nwere no such Event Ids.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static Object[] getEventIds(Object advancedOpaque) {
        Driver driver = (Driver) advancedOpaque;

        ArrayList<Object> idList = new ArrayList<Object>(8);
        Sequence sequence = driver.getSequence();

        RecordedOccurrence ro = sequence.getFirstRecordedOccurrence();
        while (ro != null) {
            Input input = ro.getInput();

            if (input instanceof TimeInput == false) {
                Object key = input.getKey();
                MessageId messageId = (MessageId) key;

                idList.add((Id.useLegacyID) ? messageId.getId().getLongValue() : messageId.getId());
            }

            ro = ro.getNext();
        }

        Object[] array = new Object[idList.size()];
        int i = 0;
        for (Object id : idList) {
            array[i++] = id;
        }

        return array;
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getRecentEventId",
        signature = "Object getRecentEventId(Object opaque)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "advancedOpaque", type = "Object", desc = "Service when it invoked the callback RuleFunction.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "Object", desc = "-1 if there was nothing."),
        version = "5.2",
        see = "void setCompletionListener(Object patternInstance, String ruleFunctionURI)",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the most Event Id observed by the pattern instance - i.e that most recent one that did not\ncause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter provided by the\nPattern Service when it invokes the listener rule function. $1-1$1 is returned if there was no such Event Id.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static Object getRecentEventId(Object advancedOpaque) {
        Driver driver = (Driver) advancedOpaque;

        Sequence sequence = driver.getSequence();

        RecordedOccurrence ro = sequence.getRecentRecordedOccurrence();
        if (ro != null) {
            Input input = ro.getInput();

            if (input instanceof TimeInput == false) {
                Object key = input.getKey();
                MessageId messageId = (MessageId) key;

                return (Id.useLegacyID ? messageId.getId().getLongValue() : messageId.getId());
            }
        }

        return -1;
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getEventExtIds",
        synopsis = "Returns an array of Event ExtIds that have been observed by the pattern instance so far - i.e that\nones that did not cause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter\nprovided by the Pattern Service when it invokes the listener rule function. An empty array is returned if there\nwere no such Event ExtIds. If the Event did not have an ExtId, then a <code>null</code> will be stored in its\nplace.",
        signature = "String[] getEventExtIds(Object opaque)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "advancedOpaque", type = "Object", desc = "Service when it invoked the callback RuleFunction.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "String[]", desc = "An empty array if there was nothing."),
        version = "5.2",
        see = "void setCompletionListener(Object patternInstance, String ruleFunctionURI)",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns an array of Event ExtIds that have been observed by the pattern instance so far - i.e that\nones that did not cause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter\nprovided by the Pattern Service when it invokes the listener rule function. An empty array is returned if there\nwere no such Event ExtIds. If the Event did not have an ExtId, then a <code>null</code> will be stored in its\nplace.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static String[] getEventExtIds(Object advancedOpaque) {
        Driver driver = (Driver) advancedOpaque;

        ArrayList<String> extIdList = new ArrayList<String>(8);
        Sequence sequence = driver.getSequence();

        RecordedOccurrence ro = sequence.getFirstRecordedOccurrence();
        while (ro != null) {
            Input input = ro.getInput();

            if (input instanceof TimeInput == false) {
                Object key = input.getKey();
                MessageId messageId = (MessageId) key;

                extIdList.add(messageId.getExtId());
            }

            ro = ro.getNext();
        }

        return extIdList.toArray(new String[extIdList.size()]);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getRecentEventExtId",
        synopsis = "Returns the most Event ExtId observed by the pattern instance - i.e that most recent one that did not\ncause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter provided by the\nPattern Service when it invokes the listener rule function. $1<code>null</code>$1 is returned if there was no such\nEvent ExtId or if the Event did not have one.",
        signature = "String getRecentEventExtId(Object opaque)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "advancedOpaque", type = "Object", desc = "Service when it invoked the callback RuleFunction.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "String", desc = "<code>null</code> if there was nothing."),
        version = "5.2",
        see = "void setCompletionListener(Object patternInstance, String ruleFunctionURI)",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the most Event ExtId observed by the pattern instance - i.e that most recent one that did not\ncause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter provided by the\nPattern Service when it invokes the listener rule function. $1<code>null</code>$1 is returned if there was no such\nEvent ExtId or if the Event did not have one.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static String getRecentEventExtId(Object advancedOpaque) {
        Driver driver = (Driver) advancedOpaque;

        Sequence sequence = driver.getSequence();

        RecordedOccurrence ro = sequence.getRecentRecordedOccurrence();
        if (ro != null) {
            Input input = ro.getInput();

            if (input instanceof TimeInput == false) {
                Object key = input.getKey();
                MessageId messageId = (MessageId) key;

                return messageId.getExtId();
            }
        }

        return null;
    }
}
