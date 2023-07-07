package com.tibco.cep.pattern.matcher.impl.dsl;

import java.util.List;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.impl.common.resource.DefaultResourceProvider;
import com.tibco.cep.impl.service.DefaultAsyncScheduler;
import com.tibco.cep.pattern.matcher.impl.master.AbstractDriverOwner;
import com.tibco.cep.pattern.matcher.impl.master.AbstractDriverOwnerSettings;
import com.tibco.cep.pattern.matcher.impl.master.DefaultEndSource;
import com.tibco.cep.pattern.matcher.impl.master.DefaultPlan;
import com.tibco.cep.pattern.matcher.impl.master.StandardTimeSource;
import com.tibco.cep.pattern.matcher.impl.master.SyncDriverOwner;
import com.tibco.cep.pattern.matcher.impl.model.AbstractNode;
import com.tibco.cep.pattern.matcher.impl.model.DefaultEnd;
import com.tibco.cep.pattern.matcher.impl.model.DefaultEndInputDef;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInputDef;
import com.tibco.cep.pattern.matcher.impl.model.DefaultStart;
import com.tibco.cep.pattern.matcher.impl.model.DefaultTimeInputDef;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.service.AsyncScheduler;

/*
* Author: Ashwin Jayaprakash Date: Jul 23, 2009 Time: 1:02:51 PM
*/

public class DefaultPatternDeploymentDef extends AbstractPatternDeploymentDef {
    protected DefaultResourceProvider resourceProvider;

    protected AsyncScheduler defaultScheduler;

    protected DefaultStart startNode;

    protected DefaultEnd endNode;

    protected DefaultEndSource endSource;

    public DefaultPatternDeploymentDef(Id id, DefaultResourceProvider resourceProvider,
                                       AsyncScheduler defaultScheduler) {
        super(id);

        this.resourceProvider = resourceProvider;
        this.defaultScheduler = defaultScheduler;

        this.startNode = new DefaultStart();

        this.endSource = new DefaultEndSource(new DefaultId(id, "End"));
        DefaultEndInputDef endInputDef = new DefaultEndInputDef();
        endInputDef.setSource(this.endSource);

        this.endNode = new DefaultEnd();
        this.endNode.setInputDef(endInputDef);
    }

    /**
     * Just creates a driver-owner.
     *
     * @return
     * @throws LifecycleException
     */
    public AbstractDriverOwner build() throws LifecycleException {
        //Decide on a dedicated time-source or not.

        StandardTimeSource standardTimeSource = new StandardTimeSource();
        standardTimeSource.setResourceProvider(resourceProvider);
        standardTimeSource.setDriverOwnerId(id);
        Id timeSourceId = new DefaultId(id, "TimeSource");
        standardTimeSource.setResourceId(timeSourceId);

        boolean dedicatedTimer = usesDedicatedTimer();
        if (dedicatedTimer) {
            DefaultAsyncScheduler scheduler = new DefaultAsyncScheduler();
            scheduler.setMaxThreads(getDedicatedTimerMaxThreads());
            Id schedulerId = new DefaultId(timeSourceId, "Scheduler");
            scheduler.setResourceId(schedulerId);
            scheduler.setThreadFamilyName(schedulerId.toString());

            standardTimeSource.setSchedulerDedicated(true);
            standardTimeSource.setScheduler(scheduler);
        }
        else {
            standardTimeSource.setSchedulerDedicated(false);
            standardTimeSource.setScheduler(defaultScheduler);
        }

        //-----------------

        //Replace all the time-sources with 1 instance common to the pattern.

        List<DefaultTimeInputDef> timeInputDefs = finalPattern.getTimeInputs();
        if (timeInputDefs != null) {
            for (DefaultTimeInputDef timeInputDef : timeInputDefs) {
                timeInputDef.setSource(standardTimeSource);
            }
        }

        //-----------------

        //Register all non-time-sources.

        DefaultInputDef[] inputDefs = getInputs();
        Source[] sources = new Source[inputDefs.length];

        for (int i = 0; i < inputDefs.length; i++) {
            sources[i] = inputDefs[i].getSource();
        }

        //-----------------

        //Create a plan and a driver-owner.

        DefaultPlan plan = new DefaultPlan();
        plan.setSources(sources);
        plan.setTimeSource(standardTimeSource);
        plan.setEndSource(endSource);
        plan.setCopyableFlow(startNode);

        AbstractDriverOwnerSettings settings = new AbstractDriverOwnerSettings();
        settings.setOwnerId(id);
        settings.setPlan(plan);
        settings.setOptionalDriverCallbackCreator(driverCallbackCreator);
        settings.setOptionalTransitionGuardSetCreator(transitionGuardSetCreator);
        settings.setOptionalRecordDriverSequence(captureSequence);

        SyncDriverOwner driverOwner = new SyncDriverOwner(resourceProvider, settings);

        //-----------------

        //Change the structure (add/remove) after all registrations are successful.

        attachStartAndEndNodes();

        //-----------------

        return driverOwner;
    }

    private void attachStartAndEndNodes() {
        AbstractNode[] firstNodes = finalPattern.getFirstNodes();
        startNode.addNext(firstNodes);

        AbstractNode[] lastNodes = finalPattern.getLastNodes();
        for (AbstractNode lastNode : lastNodes) {
            lastNode.addNext(endNode);
        }
    }
}
