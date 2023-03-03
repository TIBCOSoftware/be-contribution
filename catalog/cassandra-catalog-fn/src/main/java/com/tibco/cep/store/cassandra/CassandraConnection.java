/**
 * 
 */
package com.tibco.cep.store.cassandra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.core.metadata.Node;
import com.datastax.oss.driver.api.core.metadata.NodeState;
import com.datastax.oss.driver.api.core.metadata.NodeStateListener;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.api.core.type.codec.ExtraTypeCodecs;
import com.datastax.oss.protocol.internal.request.query.QueryOptions;
import com.tibco.be.jdbcstore.CryptoUtil;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.store.Item;
import com.tibco.cep.store.StoreConnection;
import com.tibco.cep.store.StoreConnectionInfo;
import com.tibco.cep.store.StoreContainer;
import com.tibco.cep.store.StoreIterator;

/**
 * @author rakulkar
 *
 */
public class CassandraConnection extends StoreConnection{
	private static final String SESSION_ID_PREFIX = "SESSION_ID_";

	private final static Logger logger = LogManagerFactory.getLogManager().getLogger(CassandraConnection.class);

	private CqlSession session;
	private ConcurrentHashMap<String, Statement> statementMap;
	
	private static ThreadLocal<BatchStatement> txBatchStatement = new ThreadLocal<BatchStatement>() {
		protected BatchStatement initialValue() {
			return null;
		}
	};

	private static ThreadLocal<Boolean> isTxExecution = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return false;
		}
	};

	private boolean useSsl = false;

	private static Map<String, PreparedStatement> psCache = new LinkedHashMap<>();

	public CassandraConnection(StoreConnectionInfo dgProperties) {
		super(dgProperties);
		statementMap = Optional.ofNullable(statementMap).orElse(new ConcurrentHashMap<String, Statement>());
	}
	
	public Session getConnection() {
		return this.session;
	}

	@Override
	public void connect() throws Exception {
		CqlSessionBuilder cqlSessionBuilder = null;
		
		try {
			cqlSessionBuilder = getCqlSessionBuilder();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		String keyspaceName = ((CassandraConnectionInfo) storeConnectionInfo).getConnectionProperties()
				.getProperty("keyspace");
		if (null == keyspaceName || keyspaceName.trim().isEmpty()) {
			throw new RuntimeException("Keyspace name not specified.");
		} // Not possible using store API

		session = cqlSessionBuilder.withKeyspace(keyspaceName).build();

		Map<UUID, Node> hosts = session.getMetadata().getNodes();
		StringBuffer casConnectionNodeInfo = new StringBuffer();
		for (Node host : hosts.values()) {
			if (host.getState()==NodeState.UP) {
				casConnectionNodeInfo.append(host.getEndPoint().toString()).append(",");
			}
		}
		getLogger().log(Level.INFO, "Successfully connected to Cassandra using Contact Points: "
				+ casConnectionNodeInfo + (this.useSsl ? " with security enabled." : ""));

		if (session != null)
			storeMetadata = new CassandraStoreMetadata(session.getMetadata().getKeyspace(keyspaceName).get());
	}

	@Override
	protected boolean hasContainerKeyPrefix(String containerKey) {
		return (containerKey != null && containerKey.contains(SESSION_ID_PREFIX));
	}

	@Override
	protected String getContainerKeyPrefix() {
		return SESSION_ID_PREFIX + Thread.currentThread().getName();
	}

	@Override
	public StoreContainer<? extends Item> createContainer(String containerName) throws Exception {
		StoreContainer<? extends Item> storeContainer = new CassandraStoreContainer(containerName);
		((CassandraStoreContainer) storeContainer).init(this.session, txBatchStatement, isTxExecution);
		((CassandraStoreContainer) storeContainer)
				.setTableMetadata(((CassandraStoreMetadata) this.storeMetadata).getTableMetadata(containerName));
		return storeContainer;
	}

	@Override
	public StoreIterator query(String query, Object[] queryParameters, Object queryOptions, String returnEntityPath)
			throws Exception {
		Properties queryProperties = null;
		BoundStatement bs = null;
		CassandraQueryOptions cassandraQueryOptions = (CassandraQueryOptions) queryOptions;
		if (query != null && !query.toLowerCase().endsWith("allow filtering")) {
			query = query.concat(" allow filtering");
		}
		PreparedStatement psStmt = getOrCreatePreparedStmt(query);
		if (queryParameters != null) {

			bs = psStmt.bind(queryParameters);
		} else {
			bs = psStmt.bind();
		}

		if (cassandraQueryOptions != null) {
			queryProperties = cassandraQueryOptions.getProperties();
//			bs.setConsistencyLevel((ConsistencyLevel) queryProperties.getOrDefault("consistency",
//					QueryOptions.DEFAULT.consistency));
//			bs.setTimeout(Duration.ofMillis((long) queryProperties.getOrDefault("readTimeoutMillis", 1000)));
			bs.setFetchSize((int) queryProperties.getOrDefault("fetchSize", QueryOptions.DEFAULT.pageSize));
			bs.setIdempotent((boolean) queryProperties.getOrDefault("idempotent", false));
		}

		ResultSet resultSet = null;
		resultSet = session.execute(bs);

		CassandraIterator cassandraIterator = new CassandraIterator(resultSet, returnEntityPath);
		String containerName = getContainerNameFromQuery(query);
		cassandraIterator.setContainer(containerName,
				((CassandraStoreMetadata) this.storeMetadata).getTableMetadata(containerName));
		return cassandraIterator;
	}

	@Override
	public void disconnect() throws Exception {
		if (session != null) {
			session.close();
		}
	}

	@Override
	public void enableTransactions() throws Exception {
		isTxExecution.set(true);
		txBatchStatement.set(BatchStatement.builder(BatchType.LOGGED).build());
	}

	@Override
	public void disableTransactions() throws Exception {
		isTxExecution.set(false);
	}

	@Override
	public void commit() throws Exception {
		if (!isTxExecution.get() || txBatchStatement.get() == null) {
			throw new RuntimeException("Cannot commit transaction that is not started.");
		}
		executeTxBatchStatement();
	}

	private ResultSet executeTxBatchStatement() {
		ResultSet rs = null;
		if (txBatchStatement.get() != null && txBatchStatement.get().size() > 0) {
			rs = session.execute(txBatchStatement.get());
			txBatchStatement.get().clear();
		}
		return rs;
	}

	@Override
	public void rollback() throws Exception {
		throw new UnsupportedOperationException("Method not supported.");
	}

	@Override
	public boolean putAllItems(List<Item> putItems) throws Exception {
		for (Item item : putItems) {
			CassandraStoreItem cassandraStoreItem = (CassandraStoreItem) item;
			CassandraStoreContainer container = (CassandraStoreContainer) cassandraStoreItem.getContainer();
			if (container == null) {
				throw new IllegalArgumentException("Container not found for Item.");
			}
			container.putItem(cassandraStoreItem);
		}
		return true;
	}

	@Override
	public List<Item> getAllItems(List<Item> getItems) throws Exception {
		List<Item> resultItems = new ArrayList<Item>();
		for (Item item : getItems) {
			CassandraStoreItem cassandraItem = (CassandraStoreItem) item;
			CassandraStoreContainer container = (CassandraStoreContainer) cassandraItem.getContainer();
			if (container == null) {
				throw new IllegalArgumentException("Container not found for Item.");
			}
			resultItems.add(container.getItem(cassandraItem));
		}
		return resultItems;
	}

	@Override
	public boolean deleteAllItems(List<Item> deleteItems) throws Exception {
		for (Item item : deleteItems) {
			CassandraStoreItem cassandraStoreItem = (CassandraStoreItem) item;
			CassandraStoreContainer container = (CassandraStoreContainer) cassandraStoreItem.getContainer();
			if (container == null) {
				throw new IllegalArgumentException("Container not found for Item.");
			}
			container.deleteItem(cassandraStoreItem);
		}
		return true;
	}

	@Override
	public long executeUpdate(String query) throws Exception {

		if (!isTxExecution.get()) {
			txBatchStatement.set(BatchStatement.builder(BatchType.LOGGED).build());
		}

		PreparedStatement psStmt = session.prepare(query);
		BoundStatement bs = psStmt.bind();

		txBatchStatement.get().add(bs);

		if (!isTxExecution.get()) {
			executeTxBatchStatement();
		}

		return -1;
	}

	private CqlSessionBuilder getCqlSessionBuilder() throws Exception {
		CqlSessionBuilder cqlSessionBuilder;

		CassandraConnectionInfo casStoreConnectionInfo = (CassandraConnectionInfo) storeConnectionInfo;

		cqlSessionBuilder = CqlSession.builder();
		cqlSessionBuilder.withApplicationVersion(storeConnectionInfo.getName());
		cqlSessionBuilder.withLocalDatacenter("datacenter1");
		cqlSessionBuilder.addContactPoints(getContactPointsWithPorts(storeConnectionInfo.getUrl()));
		
		String dbPswd = casStoreConnectionInfo.getPassword();
		if (dbPswd != null) {
			dbPswd = CryptoUtil.decryptIfEncrypted(dbPswd.trim());
		}

		cqlSessionBuilder.withAuthCredentials(casStoreConnectionInfo.getUserName(), dbPswd);

		this.useSsl = (casStoreConnectionInfo.getTrustStore()!=null)? true : false;
		if (this.useSsl) {
			SSLContext sslContext = createSSLContext(casStoreConnectionInfo.getTrustStore(),
					casStoreConnectionInfo.getTrustStorePassword(), "JKS", casStoreConnectionInfo.getKeyStore(),
					casStoreConnectionInfo.getKeyStorePassword(), casStoreConnectionInfo.getKeyStoreType());
			cqlSessionBuilder.withSslContext(sslContext);
		}

		cqlSessionBuilder.addTypeCodecs(ExtraTypeCodecs.ZONED_TIMESTAMP_PERSISTED);
		cqlSessionBuilder.addNodeStateListener(new NodeStateListener() {
			
			@Override
			public void close() throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUp(Node node) {
				getLogger().log(Level.INFO, ":::: Cassandra host " + node.getEndPoint() + " is UP now::::");
				
			}
			
			@Override
			public void onRemove(Node node) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDown(Node node) {
				getLogger().log(Level.INFO, ":::: Cassandra host " +  node.getEndPoint()  + " is DOWN now::::");
				
			}
			
			@Override
			public void onAdd(Node node) {
				// TODO Auto-generated method stub
				
			}
		});
		return cqlSessionBuilder;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public static List<InetSocketAddress> getContactPointsWithPorts(String nodes) throws URISyntaxException {
		String[] points = getContactPointsArray(nodes);
		List<InetSocketAddress> contactPoints = new LinkedList<>();
		for (String point : points) {
			URI uri = new URI("http://" + point.trim());
			String host = uri.getHost();
			int port = uri.getPort();
			if (uri.getPort() == -1) {
				port = 9042;
			}
			if (uri.getHost() == null) {
				throw new URISyntaxException(uri.toString(), "Please enter valid host name.");
			}
			try {
				contactPoints.add(new InetSocketAddress(host, port));
			} catch (Throwable e) {
				throw new IllegalArgumentException(
						"Incorrect contact point '" + point + "' specified for Cassandra cache storage", e);
			}
		}
		return contactPoints;
	}

	public static String[] getContactPointsArray(String nodes) {
		String[] points = nodes.split(",");
		if (points.length == 0) {
			throw new RuntimeException("No Cassandra contact points specified");
		}
		for (int i = 0; i < points.length; i++) {
			points[i] = points[i].trim();
		}
		return points;
	}

	private synchronized PreparedStatement getOrCreatePreparedStmt(String query) {
		PreparedStatement ps = psCache.get(query);
		if (ps == null) {
			ps = session.prepare(query);
			psCache.put(query, ps);
		}
		return ps;
	}

	private String getContainerNameFromQuery(String query) {
		if (query.toLowerCase().endsWith(" allow filtering")) {
			query = query.replaceAll(" allow filtering", "");
		}
		if (query.contains("where")) {
			int endIndex = (query.indexOf("where") != -1) ? query.indexOf("where") : query.length();
			return query.toLowerCase().substring(query.indexOf("from") + "from".length(), endIndex).trim();
		} else if (query.contains("group by")) {
			int endIndex = (query.indexOf("group by") != -1) ? query.indexOf("group by") : query.length();
			return query.toLowerCase().substring(query.indexOf("from") + "from".length(), endIndex).trim();
		} else if (query.contains("limit")) {
			int endIndex = (query.indexOf("limit") != -1) ? query.indexOf("limit") : query.length();
			return query.toLowerCase().substring(query.indexOf("from") + "from".length(), endIndex).trim();
		} else {
			int endIndex = query.length();
			return query.toLowerCase().substring(query.indexOf("from") + "from".length(), endIndex).trim();
		}
	}

	private SSLContext createSSLContext(String truststorePath, String truststorePwd, String trustStoreType,
			String keystorePath, String keystorePwd, String keyStoreType)
			throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException,
			KeyManagementException, CertificateException, UnrecoverableKeyException {
		TrustManagerFactory tmf = null;
		KeyStore tks = KeyStore.getInstance(trustStoreType);
		tks.load((InputStream) new FileInputStream(new File(truststorePath)), truststorePwd.toCharArray());
		tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(tks);

		KeyManagerFactory kmf = null;
		if (null != keystorePath) {
			KeyStore kks = KeyStore.getInstance(keyStoreType);
			kks.load((InputStream) new FileInputStream(new File(keystorePath)), keystorePwd.toCharArray());
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(kks, keystorePwd.toCharArray());
		}

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf != null ? kmf.getKeyManagers() : null, tmf != null ? tmf.getTrustManagers() : null,
				new SecureRandom());

		return sslContext;
	}
	
	class SessionHolder {
		private Session session;
		private boolean isTransacted;

		public SessionHolder(Session session, boolean isTransacted) {
			super();
			this.session = session;
			this.isTransacted = isTransacted;
		}

		public Session getSession() {
			return session;
		}

		public boolean isTransacted() {
			return isTransacted;
		}
	}
}
