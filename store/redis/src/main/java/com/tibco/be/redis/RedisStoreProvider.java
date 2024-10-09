/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.redis;

import java.io.File;
import java.io.Serializable;
import java.security.KeyStore;
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
import com.tibco.cep.repo.ArchiveResourceProvider;
import com.tibco.cep.repo.BEProject;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.runtime.service.cluster.Cluster;
import com.tibco.cep.runtime.service.security.BEIdentity;
import com.tibco.cep.runtime.service.security.BEIdentityUtilities;
import com.tibco.cep.runtime.service.security.BEKeystoreIdentity;
import com.tibco.cep.runtime.service.store.StoreProviderConfig;
import com.tibco.cep.store.custom.BaseStoreProvider;
import com.tibco.cep.store.custom.StoreColumnData;
import com.tibco.cep.store.custom.StoreDataTypeMapper;
import com.tibco.cep.store.custom.StoreHelper;
import com.tibco.cep.store.custom.StoreRowHolder;
import com.tibco.cep.store.custom.StoreSSLUtils;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SslOptions;
import io.lettuce.core.RedisURI.Builder;
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
		lockProvider = RedisLockProvider.INSTANCE;
	}

	public static BaseStoreProvider getStoreProviderInstance() {
		return INSTANCE;
	}
	
	@Override
	public void commit() {
		try {
			syncCommands.get().exec();
		} catch (Exception e) {
			getLogger().log(Level.ERROR, e.getMessage());
			throw new RuntimeException(e);
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
		Boolean useSsl = Boolean.parseBoolean(storeConfigProperties.getProperty("isSecurityEnabled", "false"));
		String dbName = storeConfigProperties.getProperty("dbName", "default");
		String password = storeConfigProperties.getProperty(RedisConstants.PROPERTY_KEY_REDIS_AUTH_PASSWORD,"");
				
		Builder redisBuilder = RedisURI.Builder.redis(host, port).withSsl(useSsl);
		
		if (password!=null && password!="") {
			redisBuilder.withPassword(RedisStoreUtil.decrypt(password).toCharArray());
		}
		else
		{ 
			RedisStoreUtil.restoreProviders();
		}
		
		RedisClient redisClient = RedisClient.create(redisBuilder.build());
		RediSearchClient redisSearchClient = RediSearchClient.create(redisBuilder.withDatabase(database).build());
		if (useSsl) {
			ClientOptions clientOptions = getClientOptions(storeConfigProperties);
			redisClient.setOptions(clientOptions);
			redisSearchClient.setOptions(clientOptions);
		}

		StatefulRediSearchConnection<String, String> sConnection = redisSearchClient.connect();

		pool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(),
				new GenericObjectPoolConfig());

		if (sConnection != null) {
			searchCommands = sConnection.sync();
			getLogger().log(Level.INFO, "Connected to Redis Search ." + searchCommands.ping());
		} else {
			getLogger().log(Level.ERROR, "Problem encountered while connecting to Redis.");
		}
	}

	/**
	 * @param storeConfigProperties
	 * @return
	 */
	private ClientOptions getClientOptions(Properties storeConfigProperties) {

		String trustStoreFilePath = storeConfigProperties
				.getProperty(RedisConstants.PROPERTY_KEY_SSL_TRUSTED_CERTIFICATE_FOLDER_PATH);
		String identityFile = storeConfigProperties.getProperty(RedisConstants.PROPERTY_KEY_SSL_IDENTITY_FILE_PATH);
		String trustStorePassword = storeConfigProperties
				.getProperty(RedisConstants.PROPERTY_KEY_SSL_TRUSTED_STORE_PASSWORD);

		SslOptions sslOptions = null;
		ClientOptions clientOptions = ClientOptions.create();

		if (null != trustStoreFilePath && null != identityFile && null != trustStorePassword
				&& !trustStoreFilePath.trim().isEmpty() && !trustStorePassword.trim().isEmpty()
				&& !identityFile.trim().isEmpty()) {

			KeyStore keyStore;
			try {
				keyStore = StoreSSLUtils.createKeystore(trustStoreFilePath, trustStorePassword,
						(BEProject) rsp.getProject(), rsp.getGlobalVariables(), true);

				if (keyStore.size() == 0) {
					throw new Exception("Trusted Certificates are incorrect.");
				}

				String trustStoreFile = StoreSSLUtils.storeKeystore(keyStore, trustStorePassword);
				BEIdentity keyStoreIdentity = getIdentity(identityFile,
						rsp.getProject().getSharedArchiveResourceProvider(), rsp.getGlobalVariables());

				if (keyStoreIdentity != null && keyStoreIdentity instanceof BEKeystoreIdentity) {
					String keyStorePath = ((BEKeystoreIdentity) keyStoreIdentity).getStrKeystoreURL();

					sslOptions = SslOptions.builder().jdkSslProvider()
							.truststore(new File(trustStoreFile), trustStorePassword)
							.keystore(new File(keyStorePath),
									((BEKeystoreIdentity) keyStoreIdentity).getStrStorePassword().toCharArray())
							.keyStoreType(((BEKeystoreIdentity) keyStoreIdentity).getStrStoreType())
							.build();

					clientOptions = ClientOptions.builder().sslOptions(sslOptions).build();

				}

			} catch (Exception e) {
				getLogger().log(Level.ERROR, e.getMessage());
				throw new RuntimeException(e);
			}
		}

		return clientOptions;
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
			getLogger().log(Level.ERROR, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void startTransaction() {
		try {
			isTxExecution.set(true);
			borrowConnectionFromPool();
			syncCommands.get().multi();
		} catch (Exception e) {
			getLogger().log(Level.ERROR, e.getMessage());
			throw new RuntimeException(e);
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
						throw re;
					} finally {
						if (lock != null) {
							lock.unlock();
						}
					}
				} else {
					if (lock != null) {
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
	
	/**
	 * Adds or updates a Hash
	 * 
	 * @param hName
	 * @param rowHolder
	 * @param oldMemberId
	 * @param isInsert
	 * @return boolean success or failure status of the addOrUpdate method
	 * */
	public boolean addOrUpdateHashRowHolder(String hName, StoreRowHolder rowHolder, String oldMemberId, boolean isInsert) throws Exception {
		boolean isSuccess = false;
		List<StoreRowHolder> rowHolders = new ArrayList<>();
		rowHolders.add(rowHolder);
		try {
			StoreRowHolder entryFromRedis = read(rowHolder);

			if (isInsert) {
				// Check if the entry already exists in Redis
				// Add a new entry only if it does not already exist
				if (null == entryFromRedis) {
					write(rowHolders);
					isSuccess = true;
				}
			} else {
				if (null != entryFromRedis) {
					// Update the entry in Redis
					// Create a hmap that will search for an entry with the oldMemberId
					Map<String, String> searchOldHMap = new HashMap<String, String>();
					searchOldHMap.put(RedisConstants.LOCKS_KEY_FIELD, rowHolder.getColDataMap().get(RedisConstants.LOCKS_KEY_FIELD).getColumnValue().toString());
					searchOldHMap.put(RedisConstants.LOCKS_MEMBERID_FIELD, oldMemberId);
					searchOldHMap.put(RedisConstants.LOCKS_SOCKET_FIELD, rowHolder.getColDataMap().get(RedisConstants.LOCKS_SOCKET_FIELD).getColumnValue().toString());
					
					// Create a hmap that will store the entry from redis
					Map<String, String> newHMap = new HashMap<String, String>();
					newHMap.put(RedisConstants.LOCKS_KEY_FIELD, entryFromRedis.getColDataMap().get(RedisConstants.LOCKS_KEY_FIELD).getColumnValue().toString());
					newHMap.put(RedisConstants.LOCKS_MEMBERID_FIELD, entryFromRedis.getColDataMap().get(RedisConstants.LOCKS_MEMBERID_FIELD).getColumnValue().toString());
					newHMap.put(RedisConstants.LOCKS_SOCKET_FIELD, entryFromRedis.getColDataMap().get(RedisConstants.LOCKS_SOCKET_FIELD).getColumnValue().toString());
					
					// Update the entry only if the hmap entry from Redis matches the searchOldMap entry (which has oldMemberId)
					if (newHMap.equals(searchOldHMap)) {
						write(rowHolders);
						isSuccess = true;
					}
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.ERROR, "Redis store lock :: Failed while trying to insert/update entry in Redis store", e);
			isSuccess = false;
		}
		return isSuccess;
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
			getLogger().log(Level.DEBUG, "####Index " + indexName + "not exists...");
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

	
	/**
	 * Get the identity file path
	 * 
	 * @param idReference
	 * @param provider
	 * @param gv
	 * @return
	 * @throws Exception
	 */
	private static BEIdentity getIdentity(String idReference, ArchiveResourceProvider provider, GlobalVariables gv)
			throws Exception {
		BEIdentity beIdentity = null;
		if ((idReference != null) && !idReference.trim().isEmpty()) {
			if (idReference.startsWith("/")) {
				beIdentity = BEIdentityUtilities.fetchIdentity(provider, gv, idReference);
			} else {
				throw new Exception("Incorrect Identitty : " + idReference);
			}
		}
		return beIdentity;
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
