package com.tibco.cep.driver.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tibco.be.custom.channel.framework.CustomEvent;

public class KafkaEvent extends CustomEvent {
	
	private BEKafkaConsumer beKafkaConsumer;
	private ConsumerRecord consumerRecord ;
	
	public KafkaEvent() {
	}

	public ConsumerRecord getConsumerRecord() {
		return consumerRecord;
	}

	public void setConsumerRecord(ConsumerRecord consumerRecord) {
		this.consumerRecord = consumerRecord;
	}

	public void setBEKafkaConsumer(BEKafkaConsumer beKafkaConsumer) {
		this.beKafkaConsumer = beKafkaConsumer;
	}
	
	public BEKafkaConsumer getBEKafkaConsumer() {
		return beKafkaConsumer;
	}
}
