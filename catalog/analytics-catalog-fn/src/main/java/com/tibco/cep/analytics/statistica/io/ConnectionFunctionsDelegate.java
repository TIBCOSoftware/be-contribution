package com.tibco.cep.analytics.statistica.io;

import com.tibco.cep.analytics.statistica.io.utils.StatisticaException;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

/**
 * @author swapna
 *
 * The class handling all host connection and authorization related functionality
 */
public class ConnectionFunctionsDelegate {
	
	private static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(ConnectionFunctionsDelegate.class);

	/**
	 * The function to save the host URL and return the connection object
	 * @param hostUrl - Statistica host URL
	 * @return Connection object with hostURL for Statistica
	 */
	public static Object createConnection(String hostUrl) {
		try {
			if (hostUrl == null || hostUrl.isEmpty()) {
				throw new StatisticaException("No Host Name Url provided for Statistica");
			}
			ConnectionInfo connection = new ConnectionInfo(hostUrl);
			LOGGER.log(Level.DEBUG, "Created a connection for Statistica with URL: "+ hostUrl);
			return connection;
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "Failed to create Connection object to connect to Statistica because: {}", e.getMessage());
			throw new RuntimeException(
					"Failed to create Connection object to connect to Statistica because: " + e.getMessage());
		}
	}

	/**
	 * The function to set Basic Auth information for Statistica
	 * @param connection Connection object with hostURL for Statistica
	 * @param userName - User name for Basic Auth in Statistica
	 * @param password - Password for Basic Auth in Statistica
	 * @return
	 */
	public static boolean setBasicAuth(ConnectionInfo connection, String userName, String password) {
		try {
			if (userName == null || password == null) {
				throw new StatisticaException("No user name or password specified for the user name for Basic Auth");
			}
			String decryptedPassword = decryptPwd(password);
			connection.setUserName(userName);
			connection.setPassword(decryptedPassword);
			LOGGER.log(Level.DEBUG, "Saved credentials to connect to Statistica");
			return true;
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "Error while setting credentials: {}", e.getMessage());
			throw new RuntimeException("Error while setting credentials: " + e.getMessage());
		}
	}
	
	private static String decryptPwd(String encryptedPwd) {
        try {
            String decryptedPwd = encryptedPwd;
            if (ObfuscationEngine.hasEncryptionPrefix(encryptedPwd)) {
                decryptedPwd = new String(ObfuscationEngine.decrypt(encryptedPwd));
            } else {
            	LOGGER.log(Level.WARN, "Considering the password specified as a clear text password to connect to Statistica");
            }
            return decryptedPwd;
        } catch (AXSecurityException e) {
            LOGGER.log(Level.WARN, e.getMessage());
            LOGGER.log(Level.WARN, "Considering the password specified as a clear text password to connect to Statistica");
            return encryptedPwd;
        } 
    }
	

}
