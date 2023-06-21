package com.tibco.cep.pattern.subscriber.impl.master.router;

import java.util.concurrent.Callable;

import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 21, 2009 Time: 11:06:48 AM
*/
public class DefaultRoutingJob implements Callable<Object> {
    protected EventSource source;

    protected Object messageId;

    protected Object message;

    protected SubscriptionListener listener;

    /**
     * @param source
     * @param messageId
     * @param message
     * @param listener
     */
    public DefaultRoutingJob(EventSource source, Object messageId, Object message,
                             SubscriptionListener listener) {
        this.source = source;
        this.messageId = messageId;
        this.message = message;
        this.listener = listener;
    }

    public EventSource getSource() {
        return source;
    }

    public Object getMessageId() {
        return messageId;
    }

    public Object getMessage() {
        return message;
    }

    public SubscriptionListener getListener() {
        return listener;
    }

    /**
     * @return {@link #getMessage() message}.
     * @throws Exception
     */
    public Object call() throws Exception {
        listener.onMessage(messageId, message);

        return message;
    }
}
