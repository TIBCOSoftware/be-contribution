package com.tibco.cep.driver.kafka;

import java.security.KeyStore;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.tibco.be.custom.channel.BaseChannel;
import com.tibco.be.custom.channel.BaseDestination;
import com.tibco.cep.driver.http.server.utils.SSLUtils;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.repo.ArchiveResourceProvider;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.runtime.service.security.BEIdentity;
import com.tibco.cep.runtime.service.security.BEIdentityUtilities;
import com.tibco.cep.runtime.service.security.BEKeystoreIdentity;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.util.SystemProperty;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;

/**
 * Implementation of Custom API's BaseChannel for Kafka. An instance of this
 * class represents a Kafka Channel.
 * 
 * @author moshaikh
 */
public class KafkaChannel extends BaseChannel {

	/**
	 * We have chosen to override init() It will initialize a common threadpool for
	 * all Kafka Destinations to use, to start their consumers
	 */
	private KafkaRequestReplyHandler kafkaRequestReply;

	public KafkaRequestReplyHandler getKafkaRequestReply() {
		return kafkaRequestReply;
	}

	@Override
	public void init() throws Exception {
		configureSecurity();

		getLogger().log(Level.INFO, "Initializing Kafka Channel with broker url: "
				+ getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_BOOTSTRAP_SERVER));
		// Call super.init here, it in turn invokes each of the destinations init
		// methods. This gives a chance to each of the destinations to initialize
		// itself.

		kafkaRequestReply = new KafkaRequestReplyHandler(getUri(),
				this.getBeProperties().getProperty(SystemProperty.ENGINE_NAME.getPropertyName()), this.getLogger());

		super.init();

	}

	@Override
	public void connect() throws Exception {

		Properties props = new Properties();
		for (Entry<Object, Object> entry : getChannelProperties().entrySet()) {
			if (entry.getValue() != null) {
				props.put(entry.getKey(), getGlobalVariableValue((String) entry.getValue()).toString());
				
			}
		}
		
		
		props.put(KafkaProperties.INTERNAL_PROP_KEY_KEY_DESERIALIZER,
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put(KafkaProperties.INTERNAL_PROP_KEY_VALUE_DESERIALIZER,
				"org.apache.kafka.common.serialization.ByteArrayDeserializer");

		Properties consumerProperties = initConsumerProperties(props, this.getBeProperties());
		KafkaConsumer consumer = new KafkaConsumer(consumerProperties);

		try {
			consumer.listTopics();
		} catch (Exception e) {
			getLogger().log(Level.FATAL, e, "Failed to connect Kafka server at [%s]",
					props.getProperty(KafkaProperties.KEY_CHANNEL_BOOTSTRAP_SERVER));
			throw new Exception("Failed to connect Kafka server");
		} finally {
			consumer.close();
		}

		super.connect();
	}

	private Properties initConsumerProperties(Properties destinationProps, Properties beProperties) {
		Properties consumerProperties = new Properties();
		// Kafka consumer configuration settings
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_BOOTSTRAP_SERVER), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.GROUP_ID_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_GROUP_ID), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_AUTOCOMMIT_INTERVAL), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_HEARTBEAT_INTERVAL), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_DESTINATION_SESSION_TIMEOUT), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_KEY_DESERIALIZER), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_VALUE_DESERIALIZER), consumerProperties);

		KafkaPropertiesHelper.putValueIfNotEmpty("ssl.truststore.location",
				destinationProps.getProperty("javax.net.ssl.trustStore"), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty("ssl.truststore.password",
				destinationProps.getProperty("javax.net.ssl.trustStorePassword"), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty("ssl.truststore.type",
				destinationProps.getProperty("javax.net.ssl.trustStoreType"), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty("ssl.keystore.location",
				destinationProps.getProperty("javax.net.ssl.keyStore"), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty("ssl.keystore.password",
				destinationProps.getProperty("javax.net.ssl.keyStorePassword"), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty("ssl.keystore.type",
				destinationProps.getProperty("javax.net.ssl.keyStoreType"), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
				destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL), consumerProperties);
		KafkaPropertiesHelper.putValueIfNotEmpty("sasl.mechanism",
				destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_SASL_MECHANISM), consumerProperties);

		if ("SASL_PLAINTEXT".equals(destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL))
				|| "SASL_SSL".equals(destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL))) {
			KafkaPropertiesHelper.putValueIfNotEmpty("sasl.mechanism",
					destinationProps.getProperty(KafkaProperties.KEY_CHANNEL_SASL_MECHANISM), consumerProperties);
		}
		
		String channelUri = this.getUri();
		String destinationUri = destinationProps.getProperty(KafkaProperties.INTERNAL_PROP_KEY_DESTINATION_URI);

		KafkaPropertiesHelper.loadOverridenProperties(KafkaProperties.PROPERTY_KEY_KAFKA_CLIENT_PROPERTY_PREFIX,
				beProperties, consumerProperties, channelUri, destinationUri);
		
		return consumerProperties;
	}

	/**
	 * We have chosen to override close() It will call the super close and
	 * additionally perform kafkaChannel's close tasks.
	 */
	@Override
	public void close() throws Exception {
		// this invokes destination.close on each destination.
		super.close();

		// close the executor service

		for (Entry<String, BaseDestination> dest : this.destinations.entrySet()) {
			dest.getValue().close();
		}

		/*
		 * Properties properties = new Properties();
		 * properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		 * AdminClient adminClient = AdminClient.create(properties);
		 * 
		 * try {
		 * 
		 * List<NewTopic> list = new ArrayList<NewTopic>();
		 * 
		 * CreateTopicsResult newTopic = adminClient.createTopics(list);
		 * ListTopicsResult topics = adminClient.listTopics();
		 * //topics.names().get().forEach(System.out::println); Set<String> set =
		 * topics.names().get();
		 * 
		 * List<String> TOPIC_LIST = new ArrayList<String>(set);
		 * adminClient.deleteTopics(TOPIC_LIST).all().get(); adminClient.close();
		 * 
		 * } catch (InterruptedException | ExecutionException e) { // TODO
		 * Auto-generated catch block if(null != adminClient) adminClient.close();
		 * e.printStackTrace(); }
		 */

	}

	/**
	 * Initializes the truststore/keystore properties. Should be executed before
	 * super.init so that the destinations picks up the new properties being added
	 * to channelProps here.
	 * 
	 * @throws Exception
	 */
	private void configureSecurity() throws Exception {
		if ("SSL".equals(getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL))
				|| "SASL_SSL"
						.equals(getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_SECURITY_PROTOCOL))) {

			if (getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_TRUSTED_CERTS_FOLDER) != null
					&& !getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_TRUSTED_CERTS_FOLDER)
							.equals("")) {

				RuleServiceProvider rsp = (RuleServiceProvider) getRuleServiceProvider();
				KeyStore trustedKeysStore = SSLUtils.createKeystore(
						getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_TRUSTED_CERTS_FOLDER), null,
						rsp.getProject().getSharedArchiveResourceProvider(), rsp.getGlobalVariables(), true);
				String trustedStoreFileName = formFileKeystoreName(rsp.getProject().getName(), rsp.getName(), getUri());
				String truststorePassword = decryptPwd(
						getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_TRUSTSTORE_PASSWORD));
				String trustStore = SSLUtils.storeKeystore(trustedKeysStore, truststorePassword, trustedStoreFileName);
				getChannelProperties().put("javax.net.ssl.trustStore", trustStore);
				getChannelProperties().put("javax.net.ssl.trustStoreType", "JKS");
				getChannelProperties().put("javax.net.ssl.trustStorePassword", truststorePassword);

				if (getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_KEYSTORE_IDENTITY) != null
						&& !getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_KEYSTORE_IDENTITY).trim()
								.isEmpty()) {
					getLogger().log(Level.INFO, "Configuring Kafka Channel SSL with client authentication..");
					String keyStoreIdentityPath = getSubstitutedStringValue(rsp.getGlobalVariables(),
							getChannelProperties().getProperty(KafkaProperties.KEY_CHANNEL_KEYSTORE_IDENTITY));
					BEIdentity keyStoreIdentity = getIdentity(keyStoreIdentityPath,
							rsp.getProject().getSharedArchiveResourceProvider(), rsp.getGlobalVariables());
					if (keyStoreIdentity != null && keyStoreIdentity instanceof BEKeystoreIdentity) {
						getChannelProperties().put("javax.net.ssl.keyStore",
								((BEKeystoreIdentity) keyStoreIdentity).getStrKeystoreURL());
						getChannelProperties().put("javax.net.ssl.keyStoreType",
								((BEKeystoreIdentity) keyStoreIdentity).getStrStoreType());
						getChannelProperties().put("javax.net.ssl.keyStorePassword",
								((BEKeystoreIdentity) keyStoreIdentity).getStrStorePassword());
					} else {
						String message = "Identity Resource - '" + keyStoreIdentityPath
								+ "' must be of type 'Identity file'";
						throw new Exception("Kafka Connection - " + message);
					}
				} else {
					getLogger().log(Level.INFO, "Configuring Kafka Channel SSL without client authentication..");
				}
			}
		}
	}

	/**
	 * Crates BEIdentity object for the identity file.
	 * 
	 * @param idReference
	 * @param provider
	 * @param gv
	 * @return
	 * @throws Exception
	 */
	private static BEIdentity getIdentity(String idReference, ArchiveResourceProvider provider, GlobalVariables gv)
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

	private static String getSubstitutedStringValue(GlobalVariables gv, String value) {
		final CharSequence cs = gv.substituteVariables(value);
		if (null == cs) {
			return "";
		} else {
			return cs.toString();
		}
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

	/**
	 * TODO: get rid of this
	 */
	private static String formFileKeystoreName(String projectName, String engineName, String sharedResourceUri) {
		if (sharedResourceUri.indexOf('.') > -1) {
			sharedResourceUri = sharedResourceUri.substring(0, sharedResourceUri.indexOf('.'));// Remove the shared
																								// resource extension
		}
		String name = projectName + "_" + engineName + "_" + sharedResourceUri;
		String prefix = "kafka_ssl_";
		String extension = ".ks";
		return prefix + name.replaceAll("[/\\\\. ]", "_") + extension;// Replace all slashes, spaces, periods with
																		// underscore.

	}

}
