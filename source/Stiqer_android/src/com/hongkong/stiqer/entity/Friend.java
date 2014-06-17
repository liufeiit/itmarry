package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Friend implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2003107741873077646L;
	private String  username;
	private int     user_level;
	private String  user_index;
	private String  profile_img;
	private int     is_selected;
    private int     error_code;
    private String  error_msg;
    
    
	public int getIs_selected() {
		return is_selected;
	}
	public void setIs_selected(int is_selected) {
		this.is_selected = is_selected;
	}
	public String   getUsername() {
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
	public String getUser_index() {
		return user_index;
	}
	public void setUser_index(String user_index) {
		this.user_index = user_index;
	}
	public String getProfile_img() {
		return profile_img;
	}
	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
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
