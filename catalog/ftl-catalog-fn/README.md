# FTL Store Catalog Functions

A set of catalog functions supporting:

* [Message](#Message)



## Note

This document only list the functions that are specific to FTL existing functions.

## Message

The Message catalog functions allow you to perform the operation on FTL message through the following operations:
* Check field is set or not 
  * [FTL.Message.isFieldSet](#isFieldSet)
* Get the field type
  * [FTL.Message.getFieldType](#getFieldType)
* Get String
  * [FTL.Message.getString](#getString)
* Get opaque value
  * [FTL.Message.getOpaque](#getOpaque)
* Get ftl message
  * [FTL.Message.getMessage](#getMessage)
* Get value of double
  * [FTL.Message.getDouble](#getDouble)
* Get value of Long
  * [FTL.Message.getLong](#getLong)
* Clear field
  * [FTL.Message.clearField](#clearField)
* Clear all fields
  * [FTL.Message.clearAllFields](#clearAllFields)
* Sets string
  * [FTL.Message.setString](#setString)
* Sets ftl message
  * [FTL.Message.setMessage](#setMessage)
* Sets opaque value
  * [FTL.Message.setOpaque](#setOpaque)
* Destroy message
  * [FTL.Message.destroy](#destroy)



# FTL.Message.isFieldSet
Purpose: Determine whether a field is set in a message.If it is not set, then getting the field value throws an exception.

Function Signature:

```java
public static boolean isFieldSet (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

Boolean Value

# FTL.Message.getString
Purpose: Get the value of a string field from a message.The string is valid only for the lifetime of the message. The string is part of the message object; the program must neither modify nor free it.

Function Signature:

```java
public static String getString (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

String Value

# FTL.Message.getFieldType
Purpose: Get the type of a field within the message.

Function Signature:

```java
public static int getFieldType (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

Integer field type

# FTL.Message.getOpaque
Purpose: Get the value of an opaque field from a message.This method copies the bytes from the message.

Function Signature:

```java
public static Object getOpaque (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

Object

# FTL.Message.getMessage
Purpose: Get the value of a message field from a message.This call deserializes the sub-message value, caches the result with the message object, and returns that cached sub-message. The sub-message is valid only for the lifetime of the parent message. Your program must not modify nor destroy the sub-message.Calling this method repeatedly returns the same cached sub-message; it does not repeat the deserialization.

Function Signature:

```java
Object getMessage (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name      |

Returns:

Object Value

# FTL.Message.getDouble
Purpose: Get the value of a double floating-point field from a message.

Function Signature:

```java
double getDouble (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

Double Value

# FTL.Message.getLong
Purpose: Get the value of a long integer field from a message.

Function Signature:

```java
long getLong (Object message, String fieldname)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

Long Value

# FTL.Message.clearField
Purpose: Clear a field in a mutable message. Clearing a field clears the data from a field in the message object, and flags the field so a subsequent send call does not transmit it.

Function Signature:

```java
void clearField (Object message, String fieldName)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name       |

Returns:

N/A

# FTL.Message.clearAllFields
Purpose: Clear all fields in a mutable message. After clearing all fields, you can re-use the message. The message format does not change. This call is more efficient than creating a new empty message of the same format.

Function Signature:

```java
void clearAllFields (Object message)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |

Returns:

N/A

# FTL.Message.setString
Purpose: Set a string field in a mutable message.This method copies the string value into the message.

Function Signature:

```java
void setString (Object message, String fieldname, String value)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |
| fieldname   | field name      |
| value       | value String    |

Returns:

N/A

# FTL.Message.setMessage
Purpose: Set a sub-message field in a mutable message.This call copies the sub-message data into the enclosing message field, but does not create a new Java message object. Programs may safely destroy the msg argument after this call returns.Do not set a message as a sub-message of itself (at any level of nesting).

Function Signature:

```java
void setMessage (Object message, String fieldname, Object  messageValue)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object       |
| fieldname   | field name           |
| messageValue| message value object |

Returns:

N/A

# FTL.Message.setOpaque
Purpose: Set an opaque (byte-array) field in a mutable message.This method copies the entire byte-array into the field.

Function Signature:

```java
void setOpaque (Object message, String fieldname, Object value)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object       |
| fieldname   | field name           |
| value       | byte[] object        |

Returns:

N/A

# FTL.Message.destroy
Purpose: Destroy a message object. A program may destroy only mutable messages - that is, those messages that the program creates - for example, using Realm.createMessage of mutableCopy(). Inbound messages in listener callback methods belong to the FTL library, programs must not destroy them. Do not destroy a message if the program needs data that the message owns - for example, a string (from getString), an opaque pointer (from getOpaque), a sub-message (from getMessage), or an inbox (from getInbox). Destroying a message frees all resources associated with it.

Function Signature:

```java
void destroy (Object message)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| message     | message object  |

Returns:

N/A


