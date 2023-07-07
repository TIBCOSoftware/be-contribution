package com.tibco.cep.driver.mqtt;

import static com.tibco.cep.driver.mqtt.MqttProperties.MQTT_CHANNEL_PROPERTY_KEYSTORE_IDENTITY;
import static com.tibco.cep.driver.mqtt.MqttProperties.MQTT_CHANNEL_PROPERTY_REQUIRES_CLIENT_AUTH;
import static com.tibco.cep.driver.mqtt.MqttProperties.MQTT_CHANNEL_PROPERTY_SSL_NODE;
import static com.tibco.cep.driver.mqtt.MqttProperties.MQTT_CHANNEL_PROPERTY_TRUSTSTORE_FOLDER;
import static com.tibco.cep.driver.mqtt.MqttProperties.MQTT_CHANNEL_PROPERTY_TRUSTSTORE_PASSWORD;
import static com.tibco.cep.runtime.channel.ChannelProperties.AEMETA_SERVICES_2002_NS;
import static com.tibco.cep.runtime.channel.ChannelProperties.XML_NODE_CONFIG;

import java.security.KeyStore;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.cep.driver.http.server.utils.SSLUtils;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.repo.ArchiveResourceProvider;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.runtime.channel.Channel.State;
import com.tibco.cep.runtime.channel.ChannelConfig.ConfigurationMethod;
import com.tibco.cep.runtime.service.security.BEIdentity;
import com.tibco.cep.runtime.service.security.BEIdentityUtilities;
import com.tibco.cep.runtime.service.security.BEKeystoreIdentity;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;
import com.tibco.xml.data.primitive.ExpandedName;
import com.tibco.xml.datamodel.XiNode;
import com.tibco.xml.datamodel.helpers.XiChild;

/**
 * @author ssinghal
 *
 */
public class MqttChannel extends BaseChannel{
	
	String trustStore, trustStorePass, trustStoreType;
	String keyStore, keyStorePass, keyStoreType;
	private boolean useSsl;
	private Properties sslProperties;
	private String brokerUrl, user, password;
	
	public boolean isUseSsl() {
		return useSsl;
	}

	public Properties getSslProperties() {
		return sslProperties;
	}
	
	public String getBrokerUrl() {
		return brokerUrl;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void init() throws Exception {
		
		ConfigurationMethod method = config.getConfigurationMethod();
		if (method == ConfigurationMethod.PROPERTIES) {
			configureByProperties();
		} else if (method == ConfigurationMethod.REFERENCE) {
			configureByReference();
		}
		
		getLogger().log(Level.INFO, "Initializing Mqtt Channel with broker url: " + brokerUrl);
		super.init();
		setState(State.INITIALIZED);
	}
	
	@Override
	public void connect() throws Exception {
		
		//if cache server - do not start
		boolean cacheMode = RuleServiceProviderManager.getInstance().getDefaultProvider().isCacheServerMode();
		if(cacheMode){
			return;
		}
		
		State state = getState();
		if (state == State.UNINITIALIZED) {
		    init();
		}

		if ((state == State.STARTED) || (state == State.CONNECTED) || (state == State.STOPPED)) {
		    return;
		}
		
		if ((state == State.INITIALIZED) || (state == State.RECONNECTING)) {
		    synchronized(state) {
		    	state = getState(); //Check the state again, call the getState, as opposed to the state variable
		        if ((state == State.STARTED) || (state == State.CONNECTED) || (state == State.STOPPED)) {
		            return;
		        }else{
		        	super.connect();
		        }
		    }
		}

		setState(State.CONNECTED);
	}
	
	
	@Override
	public void start() throws Exception {
		
		 if ((getState() == State.INITIALIZED) || (getState() == State.UNINITIALIZED)) {
	            connect();
	        }

        if ((getState() == State.CONNECTED) || (getState() == State.STOPPED) ) {
        	super.start();
        }
        setState(State.STARTED);
		
	}
	
	@Override
	public void stop() {
		super.stop();
		setState(State.STOPPED);
	}
	
	private void configureByProperties() {
		
		brokerUrl = (String) getGlobalVariableValue(
				getChannelProperties().getProperty(MqttProperties.MQTT_CHANNEL_PROPERTY_BROKER_URLS));
		
		user = (String) getGlobalVariableValue(
				getChannelProperties().getProperty(MqttProperties.MQTT_CHANNEL_PROPERTY_USERNAME));
		password = decryptPwd((String) getGlobalVariableValue(
				getChannelProperties().getProperty(MqttProperties.MQTT_CHANNEL_PROPERTY_PASSWORD)));
		
	}
	
	private void configureByReference() throws Exception {
		
		String ref = config.getReferenceURI();
		XiNode rootNode = null;
		RuleServiceProvider rsp = (RuleServiceProvider)getRuleServiceProvider();
		GlobalVariables gv = rsp.getGlobalVariables();
		ArchiveResourceProvider provider = null;
		provider = rsp.getProject().getSharedArchiveResourceProvider();
		rootNode = provider.getResourceAsXiNode(ref);
		XiNode configNode = XiChild.getChild(rootNode, XML_NODE_CONFIG);
		
		brokerUrl = (String) getGlobalVariableValue(XiChild.getString(configNode, ExpandedName.makeName(MqttProperties.MQTT_CHANNEL_PROPERTY_BROKER_URLS)));
		user = (String) getGlobalVariableValue(XiChild.getString(configNode, ExpandedName.makeName(MqttProperties.MQTT_CHANNEL_PROPERTY_USERNAME)));
		password = decryptPwd(
				(String) getGlobalVariableValue(XiChild.getString(configNode, ExpandedName.makeName(MqttProperties.MQTT_CHANNEL_PROPERTY_PASSWORD))));
		
		Object usessl_ = getGlobalVariableValue(XiChild.getString(configNode, ExpandedName.makeName(MqttProperties.MQTT_CHANNEL_PROPERTY_USESSL)));
		if(usessl_ instanceof Boolean){
			useSsl = (Boolean) usessl_;
		}else{
			useSsl = Boolean.parseBoolean((String) usessl_);
		}
		
		if(useSsl){
			sslProperties = new Properties();
			XiNode sslNode = XiChild.getChild(configNode, ExpandedName.makeName(AEMETA_SERVICES_2002_NS, MQTT_CHANNEL_PROPERTY_SSL_NODE));
			
			String trustStoreFolder = (String)getGlobalVariableValue(XiChild.getString(sslNode, ExpandedName.makeName(AEMETA_SERVICES_2002_NS, MQTT_CHANNEL_PROPERTY_TRUSTSTORE_FOLDER)));
			String truststorePassword = decryptPwd( (String)getGlobalVariableValue(
					XiChild.getString(sslNode, ExpandedName.makeName(AEMETA_SERVICES_2002_NS, MQTT_CHANNEL_PROPERTY_TRUSTSTORE_PASSWORD))));
			KeyStore trustedKeysStore = SSLUtils.createKeystore(trustStoreFolder, null, provider, gv, true);
			String trustStore = SSLUtils.storeKeystore(trustedKeysStore, truststorePassword, formFileKeystoreName(rsp.getProject().getName(), rsp.getName(), getUri()));
			
			sslProperties.put(SSLSocketFactoryFactory.TRUSTSTORE, trustStore);
			sslProperties.put(SSLSocketFactoryFactory.TRUSTSTOREPWD, truststorePassword);
	        sslProperties.put(SSLSocketFactoryFactory.TRUSTSTORETYPE, "JKS");
	        
	        boolean requiresClientAuth = Boolean.parseBoolean((String)getGlobalVariableValue(
	        		XiChild.getString(sslNode, ExpandedName.makeName(AEMETA_SERVICES_2002_NS, MQTT_CHANNEL_PROPERTY_REQUIRES_CLIENT_AUTH))));
	        if(requiresClientAuth){
	        	parseIdentity(sslNode, provider, gv);
	        }
		}
		
		System.out.println();
		
	}
	
	private void parseIdentity(XiNode sslNode, ArchiveResourceProvider provider, GlobalVariables gv) throws Exception{
		String keyStore = null;
		final String idReference = (String) getGlobalVariableValue(XiChild.getString(sslNode, ExpandedName.makeName(AEMETA_SERVICES_2002_NS, MQTT_CHANNEL_PROPERTY_KEYSTORE_IDENTITY)));
    	if ((idReference != null) && !idReference.trim().isEmpty()) {
            if (!idReference.startsWith("/")) {
                throw new Exception("Invalid SSL ID reference: " + idReference);
            }
            BEIdentity identity = BEIdentityUtilities.fetchIdentity(provider, gv, idReference);
            if (identity instanceof BEKeystoreIdentity) {
            	BEKeystoreIdentity id = (BEKeystoreIdentity) identity;
            	keyStore = id.getStrKeystoreURL();
            	
            	sslProperties.put(SSLSocketFactoryFactory.KEYSTORE, keyStore);
    	        sslProperties.put(SSLSocketFactoryFactory.KEYSTOREPWD, id.getStrStorePassword());
    	        sslProperties.put(SSLSocketFactoryFactory.KEYSTORETYPE, id.getStrStoreType());
            }else{
            	throw new Exception("Invalid identity File configuration: ");
            }
        }
	}
	
	private static String formFileKeystoreName(String projectName, String engineName, String sharedResourceUri) {
    	if (sharedResourceUri.indexOf('.') > -1) {
    		sharedResourceUri = sharedResourceUri.substring(0, sharedResourceUri.indexOf('.'));//Remove the shared resource extension
    	}
    	String name = projectName + "_" + engineName + "_" + sharedResourceUri;
    	String prefix = "mqtt_ssl_";
    	String extension = ".ks";
    	return prefix + name.replaceAll("[/\\\\. ]", "_") + extension; //Replace all slashes, spaces, periods with underscore.
    }
	
	private static String decryptPwd(String encryptedPwd) {
		try {
			String decryptedPwd = encryptedPwd;
			if (ObfuscationEngine.hasEncryptionPrefix(encryptedPwd)) {
				decryptedPwd = new String(ObfuscationEngine.decrypt(encryptedPwd));
			}
			return decryptedPwd;
		} catch (AXSecurityException e) {
			return encryptedPwd;
		}
	}

}
