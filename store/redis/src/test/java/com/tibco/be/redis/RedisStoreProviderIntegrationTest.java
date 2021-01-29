/**
 * # Copyright (c) 2019-2020. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */
package com.tibco.be.redis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import org.testcontainers.utility.DockerImageName;

import com.tibco.cep.config.store.StoreConfigPojo;
import com.tibco.cep.kernel.model.entity.Id;
import com.tibco.cep.runtime.service.cluster.Cluster;
import com.tibco.cep.runtime.service.cluster.ClusterConfiguration;
import com.tibco.cep.runtime.service.store.StoreProviderConfig;
import com.tibco.cep.runtime.session.impl.RuleServiceProviderImpl;
import com.tibco.cep.store.custom.StoreColumnData;
import com.tibco.cep.store.custom.StoreHelper;
import com.tibco.cep.store.custom.StoreInitializer;
import com.tibco.cep.store.custom.StoreRowHolder;

/**
 * Integration test to validate various test cases for Redis backing store.
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisStoreProviderIntegrationTest {
	
	private static final String REDISEARCH_IMAGE_NAME = "redislabs/redisearch:2.0.1";
	
	
	public static final DockerImageName REDIS_IMAGE = DockerImageName.parse(REDISEARCH_IMAGE_NAME);


	@ClassRule
	public static GenericContainer<?> rediSearchContainer = new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);
	
	public static boolean isLegacyID;

	private static StoreProviderConfig storeConfig;

	private static RedisStoreProvider redisStoreProvider;
	
	@Mock
	private RuleServiceProviderImpl rsp;

	@Mock
	private Properties rspProps;

	@Mock
	private ClusterConfiguration clusterConfig;

	@Mock
	private static Cluster cluster;

	StoreInitializer storeInitializer;

	private static final String ENTITY_TABLE = "d_book";
	
	@BeforeAll
	static void setup() {
		isLegacyID = Boolean.parseBoolean(System.getProperty(Id.USE_LEGACY_ID_PROPERTY, "false"));
		String hostPort = "6379";
		String dockerPort = "6379";
		rediSearchContainer.setPortBindings(ImmutableList.of("0.0.0.0:" + hostPort + ":" + dockerPort));
		rediSearchContainer.start();
		System.setProperty("tibco.env.BE_HOME", "..");
	}
	
	private static StoreProviderConfig createStoreConfig() {
		StoreProviderConfig storeConfig = new StoreProviderConfig();
		storeConfig.setProperty("host", "localhost");
		storeConfig.setProperty("port", "6379");
		storeConfig.setProperty("database", "0");
		
		StoreConfigPojo configPojo = new StoreConfigPojo();
		configPojo.setType("Redis");
		configPojo.setClassName("com.tibco.be.redis.RedisStoreProvider");
		storeConfig.setCdp(configPojo);
		return storeConfig;
	}

	@BeforeEach
	void isTestRedisServerRunningAndHealthy() {
		if (redisStoreProvider==null) {
			MockitoAnnotations.openMocks(this);
			storeConfig = createStoreConfig();
			try 
			{
				Mockito.when(cluster.getRuleServiceProvider()).thenReturn(rsp);
				Properties props = new Properties();
				props.putIfAbsent("tibco.repourl", "./../Deployments/fdstore.ear");
				Mockito.when(rsp.getProperties()).thenReturn(props);
				Mockito.when(cluster.getClusterConfig()).thenReturn(clusterConfig);
				redisStoreProvider = new RedisStoreProvider(cluster, storeConfig);
				assertNotNull(redisStoreProvider);
				redisStoreProvider.initConnection(storeConfig.getProperties());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		assertTrue(rediSearchContainer.isRunning());
	}
	
	@Test
	@Order(1)
	void testWriteRecord() {
		Book book = createBook("The Da Vinci Code", "Dan Brown");
		assertNotNull(book);
		writeRecord(book);
		
		Book resultBook = getRecord(book.getId());
		assertNotNull(resultBook);
		assertTrue(book.equals(resultBook));
	}
	
	@Test
	@Order(2)
	void testPublishDuplicateRecord() {
		Book book = createBook("The Da Vinci Code", "Dan Brown");
		assertNotNull(book);
		writeRecord(book);
		
		Book resultBook = getRecord(book.getId());
		assertNotNull(resultBook);
		assertTrue(book.equals(resultBook));
	}
	
	@Test
	@Order(3)
	void testPublishUpdateRecord() {
		Book origBook = createBook("The Da Vinci Code", "Dan Brown");
		assertNotNull(origBook);
		Book getOrigBook = getRecord(origBook.getId());
		assertNotNull(getOrigBook);
		assertTrue(origBook.equals(getOrigBook));
		
		// updating the book
		origBook.setAuthor("Dan Brown Sr");
		origBook.setTitle("The Da Vinci Code Vol1");
		writeRecord(origBook);
		
		Book getUpdatedBook = getRecord(origBook.getId());
		assertNotNull(getUpdatedBook);
		assertTrue(origBook.equals(getUpdatedBook));
		assertFalse(getOrigBook.equals(getUpdatedBook));
	}
	
	@Test
	@Order(4)
	void testDeleteRecord() {
		Book book = createBook("The Da Vinci Code Vol1", "Dan Brown Sr");
		assertNotNull(book);
		deleteRecord(book.getId());
		
		Book resultBook = getRecord(book.getId());
		assertNull(resultBook);
	}
	
	@Test
	@Order(5)
	void testAggregateFunctions() {
		Book book0 = createBookWithId("The Da Vinci Code1", "Dan Brown1",(long) 550);
		assertNotNull(book0);
		writeRecord(book0);
		Book book1 = createBookWithId("The Da Vinci Code2", "Dan Brown2",(long) 551);
		assertNotNull(book1);
		writeRecord(book1);
		Book book2 = createBookWithId("The Da Vinci Code3", "Dan Brown3",(long) 552);
		assertNotNull(book2);
		writeRecord(book2);
		Book book3 = createBookWithId("The Da Vinci Code4", "Dan Brown4",(long) 553);
		assertNotNull(book3);
		writeRecord(book3);
		Book book4 = createBookWithId("The Da Vinci Code5", "Dan Brown5",(long) 554);
		assertNotNull(book4);
		writeRecord(book4);
		
		double count = (double) getAggregationRecord("COUNT");
		assertNotNull(count);
		assertEquals(5, count);
		
		double avg = (double) getAggregationRecord("AVG");
		assertNotNull(avg);
		assertEquals(552, avg);
		
		double min = (double) getAggregationRecord("MIN");
		assertNotNull(min);
		assertEquals(550, min);
		
		double max = (double) getAggregationRecord("MAX");
		assertNotNull(max);
		assertEquals(554, max);
		
		double sum = (double) getAggregationRecord("SUM");
		assertNotNull(sum);
		assertEquals(2760, sum);
	}
	
	
	@AfterAll
	static void destroy() {
		rediSearchContainer.stop();
		System.clearProperty("tibco.env.BE_HOME");
	}
	
	private Book getRecord(Object id) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<Object> dataTypeList = new ArrayList<Object>();
		List<String> colNameList = new ArrayList<String>();
		List<Object> colValueList = new ArrayList<Object>();

		colNameList.add("extid");
		dataTypeList.add("STRING");
		colValueList.add(((Id)id).getExtId());
		StoreRowHolder queryHolder = StoreHelper.getRow(ENTITY_TABLE, dataTypeList.toArray(),
				colNameList.toArray(new String[colNameList.size()]), colValueList.toArray());
		
		StoreRowHolder rs;
		try 
		{
			rs = redisStoreProvider.read(queryHolder);
			if(rs==null) return null;
			Map resultMap = rs.getColDataMap();
			Book book = new Book(((StoreColumnData)resultMap.get("extid")).getColumnValue());
			book.setTitle((String) ((StoreColumnData)resultMap.get("title")).getColumnValue());
			book.setAuthor((String) ((StoreColumnData)resultMap.get("author")).getColumnValue());
			return book;
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	private void writeRecord(Book book) {
		List<Object> dataTypeList = new ArrayList<Object>();
		List<String> colNameList = new ArrayList<String>();
		List<Object> colValueList = new ArrayList<Object>();

		colNameList.add("extid");
		dataTypeList.add("STRING");
		colValueList.add(book.getId().getExtId());
		
		colNameList.add("title");
		dataTypeList.add("STRING");
		colValueList.add(book.getTitle());
		
		colNameList.add("author");
		dataTypeList.add("STRING");
		colValueList.add(book.getAuthor());
	
		StoreRowHolder storeRowHolder = StoreHelper.getRow(ENTITY_TABLE,null, dataTypeList.toArray(),
				colNameList.toArray(new String[colNameList.size()]), colValueList.toArray(), null, new String[] {"extid"}, null);
		
		List<StoreRowHolder> storeRowHolderList = new ArrayList<StoreRowHolder>();
		storeRowHolderList.add(storeRowHolder);
		
		try {
			redisStoreProvider.write(storeRowHolderList);
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	private Book deleteRecord(Object id) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<Object> dataTypeList = new ArrayList<Object>();
		List<String> colNameList = new ArrayList<String>();
		List<Object> colValueList = new ArrayList<Object>();

		colNameList.add("extid");
		dataTypeList.add("STRING");
		colValueList.add(((Id)id).getExtId());
		StoreRowHolder queryHolder = StoreHelper.getRow(ENTITY_TABLE, dataTypeList.toArray(),
				colNameList.toArray(new String[colNameList.size()]), colValueList.toArray());
		
		try 
		{
			redisStoreProvider.delete(queryHolder);
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}

		return null;
	}
	
	private Object getAggregationRecord(String aggFunc) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<Object> dataTypeList = new ArrayList<Object>();
		List<String> colNameList = new ArrayList<String>();
		List<Object> colValueList = new ArrayList<Object>();

		StoreRowHolder queryHolder = StoreHelper.getRow(ENTITY_TABLE,new String[] {"extid"}, dataTypeList.toArray(),
				colNameList.toArray(new String[colNameList.size()]), colValueList.toArray(), null, new String[] {"extid"}, null);
		
		StoreRowHolder rs = null;
		try 
		{
			switch (aggFunc) {
			case "COUNT":
				rs = redisStoreProvider.readCount(queryHolder).get(0);
				break;
			case "AVG":
				rs = redisStoreProvider.readAvg(queryHolder).get(0);
				break;
			case "MIN":
				rs = redisStoreProvider.readMin(queryHolder).get(0);
				break;
			case "MAX":
				rs = redisStoreProvider.readMax(queryHolder).get(0);
				break;
			case "SUM":
				rs = redisStoreProvider.readSum(queryHolder).get(0);
				break;
			}
			Map resultMap = rs.getColDataMap();
			Double result = (Double) ((StoreColumnData)resultMap.get(aggFunc)).getColumnValue();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	private Book createBook(String title, String author) {
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		return book;
	}
	
	private Book createBookWithId(String title, String author,Long extid) {
		Book book = new Book(extid+"");
		book.setTitle(title);
		book.setAuthor(author);
		return book;
	}
}
