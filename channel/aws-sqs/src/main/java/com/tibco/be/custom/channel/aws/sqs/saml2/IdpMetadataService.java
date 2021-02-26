/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.BasicParserPool;

import java.io.File;


public class IdpMetadataService {
	
	/*
	 * Parse and Return IDP metadata Entity Descriptor object
	 */
	public EntityDescriptor parseIdpMetadataFromFile(String idpMetadataFilePath, String entityId) throws MetadataProviderException{
		
		FilesystemMetadataProvider idpMetaDataProvider = new FilesystemMetadataProvider(new File(idpMetadataFilePath));
		idpMetaDataProvider.setRequireValidMetadata(true);
		idpMetaDataProvider.setParserPool(new BasicParserPool());
		idpMetaDataProvider.initialize();
		EntityDescriptor idpEntityDescriptor = idpMetaDataProvider.getEntityDescriptor(entityId);
		return idpEntityDescriptor;
	}
	
	/*
	 * Return HTTP Post End point URL from bindings
	 */
	public String getHTTPPostEndpoint(EntityDescriptor idpEntityDescriptor) throws SAMLException{
		for (SingleSignOnService sss : idpEntityDescriptor.getIDPSSODescriptor(IdpConstants.SAML20P_NS).getSingleSignOnServices()) {
		   if (sss.getBinding().equals(IdpConstants.SAML2_POST_BINDING_URI)) {
			   return sss.getLocation();
		   }
		}
		throw new SAMLException("SAML2 POST Binding not available for your IDP. Contact your IDP administrator");
	}

}
