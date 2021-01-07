package com.tibco.be.custom.channel.aws.sqs;

import com.tibco.be.custom.channel.*;
import com.tibco.cep.kernel.service.logging.Level;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class SqsDestination extends BaseDestination {

    private List<SqsListener> listeners = new ArrayList<SqsListener>();


    private SqsClient sqsClient;

    private String queueUrl = "";
    private int threads = 0;
    private int pollInterval;
    private int maxMessages = 1;

    private String region = "";
    private String accessKey = "";
    private String secretKey = "";
    private String roleArn = "";

    // keep a reference to the channel's executor service
    private ExecutorService executor;

    // CONSTANTS
    public static final String CONFIG_AWS_REGION = "aws.region";
    public static final String CONFIG_AWS_SQS_ACCESS_KEY = "aws.sqs.access.key";
    public static final String CONFIG_AWS_SQS_SECRET_KEY = "aws.sqs.secret.key";
    public static final String CONFIG_QUEUE_URL = "queue.url";
    public static final String CONFIG_POLL_INTERVAL = "poll.interval";
    public static final String CONFIG_THREADS = "consumer.threads";
    public static final String CONFIG_MAX_MESSAGES = "max.messages";
    public static final String CONFIG_AWS_ROLE_ARN = "aws.sqs.role.arn";

    public void init() throws Exception {


        logger.log(Level.DEBUG,"Initialising SQS Destination");

        region = getChannel().getChannelProperties().getProperty(CONFIG_AWS_REGION);
        accessKey = getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_ACCESS_KEY);
        secretKey = getChannel().getChannelProperties().getProperty(CONFIG_AWS_SQS_SECRET_KEY);
        roleArn = getChannel().getChannelProperties().getProperty(CONFIG_AWS_ROLE_ARN);

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

    private AwsCredentialsProvider createCredsProviderWithRole() throws ExecutionException, InterruptedException {


        logger.log(Level.DEBUG,"Establishing AWS Credentials");

        AwsCredentialsProvider credsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey,secretKey));


        StsClient stsClient = StsClient.builder().credentialsProvider(credsProvider).build();

        AssumeRoleRequest request = AssumeRoleRequest.builder()
                .durationSeconds(3600)
                .roleArn(roleArn)
                .roleSessionName("be")
                .build();


        StsAssumeRoleCredentialsProvider response = StsAssumeRoleCredentialsProvider
                .builder()
                .stsClient(stsClient)
                .refreshRequest(request)
                .build();

        AwsSessionCredentials creds = (AwsSessionCredentials) response.resolveCredentials();


        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(creds.accessKeyId(), creds.secretAccessKey(), creds.sessionToken());

        logger.log(Level.DEBUG,"Credentials established");

        return AwsCredentialsProviderChain.builder()
                .credentialsProviders(StaticCredentialsProvider.create(sessionCredentials))
                .build();

    }

    private AwsCredentialsProvider createCredsProvider() throws ExecutionException, InterruptedException {

        logger.log(Level.DEBUG,"Establishing AWS Credentials");

        AwsCredentialsProvider credsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey,secretKey));

        logger.log(Level.DEBUG,"Credentials established");

        return credsProvider;

    }

    public void connect() throws Exception {


        logger.log(Level.DEBUG,"Connecting to AWS SQS");

        AwsCredentialsProvider awsCredentialsProvider = null;
        if (roleArn.length()>0) {
            awsCredentialsProvider = createCredsProviderWithRole();
        } else {
            awsCredentialsProvider = createCredsProvider();
        }

        sqsClient = SqsClient.builder().credentialsProvider(awsCredentialsProvider)
                .region(Region.of(region))
                .build();

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
        sqsClient.close();
        logger.log(Level.DEBUG,"SQS Client Connection closed");
    }

    @Override
    public void send(EventWithId event, Map map) throws Exception {

        //final Message message = (Message) getSerializer().serializeUserEvent(event,null);
        String payload = ((ExtendedDefaultEventImpl) event).getUnderlyingSimpleEvent().getPayloadAsString();

        logger.log(Level.DEBUG,"Payload %s", payload);
        logger.log(Level.DEBUG,"QueueUrl %s", queueUrl);


        try {
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(payload)
                    .build());

            logger.log(Level.DEBUG, "Sent SQS msg.");

        } catch(Exception e) {
            logger.log(Level.ERROR, e, "Unable to send message to SQS");
            e.printStackTrace();
        }

    }

    public Event requestEvent(Event event, String s, BaseEventSerializer baseEventSerializer, long l, Map map) throws Exception {
        return null;
    }



}
