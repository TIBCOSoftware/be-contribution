package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.ContextualInfoProvider;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Jun 25, 2009 Time: 12:05:39 PM
*/
public abstract class AbstractNode
        implements Node<Context, DefaultExpectedInput, Input>, ContextualInfoProvider {
    protected AbstractNode[] next;

    protected DefaultInputDef inputDef;

    protected DefaultExpectedTimeInput contextualExpectedTimeInput;

    protected AbstractNode() {
    }

    public void setNext(AbstractNode[] next) {
        this.next = next;
    }

    public void addNext(AbstractNode... givenNext) {
        if (givenNext.length == 0) {
            return;
        }

        if (next != null) {
            AbstractNode[] array = new AbstractNode[next.length + givenNext.length];

            int i = 0;
            for (; i < next.length; i++) {
                array[i] = next[i];
            }

            for (int k = 0; k < givenNext.length;) {
                array[i++] = givenNext[k++];
            }

            next = array;
        }
        else {
            next = givenNext;
        }
    }

    public AbstractNode[] getNext() {
        return next;
    }

    public DefaultInputDef getInputDef() {
        return inputDef;
    }

    public void setInputDef(DefaultInputDef inputDef) {
        this.inputDef = inputDef;
    }

    public void setContextualExpectedTimeInput(DefaultExpectedTimeInput expectedTimeInput) {
        contextualExpectedTimeInput = expectedTimeInput;
    }

    public DefaultExpectedTimeInput getContextualExpectedTimeInput() {
        return contextualExpectedTimeInput;
    }

    //--------------

    /**
     * @param context
     * @return If {@link #inputDef} is <code>null</code> or if already in {@link
     *         Driver#isInExpectationTrail(Node)}, then return <code>null</code>.
     *         <p/>
     *         Otherwise, create a simple {@link ExpectedInput} and {@link
     *         Driver#recordExpectedInput(ExpectedInput) record} it before returning.
     */
    public Collection<DefaultExpectedInput> fetchExpectations(Context context) {
        if (inputDef == null) {
            return null;
        }

        //--------------

        Driver driver = context.getDriver();

        if (driver.isInExpectationTrail(this)) {
            return null;
        }

        //--------------

        DefaultExpectedInput expectedInput = createExpectedInput();
        expectedInput.setDriverInstanceId(driver.getDriverInstanceId());
        expectedInput.setSource(inputDef.getSource());
        expectedInput.appendDestination(this);

        driver.recordExpectedInput(expectedInput);
        driver.addToExpectationTrail(this);

        ExpectedTimeInput currETI = getContextualExpectedTimeInput();
        if (currETI != null) {
            currETI.addContextualInput(expectedInput);
        }

        LinkedList<DefaultExpectedInput> list = new LinkedList<DefaultExpectedInput>();
        list.add(expectedInput);

        return list;
    }

    protected DefaultExpectedInput createExpectedInput() {
        return new DefaultExpectedInput();
    }

    //--------------

    /**
     * If the first one is not <code>null</code>, then all the items from the second are added to
     * the first and the first collection is returned.
     *
     * @param one Can be <code>null</code>.
     * @param two Can be <code>null</code>.
     * @return <code>null</code> if both are <code>null</code>.
     */
    protected static Collection<DefaultExpectedInput> combine(Collection<DefaultExpectedInput> one,
                                                              Collection<DefaultExpectedInput> two) {
        Collection<DefaultExpectedInput> list = null;

        if (one != null) {
            list = one;
        }

        if (two != null) {
            if (list == null) {
                list = new LinkedList<DefaultExpectedInput>();
            }

            for (DefaultExpectedInput ei : two) {
                list.add(ei);
            }
        }

        return list;
    }

    //--------------

    /**
     * Fetches the expectations from the {@link #getNext() successors} if any, or
     * <code>null</code>.
     * <p/>
     * Always calls and sets {@link #setContextualExpectedTimeInput(DefaultExpectedTimeInput)} using
     * the value present in this node.
     *
     * @param context
     * @return
     */
    protected Collection<DefaultExpectedInput> fetchNextExpectations(Context context) {
        if (next == null) {
            return null;
        }

        Collection<DefaultExpectedInput> list = null;
        DefaultExpectedTimeInput eti = getContextualExpectedTimeInput();

        for (AbstractNode node : next) {
            node.setContextualExpectedTimeInput(eti);

            Collection<DefaultExpectedInput> eis = node.fetchExpectations(context);

            if (eis != null && eis.size() > 0) {
                if (list == null) {
                    list = eis;
                }
                else {
                    list.addAll(eis);
                }
            }
        }

        return list;
    }

    /**
     * Calls {@link Sequence#recordOccurrence(ExpectedInput, Input)}.
     * <p/>
     * Calls {@link #fetchNextExpectations(Context)}.
     *
     * @param context
     * @param expectedInput
     * @param input
     */
    public void onInput(Context context, DefaultExpectedInput expectedInput, Input input) {
        context.getSequenceRecorder().recordOccurrence(expectedInput, input);

        DefaultExpectedTimeInput eti = getContextualExpectedTimeInput();
        if (eti != null) {
            eti.removeContextualInput(expectedInput);
        }

        fetchNextExpectations(context);
    }

    public void recallExpectation(Context context,
                                  DefaultExpectedInput expectedInput) {
        Node node = expectedInput.tearOffDestination();

        if (node != null) {
            node.recallExpectation(context, expectedInput);
        }
        else {
            ExpectedTimeInput currETI = getContextualExpectedTimeInput();

            if (currETI != null) {
                currETI.removeContextualInput(expectedInput);
            }
        }
    }

    //--------------

    public AbstractNode recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        if (inputDef != null) {
            inputDef = inputDef.recover(resourceProvider);
        }

        if (next != null) {
            for (int i = 0; i < next.length; i++) {
                next[i] = next[i].recover(resourceProvider);
            }
        }

        if (contextualExpectedTimeInput != null) {
            contextualExpectedTimeInput =
                    contextualExpectedTimeInput.recover(resourceProvider, params);
        }

        return this;
    }

    //--------------

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractNode)) {
            return false;
        }

        AbstractNode that = (AbstractNode) o;

        if (inputDef != null ? !inputDef.equals(that.inputDef) : that.inputDef != null) {
            return false;
        }
        if (!Arrays.equals(next, that.next)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (next != null ? Arrays.hashCode(next) : 0);
        result = 31 * result + (inputDef != null ? inputDef.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName() + "{InputDef: " + inputDef + "}}";
    }
}
