package com.tibco.cep.store.cassandra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.IndexMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
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
		Map<CqlIdentifier, TableMetadata> tables = keyspaceMetadata.getTables();
		for (Iterator iterator = tables.keySet().iterator(); iterator.hasNext();) {
			CqlIdentifier cqlIdentifier = (CqlIdentifier) iterator.next();
			tableNames.add(cqlIdentifier.asCql(true));
		}
		
		
		return tableNames.toArray(new String[0]);
	}

	@Override
	public String getName() throws Exception {
		return keyspaceMetadata.getName().asCql(true);
	}

	@Override
	public String[] getContainerFieldNames(String containerName) throws Exception {
		List<String> columnNames = new ArrayList<String>();
		Map<CqlIdentifier, ColumnMetadata> columsMetaData = getTableMetadata(containerName).getColumns();
		for (Iterator iterator = columsMetaData.values().iterator(); iterator.hasNext();) {
			ColumnMetadata columnMetadata = (ColumnMetadata) iterator.next();
			columnNames.add(columnMetadata.getName().asCql(true));
		}
		return columnNames.toArray(new String[0]);
	}

	@Override
	public String getContainerFieldType(String containerName, String fieldName) throws Exception {
		if (getTableMetadata(containerName).getColumn(fieldName).isPresent()) {
			return getTableMetadata(containerName).getColumn(fieldName).get().getType().asCql(true, true);
		}
		else
		{
			throw new Exception(String.format("No such fieldName[%s] found. Please check the fieldName.", fieldName));
		}
	}

	@Override
	public String getContainerPrimaryIndex(String containerName) throws Exception {
		List<String> primaryIndexNames = new ArrayList<String>();
		List<ColumnMetadata> primaryKeyMetaData = getTableMetadata(containerName).getPrimaryKey();
		for (Iterator iterator = primaryKeyMetaData.iterator(); iterator.hasNext();) {
			ColumnMetadata columnMetadata = (ColumnMetadata) iterator.next();
			primaryIndexNames.add(columnMetadata.getName().asCql(true));
		}
		return primaryIndexNames.toString();
	}

	@Override
	public String[] getContainerIndexNames(String containerName) throws Exception {
		List<String> indexNames = new ArrayList<String>();
		Map<CqlIdentifier, IndexMetadata> indexesMetaData = getTableMetadata(containerName).getIndexes();
		for (Iterator<IndexMetadata> iterator = indexesMetaData.values().iterator(); iterator.hasNext();) {
			IndexMetadata indexMetadata = (IndexMetadata) iterator.next();
			indexNames.add(indexMetadata.getName().asCql(true));
		}
		return indexNames.toArray(new String[0]);
	}

	@Override
	public String[] getContainerIndexFieldNames(String containerName, String indexName) throws Exception {
		 List<String> indexFieldNames = new ArrayList<>();
		 Optional<IndexMetadata> indexMetadata = getTableMetadata(containerName).getIndex(indexName);
		 indexFieldNames.add(indexMetadata.get().getTarget());
		 return indexFieldNames.toArray(new String[0]);
	}
	
	public TableMetadata getTableMetadata(String containerName) throws Exception {
		Optional<TableMetadata> tableMetadata = keyspaceMetadata.getTable(containerName);
		if (!tableMetadata.isPresent()) throw new Exception(String.format("No such container[%s] found. Please check the container name", containerName));
		return tableMetadata.get();
	}
	
}
