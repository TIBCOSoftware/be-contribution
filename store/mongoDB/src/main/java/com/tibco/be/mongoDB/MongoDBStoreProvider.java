package com.tibco.be.mongoDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.apache.commons.lang3.SerializationUtils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Aggregates.*;

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

public class MongoDBStoreProvider extends BaseStoreProvider {

	private static final String _ID = MongoDBConstants.PROPERTY_KEY_MONGODB_ID;
	private static MongoClientSettings settings;
	private static MongoClient mongoclient;
	private static ConnectionString connString;
	private static MongoDatabase mongodatabase;
	private static ThreadLocal<ClientSession> clientsession = new ThreadLocal<ClientSession>();
	private static ThreadLocal<Boolean> isTxExecution = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return false;
		}
	};

	public MongoDBStoreProvider(Cluster cluster, StoreProviderConfig storeConfig) throws Exception {
		super(cluster, storeConfig);

	}

	@Override
	public void commit() {
		try {
			clientsession.get().commitTransaction();
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
	public void delete(StoreRowHolder queryHolder) throws Exception {
		delete_(queryHolder);

	}

	@Override
	public void endTransaction() {
		closeConnection();
		isTxExecution.set(false);

	}

	@Override
	public StoreDataTypeMapper getStoreDataTypeMapper() {
		return MongoDBDataTypeMapper.getInstance();
	}

	@Override
	protected void initConnection(Properties storeConfigProperties) throws Exception {

		String URI = storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_MONGODB_AUTH_URI,
				"localhost:27017");
		Boolean useSsl = Boolean
				.parseBoolean(storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_SSL_ENABLED, "false"));
		String dbName = storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_MONGODB_AUTH_DB_NAME, "admin");
		String options = storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_MONGODB_AUTH_OPTIONS, "");
		String user = storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_MONGODB_AUTH_USER, "");
		String passwordEncrypted = storeConfigProperties
				.getProperty(MongoDBConstants.PROPERTY_KEY_MONGODB_AUTH_PASSWORD, "");
		Boolean isSRV = Boolean.parseBoolean(
				storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_MONGODB_AUTH_SRV_ENABLED, "false"));
		String password = "";
		
		if (passwordEncrypted != null && passwordEncrypted != "") {
			password = MongoDBUtils.decrypt(passwordEncrypted);
		}
		connString = new ConnectionString(String.format("mongodb://%s/%s", URI, dbName));
		
		if (user != null && user != "") {
			if (password != null && password != "") {
				String mongoUrl = isSRV ? "mongodb+srv://%s:%s@%s/%s" : "mongodb://%s:%s@%s/%s";
				
				if (options != null && options != "") {
						connString = new ConnectionString(String.format(mongoUrl+"?%s", user,
								password, URI, dbName, options));
						if (useSsl) {
							settings = getSSLClientSettings(storeConfigProperties, connString);					
							}
						else	
							settings = MongoClientSettings.builder().applyConnectionString(connString).build();
				}else {
						connString = new ConnectionString(String.format(mongoUrl, user,
							password, URI, dbName));
						if (useSsl) {
							settings = getSSLClientSettings(storeConfigProperties, connString);					
							}
						else
							settings = MongoClientSettings.builder().applyConnectionString(connString).build();
					}
				
			} else {
				MongoDBUtils.restoreProviders();
			}
		}
		else
		{	
			settings = MongoClientSettings.builder().applyConnectionString(connString).build();
		}
		getLogger().log(Level.DEBUG, "SSl Settings Enabled?: " + settings.getSslSettings().isEnabled());
		mongoclient = MongoClients.create(settings);
		if (mongoclient != null) {
			mongodatabase = mongoclient.getDatabase(dbName);
			getLogger().log(Level.INFO, "Connecting to MongoDB with URI: " + URI + " To DB " + dbName);

		} else {
			getLogger().log(Level.ERROR, "Problem encountered while connecting to MongoDB.");
		}
	}

	/**
	 * @param storeConfigProperties
	 * @return
	 */
	private MongoClientSettings getSSLClientSettings(Properties storeConfigProperties, ConnectionString connString) {

		String trustStoreFilePath = storeConfigProperties
				.getProperty(MongoDBConstants.PROPERTY_KEY_SSL_TRUSTED_CERTIFICATE_FOLDER_PATH);
		String identityFile = storeConfigProperties.getProperty(MongoDBConstants.PROPERTY_KEY_SSL_IDENTITY_FILE_PATH);
		String trustStorePassword = storeConfigProperties
				.getProperty(MongoDBConstants.PROPERTY_KEY_SSL_TRUSTED_STORE_PASSWORD);
		
		if (null != trustStoreFilePath && null != identityFile && null != trustStorePassword
				&& !trustStoreFilePath.trim().isEmpty() && !trustStorePassword.trim().isEmpty()
				&& !identityFile.trim().isEmpty()) {

			KeyStore trustStore;
			try {
				trustStore = StoreSSLUtils.createKeystore(trustStoreFilePath, trustStorePassword,
						(BEProject) rsp.getProject(), rsp.getGlobalVariables(), true);

				if (trustStore.size() == 0) {
					throw new Exception("Trusted Certificates are incorrect.");
				}
				BEIdentity keyStoreIdentity = getIdentity(identityFile,
						rsp.getProject().getSharedArchiveResourceProvider(), rsp.getGlobalVariables());

				if (keyStoreIdentity != null && keyStoreIdentity instanceof BEKeystoreIdentity) {
					String keyStorePath = ((BEKeystoreIdentity) keyStoreIdentity).getStrKeystoreURL();
					String keystorepassword = ((BEKeystoreIdentity) keyStoreIdentity).getStrStorePassword();
					String keystoretype = ((BEKeystoreIdentity) keyStoreIdentity).getStrStoreType();
					TrustManagerFactory tmf = null;
					tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(trustStore);
					KeyManagerFactory kmf = null;
					if (null != keyStorePath) {
						KeyStore keystore = KeyStore.getInstance(keystoretype);
						keystore.load((InputStream) new FileInputStream(new File(keyStorePath)),
								keystorepassword.toCharArray());
						kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
						kmf.init(keystore, keystorepassword.toCharArray());
					}
					SSLContext sslContext = SSLContext.getInstance("TLS");
					sslContext.init(kmf != null ? kmf.getKeyManagers() : null,
							tmf != null ? tmf.getTrustManagers() : null, new SecureRandom());

					settings = MongoClientSettings.builder().applyConnectionString(connString)
							.applyToSslSettings(builder -> {
								builder.enabled(true).context(sslContext);
							}).build();
				}

			} catch (Exception e) {
				getLogger().log(Level.ERROR, e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return settings;

	}

	@Override
	protected boolean isConnectionAlive() {
		try {
			getclientSession();
			if (clientsession.get().hasActiveTransaction()) {
				getLogger().log(Level.INFO,"*********** Is connection alive=true");
				return true;
			}
		} catch (Exception e) {
			// getLogger().log(Level.ERROR, "Problem occured with active transaction: " +
			// e.getMessage());
		}
		//getLogger().log(Level.INFO,"*********** Is connection alive=false");
		return false;
	}

	@Override
	public StoreRowHolder read(StoreRowHolder queryHolder) throws Exception {
		List<StoreRowHolder> result = readFromDB(queryHolder);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public List<StoreRowHolder> readAll(StoreRowHolder queryHolder) throws Exception {
		return readFromDB(queryHolder);
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
			clientsession.get().abortTransaction();
		} catch (Exception e) {
			getLogger().log(Level.ERROR, e.getMessage());
			throw new RuntimeException(e);
		}

	}

	@Override
	public void startTransaction() {
		try {
			isTxExecution.set(true);
			getclientSession();
			clientsession.get().startTransaction();
		} catch (Exception e) {
			getLogger().log(Level.ERROR, e.getMessage());
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(List<StoreRowHolder> storeRowHolder) throws Exception {

		write(storeRowHolder, true);
	}

	@Override
	public void write(List<StoreRowHolder> storeRowHolder) throws Exception {
		write(storeRowHolder, false);

	}

	public void write(List<StoreRowHolder> storeRowHolder, boolean isUpdate) throws Exception {

		try {
			if (!isTxExecution.get()) {
				getclientSession();
			}
			// implementation for new ID

			storeRowHolder.forEach((rowData) -> {
				String tableName = rowData.getTableName();
				MongoCollection<Document> collection = mongodatabase.getCollection(tableName);
				List<Bson> updateList = new ArrayList<Bson>();
				Document keydoc = new Document();
				rowData.getColDataMap().forEach((colName, storeColData) -> {
					String columntype = (String) storeColData.getColumnType();
					String actualcolumnvalue = "";
					if (storeColData.isPrimary()) {
						actualcolumnvalue = storeColData.getColumnValue().toString();
						keydoc.append(colName, actualcolumnvalue);
					}

					if (columntype != null && columntype.equals("DATETIME")) {
						Calendar cal = (Calendar) storeColData.getColumnValue();
						updateList.add(set(colName, cal.getTime()));
					} else if (columntype != null && "OBJECT".equalsIgnoreCase(columntype.toString())) {

						byte[] barray = SerializationUtils.serialize((Serializable) storeColData.getColumnValue());
						updateList.add(set(colName, barray));
					} else if (columntype != null && "STRING".equalsIgnoreCase(columntype.toString())) {
						updateList.add(set(colName.toLowerCase(), storeColData.getColumnValue().toString()));
					}

					else {
						updateList.add(set(colName, storeColData.getColumnValue()));
					}

					if (storeColData.getIsIndexed()) {

						collection.createIndex(Indexes.ascending(colName));
					}
					long ttl = rowData.getTtl();

					if (ttl > 0) {

						if (isUpdate) {
							if (colName.equalsIgnoreCase(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_MODIFIED)) {
								if (!checkIfIndexExist(collection,
										MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_MODIFIED)) {
									if (checkIfIndexExist(collection,
											MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED)) {
										String indexName = getIndexName(collection,
												MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED);
										collection.dropIndex(indexName);
										collection.createIndex(
												Indexes.ascending(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_MODIFIED),
												new IndexOptions().expireAfter(Long.valueOf(ttl), TimeUnit.SECONDS));
									}
								} else {

									String indexName = getIndexName(collection,
											MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_MODIFIED);
									collection.dropIndex(indexName);
									collection.createIndex(
											Indexes.ascending(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_MODIFIED),
											new IndexOptions().expireAfter(Long.valueOf(ttl), TimeUnit.SECONDS));
								}
							}
						} else {
							if (colName.equalsIgnoreCase(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED)) {
								if (!checkIfIndexExist(collection,
										MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED)) {
									collection.createIndex(
											Indexes.ascending(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED),
											new IndexOptions().expireAfter(Long.valueOf(ttl), TimeUnit.SECONDS));
								} else {
									String indexName = getIndexName(collection,
											MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED);
									collection.dropIndex(indexName);
									collection.createIndex(
											Indexes.ascending(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED),
											new IndexOptions().expireAfter(Long.valueOf(ttl), TimeUnit.SECONDS));

								}

							}

						}
					} else {
						if (colName.equalsIgnoreCase(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_MODIFIED)
								|| colName.equalsIgnoreCase(MongoDBConstants.PROPERTY_KEY_MONGODB_TIME_CREATED)) {
							if (checkIfIndexExist(collection, colName)) {
								String indexName = getIndexName(collection, colName);
								collection.dropIndex(indexName);

							}
						}
					}

				});

				UpdateOptions updateoptions = new UpdateOptions();

				if (!keydoc.isEmpty()) {
					updateList.add(set(_ID, keydoc));
					List<Bson> indexlist = new ArrayList<Bson>();
					for (String fieldname : keydoc.keySet()) {
						indexlist.add(Indexes.ascending(fieldname));
					}
					collection.createIndex(Indexes.compoundIndex(indexlist), new IndexOptions().unique(true));
					collection.updateOne(eq(_ID, keydoc), updateList, updateoptions.upsert(true));

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

	private void delete_(StoreRowHolder queryHolder) {
		try {
			if (!isTxExecution.get()) {
				getclientSession();
			}

			String tableName = queryHolder.getTableName();
			MongoCollection<Document> collection = mongodatabase.getCollection(tableName);
			Document query = new Document();
			Bson filter = createFilterToRetrieveData(queryHolder);

			if (filter != null) {
				getLogger().log(Level.DEBUG, " ******* Filter delete query :" + filter.toString());
				collection.deleteOne(filter);
				getLogger().log(Level.DEBUG, "Deleted Record: " + filter.toBsonDocument().toJson());

			} else {
				collection.deleteOne(query);
				getLogger().log(Level.DEBUG, "Deleted All Records: " + query.toJson());
			}

		} catch (Exception e) {
			getLogger().log(Level.ERROR, "Problem while acquiring connection during transaction: " + e.getMessage());
		} finally {
			if (!isTxExecution.get()) {
				closeConnection();
			}
		}
	}

	private List<StoreRowHolder> readFromDB(StoreRowHolder queryHolder) {

		String tableName = queryHolder.getTableName();
		List<StoreRowHolder> result = new ArrayList<>();
		List<Document> docresult = new ArrayList<Document>();
		MongoCollection<Document> collection = mongodatabase.getCollection(tableName);
		Bson filter = createFilterToRetrieveData(queryHolder);
		getLogger().log(Level.DEBUG, " ******* Filter read query :" + filter.toString());

		if (filter != null) {
			Bson excludeID = eq(_ID, 0);
			docresult = (List<Document>) collection.find(filter).projection(excludeID).into(docresult);
			for (Document doc : docresult) {
				List<Object> dataTypeList = new ArrayList<Object>();
				List<String> colNameList = new ArrayList<String>();
				List<Object> colValueList = new ArrayList<Object>();

				for (Entry<String, BsonValue> fieldEntry : doc.toBsonDocument().entrySet()) {
					colNameList.add(fieldEntry.getKey());
					String datatype = fieldEntry.getValue().getBsonType().name();
					dataTypeList.add(datatype);
					colValueList.add(MongoDBUtils.getValue(datatype, fieldEntry.getValue()));

				}
				result.add(StoreHelper.getRow(queryHolder.getTableName(), dataTypeList.toArray(),
						colNameList.toArray(new String[colNameList.size()]), colValueList.toArray()));
			}

		}
		getLogger().log(Level.DEBUG, " ******* result set size :" + result.size());
		return result;

	}

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

	private void closeConnection() {
		if (clientsession.get() != null) {
			clientsession.get().close();
			clientsession.set(null);
			// mongoclient.close();
		}
	}

	private void getclientSession() throws Exception {
		if (clientsession.get() == null) {
			ClientSession session = mongoclient.startSession();
			clientsession.set(session);
		}
	}

	private Bson createFilterToRetrieveData(StoreRowHolder queryHolder) {
		Map<String, StoreColumnData> colDataMap = queryHolder.getColDataMap();
		Map<String, StoreColumnData> filterData = queryHolder.getFiltersDataMap();
		List<Bson> filterlist = new ArrayList<Bson>();
		Bson filter = new Document(); // empty document is used to retrieve all data without any filter condition

		if (!(colDataMap == null || colDataMap.isEmpty())) {
			for (Entry<String, StoreColumnData> fieldEntry : colDataMap.entrySet()) {
				String fieldName = fieldEntry.getKey();
				StoreColumnData fieldData = fieldEntry.getValue();
				String fieldOperator = fieldData.getOperator();
				fieldOperator = fieldOperator == null ? "=" : fieldOperator;
				Object fieldValue = fieldData.getColumnValue();
				filterlist.add(getFilterConditions(fieldOperator, fieldName, fieldValue));
			}
			if (filterlist.size() > 1) {
				filter = and(filterlist);
			} else
				filter = filterlist.get(0);
		} else {
			if (!(filterData == null || filterData.isEmpty()) && filterData.containsKey("where")) // Make sure to mention filter condition in json format which is
																									// compatible with MongoDB in case of direct store modes. For example format is
																									// { <field1>: {<operator1> <value1> }, ... }for example :"{Age:{$eq:27}}".
																									// For supported operators refer MongoDB documentation
			{
				if (filterData.containsKey("where")) {
					if (null != filterData.get("where").getColumnValue()) {
						getLogger().log(Level.DEBUG,
								" ******* where query :" + filterData.get("where").getColumnValue());
						filter = BsonDocument.parse(filterData.get("where").getColumnValue().toString());
					}
				}

			}

		}
		return filter;

	}

	private Bson getFilterConditions(String fieldOperator, String fieldName, Object fieldValue) {
		Bson filter = new Document();
		if ("=".equalsIgnoreCase(fieldOperator)) {
			filter = eq(fieldName.toLowerCase(), fieldValue);
			return filter;
		}
		if ("!=".equalsIgnoreCase(fieldOperator)) {
			filter = ne(fieldName.toLowerCase(), fieldValue);
			return filter;
		}
		if (">".equalsIgnoreCase(fieldOperator)) {
			filter = gt(fieldName, fieldValue);
			return filter;
		}
		if (">=".equalsIgnoreCase(fieldOperator)) {
			filter = gte(fieldName, fieldValue);
			return filter;
		}
		if ("<".equalsIgnoreCase(fieldOperator)) {
			filter = lt(fieldName, fieldValue);
			return filter;
		}
		if ("<=".equalsIgnoreCase(fieldOperator)) {
			filter = lte(fieldName, fieldValue);
			return filter;
		}
		return filter;
	}

	private boolean checkIfIndexExist(MongoCollection<Document> collection, String indexName) {

		List<Document> indexlist = collection.listIndexes().into(new ArrayList<Document>());
		for (Document indexdoc : indexlist) {
			if (indexdoc.get("name").toString().equalsIgnoreCase(indexName + "_1")
					|| indexdoc.get("name").toString().equalsIgnoreCase(indexName + "_-1")) {
				return true;
			}
		}
		return false;

	}

	private String getIndexName(MongoCollection<Document> collection, String columnName) {

		List<Document> indexlist = collection.listIndexes().into(new ArrayList<Document>());
		for (Document indexdoc : indexlist) {
			if (indexdoc.get("name").toString().equalsIgnoreCase(columnName + "_1")
					|| indexdoc.get("name").toString().equalsIgnoreCase(columnName + "_-1")) {
				return (String) indexdoc.get("name");
			}
		}
		return null;

	}

	private List<StoreRowHolder> readAggregate(StoreRowHolder queryHolder, String aggFunction) {

		List<StoreRowHolder> result = new ArrayList<>();
		String tableName = queryHolder.getTableName();
		String[] selectList = queryHolder.getSelectList();
		List<String> groupByColSet = new LinkedList<String>();
		List<Bson> Aggregationpipeline = new ArrayList<>();
		List<Document> docresult = new ArrayList<Document>();
		MongoCollection<Document> collection = mongodatabase.getCollection(tableName);

		Bson filter = createFilterToRetrieveData(queryHolder);
		getLogger().log(Level.DEBUG, " ******* Filter for aggregates :" + filter.toString());
		Bson match = match(filter);

		Aggregationpipeline.add(match);

		Map<String, StoreColumnData> filterData = queryHolder.getFiltersDataMap();
		if (!(filterData == null || filterData.isEmpty()) && filterData.containsKey("group by")) {
			StoreColumnData groupByData = filterData.get("group by");
			String groupByClause = (String) groupByData.getColumnValue();

			if (groupByClause != null && !groupByClause.trim().equals("")) {
				String[] groupByFieldNames = groupByClause.split(",");
				for (String groupByFieldName : groupByFieldNames) {
					groupByColSet.add(groupByFieldName);
				}
			}
		}

		Aggregationpipeline = AggregatesBuilder.build(aggFunction, groupByColSet, selectList[0], Aggregationpipeline);
		docresult = (List<Document>) collection.aggregate(Aggregationpipeline).into(docresult);
		getLogger().log(Level.DEBUG, "Aggregation Complete , Found " + docresult.size() + " Results");

		for (Document doc : docresult) {
			List<Object> dataTypeList = new ArrayList<Object>();
			List<String> colNameList = new ArrayList<String>();
			List<Object> colValueList = new ArrayList<Object>();

			for (Entry<String, BsonValue> fieldEntry : doc.toBsonDocument().entrySet()) {
				colNameList.add(fieldEntry.getKey());
				String datatype = fieldEntry.getValue().getBsonType().name();
				dataTypeList.add(datatype);
				colValueList.add(MongoDBUtils.getValue(datatype, fieldEntry.getValue()));

			}
			result.add(StoreHelper.getRow(queryHolder.getTableName(), dataTypeList.toArray(),
					colNameList.toArray(new String[colNameList.size()]), colValueList.toArray()));
		}
		getLogger().log(Level.DEBUG, " ******* result set size:" + result.size());
		return result;
	}

}
