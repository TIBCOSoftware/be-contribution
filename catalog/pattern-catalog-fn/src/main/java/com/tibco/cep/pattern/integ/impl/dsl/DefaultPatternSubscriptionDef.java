package com.tibco.cep.pattern.integ.impl.dsl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.integ.dsl.PatternSubscriptionDef;
import com.tibco.cep.pattern.integ.impl.master.BroadcastingSubscriptionListener;
import com.tibco.cep.pattern.integ.impl.master.DefaultCorrelationIdExtractor;
import com.tibco.cep.pattern.integ.impl.master.DefaultSourceBridge;
import com.tibco.cep.pattern.integ.impl.master.ForwardingSubscriptionListener;
import com.tibco.cep.pattern.integ.master.CorrelationIdExtractor;
import com.tibco.cep.pattern.subscriber.dsl.SubscriptionItemDef;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptorRegistry;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 26, 2009 Time: 6:06:13 PM
*/
public class DefaultPatternSubscriptionDef
        implements PatternSubscriptionDef<DefaultPatternSubscriptionDef> {
    protected Id parentId;

    protected EventDescriptorRegistry eventDescriptorRegistry;

    protected DefaultSubscriptionDef subscription;

    //------------

    protected HashMap<Id, DefaultSourceBridge> sources;

    protected Id inProgressSourceId;

    protected String inProgressSourceAlias;

    public DefaultPatternSubscriptionDef(ResourceProvider resourceProvider,
                                         Id parentId, DefaultSubscriptionDef actualSubscription) {
        this.parentId = parentId;

        this.eventDescriptorRegistry =
                resourceProvider.fetchResource(EventDescriptorRegistry.class);

        this.subscription = actualSubscription;

        this.sources = new HashMap<Id, DefaultSourceBridge>(4);
    }

    public HashMap<Id, DefaultSourceBridge> getSources() {
        return sources;
    }

    public DefaultSubscriptionDef getSubscription() {
        return subscription;
    }

    //------------

    public DefaultPatternSubscriptionDef listenTo(Id sourceId) {
        subscription.listenTo(sourceId);

        inProgressSourceId = sourceId;

        return this;
    }

    public DefaultPatternSubscriptionDef as(String listenerName) {
        subscription.as(listenerName);

        inProgressSourceAlias = listenerName;

        return this;
    }

    private DefaultSourceBridge<Object> makeSourceBridge() {
        DefaultId privateBridgeId = new DefaultId(inProgressSourceId, "Bridged");

        DefaultSourceBridge<Object> sourceBridge = sources.get(privateBridgeId);
        if (sourceBridge != null) {
            throw new IllegalArgumentException("The SourceId [" + inProgressSourceId
                    + "] with the ListenerName [" + inProgressSourceAlias
                    + "] cannot be used as there is another one registered with the same values.");
        }

        //-------------

        EventDescriptor eventDescriptor =
                eventDescriptorRegistry.getEventDescriptor(inProgressSourceId);
        if (eventDescriptor == null) {
            throw new IllegalArgumentException("The SourceId [" + inProgressSourceId
                    + "] cannot be used as there is no valid EventDescriptor registered"
                    + " under this Id.");
        }

        //-------------

        sourceBridge = new DefaultSourceBridge<Object>(parentId, privateBridgeId,
                inProgressSourceAlias, eventDescriptor);

        sources.put(privateBridgeId, sourceBridge);

        inProgressSourceId = null;
        inProgressSourceAlias = null;

        //-------------

        return sourceBridge;
    }

    public DefaultPatternSubscriptionDef use(String fieldName) {
        subscription.use(fieldName);

        DefaultSourceBridge<Object> sourceBridge = makeSourceBridge();

        verifyValidFieldName(fieldName, sourceBridge);

        DefaultCorrelationIdExtractor correlationIdExtractor =
                new DefaultCorrelationIdExtractor(fieldName);

        SubscriptionListener listener =
                new ForwardingSubscriptionListener(sourceBridge, correlationIdExtractor);

        subscription.forwardTo(listener);

        return this;
    }

    private void verifyValidFieldName(String fieldName, DefaultSourceBridge<Object> sourceBridge) {
        String[] validPropertyNames = sourceBridge.getEventDescriptor().getSortedPropertyNames();

        int position = Arrays.binarySearch(validPropertyNames, fieldName);
        if (position < 0) {
            throw new IllegalArgumentException("The FieldName [" + fieldName
                    + "] cannot be used as it is not valid. Valid names are " +
                    Arrays.asList(validPropertyNames) + ".");
        }
    }

    public DefaultPatternSubscriptionDef whereMatches(String fieldName, Comparable fieldValue) {
        subscription.whereMatches(fieldName, fieldValue);

        DefaultSourceBridge<Object> sourceBridge = makeSourceBridge();

        verifyValidFieldName(fieldName, sourceBridge);

        SubscriptionListener listener = new BroadcastingSubscriptionListener(sourceBridge);

        subscription.forwardTo(listener);

        return this;
    }

    public DefaultPatternSubscriptionDef whereMatches(String fieldName, Comparable fieldValue,
                                                      CorrelationIdExtractor<Object> correlationIdExtractor) {
        subscription.whereMatches(fieldName, fieldValue);

        DefaultSourceBridge<Object> sourceBridge = makeSourceBridge();

        verifyValidFieldName(fieldName, sourceBridge);

        SubscriptionListener listener =
                new ForwardingSubscriptionListener(sourceBridge, correlationIdExtractor);

        subscription.forwardTo(listener);

        return this;
    }

    public DefaultPatternSubscriptionDef alsoListenTo(Id sourceId) {
        subscription.alsoListenTo(sourceId);

        inProgressSourceId = sourceId;

        return this;
    }

    public List<? extends SubscriptionItemDef> getSubscriptionItems() {
        return subscription.getSubscriptionItems();
    }
}
