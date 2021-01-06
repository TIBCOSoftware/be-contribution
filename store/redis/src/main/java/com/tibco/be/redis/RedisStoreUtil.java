/**
 * # Copyright (c) 2019-2020. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */
package com.tibco.be.redis;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SerializationUtils;

import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

/**
 * 
 * @author TIBCO Software
 *
 * This is utility class for Redis Store.
 */
public class RedisStoreUtil {

	public static Object getValue(String origDataType, String value) {
		if (value==null|| value.equals("null")) {
			return null;
		}
		value = value.replaceAll("\\\\", "");
		Object origValue = value;
		switch (origDataType) {
		case "BOOLEAN":
			origValue = Boolean.parseBoolean(value);
			break;
		case "DATETIME":
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); 
			try {
				cal.setTime(sdf.parse(value));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			origValue = cal;
			break;
		case "DOUBLE":
			origValue = Double.parseDouble(value);
			break;
		case "FLOAT":
			origValue = Float.parseFloat(value);
			break;
		case "INTEGER":
			origValue = Integer.parseInt(value);
			break;
		case "LONG":
			origValue = Long.parseLong(value);
			break;
		case "SHORT":
			origValue = Short.parseShort(value);
			break;
		case "OBJECT":
			ByteBuffer bytes = ByteBuffer.wrap(DatatypeConverter.parseHexBinary(value));
			if (bytes==null) {
				return null;
			}
			origValue = SerializationUtils.deserialize(bytes.array());
			break;
		case "STRING":
			origValue = value;
			break;
		default:
			break;
		}
		return origValue;
	}

	public static boolean isFieldNumeric(String dataType) {
		if (null==dataType) {
			return false;
		}
		switch (dataType) {
		case "BOOLEAN":
			return false;
		case "DATETIME":
			return true;
		case "DOUBLE":
			return true;
		case "FLOAT":
			return true;
		case "INTEGER":
			return true;
		case "LONG":
			return true;
		case "SHORT":
			return true;
		case "OBJECT":
			return false;
		case "STRING":
			return false;
		default:
			break;
		}
		return false;
	}

	/**
	 * @param encryptedString
	 * @return
	 * @throws AXSecurityException
	 */
	public static String decrypt(String encryptedString) throws AXSecurityException {
		if (ObfuscationEngine.hasEncryptionPrefix(encryptedString)) {
			return(new String(ObfuscationEngine.decrypt(encryptedString)));
		}
		return encryptedString;
	}
	
	public static String sanitizeValue(String colValueString) {
		String regex = "([\",\\.<>{}\\[\\]\\':;!@#$%^&*\\(\\)+\\-=~)])";
//		String regex = "([$-:@\\(\\)])";
		if (null == colValueString) {
			return colValueString;
		}
		return colValueString.replaceAll(regex, "\\\\$1");
	}
}
