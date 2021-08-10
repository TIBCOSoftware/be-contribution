/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicContext;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicCredential;
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicCredentialsManager;
import com.tibco.be.custom.channel.aws.sqs.containercredentials.ContainerContext;
import com.tibco.be.custom.channel.aws.sqs.defaultcredentials.DefaultContext;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLContext;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLCredentialsManager;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import javax.net.ssl.SSLSocketFactory;


public class Client {

  private static AmazonSQS client = null;

  private static final Logger logger = LogManagerFactory.getLogManager().getLogger(Client.class);

  /**
   * Return an AmazonSQS client object using provided context.
   *
   * @param context - The connection context, supports either instance of BasicContext or SAMLContext
   *
   * @return AmazonSQS - An AmazonSQS client connection
   */
  public static AmazonSQS createClient(Object context) throws Exception {

    if (context instanceof BasicContext) {

      BasicContext basicContext = (BasicContext) context;

      // As we're dealing with short-lived tokens, we need to handle expiration and refresh logic here.
      // First call of createClient will always have client set to null, so will need to set it
      // Subsequent calls, client will be set but underlying short-lived token may have expired so need to handle it here too.
      if (BasicCredentialsManager.isExpired(basicContext) || client == null) {
        logger.log(Level.DEBUG, "Creating a new AmazonSQS client using Basic Context");

        // Get a valid AWS BasicCredentials from BasicCredentialManager based on the context
        // Token refresh will be handled by our CredentialManager, that way we know we always have a valid credential.
        BasicCredential credentials = BasicCredentialsManager.getBasicCredential(basicContext);

        // Create our AWS endpoint configuration
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
            new AwsClientBuilder.EndpointConfiguration(
                basicContext.getQueueUrl(), basicContext.getRegionName());

        // Create our AmazonSQS client using the correct credentials
        // Session Credentials = Assume Role,
        // Basic Credentials = API Key and Secret
        if (credentials.getBasicSessionCredentials() != null) {

          // Creating an AmazonSQS client is a time-expensive operation - we only want to do it when we have to!
          // Cache our client object so that we don't have to keep creating a new one every time.
          // We will only need to create a new client if our token is about to expire.

          client =
              AmazonSQSClientBuilder.standard()
                  .withCredentials(
                      new AWSStaticCredentialsProvider(credentials.getBasicSessionCredentials()))
                  .withEndpointConfiguration(endpointConfiguration)
                  .build();
        } else {

          // Creating an AmazonSQS client is a time-expensive operation - we only want to do it when we have to!
          // Cache our client object so that we don't have to keep creating a new one every time.
          // We will only need to create a new client if our token is about to expire.

          client =
              AmazonSQSClientBuilder.standard()
                  .withCredentials(
                      new AWSStaticCredentialsProvider(credentials.getBasicAWSCredentials()))
                  .withEndpointConfiguration(endpointConfiguration)
                  .build();
        }
        return client;
      } else {
        // We're here if we already have an non-expired set of credentials and therefore our existing client instance can be returned.
        logger.log(Level.DEBUG,"AmazonSQS client token is not stale, re-using client connection");
        return client;
      }

    } else if (context instanceof SAMLContext) {

      // We're using a SAML context for authentication

      SAMLContext samlContext = (SAMLContext) context;

      // We only want to create our AmazonSQS client using SAML credentials if our STS token has expired or if our client has not been created yet
      if (SAMLCredentialsManager.isExpired(samlContext) || client == null) {
        logger.log(Level.DEBUG, "Creating a new AmazonSQS client using SAML Context");

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        // Get a valid AWS Credentials from SAMLCredentialManager based on the context
        // Token refresh will be handled by our CredentialManager, that way we know we always have a valid credential.
        // Our credentials object will have a valid STS short-lived session token.
        Credentials credentials =
            SAMLCredentialsManager.getCredentials(
                samlContext, sslSocketFactory, clientConfiguration);

        // We need to use a BasicSessionCredential object using our Key+Secret+SessionToken
        BasicSessionCredentials sessionCredentials =
            new BasicSessionCredentials(
                credentials.getAccessKeyId(),
                credentials.getSecretAccessKey(),
                credentials.getSessionToken());

        // Creating an AmazonSQS client is a time-expensive operation - we only want to do it when we have to!
        // Cache our client object so that we don't have to keep creating a new one every time.
        // We will only need to create a new client if our STS short-lived session token is about to expire.

        client =
            AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .withRegion(samlContext.getRegionName())
                .build();
        return client;
      } else {
        // We're here if we already have an non-expired set of credentials and therefore our existing client instance can be returned.
        logger.log(Level.DEBUG,"AmazonSQS client token is not stale, re-using client connection");
        return client;
      }
    } else if (context instanceof DefaultContext) {

      if (client == null) {
        DefaultContext defaultContext = (DefaultContext) context;

        // Create our AWS endpoint configuration
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
            new AwsClientBuilder.EndpointConfiguration(
                defaultContext.getQueueUrl(), defaultContext.getRegionName());

        client = AmazonSQSClientBuilder.standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withEndpointConfiguration(endpointConfiguration)
            .build();

        return client;
      } else {
        // We're here if we already have a client instance that can be returned.
        logger.log(Level.DEBUG,"Re-using AmazonSQS client");
        return client;
      }
    } else if(context instanceof ContainerContext){
    	
    	ContainerContext containerContext = (ContainerContext) context; 
    	
    	// Create our AWS endpoint configuration
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
            new AwsClientBuilder.EndpointConfiguration(
            		containerContext.getQueueUrl(), containerContext.getRegionName());
        
    	client = AmazonSQSClientBuilder.standard()
                .withCredentials(new EC2ContainerCredentialsProviderWrapper())
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    	return client;
    }else {
      throw new RuntimeException("Unknown context");
    }


  }


}
