/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */

package com.tibco.be.custom.channel.aws.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
//import software.amazon.awssdk.services.sqs.SqsClient;
import com.amazonaws.services.sqs.AmazonSQS;

import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
//import software.amazon.awssdk.services.sqs.model.Message;
//import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

public class SqsListener implements Runnable {

    private final AmazonSQS sqsClient;
    private final String queueUrl;
    private final int maxNumberOfMessages;
    private final int pollingInterval;
    private final EventProcessor eventProcessor;
    private final BaseEventSerializer serializer;
    private final Logger logger;

    public SqsListener(final AmazonSQS sqsClient, final String queueUrl, final int maxNumberOfMessages, final int pollingInterval, final int threadNumber,
                       final EventProcessor eventProcessor, BaseEventSerializer serializer, Logger logger) {

        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.maxNumberOfMessages = maxNumberOfMessages;
        this.pollingInterval = pollingInterval;
        this.eventProcessor = eventProcessor;
        this.serializer = serializer;
        this.logger = logger;
    }

    public void start() {
        logger.log(Level.DEBUG,"Listener thread starting");
    }

    public void stop() {
        logger.log(Level.DEBUG,"Listener thread stopping");
    }

    @Override
    public void run() {

        logger.log(Level.DEBUG,"Listener thread running on SQS URL: "+this.queueUrl);

        while(true) {

            logger.log(Level.DEBUG,"Listener waiting for SQS message");

                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withWaitTimeSeconds(pollingInterval)
                        .withMaxNumberOfMessages(1);


            List<Message> messages = null;
            try {
                messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();
            } catch(Exception e) {
                logger.log(Level.DEBUG,"No messages found");
            }

            logger.log(Level.DEBUG,"Receive message poll done");

            Event event = null;
            for (Message message : messages) {

                logger.log(Level.DEBUG,"Processing message");
                try {
                    event = serializer.deserializeUserEvent(message,null);
                } catch(Exception e) {
                    logger.log(Level.ERROR,"SqsListener : Exception occurred while deserializing message : " +e );
                }

                if (event != null) {
                    try {
                        logger.log(Level.DEBUG,"Dispatching message to Event Processor");
                        eventProcessor.processEvent(event);
                        logger.log(Level.DEBUG,"Dispatch completed");
                    } catch(final Exception e) {
                        logger.log(Level.ERROR, e, "SqsListener : Exception occurred while processing event : "+ e);
                    }
                }

                logger.log(Level.DEBUG,"Deleting SQS message");

                try {
                    sqsClient.deleteMessage(queueUrl,message.getReceiptHandle());

                } catch (Exception e) {
                    logger.log(Level.ERROR, e, "Unable to delete message from SQS");
                    e.printStackTrace();
                }

                logger.log(Level.DEBUG,"SQS message deleted");
            }

            logger.log(Level.DEBUG,"Run completed");
        }
    }
}
