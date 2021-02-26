/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.tibco.be.custom.channel.*;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicContext;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicCredential;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicCredentialsManager;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLContext;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLCredentialsManager;
import com.tibco.cep.kernel.service.logging.Level;

import javax.net.ssl.SSLSocketFactory;
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

    private String authType = "";

//    private String region = "";
//    private String accessKey = "";
//    private String secretKey = "";
//    private String roleArn = "";

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

    private BasicContext basicContext = null;
    private SAMLContext samlContext = null;

    public void init() throws Exception {


        logger.log(Level.DEBUG,"Initialising SQS Destination");

        authType = getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_AUTH_TYPE);

        if (authType.equals("CREDENTIALS")) {
            basicContext = new BasicContext();
            basicContext.setAccessKey(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_ACCESS_KEY));
            basicContext.setSecretKey(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_SECRET_KEY));
            basicContext.setRegionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_REGION));
            basicContext.setRoleArn(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_ROLE_ARN));
            basicContext.setSessionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_SESSION_NAME));
            basicContext.setTokenExpirationDuration(Integer.parseInt(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_CREDENTIALS_EXPIRATION)) * 60);
        } else {
            samlContext = new SAMLContext();
            samlContext.setIdpUsername(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_USERNAME));
            samlContext.setIdpPassword(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PASSWORD));
            samlContext.setRegionName(getChannel().getChannelProperties().getProperty(CONFIG_AWS_REGION));
            samlContext.setIdProviderType(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PROVIDER_TYPE));
            samlContext.setIdpUseProxy(false);
            samlContext.setIdpEntryUrl(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_IDP_PROVIDER_URL));
            samlContext.setAwsRole(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_ROLE));
            samlContext.setTokenExpirationDuration(Integer.parseInt(getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SAML_TOKEN_EXPIRY_DURATION)));
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

        if (authType.equals("CREDENTIALS")) {
            logger.log(Level.DEBUG, "Connecting to AWS SQS using Basic Credentials");
            BasicCredential credentials = BasicCredentialsManager.getBasicCredential(basicContext);

            AwsClientBuilder.EndpointConfiguration endpointConfiguration
                    = new AwsClientBuilder.EndpointConfiguration(queueUrl, basicContext.getRegionName());

            sqsClient = AmazonSQSClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials.getBasicSessionCredentials()))
                    .withEndpointConfiguration(endpointConfiguration)
                    .build();

        } else {
            logger.log(Level.DEBUG, "Connecting to AWS SQS using SAML Authentication");
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            ClientConfiguration clientConfiguration = new ClientConfiguration();

            Credentials credentials = SAMLCredentialsManager.getCredentials(samlContext, sslSocketFactory, clientConfiguration);

            BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                    credentials.getAccessKeyId(),
                    credentials.getSecretAccessKey(),
                    credentials.getSessionToken());

            sqsClient = AmazonSQSClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                    .withRegion(samlContext.getRegionName())
                    .build();

        }


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
            SqsListener listener = new SqsListener(sqsClient, queueUrl, maxMessages, pollInterval, i, eventProcessor, getSerializer(), getLogger());
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
