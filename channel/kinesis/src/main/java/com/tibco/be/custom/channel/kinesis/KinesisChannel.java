package com.tibco.be.custom.channel.kinesis;

import java.util.Properties;
import java.util.Map.Entry;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
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

		if (profile_name != null && !profile_name.isEmpty()) {
			credentialsProvider = new AWSCredentialsProviderChain(new ProfileCredentialsProvider(profile_name));
		} else {
			credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key, secret_key));
		}
	}

	public AWSCredentialsProvider getCredentialsProvider() {
		return credentialsProvider;
	}
}

