package com.tibco.cep.driver.kafka.security;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import org.apache.kafka.common.security.scram.ScramLoginModule;

import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

/**
 * Extension to Kafka's ScramLoginModule with added support for TIBCO encrypted passwords.
 * 
 * @author moshaikh
 */
public class BEScramLoginModule extends ScramLoginModule {
	private static final String USERNAME_CONFIG = "username";
	private static final String PASSWORD_CONFIG = "password";
	
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
    	Map<String, ?> overrideOptions = new HashMap<>(options);
        String username = (String) overrideOptions.get(USERNAME_CONFIG);
        if (username != null) {
        	subject.getPublicCredentials().add(username);
        	overrideOptions.remove(USERNAME_CONFIG);//Remove processed fields from options
        }
        String password = (String) overrideOptions.get(PASSWORD_CONFIG);
        try {
			if (ObfuscationEngine.hasEncryptionPrefix(password)) {
			    password = new String(ObfuscationEngine.decrypt(password));
			}
		} catch (AXSecurityException e) {
			e.printStackTrace();
		}
        
        if (password != null) {
        	subject.getPrivateCredentials().add(password);
        	overrideOptions.remove(PASSWORD_CONFIG);//Remove processed fields from options
        }
        
        super.initialize(subject, callbackHandler, sharedState, overrideOptions);//Process remaining fields
    }
}
