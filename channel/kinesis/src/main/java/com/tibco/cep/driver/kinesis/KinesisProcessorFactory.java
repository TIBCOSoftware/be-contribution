package com.tibco.cep.driver.kinesis;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventProcessor;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

class KinesisProcessorFactory implements com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory {

	private KinesisDestination ownerDestination;

	public KinesisProcessorFactory(KinesisDestination destination) {
		this.ownerDestination = destination;
	}

	@Override
	public IRecordProcessor createProcessor() {
		return new KinesisRecordProcessor(ownerDestination);
	}
}
	
class KinesisRecordProcessor implements IRecordProcessor {
	private EventProcessor eventProcessor;
	private BaseEventSerializer serializer;
	private Logger logger;
	private KinesisDestination kinesisdest;

	public KinesisRecordProcessor(KinesisDestination kinesisDestination) {
		this.kinesisdest = kinesisDestination;
		this.logger = kinesisDestination.getLogger();
		this.eventProcessor = kinesisDestination.getEventProcessor();
		this.serializer = kinesisDestination.getSerializer();
	}

	@Override
	public void initialize(InitializationInput input) {
		logger.log(Level.INFO, "Initializing shard");
	}
	
	@Override
	public void processRecords(ProcessRecordsInput input) {
		for (com.amazonaws.services.kinesis.model.Record record : input.getRecords()) {
			// process record
			if (kinesisdest.isSuspended()) {
				break;
			}
			try {
				Event e = deserializeMessage(record);
				eventProcessor.processEvent(e);
				checkpoint(input.getCheckpointer(), record);
			} catch (Exception e) {
				logger.log(Level.ERROR, e, "Error while deserializing record");
				throw new RuntimeException(e);
			}
		}
	}

	public Event deserializeMessage(Record record) throws Exception {

		Event event = serializer.deserializeUserEvent(record, kinesisdest.getSerializationProperties());
		
		if (event == null) {
			throw new Exception("Deserializer returned null event");
		}
		return event;
	}

	private void checkpoint(IRecordProcessorCheckpointer checkpointer, Record record) {
		logger.log(Level.INFO, "Checkpointing shard ");
		try {
			checkpointer.checkpoint(record);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public void shutdown(ShutdownInput input) {
		logger.log(Level.INFO, "Shutting down");
	}
}

