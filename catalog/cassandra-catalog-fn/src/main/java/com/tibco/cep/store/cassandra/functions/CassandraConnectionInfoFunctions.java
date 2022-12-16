/**
 * 
 */
package com.tibco.cep.store.cassandra.functions;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;

import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import com.tibco.cep.store.cassandra.CassandraConnectionInfo;
import com.tibco.security.ObfuscationEngine;

/**
 * @author rakulkar
 *
 */
@BEPackage(
		catalog = "CEP Store",
        category = "CStore.ConnectionInfo.Cassandra",
        synopsis = "Cassandra Connection Information functions")
public class CassandraConnectionInfoFunctions {
	
	@com.tibco.be.model.functions.BEFunction(
		name = "setUserCredentials",
		signature = "void setUserCredentials (Object storeConnectionInfo, String userName, String password)",
		params = {
				@FunctionParamDescriptor(name = "storeConnectionInfo", type = "Object", desc = "Store Connection Info object"),
				@FunctionParamDescriptor(name = "userName", type = "String", desc = "User name"),
				@FunctionParamDescriptor(name = "password", type = "String", desc = "User password")
		},
		freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
		version = "6.3.0",
		see = "",
		mapper = @com.tibco.be.model.functions.BEMapper(),
		description = "Sets the user credentials if using a authenticated realm server.",
		cautions = "none",
		fndomain = {ACTION},
		example = ""
	)
	public static void setUserCredentials(Object storeConnectionInfo, String userName, String password) {
		if (userName == null || userName.isEmpty()) throw new RuntimeException("Missing user name. User name is required for a secure communication.");
		if (password == null || password.isEmpty()) throw new RuntimeException("Missing user password. User password is required for a secure communication.");
		if (storeConnectionInfo instanceof CassandraConnectionInfo) {
			CassandraConnectionInfo dgStoreConnectionInfo = (CassandraConnectionInfo) storeConnectionInfo;
			try {
				if (password != null || !(password.trim().isEmpty())) {
		            if (ObfuscationEngine.hasEncryptionPrefix(password)) {
		            	password = new String(ObfuscationEngine.decrypt(password));
		            }
	    		}
				dgStoreConnectionInfo.setUserCredentials(userName, password);
			} catch (Exception e) {
				throw new RuntimeException("Unable to decrypt an encrypted password.");  
	        }
		} else {
			throw new IllegalArgumentException("Invalid Store properties type. Expected type Store Connection Info object.");
		}
	}

	@com.tibco.be.model.functions.BEFunction(
		name = "setTrustStore",
		signature = "void setTrustStore (Object storeConnectionInfo, String trustFilePath, String trustStorePwd, String storeType)",
		params = {
				@FunctionParamDescriptor(name = "storeConnectionInfo", type = "Object", desc = "Store Connection Info object"),
				@FunctionParamDescriptor(name = "trustFilePath", type = "String", desc = "Trust file path"),
				@FunctionParamDescriptor(name = "trustStorePwd", type = "String", desc = "Trust Store Pwd"),
				@FunctionParamDescriptor(name = "storeType", type = "String", desc = "Trust store type")
		},
		freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
		version = "6.3.0",
		see = "",
		mapper = @com.tibco.be.model.functions.BEMapper(),
		description = "Sets the trust store related information. The client trusts the secure cassandra server based on this trust store information.",
		cautions = "none",
		fndomain = {ACTION},
		example = ""
	)
	public static void setTrustStore(Object storeConnectionInfo, String trustFilePath, String trustStorePwd, String storeType) {
		if (trustFilePath == null || trustFilePath.isEmpty()) throw new RuntimeException("Missing trust file path.");
		if (storeConnectionInfo instanceof CassandraConnectionInfo) {
			CassandraConnectionInfo cassandraConnectionInfo = (CassandraConnectionInfo) storeConnectionInfo;
			cassandraConnectionInfo.setTrustStoreProps(trustFilePath, trustStorePwd, storeType);
		} else {
			throw new IllegalArgumentException("Invalid Store properties type. Expected type Store Connection Info object.");
		}
	}
	
	@com.tibco.be.model.functions.BEFunction(
			name = "setKeyStore",
			signature = "void setKeyStore (Object storeConnectionInfo, String keystorePath, String keystorePwd, String keyStoreType)",
			params = {
					@FunctionParamDescriptor(name = "storeConnectionInfo", type = "Object", desc = "Store Connection Info object"),
					@FunctionParamDescriptor(name = "keystorePath", type = "String", desc = "Keystore file path"),
					@FunctionParamDescriptor(name = "keystorePwd", type = "String", desc = "Keystore Pwd"),
					@FunctionParamDescriptor(name = "keyStoreType", type = "String", desc = "Key store type")
			},
			freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
			version = "6.3.0",
			see = "",
			mapper = @com.tibco.be.model.functions.BEMapper(),
			description = "Sets the keystore store related information.",
			cautions = "none",
			fndomain = {ACTION},
			example = ""
		)
		public static void setKeyStore(Object storeConnectionInfo, String keystorePath, String keystorePwd, String keyStoreType) {
			if (keystorePath == null || keystorePath.isEmpty()) throw new RuntimeException("Missing keystore path.");
			if (storeConnectionInfo instanceof CassandraConnectionInfo) {
				CassandraConnectionInfo cassandraConnectionInfo = (CassandraConnectionInfo) storeConnectionInfo;
				cassandraConnectionInfo.setKeyStoreProps(keystorePath, keyStoreType, keystorePwd);
			} else {
				throw new IllegalArgumentException("Invalid Store properties type. Expected type Store Connection Info object.");
			}
		}
	
	@com.tibco.be.model.functions.BEFunction(
		name = "setKeySpace",
		signature = "void setKeySpace (Object storeConnectionInfo, String keySpaceName)",
		params = {
				@FunctionParamDescriptor(name = "storeConnectionInfo", type = "Object", desc = "Store Connection Info object"),
				@FunctionParamDescriptor(name = "keySpaceName", type = "String", desc = "keyspace name")
		},
		freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
		version = "6.3.0",
		see = "",
		mapper = @com.tibco.be.model.functions.BEMapper(),
		description = "Sets the keyspace for connection",
		cautions = "none",
		fndomain = {ACTION},
		example = ""
	)
	public static void setKeyspace(Object storeConnectionInfo, String keySpaceName) {
		if (storeConnectionInfo instanceof CassandraConnectionInfo) {
			CassandraConnectionInfo dgStoreConnectionInfo = (CassandraConnectionInfo) storeConnectionInfo;
			dgStoreConnectionInfo.setKeySpace(keySpaceName);
		} else {
			throw new IllegalArgumentException("Invalid Store properties type. Expected type Store Connection Info object.");
		}
	}
	
	
	@com.tibco.be.model.functions.BEFunction(
			name = "setUseSsl",
			signature = "void setUseSsl (Object storeConnectionInfo, boolean useSsl)",
			params = {
					@FunctionParamDescriptor(name = "storeConnectionInfo", type = "Object", desc = "Store Connection Info object"),
					@FunctionParamDescriptor(name = "useSsl", type = "boolean", desc = "Use SSL")
			},
			freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
			version = "6.3.0",
			see = "",
			mapper = @com.tibco.be.model.functions.BEMapper(),
			description = "Sets whether security(ssl) is enabled or not.",
			cautions = "none",
			fndomain = {ACTION},
			example = ""
		)
		public static void setUseSsl(Object storeConnectionInfo, boolean useSsl) {
			if (storeConnectionInfo instanceof CassandraConnectionInfo) {
				CassandraConnectionInfo dgStoreConnectionInfo = (CassandraConnectionInfo) storeConnectionInfo;
				dgStoreConnectionInfo.setUseSsl(useSsl);
			} else {
				throw new IllegalArgumentException("Invalid Store properties type. Expected type Store Connection Info object.");
			}
		}
}
