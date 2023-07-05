package com.tibco.cep.pattern.functions.io;

import static com.tibco.be.model.functions.FunctionDomain.*;
import com.tibco.cep.pattern.integ.impl.master.RuleSessionItems;
import com.tibco.cep.pattern.integ.master.OntologyService;
import com.tibco.cep.pattern.subscriber.exception.RoutingException;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.Router;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleSession;

import java.util.concurrent.RejectedExecutionException;

import static com.tibco.cep.pattern.functions.Helper.assertRSI;
import static com.tibco.cep.pattern.functions.Helper.getCurrentThreadRSI;

/*
* Author: Ashwin Jayaprakash / Date: Dec 15, 2009 / Time: 2:37:06 PM
*/

@com.tibco.be.model.functions.BEPackage(
		catalog = "CEP Pattern",
        category = "Pattern.IO",
        synopsis = "Event input-output functions")
public abstract class IOFunctions {
    @com.tibco.be.model.functions.BEFunction(
        name = "toPattern",
        synopsis = "Sends the event to the Pattern Service which will in turn route it to all interested Pattern\ninstances. Once the instances (if any) have processed this event, the reference to it will be discarded by the\nPattern Service.",
        signature = "void toPattern(SimpleEvent event)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "event", type = "SimpleEvent", desc = "The event that is to be sent to the Pattern Service.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sends the event to the Pattern Service which will in turn route it to all interested Pattern instances. Once the instances (if any) have processed this event, the reference\nto it will be discarded by the Pattern Service.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void toPattern(SimpleEvent event) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        OntologyService ontologyService = rsi.getOntologyService();
        Router router = rsi.getRouter();

        EventSource eventSource = ontologyService.getEventSource(event.getClass().getName());

        try {
            for (long waitMillis = 0; ; ) {
                try {
                    router.routeMessage(eventSource, event);
                    break;
                }
                catch (RejectedExecutionException re) {
                    if (waitMillis >= 120 * 1000) {
                        throw new RuntimeException("Unable to send event to pattern service as it appears to be busy/blocked." +
                                " Tried and waited for [" + waitMillis + "] milliseconds", re);
                    }

                    try {
                        Thread.sleep(10);
                        waitMillis += 10;
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        }
        catch (RoutingException e) {
            throw new RuntimeException(
                    "An error occurred while sending the event to the registered Patterns.", e);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "toDestination",
        synopsis = "Sends the event to its default destination.",
        signature = "void toDestination(SimpleEvent event)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "event", type = "SimpleEvent", desc = "The event that is to be sent to its default Destination.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sends the event to its default destination.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void toDestination(SimpleEvent event) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        try {
            RuleSession rs = rsi.getRuleSession();
            RuleServiceProvider rsp = rs.getRuleServiceProvider();
            ChannelManager cm = rsp.getChannelManager();

            cm.sendEvent(event, event.getDestinationURI(), null);
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "An error occurred while sending the event to the Event's default Destination.",
                    e);
        }
    }
}

