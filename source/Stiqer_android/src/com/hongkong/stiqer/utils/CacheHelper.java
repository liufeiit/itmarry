package com.hongkong.stiqer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hongkong.stiqer.entity.SUser;

public class CacheHelper {
	public static String      TAG              =  "Stiqer";
	public static final int   CACHE_TIME       =  3600;
	SharedPreferences cachePreferences;
	SharedPreferences.Editor editor;
	
	public CacheHelper(Context context){
		this.cachePreferences = context.getSharedPreferences(TAG, 0);
		this.editor = cachePreferences.edit();
	}
	
	//获得存储user
    public SUser GetUser(){
    	SUser newUser = new SUser();
    	newUser.setUid(cachePreferences.getString("uid", ""));
    	newUser.setUsername(cachePreferences.getString("username", ""));
    	newUser.setToken(cachePreferences.getString("token", ""));
    	newUser.setProfile_img_url(cachePreferences.getString("profile_image_url", ""));
    	newUser.setIs_verified(cachePreferences.getString("phone_verified", ""));
       return newUser;
    }
    
    public void clearCacheTotal() {
    	editor.putInt("friend",0);
    	editor.putInt("addfriend",0);
    	editor.putInt("fav",0);
    	editor.commit();
	}
    
    //friend, profile, addfriend, fav
    public void setCache(String key){
    	editor.putInt(key,getTimeStamp());
    	editor.commit();
    }
    
    public void clearCache(String key){
    	editor.putInt(key,0);
    	editor.commit();
    }
    
    public boolean checkCache(String key){
    	if(cachePreferences.getInt(key, 0)+CACHE_TIME>getTimeStamp()){
    		return true;
    	}
		return false;
    }
    
    public int getTimeStamp(){
    	Long tsLong = System.currentTimeMillis()/1000;
    	int tsInt = Integer.parseInt(tsLong.toString());
    	return tsInt;
    }

	
     
}
