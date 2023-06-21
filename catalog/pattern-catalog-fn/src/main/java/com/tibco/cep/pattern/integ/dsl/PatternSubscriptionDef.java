package com.tibco.cep.pattern.integ.dsl;

import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.integ.master.CorrelationIdExtractor;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionItemDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash Date: Aug 12, 2009 Time: 5:25:10 PM
*/
public interface PatternSubscriptionDef<S extends PatternSubscriptionDef> {
    /**
     * Start a new definition with this and end with {@link #use(String)} or {@link
     * #whereMatches(String, Comparable)}.
     *
     * @param sourceId Same as {@link EventDescriptor#getResourceId()}.
     * @return
     */
    S listenTo(Id sourceId);

    S as(String listenerName);

    /**
     * Specifies the field whose value will be used to generate the {@link Id correlation-id} to
     * identify the {@link Driver pattern instance}.
     * <p/>
     * Use either this method or {@link #whereMatches(String, Comparable)} but not both.
     *
     * @param fieldName
     * @return
     */
    S use(String fieldName);

    /**
     * Unlike the {@link #use(String)} method, this specifies the exact match criteria to be used to
     * subscribe to events.
     * <p/>
     * Since it is not specific to any correlation-id, all <b>existing</b> {@link Driver instances}
     * will be notified.
     * <p/>
     * Use either this method or {@link #use(String)} but not both.
     *
     * @param fieldName
     * @param fieldValue
     * @return
     */
    S whereMatches(String fieldName, Comparable fieldValue);

    /**
     * Similar to {@link #whereMatches(String, Comparable)} and similar usage patterns are to be
     * followed for this method.
     * <p/>
     * The only difference being that an explicit correlation-id extractor will be used to identify
     * the instance to which this message will be sent. i.e this method is a cross between {@link
     * #use(String)} and {@link #whereMatches(String, Comparable)}.
     *
     * @param fieldName
     * @param fieldValue
     * @param correlationIdExtractor
     * @return
     */
    S whereMatches(String fieldName, Comparable fieldValue,
                   CorrelationIdExtractor<Object> correlationIdExtractor);

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
