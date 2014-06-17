package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class CheckIn implements Serializable{

   private static final long serialVersionUID = -3657470816265054397L;
   private String        img_url;
   private String        img_id;
   private String        img_time;
   private String        img_store_id;
   private String        img_store_name;
   private String        img_store_img;
   private String        img_username;
   private String        img_user_img;
   private int           error_code;
   private String        error_msg;
   
public String getImg_url() {
	return img_url;
}
public void setImg_url(String img_url) {
	this.img_url = img_url;
}
public String getImg_id() {
	return img_id;
}
public void setImg_id(String img_id) {
	this.img_id = img_id;
}
public String getImg_time() {
	return img_time;
}
public void setImg_time(String img_time) {
	this.img_time = img_time;
}
public String getImg_store_id() {
	return img_store_id;
}
public void setImg_store_id(String img_store_id) {
	this.img_store_id = img_store_id;
}
public String getImg_store_name() {
	return img_store_name;
}
public void setImg_store_name(String img_store_name) {
	this.img_store_name = img_store_name;
}
public String getImg_store_img() {
	return img_store_img;
}
public void setImg_store_img(String img_store_img) {
	this.img_store_img = img_store_img;
}
public String getImg_username() {
	return img_username;
}
public void setImg_username(String img_username) {
	this.img_username = img_username;
}
public String getImg_user_img() {
	return img_user_img;
}
public void setImg_user_img(String img_user_img) {
	this.img_user_img = img_user_img;
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