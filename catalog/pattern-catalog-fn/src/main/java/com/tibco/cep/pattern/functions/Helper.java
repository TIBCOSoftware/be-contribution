package com.tibco.cep.pattern.functions;

import static com.tibco.cep.pattern.impl.util.Helper.$logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.integ.impl.master.RuleSessionItems;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;
import com.tibco.cep.util.annotation.LogCategory;

/*
* Author: Ashwin Jayaprakash Date: Sep 22, 2009 Time: 5:22:56 PM
*/

@LogCategory("pattern.be.catalog.function")
public abstract class Helper {
    protected static Logger logger;

    protected static ConcurrentHashMap<RuleSession, RuleSessionItems> ruleSessionItems =
            new ConcurrentHashMap<RuleSession, RuleSessionItems>();

    public static RuleSessionItems getCurrentThreadRSI() {
        RuleSession rs = RuleSessionManager.getCurrentRuleSession();
        if (rs != null) {
            return ruleSessionItems.get(rs);
        }

        throw new RuntimeException("There is no RuleSession associated with the current Thread.");
    }

    public static void assertRSI(RuleSessionItems rsi) {
        if (rsi == null || rsi.isValid() == false) {
            throw new RuntimeException(
                    "The is no valid RuleSession(Items) associated with the current Thread.");
        }
    }

    /**
     * Also initializes {@link #getLogger()}.
     *
     * @param rsi
     */
    public static void addRSI(RuleSessionItems rsi) {
        ruleSessionItems.put(rsi.getRuleSession(), rsi);

        if (logger == null) {
            ResourceProvider resourceProvider = rsi.getAdmin().getResourceProvider();

            logger = $logger(resourceProvider, Helper.class);
        }
    }

    public static void removeRSI(RuleSessionItems rsi) {
        ruleSessionItems.remove(rsi.getRuleSession());
    }

    /**
     * @return Should be invoked only after {@link #addRSI(RuleSessionItems)} has been called by
     *         someone at least once.
     */
    public static Logger getLogger() {
        if (logger == null) {
            Logger anon = Logger.getAnonymousLogger();

            anon.log(Level.SEVERE,
                    "Logger has been accessed without proper initialization. Using anonymous logger.");

            return anon;
        }

        return logger;
    }
}
