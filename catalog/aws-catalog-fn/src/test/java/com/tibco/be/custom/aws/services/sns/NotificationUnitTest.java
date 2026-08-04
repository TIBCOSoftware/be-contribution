/*
 * Copyright (c) 2026. Cloud Software Group, Inc. All Rights Reserved. Confidential & Proprietary.
 */

package com.tibco.be.custom.aws.services.sns;

import static com.tibco.be.custom.aws.services.sns.Notification.*;
import static com.tibco.be.custom.aws.services.sns.Notification.publish;

import static com.tibco.be.custom.aws.services.sns.Notification.publishWithAttributes;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
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
class NotificationUnitTest {


  private static AmazonSNS client;

  private static String topicARN = null;


  // Test data for SMS
    final static String smsSenderID = "Test";
    final static String smsOriginationNumber = null;
    final static String smsType = "Promotional";
    final static String smsMaxPrice = "0.50";

  final private static DockerImageName localStackImage =
      DockerImageName.parse("localstack/localstack:3");

  @Container
  final private static LocalStackContainer localStackContainer = new
      LocalStackContainer(localStackImage)
      .withServices(Service.SNS);

  @BeforeEach
  void prerequisitesExist() {
    assertNotNull(localStackContainer);
    assertTrue(localStackContainer.isRunning());
  }

  @BeforeAll
  public static void setup() {

    try {
    client = AmazonSNSClientBuilder
        .standard()
        .withEndpointConfiguration(new EndpointConfiguration(
            localStackContainer.getEndpointOverride(Service.SNS).toString(),
            localStackContainer.getRegion()))
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
            localStackContainer.getAccessKey(), localStackContainer.getSecretKey())))
        .build();

    topicARN = client.createTopic("MyTopic").getTopicArn();
    } catch(Exception e) {
      fail(e.getMessage());
    }

  }

    @Test
    void testPublishWithAttributes()  {

      try {
        String result =  publishWithAttributes(client,  topicARN, "message", createSMSAttributes(smsSenderID, smsOriginationNumber, smsMaxPrice, smsType));
        assertNotNull(result);
        assertNotEquals(result.length(), 0);
      } catch(Exception e) {
        fail(e.getMessage());
      }


    }



  @Test
  void testPublish() {

    try {
      String result =  publish(client ,topicARN, "message",  "subject");
      assertNotNull(result);
      assertNotEquals(result.length(), 0);
    } catch(Exception e) {
      fail(e.getMessage());
    }
  }


}