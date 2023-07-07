package com.tibco.cep.driver.kafka.serializer;

public interface KafkaSerializer {

	public String keySerializer();
	
	public String valueSerializer();
	
	public String keyDeserializer();
	
	public String valueDeserializer();
}
