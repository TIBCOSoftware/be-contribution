package com.tibco.cep.pattern.jmx;

import java.io.Serializable;

/*
* Author: Ashwin Jayaprakash Date: Oct 15, 2009 Time: 4:27:07 PM
*/

public interface XDriverOwnerMBean {
    String getOwnerId();

    String[] getDriverIds();

    /**
     * String form of {@link #fetchSequenceStats()}.
     *
     * @return
     */
    String getSequenceStats();

    SequenceStats fetchSequenceStats();

    //--------------

    public interface SequenceStats extends Serializable {
        /**
         * Same as {@link XDriverOwnerMBean#getOwnerId()}.
         *
         * @return
         */
        String getOwnerId();

        /**
         * The super group stats is always the first element.
         *
         * @return
         */
        SequenceStatsComponent[] getComponents();
    }

    public interface SequenceStatsComponent extends Serializable {
        String getId();

        int getInstanceCount();

        int getCompletedCount();
    }
}
