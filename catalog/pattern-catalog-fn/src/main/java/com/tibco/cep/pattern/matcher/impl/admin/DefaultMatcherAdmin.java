package com.tibco.cep.pattern.matcher.impl.admin;

import java.util.Collection;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.log.LoggerService;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.log.DefaultLoggerService;
import com.tibco.cep.impl.common.resource.DefaultResourceProvider;
import com.tibco.cep.impl.service.DefaultClock;
import com.tibco.cep.pattern.matcher.admin.MatcherAdmin;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultPatternDeploymentDef;
import com.tibco.cep.pattern.matcher.impl.master.AbstractDriverOwner;
import com.tibco.cep.pattern.matcher.impl.master.DefaultDriverOwnerRegistry;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.matcher.master.DriverOwnerRegistry;
import com.tibco.cep.service.AsyncScheduler;
import com.tibco.cep.service.Clock;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Jul 22, 2009 Time: 3:49:13 PM
*/
public class DefaultMatcherAdmin implements MatcherAdmin<DefaultPatternDeploymentDef> {
    protected DefaultResourceProvider resourceProvider;

    protected DefaultDriverOwnerRegistry driverOwnerRegistry;

    protected AsyncScheduler defaultScheduler;

    protected DefaultClock clock;

    @Optional
    protected LoggerService loggerServiceWeCreated;

    public DefaultMatcherAdmin() {
        this.resourceProvider = new DefaultResourceProvider();
    }

    public DefaultMatcherAdmin(DefaultResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    //------------

    public void start() throws LifecycleException {
        LoggerService loggerService = resourceProvider.fetchResource(LoggerService.class);
        if (loggerService == null) {
            loggerService = new DefaultLoggerService();
            loggerService.start();

            resourceProvider.registerResource(LoggerService.class, loggerService);

            loggerServiceWeCreated = loggerService;
        }

        clock = new DefaultClock();
        clock.start();
        resourceProvider.registerResource(Clock.class, clock);

        defaultScheduler = resourceProvider.fetchResource(AsyncScheduler.class);

        driverOwnerRegistry = new DefaultDriverOwnerRegistry();
        driverOwnerRegistry.start();
        resourceProvider.registerResource(DriverOwnerRegistry.class, driverOwnerRegistry);
    }

    public void stop() throws LifecycleException {
        driverOwnerRegistry.stop();

        clock.stop();

        if (loggerServiceWeCreated != null) {
            loggerServiceWeCreated.stop();
        }
    }

    //-----------------

    public DefaultPatternDeploymentDef createDeployment(Id id) {
        return new DefaultPatternDeploymentDef(id, resourceProvider, defaultScheduler);
    }

    public DriverOwner deploy(DefaultPatternDeploymentDef patternDeployment)
            throws LifecycleException {
        AbstractDriverOwner driverOwner = patternDeployment.build();

        boolean success = driverOwnerRegistry.addDriverOwner(driverOwner);
        if (success == false) {
            throw new LifecycleException("The Pattern with Id [" + driverOwner.getOwnerId() +
                    "] cannot be used as it is already in use.");
        }

        return driverOwner;
    }

    public DriverOwner getDriverOwner(Id id) {
        return driverOwnerRegistry.getDriverOwner(id);
    }

    public Collection<? extends DriverOwner> getDriverOwners() {
        return driverOwnerRegistry.getDriverOwners();
    }

    public void undeploy(DriverOwner driverOwner) throws LifecycleException {
        Id driverOwnerId = driverOwner.getOwnerId();

        AbstractDriverOwner registeredDO = driverOwnerRegistry.getDriverOwner(driverOwnerId);
        if (registeredDO == null) {
            throw new LifecycleException("The DriverOwner [" + driverOwnerId +
                    "] is not registered and could not be undeployed.");
        }

        driverOwnerRegistry.removeDriverOwner(registeredDO);
    }

    //---------------

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }
}
