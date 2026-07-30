/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;
import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.saml.metadata.resolver.impl.FilesystemMetadataResolver;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;

import java.io.File;


public class IdpMetadataService {

	/*
	 * Parse and Return IDP metadata Entity Descriptor object
	 */
	public EntityDescriptor parseIdpMetadataFromFile(String idpMetadataFilePath, String entityId) throws ResolverException{

		try {
			BasicParserPool parserPool = new BasicParserPool();
			parserPool.setNamespaceAware(true);
			parserPool.initialize();

			FilesystemMetadataResolver idpMetaDataResolver = new FilesystemMetadataResolver(new File(idpMetadataFilePath));
			idpMetaDataResolver.setRequireValidMetadata(true);
			idpMetaDataResolver.setParserPool(parserPool);
			idpMetaDataResolver.setId("idpFilesystemMetadataResolver");
			idpMetaDataResolver.initialize();

			EntityDescriptor idpEntityDescriptor = idpMetaDataResolver
					.resolveSingle(new CriteriaSet(new EntityIdCriterion(entityId)));
			return idpEntityDescriptor;
		} catch (ComponentInitializationException e) {
			throw new ResolverException("Error initializing IDP metadata resolver", e);
		}
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
