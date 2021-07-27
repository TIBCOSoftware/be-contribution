package com.tibco.be.mongoDB;

import java.util.Calendar;
import org.apache.commons.lang3.SerializationUtils;
import org.bson.BsonValue;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

public class MongoDBUtils {

	/**
	 * @param encryptedString
	 * @return
	 * @throws AXSecurityException
	 */
	public static String decrypt(String encryptedString) throws AXSecurityException {
		try {
			if (ObfuscationEngine.hasEncryptionPrefix(encryptedString)) {
				return (new String(ObfuscationEngine.decrypt(encryptedString)));
			}
		} finally {
			restoreProviders();
		}
		return encryptedString;
	}

	public static void restoreProviders() {
		java.security.Security.removeProvider("Entrust");
		java.security.Security.removeProvider("ENTRUST");
		java.security.Security.removeProvider("IAIK");
	}

	public static Object getValue(String DataType, BsonValue value) {
		if (value == null) {
			return null;
		}

		Object origValue = value;
		switch (DataType) {
		case "BOOLEAN":
			origValue = value.asBoolean().getValue();
			break;
		case "DATE_TIME":
			Calendar cal = Calendar.getInstance();
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			cal.setTimeInMillis(value.asDateTime().getValue());
			origValue = cal;
			break;
		case "DOUBLE":
			origValue = value.asDouble().getValue();
			break;
		case "INT32":
			origValue = value.asInt32().getValue();
			break;
		case "TIMESTAMP":
			origValue = value.asTimestamp().getValue();
			break;
		case "INT64":
			origValue = value.asInt64().getValue();
			break;
		case "NUMBER":
			origValue = value.asNumber().intValue();
			break;
		case "BINARY":
			origValue = SerializationUtils.deserialize(value.asBinary().getData());
			break;
		case "STRING":
			origValue = value.asString().getValue();
			break;
		case "NULL":
			origValue = "NULL";
			break;
		default:
			break;
		}
		return origValue;
	}
}
