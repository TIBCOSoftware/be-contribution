/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.basiccredentials;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.securitytoken.model.Credentials;

public class BasicCredential {

    private AWSCredentialsProvider provider = null;
    private BasicSessionCredentials basicSessionCredentials = null;
    private Credentials credentials = null;

    private long lastLoginTime = 0;

    public AWSCredentialsProvider getCredentialsProvider() {
        return provider;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public BasicSessionCredentials getBasicSessionCredentials() {
        return this.basicSessionCredentials;
    }

    public void setBasicSessionCredentials(BasicSessionCredentials basicSessionCredentials) {
        this.basicSessionCredentials = basicSessionCredentials;
    }

    public void setCredentialsProvider(AWSCredentialsProvider provider) {
        this.provider = provider;
    }
    public long getLastLoginTime() {
        return lastLoginTime;
    }
    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }



}
