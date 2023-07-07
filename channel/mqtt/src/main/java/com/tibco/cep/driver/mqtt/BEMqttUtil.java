/**
 * 
 */
package com.tibco.cep.driver.mqtt;

/**
 * @author ssinghal
 *
 */
public class BEMqttUtil {
	
	static boolean isIncludeEventTypeWhileDeserialize(String includeEventTypeValue) {
		return ("ALWAYS".equalsIgnoreCase(includeEventTypeValue)) || ("ON_DESERIALIZE".equalsIgnoreCase(includeEventTypeValue));
	}
	
	static boolean isIncludeEventTypeWhileSerialize(String includeEventTypeValue) {
		return "ALWAYS".equalsIgnoreCase(includeEventTypeValue) || "ON_SERIALIZE".equalsIgnoreCase(includeEventTypeValue);
	}

}
