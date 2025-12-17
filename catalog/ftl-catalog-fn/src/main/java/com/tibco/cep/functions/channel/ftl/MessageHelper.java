package com.tibco.cep.functions.channel.ftl;

import com.tibco.ftl.*;
import static com.tibco.be.model.functions.FunctionDomain.*;
import com.tibco.be.model.functions.Enabled;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@com.tibco.be.model.functions.BEPackage(
        catalog = "Communication",
        category = "FTL.Message",
        synopsis = "Message Functions",
        enabled = @com.tibco.be.model.functions.Enabled(value=true)
        )
public class MessageHelper {

    static ConcurrentMap<String, MessageFieldRef> fieldRefTable = new ConcurrentHashMap<String, MessageFieldRef>();

    @com.tibco.be.model.functions.BEFunction(
            name = "isFieldSet",
            signature = "boolean isFieldSet (Object message, String fieldname)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name ")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "boolean", desc = ""),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Determine whether a field is set in a message.If it is not set, then getting the field value throws an exception.",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static boolean isFieldSet (Object message, String fieldname) {
        Message msg = Message.class.cast(message);
        if (msg == null) return false;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.isFieldSet(fieldname) : msg.isFieldSet(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "getString",
            signature = "String getString (Object message, String fieldname)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "String", desc = ""),
            version = "6.3",
            see = "Get the value of a string field from a message.The string is valid only for the lifetime of the message. The string is part of the message object; the program must neither modify nor free it.",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static String getString (Object message, String fieldname) {
        Message msg = Message.class.cast(message);
        if (msg == null) return null;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.getString(fieldname) : msg.getString(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getFieldType",
        signature = "int getFieldType (Object message, String fieldname)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name ")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "int", desc = ""),
        version = "6.3",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Get the type of a field within the message.",
        cautions = "none",
        fndomain={ACTION},
        example = ""
    )
    public static int getFieldType (Object message, String fieldname) {
        Message msg = Message.class.cast(message);
        if (msg == null) return -1;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.getFieldType(fieldname) : msg.getFieldType(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "getOpaque",
            signature = "Object getOpaque (Object message, String fieldname)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object "),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name ")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "Object", desc = "opaque object"),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Get the value of an opaque field from a message.This method copies the bytes from the message.",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static Object getOpaque (Object message, String fieldname) {

        Message msg = Message.class.cast(message);
        if (msg == null) return Double.NaN;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.getOpaque(fieldname) : msg.getOpaque(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "getMessage",
            signature = "com.tibco.ftl.Message getMessage (Object message, String fieldname)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name ")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "Object", desc = "message object"),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Get the value of a message field from a message.This call deserializes the sub-message value, caches the result with the message object, and returns that cached sub-message. The sub-message is valid only for the lifetime of the parent message. Your program must not modify nor destroy the sub-message.Calling this method repeatedly returns the same cached sub-message; it does not repeat the deserialization.",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static Object getMessage (Object message, String fieldname) {

        Message msg = Message.class.cast(message);
        if (msg == null) return Double.NaN;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.getMessage(fieldname) : msg.getMessage(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getDouble",
        signature = "double getDouble (Object message, String fieldname)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object "),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name ")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "double", desc = ""),
        version = "6.3",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Get the value of a double floating-point field from a message.",
        cautions = "none",
        fndomain={ACTION},
        example = ""
    )
    public static double getDouble (Object message, String fieldname) {
        Message msg = Message.class.cast(message);
        if (msg == null) return Double.NaN;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.getDouble(fieldname) : msg.getDouble(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "getLong",
            signature = "long getLong (Object message, String fieldname)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object "),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name ")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "long", desc = ""),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Get the value of a long integer field from a message.",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static long getLong (Object message, String fieldname) {
        Message msg = Message.class.cast(message);
        if (msg == null) throw new RuntimeException("message is null");

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
            return fieldRef == null ? msg.getLong(fieldname) : msg.getLong(fieldRef);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "clearField",
        signature = "void clearField (Object message, String fieldName)",
        params = {
        @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object "),
        @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldName", type = "String", desc = "field name ")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "6.3",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Clear a field in a mutable message. Clearing a field clears the data from a field in the message object, and flags the field so a subsequent send call does not transmit it.",
        cautions = "none",
        fndomain={ACTION},
        example = ""
    )
    public static void clearField (Object message, String fieldname) {
        Message msg = Message.class.cast(message);
        if (msg == null) return;
        msg.clearField(fieldname);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "clearAllFields",
        signature = "void clearAllFields (Object message)",
        params = {
        @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message Object")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "6.3",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Clear all fields in a mutable message. After clearing all fields, you can re-use the message. The message format does not change. This call is more efficient than creating a new empty message of the same format.",
        cautions = "none",
        fndomain={ACTION},
        example = ""
    )
    public static void clearAllFields (Object message) {
        Message msg = Message.class.cast(message);
        if (msg == null) return;
        msg.clearAllFields();
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "setString",
            signature = "void setString (Object message, String fieldname, String value)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "String", desc = "value String")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Set a string field in a mutable message.This method copies the string value into the message.",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static void setString (Object message, String fieldname, String value) {
        Message msg = Message.class.cast(message);
        if (msg == null) return ;
        if (value == null) return;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
			if (fieldRef == null) {
				msg.setString(fieldname, value);
			} else {
				msg.setString(fieldRef, value);
			}
		}
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "setMessage",
            signature = "void setMessage (Object message, String fieldname, Object  messageValue)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "field name"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "messageValue", type = "Object", desc = "message value object")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Set a sub-message field in a mutable message.This call copies the sub-message data into the enclosing message field, but does not create a new Java message object. Programs may safely destroy the msg argument after this call returns.Do not set a message as a sub-message of itself (at any level of nesting).",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static void setMessage (Object message, String fieldname, Object value) {
        Message msg = Message.class.cast(message);
        if (msg == null) return ;
        Message msgVal = Message.class.cast(value);
        if (msgVal == null)  return;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
			if (fieldRef == null) {
				msg.setMessage(fieldname, msgVal);
			} else {
				msg.setMessage(fieldRef, msgVal);
			}
		}
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
            name = "setOpaque",
            signature = "void setOpaque (Object message, String fieldname, Object value)",
            params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "fieldname", type = "String", desc = "fieldname"),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "Object", desc = "byte[] object ")

            },
            freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
            version = "6.3",
            see = "",
            mapper = @com.tibco.be.model.functions.BEMapper(),
            description = "Set an opaque (byte-array) field in a mutable message.This method copies the entire byte-array into the field.",
            cautions = "none",
            fndomain={ACTION},
            example = ""
    )
    public static void setOpaque (Object message, String fieldname, Object value) {
        Message msg = Message.class.cast(message);
        if (msg == null) return ;
        byte[] msgVal = byte[].class.cast(value);
        if (msgVal == null)  return;

        MessageFieldRef fieldRef = fieldRefTable.get(fieldname);

        try {
			if (fieldRef == null) {
				msg.setOpaque(fieldname, msgVal);
			} else {
				msg.setOpaque(fieldRef, msgVal);
			}
		}
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "destroy",
        signature = "void destroy (Object message)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "message", type = "Object", desc = "message object")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "6.3",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Destroy a message object. A program may destroy only mutable messages - that is, those messages that the program creates - for example, using Realm.createMessage of mutableCopy(). Inbound messages in listener callback methods belong to the FTL library, programs must not destroy them. Do not destroy a message if the program needs data that the message owns - for example, a string (from getString), an opaque pointer (from getOpaque), a sub-message (from getMessage), or an inbox (from getInbox). Destroying a message frees all resources associated with it.",
        cautions = "none",
        fndomain={ACTION},
        example = ""
    )
    public static void destroy (Object message) {
        Message msg = Message.class.cast(message);
        if (msg == null) return;

        try {
            msg.destroy();
        } catch (FTLException e) {
            throw new RuntimeException(e);
        }
    }

}
