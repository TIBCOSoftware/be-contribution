/**
 * # Copyright (c) 2019-2020. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */
package com.tibco.be.redis;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.redislabs.lettusearch.RediSearchClient;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.Schema.SchemaBuilder;
import com.redislabs.lettusearch.index.field.NumericField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.runtime.service.cluster.Cluster;
import com.tibco.cep.runtime.service.store.StoreProviderConfig;
import com.tibco.cep.store.custom.BaseStoreProvider;
import com.tibco.cep.store.custom.StoreColumnData;
import com.tibco.cep.store.custom.StoreDataTypeMapper;
import com.tibco.cep.store.custom.StoreHelper;
import com.tibco.cep.store.custom.StoreRowHolder;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;

/**
 * 
 * @author TIBCO Software
 * 
 *         This is entry point for Redis store implementation. This class will
 *         help to perform write, read and aggregations on redis store data.
 */
public class RedisStoreProvider extends BaseStoreProvider {
	private static final String _IDX = "_idx";
	private static final String PRIMARY = "PRIMARY";
	private static ThreadLocal<StatefulRedisConnection<String, String>> connection = new ThreadLocal<StatefulRedisConnection<String, String>>();
	private static ThreadLocal<RedisCommands<String, String>> syncCommands = new ThreadLocal<RedisCommands<String, String>>();
	private static ThreadLocal<Boolean> isTxExecution = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return false;
		}
	};
	private static RediSearchCommands<String, String> searchCommands;
	private static GenericObjectPool<StatefulRedisConnection<String, String>> pool;
	private static Map<String, Map<String, String>> dtCache = new HashMap<>();
	private Lock lock = new ReentrantLock();

	private static List<String> existingIndexNames = new ArrayList<>();

	public RedisStoreProvider(Cluster cluster, StoreProviderConfig storeConfig) throws Exception {
		super(cluster, storeConfig);
	}

	@Override
	public void commit() {
		try {
			syncCommands.get().exec();
		} catch (Exception e) {

		}
	}

	@Override
	public void delete(List<StoreRowHolder> queryHolderList) throws Exception {
		for (StoreRowHolder queryHolder : queryHolderList) {
			delete_(queryHolder);
		}
	}

	@Override
	public void endTransaction() {
		closeConnection();
		isTxExecution.set(false);
	}

	private void closeConnection() {
		if (connection.get() != null) {
			connection.get().close();
			connection.set(null);
		}
	}

	@Override
	public StoreDataTypeMapper getStoreDataTypeMapper() {
		return RedisStoreDataTypeMapper.getInstance();
	}

	@Override
	protected void initConnection(Properties storeConfigProperties) throws Exception {
		String host = storeConfigProperties.getProperty("host", "localhost");
		Integer port = Integer.parseInt(storeConfigProperties.getProperty("port", "6379"));
		Integer database = Integer.parseInt(storeConfigProperties.getProperty("database", "0")); // Lettusearch only
																									// supports
																									// connection to db
																									// 0?

		RedisClient redisClient = RedisClient.create("redis://" + host + ":" + port + "/" + database);
//		connection = redisClient.connect();
		RediSearchClient redisSearchClient = RediSearchClient
				.create(RedisURI.create("redis://" + host + ":" + port + "/" + database));
		StatefulRediSearchConnection<String, String> sConnection = redisSearchClient.connect();

		pool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(),
				new GenericObjectPoolConfig());

//		if (connection != null) {
//			syncCommands = connection.sync();
		if (sConnection != null) {
			searchCommands = sConnection.sync();
			getLogger().log(Level.INFO, "Connected to Redis Search ." + searchCommands.ping());
		} else {
			getLogger().log(Level.ERROR, "Problem encountered while connecting to Redis.");
		}
	}

	@Override
	public StoreRowHolder read(StoreRowHolder queryHolder) throws Exception {
		List<StoreRowHolder> result = readFromDb(queryHolder);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public List<StoreRowHolder> readAll(StoreRowHolder queryHolder) throws Exception {
		return readFromDb(queryHolder);
	}

	@Override
	public List<StoreRowHolder> readAvg(StoreRowHolder queryHolder) throws Exception {
		return readAggregate(queryHolder, "AVG");
	}

	@Override
	public List<StoreRowHolder> readCount(StoreRowHolder queryHolder) throws Exception {
		return readAggregate(queryHolder, "COUNT");
	}

	@Override
	public List<StoreRowHolder> readMax(StoreRowHolder queryHolder) throws Exception {
		return readAggregate(queryHolder, "MAX");
	}

	@Override
	public List<StoreRowHolder> readMin(StoreRowHolder queryHolder) throws Exception {
		return readAggregate(queryHolder, "MIN");
	}

	@Override
	public List<StoreRowHolder> readSum(StoreRowHolder queryHolder) throws Exception {
		return readAggregate(queryHolder, "SUM");
	}

	@Override
	public void rollback() {
		try {
			syncCommands.get().discard();
		} catch (Exception e) {

		}
	}

	@Override
	public void startTransaction() {
		try {
			isTxExecution.set(true);
			borrowConnectionFromPool();
			syncCommands.get().multi();
		} catch (Exception e) {

		}
	}

	@Override
	public void update(List<StoreRowHolder> storeRowHolder) throws Exception {
		write(storeRowHolder);
	}

	@Override
	public void write(List<StoreRowHolder> storeRowHolder) throws Exception {
		try {
			if (!isTxExecution.get()) {
				borrowConnectionFromPool();
			}
			// implementation for new ID
			storeRowHolder.forEach((rowData) -> {
				String tableName = rowData.getTableName();
				Map<String, String> allValuesMap = new HashMap<String, String>();
				Map<String, String> keyValueMap = new HashMap<String, String>();
				Map<String, String> dataTypeMap = searchCommands.hgetall(tableName);

				rowData.getColDataMap().forEach((colName, storeColData) -> {
					int i = 0;
					String colValueString = "";
					String colDataType = (String) storeColData.getColumnType();
					colDataType = colDataType == null ? "STRING" : colDataType;
					dataTypeMap.put(colName, colDataType);

					// handling columnType
					if (colDataType != null && colDataType.equals("DATETIME")) {
						Calendar cal = (Calendar) storeColData.getColumnValue();
						SimpleDateFormat sdf;
						sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
						sdf.setTimeZone(cal.getTimeZone());
						colValueString = sdf.format(cal.getTime());
					} else if (colDataType != null && "OBJECT".equalsIgnoreCase(colDataType.toString())) {
						colValueString = DatatypeConverter.printHexBinary(
								SerializationUtils.serialize((Serializable) storeColData.getColumnValue()));
					} else {
						colValueString = null == storeColData.getColumnValue() ? "null"
								: storeColData.getColumnValue().toString();
					}

					// if primary key then set key as tablename:primarykey
					if (storeColData.isPrimary()) {
						keyValueMap.putIfAbsent(PRIMARY, "");
						keyValueMap.put(PRIMARY, keyValueMap.get(PRIMARY) + ":" + colValueString);
						keyValueMap.put(colName + ":" + "Indexed" + i++, colValueString);
						getLogger().log(Level.INFO, colName + ":" + colValueString);
					}

					if (storeColData.getIsIndexed()) {
						keyValueMap.put(colName + ":" + "Indexed" + i++, colValueString);
					}
					allValuesMap.put(colName, RedisStoreUtil.sanitizeValue(colValueString)); // hset is case sensitive
				});

				// Record key is set as TableName:Primarykey
				String recordName = tableName + keyValueMap.get(PRIMARY);

				// if Primary key don't exist use UUID
				final String finalRecordName = keyValueMap.get(PRIMARY) != null ? recordName
						: UUID.randomUUID().toString();

				long ttl = rowData.getTtl();

				dtCache.put(tableName, dataTypeMap);

				syncCommands.get().hset(rowData.getTableName(), dataTypeMap);

				// creating search index for indexed columns
				String indexName = rowData.getTableName().toLowerCase() + _IDX;
				if (lock != null) {
					lock.lock();
				}
				getLogger().log(Level.INFO, "Thread aquired lock::" + Thread.currentThread());
				if (!isIndexAlreadyExists(indexName)) {
					CreateOptions createOptions = CreateOptions.builder().prefixes(rowData.getTableName() + ":")
							.build();

					SchemaBuilder<String> schemaBuilder = Schema.<String>builder();
					for (Iterator<String> iterator = keyValueMap.keySet().iterator(); iterator.hasNext();) {
						String key = iterator.next();
						if (PRIMARY.equalsIgnoreCase(key)) {
							continue;
						}
						String fieldName = key.split(":")[0];
						if (RedisStoreUtil.isFieldNumeric(dataTypeMap.get(fieldName))) {
							schemaBuilder = schemaBuilder
									.field(NumericField.<String>builder().name(fieldName).sortable(true).build());
						} else {
							schemaBuilder = schemaBuilder
									.field(TextField.<String>builder().name(fieldName).sortable(true).build());
						}

					}
					try {
						searchCommands.create(indexName, schemaBuilder.build(), createOptions);
					} catch (RedisCommandExecutionException re) {
						getLogger().log(Level.INFO, "Index creation failed: " + Thread.currentThread());
						throw re;
					} finally {
						if (lock != null) {
							getLogger().log(Level.INFO, "Thread releasing lock::" + Thread.currentThread());
							lock.unlock();
						}
					}
				} else {
					if (lock != null) {
						getLogger().log(Level.INFO, "Thread releasing lock::" + Thread.currentThread());
						lock.unlock();
					}
				}

				syncCommands.get().hset(finalRecordName, allValuesMap);
				if (ttl > 0) {
					syncCommands.get().expire(finalRecordName, rowData.getTtl());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (!isTxExecution.get()) {
				closeConnection();
			}
		}
	}

	@Override
	protected boolean isConnectionAlive() {
		try {
			borrowConnectionFromPool();
			if (syncCommands.get().isOpen() && searchCommands.isOpen()) {
				return true;
			}
		} catch (Exception e) {

		}

		return false;
	}

	@Override
	public void delete(StoreRowHolder queryHolder) throws Exception {
		delete_(queryHolder);
	}

	private void delete_(StoreRowHolder queryHolder) {
		try {
			if (!isTxExecution.get()) {
				borrowConnectionFromPool();
			}
			List<String> docIdsToBeRemoved = getDocIdsToRemove(queryHolder);
			docIdsToBeRemoved.forEach((docId) -> {
				List<String> keys = searchCommands.hkeys(docId);
				syncCommands.get().hdel(docId, keys.toArray(new String[keys.size()]));
				getLogger().log(Level.DEBUG, "Deleted : " + docId);
			});
		} catch (Exception e) {
			getLogger().log(Level.ERROR, "Problem while aquiring connection during transaction: " + e.getMessage());
		} finally {
			if (!isTxExecution.get()) {
				closeConnection();
			}
		}
	}

	private void borrowConnectionFromPool() throws Exception {
		if (connection.get() == null) {
			StatefulRedisConnection<String, String> con = pool.borrowObject();
			connection.set(con);
			syncCommands.set(con.sync());
		}
	}

	private List<StoreRowHolder> readFromDb(StoreRowHolder queryHolder) {
		List<StoreRowHolder> result = new ArrayList<>();
		String tableName = queryHolder.getTableName();
		String[] selectList = queryHolder.getSelectList();
		String indexName = tableName.toLowerCase() + _IDX;

		Map<String, String> dtMapping;
		if (!dtCache.containsKey(tableName) || checkIfNullOrEmpty(dtCache.get(tableName))) {
			dtMapping = searchCommands.hgetall(tableName);
			dtCache.put(tableName, dtMapping);
		} else {
			dtMapping = dtCache.get(tableName);
		}
		if (dtMapping.isEmpty()) {
			return result;
		}

		StringBuffer query = createSelectQuery(queryHolder, dtMapping);

		boolean isIndexExists = isIndexAlreadyExists(indexName);
		if (isIndexExists) {
			getLogger().log(Level.DEBUG, "Index query :: " + query.toString());

			SearchResults<String, String> results;
			if (selectList == null || selectList[0] == null) {
				results = searchCommands.search(indexName, query.toString());
			} else {
				SearchOptions<String> searchOptions = SearchOptions.<String>builder()
						.returnFields(Arrays.asList(selectList)).build();
				results = searchCommands.search(indexName, query.toString(), searchOptions);
			}

			for (Iterator<Document<String, String>> iterator = results.iterator(); iterator.hasNext();) {
				List<Object> dataTypeList = new ArrayList<Object>();
				List<String> colNameList = new ArrayList<String>();
				List<Object> colValueList = new ArrayList<Object>();

				Document<String, String> document = iterator.next();
				document.forEach((colName, value) -> {
					colNameList.add(colName);
					dataTypeList.add(dtMapping.get(colName));
					colValueList.add(RedisStoreUtil.getValue((String) dtMapping.get(colName), value));
				});
				result.add(StoreHelper.getRow(queryHolder.getTableName(), dataTypeList.toArray(),
						colNameList.toArray(new String[colNameList.size()]), colValueList.toArray()));
			}
		}
		return result;
	}

	private StringBuffer createSelectQuery(StoreRowHolder queryHolder, Map<String, String> dtMapping) {
		Map<String, StoreColumnData> colDataMap = queryHolder.getColDataMap();
		Map<String, StoreColumnData> filterData = queryHolder.getFiltersDataMap(); // filterdata like where,limit and
																					// groupby is part of query
																					// filterbuilder which is designed
																					// for sql like syntax. We will
																					// handle it in 2nd cut.
		StringBuffer rsQuery = new StringBuffer();
		StringBuffer whereClause = new StringBuffer();

		if (!checkIfNullOrEmpty(colDataMap)) {
			for (Entry<String, StoreColumnData> fieldEntry : colDataMap.entrySet()) {
				String fieldName = fieldEntry.getKey();
				StoreColumnData fieldData = fieldEntry.getValue();
				String fieldOperator = fieldData.getOperator();
				fieldOperator = fieldOperator == null ? "=" : fieldOperator;
				String fieldValue = fieldData.getColumnValue().toString();

				whereClause.append("(");
				if (fieldOperator.equals("!=")) {
					whereClause.append("-");
				}
				if (fieldName.equalsIgnoreCase("extId")) {
					fieldName = "extid";
				}
				whereClause.append("@").append(fieldName).append(":").append(convertAndAppendFieldValue(fieldOperator,
						fieldValue, RedisStoreUtil.isFieldNumeric(dtMapping.get(fieldName)))).append(")");
			}
		} else {
			// TODO WHERE Clause from query filter builder , below implementation supports
			// filter condition in aggregation functions.
			// filterdata like where,limit and groupby is part of query filterbuilder which
			// is designed for sql like syntax. We will handle it in 2nd cut.

			if (!checkIfNullOrEmpty(filterData) && filterData.containsKey("where")) {
				if (null != filterData.get("where").getColumnValue()) {
					whereClause.append(((StoreColumnData) filterData.get("where")).getColumnValue());
				}
			}
		}
		if (whereClause.length() > 0) {
			rsQuery.append(whereClause);
		} else {
			rsQuery.append("*"); // No where clause - select all docs in index
		}
		return rsQuery;
	}

	private boolean isIndexAlreadyExists(String indexName) {
		if (existingIndexNames.contains(indexName)) {
			return true;
		}
		try {
			@SuppressWarnings("unused")
			List<Object> indexInfo = searchCommands.ftInfo(indexName);
			existingIndexNames.add(indexName);
		} catch (RedisCommandExecutionException redisCmdException) {
			getLogger().log(Level.INFO, "####Index " + indexName + "not exists...");
			return false;
		}
		return true;
	}

	private String convertAndAppendFieldValue(String fieldOperator, String fieldValue, boolean isNumeric) {
		if ("=".equalsIgnoreCase(fieldOperator) && !isNumeric) {
			return RedisStoreUtil.sanitizeValue(fieldValue);
		}
		if ("=".equalsIgnoreCase(fieldOperator) && isNumeric) {
			return "[" + fieldValue + " " + fieldValue + "]";
		}
		if (">".equalsIgnoreCase(fieldOperator)) {
			return "[(" + fieldValue + " +inf]";
		}
		if (">=".equalsIgnoreCase(fieldOperator)) {
			return "[" + fieldValue + " +inf]";
		}
		if ("<".equalsIgnoreCase(fieldOperator)) {
			return "[-inf (" + fieldValue + "]";
		}
		if ("<=".equalsIgnoreCase(fieldOperator)) {
			return "[-inf " + fieldValue + "]";
		}
		return "\"" + fieldValue + "\"";
	}

	private boolean checkIfNullOrEmpty(Map<?, ?> colDataMap) {
		if (colDataMap == null || colDataMap.isEmpty()) {
			return true;
		}
		return false;
	}

	private List<StoreRowHolder> readAggregate(StoreRowHolder queryHolder, String aggFunction) {
		List<StoreRowHolder> result = new ArrayList<>();
		Map<String, String> dtMapping;
		String tableName = queryHolder.getTableName();
		String indexName = tableName.toLowerCase() + _IDX;
		String[] selectList = queryHolder.getSelectList();
		List<String> groupByColSet = new LinkedList<String>();

		if (!dtCache.containsKey(tableName)) {
			dtMapping = searchCommands.hgetall(tableName);
			dtCache.put(tableName, dtMapping);
		} else {
			dtMapping = dtCache.get(tableName);
		}
		if (dtMapping.isEmpty()) {
			return result;
		}
		StringBuffer query = createSelectQuery(queryHolder, dtMapping);

		Map<String, StoreColumnData> filterData = queryHolder.getFiltersDataMap();
		if (!checkIfNullOrEmpty(filterData) && filterData.containsKey("group by")) {
			StoreColumnData groupByData = filterData.get("group by");
			String groupByClause = (String) groupByData.getColumnValue();

			if (groupByClause != null && !groupByClause.trim().equals("")) {
				String[] groupByFieldNames = groupByClause.split(",");
				for (String groupByFieldName : groupByFieldNames) {
					groupByColSet.add(groupByFieldName);
				}
			}
		}
		boolean isIndexExists = isIndexAlreadyExists(indexName);
		if (isIndexExists) {
			getLogger().log(Level.DEBUG, "Index query :: " + query.toString());
			AggregateResults<String, String> aggregationResults;
			AggregateOptions aggregateOptions = AggregateOptionsBuilder.build(aggFunction, groupByColSet,
					selectList[0]);
			aggregationResults = searchCommands.aggregate(indexName, query.toString(), aggregateOptions);
			getLogger().log(Level.DEBUG, "Aggregation Complete , Found " + aggregationResults.getCount() + " Results.");

			for (Iterator iterator = aggregationResults.iterator(); iterator.hasNext();) {
				List<Object> dataTypeList = new ArrayList<Object>();
				List<String> colNameList = new ArrayList<String>();
				List<Object> colValueList = new ArrayList<Object>();

				LinkedHashMap<String, String> document = (LinkedHashMap<String, String>) iterator.next();
				document.forEach((colName, value) -> {
					colNameList.add(colName);
					if (colName.equalsIgnoreCase(aggFunction)) {
						dataTypeList.add("DOUBLE");
						colValueList.add(RedisStoreUtil.getValue("DOUBLE", value));
					} else {
						dataTypeList.add(dtMapping.get(colName));
						colValueList.add(RedisStoreUtil.getValue((String) dtMapping.get(colName), value));
					}
				});
				result.add(StoreHelper.getRow(queryHolder.getTableName(), dataTypeList.toArray(),
						colNameList.toArray(new String[colNameList.size()]), colValueList.toArray()));
			}
		}
		return result;
	}

	private List<String> getDocIdsToRemove(StoreRowHolder queryHolder) {
		List<String> result = new ArrayList<>();
		String tableName = queryHolder.getTableName();
		String[] selectList = queryHolder.getSelectList();
		String indexName = tableName.toLowerCase() + _IDX;

		Map<String, String> dtMapping;
		if (!dtCache.containsKey(tableName)) {
			dtMapping = searchCommands.hgetall(tableName);
			dtCache.put(tableName, dtMapping);
		} else {
			dtMapping = dtCache.get(tableName);
		}
		if (dtMapping.isEmpty()) {
			return result;
		}

		StringBuffer query = createSelectQuery(queryHolder, dtMapping);

		boolean isIndexExists = isIndexAlreadyExists(indexName);
		if (isIndexExists) {
			getLogger().log(Level.DEBUG, "Delete Index query :: " + query.toString());

			SearchResults<String, String> results;
			if (selectList == null || selectList[0] == null) {
				results = searchCommands.search(indexName, query.toString());
			} else {
				SearchOptions<String> searchOptions = SearchOptions.<String>builder()
						.returnFields(Arrays.asList(selectList)).build();
				results = searchCommands.search(indexName, query.toString(), searchOptions);
			}

			for (Iterator<Document<String, String>> iterator = results.iterator(); iterator.hasNext();) {

				Document<String, String> document = iterator.next();
				result.add(document.getId());
			}
		}
		return result;
	}
}
