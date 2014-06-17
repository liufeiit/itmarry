package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class AddFriend implements Serializable{
	
	private static final long serialVersionUID = 8164878937205405290L;
	
	private int       type;
	private String    username; //已注册人的名字
	private String    rawfid;
	private String    name; // third_party 名字
	private String    sortKey;
	private String    avatar;
	private int       status;
	private int       user_level;

	public String getRawfid() {
		return rawfid;
	}
	public void setRawfid(String rawfid) {
		this.rawfid = rawfid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}



}
