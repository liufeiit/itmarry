package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class ReturnCheckIn implements Serializable{

   private static final long serialVersionUID = -3657470816265054397L;
   private String        img_url_1;
   private String        img_url_2;
   private int           error_code;
   private String        error_msg;
   

public String getImg_url_1() {
	return img_url_1;
}
public void setImg_url_1(String img_url_1) {
	this.img_url_1 = img_url_1;
}
public String getImg_url_2() {
	return img_url_2;
}
public void setImg_url_2(String img_url_2) {
	this.img_url_2 = img_url_2;
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