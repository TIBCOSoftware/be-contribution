package com.tibco.cep.pattern.matcher.model;

import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.Recoverable;
import com.tibco.cep.pattern.matcher.master.Source;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 1:21:58 PM
*/
public interface ExpectedInput extends Recoverable<ExpectedInput> {
    Source getSource();

    /**
     * The driver Instance Id. Not the be confused with the driver's Correlation Id.
     *
     * @return
     */
    Id getDriverInstanceId();

    //-----------

    /**
     * @param destination Sets up the given destination such that it moves the original/existing
     *                    destination and then the given destination the {@link
     *                    #getDestinationChain() chain}.
     */
    void appendDestination(Node destination);

    /**
     * @return Can be <code>null</code>.
     */
    List<Node> getDestinationChain();

    /**
     * @return The most recent destination in the {@link #getDestinationChain()}.
     */
    Node tearOffDestination();
}
