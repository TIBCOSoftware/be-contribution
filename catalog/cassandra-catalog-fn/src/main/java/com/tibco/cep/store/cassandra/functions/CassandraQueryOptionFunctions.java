/**
 * 
 */
package com.tibco.cep.store.cassandra.functions;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;

import com.datastax.driver.core.ConsistencyLevel;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import com.tibco.cep.store.cassandra.CassandraQueryOptions;

/**
 * @author rakulkar
 *
 */
@BEPackage(
		catalog = "CEP Store",
        category = "CStore.QueryOptions.Cassandra",
        synopsis = "Cassandra Query Option functions")
public class CassandraQueryOptionFunctions {
	
	@com.tibco.be.model.functions.BEFunction(
		name = "setReadTimeoutMillis",
		signature = "void readTimeoutMillis (Object queryOptions, int readTimeoutMillis)",
		params = {
				@FunctionParamDescriptor(name = "queryOptions", type = "Object", desc = "Query option object"),
				@FunctionParamDescriptor(name = "readTimeoutMillis", type = "int", desc = "readTimeoutMillis"),
		},
		freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
		version = "6.3.0",
		see = "",
		mapper = @com.tibco.be.model.functions.BEMapper(),
		description = "Set the read timeout.",
		cautions = "none",
		fndomain = {ACTION},
		example = ""
	)
	public static void setReadTimeoutMillis(Object queryOptions, int readTimeoutMillis) {
		if (queryOptions instanceof CassandraQueryOptions) {
			CassandraQueryOptions cassandraQueryOptions = (CassandraQueryOptions) queryOptions;
			cassandraQueryOptions.setReadTimeoutMillis(readTimeoutMillis);
		} else {
			throw new IllegalArgumentException("Invalid object type. Expected type Query Options object.");
		}
	}
	
	@com.tibco.be.model.functions.BEFunction(
			name = "setFetchSize",
			signature = "void fetchSize (Object queryOptions, int fetchSize)",
			params = {
					@FunctionParamDescriptor(name = "queryOptions", type = "Object", desc = "Query option object"),
					@FunctionParamDescriptor(name = "fetchSize", type = "int", desc = "fetchSize"),
			},
			freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
			version = "6.3.0",
			see = "",
			mapper = @com.tibco.be.model.functions.BEMapper(),
			description = "Sets the query fetch size.",
			cautions = "none",
			fndomain = {ACTION},
			example = ""
		)
		public static void setFetchSize(Object queryOptions, int fetchSize) {
			if (queryOptions instanceof CassandraQueryOptions) {
				CassandraQueryOptions cassandraQueryOptions = (CassandraQueryOptions) queryOptions;
				cassandraQueryOptions.setFetchSize(fetchSize);
			} else {
				throw new IllegalArgumentException("Invalid object type. Expected type Query Options object.");
			}
		}
	
	@com.tibco.be.model.functions.BEFunction(
			name = "setIdempotent",
			signature = "void setIdempotent (Object queryOptions, boolean idempotent)",
			params = {
					@FunctionParamDescriptor(name = "queryOptions", type = "Object", desc = "Query option object"),
					@FunctionParamDescriptor(name = "idempotent", type = "boolean", desc = "idempotent"),
			},
			freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
			version = "6.3.0",
			see = "",
			mapper = @com.tibco.be.model.functions.BEMapper(),
			description = "Sets whether this query is idempotent.",
			cautions = "none",
			fndomain = {ACTION},
			example = ""
		)
		public static void setIdempotent(Object queryOptions, boolean idempotent) {
			if (queryOptions instanceof CassandraQueryOptions) {
				CassandraQueryOptions cassandraQueryOptions = (CassandraQueryOptions) queryOptions;
				cassandraQueryOptions.setIdempotent(idempotent);
			} else {
				throw new IllegalArgumentException("Invalid object type. Expected type Query Options object.");
			}
		}
	
	@com.tibco.be.model.functions.BEFunction(
			name = "setConsistency",
			signature = "void setConsistency (Object queryOptions, String consistencyLevel)",
			params = {
					@FunctionParamDescriptor(name = "queryOptions", type = "Object", desc = "Query option object"),
					@FunctionParamDescriptor(name = "consistencyLevel", type = "String", desc = "Consistency Level"),
			},
			freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
			version = "6.3.0",
			see = "",
			mapper = @com.tibco.be.model.functions.BEMapper(),
			description = "Sets whether this statement is idempotent.",
			cautions = "none",
			fndomain = {ACTION},
			example = ""
		)
		public static void setConsistency(Object queryOptions, String consistencyLevel) {
			if (queryOptions instanceof CassandraQueryOptions) {
				CassandraQueryOptions cassandraQueryOptions = (CassandraQueryOptions) queryOptions;
				ConsistencyLevel conLevel = ConsistencyLevel.LOCAL_ONE;
				if (!(consistencyLevel==null || consistencyLevel.isBlank())) {
					conLevel = ConsistencyLevel.valueOf(consistencyLevel);
				}
				
				cassandraQueryOptions.setConsistency(conLevel);
			} else {
				throw new IllegalArgumentException("Invalid object type. Expected type Query Options object.");
			}
		}
}
