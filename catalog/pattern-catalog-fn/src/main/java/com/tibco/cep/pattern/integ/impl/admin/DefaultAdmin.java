package com.tibco.cep.pattern.integ.impl.admin;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.log.LoggerService;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.impl.common.log.DefaultLoggerService;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.impl.common.resource.DefaultResourceProvider;
import com.tibco.cep.impl.service.DefaultAsyncExecutor;
import com.tibco.cep.impl.service.DefaultAsyncScheduler;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.admin.Session;
import com.tibco.cep.pattern.integ.impl.jmx.MBeanManager;
import com.tibco.cep.pattern.matcher.impl.admin.DefaultMatcherAdmin;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultPatternDeploymentDef;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.subscriber.impl.admin.DefaultAdvancedSubscriberAdmin;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionDeploymentDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.pattern.subscriber.master.EventSource;
import com.tibco.cep.pattern.subscriber.master.Router;
import com.tibco.cep.pattern.subscriber.master.SubscriptionCaretaker;
import com.tibco.cep.service.AsyncExecutor;
import com.tibco.cep.service.AsyncScheduler;
import com.tibco.cep.util.annotation.LogCategory;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Aug 24, 2009 Time: 3:24:00 PM
*/

@LogCategory("pattern.core.admin")
public class DefaultAdmin implements Admin<DefaultSession> {
    protected DefaultResourceProvider resourceProvider;

    protected DefaultAdvancedSubscriberAdmin subscriberAdmin;

    protected DefaultMatcherAdmin matcherAdmin;

    protected MBeanManager mbeanManager;

    @Optional
    protected LoggerService loggerServiceWeCreated;

    protected static Logger LOGGER;

    public DefaultAdmin(DefaultResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        LoggerService loggerService = resourceProvider.fetchResource(LoggerService.class);
        if (loggerService == null) {
            loggerService = new DefaultLoggerService();
            resourceProvider.registerResource(LoggerService.class, loggerService);

            loggerServiceWeCreated = loggerService;
        }

        this.subscriberAdmin = new DefaultAdvancedSubscriberAdmin(this.resourceProvider);
        this.matcherAdmin = new DefaultMatcherAdmin(this.resourceProvider);

        this.mbeanManager = new MBeanManager();
    }

    public DefaultAdmin() {
        this(new DefaultResourceProvider());
    }

    public void start() throws LifecycleException {
        if (loggerServiceWeCreated != null) {
            loggerServiceWeCreated.start();
        }

        LoggerService loggerService = resourceProvider.fetchResource(LoggerService.class);
        LOGGER = loggerService.getLogger(getClass());

        LOGGER.log(Level.INFO, "Starting pattern services");

        //---------------

        if (Flags.DEBUG) {
            String s = "" +
                    "\n+==========================================================+" +
                    "\n|             Pattern running in DEBUG/DEV mode!           |" +
                    "\n+==========================================================+";

            System.out.println(s);

            LOGGER.log(Level.SEVERE, s);
        }

        //---------------

        startAsyncServices();

        //---------------

        matcherAdmin.start();
        LOGGER.log(Level.INFO, "Started matcher service");

        try {
            subscriberAdmin.start();
            LOGGER.log(Level.INFO, "Started subscriber service");
        }
        catch (LifecycleException e) {
            try {
                matcherAdmin.stop();
            }
            catch (LifecycleException e1) {
                //Ignore.
            }

            try {
                stopAsyncServices();
            }
            catch (LifecycleException e1) {
                //Ignore.
            }

            throw e;
        }

        //---------------

        try {
            mbeanManager.init(this);
            LOGGER.log(Level.INFO, "Started JMX service");
        }
        catch (Exception e) {
            throw new LifecycleException("Error occurred during JMX service initialization", e);
        }

        //---------------

        LOGGER.log(Level.INFO, "Started pattern services");
    }

    public void stop() throws LifecycleException {
        LOGGER.log(Level.INFO, "Stopping pattern services");

        try {
            mbeanManager.discard();
            LOGGER.log(Level.INFO, "Stopped JMX service");
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error occurred while stopping JMX service");
        }

        subscriberAdmin.stop();
        LOGGER.log(Level.INFO, "Stopped subscriber service");

        matcherAdmin.stop();
        LOGGER.log(Level.INFO, "Stopped matcher service");

        stopAsyncServices();

        LOGGER.log(Level.INFO, "Stopped pattern services");

        if (loggerServiceWeCreated != null) {
            loggerServiceWeCreated.stop();
        }
    }

    //---------------

    public DefaultResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    //---------------

    public DefaultSession create(Id id) {
        return new DefaultSession(resourceProvider, subscriberAdmin, matcherAdmin, id);
    }

    public void deploy(DefaultSession session) throws LifecycleException {
        try {
            doDeploy(session);
        }
        finally {
            try {
                session.discard();
            }
            catch (Exception e) {
                LOGGER.log(Level.INFO,
                        "Discard operation failed for session [" + session.getId() + "]");
            }
        }
    }

    protected void doDeploy(DefaultSession session) throws LifecycleException {
        session.prepare();

        try {
            new SessionValidator(session).validate();
        }
        catch (Exception e) {
            throw new LifecycleException("Error occurred in the validation phase", e);
        }

        //---------------

        DefaultPatternDeploymentDef patternDeployment = session.getPatternDeployment();

        DriverOwner driverOwner = matcherAdmin.deploy(patternDeployment);

        //---------------

        /*
        Matcher must be registered first because the subscription-listeners try to look up the
        driver-owner.
        */
        DefaultSubscriptionDeploymentDef subscriptionDeployment =
                session.getSubscriptionDeployment();

        try {
            SubscriptionCaretaker subscriptionCaretaker =
                    subscriberAdmin.deploy(subscriptionDeployment);
        }
        catch (Throwable t) {
            try {
                //Cleanup.
                matcherAdmin.undeploy(driverOwner);
            }
            catch (Exception e1) {
                LOGGER.log(Level.INFO,
                        "Matcher cleanup operation failed for session [" + session.getId() + "]");
            }

            if (t instanceof LifecycleException) {
                throw (LifecycleException) t;
            }

            throw new LifecycleException(t);
        }
    }

    /**
     * @param sessionId From {@link Session#getId()}.
     * @throws LifecycleException
     */
    public void undeploy(Id sessionId) throws LifecycleException {
        SubscriptionCaretaker subscriptionCaretaker = subscriberAdmin.getCaretaker(sessionId);
        if (subscriptionCaretaker == null) {
            return;
        }

        subscriberAdmin.undeploy(subscriptionCaretaker);

        //---------------

        DriverOwner driverOwner = matcherAdmin.getDriverOwner(sessionId);
        if (driverOwner == null) {
            return;
        }

        matcherAdmin.undeploy(driverOwner);
    }

    //---------------

    public Router getRouter() {
        return subscriberAdmin.getRouter();
    }

    public Collection<? extends EventSource> getEventSources() {
        return subscriberAdmin.getEventSources();
    }

    public EventDescriptor getEventDescriptor(Id id) {
        return subscriberAdmin.getEventDescriptor(id);
    }

    //---------------

    public Collection<? extends EventDescriptor> getEventDescriptors() {
        return subscriberAdmin.getEventDescriptors();
    }

    public void deploy(EventSource eventSource) throws LifecycleException {
        subscriberAdmin.deploy(eventSource);
    }

    public void undeploy(EventSource eventSource) throws LifecycleException {
        subscriberAdmin.undeploy(eventSource);
    }

    public void register(EventDescriptor eventDescriptor) throws LifecycleException {
        subscriberAdmin.register(eventDescriptor);
    }

    public void unregister(EventDescriptor eventDescriptor) throws LifecycleException {
        subscriberAdmin.unregister(eventDescriptor);
    }

    //---------------

    public DriverOwner getDriverOwner(Id id) {
        return matcherAdmin.getDriverOwner(id);
    }

    public Collection<? extends DriverOwner> getDriverOwners() {
        return matcherAdmin.getDriverOwners();
    }

    //---------------

    public void setMaxExecutorThreads(int numThreads) {
        AsyncExecutor executor = resourceProvider.fetchResource(AsyncExecutor.class);
        executor.setMaxThreads(numThreads);
    }

    public int getMaxExecutorThreads() {
        AsyncExecutor executor = resourceProvider.fetchResource(AsyncExecutor.class);
        return executor.getMaxThreads();
    }

    public void setMaxSchedulerThreads(int numThreads) {
        AsyncScheduler scheduler = resourceProvider.fetchResource(AsyncScheduler.class);
        scheduler.setMaxThreads(numThreads);
    }

    public int getMaxSchedulerThreads() {
        AsyncScheduler scheduler = resourceProvider.fetchResource(AsyncScheduler.class);
        return scheduler.getMaxThreads();
    }

    protected void startAsyncServices() {
        {
            DefaultAsyncScheduler defaultScheduler = new DefaultAsyncScheduler();

            String poolName = "pattern.core.scheduler.threadpool";

            Id schedulerId = new DefaultId(poolName);
            defaultScheduler.setResourceId(schedulerId);

            int numThreads = Runtime.getRuntime().availableProcessors();
            numThreads = Math.max(4, numThreads / 3);
            defaultScheduler.setMaxThreads(numThreads);

            defaultScheduler.setThreadFamilyName(schedulerId.toString());

            defaultScheduler.start();

            resourceProvider.registerResource(AsyncScheduler.class, defaultScheduler);

            LOGGER.log(Level.INFO,
                    "Started " + poolName + " with [" + numThreads + "] max threads");
        }

        //---------------

        {
            DefaultAsyncExecutor defaultExecutor = new DefaultAsyncExecutor();

            String poolName = "pattern.core.executor.threadpool";

            Id executorId = new DefaultId(poolName);
            defaultExecutor.setResourceId(executorId);

            int numThreads = Runtime.getRuntime().availableProcessors();
            numThreads = Math.max(4, numThreads / 3);
            defaultExecutor.setMaxThreads(numThreads);

            defaultExecutor.setThreadFamilyName(executorId.toString());

            defaultExecutor.start();

            resourceProvider.registerResource(AsyncExecutor.class, defaultExecutor);

            LOGGER.log(Level.INFO,
                    "Started " + poolName + " with [" + numThreads + "] max threads");
        }
    }

    protected void stopAsyncServices() throws LifecycleException {
        AsyncExecutor asyncExecutor = resourceProvider.fetchResource(AsyncExecutor.class);
        if (asyncExecutor != null) {
            asyncExecutor.stop();
            resourceProvider.unregisterResource(AsyncExecutor.class);
        }

        LOGGER.log(Level.INFO, "Stopped scheduler thread pool");

        //---------------

        AsyncScheduler asyncScheduler = resourceProvider.fetchResource(AsyncScheduler.class);
        if (asyncScheduler != null) {
            asyncScheduler.stop();
            resourceProvider.unregisterResource(AsyncScheduler.class);
        }

        LOGGER.log(Level.INFO, "Stopped executor thread pool");
    }
}
