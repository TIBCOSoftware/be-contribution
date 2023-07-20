package com.tibco.cep.driver.kafkastreams.test;

import com.tibco.cep.driver.kafka.BEKafkaConsumer;
import com.tibco.cep.driver.kafka.BEKafkaProducer;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

/**
 * This Logger impl is just for test executions of {@linkplain BEKafkaProducer} and {@linkplain BEKafkaConsumer}.
 * 
 * @author moshaikh
 */
public class KafkaTestLogger implements Logger {
	@Override
	public void log(Level level, Throwable thrown, String format, Object... args) {
		System.out.println(level + " : " + String.format(format, args));
		if (thrown != null) {
			thrown.printStackTrace();
		}
	}
	@Override
	public void setLevel(Level level) {}
	@Override
	public void log(Level level, Throwable thrown, String msg) {log(level, thrown, msg, new Object[0]);}
	@Override
	public void log(Level level, String format, Throwable thrown, Object... args) {log(level, thrown, format, args);}
	@Override
	public void log(Level level, String format, Object... args) {log(level, null, format, args);}
	@Override
	public void log(Level level, String msg) {log(level, null, msg, new Object[0]);}
	@Override
	public boolean isEnabledFor(Level level) {return false;}
	@Override
	public String getName() {return "KafkaTestLogger";}
	@Override
	public Level getLevel() {return null;}
	@Override
	public void close() {}
}
