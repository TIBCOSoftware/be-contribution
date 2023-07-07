package com.tibco.cep.analytics.statistica.io.utils;

import java.util.HashMap;
import java.util.Map;

import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.element.Property;
import com.tibco.cep.runtime.model.element.PropertyAtom;
import com.tibco.cep.runtime.model.event.SimpleEvent;

public class StatisticaFunctionsUtils {
	
	/**
	 * Parse parameters inside a concept and return as a Map 
	 * @param event - Event from which paremeters are to be extracted
	 * @return Map of parameter key and values
	 * @throws Exception
	 */
	public static Map<String, Object> parseParameters(SimpleEvent event) throws Exception {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String[] fields = event.getPropertyNames();
		for (String field : fields) {
			parameterMap.put(field, event.getProperty(field));
		}
		return parameterMap;
	}

	/**
	 * Parse parameters inside a concept and return as a Map 
	 * @param concept - Concept from which paremeters are to be extracted
	 * @return Map of parameter key and values
	 */
	public static Map<String, Object> parseParameters(Concept concept) {
		Property[] props = concept.getProperties();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        for (Property prop : props) {
        	if (prop instanceof PropertyAtom) {
                PropertyAtom pas = (PropertyAtom) prop;
                parameterMap.put(pas.getName(), pas.getValue());
            }
        }
        return parameterMap;
	}

}
