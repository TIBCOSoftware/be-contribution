package com.tibco.cep.driver.kafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tibco.be.custom.channel.Event;

public interface KafkaRequestCallback {
	
	public void kafkaReplyArrived(ConsumerRecord record) ;

}
