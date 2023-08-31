package com.tibco.cep.analytics.statistica.io;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.NodeList;

import com.sun.xml.messaging.saaj.util.ByteOutputStream;
import com.tibco.cep.analytics.statistica.io.utils.StatConstants;
import com.tibco.cep.analytics.statistica.io.utils.StatisticaException;
import com.tibco.cep.analytics.statistica.io.utils.StatisticaFunctionsUtils;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.service.ServiceRegistry;
import com.tibco.cep.runtime.service.basic.ShutdownWatcher;

/**
 * @author swapna
 * 
 *         This class contains implementation of functions used to connect to
 *         Statistica
 */
public class StatFunctionsDelegate {

	private static final String MY_NAMESPACE = "typ";
	private static final String MY_NAMESPACE_URI = "http://webstatistica.statsoft.com/types";
	private static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(StatFunctionsDelegate.class);
	private static ConcurrentHashMap<Long, SOAPConnection> connMap = new ConcurrentHashMap<Long, SOAPConnection>();
	private static final int MAX_RETRIES = 2;
	
	static {
		ServiceRegistry registry = ServiceRegistry.getSingletonServiceRegistry();
		ShutdownWatcher watcher = registry.getShutdownWatcher();
		if (watcher != null) {
			
			watcher.addPreExitJob(new Runnable() {
				@Override
				public void run() {
					LOGGER.log(Level.INFO, "Closing all SOAP connections");
					Set<Entry<Long, SOAPConnection>> connectionEntries = connMap.entrySet();
					for (Entry<Long, SOAPConnection> mapEntry : connectionEntries) {
						try {
							SOAPConnection connection = connMap.remove(mapEntry.getKey());
							connection.close();
						} catch (SOAPException e) {
							LOGGER.log(Level.ERROR, "Cannot close the connection because: " + e.getMessage());
						}

					}

				}

			});
		}
	}

	/**
	 * getScoreWithParams - Implementation of getScoreWithParams for Statistica
	 * 
	 * @param conn       - Connection object for Statistica
	 * @param scriptName - Script Name of Statistica to be called
	 * @param parameters - Parameters of the request to be sent to Statistica
	 * @return Map of results received from Statistica
	 */
	public static Object getScoreWithParams(ConnectionInfo conn, String scriptName, Object parameters) {
		
		try {
			if (conn == null) {
				LOGGER.log(Level.ERROR, "Connection is null");
				throw new StatisticaException("Connection Info is null");
			}
			if (parameters == null) {
				throw new StatisticaException("Parameter Map cannot be null");
			}
			if (scriptName == null) {
				throw new StatisticaException("Script Name cannot be null");
			}
			
			Long tID = Thread.currentThread().getId();
			
			if(!connMap.containsKey(tID)) {
				synchronized (connMap) {
					if (!connMap.containsKey(tID)) {
						SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
						SOAPConnection soapConnection = soapConnectionFactory.createConnection();
						connMap.putIfAbsent(tID, soapConnection);
					}
				}
			}
			
			return callSoapWebService(conn.getHostUrl(), conn.getUserName(), conn.getPassword(), scriptName,
					(Map<String, Object>) parameters, connMap.get(tID));
		} catch (Exception e) {
			LOGGER.log(Level.ERROR,
					"Failed to get score from the action " + scriptName + " because: " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * getScoreWithConcept - Implementation of getScoreWithConcept for Statistica
	 * 
	 * @param conn       - Connection object for Statistica
	 * @param scriptName - Script Name of Statistica to be called
	 * @param concept    - Concept from which the Properties will be used to make
	 *                   Statistica request
	 * @return - Map of results received from Statistica
	 */
	public static Object getScoreWithConcept(ConnectionInfo conn, String scriptName, Concept concept) {
		return getScoreWithParams(conn, scriptName, StatisticaFunctionsUtils.parseParameters(concept));
	}

	/**
	 * getScoreWithEvent- Implementation of getScoreWithEvent for Statistica
	 * 
	 * @param conn       - Connection object for Statistica
	 * @param scriptName - Script Name of Statistica to be called
	 * @param event      - Event from which the Properties will be used to make
	 *                   Statistica request
	 * @return - Map of results received from Statistica
	 */
	public static Object getScoreWithEvent(ConnectionInfo conn, String scriptName, SimpleEvent event) {
		try {
			return getScoreWithParams(conn, scriptName, StatisticaFunctionsUtils.parseParameters(event));
		} catch (Exception e) {
			LOGGER.log(Level.ERROR,
					"Failed to get score from the action " + scriptName + " because: +" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Create a SOAP envelope
	 * 
	 * @param soapMessage - SOAP message to be sent to Statistica
	 * @param scriptName  - Script Name of Statistica
	 * @param parameters  - Map of parameters to be sent to Statistica
	 * @throws SOAPException
	 */
	private static void createSoapEnvelope(SOAPMessage soapMessage, String scriptName, Map<String, Object> parameters)
			throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(MY_NAMESPACE, MY_NAMESPACE_URI);

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElemParent = soapBody.addChildElement(scriptName, MY_NAMESPACE);

		if (parameters != null) {
			parameters.entrySet().forEach((entry) -> {
				try {
					SOAPElement soapBodyElemChild = soapBodyElemParent.addChildElement(entry.getKey());
					
					String value = null;
					Object propertyValue = entry.getValue();
					if (!(propertyValue instanceof String)) {
						LOGGER.log(Level.TRACE, "Converting parameter " + entry.getKey() + " to String format");
						value = String.valueOf(propertyValue);
					} else {
						value = (String) propertyValue;
					}
					soapBodyElemChild.addTextNode(entry.getValue() == null ? "" : value);
				} catch (SOAPException e) {
					LOGGER.log(Level.ERROR, "Error while creating SOAP Elements: "+ e.getMessage());
				}
			});
		}

		LOGGER.log(Level.DEBUG, "Created Soap Message successfully");

	}

	/**
	 * Makes a call to the SOAP service
	 * 
	 * @param soapEndpointUrl - URL of SOAP request for Statistica
	 * @param userName        - User name for Basic Auth in Statistica
	 * @param password        - Password for Basic Auth in Statistica
	 * @param scriptName      - Script Name of Statistica
	 * @param parameters      - Parameters to be sent to Statistica in the SOAP request
	 * @param soapConnection
	 * @return response of the SOAP request received from Statistica
	 */
	private static Map<String, String> callSoapWebService(String soapEndpointUrl, String userName, String password,
			String scriptName, Map<String, Object> parameters, SOAPConnection soapConnection) {
		
		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				LOGGER.log(Level.DEBUG, "Calling Statistica SOAP web service");
				return callStatisticaWebService(soapEndpointUrl, userName, password, scriptName, parameters,
						soapConnection);
			} catch (Exception e) {
				if (!(e instanceof StatisticaException)) {
					// Retry for all other Exceptions

					Long tID = Thread.currentThread().getId();
					SOAPConnectionFactory soapConnectionFactory;
					try {
						synchronized (connMap) {
							soapConnectionFactory = SOAPConnectionFactory.newInstance();
							SOAPConnection soapConnectionNew = soapConnectionFactory.createConnection();
							connMap.put(tID, soapConnectionNew);
						}
					} catch (UnsupportedOperationException | SOAPException e1) {
						LOGGER.log(Level.ERROR, "Error while creating SOAP connection to server: " + e1.getMessage());
						throw new RuntimeException("Could not create SOAP connection to server: " + e1.getMessage());
					}

				} else {
					LOGGER.log(Level.ERROR, "Error while making SOAP request to server: " + e.getMessage());
					throw new RuntimeException("Error while making SOAP request to server because: " + e.getMessage());
				}
			}
		}
		LOGGER.log(Level.ERROR, "Error while making SOAP request to server, maximum retries exceeded");
		throw new RuntimeException("Error while making SOAP request to server, maximum retries exceeded");
	}

	private static Map<String, String> callStatisticaWebService(String soapEndpointUrl, String userName,
			String password, String scriptName, Map<String, Object> parameters, SOAPConnection soapConnection)
			throws SOAPException, Exception {
		// Send SOAP Message to SOAP Server
		SOAPMessage soapResponse = soapConnection
				.call(createSOAPRequest(userName, password, scriptName, parameters), soapEndpointUrl);
		
		LOGGER.log(Level.DEBUG, "Got SOAP response");

		Map<String, String> responseMap = new HashMap<>();

		if (soapResponse.getSOAPPart().getEnvelope().getBody().getFault() != null) {
			SOAPFault fault = soapResponse.getSOAPPart().getEnvelope().getBody().getFault();
			String faultString = fault.getFaultString();
			LOGGER.log(Level.ERROR, "Statistica request could not be completed because: "+ faultString);
			throw new StatisticaException("Statistica Request failed because: " + faultString);
		}
		
		LOGGER.log(Level.DEBUG, "Got SOAP response as success");

		if (soapResponse.getSOAPBody().getChildElements() != null
				&& soapResponse.getSOAPBody().getChildElements().next() != null) {
			NodeList elements = ((SOAPElement) soapResponse.getSOAPBody().getChildElements().next())
					.getChildNodes();
			if (elements != null && elements.getLength() > 0) {
				for (int i = 0; i < elements.getLength(); i++) {
					SOAPElement element = (SOAPElement) elements.item(i);
					String nodeName = element.getNodeName();
					NodeList itemList = element.getChildNodes();
					String nodeValue = null;
					if (itemList != null && itemList.getLength() > 0) {
						nodeValue = itemList.item(0).getNodeValue();
					}
					responseMap.put(nodeName, nodeValue);
				}
			} else {
				LOGGER.log(Level.WARN, "The SOAP response is empty");
			}
		}

		return responseMap;
	}

	/**
	 * Creates a SOAP message from the given parameters and auth
	 * 
	 * @param userName   - User name for Basic Auth in Statistica
	 * @param password   - Password for Basic Auth in Statistica
	 * @param scriptName - Script Name of Statistica
	 * @param parameters - Parameters to be sent to Statistica in the SOAP request
	 * @return SOAP message with all input
	 * @throws Exception
	 */
	private static SOAPMessage createSOAPRequest(String userName, String password, String scriptName,
			Map<String, Object> parameters) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();

		StringBuffer buffer = new StringBuffer();
		buffer.append(userName);
		buffer.append(":");
		buffer.append(password);
		String encoded = new String(Base64.getEncoder().encode(buffer.toString().getBytes()));
		String authString = "Basic " + encoded;
		soapMessage.getMimeHeaders().addHeader("Authorization", authString);

		createSoapEnvelope(soapMessage, scriptName, parameters);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader(StatConstants.SOAP_ACTION_STAT_LABEL, StatConstants.SOAP_ACTION_STAT_VALUE);

		soapMessage.saveChanges();

		LOGGER.log(Level.DEBUG, "Created SOAP request to send to Statistica for action: "+ scriptName);
	    ByteOutputStream os = new ByteOutputStream();
        soapMessage.writeTo(os);
		LOGGER.log(Level.DEBUG, "SOAP request sent is: "+ os.toString());
		return soapMessage;
	}


}
