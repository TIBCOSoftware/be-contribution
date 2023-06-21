package com.tibco.cep.pattern.matcher.impl.master;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.matcher.impl.model.DefaultExpectedTimeInput.DefaultTimeOutHint;
import com.tibco.cep.pattern.matcher.impl.trace.DefaultSequence;
import com.tibco.cep.pattern.matcher.impl.trace.LiteSequence;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.DriverCallback;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TimeInput;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSet;
import com.tibco.cep.pattern.matcher.model.ExpectedEndInput;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.response.DefaultComplete;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.response.MultipleFailures;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.pattern.matcher.response.UnexpectedOccurrence;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Jun 25, 2009 Time: 5:10:24 PM
*/

//todo Implement rollback?
//todo Implement save/restore

//todo Relative time "using some field"

//todo Use Sequence
//todo How - dynamic B such that B after A when B = A/3

public class DefaultDriver extends AbstractDriver {
    protected Sequence sequence;

    protected DefaultCorrIdGenerator corrIdGenerator;

    protected DriverCallback optionalDriverCallback;

    protected TransitionGuardSet<?, ?> optionalTransitionGuardSet;

    protected final ReentrantLock lock;

    protected final AtomicBoolean valid;

    protected transient DefaultContext context;

    protected transient ResourceProvider resourceProvider;

    public DefaultDriver(ResourceProvider resourceProvider, DefaultDriverSettings settings) {
        super(resourceProvider, settings);

        this.resourceProvider = resourceProvider;

        this.sequence = settings.isRecordSequence() ? new DefaultSequence() : new LiteSequence();

        Id flowId = settings.getDriverId();
        this.corrIdGenerator = new DefaultCorrIdGenerator(flowId);

        this.optionalDriverCallback = settings.getOptionalDriverCallback();

        this.optionalTransitionGuardSet = settings.getOptionalTransitionGuardSet();

        this.lock = new ReentrantLock(true);
        this.valid = new AtomicBoolean(true);
    }

    //---------------

    public final Sequence getSequence() {
        return sequence;
    }

    public final TransitionGuardSet getTransitionGuardSet() {
        return optionalTransitionGuardSet;
    }

    //---------------

    public final void lock() {
        lock.lock();
    }

    public final boolean tryLock() {
        return lock.tryLock();
    }

    public final boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    public final void unlock() {
        lock.unlock();
    }

    public final boolean isValid() {
        return valid.get();
    }

    //---------------

    public void start() {
        context = new DefaultContext(resourceProvider, getDriverCorrelationId(),
                corrIdGenerator, this);

        if (optionalDriverCallback != null) {
            optionalDriverCallback.start(resourceProvider, this);
        }

        if (optionalTransitionGuardSet != null) {
            optionalTransitionGuardSet.start(resourceProvider, this);
        }

        try {
            start.start(context);
        }
        finally {
            clearFailures();
            clearExpectationTrail();
        }

        valid.set(true);
    }

    public void stop() {
        valid.set(false);

        if (optionalTransitionGuardSet != null) {
            optionalTransitionGuardSet.stop();
        }

        if (optionalDriverCallback != null) {
            optionalDriverCallback.stop();
        }

        this.context = null;
    }

    //---------------    

    public Response onInput(Source source, Input input) {
        Response r = null;

        try {
            r = handleOnInput(source, input);
        }
        finally {
            clearFailures();
            clearExpectationTrail();
        }

        if (optionalDriverCallback != null) {
            optionalDriverCallback.afterInput(source, input, r);
        }

        return r;
    }

    private Response handleOnInput(Source source, Input input) {
        ExpectedInput expectedInput = expectedInputChoices.remove(source);

        if (expectedInput != null) {
            recall(context, expectedInputChoices);

            //---------------

            Node node = expectedInput.tearOffDestination();
            node.onInput(context, expectedInput, input);

            if (hasFailures()) {
                //todo Clear all.
                return moveFailures(input, failures);
            }

            if (isThisTheEnd()) {
                return handleEnd(source, input);
            }

            return fixedSuccessResponse;
        }

        //---------------

        //todo Clear all.
        return Flags.DEBUG ?
                new UnexpectedOccurrence(input, source, null,
                        new Exception(
                                "Unexpected occurrence in driver: [" + correlationId + "]"),
                        newCombined(expectedInputChoices.values(),
                                expectedTimeInputs.values())) :
                new UnexpectedOccurrence(input, null, null,
                        newCombined(expectedInputChoices.values(),
                                expectedTimeInputs.values()));
    }

    private boolean isThisTheEnd() {
        return expectedInputChoices.size() == 1 && expectedInputChoices.containsKey(endSource);
    }

    protected Response handleEnd(Source source, Input input) {
        ExpectedEndInput expectedEndInput =
                (ExpectedEndInput) expectedInputChoices.remove(endSource);

        Node node = expectedEndInput.tearOffDestination();
        node.onInput(context, expectedEndInput, input);

        return new DefaultComplete(correlationId, instanceId);
    }

    private static Collection<ExpectedInput> newCombined(
            Collection<? extends ExpectedInput>... lists) {
        LinkedList<ExpectedInput> newList = new LinkedList<ExpectedInput>();

        for (Collection<? extends ExpectedInput> list : lists) {
            newList.addAll(list);
        }

        return newList;
    }

    protected static DefaultTimeOutHint recall(Context context, ExpectedTimeInput expectedTimeInput,
                                               HashMap<Source, ExpectedInput> expectedInputChoices) {
        Set<? extends ExpectedInput> contextualInputs = expectedTimeInput.getContextualInputs();
        if (contextualInputs != null) {
            //Make a copy so that node.recallXXX() will not barf on concurrent-mod.
            contextualInputs = new HashSet<ExpectedInput>(contextualInputs);
        }

        LinkedList<ExpectedInput> nonContextualInputs = null;

        Set<Entry<Source, ExpectedInput>> choices = expectedInputChoices.entrySet();

        for (Iterator<Entry<Source, ExpectedInput>> entries = choices.iterator();
             entries.hasNext();) {

            Entry<Source, ExpectedInput> entry = entries.next();
            ExpectedInput ei = entry.getValue();

            if (contextualInputs == null || contextualInputs.contains(ei) == false) {
                if (nonContextualInputs == null) {
                    nonContextualInputs = new LinkedList<ExpectedInput>();
                }

                nonContextualInputs.add(ei);

                //Should not remove because it's not in our context.
                continue;
            }

            //----------------

            Node node = ei.tearOffDestination();
            node.recallExpectation(context, ei);

            //----------------

            entries.remove();
        }

        //----------------

        return new DefaultTimeOutHint(contextualInputs, nonContextualInputs);
    }

    protected static void recall(Context context,
                                 HashMap<Source, ExpectedInput> expectedInputChoices) {
        Set<Entry<Source, ExpectedInput>> choices = expectedInputChoices.entrySet();

        for (Iterator<Entry<Source, ExpectedInput>> entries = choices.iterator();
             entries.hasNext();) {

            Entry<Source, ExpectedInput> entry = entries.next();

            //----------------

            ExpectedInput ei = entry.getValue();

            Node node = ei.tearOffDestination();
            node.recallExpectation(context, ei);

            //----------------

            entries.remove();
        }
    }

    public Response onTimeOut(TimeInput input) {
        Response r;
        try {
            r = handleOnTimeOut(input);
        }
        finally {
            clearFailures();
            clearExpectationTrail();
        }

        if (optionalDriverCallback != null) {
            optionalDriverCallback.afterTimeOut(input, r);
        }

        return r;
    }

    private Response handleOnTimeOut(TimeInput input) {
        Id id = input.getKey();

        ExpectedTimeInput expectedTimeInput = expectedTimeInputs.remove(id);

        if (expectedTimeInput != null) {
            DefaultTimeOutHint timeOutHint =
                    recall(context, expectedTimeInput, expectedInputChoices);

            expectedTimeInput.setTimeOutHint(timeOutHint);

            //---------------

            Node destination = expectedTimeInput.tearOffDestination();
            destination.onInput(context, expectedTimeInput, input);

            if (hasFailures()) {
                //todo Clear all.
                Response multipleFailures = moveFailures(input, failures);

                return multipleFailures;
            }
        }

        //---------------

        if (isThisTheEnd()) {
            return handleEnd(timeSource, input);
        }

        return fixedSuccessResponse;
    }

    /**
     * Clears the sent list.
     *
     * @param trigger
     * @param failures
     * @return
     */
    protected static Response moveFailures(Input trigger, List<Failure> failures) {
        Failure f = null;

        if (failures.size() == 1) {
            f = failures.remove(0);
        }
        else {
            LinkedList<Failure> allFs = new LinkedList<Failure>(failures);

            f = Flags.DEBUG ?
                    new MultipleFailures(trigger, null, new Exception("Multiple failures"), allFs)
                    : new MultipleFailures(trigger, null, null, allFs);

            failures.clear();
        }

        return f;
    }

    //---------------

    @Override
    public DefaultDriver recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        super.recover(resourceProvider, params);

        this.resourceProvider = resourceProvider;

        this.sequence = this.sequence.recover(resourceProvider, params);
        this.corrIdGenerator = this.corrIdGenerator.recover(resourceProvider, params);

        if (this.optionalDriverCallback != null) {
            this.optionalDriverCallback =
                    this.optionalDriverCallback.recover(resourceProvider, params);
        }

        if (this.optionalTransitionGuardSet != null) {
            this.optionalTransitionGuardSet =
                    this.optionalTransitionGuardSet.recover(resourceProvider, params);
        }

        return this;
    }
}
