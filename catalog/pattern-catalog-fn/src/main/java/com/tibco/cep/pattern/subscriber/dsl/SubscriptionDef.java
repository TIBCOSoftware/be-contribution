package com.tibco.cep.pattern.subscriber.dsl;

import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 12, 2009 Time: 5:25:10 PM
*/
public interface SubscriptionDef<S extends SubscriptionDef> {
    /**
     * Start a new definition with this and end with {@link #forwardTo(SubscriptionListener)}.
     *
     * @param sourceId Same as {@link EventDescriptor#getResourceId()}.
     * @return
     */
    S listenTo(Id sourceId);

    S as(String listenerName);

    /**
     * Use either this method or {@link #whereMatches(String, Comparable)} but not both.
     *
     * @param fieldName
     * @return
     */
    S use(String fieldName);

    /**
     * Use either this method or {@link #use(String)} but not both.
     *
     * @param fieldName
     * @param fieldValue
     * @return
     */
    S whereMatches(String fieldName, Comparable fieldValue);

    /**
     * End the definition with this.
     *
     * @param listener
     * @return
     */
    S forwardTo(SubscriptionListener listener);

    /**
     * Start a new definition like {@link #listenTo(Id)}.
     *
     * @param sourceId
     * @return
     */
    S alsoListenTo(Id sourceId);

    //-------------

    List<? extends SubscriptionItemDef> getSubscriptionItems();
}
