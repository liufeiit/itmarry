package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Mission implements Serializable{

   private static final long serialVersionUID = 4914283229916679308L;
   private String      mission_id;
   private String      mission_name;
   private String      mission_img;
   private String      mission_des;
   private int         stiqer_all;
   private int         stiqer_now;
   private String      misson_completed;
   

public String getMission_id() {
	return mission_id;
}
public void setMission_id(String mission_id) {
	this.mission_id = mission_id;
}
public String getMission_name() {
	return mission_name;
}
public void setMission_name(String mission_name) {
	this.mission_name = mission_name;
}
public String getMission_img() {
	return mission_img;
}
public void setMission_img(String mission_img) {
	this.mission_img = mission_img;
}

public String getMission_des() {
	return mission_des;
}
public void setMission_des(String mission_des) {
	this.mission_des = mission_des;
}
public int getStiqer_all() {
	return stiqer_all;
}
public void setStiqer_all(int stiqer_all) {
	this.stiqer_all = stiqer_all;
}
public int getStiqer_now() {
	return stiqer_now;
}
public void setStiqer_now(int stiqer_now) {
	this.stiqer_now = stiqer_now;
}
public String getMisson_completed() {
	return misson_completed;
}
public void setMisson_completed(String misson_completed) {
	this.misson_completed = misson_completed;
}
   
 
 
   
}