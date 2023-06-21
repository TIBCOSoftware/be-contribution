package com.tibco.cep.driver.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

public class KafkaListnerManager {

	private ConcurrentHashMap<String, KafkaRequestCallback> hashMap;
	private ExecutorService executorService;
	private int noOfThreads;
	private Properties destinationProps;
	private Properties beProperties;
	private ArrayList<Thread> requestConsumerList;
	private boolean listenerManagerStarted;
	private KafkaDestination channelDestination;
	private ConsumerState requestedState;
	private KafkaChannel kafkaChannel;
	private final Logger logger;

	private enum ConsumerState {
		ACTIVE, SUSPENDED, STOPPED
	}

	public boolean isListenerManagerStarted() {
		return listenerManagerStarted;
	}

	public void setListenerManagerStarted(boolean listenerManagerStarted) {
		this.listenerManagerStarted = listenerManagerStarted;
	}

	public KafkaListnerManager(Properties destinationProps, Properties beProperties,
			ConcurrentHashMap<String, KafkaRequestCallback> map, KafkaDestination ownerDestination,
			KafkaChannel channel) {

		noOfThreads = 1;
		hashMap = map;
		executorService = Executors.newFixedThreadPool(noOfThreads);
		this.beProperties = beProperties;
		this.destinationProps = new Properties(destinationProps);
		requestConsumerList = new ArrayList<>();
		listenerManagerStarted = Boolean.FALSE;
		this.channelDestination = ownerDestination;
		this.kafkaChannel = channel;
		this.logger = this.kafkaChannel.getLogger();

	}

	public void start() {

		synchronized (this) {
			if (listenerManagerStarted == Boolean.FALSE) {

				listenerManagerStarted = Boolean.TRUE;

				// Thread replyConsumer =
				for (int i = 0; i < noOfThreads; i++) {
					Thread requestConsumer = null;
					try {
						destinationProps.put(KafkaProperties.KEY_DESTINATION_AUTO_OFFSET_RESET, "latest");
						requestConsumer = new Thread(new BEKafkaConsumer(null, destinationProps, beProperties,
								"requestConsumer", channelDestination) {
							@Override
							public void run() { // anonymous class

								KafkaConsumer consumer = this.getConsumer();
								subscribe(consumer);

								while (true) {

									if (requestedState == ConsumerState.STOPPED) {
										// logger.log(Level.INFO, "Stopping KafkaConsumer - " + this);
										consumer.unsubscribe();
										consumer.close();
										return;
									}

									int pollInterval = Integer.parseInt(destinationProps
											.getProperty(KafkaProperties.KEY_DESTINATION_POLL_INTERVAL, "1000"));

									ConsumerRecords records = consumer.poll(pollInterval);

									for (Object record : records) {
										try {
											ConsumerRecord consumerRecord = (ConsumerRecord) record;

											String correLationID = new String(consumerRecord.headers()
													.lastHeader(KafkaProperties.INTERNAL_PROP_KEY_CORRELATION_ID)
													.value());

											if (hashMap.containsKey(correLationID)) {
												KafkaRequestCallback callback = hashMap.get(correLationID);
												callback.kafkaReplyArrived(consumerRecord);
												if (hashMap.containsKey(correLationID))
													hashMap.remove(correLationID);
											}

										} catch (Exception e) {
											logger.log(Level.ERROR, e, "Error while processing event.");
											// e.printStackTrace();
										}
									}
								}

							}
						});

						destinationProps.remove(KafkaProperties.KEY_DESTINATION_AUTO_OFFSET_RESET);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.log(Level.ERROR, e, "Error in starting KafkaListner thread.");
						// e.printStackTrace();
					}

					if (null != requestConsumer)
						requestConsumerList.add(requestConsumer);
				}

				for (int i = 0; i < requestConsumerList.size(); i++) {
					executorService.execute(requestConsumerList.get(i));
				}

			}
		}
	}

	public void shutdown() {
		requestedState = ConsumerState.STOPPED;
	}

	private void subscribe(KafkaConsumer consumer) {
		// String str = kafkaChannel.getKafkaRequestReply().getReplyTopic();
		consumer.subscribe(Arrays.asList(kafkaChannel.getKafkaRequestReply().getReplyTopic()));
	}

}
