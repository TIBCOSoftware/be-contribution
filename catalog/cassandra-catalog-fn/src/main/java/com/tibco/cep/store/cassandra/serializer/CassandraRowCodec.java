package com.tibco.cep.store.cassandra.serializer;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.serializers.FieldType;
import com.tibco.cep.store.Item;
import com.tibco.cep.store.cassandra.CassandraStoreItem;
import com.tibco.cep.store.serializer.ItemCodec;

/**
 * @author rakulkar
 *
 */
public class CassandraRowCodec implements ItemCodec {
	private Logger logger;
	
	public CassandraRowCodec() {
		logger = LogManagerFactory.getLogManager().getLogger(CassandraRowCodec.class);
	}
	
	@Override
	public void putInItem(Item item, String fieldName, FieldType fieldType, Object fieldValue) {
		CassandraStoreItem dgItem = (CassandraStoreItem) item;
		Map<String, Object> row = dgItem.getKeyValueMap();
		if (fieldName.endsWith("__")||fieldName.equalsIgnoreCase("id")) {
			return;
		}
		try {
				switch (fieldType) {
				case DATETIME:
					if (fieldValue instanceof Calendar) {
						Calendar cal = (Calendar) fieldValue;
						SimpleDateFormat sdf;
						sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
						sdf.setTimeZone(cal.getTimeZone());
						fieldValue = ZonedDateTime.parse(sdf.format(cal.getTime()));
					}
					break;
				case BLOB:
					fieldValue = ByteBuffer.wrap(SerializationUtils.serialize((Serializable) fieldValue));
					break;
				}
				row.put(fieldName, fieldValue);
		} catch(Exception e) {
				throw new RuntimeException(e);
		}
	}
	
	@Override
	public Object getFromItem(Item item, String fieldName, FieldType fieldType) {
		CassandraStoreItem cassandraStoreItem = (CassandraStoreItem) item;
		Map<String,Object> keyValueMap = cassandraStoreItem.getKeyValueMap();
		return keyValueMap.get(fieldName.toLowerCase());
	}
	
}
