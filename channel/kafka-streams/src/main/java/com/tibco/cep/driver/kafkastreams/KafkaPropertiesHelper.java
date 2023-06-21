package com.tibco.cep.driver.kafka;

import java.security.KeyStore;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;

import com.tibco.cep.driver.http.server.utils.SSLUtils;
import com.tibco.cep.repo.ArchiveResourceProvider;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.repo.provider.SharedArchiveResourceProvider;
import com.tibco.cep.runtime.service.security.BEIdentity;
import com.tibco.cep.runtime.service.security.BEIdentityUtilities;
import com.tibco.cep.runtime.service.security.BEKeystoreIdentity;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

public class KafkaPropertiesHelper {
	
	private static String TRUST_STORE_PATH="ssl.trustStore";
	private static String TRUST_STORE_PASSWORD="ssl.truststorePwd";
	private static String TRUST_STORE_TYPE="ssl.trustStoreType";
	private static String KEY_STORE_PATH="ssl.keystorePath";
	private static String KEY_STORE_PWD="ssl.keystorePwd";
	private static String KEY_STORE_TYPE="ssl.keyStoreType";
	

	public static void initSslProperties(Properties channelProps, Properties clientProperties) {

		putValueIfNotEmpty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
				channelProps.getProperty("javax.net.ssl.trustStore"), clientProperties);
		putValueIfNotEmpty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
				channelProps.getProperty("javax.net.ssl.trustStorePassword"), clientProperties);
		putValueIfNotEmpty(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG,
				channelProps.getProperty("javax.net.ssl.trustStoreType"), clientProperties);
		putValueIfNotEmpty(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, channelProps.getProperty("javax.net.ssl.keyStore"),
				clientProperties);
		putValueIfNotEmpty(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,
				channelProps.getProperty("javax.net.ssl.keyStorePassword"), clientProperties);
		putValueIfNotEmpty(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, channelProps.getProperty("javax.net.ssl.keyStoreType"),
				clientProperties);
		putValueIfNotEmpty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
				channelProps.getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL), clientProperties);
		putValueIfNotEmpty(SaslConfigs.SASL_MECHANISM,
				channelProps.getProperty(KafkaProperties.KEY_CHANNEL_SASL_MECHANISM), clientProperties);

		if ("SASL_PLAINTEXT".equals(channelProps.getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL))
				|| "SASL_SSL".equals(channelProps.getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL))) {
			putValueIfNotEmpty(SaslConfigs.SASL_MECHANISM,
					channelProps.getProperty(KafkaProperties.KEY_CHANNEL_SASL_MECHANISM), clientProperties);
		}
	}

	/**
	 * Puts the key and value in passed Properties instance only if the value is not
	 * empty (not null and not blank string in case of string)
	 * 
	 * @param key
	 * @param value
	 * @param props
	 */
	public static void putValueIfNotEmpty(String key, Object value, Properties props) {
		if (value != null
				&& ((value instanceof String && !((String) value).trim().isEmpty()) || !(value instanceof String))) {
			props.put(key, value);
		}
	}

	/**
	 * Loads the overridden kafka client properties (if any).
	 * 
	 * @param beProperties
	 * @param destinationProps
	 * @param channelUri
	 * @param destUri
	 */
	public static void loadOverridenProperties(String basePrefix, Properties beProperties, Properties destinationProps,
			String channelUri, String destUri) {
		if (beProperties == null) {
			return;
		}
		// Read properties one level of override at a time, so that proper priorities
		// are maintained.
		String prefix = basePrefix + ".";
		for (Entry<Object, Object> prop : beProperties.entrySet()) {// Load all global kafka properties
			if (((String) prop.getKey()).startsWith(prefix)) {
				destinationProps.put(((String) prop.getKey()).substring(prefix.length()), prop.getValue());
			}
		}
		if (channelUri != null) {
			prefix = basePrefix + channelUri + ".";
			for (Entry<Object, Object> prop : beProperties.entrySet()) {// Load all channel level kafka properties
				if (((String) prop.getKey()).startsWith(prefix)) {
					destinationProps.put(((String) prop.getKey()).substring(prefix.length()), prop.getValue());
				}
			}
		}
		if (destUri != null) {
			prefix = basePrefix + destUri + ".";
			for (Entry<Object, Object> prop : beProperties.entrySet()) {// Load all destination level kafka properties
				if (((String) prop.getKey()).startsWith(prefix)) {
					destinationProps.put(((String) prop.getKey()).substring(prefix.length()), prop.getValue());
				}
			}
		}
	}
	
	public static void initSchemaReistryAutheticationProperties(Properties destinationProps , Properties clientProperties) throws Exception {
	   
	   KafkaPropertiesHelper.putValueIfNotEmpty(KafkaProperties.SCHEMA_REGISTRY_URL,
				destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_URL), clientProperties);
				
		String authType = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_AUTH_TYPE);
		String schemaRegistryPlatform = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_PLATFORM);
		
		if(authType.equals(KafkaProperties.BASIC_AUTH)) {
			
			String userName  = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_USERNAME);
			String password = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_PASSWORD);
			String authString = userName + ":" + password;
			
			KafkaPropertiesHelper.putValueIfNotEmpty(KafkaProperties.SCHEMA_REGISTRY_BASIC_AUTH_CREDENTIAL_SOURCE,
					"USER_INFO", clientProperties);
			KafkaPropertiesHelper.putValueIfNotEmpty(KafkaProperties.SCHEMA_REGISTRY_BASIC_AUTH_USER_INFO,
				authString, clientProperties);
			
		}else if(authType.equals(KafkaProperties.SSL_ONE_WAY) || authType.equals(KafkaProperties.MUTUAL_AUTH)){
			
			String  trustCertPath=destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_TRUSTSTORE_LOCATION);
			String trustStorePwd=destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_TRUSTSTORE_PWD);
			String identityPath=destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_KEYSTORE_IDENTITY);
			String userName  = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_USERNAME);
			String password = destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_PASSWORD);
			RuleServiceProvider rsp= RuleServiceProviderManager.getInstance().getDefaultProvider();
			String sharedResourceUri=null;
			
			Map<Object,Object> sslproperties = new HashMap<Object,Object>();
			
			
			
			if (schemaRegistryPlatform.equals(KafkaProperties.CONFLUENT_PLATFORM)) { 
				getSSLConnectionInfo(trustCertPath,trustStorePwd,rsp,"schema_registry",identityPath,sslproperties);
				
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_KEYSTORE_LOCATION,sslproperties.getOrDefault(KEY_STORE_PATH,""));
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_KEYSTORE_PASSWORD,sslproperties.getOrDefault(KEY_STORE_PWD,""));
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_KEY_PASSWORD,sslproperties.getOrDefault(KEY_STORE_PWD,""));
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_KEYSTORE_TYPE,sslproperties.getOrDefault(KEY_STORE_TYPE,""));
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_TRUSTSTORE_LOCATION,sslproperties.getOrDefault(TRUST_STORE_PATH,""));
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_TRUSTSTORE_PASSWORD,sslproperties.getOrDefault(TRUST_STORE_PASSWORD,""));
				clientProperties.put(KafkaProperties.SCHEMA_REGISTRY_SSL_TRUSTSTORE_TYPE,sslproperties.getOrDefault(TRUST_STORE_TYPE,""));
			} else {
				clientProperties.put(KafkaProperties.FTL_REALMSERVERS, destinationProps.getProperty("ftl.real.server"));
				clientProperties.put(KafkaProperties.FTL_TRUST_TYPE, "file");
				clientProperties.put(KafkaProperties.FTL_TRUST_FILE, trustCertPath);
				clientProperties.put(KafkaProperties.FTL_USERNAME, destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_USERNAME));
				clientProperties.put(KafkaProperties.FTL_PASSWORD, destinationProps.getProperty(KafkaProperties.SCHEMA_REGISTRY_PASSWORD));
			}
		} 
		
	}

	
	public static void getSSLConnectionInfo( String trustCertPath, String trustStorePwd, RuleServiceProvider rsp, String sharedResourceUri,
			String identityPath,Map<Object,Object> sslClientProperties)
			throws Exception {

		SharedArchiveResourceProvider sharedArchiveResourceProvider = rsp.getProject()
				.getSharedArchiveResourceProvider();
		GlobalVariables gv = rsp.getGlobalVariables();

		String trustedCertsURI = getSubstitutedStringValue(gv, trustCertPath);

		String trustStorePassword = decryptPwd(getSubstitutedStringValue(gv, trustStorePwd));

		KeyStore trustedKeysStore = SSLUtils.createKeystore(trustedCertsURI, null, sharedArchiveResourceProvider, gv,
				true);
		String trustedKsFileName = formFileKeystoreName(rsp.getProject().getName(), rsp.getName(), sharedResourceUri);
		String trustStore = SSLUtils.storeKeystore(trustedKeysStore, trustStorePassword, trustedKsFileName);

		sslClientProperties.put(TRUST_STORE_PATH, trustStore);
		sslClientProperties.put(TRUST_STORE_PASSWORD, trustStorePassword);
		sslClientProperties.put(TRUST_STORE_TYPE, SSLUtils.KEYSTORE_JKS_TYPE);
		
		if (null != identityPath && !identityPath.isBlank()) {
			BEIdentity keyStoreIdentity = getIdentity(identityPath, sharedArchiveResourceProvider, gv);
			if (keyStoreIdentity != null && keyStoreIdentity instanceof BEKeystoreIdentity) {
				
				sslClientProperties.put(KEY_STORE_PATH,  ((BEKeystoreIdentity) keyStoreIdentity).getStrKeystoreURL());
				sslClientProperties.put(KEY_STORE_PWD,((BEKeystoreIdentity) keyStoreIdentity).getStrStorePassword());
				sslClientProperties.put(KEY_STORE_TYPE, ((BEKeystoreIdentity) keyStoreIdentity).getStrStoreType());
				
			} else {
				String message = "Identity Resource - '" + identityPath + "' must be of type 'Identity file'";
				throw new Exception("Cassandra Connection - " + message);
			}
		}
	}
	

	public static BEIdentity getIdentity(String idReference, ArchiveResourceProvider provider, GlobalVariables gv)
			throws Exception {
		BEIdentity beIdentity = null;
		if ((idReference != null) && !idReference.trim().isEmpty()) {
			if (idReference.startsWith("/")) {
				beIdentity = BEIdentityUtilities.fetchIdentity(provider, gv, idReference);
			} else {
				throw new Exception("Incorrect Trusted Certificate Folder string: " + idReference);
			}
		}
		return beIdentity;
	}
	
	public static String formFileKeystoreName(String projectName, String engineName, String sharedResourceUri) {
		if (sharedResourceUri.indexOf('.') > -1) {
			sharedResourceUri = sharedResourceUri.substring(0, sharedResourceUri.indexOf('.'));// Remove the shared
			// resource extension
		}
		String name = projectName + "_" + engineName + "_" + sharedResourceUri;
		String prefix = "schema_ssl_";
		String extension = ".ks";
		return prefix + name.replaceAll("[/\\\\. ]", "_") + extension;// Replace all slashes, spaces, periods with
		// underscore.
	}
	
	public static String getSubstitutedStringValue(GlobalVariables gv, String value) {
		final CharSequence cs = gv.substituteVariables(value);
		if (null == cs) {
			return "";
		} else {
			return cs.toString();
		}
	}
	
	public static String decryptPwd(String encryptedPwd) {
		try {
			String decryptedPwd = encryptedPwd;
			if (ObfuscationEngine.hasEncryptionPrefix(encryptedPwd)) {
				decryptedPwd = new String(ObfuscationEngine.decrypt(encryptedPwd));
			}
			return decryptedPwd;
		} catch (AXSecurityException e) {
			//logger.log(Level.WARN, e.getMessage());
			return encryptedPwd;
		} finally {
			//restoreProviders();
		}
	}
	

}
