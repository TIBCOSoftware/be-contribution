package com.tibco.cep.store.cassandra;

import com.tibco.cep.store.StoreConnection;
import com.tibco.cep.store.StoreConnectionInfo;
import com.tibco.cep.store.StoreExt;
import com.tibco.cep.store.StoreQueryOptions;
import com.tibco.cep.store.factory.IStoreFactory;

public class CassandraStoreFactory implements IStoreFactory{

	private static final String CASSANDRA = "Cassandra";

	@Override
	public String getType() {
		return CASSANDRA;
	}

	@Override
	public StoreConnection createConnection(StoreConnectionInfo storeConnectionInfo) {
		return new CassandraConnection(storeConnectionInfo);
	}

	@Override
	public StoreConnectionInfo createConnectionInfo(String storeTypeString, String url) {
		return new CassandraConnectionInfo(storeTypeString, url);
	}

	@Override
	public StoreQueryOptions createQueryOptions() {
		return new CassandraQueryOptions();
	}

	@Override
	public StoreExt createStoreExt() {
		return new CassandraStoreExt();
	}
}
