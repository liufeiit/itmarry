package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Comment implements Serializable{

   private static final long serialVersionUID = -6393592105727931008L;
   private String      cmt_id;
   private String      cmt_username;
   private String      cmt_time;
   private int         cmt_user_level;
   private String      cmt_message;
   private String      cmt_user_img;
   private int         error_code;
   private String      error_msg;
   

   
public String getCmt_id() {
	return cmt_id;
}
public void setCmt_id(String cmt_id) {
	this.cmt_id = cmt_id;
}
public String getCmt_username() {
	return cmt_username;
}
public void setCmt_username(String cmt_username) {
	this.cmt_username = cmt_username;
}
public String getCmt_time() {
	return cmt_time;
}
public void setCmt_time(String cmt_time) {
	this.cmt_time = cmt_time;
}
public int getCmt_user_level() {
	return cmt_user_level;
}
public void setCmt_user_level(int cmt_user_level) {
	this.cmt_user_level = cmt_user_level;
}
public String getCmt_message() {
	return cmt_message;
}
public void setCmt_message(String cmt_message) {
	this.cmt_message = cmt_message;
}
public String getCmt_user_img() {
	return cmt_user_img;
}
public void setCmt_user_img(String cmt_user_img) {
	this.cmt_user_img = cmt_user_img;
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