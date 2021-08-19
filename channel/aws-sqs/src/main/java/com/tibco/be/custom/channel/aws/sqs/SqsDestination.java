/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.custom.channel.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.tibco.be.custom.channel.*;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicContext;
import com.tibco.be.custom.channel.aws.sqs.containercredentials.ContainerContext;
import com.tibco.be.custom.channel.aws.sqs.defaultcredentials.DefaultContext;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLContext;
import com.tibco.cep.kernel.service.logging.Level;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class SqsDestination extends BaseDestination {

    private List<SqsListener> listeners = new ArrayList<SqsListener>();

    private AmazonSQS sqsClient;

    private String queueUrl = "";
    private int threads = 0;
    private int pollInterval;
    private int maxMessages = 1;

    private int retryConnectionCount = 3;
    private long retryConnectionSleep = 2000;


    private String authType = "";

    // keep a reference to the channel's executor service
    private ExecutorService executor;

    private static final String DEFAULT_RETRIES = "3";
    private static final String DEFAULT_TIME_TO_WAIT_MS = "2000";

    private Object context = null;

    public void init() throws Exception {


        logger.log(Level.DEBUG,"Initialising SQS Destination");
        

        authType = getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_AUTH_TYPE);

        retryConnectionCount = Integer.parseInt(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_CONNECTION_RETRY_COUNT,DEFAULT_RETRIES));
        retryConnectionSleep = Long.parseLong(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_CONNECTION_RETRY_TIME_TO_WAIT_MS, DEFAULT_TIME_TO_WAIT_MS));


        if (authType.equals("CREDENTIALS")) {
            context = new BasicContext.BasicContextBuilder()
                .setAccessKey(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_CREDENTIALS_ACCESS_KEY))
                .setSecretKey(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_CREDENTIALS_SECRET_KEY))
                .setRegionName(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_REGION))
                .setRoleArn(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_CREDENTIALS_ROLE_ARN))
                .setSessionName(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_CREDENTIALS_SESSION_NAME))
                .setTokenExpirationDuration(Integer.parseInt(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_CREDENTIALS_EXPIRATION)) * 60)
                .setQueueUrl(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_QUEUE_URL))
                .build();
        } else if(authType.equals("SAML")){
            context = new SAMLContext.SAMLContextBuilder()
                .setIdpUsername(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_IDP_USERNAME))
                .setIdpPassword(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_IDP_PASSWORD))
                .setRegionName(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_REGION))
                .setIdProviderType(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_IDP_PROVIDER_TYPE))
                .setIdProviderType(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_IDP_PROVIDER_TYPE))
                .setIdpUseProxy(false)
                .setIdpEntryUrl(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_IDP_PROVIDER_URL))
                .setAwsRole(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_ROLE))
                .setTokenExpirationDuration(Integer.parseInt(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_SQS_SAML_TOKEN_EXPIRY_DURATION)))
                .setQueueUrl(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_QUEUE_URL))
                .build();
        } else if(authType.equals("DEFAULT")) {
            context = new DefaultContext.DefaultContextBuilder()
                .setRegionName(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_REGION))
                .setQueueUrl(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_QUEUE_URL))
                .build();
        } else if(authType.equals("CONTAINER")) {
            context = new ContainerContext.ContainerContextBuilder()
                    .setRegionName(getChannel().getChannelProperties().getProperty(SqsChannelProperties.CONFIG_AWS_REGION))
                    .setQueueUrl(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_QUEUE_URL))
                    .build();
            }else {
            throw new RuntimeException("Invalid authType");
        }

        executor = ((SqsChannel) getChannel()).getJobPool();

        try {
            threads = Integer.parseInt(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_THREADS));
        } catch (Exception e) {
            logger.log(Level.ERROR,e,"Unable to parse Consumer Threads for destination");
            e.printStackTrace();
        }

        queueUrl = getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_QUEUE_URL);

        try {
            pollInterval = Integer.parseInt(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_POLL_INTERVAL));
        } catch (Exception e) {
            logger.log(Level.ERROR,e,"Unable to parse Poll Interval for destination");
            e.printStackTrace();
        }

        try {
            maxMessages = Integer.parseInt(getDestinationProperties().getProperty(SqsChannelProperties.CONFIG_MAX_MESSAGES));
        } catch (Exception e) {
            logger.log(Level.ERROR,e,"Unable to parse Max Messages for destination");
            e.printStackTrace();
        }


        logger.log(Level.DEBUG,"Initialisation of SQS Destination completed");

    }



    public void connect() throws Exception {

        sqsClient = Client.createClient(context);
        logger.log(Level.DEBUG,"Successfully connected to AWS SQS");

    }

    /**
     * Create listener for the specified EventProcessor don't start polling here.
     */
    @Override
    public void bind(EventProcessor eventProcessor) throws Exception {
        //Create consumer(s) for received EventProcessor, don't start polling yet

        logger.log(Level.DEBUG,"Binding Message Receivers to Listener threads");
        for (int i = 0; i < threads; i++) {
            SqsListener listener = new SqsListener(context, maxMessages, pollInterval, i, retryConnectionCount, retryConnectionSleep,eventProcessor, getSerializer(), getLogger());
            listeners.add(listener);
        }
        logger.log(Level.DEBUG,"Completed binding Message Receivers to Listener threads");
    }

    /**
     * Start receiving Sqs messages on this destination. A Sqs receiver job
     * is started. This job runs forever, polling the Sqs endpoint for
     * messages.
     */
    public void start() throws Exception {

        logger.log(Level.DEBUG,"Starting Listeners");
        for(final SqsListener listener : listeners) {
            executor.submit(listener);
        }
        logger.log(Level.DEBUG,"Listeners started");
    }

    public void close() throws Exception {

        logger.log(Level.DEBUG,"Closing SQS Client Connection");
        sqsClient.shutdown();
        //sqsClient.close();
        logger.log(Level.DEBUG,"SQS Client Connection closed");
    }

    @Override
    public void send(EventWithId event, Map map) throws Exception {

        //final Message message = (Message) getSerializer().serializeUserEvent(event,null);
        String payload = ((ExtendedDefaultEventImpl) event).getUnderlyingSimpleEvent().getPayloadAsString();

        logger.log(Level.DEBUG,"Payload %s", payload);
        logger.log(Level.DEBUG,"QueueUrl %s", queueUrl);


        try {
            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody("hello world")
                    .withDelaySeconds(5);
            sqsClient.sendMessage(send_msg_request);

            logger.log(Level.DEBUG, "Sent SQS msg.");

        } catch(Exception e) {
            logger.log(Level.ERROR, e, "Unable to send message to SQS");
            e.printStackTrace();
        }

    }

    public Event requestEvent(Event event, String s, BaseEventSerializer baseEventSerializer, long l, Map map) throws Exception {
        return null;
    }


    public AmazonSQS getSQSClient() {
    	return sqsClient;
    }

    public String getQueueUrl() { return queueUrl;}

}
