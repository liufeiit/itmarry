package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class SUser implements Serializable{
   /**
	** 
	**/
   private static final long serialVersionUID = -6402636005027698069L;
   private String     uid;
   private String     token;
   private String     username;
   private String     profile_img_url;
   private String     is_verified;
   private int        error_code;
   private String     error_msg;
 
   
   public String getIs_verified() {
	return is_verified;
}

public void setIs_verified(String is_verified) {
	this.is_verified = is_verified;
}

public String getError_msg() {
	return error_msg;
  }

  public void setError_msg(String error_msg) {
	this.error_msg = error_msg;
  }

   public String getProfile_img_url() {
	return profile_img_url;
  }

  public void setProfile_img_url(String profile_img_url) {
	this.profile_img_url = profile_img_url;
  }

   public int getError_code() {
	  return error_code;
   }

   public void setError_code(int error_code) {
	  this.error_code = error_code;
   }

   public String getUsername() {
	  return username;
   }

   public void setUsername(String username) {
	  this.username = username;
   }

  public String getUid() {
	  return uid;
   }
   
   public void setUid(String uid) {
	  this.uid = uid;
   }
   
   public String getToken() {
	  return token;
   }
   
   public void setToken(String token) {
	  this.token = token;
   }
   
   
   
}
