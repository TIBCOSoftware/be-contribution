package com.tibco.cep.driver.kafka.security;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.apache.kafka.common.security.plain.PlainLoginModule;

import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

/**
 * Extension to Kafka's PlainLoginModule with added support for TIBCO encrypted passwords.
 * 
 * @author moshaikh
 */
public class BEPlainLoginModule extends PlainLoginModule {

	private static final String USERNAME_CONFIG = "username";
	private static final String PASSWORD_CONFIG = "password";
	
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        String username = (String) options.get(USERNAME_CONFIG);
        if (username != null) {
        	subject.getPublicCredentials().add(username);
        }
        String password = (String) options.get(PASSWORD_CONFIG);
        try {
			if (ObfuscationEngine.hasEncryptionPrefix(password)) {
			    password = new String(ObfuscationEngine.decrypt(password));
			}
		} catch (AXSecurityException e) {
			e.printStackTrace();
		}
        
        if (password != null) {
        	subject.getPrivateCredentials().add(password);
        }
    }
}
