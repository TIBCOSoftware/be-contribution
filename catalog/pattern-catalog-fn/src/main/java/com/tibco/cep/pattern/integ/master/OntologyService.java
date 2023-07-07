package com.tibco.cep.pattern.integ.master;


import java.util.Collection;
import java.util.Set;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.service.Service;

/*
* Author: Ashwin Jayaprakash Date: Sep 22, 2009 Time: 11:41:09 AM
*/
public interface OntologyService extends Service {
    RuleServiceProvider getRsp();

    LanguageManager getLanguageManager();

    //------------

    Set<String> getEventURIs();

    String getEventClassName(String uri);

    EventDescriptor<? extends SimpleEvent> getEventDescriptor(String className);

    Collection<EventDescriptor<? extends SimpleEvent>> getEventDescriptors();

    EventSource<? extends SimpleEvent> getEventSource(String className);

    Collection<EventSource<? extends SimpleEvent>> getEventSources();

    //------------

    void registerPatternDef(Definition definition) throws LifecycleException;

    Definition getPatternDef(String uri);

    Collection<Definition> getPatternDefs();

    Definition unregisterPatternDef(String uri) throws LifecycleException;
}
