/*
* Copyright © 2021. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.store.cassandra;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.tibco.cep.store.Item;
import com.tibco.cep.store.cassandra.CassandraConnection;
import com.tibco.cep.store.cassandra.CassandraConnectionInfo;
import com.tibco.cep.store.cassandra.CassandraStoreContainer;
import com.tibco.cep.store.cassandra.CassandraStoreItem;

/**
 * Integration test to validate various test cases for cassandra store api.
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CassandraCatalogIntegrationTest {

	private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("cassandra");
    private static final String DEFAULT_TAG = "4.1";
    @Deprecated
    public static final String IMAGE = DEFAULT_IMAGE_NAME.getUnversionedPart();
    public static CassandraContainer cassandraContainer ;
    
	private static CassandraConnection cassandraConnection;

	private CassandraConnectionInfo storeConnInfo;

	@BeforeAll
	static void setup() {
		System.setProperty("tibco.env.BE_HOME", "..");
		cassandraContainer = new CassandraContainer<>(IMAGE);
		cassandraContainer.start();
		
		try (
	            CqlSession session = CqlSession
	                .builder()
	                .addContactPoint(cassandraContainer.getContactPoint())
	                .withLocalDatacenter(cassandraContainer.getLocalDatacenter())
	                .build()
	        ) {
	            session.execute(
	                "CREATE KEYSPACE IF NOT EXISTS test WITH replication = \n" +
	                "{'class':'SimpleStrategy','replication_factor':'1'};"
	            );

	            KeyspaceMetadata keyspace = session.getMetadata().getKeyspaces().get(CqlIdentifier.fromCql("test"));

	            session.execute("CREATE TABLE test.book(bid int PRIMARY KEY,title text,author text);");
	        }
	}
	
	private static CassandraConnectionInfo createConnectionInfo() {
		CassandraConnectionInfo connectionInfo = new CassandraConnectionInfo("Cassandra", cassandraContainer.getContactPoint().toString().split("/")[1]);
		connectionInfo.setKeySpace("test");
		return connectionInfo;
	}

	@BeforeEach
	void isTestRedisServerRunningAndHealthy() {
		if (cassandraConnection == null) {
			MockitoAnnotations.openMocks(this);
			storeConnInfo = createConnectionInfo();
			try {
				cassandraConnection = new CassandraConnection(storeConnInfo);
				assertNotNull(cassandraConnection);
				cassandraConnection.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		assertTrue(cassandraConnection.getConnection() != null);
		assertFalse(cassandraConnection.getConnection().isClosed());
	}

	@Test
	@Order(1)
	void testWriteRecord_() {
		System.out.println("CassandraCatalogIntegrationTest.testWriteRecord()");
	}

	@Test
	@Order(1)
	void testWriteRecord() {
		Book book = createBook(1, "The Da Vinci Code", "Dan Brown");
		assertNotNull(book);
		writeRecord(book);

		Book resultBook = getRecord(book.getId());
		assertNotNull(resultBook);
		assertTrue(book.equals(resultBook));
	}

	@Test
	@Order(4)
	void testDeleteRecord() {
		
		Book origBook = getRecord(1);
		assertNotNull(origBook);
		
		try {
			cassandraConnection.openContainer("Book");
			CassandraStoreContainer container = (CassandraStoreContainer) cassandraConnection.getContainer("Book");

			List<Item> getItems = new ArrayList<>();
			CassandraStoreItem cItem = new CassandraStoreItem(container);
			cItem.setValue("bid", "INTEGER", 1);
			getItems.add(cItem);

			cassandraConnection.deleteAllItems(getItems);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Book resultBook = getRecord(1);
		assertNull(resultBook);
	}
	
	
	@AfterAll
	static void destroy() {
		try {
			cassandraConnection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.clearProperty("tibco.env.BE_HOME");
	}
	
	private Book getRecord(Object id) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			cassandraConnection.openContainer("Book");
			CassandraStoreContainer container = (CassandraStoreContainer) cassandraConnection
					.getContainer("Book");
			List<Item> getItems = new ArrayList<>();
			CassandraStoreItem cItem = new CassandraStoreItem(container);
			cItem.setValue("bid", "INTEGER", 1);
			getItems.add(cItem);
			List<Item> returnItemList = cassandraConnection.getAllItems(getItems);
			CassandraStoreItem item = (CassandraStoreItem) returnItemList.get(0);
			if (item==null) {
				return null;
			}
			Book book = new Book((int) item.getValue("bid", "INTEGER"));
			book.setAuthor((String) item.getValue("author", "STRING"));
			book.setTitle((String) item.getValue("title", "STRING"));
			return book;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void writeRecord(Book book) {
		try {
			cassandraConnection.openContainer("Book");
			CassandraStoreContainer container = (CassandraStoreContainer) cassandraConnection
					.getContainer("Book");
			List<Item> putItems = new ArrayList<>();
			CassandraStoreItem cItem = new CassandraStoreItem(container);
			cItem.setValue("bid", "INTEGER", book.getId());
			cItem.setValue("title", "STRING", book.getTitle());
			cItem.setValue("author", "STRING", book.getAuthor());
			putItems.add(cItem);
			cassandraConnection.putAllItems(putItems);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Book createBook(int id, String title, String author) {
		Book book = new Book(id);
		book.setTitle(title);
		book.setAuthor(author);
		return book;
	}
}
