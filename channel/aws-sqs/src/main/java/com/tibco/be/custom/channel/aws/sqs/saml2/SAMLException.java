/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

public class SAMLException extends Exception{

	
	private static final long serialVersionUID = -5674895492172646580L;

	public SAMLException(String exceptionMsg){
		super(exceptionMsg);
	}
}
