package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Prom implements Serializable{

   private static final long serialVersionUID = -6346350652837051737L;
   private String      promo_id;
   private String      img_uri;
   private String      banner_uri;
   private String      promo_des;
   private String      store_name;
   private String      store_id;
   private int         default_img;
   private int         error_code;
   private String      error_msg;
   
public int getDefault_img() {
	return default_img;
}
public void setDefault_img(int default_img) {
	this.default_img = default_img;
}
public String getStore_name() {
	return store_name;
}
public void setStore_name(String store_name) {
	this.store_name = store_name;
}
public String getStore_id() {
	return store_id;
}
public void setStore_id(String store_id) {
	this.store_id = store_id;
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
public String getBanner_uri() {
	return banner_uri;
}
public void setBanner_uri(String banner_uri) {
	this.banner_uri = banner_uri;
}

public String getPromo_id() {
	return promo_id;
}
public void setPromo_id(String promo_id) {
	this.promo_id = promo_id;
}
public String getPromo_des() {
	return promo_des;
}
public void setPromo_des(String promo_des) {
	this.promo_des = promo_des;
}
public String getImg_uri() {
	return img_uri;
}
public void setImg_uri(String img_uri) {
	this.img_uri = img_uri;
}

   
}