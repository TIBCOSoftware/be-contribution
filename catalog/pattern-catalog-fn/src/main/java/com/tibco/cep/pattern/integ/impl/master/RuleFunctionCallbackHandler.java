package com.tibco.cep.pattern.integ.impl.master;

import static com.tibco.cep.pattern.impl.util.Helper.$logger;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.kernel.model.rule.RuleFunction;
import com.tibco.cep.kernel.model.rule.RuleFunction.ParameterDescriptor;
import com.tibco.cep.pattern.matcher.master.AdvancedDriverOwner;
import com.tibco.cep.pattern.matcher.master.DriverCallback;
import com.tibco.cep.pattern.matcher.master.DriverCallbackCreator;
import com.tibco.cep.pattern.matcher.master.DriverView;
import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TimeInput;
import com.tibco.cep.pattern.matcher.response.Complete;
import com.tibco.cep.pattern.matcher.response.Failure;
import com.tibco.cep.pattern.matcher.response.Response;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;
import com.tibco.cep.runtime.session.impl.RuleSessionImpl;
import com.tibco.cep.util.annotation.LogCategory;

/*
* Author: Ashwin Jayaprakash Date: Sep 28, 2009 Time: 11:47:04 AM
*/

@LogCategory("pattern.be.listener")
public abstract class RuleFunctionCallbackHandler {
    public static enum CallbackFunctionParameter {
        PATTERN_DEF_URI(String.class, "patternDefURI"),
        PATTERN_INSTANCE_NAME(String.class, "patternInstanceName"),
        CORRELATION_ID(Object.class, "correlationId"),
        CLOSURE(Object.class, "closure"),
        OPTIONAL_OPAQUE(Object.class, "advancedOpaque", false);

        private Class type;

        private String name;

        private boolean required;

        CallbackFunctionParameter(Class type, String name) {
            this(type, name, true);
        }

        CallbackFunctionParameter(Class type, String name, boolean required) {
            this.type = type;
            this.name = name;
            this.required = required;
        }

        public Class getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public boolean isRequired() {
            return required;
        }

        @Override
        public String toString() {
            return "(name: " + name
                    + ", type: " + type.getSimpleName()
                    + ", mandatory: " + required
                    + ")";
        }
    }

    public static boolean validateAndCheckAllParamsNeeded(RuleFunction rf) {
        ParameterDescriptor[] pds = rf.getParameterDescriptors();
        //Ignore the last return type;
        int actualPDLength = pds.length - 1;
        CallbackFunctionParameter[] parameters = CallbackFunctionParameter.values();

        //All parameters or at least the mandatory parameters.
        if ((actualPDLength < (parameters.length - 1)) ||
                (actualPDLength > parameters.length)) {
            throw new IllegalArgumentException(
                    "The parameters of the RuleFunction [" + rf.getSignature() +
                            "] are invalid. Expected parameters " + Arrays.asList(parameters));
        }

        for (int i = 0; i < actualPDLength; i++) {
            CallbackFunctionParameter parameter = parameters[i];
            ParameterDescriptor pd = pds[i];

            Class requiredType = parameter.getType();
            Class actualType = pd.getType();

            if (requiredType.isAssignableFrom(actualType) == false) {
                throw new IllegalArgumentException(
                        "The parameter [" + pd.getName() + "] of the RuleFunction [" +
                                rf.getSignature() +
                                "] is invalid. Received type [" + actualType.getName() +
                                "]. Expected types " + Arrays.asList(parameters));
            }
        }

        return (actualPDLength == parameters.length);
    }

    //--------------

    public static class Creator implements DriverCallbackCreator {
        protected String patternDefURI;

        protected String patternInstanceName;

        protected RF successListener;

        protected RF failureListener;

        protected Serializable closure;

        //todo How do we recover this?

        protected transient RuleSessionItems rsi;

        //todo How do we recover this?

        protected transient RuleFunction successRuleFunction;

        //todo How do we recover this?

        protected transient RuleFunction failureRuleFunction;

        /**
         * @param rsi
         * @param patternDefURI
         * @param patternInstanceName
         * @param successListenerURI  See {@link CallbackFunctionParameter} for parameter details.
         * @param failureListenerURI  See {@link CallbackFunctionParameter} for parameter details.
         * @param closure
         */
        public Creator(RuleSessionItems rsi, String patternDefURI, String patternInstanceName,
                       String successListenerURI, String failureListenerURI, Serializable closure) {
            RuleSessionImpl rs = (RuleSessionImpl) rsi.getRuleSession();

            RuleFunction rf = rs.getRuleFunction(successListenerURI);
            boolean allSuccessParams = validateAndCheckAllParamsNeeded(rf);
            this.successRuleFunction = rf;

            rf = rs.getRuleFunction(failureListenerURI);
            boolean allFailureParams = validateAndCheckAllParamsNeeded(rf);
            this.failureRuleFunction = rf;

            this.rsi = rsi;

            this.patternDefURI = patternDefURI;
            this.patternInstanceName = patternInstanceName;
            this.successListener = new RF(successListenerURI, allSuccessParams);
            this.failureListener = new RF(failureListenerURI, allFailureParams);
            this.closure = closure;
        }

        /**
         * @return <code>true</code> if at least 1 of the listeners uses all the parameters specified in {@link
         *         CallbackFunctionParameter}.
         */
        public boolean usesAllParameters() {
            return successListener.isAllParameters() || failureListener.isAllParameters();
        }

        public Callback create(ResourceProvider resourceProvider,
                               AdvancedDriverOwner driverOwner) {
            return new Callback(rsi, patternDefURI, patternInstanceName,
                    successListener, failureListener, closure,
                    successRuleFunction, failureRuleFunction);
        }

        public DriverCallbackCreator recover(ResourceProvider resourceProvider, Object... params)
                throws RecoveryException {
            //todo

            return this;
        }
    }

    //--------------

    @LogCategory("pattern.be.listener")
    public static class Callback implements DriverCallback {
        protected String patternDefURI;

        protected String patternInstanceName;

        protected RF successListener;

        protected RF failureListener;

        protected Serializable closure;

        //todo How do we recover this?

        protected transient RuleSessionItems rsi;

        //todo How do we recover this?

        protected transient RuleFunction successRuleFunction;

        //todo How do we recover this?

        protected transient RuleFunction failureRuleFunction;

        //todo How do we recover this?

        protected transient DriverView driverView;

        protected static transient Logger LOGGER;

        public Callback(RuleSessionItems rsi, String patternDefURI, String patternInstanceName,
                        RF successListener, RF failureListener,
                        Serializable closure,
                        RuleFunction successRuleFunction, RuleFunction failureRuleFunction) {
            this.patternDefURI = patternDefURI;
            this.patternInstanceName = patternInstanceName;
            this.successListener = successListener;
            this.failureListener = failureListener;
            this.closure = closure;

            this.rsi = rsi;
            this.successRuleFunction = successRuleFunction;
            this.failureRuleFunction = failureRuleFunction;

            if (LOGGER == null) {
                LOGGER = $logger(rsi.getAdmin().getResourceProvider(), getClass());
            }
        }

        public void start(ResourceProvider resourceProvider, DriverView driverView) {
            this.driverView = driverView;
        }

        private void handleResponse(Response response) {
            RuleFunction ruleFunction = null;
            RF rf = null;

            if (response instanceof Complete) {
                ruleFunction = successRuleFunction;
                rf = successListener;
            }
            else if (response instanceof Failure) {
                ruleFunction = failureRuleFunction;
                rf = failureListener;
            }
            else {
                //IgnoredTimeOut, DriverNotFound and the rest.
                return;
            }

            //--------------

            $log(response);

            //--------------

            RuleSession rs = rsi.getRuleSession();
            Object[] params = null;
            if (rf.isAllParameters()) {
                params = new Object[]{patternDefURI, patternInstanceName,
                        driverView.getDriverCorrelationId(), closure, driverView};
            }
            else {
                params = new Object[]{patternDefURI, patternInstanceName,
                        driverView.getDriverCorrelationId(), closure};
            }

            RuleSession oldSession = RuleSessionManager.getCurrentRuleSession();
            if (oldSession == rs) {
                callRF(ruleFunction, params);

                return;
            }

            try {
                RuleSessionManager.currentRuleSessions.set(rs);

                callRF(ruleFunction, params);
            }
            finally {
                RuleSessionManager.currentRuleSessions.set(oldSession);
            }
        }

        private void $log(Response response) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                        "Response for" +
                                " correlation Id [" + driverView.getDriverCorrelationId() + "]" +
                                ", instance Id [" + driverView.getDriverInstanceId() + "]" +
                                ": " + response);
            }
        }

        private void callRF(RuleFunction ruleFunction, Object[] params) {
            try {
                ruleFunction.invoke(params);
            }
            catch (Throwable t) {
                LOGGER.log(Level.SEVERE,
                        "Error occurred while invoking the listener" +
                                " RuleFunction [" + ruleFunction.getSignature() + "]" +
                                " with closure [" + closure + "]" +
                                ", correlation Id [" + driverView.getDriverCorrelationId() + "]" +
                                " and instance Id [" + driverView.getDriverInstanceId() + "]", t);
            }
        }

        public void afterInput(Source source, Input input, Response response) {
            handleResponse(response);
        }

        public void afterTimeOut(TimeInput input, Response response) {
            handleResponse(response);
        }

        public void stop() {
            successRuleFunction = null;
            failureRuleFunction = null;

            successRuleFunction = null;
            failureRuleFunction = null;

            rsi = null;
            driverView = null;
        }

        public Callback recover(ResourceProvider resourceProvider, Object... params)
                throws RecoveryException {
            if (LOGGER == null) {
                LOGGER = $logger(resourceProvider, getClass());
            }

            //todo

            return this;
        }
    }

    //--------------

    protected static class RF implements Serializable {
        protected String ruleFunctionURI;

        protected boolean allParameters;

        public RF(String ruleFunctionURI, boolean allParameters) {
            this.ruleFunctionURI = ruleFunctionURI;
            this.allParameters = allParameters;
        }

        public String getRuleFunctionURI() {
            return ruleFunctionURI;
        }

        public boolean isAllParameters() {
            return allParameters;
        }
    }
}
