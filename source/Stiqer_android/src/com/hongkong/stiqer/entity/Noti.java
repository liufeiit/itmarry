package com.hongkong.stiqer.entity;

import java.io.Serializable;

import org.codehaus.jettison.json.JSONObject;

public class Noti implements Serializable{
   /*（1）type 共11种
    *1 回复message 2 为回复评论   3 点击like  4 checkin时@sb  5 store发布  6 level up时发布 7 商店内class up 8 为获得勋章  9 好友处理信息  10 邀请反馈
    *（2）from_type 有3种，谁发布的
    *1 为 user  2 为store 3 为stiqer官方
    *（3）noti_img_1 有4种情况
    *checkin时候，升级时class图标，获得勋章时勋章图案，store发布的消息
    *（4）noti_img_2 有两种
    *checkin时候，store 发布的信息
    * */
   private static final long serialVersionUID = 40762666949231922L;
   /*
   private String      noti_id;
   private int         type; // 1~10
   private int         from_type;
   
   private String      from_name;
   private String      from_img;
   private String      from_id;
   private int         from_level;
   
   private String      noti_time;
   private String      message;
 
   private String      node_id;
   private String      invite_username;
   private String      invite_img;
   
   private int         store_class;
   private String      store_class_name;
   private String      store_class_img;
   private String      store_class_id;
   
   private String      noti_img_1;
   private String      noti_img_2;
   
   private String      at_store_id;
   private String      at_store_name;
   */
   private int         error_code;
   private String      error_msg;
   
   private String      noti_id;
   private int         noti_type;
   private int         noti_sender_type;
   
   private String      noti_sender_name;
   private String      noti_sender_id;
   private String      noti_sender_img;
   
   private String      noti_time;
   private String      noti_message;
   private JSONObject  noti_extra;
   
public String getNoti_message() {
	return noti_message;
}
public void setNoti_message(String noti_message) {
	this.noti_message = noti_message;
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
public String getNoti_id() {
	return noti_id;
}
public void setNoti_id(String noti_id) {
	this.noti_id = noti_id;
}
public int getNoti_type() {
	return noti_type;
}
public void setNoti_type(int noti_type) {
	this.noti_type = noti_type;
}
public int getNoti_sender_type() {
	return noti_sender_type;
}
public void setNoti_sender_type(int noti_sender_type) {
	this.noti_sender_type = noti_sender_type;
}
public String getNoti_sender_name() {
	return noti_sender_name;
}
public void setNoti_sender_name(String noti_sender_name) {
	this.noti_sender_name = noti_sender_name;
}
public String getNoti_sender_id() {
	return noti_sender_id;
}
public void setNoti_sender_id(String noti_sender_id) {
	this.noti_sender_id = noti_sender_id;
}
public String getNoti_sender_img() {
	return noti_sender_img;
}
public void setNoti_sender_img(String noti_sender_img) {
	this.noti_sender_img = noti_sender_img;
}
public String getNoti_time() {
	return noti_time;
}
public void setNoti_time(String noti_time) {
	this.noti_time = noti_time;
}
public JSONObject getNoti_extra() {
	return noti_extra;
}
public void setNoti_extra(JSONObject noti_extra) {
	this.noti_extra = noti_extra;
}


}