package com.tibco.cep.driver.sb.internal;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.tibco.cep.designtime.model.service.channel.WebApplicationDescriptor;
import com.tibco.cep.driver.sb.SBConstants;
import com.tibco.cep.repo.ArchiveResourceProvider;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.runtime.channel.ChannelConfig;
import com.tibco.xml.data.primitive.ExpandedName;
import com.tibco.xml.datamodel.XiNode;
import com.tibco.xml.datamodel.helpers.XiChild;

public class SBChannelConfig implements ChannelConfig, SBConstants {

	private ChannelConfig config;
	private GlobalVariables gv;
	private ArchiveResourceProvider resourceProvider;
	private String userName;
	private String password;
	private String serverURI;
	private String trustStore, trustStorePass, keyStore, keyPass, keyStorePass;

	public SBChannelConfig(ChannelConfig config, GlobalVariables gv,
			ArchiveResourceProvider provider) throws Exception {
		this.config = config;
		this.gv = gv;
		this.resourceProvider = provider;
		
		postConstruction();
	}

	private void postConstruction() throws Exception {
		ConfigurationMethod method = getConfigurationMethod();
		if (method == ConfigurationMethod.PROPERTIES) {
			configureByProperties();
		}
		else if (method == ConfigurationMethod.REFERENCE) {
			configureByReference();
		}
	}

	private void configureByReference() throws Exception {
		String ref = this.getReferenceURI();
		XiNode rootNode = resourceProvider.getResourceAsXiNode(ref);
		XiNode configNode = XiChild.getChild(rootNode, XML_NODE_CONFIG);

		serverURI = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_SERVER_URI);
		userName = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_AUTH_USERNAME);
		password = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_AUTH_PASSWORD);
		trustStore = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_TRUST_STORE);
		trustStorePass = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_TRUST_STORE_PASSWORD);
		keyStore = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_KEY_STORE);
		keyStorePass = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_KEY_STORE_PASSWORD);
		keyPass = getSubstitutedStringValue(gv, configNode, CHANNEL_PROPERTY_KEY_PASSWORD);
	}

	private void configureByProperties() {
		serverURI = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_SERVER_URI.getLocalName());

		// user-pwd
		userName = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_AUTH_USERNAME.getLocalName());
		password = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_AUTH_PASSWORD.getLocalName());
		trustStore = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_TRUST_STORE.getLocalName());
		trustStorePass = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_TRUST_STORE_PASSWORD.getLocalName());
		keyStore = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_KEY_STORE.getLocalName());
		keyStorePass = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_KEY_STORE_PASSWORD.getLocalName());
		keyPass = getSubstitutedStringValue(gv, config, CHANNEL_PROPERTY_KEY_PASSWORD.getLocalName());

	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getKeyStore() {
		return keyStore;
	}
	
	public String getKeyStorePass() {
		return keyStorePass;
	}
	
	public String getTrustStore() {
		return trustStore;
	}
	
	public String getTrustStorePassword() {
		return trustStorePass;
	}
	
	public String getKeyPass() {
		return keyPass;
	}
	
	public String getServerURI() {
		return serverURI;
	}

	@Override
	public boolean isActive() {
		return config.isActive();
	}

	private String getSubstitutedStringValue(GlobalVariables gv, ChannelConfig config, String propName) {
		return getSubstitutedStringValue0(gv, config.getProperties().getProperty(propName));
	}

	private String getSubstitutedStringValue(GlobalVariables gv, XiNode node, ExpandedName name) {
		return getSubstitutedStringValue0(gv, XiChild.getString(node, name));
	}

    private String getSubstitutedStringValue0(GlobalVariables gv, String key) {
		CharSequence cs = gv.substituteVariables(key);
		return cs == null ? "" : cs.toString();
	}

	@Override
	public ConfigurationMethod getConfigurationMethod() {
		return config.getConfigurationMethod();
	}

	@Override
	public Properties getProperties() {
		return config.getProperties();
	}

	@Override
	public String getReferenceURI() {
		return config.getReferenceURI();
	}

	@Override
	public String getType() {
		return config.getType();
	}

	@Override
	public String getURI() {
		return config.getURI();
	}

	public String getName() {
		return config.getName();
	}

	@Override
	public Collection<?> getDestinations() {
		return config.getDestinations();
	}

	@Override
	public String getServerType() {
		return null;
	}

	@Override
	public List<WebApplicationDescriptor> getWebApplicationDescriptors() {
		return null;
	}

}
