package com.tibco.cep.pattern.integ.impl.admin;

import java.util.HashMap;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.impl.common.resource.DefaultResourceProvider;
import com.tibco.cep.pattern.integ.admin.Session;
import com.tibco.cep.pattern.integ.dsl.PatternSubscriptionDef;
import com.tibco.cep.pattern.integ.impl.dsl.DefaultPatternSubscriptionDef;
import com.tibco.cep.pattern.integ.impl.master.DefaultSourceBridge;
import com.tibco.cep.pattern.matcher.dsl.InputDefLB;
import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.dsl.PatternDefLB;
import com.tibco.cep.pattern.matcher.impl.admin.DefaultMatcherAdmin;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultPatternDef;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultPatternDeploymentDef;
import com.tibco.cep.pattern.matcher.impl.model.AbstractNode;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInputDef;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TransitionGuardSetCreator;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.subscriber.impl.admin.DefaultAdvancedSubscriberAdmin;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionDef;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionDeploymentDef;

/*
* Author: Ashwin Jayaprakash Date: Aug 24, 2009 Time: 3:24:20 PM
*/
public class DefaultSession implements Session<DefaultPatternDef, DefaultInputDef, AbstractNode> {
    protected DefaultResourceProvider resourceProvider;

    protected DefaultAdvancedSubscriberAdmin subscriberAdmin;

    protected DefaultMatcherAdmin matcherAdmin;

    protected Id id;

    //--------------

    protected DefaultSubscriptionDeploymentDef subscriptionDeployment;

    protected DefaultPatternSubscriptionDef subscription;

    protected HashMap<String, DefaultSourceBridge> aliasesAndSourceBridges;

    //--------------

    protected HashMap<String, DefaultInputDef<Source>> aliasesAndInputDefs;

    protected DefaultPatternDeploymentDef patternDeployment;

    public DefaultSession(DefaultResourceProvider resourceProvider,
                          DefaultAdvancedSubscriberAdmin subscriberAdmin,
                          DefaultMatcherAdmin matcherAdmin, Id id) {
        this.resourceProvider = resourceProvider;
        this.subscriberAdmin = subscriberAdmin;
        this.matcherAdmin = matcherAdmin;
        this.id = id;

        //--------------

        this.subscriptionDeployment = this.subscriberAdmin.createDeployment(id);

        DefaultSubscriptionDef actualSubscription = this.subscriptionDeployment.create();
        this.subscription =
                new DefaultPatternSubscriptionDef(resourceProvider, id, actualSubscription);

        this.aliasesAndSourceBridges = new HashMap<String, DefaultSourceBridge>(4);

        //--------------

        this.patternDeployment = this.matcherAdmin.createDeployment(id);
        this.aliasesAndInputDefs = new HashMap<String, DefaultInputDef<Source>>(4);
    }

    //--------------

    public Id getId() {
        return id;
    }

    public PatternSubscriptionDef definePatternSubscription() {
        return subscription;
    }

    public HashMap<String, DefaultInputDef<Source>> getAliasesAndInputDefs() {
        return aliasesAndInputDefs;
    }

    public HashMap<String, DefaultSourceBridge> getAliasesAndSourceBridges() {
        return aliasesAndSourceBridges;
    }

    private void populateAliasesAndSourceBridges() {
        HashMap<Id, DefaultSourceBridge> sourceBridges = subscription.getSources();

        //Already updated everything.
        if (aliasesAndSourceBridges.size() == sourceBridges.size()) {
            return;
        }

        //-------------

        for (DefaultSourceBridge sourceBridge : sourceBridges.values()) {
            String alias = sourceBridge.getAlias();

            aliasesAndSourceBridges.put(alias, sourceBridge);
        }

        //-------------

        if (aliasesAndSourceBridges.isEmpty()) {
            throw new IllegalArgumentException("At least 1 valid Subscription must be defined.");
        }
    }

    public InputDefLB<DefaultInputDef> createList(DefaultInputDef firstInput) {
        return patternDeployment.list(firstInput);
    }

    public PatternDefLB<DefaultPatternDef> createList(DefaultPatternDef firstPattern) {
        return patternDeployment.list(firstPattern);
    }

    public InputDef defineInput(String alias) {
        DefaultInputDef<Source> input = aliasesAndInputDefs.get(alias);
        if (input != null) {
            return input;
        }

        //-------------

        DefaultSourceBridge sourceBridge = aliasesAndSourceBridges.get(alias);
        if (sourceBridge == null) {
            populateAliasesAndSourceBridges();

            sourceBridge = aliasesAndSourceBridges.get(alias);
        }

        //-------------

        input = new DefaultInputDef<Source>();
        input.setSource(sourceBridge);

        aliasesAndInputDefs.put(alias, input);

        //-------------

        patternDeployment.addInput(input);

        //-------------

        return input;
    }

    public DefaultPatternDef createPattern() {
        return patternDeployment.createPattern();
    }

    public void setPattern(PatternDef mainPattern) {
        DefaultPatternDef patternDef = (DefaultPatternDef) mainPattern;

        patternDeployment.setPattern(patternDef);
    }

    public void setDriverCallbackCreator(DriverCallbackCreator callbackCreator) {
        patternDeployment.useDriverCallbackCreator(callbackCreator);
    }

    public void setTransitionGuardSetCreator(TransitionGuardSetCreator guardSetCreator) {
        patternDeployment.useTransitionGuardSetCreator(guardSetCreator);
    }

    public void captureSequence() {
        patternDeployment.captureSequence();
    }

    //--------------

    public DefaultSubscriptionDeploymentDef getSubscriptionDeployment() {
        return subscriptionDeployment;
    }

    public DefaultPatternDeploymentDef getPatternDeployment() {
        return patternDeployment;
    }

    //-------------

    public void prepare() {
        DefaultSubscriptionDef actualSubscription = subscription.getSubscription();
        subscriptionDeployment.setSubscription(actualSubscription);
    }

    public void discard() {
        if (aliasesAndSourceBridges != null) {
            aliasesAndSourceBridges.clear();
        }

        if (aliasesAndInputDefs != null) {
            aliasesAndInputDefs.clear();
        }
    }
}
