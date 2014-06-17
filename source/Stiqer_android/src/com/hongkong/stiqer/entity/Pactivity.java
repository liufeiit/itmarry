package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Pactivity implements Serializable{

   private static final long serialVersionUID = -8932078272999496466L;
   
   private String      act_id;
   private String      act_type;
   private String      store_id;
   private String      store_name;
   private String      time;
   private String      new_class;
   private int         new_level;
   
   
   public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
private int         error_code;
   private String      error_msg;

public String getAct_id() {
	return act_id;
}
public void setAct_id(String act_id) {
	this.act_id = act_id;
}
public String getAct_type() {
	return act_type;
}
public void setAct_type(String act_type) {
	this.act_type = act_type;
}
public String getStore_id() {
	return store_id;
}
public void setStore_id(String store_id) {
	this.store_id = store_id;
}
public String getStore_name() {
	return store_name;
}
public void setStore_name(String store_name) {
	this.store_name = store_name;
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
public String getNew_class() {
	return new_class;
}
public void setNew_class(String new_class) {
	this.new_class = new_class;
}
public int getNew_level() {
	return new_level;
}
public void setNew_level(int new_level) {
	this.new_level = new_level;
}

}