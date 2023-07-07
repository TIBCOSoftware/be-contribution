package com.tibco.cep.pattern.impl.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* Author: Ashwin Jayaprakash Date: Aug 20, 2009 Time: 12:59:44 PM
*/

/**
 * Used by {@link ReflectionEventDescriptor} to annotate methods that will be used in
 * subscriptions.
 * <p/>
 * The annotated method should not accept any parameters but have an {@link Object} return type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventUniqueId {
}