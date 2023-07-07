package com.tibco.cep.pattern.matcher.impl.master;

import static com.tibco.cep.pattern.impl.util.Helper.$logger;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.matcher.impl.stats.DefaultMergedSequenceStats;
import com.tibco.cep.pattern.matcher.master.AdvancedDriverOwner;
import com.tibco.cep.pattern.matcher.master.Driver;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TimeInput;
import com.tibco.cep.pattern.matcher.master.TimeSource;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSet;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;
import com.tibco.cep.pattern.matcher.response.Complete;
import com.tibco.cep.pattern.matcher.response.DriverNotFound;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.response.IgnoredTimeOut;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.util.annotation.LogCategory;

/*
* Author: Ashwin Jayaprakash Date: Jul 6, 2009 Time: 2:58:12 PM
*/

@LogCategory("pattern.core.matcher.driverowner")
public abstract class AbstractDriverOwner<D extends AbstractDriver> implements AdvancedDriverOwner {
    protected ConcurrentHashMap<Id, D> drivers;

    protected DefaultPlan plan;

    protected Id ownerId;

    protected DriverCallbackCreator optionalDriverCallbackCreator;

    protected TransitionGuardSetCreator<? extends TransitionGuardSet, ?, ?>
            optionalTransitionGuardSetCreator;

    protected boolean recordDriverSequence;
    
    protected ThreadLocal<Set<Id>> threadLocalDriverCorrelationIds = new ThreadLocal<Set<Id>>() {
    	protected Set<Id> initialValue() {
    		return new HashSet<Id>();
    	}
    };
    
    public Set<Id> getThreadLocalDriverCorrelationIds() {
    	return threadLocalDriverCorrelationIds.get();
    }

    /**
     * Only if {@link #recordDriverSequence} is <code>true</code>.
     */
    protected transient DefaultMergedSequenceStats optionalMergedSequenceStats;

    protected transient ResourceProvider resourceProvider;

    protected static transient Logger LOGGER;

    /**
     * @param resourceProvider
     * @param settings
     */
    public AbstractDriverOwner(ResourceProvider resourceProvider,
                               AbstractDriverOwnerSettings settings) {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        this.resourceProvider = resourceProvider;
        this.ownerId = settings.getOwnerId();

        this.plan = settings.getPlan();
        this.optionalDriverCallbackCreator = settings.getOptionalDriverCallbackCreator();
        this.optionalTransitionGuardSetCreator = settings.getOptionalTransitionGuardSetCreator();

        this.recordDriverSequence = settings.isOptionalRecordDriverSequence();
        if (this.recordDriverSequence) {
            this.optionalMergedSequenceStats = new DefaultMergedSequenceStats(this.ownerId);
        }

        this.drivers = new ConcurrentHashMap<Id, D>(4);
    }

    public Id getOwnerId() {
        return ownerId;
    }

    public DefaultPlan getPlan() {
        return plan;
    }

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    //--------------

    public Set<Id> getDriverCorrelationIds() {
        return drivers.keySet();
    }

    public boolean checkDriverCorrelationIdExists(Id id) {
        return drivers.containsKey(id);
    }

    public Driver getDriver(Id driverId) {
        return drivers.get(driverId);
    }

    public boolean isRecordDriverSequence() {
        return recordDriverSequence;
    }

    public DefaultMergedSequenceStats getMergedSequenceStats() {
        return optionalMergedSequenceStats;
    }

    //--------------

    /**
     * Starts the {@link TimeSource}, registers all the {@link Source}s and starts all the {@link
     * Driver}s.
     *
     * @throws LifecycleException
     */
    public void start() throws LifecycleException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Starting driver owner [" + ownerId + "]");
        }

        //-----------

        InternalTimeSource timeSource = plan.getTimeSource();
        timeSource.start();

        //-----------

        for (D d : drivers.values()) {
            d.lock();
            try {
                d.start();
            }
            finally {
                d.unlock();
            }
        }

        //-----------

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Started driver owner [" + ownerId + "]");
        }
    }

    /**
     * Does the reverse of what is done in {@link #start()}.
     */
    public void stop() throws LifecycleException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Stopping driver owner [" + ownerId + "]");
        }

        //-----------

        for (D d : drivers.values()) {
            d.lock();
            try {
                d.stop();
            }
            finally {
                d.unlock();
            }
        }

        //-----------

        InternalTimeSource timeSource = plan.getTimeSource();
        timeSource.stop();

        //-----------

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Stopped driver owner [" + ownerId + "]");
        }
    }

    //--------------

    public Response onInput(Source source, Id driverCorrelationId, Input input) {
        //Keep trying until a valid driver is found.
        for (; ;) {
            D driver = drivers.get(driverCorrelationId);

            if (driver == null) {
                try {
                    driver = driverNotFound(driverCorrelationId, input);
                }
                catch (Exception e) {
                    return new DriverNotFound(input, null,
                            new Exception("An error occurred while handling missing Driver.", e));
                }
            }

            //------------

            driver.lock();
            try {
                if (driver.isValid()) {
                    return handleInput(driver, input, source);
                }
            }
            finally {
                driver.unlock();
            }
        }
    }

    public Response onTimeOut(Id driverCorrelationId, TimeInput input) {
        //Keep trying until a valid driver is found.
        for (; ;) {
            D driver = drivers.get(driverCorrelationId);

            if (driver == null) {
                return Flags.DEBUG ?
                        new DriverNotFound(input, null,
                                new Exception("Input has arrived too late. Driver not found:" +
                                        " [" + driverCorrelationId + "]")) :
                        new DriverNotFound(input, null, null);
            }

            //------------

            driver.lock();
            try {
                if (driver.isValid()) {
                    Id currentInstanceId = driver.getDriverInstanceId();
                    Id expectedInstanceId = input.getDriverInstanceId();

                    if (currentInstanceId.equals(expectedInstanceId)) {
                        return handleInput(driver, input, null);
                    }

                    return Flags.DEBUG ?
                            new IgnoredTimeOut(input, null,
                                    new Exception("Input has arrived too late." +
                                            " Found driver with correlation Id [" +
                                            driverCorrelationId +
                                            "] and instance Id [" + currentInstanceId +
                                            "], but the input was for the driver instance Id [" +
                                            expectedInstanceId + "]")) :
                            new IgnoredTimeOut(input, null, null);
                }
            }
            finally {
                driver.unlock();
            }
        }
    }

    /**
     * @param driver
     * @param input
     * @param source If <code>null</code>, then the input has to be a {@link TimeInput}.
     * @return
     */
    protected Response handleInput(D driver, Input input, Source source) {
        Response response = null;

        if (source != null) {
            response = onDriverInputReceived(source, driver, input);
        }
        else {
            response = onDriverTimeOutReceived(driver, (TimeInput) input);
        }

        //------------

        if ((response instanceof Complete) || (response instanceof Failure)) {
            drivers.remove(driver.getDriverCorrelationId());
            this.threadLocalDriverCorrelationIds.get().remove(driver.getDriverCorrelationId());

            onDriverCompleted(driver, input);
        }

        return response;
    }

    private D driverNotFound(Id driverCorrelationId, Input input) throws Exception {
        D driver = onDriverMissing(driverCorrelationId, input);

        D existing = drivers.putIfAbsent(driverCorrelationId, driver);
        this.threadLocalDriverCorrelationIds.get().add(driverCorrelationId);
        if (existing != null) {
            //Discard our new local object.
            driver.stop();

            driver = existing;
        }

        return driver;
    }

    //--------------

    /**
     * @param driverCorrelationId
     * @param input
     * @return A new driver or throw appropriate exception.
     * @throws Exception
     */
    protected abstract D onDriverMissing(Id driverCorrelationId, Input input) throws Exception;

    protected abstract Response onDriverInputReceived(Source source, D driver, Input input);

    protected abstract Response onDriverTimeOutReceived(D driver, TimeInput input);

    protected abstract void onDriverCompleted(D driver, Input input);

    //--------------

    public AbstractDriverOwner<D> recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        if (LOGGER == null) {
            LOGGER = $logger(resourceProvider, getClass());
        }

        this.plan = this.plan.recover(resourceProvider);
        this.resourceProvider = resourceProvider;

        for (Entry<Id, D> entry : drivers.entrySet()) {
            D d = entry.getValue();

            d = (D) d.recover(resourceProvider);

            entry.setValue(d);
        }

        if (optionalDriverCallbackCreator != null) {
            optionalDriverCallbackCreator =
                    optionalDriverCallbackCreator.recover(resourceProvider, params);
        }

        if (optionalTransitionGuardSetCreator != null) {
            optionalTransitionGuardSetCreator =
                    optionalTransitionGuardSetCreator.recover(resourceProvider, params);
        }

        if (recordDriverSequence) {
            optionalMergedSequenceStats = new DefaultMergedSequenceStats(ownerId);
        }

        return this;
    }
}
