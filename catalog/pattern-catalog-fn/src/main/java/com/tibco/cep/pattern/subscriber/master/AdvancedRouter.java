package com.tibco.cep.pattern.subscriber.master;

/*
* Author: Ashwin Jayaprakash Date: Aug 27, 2009 Time: 6:41:48 PM
*/
public interface AdvancedRouter<R extends RoutingTable<EventSource, S>, S extends SubscriptionRecord>
        extends Router {
    R getRoutingTable();
}
