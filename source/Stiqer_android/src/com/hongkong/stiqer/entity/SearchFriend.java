package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class SearchFriend implements Serializable{

	private static final long serialVersionUID = -6776114435189946867L;
	
	private String  username;
	private String  profile_img;
	private int     user_level;
	private int     friend_state;
	private int     error_code;
    private String  error_msg;
    
	public String getProfile_img() {
		return profile_img;
	}
	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
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
	public int getFriend_state() {
		return friend_state;
	}
	public void setFriend_state(int friend_state) {
		this.friend_state = friend_state;
	}
	
}
