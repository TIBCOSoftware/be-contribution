package com.tibco.cep.pattern.functions.manager;

import static com.tibco.be.model.functions.FunctionDomain.*;
import com.tibco.cep.common.exception.LifecycleException;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.functions.Helper;
import com.tibco.cep.pattern.integ.admin.Admin;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.integ.impl.master.PatternBuilder;
import com.tibco.cep.pattern.integ.impl.master.PatternRegistry;
import com.tibco.cep.pattern.integ.impl.master.RuleSessionItems;
import com.tibco.cep.pattern.integ.master.LanguageManager;
import com.tibco.cep.pattern.integ.master.OntologyService;

import java.io.Serializable;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tibco.cep.pattern.functions.Helper.assertRSI;
import static com.tibco.cep.pattern.functions.Helper.getCurrentThreadRSI;

/*
* Author: Ashwin Jayaprakash / Date: Dec 15, 2009 / Time: 2:36:09 PM
*/

@com.tibco.be.model.functions.BEPackage(
		catalog = "CEP Pattern",
        category = "Pattern.Manager",
        synopsis = "Pattern management functions")
public abstract class ManagerFunctions {
    protected static Logger LOGGER;

    static final String[] EMPTY_STRING_ARRAY = {};

    protected static Logger $logger() {
        if (LOGGER == null) {
            LOGGER = Helper.getLogger();
        }

        return LOGGER;
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "register",
        synopsis = "Registers the pattern definition text under the URI (define pattern XYZ ...) provided in the text. The\nURI (XYZ) that was extracted is also returned by the method for reference. The URI has to be unique for the\nregistration to succeed.",
        signature = "String register(String patternLangString)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternLangString", type = "String", desc = "The syntactically valid pattern definition string.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "String", desc = "The URI of the pattern that was parsed. The URI is obtained after parsing the string."),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Registers the pattern definition text under the URI (define pattern XYZ ...) provided in the text. The URI (XYZ) that was extracted is also returned by the method\nfor reference. The URI has to be unique for the registration to succeed.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static String register(String patternLangString) {
        try {
            RuleSessionItems rsi = getCurrentThreadRSI();
            assertRSI(rsi);

            OntologyService ontologyService = rsi.getOntologyService();

            LanguageManager languageManager = ontologyService.getLanguageManager();

            if (patternLangString == null || patternLangString.trim().length() == 0) {
                throw new IllegalArgumentException(
                        "The pattern string is invalid [" + patternLangString + "].");
            }

            Definition definition = languageManager.parse(patternLangString);
            definition = languageManager.validate(definition);

            String uri = definition.getUri();

            ontologyService.registerPatternDef(definition);

            PatternRegistry patternRegistry = rsi.getPatternRegistry();
            patternRegistry.registerPatternUri(uri);

            $logger().log(Level.INFO, "Registered new Pattern definition under [" + uri + "]");

            return uri;
        }
        catch (IllegalArgumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "An error occurred while registering the pattern definition [" +
                            patternLangString + "].", e);
        }
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getRegistered",
        synopsis = "Returns the URIs of all the registered patterns.",
        signature = "String[] getRegistered()",
        params = {
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "String[]", desc = "Array of registered pattern URIs."),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the URIs of all the registered patterns.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static String[] getRegistered() {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        PatternRegistry patternRegistry = rsi.getPatternRegistry();

        return patternRegistry.getPatternUris().toArray(EMPTY_STRING_ARRAY);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "getDeployed",
        synopsis = "Returns the pattern instance names that are deployed under the given URI.",
        signature = "String[] getDeployed(String patternDefURI)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternDefURI", type = "String", desc = "URI of the registered pattern.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "String[]", desc = "Array of instance names of the patterns deployed under the given URI."),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the pattern instance names that are deployed under the given URI.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static String[] getDeployed(String patternDefURI) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        PatternRegistry patternRegistry = rsi.getPatternRegistry();

        return patternRegistry.getPatternInstanceNames(patternDefURI).toArray(EMPTY_STRING_ARRAY);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "unregister",
        synopsis = "Unregisters the Pattern that was previously registered under the given URI.",
        signature = "void unregister(String patternDefURI)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternDefURI", type = "String", desc = "URI of the pattern that is currently registered.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Unregisters the Pattern that was previously registered under the given URI.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void unregister(String patternDefURI) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        OntologyService ontologyService = rsi.getOntologyService();

        if (patternDefURI == null) {
            throw new IllegalArgumentException(
                    "There is no pattern definition registered under the URI [" + patternDefURI +
                            "] provided");
        }

        try {
            Definition definition = ontologyService.unregisterPatternDef(patternDefURI);
            if (definition == null) {
                throw new IllegalArgumentException(
                        "There is no pattern definition registered under the URI [" +
                                patternDefURI + "] provided");
            }

            PatternRegistry patternRegistry = rsi.getPatternRegistry();
            patternRegistry.unregisterPatternUri(patternDefURI);
        }
        catch (IllegalArgumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "An error occurred while unregistering the pattern definition [" +
                            patternDefURI + "].", e);
        }

        $logger().log(Level.INFO, "Unregistered Pattern definition under [" + patternDefURI + "]");
    }

    //-------------

    @com.tibco.be.model.functions.BEFunction(
        name = "instantiate",
        synopsis = "Creates an instance of the pattern registered under the given URI for deployment. This instance should\nbe configured before deployment.",
        signature = "Object instantiate(String patternDefURI)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternDefURI", type = "String", desc = "URI of the registered pattern.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "Object", desc = "The pattern instance. This should be treated as an opaque object meant for configuring a pattern\ninstance."),
        version = "5.2",
        see = "setXX(Object patternInstance, ...) methods",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Creates an instance of the pattern registered under the given URI for deployment. This instance should be configured before deployment.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static Object instantiate(String patternDefURI) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        OntologyService ontologyService = rsi.getOntologyService();

        if (patternDefURI == null) {
            throw new IllegalArgumentException(
                    "There is no pattern definition registered under the URI [" + patternDefURI +
                            "] provided");
        }

        Definition definition = ontologyService.getPatternDef(patternDefURI);
        if (definition == null) {
            throw new IllegalArgumentException(
                    "There is no pattern definition registered under the URI [" + patternDefURI +
                            "] provided");
        }

        return new PatternBuilder(definition);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setParameterInt",
        synopsis = "If the pattern string has a bind parameter that expects an integer, then its value has to be set using\nthis method. The value is set only for the given pattern instance.",
        signature = "void setParameterInt(Object patternInstance, String parameterName, int value)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "parameterName", type = "String", desc = "Name of the bind parameter whose value is being set."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "int", desc = "Value of the bind parameter")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "If the pattern string has a bind paramater that expects an integer, then its value has to be set using this method.\nThe value is set only for a given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setParameterInt(Object patternInstance, String parameterName,
                                       int value) {
        validatePatternInstance(patternInstance).setParameter(parameterName, value);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setParameterLong",
        synopsis = "If the pattern string has a bind parameter that expects a long, then its value has to be set using\nthis method. The value is set only for the given pattern instance.",
        signature = "void setParameterLong(Object patternInstance, String parameterName, long value)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "parameterName", type = "String", desc = "Name of the bind parameter whose value is being set."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "long", desc = "Value of the bind parameter")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "If the pattern string has a bind parameter that expects a long, then its value has to be set using this method.\nThe value is set only for the given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setParameterLong(Object patternInstance, String parameterName,
                                        long value) {
        validatePatternInstance(patternInstance).setParameter(parameterName, value);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setParameterDouble",
        synopsis = "If the pattern string has a bind parameter that expects a double, then its value has to be set using\nthis method. The value is set only for the given pattern instance.",
        signature = "void setParameterDouble(Object patternInstance, String parameterName, double value)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "parameterName", type = "String", desc = "Name of the bind parameter whose value is being set."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "double", desc = "Value of the bind parameter")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "If the pattern string has bind parameter that expects a double, then its value has to be set using this method.\nThis value if set only for the given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setParameterDouble(Object patternInstance, String parameterName,
                                          double value) {
        validatePatternInstance(patternInstance).setParameter(parameterName, value);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setParameterString",
        synopsis = "If the pattern string has a bind parameter that expects a String, then its value has to be set using\nthis method. The value is set only for the given pattern instance.",
        signature = "void setParameterString(Object patternInstance, String parameterName, String value)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "parameterName", type = "String", desc = "Name of the bind parameter whose value is being set."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "String", desc = "Value of the bind parameter")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "If the pattern string has a bind parameter that expects a String, then its value has to be set using this method.\nThe value is et only for the given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setParameterString(Object patternInstance, String parameterName,
                                          String value) {
        validatePatternInstance(patternInstance).setParameter(parameterName, value);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setParameterDateTime",
        synopsis = "If the pattern string has a bind parameter that expects a DateTime object (java.util.Calendar), then\nits value has to be set using this method. The value is set only for the given pattern instance.",
        signature = "void setParameterDateTime(Object patternInstance, String parameterName, DateTime value)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "parameterName", type = "String", desc = "Name of the bind parameter whose value is being set."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "value", type = "DateTime", desc = "Value of the bind parameter")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "If the pattern string has a bind parameter that expects a DateTime object (java.util.Calendar), then its value has to be set using this method.\nThe value is set only for the given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setParameterDateTime(Object patternInstance, String parameterName,
                                            Calendar value) {
        validatePatternInstance(patternInstance).setParameter(parameterName, value);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setClosure",
        synopsis = "Sets a $1closure$1 object that will be delivered in the listener function call to help identify this\npattern instance. The value is set only for the given pattern instance.",
        signature = "void setClosure(Object patternInstance, Object closure)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "closure", type = "Object", desc = "The Pattern Service will not interpret this object. It can be of any type.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sets a $1closure$1 object that will be delivered in the listener function call to help identify this pattern instance. The value is set only for the given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setClosure(Object patternInstance, Object closure) {
        Serializable serializable = (Serializable) closure;

        validatePatternInstance(patternInstance).setClosure(serializable);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setFailureListener",
        synopsis = "Sets the given rule function URI as the listener that will be invoked by the Pattern Service when/if\nthis pattern instance fails due to a wrong event sequence. The value is set only for the given pattern instance.",
        signature = "void setFailureListener(Object patternInstance, String ruleFunctionURI)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "ruleFunctionURI", type = "String", desc = "has obeserved so far.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sets the given rule function URI as the listener that will be invoked by the Pattern Service when/if this pattern instance fails due to a wrong event sequence.\nThe value is set only for the given patten instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setFailureListener(Object patternInstance, String ruleFunctionURI) {
        //See {@link CallbackFunctionParameter} for the parameter list.
        validatePatternInstance(patternInstance).setFailureListenerURI(ruleFunctionURI);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "setCompletionListener",
        synopsis = "Sets the given rule function URI as the listener that will be invoked by the Pattern Service when/if\nthis pattern instance observes the specified event sequence. The value is set only for the given pattern\ninstance.",
        signature = "void setCompletionListener(Object patternInstance, String ruleFunctionURI)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "ruleFunctionURI", type = "String", desc = "has obeserved so far.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Sets the given rule function URI as the listenet that will be invoked by the Pattern Service when/if this pattern instance observes the specified event sequence.\nThe value is set only for the given pattern instance.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void setCompletionListener(Object patternInstance, String ruleFunctionURI) {
        //See {@link CallbackFunctionParameter} for the parameter list.
        validatePatternInstance(patternInstance).setSuccessListenerURI(ruleFunctionURI);
    }

    private static DefaultId createPatternInstanceId(String patternInstanceName) {
        return new DefaultId(patternInstanceName);
    }

    //-------------

    @com.tibco.be.model.functions.BEFunction(
        name = "deploy",
        synopsis = "Deploys the pattern instance that has all the necessary parameters set. The instance will be deployed\nunder the name provided. This name has to be unique.",
        signature = "void deploy(Object patternInstance, String patternInstanceName)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstance", type = "Object", desc = "The return value (opaque) from the pattern $1instantiate(String)$1 method."),
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstanceName", type = "String", desc = "The name for the instance being deployed.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "void undeploy(String patternInstanceName)",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Deploys the pattern instance that has all the necessary parameters set. The instance will be deployed under the name provided. This name has to be unique.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void deploy(Object patternInstance, String patternInstanceName) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        validatePatternInstance(patternInstance);

        if (patternInstanceName == null || patternInstanceName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "The pattern instance name [" + patternInstanceName + "] is invalid.");
        }

        try {
            DefaultId id = createPatternInstanceId(patternInstanceName);

            PatternBuilder patternBuilder = validatePatternInstance(patternInstance);
            patternBuilder.buildAndDeploy(rsi, patternInstanceName, id);

            String uri = patternBuilder.getDefinition().getUri();

            PatternRegistry patternRegistry = rsi.getPatternRegistry();
            patternRegistry.registerPatternInstanceName(uri, patternInstanceName);

            $logger().log(Level.INFO,
                    "Deployed new Pattern instance of" +
                            " [" + uri + "] as [" + patternInstanceName + "]");
        }
        catch (LifecycleException e) {
            throw new RuntimeException("An error occurred during deployment.", e);
        }
    }

    private static PatternBuilder validatePatternInstance(Object patternInstance) {
        if (patternInstance == null
                || (patternInstance instanceof PatternBuilder == false)
                || ((PatternBuilder) patternInstance).isValid() == false) {
            throw new IllegalArgumentException("The pattern instance is invalid.");
        }

        return (PatternBuilder) patternInstance;
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "undeploy",
        synopsis = "Undeploys the pattern instance that was deployed under the name provided.",
        signature = "void undeploy(String patternInstanceName)",
        params = {
            @com.tibco.be.model.functions.FunctionParamDescriptor(name = "patternInstanceName", type = "String", desc = "The name of the pattern instance that is currently deployed.")
        },
        freturn = @com.tibco.be.model.functions.FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Undeploys the pattern instance that was deployed under the name provided.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void undeploy(String patternInstanceName) {
        RuleSessionItems rsi = getCurrentThreadRSI();
        assertRSI(rsi);

        try {
            DefaultId id = createPatternInstanceId(patternInstanceName);
            Admin admin = rsi.getAdmin();
            admin.undeploy(id);

            PatternRegistry patternRegistry = rsi.getPatternRegistry();
            String uri = patternRegistry.getPatternUri(patternInstanceName);
            if (uri == null) {
                throw new IllegalArgumentException(
                        "There is no pattern instance deployed under the name [" + patternInstanceName + "] provided");
            }
            patternRegistry.unregisterPatternInstanceName(uri, patternInstanceName);

            $logger().log(Level.INFO, "Undeployed Pattern instance [" + patternInstanceName + "]");
        }
        catch (LifecycleException e) {
            throw new RuntimeException("An error occurred while undeploying.", e);
        }
    }
}
