package com.tibco.cep.store.cassandra;

import com.tibco.cep.store.StoreExt;

/**
 * @author rakulkar
 */
public class CassandraStoreExt extends StoreExt {

	@Override
	public String getVersion() {
		throw new UnsupportedOperationException("Method not supported.");
	}

	@Override
	public void setLogLevel(String logLevel) throws Exception {
		throw new UnsupportedOperationException("Method not supported.");
	}

	@Override
	public void setLogFiles(String filePrefix, long maxFileSize, int maxFiles) throws Exception {
		throw new UnsupportedOperationException("Method not supported.");
	}

	@Override
	public void setLogHandler(Object logHandler) throws Exception {
		throw new UnsupportedOperationException("Method not supported.");
	}

}
