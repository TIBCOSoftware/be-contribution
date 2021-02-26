/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Proxy;

public interface IdpAuthHandler {
	
	public String generateSAMLAssertion(IdpEnum.Idp idpName, String idpEntryUrl, String username, String password, boolean decoded, boolean isProxy, Proxy proxy, final String proxyUsername, final String proxyPassword, SSLSocketFactory sSLSocketFactory) throws IOException, SAMLException;
}
