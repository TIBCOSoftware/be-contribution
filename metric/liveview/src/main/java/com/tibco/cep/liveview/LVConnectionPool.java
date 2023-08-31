package com.tibco.cep.liveview;

import com.streambase.liveview.client.ConnectionConfig;
import com.streambase.liveview.client.LiveViewConnection;
import com.streambase.liveview.client.LiveViewConnectionFactory;
import com.tibco.be.util.pool.ObjectPool;

/**
 * @author vpatil
 */
public class LVConnectionPool extends ObjectPool<LiveViewConnection> {

	private ConnectionConfig config;

	/**
	 * 
	 * @param urls
	 * @param userName
	 * @param userPassword
	 * @param initialSize
	 * @param maxSize
	 * @throws Exception
	 */
	public LVConnectionPool(String uri, String userName, String userPassword, int initialSize, int maxSize)
			throws Exception {
		super(initialSize, maxSize, false);

		config = new ConnectionConfig();
		config.setUri(uri);
		config.setUsername(userName);
		config.setPassword(userPassword);
	}

	@Override
	protected void initialize() throws Exception {
		super.initialize();
	}

	@Override
	protected LiveViewConnection createObject() throws Exception {
		return LiveViewConnectionFactory.getConnection(config);
	}

	@Override
	public boolean removeObject(LiveViewConnection object) throws Exception {
		object.close();
		return true;
	}
}
