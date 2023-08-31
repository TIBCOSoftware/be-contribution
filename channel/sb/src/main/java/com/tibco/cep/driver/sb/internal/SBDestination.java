package com.tibco.cep.driver.sb.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.streambase.sb.StreamBaseException;
import com.streambase.sb.Tuple;
import com.streambase.sb.client.ConnectionStatus;
import com.streambase.sb.client.ConnectionStatusCallback;
import com.streambase.sb.client.StreamBaseClient;
import com.streambase.sb.client.StreamBaseURI;
import com.tibco.cep.driver.sb.SBConstants;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.channel.Channel.State;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.channel.DestinationConfig;
import com.tibco.cep.runtime.channel.impl.AbstractDestination;
import com.tibco.cep.runtime.channel.impl.DefaultSerializationContext;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;
import com.tibco.xml.data.primitive.ExpandedName;

public class SBDestination extends AbstractDestination<SBChannel> implements ISBDestination, SBConstants {

	private class QueueEntry {
		private Tuple tuple;

		QueueEntry(Tuple tuple) {
			this.tuple = tuple;
		}
	}

    private StreamBaseClient client;

	// Supports suspend and resume
	private boolean isBinded;

	private Logger logger;
	private String streamName;
	private String clientType;
	private String filterPredicate;
	private boolean enableBuffering;
	private int bufferSize = 0;
	private long flushInterval = 0;

	private List<QueueEntry> localQueue;

	private SBDequeueClient dequeueClientRunnable;

	private boolean reconnecting;

	public SBDestination(SBChannel sbChannel, DestinationConfig config) {
		super(sbChannel, config);
		postConstruction();
	}

	private void postConstruction() {
		logger = channel.getLogger();
		streamName = channel.getGlobalVariables().substituteVariables(config.getProperties().getProperty(SB_DEST_PROP_STREAM_NAME)).toString();
		clientType = channel.getGlobalVariables().substituteVariables(config.getProperties().getProperty(SB_DEST_PROP_CLIENT_TYPE)).toString();
		filterPredicate = channel.getGlobalVariables().substituteVariables(config.getProperties().getProperty(SB_DEST_PROP_FILTER_PREDICATE)).toString();

		enableBuffering = Boolean.valueOf(channel.getGlobalVariables().substituteVariables(config.getProperties().getProperty(SB_DEST_BUFFERING_ENABLED)).toString());
		if (enableBuffering) {
			String buffSizeString = channel.getGlobalVariables().substituteVariables(config.getProperties().getProperty(SB_DEST_BUFFER_SIZE)).toString();
			if (buffSizeString != null && buffSizeString.trim().length() > 0) {
				bufferSize = Integer.valueOf(buffSizeString);
			}
			String buffIntervalString = channel.getGlobalVariables().substituteVariables(config.getProperties().getProperty(SB_DEST_BUFFER_INTERVAL)).toString();
			if (buffIntervalString != null && buffIntervalString.trim().length() > 0) {
				flushInterval = Long.valueOf(buffIntervalString);
			}
		}

		logger.log(Level.TRACE, "Destination '%s' created. StreamName: %s - ClientType: %s", this.getName(), streamName, clientType);
		localQueue = new LinkedList<QueueEntry>();
	}

	@Override
	public void bind(RuleSession session) throws Exception {
		isBinded = true;
		if (isDequeueClient()) {
			dequeueClientRunnable = new SBDequeueClient(session, client, logger, streamName, this);
			logger.log(Level.INFO, "Destination '%s' bound as an input destination for stream '%s'", this.getURI(), streamName);
		}
		super.bind(session);
	}

	@Override
	public void start(int mode) throws Exception {
		logger.log(Level.DEBUG, "'%s' starting Stream connection to '%s'", this.getName(), streamName);
		if (mode == ChannelManager.ACTIVE_MODE) {
			synchronized (syncObject) {
				for (QueueEntry qe : localQueue) {
					client.enqueue(streamName, qe.tuple);
					localQueue.remove(qe);
				}
			}
		}
        int startMode = (ChannelManager.ACTIVE_MODE == mode) && (this.userControlled) ? ChannelManager.SUSPEND_MODE : mode;
		if (isBinded) {
			if (!isDequeueClient()) {
				logger.log(Level.WARN, "Input destination '%s' not marked as Dequeuer, dequeue thread not started", this.getName());
			} else {
				startDequeuing(startMode);
			}
		}
		super.start(mode);
		logger.log(Level.DEBUG, "Destination '%s' started Stream connection to '%s'", this.getName(), streamName);
	}

	private boolean isDequeueClient() {
		return SB_DEST_CLIENT_TYPE_DEQUEUER.equals(clientType);
	}

	@Override
	public void connect() throws Exception {
		if (null != client) {
			return;
		}
		logger.log(Level.DEBUG, "Destination %s connecting to stream '%s'", this.getName(), streamName);
//		try {
			connectSBClient();
//		} catch (Exception e) {
//			logger.log(Level.WARN, "%s failed connecting to Stream '%s'! Error: %s", this.getName(), streamName, e.getMessage());
//			return;
//		}
		if (client != null) {
			if (SB_DEST_CLIENT_TYPE_DEQUEUER.equals(clientType)) {
				if (!client.canDequeue()) {
					// TODO : report error - throw exception?
					logger.log(Level.WARN, "Server '%s' cannot dequeue", client.getURI());
				}
			} else {
				
			}
		}
	}


//	static {
//	    //for localhost testing only
//	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
//	    new javax.net.ssl.HostnameVerifier(){
//
//	        public boolean verify(String hostname,
//	                javax.net.ssl.SSLSession sslSession) {
//	            if (hostname.equals("localhost")) {
//	                return true;
//	            }
//	            return false;
//	        }
//	    });
//	}
	
	private void connectSBClient() throws StreamBaseException, AXSecurityException {
		final String serverURI = getChannel().getServerURI();
//		try {
		
			List<StreamBaseURI> uris = new ArrayList<StreamBaseURI>();
			String uname = getChannel().getUserName();
			String password = getChannel().getPassword();
			if (password != null && password.startsWith("#!")) {
				password = new String(ObfuscationEngine.decrypt(password));
			}
			
			if (serverURI.indexOf(',') > 0) {
				// uri string contains multiple uris
				String[] serverURIs = serverURI.split(",");
				for (String sURI : serverURIs) {
					uris.add(new StreamBaseURI(sURI));
				}
			} else {
				uris.add(new StreamBaseURI(serverURI));
			}
			StreamBaseURI[] uriArr = new StreamBaseURI[uris.size()];
			uris.toArray(uriArr);
			uris.clear();
			String trustStore = getChannel().getTrustStore();
			String trustStorePass = getChannel().getTrustStorePass();
			if (trustStorePass != null && trustStorePass.startsWith("#!")) {
				trustStorePass = new String(ObfuscationEngine.decrypt(trustStorePass));
			}
			String keyStore = getChannel().getKeyStore();
			String keyStorePass = getChannel().getKeyStorePass();
			if (keyStorePass != null && keyStorePass.startsWith("#!")) {
				keyStorePass = new String(ObfuscationEngine.decrypt(keyStorePass));
			}
			String keyPass = getChannel().getKeyPass();
			if (keyPass != null && keyPass.startsWith("#!")) {
				keyPass = new String(ObfuscationEngine.decrypt(keyPass));
			}
			if (trustStore != null && trustStore.length() > 0) {
				System.setProperty("javax.net.ssl.trustStore", trustStore);
			}
			if (trustStorePass != null && trustStorePass.length() > 0) {
				System.setProperty("javax.net.ssl.trustStorePassword", trustStorePass);
			}
			boolean setUnameAndPass = uname != null && uname.trim().length() > 0 && password != null && password.trim().length() > 0;
			for (StreamBaseURI uri : uriArr) {
				Map<String, String> params = new HashMap<>();
				if (setUnameAndPass) {
					// only set uname/pass if both are specified
					params.put(StreamBaseURI.USER_PARAM, uname);
					params.put(StreamBaseURI.PASSWORD_PARAM, password);
				}
				if (keyStore != null && keyStore.length() > 0) {
					params.put(StreamBaseURI.KEYSTORE_PARAM, keyStore);
				}
				if (keyPass != null && keyPass.length() > 0) {
					params.put(StreamBaseURI.KEY_PASS_PARAM, keyPass);
				}
				if (keyStorePass != null && keyStorePass.length() > 0) {
					params.put(StreamBaseURI.KEYSTORE_PASS_PARAM, keyStorePass);
				}
				uris.add(new StreamBaseURI(uri.getHost(), uri.getContainer(), uri.getPort(), uri.isSSL(), params));
			}
			try {
				client = new StreamBaseClient(uris);
			} catch (StreamBaseException e) {
				if (uname.trim().length() > 0) {
					throw new StreamBaseException("Unable to connect to server '"+serverURI+"' with the specified credentials", e.getCause());
				} else {
					throw new StreamBaseException("Unable to connect to server '"+serverURI+"'", e.getCause());
				}
			}
			client.enableHeartbeating();
			if (enableBuffering) {
				if (bufferSize > 0) {
					if (flushInterval < 0) {
						flushInterval = 0;
					}
					client.enableBuffering(bufferSize, flushInterval);
				}
			}
			client.addConnectionStatusCallback(new ConnectionStatusCallback() {
				
				@Override
				public void stateChanged(ConnectionStatus status, StreamBaseClient cl, String details) {
					if (status == ConnectionStatus.DISCONNECTED) {
						if (details == null) {
							if (getChannel().getState() == State.STOPPED) {
								details = "SBChannel stopped";
							} else {
								details = "No details provided";
							}
						}
						logger.log(Level.INFO, "Destination [%s]: disconnected from Stream server URI '%s' [details: %s].", getURI(), serverURI, details);
						if (!client.isConnected() && shouldReconnect()) {
							// attempt reconnect
							reconnect();
						}
					}
				}
			});
//		} catch (StreamBaseException e) {
//			logger.log(Level.ERROR, e, "Destination: error connecting client");
//		}
		logger.log(Level.INFO, "Destination [%s]: client connected to StreamBase server URI '%s'", getURI(), serverURI);
		
	}

	protected void reconnect() {
		reconnecting = true;
		// close the existing client, shutdown dequeue thread, and attempt to connect a new client
		try {
			if (dequeueClientRunnable != null) {
				logger.log(Level.INFO, "Destination [%s]: Shutting down dequeue client for Stream '%s'", getURI(), streamName);
				dequeueClientRunnable.stop();
				dequeueClientRunnable = null;
			}
			if (client != null) {
				logger.log(Level.INFO, "Destination [%s]: Closing connection to StreamBase server URI '%s'", getURI(), getChannel().getServerURI());
				client.close();
				client = null;
			}
		} catch (StreamBaseException e) {
			logger.log(Level.WARN, "Destination [%s]: Exception while closing connection to StreamBase server URI '%s'", getURI(), getChannel().getServerURI());
		} catch (Exception e) {
			logger.log(Level.WARN, "Destination [%s]: Exception while closing connection to StreamBase server URI '%s'", getURI(), getChannel().getServerURI());
		}
		int retryCount = 0;
		int maxRetryCount = getRetryCount();
		if (maxRetryCount == 0) {
			logger.log(Level.INFO, "Destination [%s]: Property '%s' set to %d, no reconnect attempts will be made", getURI(), SBConstants.SB_RECONNECT_ATTEMPTS_PROP, maxRetryCount);
			return;
		}
		while (true) {
			logger.log(Level.INFO, "Destination [%s]: Attempting reconnect to StreamBase server URI '%s'", getURI(), getChannel().getServerURI());
			try {
				connectSBClient();
			} catch (AXSecurityException | StreamBaseException e) {
				retryCount++;
				logger.log(Level.WARN, "Destination [%s]: Reconnect failed [cause '%s']", getURI(), e.getMessage());
				if (maxRetryCount > 0 && retryCount >= maxRetryCount) {
					logger.log(Level.INFO, "Destination [%s]: Reconnect attempts reached max, no further reconnect attempts will be made", getURI());
					return;
				}
				try {
					Thread.sleep(getRetryInterval());
				} catch (InterruptedException e1) {
				}
			}
			// no exception means success
			if (client != null) {
				try {
					rebind();
					start(ChannelManager.ACTIVE_MODE);
				} catch (Exception e) {
					logger.log(Level.WARN, "Destination [%s]: Rebind failed [cause '%s']", getURI(), e.getMessage());
				}
				reconnecting = false;
				return;
			}
		}
	}
	
    protected void rebind() throws Exception {
        Collection<RuleSession> ruleSessions = getBoundSessions();
        Iterator<RuleSession> r = ruleSessions.iterator();
        while (r.hasNext()) {
            RuleSession rsess = r.next();
            bind(rsess);
        }
    }
    
	private int getRetryCount() {
		String property = getChannel().getServiceProviderProperties().getProperty(SBConstants.SB_RECONNECT_ATTEMPTS_PROP, "10");
		try {
			return Integer.parseInt(property);
		} catch (Exception e) {
		}
		return 10;
	}

	private long getRetryInterval() {
		String property = getChannel().getServiceProviderProperties().getProperty(SBConstants.SB_RECONNECT_INTERVAL_PROP, "5000");
		try {
			return Integer.parseInt(property);
		} catch (Exception e) {
		}
		return 5000;
	}

	protected boolean shouldReconnect() {
		return getChannel().getState() != State.STOPPED;
	}

	private void startDequeuing(int startMode) throws Exception {
		dequeueClientRunnable.start(startMode);
	}

	@Override
	public int send(SimpleEvent event, Map overrideData) throws Exception {
		if (client == null) {
			logger.log(Level.ERROR, "StreamBase client is null for destination '%s', Event not sent", getURI());
			return -1;
		}
		RuleSession rSession = RuleSessionManager.getCurrentRuleSession();
		Tuple tuple = (Tuple) serializer.serialize(event, new DefaultSerializationContext(rSession, this));
		if (channel.getState() == State.CONNECTED || channel.getState() == State.STARTED) {
			logger.log(Level.DEBUG, "Put tuple '%s' to stream '%s'", tuple.toString(), streamName);
			client.enqueue(streamName, tuple);
		} else {
			if (channel.getState() == State.INITIALIZED) {
				channel.getLogger().log(Level.INFO, "Channel Initialized, but not started. Queuing message for later delivery");

				synchronized (this.syncObject) {
					QueueEntry localQueueEntry = new QueueEntry(tuple);
					localQueue.add(localQueueEntry);
				}
				return 1;
			}
		}
		return super.send(event, overrideData);
	}

	@Override
	public void suspend() {
		logger.log(Level.INFO, "Suspending destination '%s'", getURI());
		try {
			if (isBinded && isDequeueClient()) {
				dequeueClientRunnable.suspend();
			}
			super.suspend();
		} catch (Exception e) {
			logger.log(Level.WARN, e, "Failed to suspend destination '%s'", getURI());
		}
	}

	@Override
	public void stop() {
		logger.log(Level.INFO, "Stopping destination '%s'", getURI());
		try {
			if (isBinded && isDequeueClient()) {
				dequeueClientRunnable.shutdown();
			}
			super.stop();
		} catch (Exception e) {
			logger.log(Level.WARN, e, "Failed to stop destination '%s'", getURI());
		}
	}

	@Override
	public void resume() {
		logger.log(Level.INFO, "Resuming destination '%s'", getURI());
		if (!this.userControlled) {
			try {
				if (isBinded && isDequeueClient()) {
					if (dequeueClientRunnable == null) {
						logger.log(Level.WARN, "Failed to resume destination '%s' [Dequeue client is null]", getURI());
						super.resume();
						return;
					}
					dequeueClientRunnable.resume();
				}
				super.resume();
			} catch (Exception e) {
				logger.log(Level.WARN, e, "Failed to resume destination '%s'", getURI());
			}
		}
	}

	@Override
	public void close() {
		logger.log(Level.INFO, "%s closing Stream '%s'", this.getName(), streamName);
		if (null != dequeueClientRunnable) {
			// destroy() won't be called if this is not an input destination,
			// so we must ensure that close dequeueClientRunnable instance.
			try {
				dequeueClientRunnable.close();
			} catch (Exception ex) {
				logger.log(Level.ERROR, ex, "%s failed closing dequeue client: '%s'", this.getName(), streamName);
			} finally {
				dequeueClientRunnable = null;
			}
		}
		if (null != client) {
			try {
				client.close();
			} catch (StreamBaseException e) {
				logger.log(Level.ERROR, e, "%s failed closing StreamBase client: '%s'", this.getName(), streamName);
			}
		}
	}

	@Override
	protected void destroy(RuleSession session) throws Exception {
		logger.log(Level.INFO, "'%s' destroying Stream connection to '%s'", this.getName(), streamName);
		try {
			if (null != dequeueClientRunnable) {
				dequeueClientRunnable.close();
			}
		} finally {
			dequeueClientRunnable = null;
		}
	}

	@Override
	public Object requestEvent(SimpleEvent outevent,
			ExpandedName responseEventURI, String serializerClass,
			long timeout, Map overrideData) throws Exception {
		throw new Exception("requestEvent() is not supported with SBChannel");
	}

	@Override
	public StreamBaseClient getClient() {
		return client;
	}

	@Override
	public String getPredicate() {
		return filterPredicate;
	}

}
