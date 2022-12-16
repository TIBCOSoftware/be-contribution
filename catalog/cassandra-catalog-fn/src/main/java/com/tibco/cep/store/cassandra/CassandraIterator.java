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

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.TableMetadata;
import com.tibco.cep.store.Item;
import com.tibco.cep.store.StoreIterator;

/**
 * @author rakulkar
 *
 */
public class CassandraIterator extends StoreIterator {

	private CassandraStoreContainer cassandraStoreContainer;

	public CassandraIterator(ResultSet resultSet, String returnEntityPath) {
		super(resultSet.iterator(), returnEntityPath);
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

		for (Definition col : colDefns) {
			String colName = col.getName();
			DataType type = col.getType();
			Object value = getValue(next, colName, type);
			keyValueMap.put(colName.toLowerCase(), value);
		}
		returnItem.setKeyValueMap(keyValueMap);

		return returnItem;
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
		switch (type.getName()) {
		case VARCHAR:
			return next.getString(colName);
		case UUID:
			return next.getUUID(colName);
		case VARINT:
			return next.getVarint(colName);
		case BIGINT:
			return next.getLong(colName);
		case INT:
			return next.getInt(colName);
		case FLOAT:
			return next.getFloat(colName);
		case DOUBLE:
			return next.getDouble(colName);
		case BOOLEAN:
			return next.getBool(colName);
		case MAP:
			return next.getMap(colName, String.class, String.class);
		case TUPLE:
			ZonedDateTime time = (java.time.ZonedDateTime) next.getObject(colName);
			if (time == null) {
				return null;
			}
			Calendar value = java.util.GregorianCalendar.from(time);
			TimeZone tz = TimeZone.getTimeZone(time.getOffset().getId());
			value.setTimeZone(tz);
			return value;
		case TIMESTAMP:
			return next.getTimestamp(colName);
		case BLOB:
			ByteBuffer bytes = next.getBytes(colName);
			if (bytes == null) {
				return null;
			}
			return SerializationUtils.deserialize(bytes.array());
		default:
			return null;
		}
	}
}
