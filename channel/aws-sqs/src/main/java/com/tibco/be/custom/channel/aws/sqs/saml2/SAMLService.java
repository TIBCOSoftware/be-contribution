/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SAMLService {
	
	private static SAMLService samlService = null;
	
	private SAMLService() throws ConfigurationException, SAMLException{
	
		Thread thread = Thread.currentThread();
	    ClassLoader loader = thread.getContextClassLoader();
	    thread.setContextClassLoader(this.getClass().getClassLoader());
	    try {
			//Initialize opensaml
			System.out.println("Initializing OpenSAML");
			DefaultBootstrap.bootstrap();
	    } catch (ConfigurationException e) {
	        throw new SAMLException("Error in bootstrapping the OpenSAML2 library");
	    } finally {
	        thread.setContextClassLoader(loader);
	    }
	}

	public static SAMLService getInstance() throws ConfigurationException, SAMLException{
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
	     
	    StringReader reader = new StringReader(samlResponse);
	     
	    Document document = parser.parse(reader);
		Element element = document.getDocumentElement();
		 
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
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
