package com.tibco.cep.store.cassandra;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.SerializationUtils;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ColumnDefinition;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.delete.DeleteSelection;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.store.StoreContainer;
import com.tibco.cep.store.cassandra.serializer.CassandraRowCodec;

/**
 * @author rakulkar
 */
public class CassandraStoreContainer extends StoreContainer<CassandraStoreItem> {

	private CqlSession session;
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
		LogManagerFactory.getLogManager().getLogger(CassandraRowCodec.class);
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
			txBatchStatement.set(BatchStatement.builder(BatchType.LOGGED).build());
		}
		
		Map<String,Object> colNamesVals = item.getKeyValueMap();
		InsertInto insertQuery = QueryBuilder.insertInto(name);
		RegularInsert rInsert = null;
		for (String colName : colNamesVals.keySet()) {
			Object value = colNamesVals.get(colName);
				if (rInsert==null) {
					rInsert = insertQuery.value(colName, literal(value, session.getContext().getCodecRegistry()));
				}
				else
				{
					rInsert = rInsert.value(colName, literal(value, session.getContext().getCodecRegistry()));
				}
		}
		
		Insert finalInsert = updateQueryWithTtl(item, rInsert);
		
		txBatchStatement.set(txBatchStatement.get().add(finalInsert.build()));
		if (!isTxExecution.get()) {
			executeTxBatchStatement();
		}
	}

	private Insert updateQueryWithTtl(CassandraStoreItem item, RegularInsert insertQuery) {
		try {
			long ttl;
			ttl = item.getExpiration();
			if (ttl<0) {
				ttl=0;
			}
			return insertQuery.usingTtl((int) ttl);
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
		Select selectQuery = QueryBuilder.selectFrom(name).all();
		for (String primaryKey : primaryKeys) {
			Object itemValue = item.getFieldValue(primaryKey);
			selectQuery = selectQuery.where(Relation.column(primaryKey).isEqualTo(literal(itemValue)));
			
		}
		ResultSet rs = session.execute(selectQuery.build());
		if (rs != null && rs.iterator().hasNext()) {
			returnItem = new CassandraStoreItem(this);
			keyValueMap = returnItem.getKeyValueMap();
			for (Row next : rs) {
				ColumnDefinitions colDefns = next.getColumnDefinitions();
				
				for (ColumnDefinition col : colDefns) {
					String colName = col.getName().asCql(true);
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

	@Override
	protected void deleteItem(CassandraStoreItem item) throws Exception {
		if (!isTxExecution.get()) {
			txBatchStatement.set(BatchStatement.builder(BatchType.LOGGED).build());
		}
		String[] primaryKeys = getPrimaryKeyNames();
		DeleteSelection deleteQuery = QueryBuilder.deleteFrom(name);
		Delete delete = null;
		for (String primaryKey : primaryKeys) {
			Object itemValue = item.getFieldValue(primaryKey);
			delete = deleteQuery.where(Relation.column(primaryKey).isEqualTo(literal(itemValue)));
		}
		if(delete!=null) txBatchStatement.set(txBatchStatement.get().add(delete.build()));
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
			primaryIndexNames.add(columnMetadata.getName().asCql(true));
		}
		return primaryIndexNames.toArray(new String[0]);
	}

	public void init(Session session, ThreadLocal<BatchStatement> txBatchStatement, ThreadLocal<Boolean> isTxExecution) {
		this.session = (CqlSession) session;
		this.txBatchStatement = txBatchStatement;
		this.isTxExecution = isTxExecution;
	}

	public void setTableMetadata(TableMetadata tableMetadata) {
		this.tableMetadata = tableMetadata;
	}
}
