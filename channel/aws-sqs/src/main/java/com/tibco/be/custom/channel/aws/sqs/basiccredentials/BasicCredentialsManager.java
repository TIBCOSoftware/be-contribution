/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.basiccredentials;

import com.amazonaws.auth.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;

import java.util.HashMap;
import java.util.Map;

public class BasicCredentialsManager {


    private static Map<BasicContext, BasicCredential> credentialsMap = new HashMap<>();

    public static BasicCredential getBasicCredential(BasicContext context) throws Exception{
        //if(credentials == null || isExpired())
        if(!credentialsMap.containsKey(context) || isExpired(context))
        {
            BasicCredential basicCredential = new BasicCredential();
            basicCredential.setCredentialsProvider(getCredentialProvider(context.getAccessKey(), context.getSecretKey()));
            if (context.getRoleArn()!=null) {
                Credentials creds = assumeRole(context.getAccessKey(), context.getSecretKey(), context.getRoleArn(), context.getSessionName(), context.getRegionName(), context.getTokenExpirationDuration());
                basicCredential.setCredentials(creds);

                BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                        creds.getAccessKeyId(),
                        creds.getSecretAccessKey(),
                        creds.getSessionToken());

                basicCredential.setBasicSessionCredentials(basicSessionCredentials);
            }

            basicCredential.setLastLoginTime(System.currentTimeMillis());
            credentialsMap.put(context, basicCredential);
            return basicCredential;
        }

        //logger.log(Level.DEBUG,"Found un-expired AWS credentials in cache..");

        return credentialsMap.get(context);
    }


    public static boolean isExpired(BasicContext context) {

        int tokenExpirationDuration = context.getTokenExpirationDuration();

        //Renew token mins before expiry
        int minBeforeExpiry = 2;
        if(System.getProperty("com.tibco.be.awssqs.minbeforeexpiry") != null && System.getProperty("com.tibco.be.awssqs.minbeforeexpiry").length()>0)
            minBeforeExpiry = Integer.parseInt(System.getProperty("com.tibco.bw.awssqs.minbeforeexpiry"));
        //Use default value 60 if 0
        tokenExpirationDuration = (tokenExpirationDuration > 0 ? tokenExpirationDuration : 60);

        if((tokenExpirationDuration - minBeforeExpiry) > 0)
            tokenExpirationDuration = tokenExpirationDuration - minBeforeExpiry;

        if(System.currentTimeMillis() - credentialsMap.get(context).getLastLoginTime() >= tokenExpirationDuration * 60 * 1000)
            return true;
        else
            return false;

    }

    private static Credentials assumeRole(String accessKey, String secretKey, String roleArn, String region, String sessionName, int duration) {

        // Creating the STS client is part of your trusted code. It has
        // the security credentials you use to obtain temporary security credentials.
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(getCredentialProvider(accessKey,secretKey))
                .withRegion(region)
                .build();

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AssumeRoleRequest roleRequest = new AssumeRoleRequest()
                .withRoleArn(roleArn)
                .withExternalId("externalId")
                .withDurationSeconds(duration)
                .withRoleSessionName(sessionName);


        AssumeRoleResult roleResponse = stsClient.assumeRole(roleRequest);

        return roleResponse.getCredentials();

    }



    private static AWSCredentialsProvider getCredentialProvider(String accessKey, String secretKey) {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        return credentialsProvider;
    }



}
