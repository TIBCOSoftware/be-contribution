package com.tibco.cep.driver.sb.internal;

import com.streambase.sb.client.StreamBaseClient;
import com.tibco.cep.runtime.channel.Channel.Destination;

public interface ISBDestination extends Destination {

	StreamBaseClient getClient();

	String getPredicate();

}
