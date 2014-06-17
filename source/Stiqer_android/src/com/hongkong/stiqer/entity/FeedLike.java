package com.hongkong.stiqer.entity;

import java.io.Serializable;

public class FeedLike implements Serializable{

   private static final long serialVersionUID = -3133926929025624590L;
   
   private int           feed_like_num;
   private int           error_code;
   private String        error_msg;
   
public int getFeed_like_num() {
	return feed_like_num;
}
public void setFeed_like_num(int feed_like_num) {
	this.feed_like_num = feed_like_num;
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