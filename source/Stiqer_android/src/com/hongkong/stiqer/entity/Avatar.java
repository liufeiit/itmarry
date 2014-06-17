package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Avatar implements Serializable{

   private static final long serialVersionUID = -1007354932980488251L;
   private String      img_uri;
   private int         error_code;
   private String      error_msg;
   
public String getImg_uri() {
	return img_uri;
}
public void setImg_uri(String img_uri) {
	this.img_uri = img_uri;
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