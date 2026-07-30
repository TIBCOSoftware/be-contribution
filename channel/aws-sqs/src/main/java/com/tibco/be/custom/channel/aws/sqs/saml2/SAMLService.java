/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;
import net.shibboleth.utilities.java.support.xml.XMLParserException;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SAMLService {

	private static final Logger logger = LogManagerFactory
			.getLogManager().getLogger(SAMLService.class);

	private static SAMLService samlService = null;

	private SAMLService() throws SAMLException{

		Thread thread = Thread.currentThread();
	    ClassLoader loader = thread.getContextClassLoader();
	    thread.setContextClassLoader(this.getClass().getClassLoader());
	    try {
			//Initialize opensaml
				logger.log(Level.DEBUG,"Initializing OpenSAML");

				InitializationService.initialize();
	    } catch (InitializationException e) {
	        throw new SAMLException("Error in bootstrapping the OpenSAML library");
	    } finally {
	        thread.setContextClassLoader(loader);
	    }
	}

	public static SAMLService getInstance() throws SAMLException{
		if(samlService == null)
			samlService = new SAMLService();
		return samlService;
	}

	/*
	 * Parse SAML Response and return assertion object.
	 */

	public Assertion parseSAMLResponse(String samlResponse)throws ParserConfigurationException, SAXException, IOException, UnmarshallingException, XMLParserException
	{
		BasicParserPool parser = new BasicParserPool();
	    parser.setNamespaceAware(true);
	    try {
	        parser.initialize();
	    } catch (ComponentInitializationException e) {
	        throw new XMLParserException("Unable to initialize parser pool", e);
	    }

	    InputStream inputStream = new ByteArrayInputStream(samlResponse.getBytes(StandardCharsets.UTF_8));

	    Document document = parser.parse(inputStream);
		Element element = document.getDocumentElement();

		UnmarshallerFactory unmarshallerFactory = XMLObjectProviderRegistrySupport.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
		XMLObject responseXmlObj = unmarshaller.unmarshall(element);
		Response response = (Response) responseXmlObj;
		return response.getAssertions().get(0);
	}

	/*
	 * Returns Attribute Object for provided Attribute name.
	 */
	 public Attribute getRoleAttribute(Assertion assertionObj, String attributeName) throws SAMLException{
		 List<AttributeStatement> attrStmtLst =  assertionObj.getAttributeStatements();

		 for(AttributeStatement attrStmt : attrStmtLst){
			 List<Attribute> attrLst = attrStmt.getAttributes();

			 for(Attribute attr : attrLst){
				 if(attr.getName().equals(attributeName))
					 return attr;
			 }
		 }

		 throw new SAMLException("SAML Attribute not found - "+ attributeName);
	 }

	 /*
		 * Returns Attribute Object for provided Attribute name.
		 */
		 public List<String> getRoleAttributeValues(Assertion assertionObj, String attributeName) throws SAMLException{
			 List<AttributeStatement> attrStmtLst =  assertionObj.getAttributeStatements();

			 for(AttributeStatement attrStmt : attrStmtLst){
				 List<Attribute> attrLst = attrStmt.getAttributes();

				 for(Attribute attr : attrLst){
					 if(attr.getName().equals(attributeName))
					 {
						 List<String> attrLstStr = new ArrayList<String>();
						 for(XMLObject xmlObj : attr.getAttributeValues())
						 {
							 attrLstStr.add(xmlObj.getDOM().getTextContent());
						 }
						 return attrLstStr;
					 }
				 }
			 }

			 throw new SAMLException("SAML Attribute not found - "+ attributeName);
		 }
}
