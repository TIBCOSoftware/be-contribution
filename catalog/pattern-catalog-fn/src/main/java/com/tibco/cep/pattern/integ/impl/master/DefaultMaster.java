package com.tibco.cep.pattern.integ.impl.master;

import java.util.Collection;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.log.LoggerService;
import com.tibco.cep.impl.common.log.LogDelegatorService;
import com.tibco.cep.impl.common.resource.DefaultResourceProvider;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.impl.admin.DefaultAdmin;
import com.tibco.cep.pattern.integ.master.Master;
import com.tibco.cep.pattern.integ.master.OntologyService;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/*
* Author: Ashwin Jayaprakash Date: Sep 22, 2009 Time: 4:14:34 PM
*/
public class DefaultMaster implements Master {
    protected DefaultResourceProvider resourceProvider;

    protected LogDelegatorService logDelegatorService;

    protected DefaultOntologyService ontologyService;

    protected DefaultAdmin admin;

    public void start(RuleServiceProvider rsp) throws LifecycleException {
        resourceProvider = new DefaultResourceProvider();

        //--------------

        logDelegatorService = new LogDelegatorService();
        logDelegatorService.start();
        resourceProvider.registerResource(LoggerService.class, logDelegatorService);

        RSPResource rspResource = new RSPResource(rsp);
        resourceProvider.registerResource(RSPResource.class, rspResource);

        ontologyService = new DefaultOntologyService(rspResource);
        ontologyService.start();
        resourceProvider.registerResource(OntologyService.class, ontologyService);

        //--------------

        admin = new DefaultAdmin(resourceProvider);
        admin.start();

        //--------------

        Collection<EventDescriptor<? extends SimpleEvent>> eventDescriptors =
                ontologyService.getEventDescriptors();
        for (EventDescriptor<? extends SimpleEvent> eventDescriptor : eventDescriptors) {
            admin.register(eventDescriptor);
        }

        Collection<EventSource<? extends SimpleEvent>> eventSources =
                ontologyService.getEventSources();
        for (EventSource<? extends SimpleEvent> eventSource : eventSources) {
            admin.deploy(eventSource);
        }
    }

    public Admin getAdmin() {
        return admin;
    }

    public void stop() throws LifecycleException {
        admin.stop();

        //--------------

        Collection<EventSource<? extends SimpleEvent>> eventSources =
                ontologyService.getEventSources();
        for (EventSource<? extends SimpleEvent> eventSource : eventSources) {
            admin.undeploy(eventSource);
        }

        Collection<EventDescriptor<? extends SimpleEvent>> eventDescriptors =
                ontologyService.getEventDescriptors();
        for (EventDescriptor<? extends SimpleEvent> eventDescriptor : eventDescriptors) {
            admin.unregister(eventDescriptor);
        }

        //--------------

        ontologyService.stop();

        //--------------

        logDelegatorService.stop();

        //--------------

        resourceProvider.discard();
    }
}
