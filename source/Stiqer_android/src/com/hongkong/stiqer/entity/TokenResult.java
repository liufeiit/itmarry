package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class TokenResult implements Serializable{

   private static final long serialVersionUID = -1227839437389807744L;
   
   private String      uid;
   private String      new_token;
   private int         error_code;
   private String      error_msg;
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getNew_token() {
	return new_token;
}
public void setNew_token(String new_token) {
	this.new_token = new_token;
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