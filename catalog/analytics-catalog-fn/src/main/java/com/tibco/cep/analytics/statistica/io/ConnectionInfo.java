package com.tibco.cep.analytics.statistica.io;

public class ConnectionInfo {
	private String hostUrl;
	private String userName;
	private String password;
	
	public ConnectionInfo(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	
	public ConnectionInfo(String hostUrl, String userName, String password) {
		this.hostUrl = hostUrl;
		this.userName = userName;
		this.password = password;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getUserName() {
		return userName;
	}

	void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	void setPassword(String password) {
		this.password = password;
	}

}
