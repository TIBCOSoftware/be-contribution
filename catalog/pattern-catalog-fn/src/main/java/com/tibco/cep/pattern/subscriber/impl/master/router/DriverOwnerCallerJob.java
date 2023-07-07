package com.tibco.cep.pattern.subscriber.impl.master.router;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.impl.util.Flags;
import com.tibco.cep.pattern.integ.impl.master.DefaultSourceBridge;
import com.tibco.cep.pattern.integ.master.SourceBridge;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInput;
import com.tibco.cep.pattern.matcher.master.DriverOwner;
import com.tibco.cep.pattern.matcher.response.Response;

import java.util.concurrent.Callable;


public class DriverOwnerCallerJob
        implements Callable<Object>
{

    protected SourceBridge<Object> sourceBridge;

    protected DriverOwner driverOwner;

    protected DefaultInput input;

    protected Object messageId;

    protected Object message;


    public DriverOwnerCallerJob(
            SourceBridge<Object> sourceBridge,
            DriverOwner driverOwner,
            DefaultInput input,
            Object messageId,
            Object message)
    {
        this.sourceBridge = sourceBridge;
        this.driverOwner = driverOwner;
        this.input = input;
        this.messageId = messageId;
        this.message = message;
    }


    /**
     * @return The given message.
     * @throws Exception
     */
    public Object call()
            throws Exception
    {
        for(Id correlationId : driverOwner.getThreadLocalDriverCorrelationIds()) {
	    	Response response = driverOwner.onInput(sourceBridge, correlationId, input);
	
	        //-------------
	
	        if (Flags.DEBUG) {
	            System.out
	                    .println("Message [" + messageId + "] to alias ["
	                            + sourceBridge.getAlias() + "] with subscription ["
	                            + driverOwner.getOwnerId() + "] and correlation-id ["
	                            + correlationId + "] received response [" + response + "].");
	        }
        }

        return message;
    }

}
