package com.tibco.cep.store.cassandra;

import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.SerializationUtils;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Using;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.store.StoreContainer;
import com.tibco.cep.store.cassandra.serializer.CassandraRowCodec;

/**
 * @author rakulkar
 */
public class CassandraStoreContainer extends StoreContainer<CassandraStoreItem> {

	private Logger logger;
	private Session session;
	private TableMetadata tableMetadata;
	private static ThreadLocal<BatchStatement> txBatchStatement = new ThreadLocal<BatchStatement>()
	{
		protected BatchStatement initialValue() {return null;}
	};
	private static ThreadLocal<Boolean> isTxExecution = new ThreadLocal<Boolean>()
	{
		protected Boolean initialValue() {return false;}
	};
	
	public CassandraStoreContainer(String containerName) {
		super(containerName);
		logger = LogManagerFactory.getLogManager().getLogger(CassandraRowCodec.class);
	}

	@Override
	public CassandraStoreItem createItem() throws Exception {
		return new CassandraStoreItem(this);
	}

	@Override
	protected void putItem(CassandraStoreItem item) throws Exception {
		insertOrUpdate(item);
	}

	private void insertOrUpdate(CassandraStoreItem item) {
		if (!isTxExecution.get()) {
			txBatchStatement.set(new BatchStatement(BatchStatement.Type.LOGGED));
		}
		
		Map<String,Object> colNamesVals = item.getKeyValueMap();
		
		Insert insertQuery = QueryBuilder.insertInto(name);
		for (String colName : colNamesVals.keySet()) {
			Object value = colNamesVals.get(colName);
			insertQuery.value(colName, value);
		}
		
		updateQueryWithTtl(item, insertQuery);
		
		
		txBatchStatement.get().add(insertQuery);
		if (!isTxExecution.get()) {
			executeTxBatchStatement();
		}
	}

	private void updateQueryWithTtl(CassandraStoreItem item, Insert insertQuery) {
		try {
			long ttl;
			ttl = item.getExpiration();
			if (ttl<0) {
				ttl=0;
			}
			insertQuery.using(QueryBuilder.ttl((int) ttl));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void executeTxBatchStatement() {
		if (txBatchStatement.get()!=null && txBatchStatement.get().size()>0) {
			session.execute(txBatchStatement.get());
			txBatchStatement.get().clear();
		}
	}

	@Override
	protected CassandraStoreItem getItem(CassandraStoreItem item) throws Exception {
		CassandraStoreItem returnItem = null;
		Map keyValueMap = null;
		String[] primaryKeys = getPrimaryKeyNames();
		Select selectQuery = QueryBuilder.select().from(name);
		for (String primaryKey : primaryKeys) {
			Object itemValue = item.getFieldValue(primaryKey);
			selectQuery.where(QueryBuilder.eq(primaryKey, itemValue));
		}
		ResultSet rs = session.execute(selectQuery);
		if (rs != null && rs.iterator().hasNext()) {
			returnItem = new CassandraStoreItem(this);
			keyValueMap = returnItem.getKeyValueMap();
			for (Row next : rs) {
				ColumnDefinitions colDefns = next.getColumnDefinitions();
				
				for (Definition col : colDefns) {
					String colName = col.getName();
						DataType type = col.getType();
						Object value = getValue(next, colName, type);
						keyValueMap.put(colName.toLowerCase(), value);
				}
			}
			returnItem.setKeyValueMap(keyValueMap);
		}
		
		return returnItem;
	}

	private Object getValue(Row next, String colName, DataType type) {
			switch(type.getName()){
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
				if (time==null) {
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
				if (bytes==null) {
					return null;
				}
				return SerializationUtils.deserialize(bytes.array());
			default: 
				return null;
			}
	}

	@Override
	protected void deleteItem(CassandraStoreItem item) throws Exception {
		if (!isTxExecution.get()) {
			txBatchStatement.set(new BatchStatement(BatchStatement.Type.LOGGED));
		}
		String[] primaryKeys = getPrimaryKeyNames();
		Delete deleteQuery = QueryBuilder.delete().from(name);
		for (String primaryKey : primaryKeys) {
			Object itemValue = item.getFieldValue(primaryKey);
			deleteQuery.where(QueryBuilder.eq(primaryKey, itemValue));
		}
		txBatchStatement.get().add(deleteQuery);
		if (!isTxExecution.get()) {
			executeTxBatchStatement();
		}
	}

	@Override
	protected void updateItem(CassandraStoreItem item) throws Exception {
		insertOrUpdate(item);
	}

	@Override
	protected void close() throws Exception {
		
	}

	@Override
	protected String[] getPrimaryKeyNames() throws Exception {
		List<String> primaryIndexNames = new ArrayList<String>();
		List<ColumnMetadata> primaryKeyMetaData = tableMetadata.getPrimaryKey();
		for (Iterator iterator = primaryKeyMetaData.iterator(); iterator.hasNext();) {
			ColumnMetadata columnMetadata = (ColumnMetadata) iterator.next();
			primaryIndexNames.add(columnMetadata.getName());
		}
		return primaryIndexNames.toArray(new String[0]);
	}

	public void init(Session session, ThreadLocal<BatchStatement> txBatchStatement, ThreadLocal<Boolean> isTxExecution) {
		this.session = session;
		this.txBatchStatement = txBatchStatement;
		this.isTxExecution = isTxExecution;
	}

	public void setTableMetadata(TableMetadata tableMetadata) {
		this.tableMetadata = tableMetadata;
	}
}
