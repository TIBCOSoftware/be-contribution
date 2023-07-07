package com.tibco.cep.pattern.integ.impl.admin;

import java.util.HashMap;
import java.util.List;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultPatternDef;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInputDef;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.GroupBoundaryStart;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionDef;
import com.tibco.cep.pattern.subscriber.impl.dsl.DefaultSubscriptionItemDef;

/*
* Author: Ashwin Jayaprakash / Date: 12/20/10 / Time: 2:25 PM
*/
public class SessionValidator {
    protected DefaultSession session;

    protected DefaultPatternDef patternDef;

    protected DefaultSubscriptionDef subscriptionDef;

    protected HashMap<Id, DefaultSubscriptionItemDef> exactMatchSourceIdAndItemDefs;

    public SessionValidator(DefaultSession session) {
        this.session = session;

        this.patternDef = session.getPatternDeployment().getPattern();
        this.subscriptionDef = session.getSubscriptionDeployment().getSubscription();

        HashMap<String, DefaultInputDef<Source>> inputDefs = session.getAliasesAndInputDefs();

        this.exactMatchSourceIdAndItemDefs = new HashMap<Id, DefaultSubscriptionItemDef>();
        for (DefaultSubscriptionItemDef subscriptionItemDef : this.subscriptionDef.getSubscriptionItems()) {
            if (subscriptionItemDef.isPropertyValueSet()) {
                String alias = subscriptionItemDef.getName();

                Source source = inputDefs.get(alias).getSource();

                this.exactMatchSourceIdAndItemDefs.put(source.getResourceId(), subscriptionItemDef);
            }
        }
    }

    public void validate() throws Exception {
        insureFirstNodesAreNotExactMatch();
    }

    protected void insureFirstNodesAreNotExactMatch() {
        if (exactMatchSourceIdAndItemDefs.isEmpty()) {
            //Nothing to do.
            return;
        }

        Node[] firstNodes = patternDef.getFirstNodes();
        for (Node firstNode : firstNodes) {
            recursiveCheckNotExactMatch(firstNode);
        }
    }

    private void recursiveCheckNotExactMatch(Node node) {
        InputDef inputDef = node.getInputDef();

        if (inputDef != null) {
            Source source = inputDef.getSource();

            DefaultSubscriptionItemDef itemDef = exactMatchSourceIdAndItemDefs.get(source.getResourceId());
            if (itemDef != null) {
                String msg = String.format(
                        "The exact-match subscription [%s:%s] cannot be used as the first item in the pattern sequence." +
                                " Only correlation subscriptions are allowed as the first event",
                        itemDef.getName(), itemDef.getPropertyName());

                throw new RuntimeException(msg);
            }
        }
        else if (node instanceof GroupBoundaryStart) {
            GroupBoundaryStart boundaryStart = (GroupBoundaryStart) node;

            List<? extends GroupBoundaryStart> children = boundaryStart.getChildGroups();
            if (children != null) {
                for (GroupBoundaryStart child : children) {
                    recursiveCheckNotExactMatch(child);
                }
            }
            else {
                Node[] firstNodes = node.getNext();

                if (firstNodes != null) {
                    for (Node firstNode : firstNodes) {
                        recursiveCheckNotExactMatch(firstNode);
                    }
                }
            }
        }
    }
}
