package com.tibco.be.custom.channel.kinesis;

import java.util.Properties;
import java.util.Map.Entry;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceAsyncClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.cep.kernel.service.logging.Level;

public class KinesisChannel extends BaseChannel {
	
	private AWSCredentialsProvider credentialsProvider;
	
	@Override
	public void init() throws Exception {

		getLogger().log(Level.INFO, "Initializing Kinesis Channel");
		super.init();
		Properties props = new Properties();
		for (Entry<Object, Object> entry : getChannelProperties().entrySet()) {
			if (entry.getValue() != null) {
				props.put(entry.getKey(), getGlobalVariableValue((String) entry.getValue()).toString());
			}
		}
				
		String access_key = props.getProperty("access_key");
		String secret_key = props.getProperty("secret_key");
		String profile_name = props.getProperty("profile_name");
		String session_token = props.getProperty("session_token");
		String role_arn = props.getProperty("role_arn");
		String region = props.getProperty("region.name");
		String role_session_name = props.getProperty("role_session_name");

		if (profile_name != null && !profile_name.isBlank()) {
			credentialsProvider = new AWSCredentialsProviderChain(new ProfileCredentialsProvider(profile_name));
		} else if (role_arn != null && !role_arn.isBlank()) {

			AWSSecurityTokenService stsClient = AWSSecurityTokenServiceAsyncClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(
							new BasicSessionCredentials(access_key, secret_key, session_token)))
					.withRegion(region).build();
			AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest().withRoleArn(role_arn).withRoleSessionName(role_session_name);
			AssumeRoleResult assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);
			Credentials creds = assumeRoleResult.getCredentials();
			credentialsProvider = new AWSStaticCredentialsProvider(
	                new BasicSessionCredentials(creds.getAccessKeyId(),
	                        creds.getSecretAccessKey(),
	                        creds.getSessionToken())
	        );			

		} else {
			credentialsProvider = new AWSStaticCredentialsProvider(
					new BasicSessionCredentials(access_key, secret_key, session_token));

		}
	}

	public AWSCredentialsProvider getCredentialsProvider() {
		return credentialsProvider;
	}
}

