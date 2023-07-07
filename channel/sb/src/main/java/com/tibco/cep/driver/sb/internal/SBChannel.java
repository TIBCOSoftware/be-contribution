package com.tibco.cep.driver.sb.internal;

import java.util.Map;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.runtime.channel.ChannelConfig;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.channel.DestinationConfig;
import com.tibco.cep.runtime.channel.impl.AbstractChannel;
import com.tibco.cep.runtime.model.event.SimpleEvent;

public class SBChannel extends AbstractChannel<ISBDestination> {

	protected SBChannel(ChannelManager manager, String uri, ChannelConfig config) {
		super(manager, uri, config);
		postConstruction();
	}

	private void postConstruction() {
        for (Object config : channelConfig.getDestinations()) {
            DestinationConfig destConfig = (DestinationConfig) config;
            ISBDestination sbDest = createDestination(destConfig);
            this.destinations.put(sbDest.getURI(), sbDest);
        }
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		
        for (final Destination d : this.getDestinations().values()) {
            d.init();
        }
	}

	@Override
	public void start(int mode) throws Exception {
		getLogger().log(Level.INFO, "Starting SBChannel '%s'", getURI());
		if ((getState() == State.INITIALIZED) || (getState() == State.UNINITIALIZED)) {
			connect();
		}

		if ((getState() == State.CONNECTED) || (getState() == State.STOPPED) || (getState() == State.STARTED)) {
			for (Destination dest : getDestinations().values()) {
				dest.start(mode);
			}
		}
        super.start(mode);
		getLogger().log(Level.INFO, "SBChannel '%s' started", getURI());
    }
	
	@Override
	public void connect() throws Exception {
		State state = getState();

		if (state == State.UNINITIALIZED) {
			init();
		} else if ((state == State.STARTED) || (state == State.CONNECTED) || (state == State.STOPPED)) {
			return;
		} else if ((state == State.INITIALIZED) || (state == State.RECONNECTING)) {
			synchronized (state) {
				state = getState(); // Check the state again, call the getState,
									// as opposed to the state variable
				if ((state != State.STARTED) && (state != State.CONNECTED) && (state != State.STOPPED)) {
					// connect sb client
//					connectSBClient();

                    for (Destination d : getDestinations().values()) {
                        d.connect();
                    }

					setState(State.CONNECTED);
				}
			}
		} else if (state == State.EXCEPTION) {
			// re-connect sb client?
//			connectSBClient();
			setState(State.CONNECTED);
			return;
		} else {
			throw new Exception("Invalid state to connect..." + state);
		}
	}

	@Override
	public void close() throws Exception {

        for (Destination d : getDestinations().values()) {
            d.close();
        }

        destinations.clear();

        setState(State.INITIALIZED);
    }

	@Override
	public void send(SimpleEvent event, String destinationURI, Map overrideData)
			throws Exception {
		getLogger().log(Level.TRACE, "sending event name %s", event.getExpandedName().localName);
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	public <D extends Destination> D createDestination(DestinationConfig config) {
		getLogger().log(Level.TRACE, "Creating SBDestination name: %s", config.getName());
		return (D) new SBDestination(this, config);
	}

	public String getServerURI() {
		return ((SBChannelConfig)this.channelConfig).getServerURI();
	}
	
	public String getUserName() {
		return ((SBChannelConfig)this.channelConfig).getUserName();
	}

	public String getPassword() {
		return ((SBChannelConfig)this.channelConfig).getPassword();
	}
	
	public String getKeyStore() {
		return ((SBChannelConfig)this.channelConfig).getKeyStore();
	}
	
	public String getKeyStorePass() {
		return ((SBChannelConfig)this.channelConfig).getKeyStorePass();
	}
	
	public String getTrustStore() {
		return ((SBChannelConfig)this.channelConfig).getTrustStore();
	}
	
	public String getTrustStorePass() {
		return ((SBChannelConfig)this.channelConfig).getTrustStorePassword();
	}
	
	public String getKeyPass() {
		return ((SBChannelConfig)this.channelConfig).getKeyPass();
	}
	
}
