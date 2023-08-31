package com.tibco.cep.driver.mqtt.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Properties;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.cep.driver.mqtt.MqttProperties;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * @author ssinghal
 *
 */
public abstract class MqttBaseSerializer extends BaseEventSerializer{
	
	@Override
	public void initUserEventSerializer(String destinationName, Properties destinationProperties, Logger logger) {
		
	}

	protected boolean isIncludeEventType(Map<String, Object> properties) {
		if (properties == null || !properties.containsKey(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE)) {
			return true;
		} else {
			return (boolean) properties.get(MqttProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE);
		}
	}
	
	protected Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
	
	protected byte[] serialize(Object value) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		try{
			oos.writeObject(value);
		}
		finally{
			oos.close();
			return baos.toByteArray();
		}
	}

}
