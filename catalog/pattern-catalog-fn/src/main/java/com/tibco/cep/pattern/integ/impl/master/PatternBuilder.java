package com.tibco.cep.pattern.integ.impl.master;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.kernel.model.rule.RuleFunction;
import com.tibco.cep.pattern.functions.Helper;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.admin.Session;
import com.tibco.cep.pattern.integ.impl.dsl2.Converter;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Event;
import com.tibco.cep.pattern.integ.impl.dsl2.OntologyProvider;
import com.tibco.cep.pattern.integ.impl.master.RuleFunctionCallbackHandler.Creator;
import com.tibco.cep.pattern.integ.master.OntologyService;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.runtime.session.impl.RuleSessionImpl;

/*
* Author: Ashwin Jayaprakash Date: Sep 24, 2009 Time: 4:37:22 PM
*/

public class PatternBuilder {
    protected Definition definition;

    protected HashMap<String, Comparable> parameters;

    protected Serializable closure;

    protected String successListenerURI;

    protected String failureListenerURI;

    protected boolean valid;

    public PatternBuilder(Definition definition) {
        this.definition = definition;
        this.parameters = new HashMap<String, Comparable>();
        this.valid = true;
    }

    public Definition getDefinition() {
        return definition;
    }

    public Map<String, Comparable> getParameters() {
        return parameters;
    }

    public void setParameter(String parameter, Comparable value) {
        parameters.put(parameter, value);
    }

    public Serializable getClosure() {
        return closure;
    }

    public void setClosure(Serializable closure) {
        this.closure = closure;
    }

    static void validateListenerRF(String listenerURI) {
        RuleSessionItems rsi = Helper.getCurrentThreadRSI();

        RuleSessionImpl rs = (RuleSessionImpl) rsi.getRuleSession();

        RuleFunction rf = rs.getRuleFunction(listenerURI);

        RuleFunctionCallbackHandler.validateAndCheckAllParamsNeeded(rf);
    }

    public String getSuccessListenerURI() {
        return successListenerURI;
    }

    public void setSuccessListenerURI(String successListenerURI) {
        validateListenerRF(successListenerURI);

        this.successListenerURI = successListenerURI;
    }

    public String getFailureListenerURI() {
        return failureListenerURI;
    }

    public void setFailureListenerURI(String failureListenerURI) {
        validateListenerRF(failureListenerURI);

        this.failureListenerURI = failureListenerURI;
    }

    /**
     * @return <code>false</code> after {@link #buildAndDeploy(RuleSessionItems, String, Id) deployment}
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param rsi
     * @param patternInstanceName
     * @throws LifecycleException
     */
    public void buildAndDeploy(RuleSessionItems rsi, String patternInstanceName, Id patternId)
            throws LifecycleException {
        Admin admin = rsi.getAdmin();
        ResourceProvider resourceProvider = admin.getResourceProvider();

        //--------------

        final OntologyService ontologyService =
                resourceProvider.fetchResource(OntologyService.class);

        OntologyProvider ontologyProvider = new OntologyProvider() {
            public EventDescriptor getEventDescriptor(Event event) {
                String uri = event.getUri();
                String eventClassName = ontologyService.getEventClassName(uri);

                return ontologyService.getEventDescriptor(eventClassName);
            }

            @Override
            public Collection<? extends EventDescriptor> getEventDescriptors() {
                return ontologyService.getEventDescriptors();
            }
        };

        //--------------

        Session session = admin.create(patternId);

        Converter converter = new Converter(definition, parameters, session, ontologyProvider);
        try {
            session = converter.convert();
        }
        catch (Exception e) {
            throw new LifecycleException(
                    "Error occurred while converting the pattern definition to its internal format",
                    e);
        }

        //--------------

        Creator creator = new Creator(rsi, definition.getUri(), patternInstanceName,
                successListenerURI, failureListenerURI, closure);

        if (creator.usesAllParameters()) {
            session.captureSequence();
        }

        session.setDriverCallbackCreator(creator);

        //--------------

        admin.deploy(session);

        valid = false;
    }
}
