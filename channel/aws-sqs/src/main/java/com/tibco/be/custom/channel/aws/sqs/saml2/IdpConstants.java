/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;


public class IdpConstants  {
	

	public static String SAML2_POST_BINDING_URI="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST";
	public static String SAML20P_NS="urn:oasis:names:tc:SAML:2.0:protocol";
	
	//ping
	public static String PING_USERNAME="pf.username";
	public static String PING_PASSWORD="pf.pass";
	
	//shibboleth
	public static String SHIBBOLETH_USERNAME="j_username";
	public static String SHIBBOLETH_PASSWORD="j_password";
	
	//ADFS
	public static String ADFS_USERNAME = "UserName";
	public static String ADFS_PASSWORD = "Password";
	public static String ADFS_AUTH_METHOD= "AuthMethod";
	
	public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
	
}
