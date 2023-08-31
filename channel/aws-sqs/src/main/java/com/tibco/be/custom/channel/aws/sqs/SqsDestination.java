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
import com.tibco.be.custom.channel.aws.sqs.defaultcredentials.DefaultContext;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLContext;
import com.tibco.cep.kernel.service.logging.Level;


import java.util.ArrayList;
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

    // CONSTANTS
    public static final String CONFIG_AWS_REGION = "aws.region";
    public static final String CONFIG_AWS_SQS_AUTH_TYPE = "aws.sqs.auth.type";

    public static final String CONFIG_AWS_SQS_CREDENTIALS_ACCESS_KEY = "aws.sqs.credentials.access.key";
    public static final String CONFIG_AWS_SQS_CREDENTIALS_SECRET_KEY = "aws.sqs.credentials.secret.key";
    public static final String CONFIG_AWS_SQS_CREDENTIALS_ROLE_ARN = "aws.sqs.credentials.role.arn";
    public static final String CONFIG_AWS_SQS_CREDENTIALS_SESSION_NAME = "aws.sqs.credentials.role.session.name";
    public static final String CONFIG_AWS_SQS_CREDENTIALS_EXPIRATION = "aws.sqs.credentials.expiration";

    public static final String CONFIG_AWS_SQS_SAML_IDP_PROVIDER_TYPE = "aws.sqs.saml.idp.provider.type";
    public static final String CONFIG_AWS_SQS_SAML_IDP_PROVIDER_URL = "aws.sqs.saml.idp.provider.url";
    public static final String CONFIG_AWS_SQS_SAML_IDP_USERNAME = "aws.sqs.saml.idp.username";
    public static final String CONFIG_AWS_SQS_SAML_IDP_PASSWORD = "aws.sqs.saml.idp.password";
    public static final String CONFIG_AWS_SQS_SAML_ROLE = "aws.sqs.saml.role";
    public static final String CONFIG_AWS_SQS_SAML_TOKEN_EXPIRY_DURATION = "aws.sqs.saml.token.expiry.duration";

    public static final String CONFIG_QUEUE_URL = "queue.url";
    public static final String CONFIG_POLL_INTERVAL = "poll.interval";
    public static final String CONFIG_THREADS = "consumer.threads";
    public static final String CONFIG_MAX_MESSAGES = "max.messages";

    public static final String CONFIG_CONNECTION_RETRY_COUNT = "connection.retry.count";
    public static final String CONFIG_CONNECTION_RETRY_TIME_TO_WAIT_MS = "connection.retry.sleep.duration";

    private static final String DEFAULT_RETRIES = "3";
    private static final String DEFAULT_TIME_TO_WAIT_MS = "2000";

    private Object context = null;

    public void init() throws Exception {


        logger.log(Level.DEBUG,"Initialising SQS Destination");

        authType = getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_AUTH_TYPE);

        retryConnectionCount = Integer.parseInt(getChannel().getChannelProperties().getProperty(CONFIG_CONNECTION_RETRY_COUNT,DEFAULT_RETRIES));
        retryConnectionSleep = Long.parseLong(getChannel().getChannelProperties().getProperty(CONFIG_CONNECTION_RETRY_TIME_TO_WAIT_MS, DEFAULT_TIME_TO_WAIT_MS));


        if (authType.equals("CREDENTIALS")) {
            context = new BasicContext.BasicContextBuilder()
                .setAccessKey(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_ACCESS_KEY))
                .setSecretKey(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_SECRET_KEY))
                .setRegionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_REGION))
                .setRoleArn(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_ROLE_ARN))
                .setSessionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_SESSION_NAME))
                .setTokenExpirationDuration(Integer.parseInt(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_EXPIRATION)) * 60)
                .setQueueUrl(getDestinationProperties().getProperty(CONFIG_QUEUE_URL))
                .build();
        } else if(authType.equals("SAML")){
            context = new SAMLContext.SAMLContextBuilder()
                .setIdpUsername(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_USERNAME))
                .setIdpPassword(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PASSWORD))
                .setRegionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_REGION))
                .setIdProviderType(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PROVIDER_TYPE))
                .setIdProviderType(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PROVIDER_TYPE))
                .setIdpUseProxy(false)
                .setIdpEntryUrl(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PROVIDER_URL))
                .setAwsRole(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_ROLE))
                .setTokenExpirationDuration(Integer.parseInt(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_TOKEN_EXPIRY_DURATION)))
                .setQueueUrl(getDestinationProperties().getProperty(CONFIG_QUEUE_URL))
                .build();
        } else if(authType.equals("DEFAULT")) {
            context = new DefaultContext.DefaultContextBuilder()
                .setRegionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_REGION))
                .setQueueUrl(getDestinationProperties().getProperty(CONFIG_QUEUE_URL))
                .build();
        } else {
            throw new RuntimeException("Invalid authType");
        }

        executor = ((SqsChannel) getChannel()).getJobPool();

        try {
            threads = Integer.parseInt(getDestinationProperties().getProperty(CONFIG_THREADS));
        } catch (Exception e) {
            logger.log(Level.ERROR,e,"Unable to parse Consumer Threads for destination");
            e.printStackTrace();
        }

        queueUrl = getDestinationProperties().getProperty(CONFIG_QUEUE_URL);

        try {
            pollInterval = Integer.parseInt(getDestinationProperties().getProperty(CONFIG_POLL_INTERVAL));
        } catch (Exception e) {
            logger.log(Level.ERROR,e,"Unable to parse Poll Interval for destination");
            e.printStackTrace();
        }

        try {
            maxMessages = Integer.parseInt(getDestinationProperties().getProperty(CONFIG_MAX_MESSAGES));
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
