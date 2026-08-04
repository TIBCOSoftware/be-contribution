/*
 * Copyright (c) 2026. Cloud Software Group, Inc. All Rights Reserved. Confidential & Proprietary.
 */

package com.tibco.be.custom.aws.services.s3;

import static com.tibco.be.custom.aws.services.s3.Bucket.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
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
public class BucketUnitTest {

  private static final DockerImageName localStackImage =
      DockerImageName.parse("localstack/localstack:3");

  @Container
  private static final LocalStackContainer localStackContainer = new
      LocalStackContainer(localStackImage)
      .withServices(Service.S3);

  @BeforeEach
  void prerequisitesExist() {
    assertNotNull(localStackContainer);
    assertTrue(localStackContainer.isRunning());
  }


  private static AmazonS3 client;

  // Test for KMS & CSE
  static String kmsKeyID;



  @BeforeAll
  public static void setup() {

    try {

      kmsKeyID = UUID.randomUUID().toString();

      BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(
          localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

      EndpointConfiguration endpointConfiguration = new EndpointConfiguration(
          localStackContainer.getEndpointOverride(Service.S3).toString(),
          localStackContainer.getRegion());

      client = AmazonS3ClientBuilder
        .standard()
          .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
        .withEndpointConfiguration(endpointConfiguration)
        .withPathStyleAccessEnabled(true)
        .build();


    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

  }

  @BeforeEach
  public void before() {
  }

  @AfterEach
  public void after() {}

  @Test
  void testBucketExists() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    boolean exists =
        doesBucketExist(client, bucketName);
    assertTrue(exists);

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }


  @Test
  void testCreateBucket() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(client, bucketName);

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }


  @Test
  void testDeleteBucket() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(client, bucketName);

      assertFalse(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3Object() {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObject(
              client,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String content =
          getObject(
              client, bucketName, objectName);

      assertEquals(content, objectContent);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          client, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }


  @Test
  public void testDeleteS3Object() {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
          putObject(
              client,
              bucketName,
              objectName,
              objectContent);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          client, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
          getObject(
              client, bucketName, objectName);

      fail("Object should have been deleted");

    } catch (RuntimeException e) {
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDoesBucketExist()  {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(client, bucketName);

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDoesObjectExist() {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObject(
              client,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesObjectExist(
              client, bucketName, objectName);

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          client, bucketName, objectName );
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetS3Object() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
          putObject(
              client,
              bucketName,
              objectName,
              objectContent);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      String content =
          getObject(
              client, bucketName, objectName);

      assertEquals(content, objectContent);

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          client, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

//  @Test
//  public void testPutS3ObjectUsingCSEKMS() throws Exception {
//
//    String bucketName = generateAlphaNumericRandomString(60);
//    String objectName = generateAlphaNumericRandomString(60);
//    String objectContent = generateAlphaNumericRandomString(1024);
//
//    try {
//      createBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      String result =
//          putObjectWithCSE_KMS(
//              amazonS3EncryptionV2,
//              bucketName,
//              objectName,
//              objectContent);
//
//      Assert.assertNotNull(result);
//      Assert.assertFalse(result.isEmpty());
//
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    Thread.sleep(3000);
//
//    try {
//      boolean exists =
//          doesObjectExist(
//              amazonS3, bucketName, objectName);
//
//      Assert.assertTrue(exists);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteObject(amazonS3, bucketName, objectName );
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//  }

//  @Test
//  public void testPutS3ObjectUsingSSEKMS() throws Exception {
//
//    String bucketName = generateAlphaNumericRandomString(60);
//    String objectName = generateAlphaNumericRandomString(60);
//    String objectContent = generateAlphaNumericRandomString(1024);
//
//    try {
//      createBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      String result =
//          putObjectWithSSE_KMS(
//              amazonS3,
//              bucketName,
//              objectName,
//              kmsKeyID,
//              objectContent);
//
//      Assert.assertNotNull(result);
//      Assert.assertFalse(result.isEmpty());
//
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    Thread.sleep(3000);
//
//    try {
//      boolean exists =
//          doesObjectExist(
//              amazonS3, bucketName, objectName );
//
//      Assert.assertTrue(exists);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteObject(
//          amazonS3, bucketName, objectName );
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//  }

  @Test
  public void testPutS3ObjectUsingSSES3() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObjectWithSSE_S3(
              client,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExist(
              client, bucketName, objectName );

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          client, bucketName, objectName );
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGeneratePreSignedUrl()  {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObject(
              client,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          generatePreSignedUrl(
              client,
              bucketName,
              objectName, null, 6000);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          client, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(client, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
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
