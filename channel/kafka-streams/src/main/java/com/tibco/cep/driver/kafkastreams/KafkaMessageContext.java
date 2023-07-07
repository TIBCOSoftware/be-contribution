package com.tibco.cep.driver.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventContext;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.cep.driver.kafka.serializer.KafkaMapSerializer;
import com.tibco.cep.driver.util.IncludeEventType;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.be.custom.channel.BaseEventSerializer;

/**
 * EventContext for Kafka events.
 * 
 * @author moshaikh
 */
public class KafkaMessageContext implements EventContext {

	private BEKafkaConsumer beKafkaConsumer;
	private Event event;
	private final Logger logger;

	public KafkaMessageContext(Event event, BEKafkaConsumer beKafkaConsumer) {
		this.event = event;
		this.beKafkaConsumer = beKafkaConsumer;
		this.logger = beKafkaConsumer.getLogger();
	}

	@Override
	public boolean reply(Event replyEvent) {
		// TODO Auto-generated method stub

		try {

			logger.log(Level.INFO, " Message context replyEvent  \n" + Thread.currentThread().getId());

			ConsumerRecord record = ((KafkaEvent) event).getConsumerRecord();
			
			String corrID = new String(
					record.headers().lastHeader(KafkaProperties.INTERNAL_PROP_KEY_CORRELATION_ID).value());
			// byte[] arr = record.headers().lastHeader("kafka_replyTopic").value();
			byte[] arr = record.headers().lastHeader(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC).value();
			String replyTopic = new String(arr);

			BaseEventSerializer serializer = beKafkaConsumer.getSerializer();

			EventWithId event = (EventWithId) replyEvent;

			Map<String, Object> properties = new HashMap<>();
			properties.put(KafkaProperties.KEY_DESTINATION_REPLY_TOPIC, replyTopic);
			String includeEventTypeval = beKafkaConsumer.getOwnerDestination().getDestinationProperties()
					.getProperty(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE);
			final IncludeEventType includeEventType = IncludeEventType.valueOf(includeEventTypeval);

			properties.put(KafkaProperties.KEY_DESTINATION_INCLUDE_EVENTTYPE, includeEventType.isOkOnSerialize());
			// Boolean.TRUE);
			Object message = serializer.serializeUserEvent(event, properties);

			BEKafkaProducer replyProducer = beKafkaConsumer.getOwnerDestination().getProducer();

			if (message instanceof ProducerRecord) {
				ProducerRecord kafkaMessage = (ProducerRecord) message;

				kafkaMessage.headers().add(KafkaProperties.INTERNAL_PROP_KEY_CORRELATION_ID, corrID.getBytes());

				replyProducer.sendProducerRecord(kafkaMessage);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.ERROR, e, "Failed to send kafka reply either event not requested or Correlation ID missing");
			// e.printStackTrace();
		}

		return false;
	}

	@Override
	public void acknowledge() {
		this.beKafkaConsumer.setCurrentEventAcked();
	}

	@Override
	public void rollback() {
		this.beKafkaConsumer.setCurrentEventRolledback();
	}

	@Override
	public BaseDestination getDestination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}
