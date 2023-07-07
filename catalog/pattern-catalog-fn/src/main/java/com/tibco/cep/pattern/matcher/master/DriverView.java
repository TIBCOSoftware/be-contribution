package com.tibco.cep.pattern.matcher.master;

import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.trace.SequenceView;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 5:40:09 PM
*/

/**
 * A minimal set of methods for external API users to view and interact with the "Driver".
 * <p/>
 * Note: This interface must be used only from within the Driver's Thread of execution.
 */
public interface DriverView {
    Id getDriverCorrelationId();

    /**
     * Uniquely identifies this driver instance unlike {@link #getDriverCorrelationId()} where over
     * a period of time several driver instances might have had the same correlation Id. But never
     * at the same time.
     *
     * @return
     */
    Id getDriverInstanceId();

    //--------------

    SequenceView getSequence();

    //--------------

    boolean isValid();

    //-----------

    void flag(Failure failure);

    boolean hasFailures();

    @Optional
    List<Failure> getFailures();
}