package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Rank implements Serializable{

   private static final long serialVersionUID = 5710506780275634242L;
   private String      profile_img;
   private String      username;
   private int         rank;
   private int         level;
   
public int getRank() {
	return rank;
}
public void setRank(int rank) {
	this.rank = rank;
}

public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}

public String getProfile_img() {
	return profile_img;
}
public void setProfile_img(String profile_img) {
	this.profile_img = profile_img;
}
public int getLevel() {
	return level;
}
public void setLevel(int level) {
	this.level = level;
}

   
   
}