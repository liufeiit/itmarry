package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class Feed implements Serializable{

   private static final long serialVersionUID = -6393592105727931008L;
   private String      feed_id;
   private String      profile_img;
   private String      username;
   private String      description;
   private String      store_name;
   private String      store_id;
   private String      feed_msg;
   private String      feed_at_users;
   private int         level;
   private int         stiqer_num;
   private int         egg_num;
   private int         like_num;
   private int         comment_num;
   private String      feed_img_url_1;
   private String      feed_img_url_2;
   private String      feed_img_url_3;
   private String      time;
   private int         is_like;
   private int         feed_type;
   
   public int getFeed_type() {
	return feed_type;
}
public void setFeed_type(int feed_type) {
	this.feed_type = feed_type;
}
public String getFeed_at_users() {
	return feed_at_users;
}
public void setFeed_at_users(String feed_at_users) {
	this.feed_at_users = feed_at_users;
}
public String getFeed_msg() {
	return feed_msg;
}
public void setFeed_msg(String feed_msg) {
	this.feed_msg = feed_msg;
}
public int getIs_like() {
	return is_like;
}
public void setIs_like(int is_like) {
	this.is_like = is_like;
}
private int         error_code;
   private String      error_msg;
   

public String getFeed_id() {
	return feed_id;
}
public void setFeed_id(String feed_id) {
	this.feed_id = feed_id;
}
public String getProfile_img() {
	return profile_img;
}
public void setProfile_img(String profile_img) {
	this.profile_img = profile_img;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getStore_name() {
	return store_name;
}
public void setStore_name(String store_name) {
	this.store_name = store_name;
}
public int getLevel() {
	return level;
}
public void setLevel(int level) {
	this.level = level;
}
public int getStiqer_num() {
	return stiqer_num;
}
public void setStiqer_num(int stiqer_num) {
	this.stiqer_num = stiqer_num;
}
public int getEgg_num() {
	return egg_num;
}
public void setEgg_num(int egg_num) {
	this.egg_num = egg_num;
}
public int getLike_num() {
	return like_num;
}
public void setLike_num(int like_num) {
	this.like_num = like_num;
}
public int getComment_num() {
	return comment_num;
}
public void setComment_num(int comment_num) {
	this.comment_num = comment_num;
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
public String getStore_id() {
	return store_id;
}
public void setStore_id(String store_id) {
	this.store_id = store_id;
}
public String getFeed_img_url_1() {
	return feed_img_url_1;
}
public void setFeed_img_url_1(String feed_img_url_1) {
	this.feed_img_url_1 = feed_img_url_1;
}
public String getFeed_img_url_2() {
	return feed_img_url_2;
}
public void setFeed_img_url_2(String feed_img_url_2) {
	this.feed_img_url_2 = feed_img_url_2;
}
public String getFeed_img_url_3() {
	return feed_img_url_3;
}
public void setFeed_img_url_3(String feed_img_url_3) {
	this.feed_img_url_3 = feed_img_url_3;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
   
   
}
