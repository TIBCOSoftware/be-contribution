/**
 * 
 */
package com.tibco.metric.store.elasticsearch;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;

import com.tibco.cep.kernel.model.entity.Id;
import com.tibco.cep.runtime.appmetrics.AppMetricsEntityConfig;
import com.tibco.cep.runtime.appmetrics.MetricRecord;
import com.tibco.cep.runtime.appmetrics.MetricsRecordBuilder;
import com.tibco.cep.runtime.appmetrics.Tag;

/**
 * This class builds a record object to be published into Elasticsearch. Every RTC change list entry 
 * along with the operation type is used to create the corresponding ElasticSearchRecord for publishing.
 */
public class ElasticSearchMetricsRecordBuilder implements MetricsRecordBuilder<ElasticSearchRecord> {
	
	private static final String FIELD_NAME_ID = "Id";
	private static final String FIELD_NAME_EXTID = "ExtId";
	private static final String FIELD_NAME_VERSION = "Version";
	
	private Map<String, Object> properties;
	private String indexName;
	private MetricRecord.OpType opType;
	
	public ElasticSearchMetricsRecordBuilder(AppMetricsEntityConfig config, MetricRecord.OpType opType) {
		properties = new HashMap<String, Object>();
		this.opType = opType;
		indexName = getIndexName(config.getEntityUri());
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addField(String name, String value) {
		properties.put(name, value);
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addField(String name, Integer value) {
		properties.put(name, value);
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addField(String name, Double value) {
		properties.put(name, value);
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addField(String name, Boolean value) {
		properties.put(name, value);
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addField(String name, Long value) {
		properties.put(name, value);
		return null;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addField(String name, Calendar value) {
		properties.put(name, value);
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addId(Id id) {
		if (Id.useLegacyID) {
			properties.put(FIELD_NAME_ID, id.getLongValue());
			if (id.getExtId() != null && !id.getExtId().isEmpty()) {
				properties.put(FIELD_NAME_EXTID, id.getExtId());
			}
		} else properties.put(FIELD_NAME_ID, id.toString());
		
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addTags(Collection<Tag> arg0) {
		return this;
	}

	@Override
	public MetricsRecordBuilder<ElasticSearchRecord> addVersion(int version) {
		properties.put(FIELD_NAME_VERSION, version);
		return this;
	}

	@Override
	public ElasticSearchRecord build() {
		String idValue = (Id.useLegacyID) ? String.valueOf((Long)properties.remove(FIELD_NAME_ID)) : (String) properties.remove(FIELD_NAME_ID);

		DocWriteRequest<?> request = null;
		switch(opType) {
		case ADD: request = new IndexRequest(indexName).id(idValue).opType(OpType.CREATE).source(properties); break;
		case MODIFY: request = new UpdateRequest(indexName, idValue).doc(properties); break;
		case DELETE: request = new DeleteRequest(indexName, idValue); break;
		default: throw new RuntimeException(String.format("Invalid/null operation type[%s]", opType));
		}
		
		return new ElasticSearchRecord(request);
	}
	
	private String getIndexName(String entityUri) {
		return entityUri.substring(1).replace("/", "_").concat("_index").toLowerCase();
	}
}
