/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2.idpimpl;

import com.tibco.be.custom.channel.aws.sqs.saml2.IdpAuthHandler;
import com.tibco.be.custom.channel.aws.sqs.saml2.IdpConstants;
import com.tibco.be.custom.channel.aws.sqs.saml2.IdpEnum;
import com.tibco.be.custom.channel.aws.sqs.saml2.SAMLException;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericIdpAuthHandler implements IdpAuthHandler {
	
	private SSLSocketFactory sSLSocketFactory = null;
	private String userAgent = IdpConstants.USER_AGENT;

	public String generateSAMLAssertion(IdpEnum.Idp idpName, String idpEntryUrl, String username, String password, boolean decoded, boolean isProxy, Proxy proxy, final String proxyUsername, final String proxyPassword, SSLSocketFactory sSLSocketFactory) throws IOException, SAMLException
	{

		this.sSLSocketFactory = sSLSocketFactory;
		
		if(isProxy && proxyUsername != null && proxyPassword != null){
			Authenticator.setDefault(new Authenticator() {
			      protected PasswordAuthentication getPasswordAuthentication() {
			        return new
			           PasswordAuthentication(proxyUsername,proxyPassword.toCharArray());
			    }});
		}
		
		String userAgentProp = System.getProperty("com.tibco.bw.awsplugins.saml.useragent");
		if(userAgentProp != null && !userAgentProp.trim().isEmpty()){
			userAgent = userAgentProp;
		}
		System.out.println("User-Agent : "+ userAgent);
		
		Response response = getLoginForm(idpEntryUrl, isProxy, proxy);
		Map<String, String> cookies = response.cookies();
		Map<String, String> payload = preparePayload(idpName, username, password);
		String formActionUrl = getFormActionUrl(idpName, idpEntryUrl, response);
		
		System.out.println("Form Action Url : "+ formActionUrl);
		System.out.println("Payload : "+ payload);
		System.out.println("Cookies : "+ cookies);
		
		String assertion = invokeSAMLRequest(formActionUrl, cookies, payload, isProxy, proxy);
		
		System.out.println("Assertion:"+assertion);
		
		if(!decoded)
		{
			return assertion;
		}else{
			byte[] decodedAssertion = DatatypeConverter.parseBase64Binary(assertion);
			return new String(decodedAssertion, "UTF-8");
		}
	}
	
	/*
	 * Launches IDP entry URL and follows all redirects, returns response object for further requests.
	 */
	
	private Response getLoginForm(String idpEntryUrl, boolean isProxy, Proxy proxy) throws IOException{

		Response response = null;
		if(!isProxy && this.sSLSocketFactory == null)
		{
			response = Jsoup.connect(idpEntryUrl)
					.followRedirects(true)
					.method(Connection.Method.GET)
					.userAgent(userAgent)
					.execute();
		} else if(isProxy && this.sSLSocketFactory == null) {
			response = Jsoup.connect(idpEntryUrl)
					.followRedirects(true)
					.method(Connection.Method.GET)
					.userAgent(userAgent)
					.proxy(proxy)
					.execute();
		} else if(!isProxy && this.sSLSocketFactory != null) {
			response = Jsoup.connect(idpEntryUrl)
					.followRedirects(true)
					.method(Connection.Method.GET)
					.sslSocketFactory(sSLSocketFactory)
					.userAgent(userAgent)
					.execute();
		} else if(isProxy && this.sSLSocketFactory != null) {
			response = Jsoup.connect(idpEntryUrl)
					.followRedirects(true)
					.method(Connection.Method.GET)
					.userAgent(userAgent)
					.proxy(proxy)
					.sslSocketFactory(sSLSocketFactory)
					.execute();
		}
		
		return response;
	}
	
	/*
	 * Prepare the post request payload
	 */
	
	private Map<String, String> preparePayload(IdpEnum.Idp idpName, String username, String password){
		Map<String, String> payload = new HashMap<String, String>();
		
		if(idpName == IdpEnum.Idp.PingFederate){
			payload.put(IdpConstants.PING_USERNAME, username);
			payload.put(IdpConstants.PING_PASSWORD, password);
		}
		else if(idpName == IdpEnum.Idp.Shibboleth){
			payload.put(IdpConstants.SHIBBOLETH_USERNAME, username);
			payload.put(IdpConstants.SHIBBOLETH_PASSWORD, password);
		}
		else if(idpName == IdpEnum.Idp.ADFS){
			payload.put(IdpConstants.ADFS_USERNAME, username);
			payload.put(IdpConstants.ADFS_PASSWORD, password);
			payload.put(IdpConstants.ADFS_AUTH_METHOD, "FormsAuthentication");
		}
		
		return payload;
	}
	
	/*
	 * Returns Form action URL for POST requests
	 */
	
	private String getFormActionUrl(IdpEnum.Idp idpName, String idpEntryUrl, Response response) throws IOException{
		if(idpName == IdpEnum.Idp.PingFederate){
			/*Document doc = response.parse();
			//extract form action value.
			Elements forms = doc.select("form");		
			String action = forms.get(0).attr("action");
			String actionUrl = "";
			if(action.contains("http://") || action.contains("https://"))
				actionUrl = action;
			else
			{
				URL idpUrl = new URL(idpEntryUrl);
				actionUrl = idpUrl.getProtocol() + "://" + idpUrl.getHost() + action;
			}
			return actionUrl;*/
			return idpEntryUrl;
		}
		else if(idpName == IdpEnum.Idp.Shibboleth){
			return idpEntryUrl;
		}
		else if(idpName == IdpEnum.Idp.ADFS){
			return idpEntryUrl;
		}
		return idpEntryUrl;
	}

	/*
	 * Invokes POST request and extract SAMLResponse element from response.
	 */
	private String invokeSAMLRequest(String formActionUrl, Map<String, String> cookies, Map<String, String> payload, boolean isProxy, Proxy proxy) throws IOException, SAMLException{
		//invoke HTTP Post request for Idp Authentication
		Response samlResponse = null;
		if(!isProxy && this.sSLSocketFactory == null)
		{
			samlResponse = Jsoup.connect(formActionUrl)
				.method(Connection.Method.POST)
				.userAgent(userAgent)
				.data(payload)
				.cookies(cookies)
				.execute();
		} else if(isProxy && this.sSLSocketFactory == null) {
			samlResponse = Jsoup.connect(formActionUrl)
					.method(Connection.Method.POST)
					.userAgent(userAgent)
					.data(payload)
					.cookies(cookies)
					.proxy(proxy)
					.execute();
		} else if(!isProxy && this.sSLSocketFactory != null) {
			samlResponse = Jsoup.connect(formActionUrl)
					.method(Connection.Method.POST)
					.userAgent(userAgent)
					.data(payload)
					.cookies(cookies)
					.sslSocketFactory(sSLSocketFactory)
					.execute();
		} else if(isProxy && this.sSLSocketFactory != null) {
			samlResponse = Jsoup.connect(formActionUrl)
					.method(Connection.Method.POST)
					.userAgent(userAgent)
					.data(payload)
					.cookies(cookies)
					.proxy(proxy)
					.sslSocketFactory(sSLSocketFactory)
					.execute();
		}
		
		System.out.println("SAML Response Body : " + samlResponse.statusCode() + samlResponse.body());
		
		if(samlResponse.statusCode() == 200)
		{
			Document resDoc = samlResponse.parse();
			
			//Extract SAMLResponse element from body.		
			Elements inputElem = resDoc.select("input");
			String assertion =null;
			for (Iterator<Element> i = inputElem.iterator(); i.hasNext();){
				Element ele = i.next();
				if(ele.attr("name").equals("SAMLResponse")){
					assertion = ele.val();
					break;
				}
			}
			if(assertion != null)
				return assertion;
		}
		throw new SAMLException("SAML assertion request failed.");
	}
}
