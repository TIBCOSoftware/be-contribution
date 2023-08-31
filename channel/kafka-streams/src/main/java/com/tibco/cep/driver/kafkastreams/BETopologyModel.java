/**
 * 
 */
package com.tibco.cep.driver.kafkastreams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shivkumarchelwa
 *
 */
public class BETopologyModel {

	private final List<BETransformationModel> transformations;

	public BETopologyModel() {
		this.transformations = new ArrayList<>();
	}

	/**
	 * @return the list
	 */
	public List<BETransformationModel> getTransformations() {
		return transformations;
	}

	public void addTransformation(BETransformationModel t) {
		transformations.add(t);
	}

	public void removeTransformation(BETransformationModel t) {
		transformations.remove(t);
	}

	public void replaceTransformation(int index, BETransformationModel t) {
		transformations.set(index, t);
	}
}
