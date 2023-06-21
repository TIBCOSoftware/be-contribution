package com.tibco.cep.analytics.pmml.io;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.CONDITION;
import static com.tibco.be.model.functions.FunctionDomain.QUERY;

import com.tibco.be.model.functions.Enabled;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.event.SimpleEvent;


@com.tibco.be.model.functions.BEPackage(
	catalog = "CEP Analytics",
    category = "Analytics.PMML",
    enabled = @Enabled(property="TIBCO.CEP.modules.function.catalog.analytics.pmml", value=true),
    synopsis = "Functions to access PMML.")

public class PmmlFunctions {
    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(PmmlFunctions.class);

    @com.tibco.be.model.functions.BEFunction(
        name = "loadModel",
        signature = "void loadModel(String modelName, String filePath)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
                @FunctionParamDescriptor(name = "filePath", type = "String", desc = "Location of Pmml model")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Loads the PMML model from a file (Users have to specify the model name). Throws an exception if input file does not exist or model is not valid",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void loadModel(String modelName, String filePath) {
            PmmlFunctionsDelegate.loadModel(modelName, filePath, true);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "modelExists",
        signature = "boolean modelExists(String modelName)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Boolean", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Tests if the specified model exists in the PMML engine.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Boolean modelExists(String modelName) {
        return PmmlFunctionsDelegate.modelExists(modelName);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "removeModel",
        signature = "void removeModel(String modelName)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Removes the specified model from PMML engine.",
        cautions = "none",
        fndomain = {ACTION},
        example = ""
    )
    public static void removeModel(String modelName) {
    	PmmlFunctionsDelegate.removeModel(modelName);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "evalModelwithParams",
        signature = "Object evalModelwithParams(String modelName, Object... parameters)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
                @FunctionParamDescriptor(name = "parameters", type = "Object...", desc = "Parameters of the model.For example, if a model has an input field Age, then the parameters to this function would be Age,20.")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Calls the PMML engine with the model name and parameters, and then returns the result.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Object evalModelwithParams(String modelName, Object... parameters) {
        return PmmlFunctionsDelegate.evalModelwithParams(modelName, parameters);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "evalModelWithConcept",
        signature = "Object evalModelWithConcept(String modelName, Concept cept)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
                @FunctionParamDescriptor(name = "concept", type = "Concept", desc = "A Concept")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Calls the PMML engine with the model name and Concept, and then returns the result.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Object evalModelWithConcept(String modelName, Concept concept) {
        return PmmlFunctionsDelegate.evalModelWithConcept(modelName, concept);
    }

    @com.tibco.be.model.functions.BEFunction(
        name = "evalModelwithEvent",
        signature = "Object evalModelwithEvent(String modelName, SimpleEvent event)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
                @FunctionParamDescriptor(name = "event", type = "SimpleEvent", desc = "A SimpleEvent")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Calls the PMML engine with the model name and Event, and then returns the result.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Object evalModelwithEvent(String modelName, SimpleEvent event) {
        return PmmlFunctionsDelegate.evalModelwithEvent(modelName, event);
    }
    
    
    @com.tibco.be.model.functions.BEFunction(
        name = "getTargetFields",
        signature = "Object getTargetFields(String modelName)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the names of target fields of PMML model as a list",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Object getTargetFields(String modelName) {
        return PmmlFunctionsDelegate.getTargetFields(modelName);
    }
    
    @com.tibco.be.model.functions.BEFunction(
        name = "getInputFields",
        signature = "Object getInputFields(String modelName)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the names of input fields of PMML model as a list",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Object getInputFields(String modelName) {
        return PmmlFunctionsDelegate.getInputFields(modelName);
    }


    @com.tibco.be.model.functions.BEFunction(
        name = "getOutputFields",
        signature = "Object getOutputFields(String modelName)",
        params = {
                @FunctionParamDescriptor(name = "modelName", type = "String", desc = "Name of the model"),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
        version = "5.5",
        see = "",
        mapper = @com.tibco.be.model.functions.BEMapper(),
        description = "Returns the names of output fields of PMML model as a list",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = ""
    )
    public static Object getOutputFields(String modelName) {
        return PmmlFunctionsDelegate.getOutputFields(modelName);
    }
}
