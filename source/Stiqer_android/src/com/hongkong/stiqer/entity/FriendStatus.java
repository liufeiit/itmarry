package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class FriendStatus implements Serializable{

	private static final long serialVersionUID = -8698777457428413321L;
	
	private int       friend_state;    
	private int       error_code;
	private String    error_msg;
	
	public int getFriend_state() {
		return friend_state;
	}
	public void setFriend_state(int friend_state) {
		this.friend_state = friend_state;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	
}
