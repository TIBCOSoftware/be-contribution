package com.tibco.cep.driver.mqtt;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * @author ssinghal
 *
 */
public class BEMqttConnectionOptions {
	
	MqttConnectOptions connOpt;
	ReentrantLock lock = new ReentrantLock();
	Integer connectionAttempts;
	long attemptDelay;
	String willTopicName;
	
	
	public MqttConnectOptions getConnOpt() {
		return connOpt;
	}
	public void setConnOpt(MqttConnectOptions connOpt) {
		this.connOpt = connOpt;
	}
	public Integer getConnectionAttempts() {
		return connectionAttempts;
	}
	public void setConnectionAttempts(Integer connectionAttempts) {
		this.connectionAttempts = connectionAttempts;
	}
	public long getAttemptDelay() {
		return attemptDelay;
	}
	public void setAttemptDelay(long attemptDelay) {
		this.attemptDelay = attemptDelay;
	}
	public ReentrantLock getLock() {
		return lock;
	}
	public String getWillTopicName() {
		return willTopicName;
	}
	public void setWillTopicName(String willTopicName) {
		this.willTopicName = willTopicName;
	}
	
}
