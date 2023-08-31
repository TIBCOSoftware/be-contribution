package com.tibco.cep.analytics.statistica.io;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.CONDITION;
import static com.tibco.be.model.functions.FunctionDomain.QUERY;

import com.tibco.be.model.functions.Enabled;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;

@com.tibco.be.model.functions.BEPackage(catalog = "CEP Analytics", 
category = "Analytics.Statistica.ConnectionInfo", 
enabled = @Enabled(property = "TIBCO.CEP.modules.function.catalog.analytics.statistica", value = true), synopsis = "Connection Info to access Statistica server.")

public class ConnectionFunctions {
	static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(ConnectionFunctions.class);


	@com.tibco.be.model.functions.BEFunction(name = "createConnection", signature = "Object createConnection(String hostUrl)", params = {
			@FunctionParamDescriptor(name = "hostUrl", type = "String", desc = "URL of the Statistica Server")},
				freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""), 
				version = "5.6", see = "", mapper = @com.tibco.be.model.functions.BEMapper(), 
				description = "Sets the connection information for Host URL  to connect to the server.", 
				cautions = "none", fndomain = {	ACTION, CONDITION, QUERY }, example = "")
	public static Object createConnection(String hostUrl) {
		return ConnectionFunctionsDelegate.createConnection( hostUrl);
	}
	

	@com.tibco.be.model.functions.BEFunction(name = "setBasicAuth", signature = "Object setBasicAuth(Object connection, String userName, String password)", params = {
			@FunctionParamDescriptor(name = "connectionInfo", type = "Object", desc = "ConnectionInfo object returned using createConnection"),
			@FunctionParamDescriptor(name = "userName", type = "String", desc = "User name to connect to the Statistica Server using Basic Auth"),
			@FunctionParamDescriptor(name = "password", type = "String", desc = "Password to connect to the Statistica Server using Basic Auth")},
				freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""), 
				version = "5.6", see = "", mapper = @com.tibco.be.model.functions.BEMapper(), 
				description = "Sets the credential information like User name and Password for Basic Authorization for Statistica for it to connect to the server.", 
				cautions = "none", fndomain = {	ACTION, CONDITION, QUERY }, example = "")
	public static boolean setCredentials(Object connection, String userName, String password) {
		return ConnectionFunctionsDelegate.setBasicAuth((ConnectionInfo) connection, userName, password);
	}


}
