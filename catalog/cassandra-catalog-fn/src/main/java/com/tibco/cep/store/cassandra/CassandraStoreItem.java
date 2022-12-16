package com.tibco.cep.store.cassandra;

import java.util.Map;
import java.util.TreeMap;

import com.tibco.cep.store.Item;
import com.tibco.cep.store.StoreContainer;
import com.tibco.cep.store.cassandra.serializer.CassandraRowCodec;

/**
 * @author rakulkar
 */
public class CassandraStoreItem extends Item {

	private Map<String, Object> keyValueMap;
	
	private int ttl=0;

	public Map<String, Object> getKeyValueMap() {
		return keyValueMap;
	}

	public void setKeyValueMap(Map<String, Object> keyValueMap) {
		this.keyValueMap = keyValueMap;
	}

	public CassandraStoreItem(StoreContainer<? extends Item> storeContainer) {
		this.storeContainer = storeContainer;
		this.keyValueMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	protected void setItemCodec() {
		itemCodec = new CassandraRowCodec();
	}

	@Override
	protected void createItem() throws Exception {
		this.keyValueMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	public void destroy() throws Exception {
		this.keyValueMap = null;
	}

	@Override
	public void clear() throws Exception {
		this.keyValueMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	public void setTTL(long ttl) throws Exception {
		this.ttl = (int) ttl;
	}

	@Override
	public long getExpiration() throws Exception {
		return this.ttl;
	}

	public Object getFieldValue(String fieldName) {
		Object value = this.keyValueMap.get(fieldName);
		if (null == value)
			value = this.keyValueMap.get(fieldName.toUpperCase());
		return value;
	}
}
