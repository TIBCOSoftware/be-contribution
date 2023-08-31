/**
 * 
 */
package com.tibco.cep.driver.kafkastreams;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shivkumarchelwa
 *
 */
public class BETransformationModel {

	private String type;
	private Map<String, Object> inputs = new LinkedHashMap<>();

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the inputs
	 */
	public Map<String, Object> getInputs() {
		return inputs;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addInput(String name, Object value) {
		this.inputs.put(name, value);
	}
}
