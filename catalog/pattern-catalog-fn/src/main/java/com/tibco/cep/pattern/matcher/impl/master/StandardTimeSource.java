package com.tibco.cep.pattern.matcher.impl.master;

import static com.tibco.cep.pattern.impl.util.Helper.$logger;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.matcher.master.DriverOwnerRegistry;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.service.AsyncScheduler;
import com.tibco.cep.util.annotation.LogCategory;

/*
* Author: Ashwin Jayaprakash Date: Jun 26, 2009 Time: 3:21:18 PM
*/

/**
 * The time-source that will be used at runtime.
 */
@LogCategory("pattern.core.matcher.source.time")
public class StandardTimeSource extends DefaultSource implements InternalTimeSource {
    protected Id driverOwnerId;

    protected boolean schedulerDedicated;

    protected AsyncScheduler scheduler;

    //------------

    protected transient ResourceProvider resourceProvider;

    protected transient DriverOwner driverOwner;

    protected static transient Logger LOGGER;

    public StandardTimeSource() {
    }

    public Id getDriverOwnerId() {
        return driverOwnerId;
    }

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    /**
     * If the {@link #setScheduler(AsyncScheduler)} is dedicated to this instance, then its life
     * cycle will be maintained by this instance.
     *
     * @return
     */
    public boolean isSchedulerDedicated() {
        return schedulerDedicated;
    }

    public AsyncScheduler getScheduler() {
        return scheduler;
    }

    public void setDriverOwnerId(Id driverOwnerId) {
        this.driverOwnerId = driverOwnerId;

        if (this.resourceProvider != null) {
            initDriverOwner();
        }
    }

    public void setSchedulerDedicated(boolean schedulerDedicated) {
        this.schedulerDedicated = schedulerDedicated;
    }

    public void setScheduler(AsyncScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setResourceProvider(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        if (this.driverOwnerId != null) {
            initDriverOwner();
        }
    }

    private void initDriverOwner() {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        DriverOwnerRegistry driverOwnerRegistry =
                resourceProvider.fetchResource(DriverOwnerRegistry.class);

        driverOwner = driverOwnerRegistry.getDriverOwner(driverOwnerId);
    }

    //-----------

    /**
     * Starts the {@link #getScheduler()} only if it is {@link #isSchedulerDedicated() dedicated}.
     */
    public void start() throws LifecycleException {
        initDriverOwner();

        if (schedulerDedicated) {
            scheduler.start();
        }
    }

    /**
     * Stops the {@link #getScheduler()} only if it is {@link #isSchedulerDedicated() dedicated}.
     */
    public void stop() throws LifecycleException {
        if (schedulerDedicated) {
            scheduler.stop();
        }
    }

    //-----------

    public void recordExpectedTimeInput(Id driverCorrelationId,
                                        ExpectedTimeInput expectedTimeInput) {
        Job job = new Job(driverCorrelationId, expectedTimeInput);

        long delay = expectedTimeInput.getExpectedTimeOffsetMillis();

        scheduler.schedule(job, delay, TimeUnit.MILLISECONDS);
    }

    //-----------

    @Override
    public StandardTimeSource recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        if (this.schedulerDedicated) {
            this.scheduler = this.scheduler.recover(resourceProvider, params);
        }

        this.resourceProvider = resourceProvider;

        initDriverOwner();

        return this;
    }

    //-----------

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StandardTimeSource)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        StandardTimeSource that = (StandardTimeSource) o;

        if (driverOwnerId != null ? !driverOwnerId.equals(that.driverOwnerId) :
                that.driverOwnerId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (driverOwnerId != null ? driverOwnerId.hashCode() : 0);
        return result;
    }

    //-----------

    protected class Job implements Runnable {
        protected Id driverCorrelationId;

        protected ExpectedTimeInput expectedTimeInput;

        public Job(Id driverCorrelationId, ExpectedTimeInput expectedTimeInput) {
            this.driverCorrelationId = driverCorrelationId;
            this.expectedTimeInput = expectedTimeInput;
        }

        public void run() {
            try {

                /*
                todo How do we force 1 active job per DriverId? Otherwise there could be 2 jobs
                scheduled for the same Driver on 2 separate threads and get delivered out of order.
                */

                DefaultTimeInput input = new DefaultTimeInput(expectedTimeInput);

                Response response = driverOwner.onTimeOut(driverCorrelationId, input);

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                            "Response for driver [" + driverCorrelationId + "] and input [" +
                                    input + "] is [" + response + "]");
                }
            }
            catch (Throwable t) {
                LOGGER.log(Level.SEVERE,
                        "Error occurred when expected time input [" + expectedTimeInput +
                                "] for driver [" + driverCorrelationId + "] was being delivered",
                        t);
            }
        }
    }
}
