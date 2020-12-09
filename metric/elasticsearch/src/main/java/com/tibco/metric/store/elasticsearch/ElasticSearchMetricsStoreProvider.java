/**
 * 
 */
package com.tibco.metric.store.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.appmetrics.AppMetricsConfig;
import com.tibco.cep.runtime.appmetrics.AppMetricsEntityConfig;
import com.tibco.cep.runtime.appmetrics.MetricRecord;
import com.tibco.cep.runtime.appmetrics.MetricsRecordBuilder;
import com.tibco.cep.runtime.appmetrics.MetricsStoreProvider;

/**
 * ElasticSearchMetricsStoreProvider implements the MetricStoreProvider to enable publishing metrics to Elasticsearch. It 
 * uses the configuration made available off the deployment descriptor file to initialize, connect and publish records
 * to Elasticsearch server.
 */
public class ElasticSearchMetricsStoreProvider implements MetricsStoreProvider<ElasticSearchRecord> {
	
	private String host;
	private int port;
	
	private long ackTimeout;
	private long masterTimeout;
	private int activeShardResponseCount;
	
	private String userName;
	private String password;
	private String accessToken;
	private String apiKey;
	private String apiSecret;
	private String trustStorePath;
	private String trustStorePwd;
	
	private RestHighLevelClient elasticClient;
	private AppMetricsConfig appMetricsConfig;
	
	private Logger logger;

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> builder(AppMetricsEntityConfig config, MetricRecord.OpType opType) {
		return new ElasticSearchMetricsRecordBuilder(config, opType);
	}

	@Override
	public void close() throws Exception {
		if (elasticClient != null) {
			elasticClient.close();
			elasticClient = null;
		}
	}

	@Override
	public void connect() throws Exception {
		RestClientBuilder restBuilder = null;
		if (!userName.isBlank()) {
			restBuilder = RestClient.builder(new HttpHost(host, port, "http"));
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(userName, password));
			restBuilder = restBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		
		} else if (!accessToken.isBlank()) {
			restBuilder = RestClient.builder(new HttpHost(host, port, "http"));
			Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization", "Bearer " + accessToken)};
			restBuilder.setDefaultHeaders(defaultHeaders);
		
		} else if (!apiKey.isBlank()) {
			restBuilder = RestClient.builder(new HttpHost(host, port, "http"));
			String apiKeyAuth = Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes(StandardCharsets.UTF_8));
			Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization", "ApiKey " + apiKeyAuth)};
			restBuilder.setDefaultHeaders(defaultHeaders);
		
		} else if (!trustStorePath.isBlank() && !trustStorePwd.isBlank()) {
			Path tsPath = Paths.get(trustStorePath);
			KeyStore truststore = KeyStore.getInstance("JKS");
			try (InputStream is = Files.newInputStream(tsPath)) {
			    truststore.load(is, trustStorePwd.toCharArray());
			}
			
			SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(truststore, null);
			final SSLContext sslContext = sslBuilder.build();
			
			restBuilder = RestClient.builder(new HttpHost(host, port, "https"));
			restBuilder = restBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setSSLContext(sslContext)
					.setSSLHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			}));
		} else {
			restBuilder = RestClient.builder(new HttpHost(host, port, "http"));
		}
		
		elasticClient = new RestHighLevelClient(restBuilder);
		
		createIndexes();
	}

	@Override
	public void init(AppMetricsConfig config) throws Exception {
		logger = LogManagerFactory.getLogManager().getLogger(ElasticSearchMetricsStoreProvider.class);
		logger.log(Level.INFO, "Initializing Elastic MetricsStoreProvider ...");
		this.appMetricsConfig = config;
		
		host = config.getProperty("host");
		port = Integer.parseInt(config.getProperty("port"));
		
		ackTimeout = Long.parseLong(config.getProperty("acknowledgement-timeout"));
		masterTimeout = Long.parseLong(config.getProperty("master-timeout"));
		activeShardResponseCount = Integer.parseInt(config.getProperty("active-shard-response-count"));
		
		userName = config.getProperty("username");
		password = config.getProperty("password");
		
		accessToken = config.getProperty("access-token");
		apiKey = config.getProperty("api-key");
		apiSecret = config.getProperty("api-secret");
		trustStorePath = config.getProperty("trust-store-location");
		trustStorePwd = config.getProperty("trust-store-pwd");
	}
	
	private void createIndexes() throws Exception {
		appMetricsConfig.getEntities().forEach(entityUri -> {
			boolean indexExist = false;
			String indexName = getIndexName(entityUri);
			GetIndexRequest getIndexReq = new GetIndexRequest(indexName);
			try {
				indexExist = elasticClient.indices().exists(getIndexReq, RequestOptions.DEFAULT);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			if(!indexExist) {
				logger.log(Level.INFO, "Creating index for Entity[%s]", entityUri);
				CreateIndexRequest createIndexReq = new CreateIndexRequest(getIndexName(entityUri).toLowerCase());
				
				int numOfShards = Integer.parseInt(appMetricsConfig.getAppMetricsEntityConfig(entityUri).getProperty("no-of-shards", "1"));
				int numOfReplicas = Integer.parseInt(appMetricsConfig.getAppMetricsEntityConfig(entityUri).getProperty("no_of_replicas", "1"));
				createIndexReq.settings(Settings.builder()
						.put("index.number_of_shards", numOfShards)
						.put("index.number_of_replicas", numOfReplicas));
				
				createIndexReq.setTimeout(TimeValue.timeValueMillis(ackTimeout));
				createIndexReq.setMasterTimeout(TimeValue.timeValueMillis(masterTimeout));
				createIndexReq.waitForActiveShards(ActiveShardCount.from(activeShardResponseCount));
				try {
	
					CreateIndexResponse createIndexResponse = elasticClient.indices().create(createIndexReq, RequestOptions.DEFAULT);
					if (!createIndexResponse.isAcknowledged()) throw new Exception(String.format("Error creating index[%s] for entity[%s]", createIndexResponse.index(), entityUri));
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			}
		});
	}
	
	private String getIndexName(String entityUri) {
		return entityUri.substring(1).replace("/", "_").concat("_index").toLowerCase();
	}

	@Override
	public void publish(Iterator<MetricRecord<ElasticSearchRecord>> records) throws Exception {
		BulkRequest bulkRequest = new BulkRequest();
		records.forEachRemaining(record -> bulkRequest.add(record.getMetric().getRequest()));
		
		bulkRequest.timeout(TimeValue.timeValueMillis(ackTimeout));
		bulkRequest.waitForActiveShards(activeShardResponseCount);
		BulkResponse bulkResponse = elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		
		if (bulkResponse.hasFailures()) throw new RuntimeException(String.format("Error[%s] while publishing records", bulkResponse.buildFailureMessage()));
	}

	@Override
	public void reconnectOnError(Exception exception) throws Exception {
		close();
		connect();		
	}
}
