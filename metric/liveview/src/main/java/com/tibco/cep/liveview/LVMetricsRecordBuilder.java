package com.tibco.cep.liveview;

import java.util.Calendar;
import java.util.Collection;

import com.streambase.sb.Timestamp;
import com.streambase.sb.TupleException;
import com.tibco.cep.kernel.model.entity.Entity;
import com.tibco.cep.kernel.model.entity.Id;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.appmetrics.AppMetricsEntityConfig;
import com.tibco.cep.runtime.appmetrics.MetricsRecordBuilder;
import com.tibco.cep.runtime.appmetrics.Tag;

/**
 * 
 * @author shivkumarchelwa
 *
 */
public class LVMetricsRecordBuilder implements MetricsRecordBuilder<LVTuple> {

	private Logger logger;
	private LVTuple lvTuple;

	public LVMetricsRecordBuilder(AppMetricsEntityConfig config, LVTuple tuple) {
		this.lvTuple = tuple;
		this.logger = LogManagerFactory.getLogManager().getLogger(LVMetricsRecordBuilder.class);
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addField(String fieldName, String fieldValue) {
		try {
			lvTuple.getTuple().setString(fieldName, fieldValue);
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					fieldName, fieldValue, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addField(String fieldName, Integer fieldValue) {
		try {
			lvTuple.getTuple().setInt(fieldName, fieldValue);
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					fieldName, fieldValue, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addField(String fieldName, Long fieldValue) {
		try {
			lvTuple.getTuple().setLong(fieldName, fieldValue);
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					fieldName, fieldValue, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addField(String fieldName, Boolean fieldValue) {
		try {
			lvTuple.getTuple().setBoolean(fieldName, fieldValue);
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					fieldName, fieldValue, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addField(String fieldName, Double fieldValue) {
		try {
			lvTuple.getTuple().setDouble(fieldName, fieldValue);
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					fieldName, fieldValue, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addField(String fieldName, Calendar fieldValue) {
		try {
			lvTuple.getTuple().setTimestamp(fieldName,
					(fieldValue != null ? new Timestamp(fieldValue.getTime()) : null));
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					fieldName, fieldValue, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addId(Id id) {
		try {
			if (Id.useLegacyID) {
				lvTuple.getTuple().setLong("id", id.getLongValue());
				lvTuple.getTuple().setString(Entity.ATTRIBUTE_EXTID, id.getExtId());
			} else {
				lvTuple.getTuple().setString(Entity.ATTRIBUTE_EXTID, id.getExtId());
			}
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					Entity.ATTRIBUTE_ID, id, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addVersion(int version) {
		try {
			lvTuple.getTuple().setInt("version__", version);
		} catch (TupleException e) {
			String exceptionMessage = String.format(
					"Error adding field [%s] and value [%s] to lvTuple.getTuple().getTuple(). Table [%s]. Error [%s].",
					"version__", version, lvTuple.getTuple().getSchema().getName(), e.getMessage());
			logger.log(Level.ERROR, exceptionMessage);
		}
		return this;
	}

	@Override
	public MetricsRecordBuilder<LVTuple> addTags(Collection<Tag> tags) {
		// Not Required
		return this;
	}

	@Override
	public LVTuple build() {
		return lvTuple;
	}

}
