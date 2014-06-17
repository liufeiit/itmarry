package com.jingtuo.android.common.ftp;

public class FtpConfig {

	private String hostname;
	
	private int port;
	
	private int mode = MODE_LOCAL_PASSIVE;
	
	private String username;
	
	private String password;
	
	public static final int MODE_LOCAL_PASSIVE = 0;
	

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
