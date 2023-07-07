package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.impl.stats.DefaultSequenceStats;
import com.tibco.cep.pattern.matcher.master.DriverCallback;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TimeInput;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSet;
import com.tibco.cep.pattern.matcher.model.Start;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.pattern.matcher.trace.Sequence;

/*
* Author: Ashwin Jayaprakash Date: Jul 7, 2009 Time: 11:41:04 AM
*/
public class SyncDriverOwner extends AbstractDriverOwner<DefaultDriver> {
    /**
     * @param resourceProvider
     * @param settings
     */
    public SyncDriverOwner(ResourceProvider resourceProvider,
                           AbstractDriverOwnerSettings settings) {
        super(resourceProvider, settings);
    }

    //--------------

    protected DefaultDriver onDriverMissing(Id driverCorrelationId, Input input)
            throws Exception {
        Start start = plan.createNewFlow(resourceProvider);

        DriverCallback driverCallback = null;
        if (optionalDriverCallbackCreator != null) {
            driverCallback = optionalDriverCallbackCreator.create(resourceProvider, this);
        }

        TransitionGuardSet transitionGuardSet = null;
        if (optionalTransitionGuardSetCreator != null) {
            transitionGuardSet = optionalTransitionGuardSetCreator.create(resourceProvider, this);
        }

        DefaultDriverSettings settings = new DefaultDriverSettings();
        settings.setDriverId(driverCorrelationId);
        settings.setStart(start);
        settings.setTimeSource(plan.getTimeSource());
        settings.setEndSource(plan.getEndSource());
        settings.setRecordSequence(recordDriverSequence);
        settings.setOptionalDriverCallback(driverCallback);
        settings.setOptionalTransitionGuardSet(transitionGuardSet);

        DefaultDriver driver = new DefaultDriver(resourceProvider, settings);

        driver.start();

        return driver;
    }

    protected Response onDriverInputReceived(Source source, DefaultDriver driver, Input input) {
        return driver.onInput(source, input);
    }

    protected Response onDriverTimeOutReceived(DefaultDriver driver, TimeInput input) {
        return driver.onTimeOut(input);
    }

    protected void onDriverCompleted(DefaultDriver driver, Input input) {
        if (optionalMergedSequenceStats != null) {
            Sequence sequence = driver.getSequence();
            DefaultSequenceStats sequenceStats = new DefaultSequenceStats(sequence);

            optionalMergedSequenceStats.merge(sequenceStats);
        }

        driver.stop();
    }
}
