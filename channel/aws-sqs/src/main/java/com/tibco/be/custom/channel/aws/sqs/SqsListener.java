/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.custom.channel.aws.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicContext;
import com.tibco.be.custom.channel.aws.sqs.containercredentials.ContainerContext;
import com.tibco.be.custom.channel.aws.sqs.defaultcredentials.DefaultContext;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLContext;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
import com.amazonaws.services.sqs.AmazonSQS;

import java.util.List;

public class SqsListener implements Runnable {

    private final int maxNumberOfMessages;
    private final int pollingInterval;
    private final EventProcessor eventProcessor;
    private final BaseEventSerializer serializer;
    private final Logger logger;
    private final int connectionRetryCount;
    private final long connectionRetrySleep;

    private Object context = null;


    public SqsListener(final Object context, final int maxNumberOfMessages, final int pollingInterval, final int threadNumber, final int retries, final long retrySleep,
                       final EventProcessor eventProcessor, BaseEventSerializer serializer, Logger logger) {

        this.context = context;
        this.maxNumberOfMessages = maxNumberOfMessages;
        this.pollingInterval = pollingInterval;
        this.eventProcessor = eventProcessor;
        this.serializer = serializer;
        this.logger = logger;
        this.connectionRetryCount = retries;
        this.connectionRetrySleep = retrySleep;
    }

    public void start() {
        logger.log(Level.DEBUG,"Listener thread starting");
    }

    public void stop() {
        logger.log(Level.DEBUG,"Listener thread stopping");
    }

    @Override
    public void run() {

        RetryOnException retryHandler = new RetryOnException(connectionRetryCount, connectionRetrySleep);

        String queueUrl = "";

        if (context instanceof BasicContext) {
            logger.log(Level.DEBUG,"Using Basic Context");
            queueUrl = ((BasicContext)context).getQueueUrl();
        } else if (context instanceof SAMLContext)  {
            logger.log(Level.DEBUG,"Using SAML Context");
            queueUrl = ((SAMLContext)context).getQueueUrl();
        } else if (context instanceof DefaultContext) {
            logger.log(Level.DEBUG,"Using Default Context");
            queueUrl = ((DefaultContext)context).getQueueUrl();
        } else if (context instanceof ContainerContext) {
            logger.log(Level.DEBUG,"Using Contaimer Context");
            queueUrl = ((ContainerContext)context).getQueueUrl();
        } else {
            throw new RuntimeException("Unknown context");
        }



        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(pollingInterval)
                .withMaxNumberOfMessages(maxNumberOfMessages);

        logger.log(Level.DEBUG,"Listener thread running on SQS URL: "+queueUrl);

        while(true) {

            logger.log(Level.DEBUG,"Starting next poll loop");
            AmazonSQS client = null;

            try {
                client = Client.createClient(this.context);

                List<Message> messages = null;
                try {
                    logger.log(Level.DEBUG,"Attempting blocking receiveMessage() call");
                    ReceiveMessageResult receiveMessageResult = client.receiveMessage(receiveMessageRequest);
                    logger.log(Level.DEBUG,"Return from blocking receiveMessage() call");
                    if (receiveMessageResult != null) {
                        int httpStatusCode = receiveMessageResult.getSdkHttpMetadata().getHttpStatusCode();
                        logger.log(Level.DEBUG,"HTTP Status Code : " + httpStatusCode);
                        if (httpStatusCode == 200) {
                            messages = receiveMessageResult.getMessages();
                            logger.log(Level.DEBUG, "Got "+messages.size()+" messages");
                            Event event = null;
                            for (Message message : messages) {

                                try {
                                    logger.log(Level.DEBUG,"Processing message");
                                    event = serializer.deserializeUserEvent(message,null);

                                    if (event != null) {
                                        logger.log(Level.DEBUG,"Dispatching message to Event Processor");
                                        eventProcessor.processEvent(event);
                                        logger.log(Level.DEBUG,"Dispatch completed");

                                        logger.log(Level.DEBUG,"Deleting SQS message");
                                        client.deleteMessage(queueUrl,message.getReceiptHandle());
                                        logger.log(Level.DEBUG,"SQS message deleted");
                                    }
                                } catch(Exception e) {
                                    logger.log(Level.ERROR,e,"Exception occurred while processing message");
                                }
                            }
                        } else {
                            logger.log(Level.ERROR,"receiveMessage failed with HTTP Status Code : " + httpStatusCode);
                        }

                    } else {
                        logger.log(Level.ERROR, "Unable to receive messages");
                    }

                } catch(Exception e) {
                    logger.log(Level.ERROR,e,"Unable to receive messages from SQS");
                }

            } catch (Exception e) {
                logger.log(Level.ERROR, e, "Unable to get client connection to SQS");

                try {
                    retryHandler.exceptionOccurred();
                    continue;
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }

            }


            logger.log(Level.DEBUG,"Poll loop completed");
        }
    }
}
