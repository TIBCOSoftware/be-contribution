package com.tibco.cep.driver.sb.internal;

public interface ISBClient {

	void close() throws Exception;

	void stop() throws Exception;

	void resume() throws Exception;

	void suspend() throws Exception;

	void start(int startMode) throws Exception;

}
