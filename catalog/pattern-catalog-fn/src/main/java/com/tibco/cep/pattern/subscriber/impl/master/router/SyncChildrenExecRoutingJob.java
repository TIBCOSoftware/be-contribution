package com.tibco.cep.pattern.subscriber.impl.master.router;

import java.util.LinkedList;
import java.util.concurrent.Future;

import com.tibco.cep.pattern.impl.util.Helper;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.SubscriptionListener;

/*
* Author: Ashwin Jayaprakash Date: Aug 21, 2009 Time: 11:06:48 AM
*/

/**
 * All child jobs will be executed synchronously.
 */
public class SyncChildrenExecRoutingJob extends DefaultRoutingJob {
    /**
     * @param source
     * @param messageId
     * @param message
     * @param listener
     */
    public SyncChildrenExecRoutingJob(EventSource source, Object messageId, Object message,
                                      SubscriptionListener listener) {
        super(source, messageId, message, listener);
    }

    /**
     * @return {@link #getMessage() message}.
     * @throws Exception
     */
    public Object call() throws Exception {
        LinkedList<Future> list = new LinkedList<Future>();

        listener.onMessage(messageId, message, list);

        Helper.$waitForAll(list);

        return message;
    }
}