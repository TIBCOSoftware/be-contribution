package com.tibco.cep.pattern.functions.service;

import static com.tibco.be.model.functions.FunctionDomain.*;
import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.impl.master.DefaultMaster;
import com.tibco.cep.pattern.integ.impl.master.PatternRegistry;
import com.tibco.cep.pattern.integ.impl.master.RuleSessionItems;
import com.tibco.cep.pattern.integ.master.OntologyService;
import com.tibco.cep.pattern.subscriber.master.Router;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;

import static com.tibco.cep.pattern.functions.Helper.*;

/*
* Author: Ashwin Jayaprakash / Date: Dec 15, 2009 / Time: 2:35:09 PM
*/

@com.tibco.be.model.functions.BEPackage(
		catalog = "CEP Pattern",
        category = "Pattern.Service",
        synopsis = "Pattern service functions")
public abstract class ServiceFunctions {
    @com.tibco.be.model.functions.BEFunction(
        name = "start",
        synopsis = "Starts the Pattern Matcher service.",
        signature = "void start()",
        params = {
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Starts the Pattern Matcher service.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public synchronized static void start() {
        RuleSessionItems rsi = getCurrentThreadRSI();
        if (rsi != null) {
            return;
        }

        //-------------

        RuleSession rs = RuleSessionManager.getCurrentRuleSession();
        RuleServiceProvider rsp = rs.getRuleServiceProvider();

        DefaultMaster master = new DefaultMaster();
        try {
            master.start(rsp);
        }
        catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

        //-------------

        Admin admin = master.getAdmin();
        ResourceProvider resourceProvider = admin.getResourceProvider();
        OntologyService ontologyService = resourceProvider.fetchResource(OntologyService.class);
        Router router = resourceProvider.fetchResource(Router.class);

        rsi = new RuleSessionItems(rs, master, admin, ontologyService, router, new PatternRegistry());
        addRSI(rsi);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "stop",
        synopsis = "Stops the Pattern Matcher service.",
        signature = "void stop()",
        params = {
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Stops the Pattern Matcher service.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public synchronized static void stop() {
        RuleSessionItems rsi = getCurrentThreadRSI();
        if (rsi == null) {
            return;
        }

        //-------------

        removeRSI(rsi);

        try {
            rsi.discard();
        }
        catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
