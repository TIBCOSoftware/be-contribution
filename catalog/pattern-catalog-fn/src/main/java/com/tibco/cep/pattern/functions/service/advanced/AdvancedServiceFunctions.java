package com.tibco.cep.pattern.functions.service.advanced;

import static com.tibco.be.model.functions.FunctionDomain.*;
import com.tibco.cep.pattern.functions.Helper;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.impl.master.RuleSessionItems;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tibco.cep.pattern.functions.Helper.assertRSI;
import static com.tibco.cep.pattern.functions.Helper.getCurrentThreadRSI;

/*
* Author: Ashwin Jayaprakash / Date: Dec 15, 2009 / Time: 2:43:37 PM
*/

@com.tibco.be.model.functions.BEPackage(
		catalog = "CEP Pattern",
        category = "Pattern.Service.Advanced",
        synopsis = "Advanced pattern service functions")
public abstract class AdvancedServiceFunctions {
    protected static Logger LOGGER;

    protected static Logger $logger() {
        if (LOGGER == null) {
            LOGGER = Helper.getLogger();
        }

        return LOGGER;
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setMaxExecutorThreads",
        synopsis = "Sets the maximum number of pooled Threads that the Service can create and use for pattern processing.",
        signature = "void setMaxExecutorThreads(int numThreads)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "numThreads", type = "int", desc = "The maximum number of Threads to be used by the Pattern Service.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sets the maximum number of pooled Threads that the Service can create and use for pattern processing.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setMaxExecutorThreads(int numThreads) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        Admin admin = rsi.getAdmin();

        int old = admin.getMaxExecutorThreads();
        admin.setMaxExecutorThreads(numThreads);

        String s = "Set maximum Executor Threads to [" + numThreads + "]";
        if (old < numThreads) {
            s = s + ". Excess live Threads will be removed over time.";
        }
        $logger().log(Level.INFO, s);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getMaxExecutorThreads",
        synopsis = "Returns the maximum number of pooled Threads that the Service can create and use for pattern\nprocessing.",
        signature = "int getMaxExecutorThreads()",
        params = {
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "int", desc = "The maximum number of Threads that will be used by the Pattern Service."),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the maximum number of pooled Threads that the Service can create and use for pattern processing.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static int getMaxExecutorThreads() {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        Admin admin = rsi.getAdmin();
        return admin.getMaxExecutorThreads();
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setMaxSchedulerThreads",
        synopsis = "Sets the maximum number of pooled Threads that the Service can create and use for scheduling time\nbased tasks during pattern processing.",
        signature = "void setMaxSchedulerThreads(int numThreads)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "numThreads", type = "int", desc = "temporal/time based patterns.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sets the maximum number of pooled Threads that the Service can create and use for scheduling time based tasks during pattern processing.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setMaxSchedulerThreads(int numThreads) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        Admin admin = rsi.getAdmin();

        int old = admin.getMaxSchedulerThreads();
        admin.setMaxSchedulerThreads(numThreads);

        String s = "Set maximum Scheduler Threads to [" + numThreads + "]";
        if (old < numThreads) {
            s = s + ". Excess live Threads will be removed over time.";
        }
        $logger().log(Level.INFO, s);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getMaxSchedulerThreads",
        synopsis = "Returns the maximum number of pooled Threads that the Service can create and use for scheduling time\nbased tasks during pattern processing.",
        signature = "int getMaxSchedulerThreads()",
        params = {
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "int", desc = "The maximum number of Threads used by the Pattern Service for scheduling temporal/time based\npatterns."),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the maximum number of pooled Threads that the Service can create and use for scheduling time based tasks during pattern processing.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static int getMaxSchedulerThreads() {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        Admin admin = rsi.getAdmin();
        return admin.getMaxSchedulerThreads();
    }
}
