package com.tibco.cep.pattern.matcher.model;

import java.util.Collection;

import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.matcher.master.Context;
import com.tibco.cep.pattern.matcher.master.Input;

/*
* Author: Ashwin Jayaprakash Date: Jun 23, 2009 Time: 5:44:41 PM
*/
public interface Node<C extends Context, E extends ExpectedInput, I extends Input>
        extends Recoverable<Node<C, E, I>> {
    /**
     * Can be <code>null</code> for pass-through nodes.
     *
     * @return
     */
    InputDef getInputDef();

    /**
     * Can be <code>null</code>.
     *
     * @return
     */
    Node<C, E, I>[] getNext();

    //--------------

    /**
     * @param context
     * @return <code>null</code> if there is nothing. Otherwise the collection should be a new one
     *         each time. The caller can add or remove elements from it.
     */
    Collection<? extends E> fetchExpectations(C context);

    //--------------

    void onInput(C context, E expectedInput, I input);

    void recallExpectation(C context, E expectedInput);
}
