/**
 * 
 */
package com.tibco.metric.store.elasticsearch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.tibco.cep.kernel.model.entity.Id;
import com.tibco.cep.runtime.appmetrics.AppMetricsConfig;
import com.tibco.cep.runtime.appmetrics.AppMetricsEntityConfig;
import com.tibco.cep.runtime.appmetrics.MetricRecord;
import com.tibco.cep.runtime.appmetrics.MetricsRecordBuilder;


/**
 * Integration test to validate various test cases for Elasticsearch base metric store
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElasticSearchMetricStoreProviderIntegrationTest {
	
	private static final String ELASTICSEARCH_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:7.10.1";
	
	@Container
	private static ElasticsearchContainer elasticSearchContainer = new ElasticsearchContainer(ELASTICSEARCH_IMAGE_NAME);
	
	private static ElasticSearchMetricsStoreProvider elasticSearchMetricStoreProvider;
	private static AppMetricsConfig appMetricsConfig;
	
	public static boolean isLegacyID;
	
	private static final String ENTITY_URI = "/Book";
	private static final String DOCUMENT_ID_NAME = "_id";
	
	@BeforeAll
	static void setup() {
		isLegacyID = Boolean.parseBoolean(System.getProperty(Id.USE_LEGACY_ID_PROPERTY, "true"));
		elasticSearchContainer.start();
		
		appMetricsConfig = createAppMetricsConfig();
		elasticSearchMetricStoreProvider = new ElasticSearchMetricsStoreProvider();
		try {
			elasticSearchMetricStoreProvider.init(appMetricsConfig);
			elasticSearchMetricStoreProvider.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeEach
	void isTestElasticServerRunningAndHealthy() {
		assertTrue(elasticSearchContainer.isRunning());
		
		appMetricsConfig.getEntities().forEach(entityUri -> {
			assertTrue(indexExists(entityUri));
		});
	}
	
	
	static boolean indexExists(String entityUri) {
		GetIndexRequest getIndexReq = new GetIndexRequest(getIndexName(entityUri));
		try {
			return elasticSearchMetricStoreProvider.getElasticsearchClient().indices().exists(getIndexReq, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	@Order(1)
	void testPublishRecord() {
		Book book = createBook("The Da Vinci Code", "Dan Brown");
		assertNotNull(book);
		publishRecord(book, MetricRecord.OpType.ADD);
		
		Book resultBook = getRecord(ENTITY_URI, book.getId());
		assertNotNull(resultBook);
		assertTrue(book.equals(resultBook));
	}
	
	@Test
	@Order(2)
	void testPublishDuplicateRecord() {
		Book book = createBook("The Da Vinci Code", "Dan Brown");
		assertNotNull(book);
		publishRecord(book, MetricRecord.OpType.ADD);
		
		Book resultBook = getRecord(ENTITY_URI, book.getId());
		assertNotNull(resultBook);
		assertTrue(book.equals(resultBook));
	}
	
	@Test
	@Order(3)
	void testPublishUpdateRecord() {
		Book origBook = createBook("The Da Vinci Code", "Dan Brown");
		assertNotNull(origBook);
		Book getOrigBook = getRecord(ENTITY_URI, origBook.getId());
		assertNotNull(getOrigBook);
		assertTrue(origBook.equals(getOrigBook));
		
		// updating the book
		origBook.setAuthor("Dan Brown Sr");
		origBook.setTitle("The Da Vinci Code Vol1");
		publishRecord(origBook, MetricRecord.OpType.MODIFY);
		
		Book getUpdatedBook = getRecord(ENTITY_URI, origBook.getId());
		assertNotNull(getUpdatedBook);
		assertTrue(origBook.equals(getUpdatedBook));
		assertFalse(getOrigBook.equals(getUpdatedBook));
	}
	
	@Test
	@Order(4)
	void testDeleteRecord() {
		Book book = createBook("The Da Vinci Code Vol1", "Dan Brown Sr");
		assertNotNull(book);
		publishRecord(book, MetricRecord.OpType.DELETE);
		
		Book resultBook = getRecord(ENTITY_URI, book.getId());
		assertNull(resultBook);
	}
	
	
	@AfterAll
	static void destroy() {
		elasticSearchContainer.stop();
	}
	
	private Book getRecord(String entityUri, Object id) {
		try {
			Thread.sleep(700);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		SearchRequest searchRequest = new SearchRequest(getIndexName(entityUri));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		if (isLegacyID) searchSourceBuilder.query(QueryBuilders.termQuery(DOCUMENT_ID_NAME, ((Id)id).getLongValue()));
		else searchSourceBuilder.query(QueryBuilders.termQuery(DOCUMENT_ID_NAME, id.toString())); 
		searchRequest.source(searchSourceBuilder);
		
		try {
			SearchResponse searchResponse = elasticSearchMetricStoreProvider.getElasticsearchClient().search(searchRequest, RequestOptions.DEFAULT);			
			if (searchResponse != null) {
				SearchHits hits = searchResponse.getHits();
				if (hits != null) {
					for (SearchHit searchHit : hits.getHits()) {
						Map<String, Object> resultMap = searchHit.getSourceAsMap();
						Book book = new Book(searchHit.getId());
						book.setTitle((String)resultMap.get("title"));
						book.setAuthor((String)resultMap.get("author"));

						return book;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void publishRecord(Book book, MetricRecord.OpType opType) {
		MetricsRecordBuilder<ElasticSearchRecord> metricRecordBuilder = elasticSearchMetricStoreProvider.builder(appMetricsConfig.getAppMetricsEntityConfig(ENTITY_URI), opType);
		metricRecordBuilder.addId(book.getId());
		metricRecordBuilder.addField("title", book.getTitle());
		metricRecordBuilder.addField("author", book.getAuthor());
		
		MetricRecord<ElasticSearchRecord> record = new MetricRecord<ElasticSearchRecord>();
		record.setMetric(metricRecordBuilder.build());
		record.setOpType(opType);
		
		Set<MetricRecord<ElasticSearchRecord>> records = new HashSet<MetricRecord<ElasticSearchRecord>>();
		records.add(record);
		
		try {
			elasticSearchMetricStoreProvider.publish(records.iterator());			
		} catch (Exception e) {
			if (opType == MetricRecord.OpType.ADD) {
				assertThrows(Exception.class, () -> {
					throw e;
				}, e.getMessage());
			} else throw new RuntimeException(e); 
		}
	}

	private static AppMetricsConfig createAppMetricsConfig() {
		AppMetricsConfig appMetricsConfig = new AppMetricsConfig();
		appMetricsConfig.setProperty("host", elasticSearchContainer.getHost());
		appMetricsConfig.setProperty("port", elasticSearchContainer.getFirstMappedPort()+"");
		appMetricsConfig.setProperty("acknowledgement-timeout", "2000");
		appMetricsConfig.setProperty("master-timeout", "2000");
		appMetricsConfig.setProperty("active-shard-response-count", "1");
		appMetricsConfig.setProperty("username", "");
		appMetricsConfig.setProperty("access-token", "");
		appMetricsConfig.setProperty("api-key", "");
		appMetricsConfig.setProperty("trust-store-location", "");
		
		AppMetricsEntityConfig appMetricEntityConfig = new AppMetricsEntityConfig();
		appMetricEntityConfig.setEntityUri(ENTITY_URI);
		appMetricEntityConfig.setProperty("no-of-shards", "1");
		appMetricEntityConfig.setProperty("no-of-replicas", "1");
		
		appMetricsConfig.putAppMetricsEntityConfig(ENTITY_URI, appMetricEntityConfig);
		
		return appMetricsConfig;
	}
	
	private static String getIndexName(String entityUri) {
		return entityUri.substring(1).replace("/", "_").concat("_index").toLowerCase();
	}
	
	private Book createBook(String title, String author) {
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		return book;
	}
}
