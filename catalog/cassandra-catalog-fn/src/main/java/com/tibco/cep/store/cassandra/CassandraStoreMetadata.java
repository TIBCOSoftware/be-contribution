package com.tibco.cep.store.cassandra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.IndexMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.tibco.cep.store.StoreMetadata;

/**
 * @author rakulkar
 */
public class CassandraStoreMetadata implements StoreMetadata {

	private KeyspaceMetadata keyspaceMetadata;

	public CassandraStoreMetadata(KeyspaceMetadata keyspaceMetadata) {
		this.keyspaceMetadata = keyspaceMetadata;
	}

	@Override
	public String getVersion() throws Exception {
		return "1";
		//throw new UnsupportedOperationException("Method not supported.");
	}

	@Override
	public String[] getContainerNames() throws Exception {
		List<String> tableNames = new ArrayList<>();
		Collection<TableMetadata> tables = keyspaceMetadata.getTables();
		for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
			TableMetadata tableMetadata = (TableMetadata) iterator.next();
			tableNames.add(tableMetadata.getName());
		}
		return tableNames.toArray(new String[0]);
	}

	@Override
	public String getName() throws Exception {
		return keyspaceMetadata.getName();
	}

	@Override
	public String[] getContainerFieldNames(String containerName) throws Exception {
		List<String> columnNames = new ArrayList<String>();
		List<ColumnMetadata> columsMetaData = getTableMetadata(containerName).getColumns();
		for (Iterator iterator = columsMetaData.iterator(); iterator.hasNext();) {
			ColumnMetadata columnMetadata = (ColumnMetadata) iterator.next();
			columnNames.add(columnMetadata.getName());
		}
		return columnNames.toArray(new String[0]);
	}

	@Override
	public String getContainerFieldType(String containerName, String fieldName) throws Exception {
		return getTableMetadata(containerName).getColumn(fieldName).getType().getName().name();
	}

	@Override
	public String getContainerPrimaryIndex(String containerName) throws Exception {
		List<String> primaryIndexNames = new ArrayList<String>();
		List<ColumnMetadata> primaryKeyMetaData = getTableMetadata(containerName).getPrimaryKey();
		for (Iterator iterator = primaryKeyMetaData.iterator(); iterator.hasNext();) {
			ColumnMetadata columnMetadata = (ColumnMetadata) iterator.next();
			primaryIndexNames.add(columnMetadata.getName());
		}
		return primaryIndexNames.toString();
	}

	@Override
	public String[] getContainerIndexNames(String containerName) throws Exception {
		List<String> indexNames = new ArrayList<String>();
		Collection<IndexMetadata> indexesMetaData = getTableMetadata(containerName).getIndexes();
		for (Iterator<IndexMetadata> iterator = indexesMetaData.iterator(); iterator.hasNext();) {
			IndexMetadata indexMetadata = (IndexMetadata) iterator.next();
			indexNames.add(indexMetadata.getName());
		}
		return indexNames.toArray(new String[0]);
	}

	@Override
	public String[] getContainerIndexFieldNames(String containerName, String indexName) throws Exception {
		 List<String> indexFieldNames = new ArrayList<>();
		 IndexMetadata indexMetadata = getTableMetadata(containerName).getIndex(indexName);
		 System.out.println("CassandraStoreMetadata.getContainerIndexFieldNames()" + indexMetadata.getTarget());
		 indexFieldNames.add(indexMetadata.getTarget());
		 return indexFieldNames.toArray(new String[0]);
	}
	
	public TableMetadata getTableMetadata(String containerName) throws Exception {
		TableMetadata tableMetadata = keyspaceMetadata.getTable(containerName);
		if (tableMetadata == null) throw new Exception(String.format("No such container[%s] found. Please check the container name", containerName));
		return tableMetadata;
	}
	
}
