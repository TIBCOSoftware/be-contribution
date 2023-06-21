package com.tibco.cep.pattern.matcher.impl.master;

import static com.tibco.cep.pattern.impl.util.Helper.$logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.EndSource;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.model.Start;
import com.tibco.cep.pattern.matcher.response.DefaultSuccess;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.response.Success;
import com.tibco.cep.util.annotation.LogCategory;
import com.tibco.cep.util.annotation.Optional;
import com.tibco.cep.util.annotation.ReadOnly;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 5:53:48 PM
*/
@LogCategory("pattern.core.matcher.driver")
public abstract class AbstractDriver implements Driver {
    protected HashMap<Source, ExpectedInput> expectedInputChoices;

    protected HashMap<Id, ExpectedTimeInput> expectedTimeInputs;

    protected Success fixedSuccessResponse;

    protected Id correlationId;

    protected Id instanceId;

    protected EndSource endSource;

    protected InternalTimeSource timeSource;

    protected Start start;

    protected transient LinkedList<Failure> failures;

    protected transient HashSet<Node> expectationSetterTrail;

    protected static transient Logger LOGGER;

    public AbstractDriver(ResourceProvider resourceProvider, AbstractDriverSettings settings) {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        this.correlationId = settings.getDriverId();
        this.endSource = settings.getEndSource();
        this.timeSource = settings.getTimeSource();
        this.start = settings.getStart();

        UUID uuid = new UUID(System.currentTimeMillis(), System.identityHashCode(this));
        String uuidStr = uuid.toString();
        this.instanceId = new DefaultId(uuidStr);

        //------------

        this.expectedInputChoices = new HashMap<Source, ExpectedInput>(4);

        this.expectedTimeInputs = new HashMap<Id, ExpectedTimeInput>(4);

        this.fixedSuccessResponse = new DefaultSuccess(correlationId, instanceId);
    }

    public final Id getDriverCorrelationId() {
        return correlationId;
    }

    public Id getDriverInstanceId() {
        return instanceId;
    }

    //------------

    public void recordExpectedInput(ExpectedInput expectedInput) {
        ExpectedInput existing = expectedInputChoices.put(expectedInput.getSource(), expectedInput);

        if (existing != null) {
            String s = "An existing expected input was replaced in driver [" + correlationId
                    + "] possibly due to ambiguous pattern definition (alternate paths)." +
                    "Source [" + expectedInput.getSource() + "], existing [" + existing
                    + "], replaced with [" + expectedInput + "].";

            LOGGER.log(Level.WARNING, s);
        }
    }

    public void eraseExpectedInput(ExpectedInput expectedInput) {
        expectedInputChoices.remove(expectedInput.getSource());
    }

    @Optional
    @ReadOnly
    public Collection<? extends ExpectedInput> getExpectedInputs() {
        return expectedInputChoices.values();
    }

    public void recordExpectedTimeInput(ExpectedTimeInput expectedTimeInput) {
        expectedTimeInputs.put(expectedTimeInput.getUniqueId(), expectedTimeInput);

        timeSource.recordExpectedTimeInput(correlationId, expectedTimeInput);
    }

    public void eraseExpectedTimeInput(ExpectedTimeInput expectedTimeInput) {
        ExpectedTimeInput eti = expectedTimeInputs.remove(expectedTimeInput.getUniqueId());

        eraseContextualInputs(eti);
    }

    protected void eraseContextualInputs(ExpectedTimeInput eti) {
        Set<? extends ExpectedInput> contextualInputs = eti.removeContextualInputs();

        if (contextualInputs == null) {
            return;
        }

        for (Iterator<? extends ExpectedInput> i = contextualInputs.iterator(); i.hasNext();) {
            ExpectedInput contextualInput = i.next();

            eraseExpectedInput(contextualInput);

            i.remove();
        }
    }

    @Optional
    @ReadOnly
    public Collection<? extends ExpectedTimeInput> getExpectedTimeInputs() {
        return expectedTimeInputs.values();
    }

    //------------

    public void addToExpectationTrail(Node node) {
        if (expectationSetterTrail == null) {
            expectationSetterTrail = new HashSet<Node>(4);
        }

        expectationSetterTrail.add(node);
    }

    public boolean isInExpectationTrail(Node node) {
        if (expectationSetterTrail == null) {
            return false;
        }

        return expectationSetterTrail.contains(node);
    }

    public void clearExpectationTrail() {
        if (expectationSetterTrail != null) {
            expectationSetterTrail.clear();
        }
    }

    //------------

    public void flag(Failure failure) {
        if (failures == null) {
            failures = new LinkedList<Failure>();
        }

        failures.add(failure);
    }

    public boolean hasFailures() {
        return (failures != null) && (failures.size() > 0);
    }

    public List<Failure> getFailures() {
        return failures;
    }

    protected void clearFailures() {
        if (failures != null) {
            failures.clear();
        }
    }

    //------------

    public AbstractDriver recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        expectedInputChoices = recoverMap(expectedInputChoices, resourceProvider);

        timeSource = resourceProvider
                .fetchResource(InternalTimeSource.class, timeSource.getResourceId());

        for (Entry<Id, ExpectedTimeInput> entry : expectedTimeInputs.entrySet()) {
            ExpectedTimeInput timeInput = entry.getValue();

            timeInput = timeInput.recover(resourceProvider, params);

            entry.setValue(timeInput);
        }

        recoverMapValues(expectedTimeInputs, resourceProvider, params);

        return this;
    }

    protected static void recoverMapValues(HashMap<Id, ExpectedTimeInput> map,
                                           ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        for (Entry<Id, ExpectedTimeInput> entry : map.entrySet()) {
            ExpectedTimeInput timeInput = entry.getValue();

            timeInput = timeInput.recover(resourceProvider, params);

            entry.setValue(timeInput);
        }
    }

    protected static HashMap<Source, ExpectedInput> recoverMap(
            HashMap<Source, ExpectedInput> oldMap,
            ResourceProvider resourceProvider, Object... params) throws RecoveryException {
        HashMap<Source, ExpectedInput> newMap = new HashMap<Source, ExpectedInput>();

        for (Entry<Source, ExpectedInput> entry : oldMap.entrySet()) {
            Source source = entry.getKey();
            ExpectedInput input = entry.getValue();

            Source newSource = resourceProvider.fetchResource(Source.class, source.getResourceId());
            input = input.recover(resourceProvider, params);

            newMap.put(newSource, input);
        }

        oldMap.clear();

        return newMap;
    }
}
