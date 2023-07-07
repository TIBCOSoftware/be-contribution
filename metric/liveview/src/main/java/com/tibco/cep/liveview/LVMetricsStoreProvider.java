package com.tibco.cep.liveview;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.streambase.liveview.client.LiveViewConnection;
import com.streambase.liveview.client.Table;
import com.streambase.liveview.client.TablePublisher;
import com.streambase.sb.Tuple;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.appmetrics.AppMetricsConfig;
import com.tibco.cep.runtime.appmetrics.AppMetricsEntityConfig;
import com.tibco.cep.runtime.appmetrics.MetricRecord;
import com.tibco.cep.runtime.appmetrics.MetricsRecordBuilder;
import com.tibco.cep.runtime.appmetrics.MetricsStoreProvider;

/**
 * 
 * @author shivkumarchelwa
 *
 */
public class LVMetricsStoreProvider implements MetricsStoreProvider<LVTuple> {

	private static final String TRANSFORMER_FACTORY_PROPERTY = "javax.xml.transform.TransformerFactory";
	private static final String DEFAULT_TRANSFORMER_FACTORY_IMPL = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";

	public static final String PUBLISHER_NAME_SUFFIX = "_Publisher";

	// LiveView connection pool
	private LVConnectionPool lvConnectionPool;

	private Map<String, TablePublisher> tableToPublisherMap = new ConcurrentHashMap<String, TablePublisher>();

	private AtomicLong sequenceNo = new AtomicLong(1000);

	private Logger logger;

	@Override
	public void init(AppMetricsConfig config) throws Exception {
		logger = LogManagerFactory.getLogManager().getLogger(LVMetricsStoreProvider.class);
		logger.log(Level.INFO, "Initializing LiveView MetricsStoreProvider ...");

		String lvConnectionURI = config.getProperty("ldm-url");
		String lvUserName = config.getProperty("user-name");
		String lvUserPassword = config.getProperty("user-password");
		int lvInitialSize = Integer.parseInt(config.getProperty("initial-size", "1"));
		int lvMaxSize = Integer.parseInt(config.getProperty("max-size", "10"));

		StringBuilder builder = new StringBuilder();
		builder.append("\turl = ").append(lvConnectionURI).append("\n");
		builder.append("\tinitial-size = ").append(lvInitialSize).append("\n");
		builder.append("\tmax-size = ").append(lvMaxSize).append("\n");
		logger.log(Level.INFO, "LiveView Client Config :\n %s", builder.toString());

		this.lvConnectionPool = new LVConnectionPool(lvConnectionURI, lvUserName, lvUserPassword, lvInitialSize,
				lvMaxSize);
	}

	@Override
	public void connect() throws Exception {
		lvConnectionPool.initialize();
	}

	@Override
	public void close() throws Exception {
		lvConnectionPool.removeAllFromPool();

		for (TablePublisher tablePublisher : tableToPublisherMap.values()) {
			if (tablePublisher != null)
				tablePublisher.close();
		}
		tableToPublisherMap.clear();
	}

	@Override
	public MetricsRecordBuilder<LVTuple> builder(AppMetricsEntityConfig entityConfig, MetricRecord.OpType opType) {
		try {
			String lvTableName = toLVTableName(entityConfig.getEntityUri());
			TablePublisher lvTuplePublisher = getTablePublisher(lvTableName);
			Tuple tuple = lvTuplePublisher.getSchema().createTuple();
			LVTuple lvTuple = new LVTuple(tuple, lvTableName);
			return new LVMetricsRecordBuilder(entityConfig, lvTuple);
		} catch (Exception e) {
			logger.log(Level.ERROR, "Failed to create tuple for entity %s", entityConfig.getEntityUri());
		}
		return null;
	}

	@Override
	public void publish(Iterator<MetricRecord<LVTuple>> iterator) throws Exception {
		while (iterator.hasNext()) {
			MetricRecord<LVTuple> record = iterator.next();
			LVTuple lvTuple = record.getMetric();
			TablePublisher tablePublisher = getTablePublisher(lvTuple.getTableName());
			boolean isDelete = (MetricRecord.OpType.DELETE == record.getOpType());
			tablePublisher.publish(sequenceNo.getAndIncrement(), isDelete, lvTuple.getTuple());
		}
	}

	public TablePublisher getTablePublisher(String lvTableName) throws Exception {
		LiveViewConnection lvConnection = null;

		try {
			setTransformerFactory();
			TablePublisher tablePublisher = tableToPublisherMap.get(lvTableName);
			if (tablePublisher == null) {
				lvConnection = lvConnectionPool.takeFromPool();
				Table lvTable = lvConnection.getTable(lvTableName);
				if (lvTable != null) {
					// Table publisher name needs to be unique with 10.5.x
					tablePublisher = lvTable.getTablePublisher(lvTableName + PUBLISHER_NAME_SUFFIX + "_" + System.nanoTime());
					tableToPublisherMap.put(lvTableName, tablePublisher);
				} else {
					// TODO throw exception?
					logger.log(Level.ERROR, String.format("LiveView table[%s] does not exist.", lvTableName));
				}
			}
			setTransformerFactory();
			return tablePublisher;
		} finally {
			if (lvConnection != null) {
				lvConnectionPool.returnToPool(lvConnection);
				lvConnection = null;
			}
		}
	}

	@Override
	public void reconnectOnError(Exception ex) {
		try {
			// If exception is instanceof LiveViewException, getErrorCode to determine
			// action
			close();
			connect();
		} catch (Exception e) {
			logger.log(Level.ERROR, ex, "Failed to reconnect LiveView server.");
		}
	}

	public static String toLVTableName(String entityURI) {
		return entityURI.substring(1).replaceAll("/", "_");
	}

	private void setTransformerFactory() {
		String existingTransformerFactoryImpl = System.getProperty(TRANSFORMER_FACTORY_PROPERTY);

		// set it back to java impl of TransformerFactory
		if (existingTransformerFactoryImpl != null
				&& !existingTransformerFactoryImpl.equals(DEFAULT_TRANSFORMER_FACTORY_IMPL)) {
			System.setProperty(TRANSFORMER_FACTORY_PROPERTY, DEFAULT_TRANSFORMER_FACTORY_IMPL);
		}
	}

}
