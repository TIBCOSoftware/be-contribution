package com.tibco.cep.analytics.statistica.io;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.CONDITION;
import static com.tibco.be.model.functions.FunctionDomain.QUERY;

import com.tibco.be.model.functions.Enabled;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.event.SimpleEvent;

@com.tibco.be.model.functions.BEPackage(catalog = "CEP Analytics", 
category = "Analytics.Statistica", 
enabled = @Enabled(property = "TIBCO.CEP.modules.function.catalog.analytics.statistica", value = true), synopsis = "Functions to access Statistica.")

public class StatFunctions {
	static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(StatFunctions.class);


	@com.tibco.be.model.functions.BEFunction(name = "getScoreWithParams", signature = "Object getScorewithParams(Object connectionInfo, String scriptName, Map<String, String> parameters)", params = {
			@FunctionParamDescriptor(name = "connectionInfo", type = "Object", desc = "ConnectionInfo object returned using createConnection"),
			@FunctionParamDescriptor(name = "scriptName", type = "String", desc = "Script Name of Statistica to be called"),
			@FunctionParamDescriptor(name = "parameters", type = "Object", desc = "Map of parameters to send in the request") }, 
				freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = "The result of statistica call in form of a map"), version = "5.6", see = "", 
				mapper = @com.tibco.be.model.functions.BEMapper(), description = "Calls the Statistica with the the provided scriptName through a SOAP request and returns the result.", 
				cautions = "none", fndomain = {	ACTION, CONDITION, QUERY }, example = "")
	public static Object getScorewithParams(Object conn, String scriptName, Object parameters){
		return StatFunctionsDelegate.getScoreWithParams((ConnectionInfo) conn, scriptName, parameters);
	}

	@com.tibco.be.model.functions.BEFunction(name = "getScoreWithConcept", signature = "Object getScorewithConcept(Object connectionInfo, String scriptName, Concept cept)", params = {
			@FunctionParamDescriptor(name = "connectionInfo", type = "Object", desc = "ConnectionInfo object returned using createConnection"),
			@FunctionParamDescriptor(name = "scriptName", type = "String", desc = "Script Name of Statistica to be called"),
			@FunctionParamDescriptor(name = "concept", type = "Concept", desc = "A Concept") }, 
				freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""), version = "5.6", see = "", 
				mapper = @com.tibco.be.model.functions.BEMapper(), description = "Calls Statistica with the the provided scriptName and Concept through a SOAP request, and then returns the result.", cautions = "none", fndomain = {
					ACTION, CONDITION, QUERY }, example = "")
	public static Object getScorewithConcept(Object conn, String scriptName, Concept concept) {
		return StatFunctionsDelegate.getScoreWithConcept((ConnectionInfo) conn, scriptName, concept);
	}

	@com.tibco.be.model.functions.BEFunction(name = "getScoreWithEvent", signature = "Object getScorewithParams(Object connectionInfo, String scriptName, SimpleEvent event)", params = {
			@FunctionParamDescriptor(name = "connectionInfo", type = "Object", desc = "ConnectionInfo object returned using createConnection"),
			@FunctionParamDescriptor(name = "scriptName", type = "String", desc = "Script Name of Statistica to be called"),
			@FunctionParamDescriptor(name = "event", type = "SimpleEvent", desc = "A SimpleEvent") }, 
				freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""), version = "5.6", see = "", 
				mapper = @com.tibco.be.model.functions.BEMapper(), description = "Calls Statistica with the the provided scriptName and Event through a SOAP request, and then returns the result.", cautions = "none", fndomain = {
					ACTION, CONDITION, QUERY }, example = "")
	public static Object getScoreWithEvent(Object conn, String scriptName, SimpleEvent event) {
		return StatFunctionsDelegate.getScoreWithEvent((ConnectionInfo) conn, scriptName, event);
	}

}
