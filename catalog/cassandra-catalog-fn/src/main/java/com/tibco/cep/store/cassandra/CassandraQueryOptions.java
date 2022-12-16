package com.tibco.cep.store.cassandra;

import java.util.Properties;

import com.datastax.driver.core.ConsistencyLevel;
import com.tibco.cep.store.StoreQueryOptions;

public class CassandraQueryOptions extends StoreQueryOptions{
	private Properties queryProperties;
	
	public void setPrefetchSize(long prefetchSize) {
		throw new UnsupportedOperationException("Method not supported.");
	}

	public void setReuse(boolean reuse) {
		throw new UnsupportedOperationException("Method not supported.");
	}

	public void setReadTimeoutMillis(int readTimeoutMillis) {
		queryProperties.put("readTimeoutMillis", readTimeoutMillis);
	}

	public void setConsistency(ConsistencyLevel consistency) {
		queryProperties.put("consistency", consistency);
	}

	public void setIdempotent(boolean idempotent) {
		queryProperties.put("idempotent", idempotent);
	}

	public void setFetchSize(int fetchSize) {
		queryProperties.put("fetchSize", fetchSize);
	}

	public Properties getProperties() {
		return queryProperties;
	}
}

