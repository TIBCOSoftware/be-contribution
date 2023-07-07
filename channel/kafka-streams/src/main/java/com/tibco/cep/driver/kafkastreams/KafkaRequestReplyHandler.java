package com.tibco.cep.driver.kafka;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

public class KafkaRequestReplyHandler {

	private String replyTopic;
	private Logger logger;

	public String getReplyTopic() {
		return replyTopic;
	}

	public ConcurrentHashMap<String, KafkaRequestCallback> getReqCallbackMap() {
		return reqCallbackMap;
	}

	private ConcurrentHashMap<String, KafkaRequestCallback> reqCallbackMap;

	public KafkaRequestReplyHandler(String channelUri, String engineName, Logger logger) {

		String str = channelUri.replace('/', '-').substring(1, channelUri.length());

		if (replyTopic == null) {
			replyTopic = str + "-" + (null != engineName ? engineName : UUID.randomUUID().toString().substring(0, 4));

		}

		if (reqCallbackMap == null) {
			reqCallbackMap = new ConcurrentHashMap<String, KafkaRequestCallback>();
		}

		this.logger = logger;

	}

	public void getResponseFromReplyTopic(Event[] receivedEvt, Object lock, String correlationId,
			Map<String, Object> serializationPropertise, BaseEventSerializer serializer) {

		KafkaRequestCallback callback = new KafkaRequestCallback() {

			@Override
			public void kafkaReplyArrived(ConsumerRecord record) {

				synchronized (lock) {
					// Event creation
					Event event = null;
					try {
						event = serializer.deserializeUserEvent(record, serializationPropertise);

						receivedEvt[0] = event;

						lock.notify();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.log(Level.INFO, "Error in handling the callback: ");
						// e.printStackTrace();
					}
				}
			}
		};

		reqCallbackMap.put(correlationId, callback);

	}

}
