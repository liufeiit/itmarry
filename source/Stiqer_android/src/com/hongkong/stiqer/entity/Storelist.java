package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Storelist implements Serializable{

   private static final long serialVersionUID = -1227839437389807744L;
   private String      store_id;
   private String      store_name;
   private String      distance;
   private String      store_image_url;
   private String      des_first;
   private String      des_second;
   private int         error_code;
   private String      error_msg;
   private double      longitude;
   private double      latitude;
   
public double getLongitude() {
	return longitude;
}
public void setLongitude(double longitude) {
	this.longitude = longitude;
}
public double getLatitude() {
	return latitude;
}
public void setLatitude(double latitude) {
	this.latitude = latitude;
}
public String getStore_id() {
	return store_id;
}
public void setStore_id(String store_id) {
	this.store_id = store_id;
}
public String getStore_image_url() {
	return store_image_url;
}
public void setStore_image_url(String store_image_url) {
	this.store_image_url = store_image_url;
}
public String getDes_first() {
	return des_first;
}
public void setDes_first(String des_first) {
	this.des_first = des_first;
}
public String getDes_second() {
	return des_second;
}
public void setDes_second(String des_second) {
	this.des_second = des_second;
}

public String getStore_name() {
	return store_name;
}
public void setStore_name(String store_name) {
	this.store_name = store_name;
}
public String getDistance() {
	return distance;
}
public void setDistance(String distance) {
	this.distance = distance;
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