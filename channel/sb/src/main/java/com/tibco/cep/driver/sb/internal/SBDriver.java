package com.tibco.cep.driver.sb.internal;

import com.tibco.cep.repo.ArchiveResourceProvider;
import com.tibco.cep.repo.GlobalVariables;
import com.tibco.cep.runtime.channel.Channel;
import com.tibco.cep.runtime.channel.ChannelConfig;
import com.tibco.cep.runtime.channel.ChannelManager;
import com.tibco.cep.runtime.channel.spi.ChannelDriver;


public class SBDriver implements ChannelDriver {

	public Channel createChannel (ChannelManager manager, String uri, ChannelConfig config) throws Exception {
		GlobalVariables gv = manager.getRuleServiceProvider().getGlobalVariables();
		ArchiveResourceProvider provider = manager.getRuleServiceProvider().getProject().getSharedArchiveResourceProvider();
		SBChannelConfig sbConfig = new SBChannelConfig(config, gv, provider);
		return new SBChannel(manager, uri, sbConfig);    
	}

}
