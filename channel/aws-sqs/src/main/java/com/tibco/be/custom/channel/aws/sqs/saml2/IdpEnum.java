/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

public class IdpEnum {

	public enum Idp{
		ADFS,
		PingFederate,
		Shibboleth;
	}
	
	public static Idp getIdpByName(String idpName){
		return Idp.valueOf(idpName); 
	}
}
