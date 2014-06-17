package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class UpdateFriend implements Serializable{
	
	private static final long serialVersionUID = 8594255048514053486L;
	
	private String   username;
	private String   ext_uid;
	private int      friend_state;
	private int      user_level;
	private String   error_msg;
	private int      error_code;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getExt_uid() {
		return ext_uid;
	}
	public void setExt_uid(String ext_uid) {
		this.ext_uid = ext_uid;
	}
	public int getFriend_state() {
		return friend_state;
	}
	public void setFriend_state(int friend_state) {
		this.friend_state = friend_state;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	
	

}
