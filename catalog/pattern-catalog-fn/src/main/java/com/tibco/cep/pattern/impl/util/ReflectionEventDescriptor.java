package com.tibco.cep.pattern.impl.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash Date: Aug 18, 2009 Time: 6:18:16 PM
*/

/**
 * Relies on methods annotated by {@link EventProperty}.
 * <p/>
 * {@link #equals(Object)} only on {@link #getResourceId()}.
 */
public class ReflectionEventDescriptor<T> implements EventDescriptor<T> {
    protected transient HashMap<String, Method> propertyGetters;

    protected transient Method uniqueIdGetter;

    protected Class<T> type;

    protected Id id;

    private static final Object[] EMPTY_VARARGS = {};

    /**
     * @param type The self-describing class from which the {@link #getSortedPropertyNames()} are
     *             obtained from JavaBean style Getter methods annotated with {@link EventProperty}
     *             and the names are obtained from {@link EventProperty#value()}.
     *             <p/>
     *             The return type of the Getters should implement {@link Comparable}.
     * @param id
     * @throws IllegalArgumentException If the above conditions are not met.
     */
    public ReflectionEventDescriptor(Class<T> type, Id id) {
        this.propertyGetters = collectPropertyGetters(type);
        this.uniqueIdGetter = collectUniqueIdGetter(type);
        this.type = type;
        this.id = id;
    }

    private static HashMap<String, Method> collectPropertyGetters(Class type) {
        HashMap<String, Method> annotatedGetters = new HashMap<String, Method>(2);

        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(EventProperty.class) == false) {
                continue;
            }

            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length > 0) {
                throw new IllegalArgumentException(
                        "The annotated method is invalid as it accepts parameters: " + method);
            }

            Class returnType = method.getReturnType();
            if (Comparable.class.isAssignableFrom(returnType) == false
                    && returnType.isPrimitive() == false) {
                throw new IllegalArgumentException(
                        "The annotated method is invalid due to its return type," +
                                " which neither implements [" + Comparable.class.getName() +
                                "] nor is it a Java Primitive : " + method);
            }

            //-------------

            EventProperty annotation = method.getAnnotation(EventProperty.class);
            Method existingMethod = annotatedGetters.put(annotation.value(), method);
            if (existingMethod != null) {
                throw new IllegalArgumentException(
                        "The annotated method is invalid as there is another method" +
                                " annotated using the same name [" + annotation.value() + "]: " +
                                method);
            }
        }

        if (annotatedGetters.isEmpty()) {
            throw new IllegalArgumentException(
                    "The type is invalid as it does not contain any JavaBean-style Getter methods" +
                            " annotated with [" + EventProperty.class.getName() + "]: " +
                            type.getName());
        }

        return annotatedGetters;
    }

    private static Method collectUniqueIdGetter(Class type) {
        Method annotatedGetter = null;

        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(EventUniqueId.class) == false) {
                continue;
            }

            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length > 0) {
                throw new IllegalArgumentException(
                        "The annotated method is invalid as it accepts parameters: " + method);
            }

            Class returnType = method.getReturnType();
            if (Void.TYPE.equals(returnType)) {
                throw new IllegalArgumentException(
                        "The annotated method is invalid as its return type is [" +
                                Void.TYPE.getName() + "]: " + method);
            }

            //-------------

            if (annotatedGetter != null) {
                throw new IllegalArgumentException(
                        "The type is invalid as it has more than one JavaBean-style Getter method" +
                                " annotated with [" + EventUniqueId.class.getName() + "]: " +
                                type.getName());
            }

            annotatedGetter = method;
        }

        if (annotatedGetter == null) {
            throw new IllegalArgumentException(
                    "The type is invalid as it does not contain any JavaBean-style Getter method" +
                            " annotated with [" + EventUniqueId.class.getName() + "]: " +
                            type.getName());
        }

        return annotatedGetter;
    }

    public Class<T> getType() {
        return type;
    }

    public Id getResourceId() {
        return id;
    }

    public String[] getSortedPropertyNames() {
        String[] array = propertyGetters.keySet().toArray(new String[propertyGetters.size()]);

        Arrays.sort(array);

        return array;
    }

    @Override
    public Class getPropertyType(String propertyName) {
        Method getter = propertyGetters.get(propertyName);
        if (getter == null) {
            throw new IllegalArgumentException("The property name [" + propertyName +
                    "] is invalid. Valid names: " + propertyGetters.keySet());
        }

        return getter.getReturnType();
    }

    public Comparable extractPropertyValue(T source, String propertyName) {
        Method getter = propertyGetters.get(propertyName);
        if (getter == null) {
            throw new IllegalArgumentException("The property name [" + propertyName +
                    "] is invalid. Valid names: " + propertyGetters.keySet());
        }

        try {
            return (Comparable) getter.invoke(source, EMPTY_VARARGS);
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Object extractUniqueId(T source) {
        try {
            return uniqueIdGetter.invoke(source, EMPTY_VARARGS);
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    //-------------

    public ReflectionEventDescriptor<T> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        propertyGetters = collectPropertyGetters(type);

        return this;
    }

    //-------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReflectionEventDescriptor)) {
            return false;
        }

        ReflectionEventDescriptor that = (ReflectionEventDescriptor) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
