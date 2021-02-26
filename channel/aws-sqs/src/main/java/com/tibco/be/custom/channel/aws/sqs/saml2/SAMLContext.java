/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import com.amazonaws.services.securitytoken.model.Credentials;

public class SAMLContext {

    private String idProviderType;
    private String idpEntryUrl;
    private String idpUsername;
    private String idpPassword;
    private String awsRole;
    private int tokenExpirationDuration;
    private Credentials credentials;
    private boolean idpUseProxy;
    private String proxyUserName;
    private String proxyPassword;
    private String proxyHost;
    private int proxyPort;
    private String regionName;

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setIdProviderType(String idProviderType) {
        this.idProviderType = idProviderType;
    }

    public void setIdpEntryUrl(String idpEntryUrl) {
        this.idpEntryUrl = idpEntryUrl;
    }

    public void setIdpUsername(String idpUsername) {
        this.idpUsername = idpUsername;
    }

    public void setIdpPassword(String idpPassword) {
        this.idpPassword = idpPassword;
    }

    public void setAwsRole(String awsRole) {
        this.awsRole = awsRole;
    }

    public void setTokenExpirationDuration(int tokenExpirationDuration) {
        this.tokenExpirationDuration = tokenExpirationDuration;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getIdProviderType() {
        return idProviderType;
    }

    public String getIdpEntryUrl() {
        return idpEntryUrl;
    }

    public String getIdpUsername() {
        return idpUsername;
    }

    public String getIdpPassword() {
        return idpPassword;
    }

    public String getAwsRole() {
        return awsRole;
    }

    public int getTokenExpirationDuration() {
        return tokenExpirationDuration;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public boolean isIdpUseProxy() {
        return idpUseProxy;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setIdpUseProxy(boolean idpUseProxy) {
        this.idpUseProxy = idpUseProxy;
    }

    public boolean getIdpUseProxy() {
        return idpUseProxy;
    }









}
