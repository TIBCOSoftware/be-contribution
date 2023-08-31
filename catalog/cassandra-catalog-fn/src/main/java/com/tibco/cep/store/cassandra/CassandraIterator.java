/**
 * 
 */
package com.tibco.cep.store.cassandra;

import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.SerializationUtils;

import com.datastax.oss.driver.api.core.cql.ColumnDefinition;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
import com.datastax.oss.driver.api.core.type.DataType;
import com.tibco.cep.store.Item;
import com.tibco.cep.store.StoreIterator;

/**
 * @author rakulkar
 *
 */
public class CassandraIterator extends StoreIterator {

	private CassandraStoreContainer cassandraStoreContainer;
	private ResultSet resultSet;

	public CassandraIterator(ResultSet resultSet, String returnEntityPath) {
		super(resultSet.iterator(), returnEntityPath);
		this.resultSet = resultSet; 
	}

	@Override
	protected Item createItem(Object result) throws Exception {
		CassandraStoreItem returnItem = null;
		Row next = (Row) result;
		Map<String, Object> keyValueMap = null;
		if (next != null) {
			returnItem = new CassandraStoreItem(cassandraStoreContainer);
			keyValueMap  = returnItem.getKeyValueMap();
		}
		ColumnDefinitions colDefns = next.getColumnDefinitions();

		for (ColumnDefinition col : colDefns) {
			String colName = col.getName().asCql(true);
			DataType type = col.getType();
			Object value = getValue(next, colName, type);
			keyValueMap.put(colName.toLowerCase(), value);
		}
		returnItem.setKeyValueMap(keyValueMap);

		return returnItem;
	}
	
	@Override
	public boolean hasNext() {
		boolean isAvailable = false;
		try {
			isAvailable = resultSet.getAvailableWithoutFetching()>0;
			if (!isAvailable) cleanup();
		} catch (Exception e) {
			throw new RuntimeException("Error traversing the store iterator", e);
		}
		return isAvailable;
	}

	public void setContainer(String containerName, TableMetadata tableMetadata) {
		cassandraStoreContainer = new CassandraStoreContainer(containerName);
		cassandraStoreContainer.setTableMetadata(tableMetadata);
	}

	@Override
	public void cleanup() throws Exception {
		cassandraStoreContainer = null;
	}

	private Object getValue(Row next, String colName, DataType type) {
		String cqlType = type.asCql(false, true).toUpperCase();
		cqlType = cqlType.startsWith("TUPLE") ? "TUPLE" : cqlType; 
		switch(cqlType){
		case "VARCHAR": 
		case "TEXT": 
			return next.getString(colName);
		case "UUID": 
			return next.getUuid(colName);
		case "VARINT": 
			return next.getInt(colName);
		case "BIGINT": 
			return next.getLong(colName);
		case "INT": 
			return next.getInt(colName);
		case "FLOAT": 
			return next.getFloat(colName);	
		case "DOUBLE": 
			return next.getDouble(colName);
		case "BOOLEAN": 
			return next.getBoolean(colName);
		case "MAP": 
			return next.getMap(colName, String.class, String.class);
		case "TUPLE":
			ZonedDateTime time = (java.time.ZonedDateTime) next.getObject(colName);
			if (time==null) {
				return null;
			}
			Calendar value = java.util.GregorianCalendar.from(time);
			TimeZone tz = TimeZone.getTimeZone(time.getOffset().getId());
			value.setTimeZone(tz);
			return value;
		case "TIMESTAMP":
			return next.getLocalTime(colName);
		case "BLOB":
			ByteBuffer bytes = next.getByteBuffer(colName);
			if (bytes==null) {
				return null;
			}
			return SerializationUtils.deserialize(bytes.array());
		default: 
			return null;
		}
}
}
