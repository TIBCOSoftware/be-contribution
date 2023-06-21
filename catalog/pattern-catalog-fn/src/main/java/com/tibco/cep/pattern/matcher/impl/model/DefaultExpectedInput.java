package com.tibco.cep.pattern.matcher.impl.model;

import java.util.LinkedList;
import java.util.List;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jun 29, 2009 Time: 2:41:43 PM
*/
public class DefaultExpectedInput implements ExpectedInput {
    protected Source source;

    protected Id driverInstanceId;

    protected LinkedList<Node> destinationChain;

    public DefaultExpectedInput() {
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Id getDriverInstanceId() {
        return driverInstanceId;
    }

    public void setDriverInstanceId(Id driverInstanceId) {
        this.driverInstanceId = driverInstanceId;
    }

    //------------

    public void appendDestination(Node givenDestination) {
        if (destinationChain == null) {
            destinationChain = new LinkedList<Node>();
        }

        destinationChain.add(givenDestination);
    }

    public List<Node> getDestinationChain() {
        return destinationChain;
    }

    public Node tearOffDestination() {
        if (destinationChain == null) {
            return null;
        }

        //----------

        Node destination = destinationChain.removeLast();

        if (destinationChain.size() == 0) {
            destinationChain = null;
        }

        return destination;
    }

    //--------------

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DefaultExpectedInput)) {
            return false;
        }

        DefaultExpectedInput that = (DefaultExpectedInput) o;

        if (!driverInstanceId.equals(that.driverInstanceId)) {
            return false;
        }

        if (!source.equals(that.source)) {
            return false;
        }

        if (destinationChain != null ? !destinationChain.equals(that.destinationChain) :
                that.destinationChain != null) {
            return false;
        }

        return true;
    }

    public int hashCodea() {
        /*
        Cannot use destinationChain for hashCode because the chain keeps growing while adding
        and shrinking while processing. So, it would never hash back to the same slots.
        */
        return (31 * driverInstanceId.hashCode()) + source.hashCode();
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName()
                + "{Driver instance Id: " + driverInstanceId
                + ", Source: " + source
                + ", Destination chain: " + destinationChain
                + "}}";
    }

    //--------------

    public DefaultExpectedInput recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        source = resourceProvider.fetchResource(Source.class, source.getResourceId());

        return this;
    }
}
