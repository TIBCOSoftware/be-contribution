package com.tibco.cep.pattern.integ.impl.master;

import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.master.OntologyService;
import com.tibco.cep.pattern.subscriber.master.Router;
import com.tibco.cep.runtime.session.RuleSession;

/*
* Author: Ashwin Jayaprakash Date: Sep 22, 2009 Time: 5:22:56 PM
*/

public class RuleSessionItems {
    protected volatile RuleSession ruleSession;

    protected DefaultMaster master;

    protected Admin admin;

    protected OntologyService ontologyService;

    protected Router router;

    protected PatternRegistry patternRegistry;

    public RuleSessionItems(RuleSession ruleSession, DefaultMaster master, Admin admin,
                            OntologyService ontologyService, Router router, PatternRegistry patternRegistry) {
        this.ruleSession = ruleSession;
        this.master = master;
        this.admin = admin;
        this.ontologyService = ontologyService;
        this.router = router;
        this.patternRegistry = patternRegistry;
    }

    public RuleSession getRuleSession() {
        return ruleSession;
    }

    public DefaultMaster getMaster() {
        return master;
    }

    public Admin getAdmin() {
        return admin;
    }

    public OntologyService getOntologyService() {
        return ontologyService;
    }

    public Router getRouter() {
        return router;
    }

    public PatternRegistry getPatternRegistry() {
        return patternRegistry;
    }

    /**
     * @return <code>true</code> if this instance has not been {@link #discard() discarded}.
     */
    public boolean isValid() {
        return (ruleSession != null);
    }

    public void discard() throws
            LifecycleException {
        try {
            master.stop();
            master = null;
        }
        finally {
            admin = null;
            ontologyService = null;
            router = null;

            ruleSession = null;
        }
    }
}
