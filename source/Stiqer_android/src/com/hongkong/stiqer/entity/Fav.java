package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Fav implements Serializable{

   private static final long serialVersionUID = -1227839437389807744L;
   private String      favor_id;
   private int         favor_type;
   
   private String      store_id;
   private String      store_name;
   private String      store_img;
   private int         store_type;
   
   private String      promo_id;
   private String      promo_img;
   private String      promo_des;
   
   private int         error_code;
   private String      error_msg;
   
   
public String getFavor_id() {
	return favor_id;
}
public void setFavor_id(String favor_id) {
	this.favor_id = favor_id;
}
public int getFavor_type() {
	return favor_type;
}
public void setFavor_type(int favor_type) {
	this.favor_type = favor_type;
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
public String getStore_img() {
	return store_img;
}
public void setStore_img(String store_img) {
	this.store_img = store_img;
}
public int getStore_type() {
	return store_type;
}
public void setStore_type(int store_type) {
	this.store_type = store_type;
}
public String getPromo_id() {
	return promo_id;
}
public void setPromo_id(String promo_id) {
	this.promo_id = promo_id;
}
public String getPromo_img() {
	return promo_img;
}
public void setPromo_img(String promo_img) {
	this.promo_img = promo_img;
}
public String getPromo_des() {
	return promo_des;
}
public void setPromo_des(String promo_des) {
	this.promo_des = promo_des;
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