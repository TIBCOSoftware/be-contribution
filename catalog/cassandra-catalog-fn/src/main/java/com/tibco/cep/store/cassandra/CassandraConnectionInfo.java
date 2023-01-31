/**
 * 
 */
package com.tibco.cep.store.cassandra;

import java.util.Properties;

import com.tibco.cep.store.StoreConnectionInfo;

/**
 * @author rakulkar
 *
 */
public class CassandraConnectionInfo extends StoreConnectionInfo {
	
	private Properties connectionProperties;

	public CassandraConnectionInfo(String type, String url) {
		super(type, url);
		
		connectionProperties = new Properties();
	}
	
	public void setConnectionTimeout(double timeout) {
		connectionProperties.setProperty("DEFAULT_CONNECT_TIMEOUT_MILLIS", String.valueOf(timeout));
	}
	
	public void setUserCredentials(String userName, String password) {
		if (userName != null) {
			this.connectionProperties.put("user", userName);
        }
        if (password != null) {
        	this.connectionProperties.put("password", password);
        }
	}
	
	public void setKeySpace(String keyspaceName)
	{
		if (keyspaceName != null) {
			this.connectionProperties.put("keyspace", keyspaceName);
		}
	}
	
	public Properties getConnectionProperties() {
		return this.connectionProperties;
	}
	
	public String getPassword()
	{
		return this.connectionProperties.getProperty("password");
	}
	
	public String getUserName()
	{
		return this.connectionProperties.getProperty("user");
	}
	
	public void setTrustStoreProps(String trustStore, String trustStorePassword, String trustStoreType) {
		this.connectionProperties.setProperty("cas.net.ssl.trustStore", trustStore);
		if (trustStoreType != null) {
			this.connectionProperties.setProperty("cas.net.ssl.trustStoreType", trustStoreType);
		}
		if (trustStorePassword != null) {
			this.connectionProperties.setProperty("cas.net.ssl.trustStorePassword", trustStorePassword);
		}
	}
	
	public void setKeyStoreProps(String keyStore, String keyStoreType, String keyStorePassword) {
		this.connectionProperties.setProperty("cas.net.ssl.keyStore", keyStore);
		if (keyStoreType != null) {
			this.connectionProperties.setProperty("cas.net.ssl.keyStoreType", keyStoreType);
		}
		if (keyStorePassword != null) {
			this.connectionProperties.setProperty("cas.net.ssl.keyStorePassword", keyStorePassword);
		}
	}
	
	public String getTrustStore() {
		return this.connectionProperties.getProperty("cas.net.ssl.trustStore");
	}
	public String getTrustStoreType() {
		return this.connectionProperties.getProperty("cas.net.ssl.trustStoreType");
	}
	public String getTrustStorePassword() {
		return this.connectionProperties.getProperty("cas.net.ssl.trustStorePassword");
	}
	
	public String getKeyStore() {
		return this.connectionProperties.getProperty("cas.net.ssl.keyStore");
	}
	public String getKeyStoreType() {
		return this.connectionProperties.getProperty("cas.net.ssl.keyStoreType");
	}
	public String getKeyStorePassword() {
		return this.connectionProperties.getProperty("cas.net.ssl.keyStorePassword");
	}
}
