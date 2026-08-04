/*
 * Copyright (c) 2026. Cloud Software Group, Inc. All Rights Reserved. Confidential & Proprietary.
 */

package com.tibco.be.custom.aws.services.sqs;

import static com.tibco.be.custom.aws.services.sqs.Queue.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueueUnitTest {

    private static DockerImageName localStackImage =
        DockerImageName.parse("localstack/localstack:3");

    @Container
    private static LocalStackContainer localStackContainer = new
        LocalStackContainer(localStackImage)
        .withServices(Service.SQS)
        .withEnv("SQS_ENDPOINT_STRATEGY", "path");

    static {
        // AWS SDK v1 routes SQS operations to the host:port embedded in the queue
        // URL that LocalStack returns (it ignores the client's endpoint override).
        // LocalStack bakes its internal edge port (4566) into that URL, so pin the
        // host port to 4566 to keep the returned queue URLs reachable.
        localStackContainer.setPortBindings(java.util.Arrays.asList("4566:4566"));
    }

    @BeforeEach
    void prerequisitesExist() {
        assertNotNull(localStackContainer);
        assertTrue(localStackContainer.isRunning());
    }


    @BeforeAll
    public static void setup() {
        // The no-credential production helpers build their SQS client with a
        // DefaultAWSCredentialsProviderChain. Feed that chain the LocalStack
        // credentials via system properties (LocalStack accepts any values).
        System.setProperty("aws.accessKeyId", localStackContainer.getAccessKey());
        System.setProperty("aws.secretKey", localStackContainer.getSecretKey());
    }

    @AfterAll
    public static void teardown() {
        System.clearProperty("aws.accessKeyId");
        System.clearProperty("aws.secretKey");
    }


    @BeforeEach
    public void before() throws Exception {
    }

    @AfterEach
    public void after() throws Exception {
    }


    @Test
    @Order(1)
    public void testGetQueueAttributes() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(new EndpointConfiguration(
                localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS).toString(),
                localStackContainer.getRegion()))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        Map<String, String> attributes =
            (Map<String,String>) getQueueAttributes(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                localStackContainer.getRegion());

        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());

        int count = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }

    @Test
    @Order(2)
    public void testGetQueueAttribute() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(new EndpointConfiguration(
                localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS).toString(),
                localStackContainer.getRegion()))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        String attribute =
            getQueueAttribute(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                "ApproximateNumberOfMessages",
                localStackContainer.getRegion());

        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());

        int count = Integer.parseInt(attribute);

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }


    @Test
    @Order(3)
    public void testGetQueueAttributesWithAccessKeySecret() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(new EndpointConfiguration(
                localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS).toString(),
                localStackContainer.getRegion()))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        Map<String, String> attributes =
            (Map<String,String>) getQueueAttributesWithAccessKeySecret(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                localStackContainer.getRegion(),
                localStackContainer.getAccessKey(),
                localStackContainer.getSecretKey());

        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());

        int count = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }

    @Test
    @Order(4)
    public void testGetQueueAttributeWithAccessKeySecret() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(new EndpointConfiguration(
                localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS).toString(),
                localStackContainer.getRegion()))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        String attribute =
            getQueueAttributeWithAccessKeySecret(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                "ApproximateNumberOfMessages",
                localStackContainer.getRegion(),
                localStackContainer.getAccessKey(),
                localStackContainer.getSecretKey());

        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());

        int count = Integer.parseInt(attribute);

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }


    // function to generate a random string of length n
    private static String generateAlphaNumericRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "0123456789abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

} 
