package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class NotiNum implements Serializable{

   private static final long serialVersionUID = -3133926929025624590L;
   
   private int           noti_num;
   private int           error_code;
   private String        error_msg;
   
public int getNoti_num() {
	return noti_num;
}
public void setNoti_num(int noti_num) {
	this.noti_num = noti_num;
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