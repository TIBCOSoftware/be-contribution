package com.tibco.cep.pattern.integ.impl.master;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.designtime.model.Ontology;
import com.tibco.cep.designtime.model.event.Event;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.impl.util.SimpleEventSource;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.integ.master.OntologyService;
import com.tibco.cep.pattern.subscriber.impl.master.SimpleEventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.runtime.model.TypeManager;
import com.tibco.cep.runtime.model.TypeManager.TypeDescriptor;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/*
* Author: Ashwin Jayaprakash Date: Sep 22, 2009 Time: 1:59:08 PM
*/

public class DefaultOntologyService implements OntologyService {
    protected Id resourceId;

    protected HashMap<String, String> uriAndEventClassNames;

    protected HashMap<String, EventDescriptor<? extends SimpleEvent>> classNameAndEventDescriptors;

    protected HashMap<String, EventSource<? extends SimpleEvent>> classNameAndEventSources;

    protected ConcurrentHashMap<String, Definition> uriAndPatternDefs;

    protected transient RuleServiceProvider rsp;

    protected transient DefaultLanguageManager languageManager;

    /**
     * @param rspResource
     */
    public DefaultOntologyService(RSPResource rspResource) {
        RuleServiceProvider rsp = rspResource.getRsp();

        this.resourceId = new DefaultId(rsp.getName(), getClass().getName());

        this.rsp = rsp;

        //-----------------

        this.languageManager = new DefaultLanguageManager();
        this.languageManager.init(rsp);

        a(rsp);

        //-----------------

        this.uriAndPatternDefs = new ConcurrentHashMap<String, Definition>();
    }

    private void a(RuleServiceProvider rsp) {
        Ontology ontology = rsp.getProject().getOntology();

        this.uriAndEventClassNames = new HashMap<String, String>();
        this.classNameAndEventDescriptors =
                new HashMap<String, EventDescriptor<? extends SimpleEvent>>();
        this.classNameAndEventSources = new HashMap<String, EventSource<? extends SimpleEvent>>();

        TypeManager typeManager = rsp.getTypeManager();

        //----------------

        //First simply collect the types and EDs.

        HashMap<String, Class> tempClassNameToClass = new HashMap<String, Class>();

        Collection<Event> events = ontology.getEvents();
        for (Event event : events) {
            String uri = event.getFullPath();
            TypeDescriptor typeDescriptor = typeManager.getTypeDescriptor(uri);
            Class clazz = typeDescriptor.getImplClass();
            String className = clazz.getName();

            this.uriAndEventClassNames.put(uri, className);

            EventDescriptor eventDescriptor = new SimpleEventDescriptor(className, event);
            this.classNameAndEventDescriptors.put(className, eventDescriptor);

            tempClassNameToClass.put(className, clazz);
        }

        //----------------

        //Now create all the ES with the full hierarchy of EDs.

        for (Entry<String, EventDescriptor<? extends SimpleEvent>> entry : classNameAndEventDescriptors.entrySet()) {
            String className = entry.getKey();
            EventDescriptor eventDescriptor = entry.getValue();

            Class klass = tempClassNameToClass.get(className);
            LinkedList<EventDescriptor> superClassEDs = collectParentEDs(klass);

            EventSource<? extends SimpleEvent> eventSource = null;
            if (superClassEDs.isEmpty()) {
                eventSource = new SimpleEventSource(eventDescriptor);
            }
            else {
                EventDescriptor[] eds = superClassEDs.toArray(new EventDescriptor[superClassEDs.size()]);

                eventSource = new SimpleEventSource(eventDescriptor, eds);
            }

            this.classNameAndEventSources.put(className, eventSource);
        }

        tempClassNameToClass.clear();
    }

    protected LinkedList<EventDescriptor> collectParentEDs(Class clazz) {
        LinkedList<EventDescriptor> superClassEDs = new LinkedList<EventDescriptor>();

        Class superClass = clazz.getSuperclass();
        //Parent class is also an Event. Add that also.
        while (superClass != null) {
            EventDescriptor superClassED = this.classNameAndEventDescriptors.get(superClass.getName());
            if (superClassED != null) {
                superClassEDs.add(superClassED);
            }

            superClass = superClass.getSuperclass();
        }

        return superClassEDs;
    }

    public void start() throws LifecycleException {
    }

    public void stop() throws LifecycleException {
        if (classNameAndEventDescriptors != null) {
            classNameAndEventDescriptors.clear();
            classNameAndEventDescriptors = null;
        }

        if (classNameAndEventSources != null) {
            classNameAndEventSources.clear();
            classNameAndEventSources = null;
        }

        if (uriAndPatternDefs != null) {
            uriAndPatternDefs.clear();
            uriAndPatternDefs = null;
        }

        rsp = null;
    }

    public Id getResourceId() {
        return resourceId;
    }

    public DefaultOntologyService recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        RSPResource rspResource = resourceProvider.fetchResource(RSPResource.class);
        RuleServiceProvider rsp = rspResource.getRsp();

        this.rsp = rsp;

        this.languageManager = new DefaultLanguageManager();
        this.languageManager.init(rsp);

        for (Entry<String, EventDescriptor<? extends SimpleEvent>> entry : classNameAndEventDescriptors
                .entrySet()) {
            EventDescriptor<? extends SimpleEvent> ed = entry.getValue();

            ed = ed.recover(resourceProvider, params);

            entry.setValue(ed);
        }

        for (Entry<String, EventSource<? extends SimpleEvent>> entry : classNameAndEventSources
                .entrySet()) {
            EventSource<? extends SimpleEvent> es = entry.getValue();

            es = es.recover(resourceProvider, params);

            entry.setValue(es);
        }

        return this;
    }

    //---------------

    public RuleServiceProvider getRsp() {
        return rsp;
    }

    public DefaultLanguageManager getLanguageManager() {
        return languageManager;
    }

    //---------------

    public Set<String> getEventURIs() {
        return uriAndEventClassNames.keySet();
    }

    public String getEventClassName(String uri) {
        return uriAndEventClassNames.get(uri);
    }

    public EventDescriptor<? extends SimpleEvent> getEventDescriptor(String className) {
        return classNameAndEventDescriptors.get(className);
    }

    public Collection<EventDescriptor<? extends SimpleEvent>> getEventDescriptors() {
        return classNameAndEventDescriptors.values();
    }

    public EventSource<? extends SimpleEvent> getEventSource(String className) {
        return classNameAndEventSources.get(className);
    }

    public Collection<EventSource<? extends SimpleEvent>> getEventSources() {
        return classNameAndEventSources.values();
    }

    //------------

    public void registerPatternDef(Definition definition) throws LifecycleException {
        Definition existing = uriAndPatternDefs.putIfAbsent(definition.getUri(), definition);

        if (existing != null) {
            throw new LifecycleException(
                    "The Pattern definition URI [" + definition.getUri() + "] is already in use.");
        }
    }

    public Definition getPatternDef(String uri) {
        return uriAndPatternDefs.get(uri);
    }

    public Collection<Definition> getPatternDefs() {
        return uriAndPatternDefs.values();
    }

    public Definition unregisterPatternDef(String uri) {
        return uriAndPatternDefs.remove(uri);
    }
}
