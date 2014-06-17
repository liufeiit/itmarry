package com.hongkong.stiqer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "stiqer.db";  
    private static final int DATABASE_VERSION = 22;  
      
    public DBHelper(Context context) {  
        //CursorFactory设置为null,使用默认值  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }  
  
    @Override  
    public void onCreate(SQLiteDatabase db) {
    	
    	db.execSQL("DROP TABLE IF EXISTS addfriend");
    	db.execSQL("CREATE TABLE IF NOT EXISTS addfriend" +  
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, username VARCHAR, rawfid VARCHAR, name VARCHAR, sortKey VARCHAR, avatar VARCHAR, status INTEGER, user_level INTEGER)");
    	
    	db.execSQL("DROP TABLE IF EXISTS friend");
    	db.execSQL("CREATE TABLE IF NOT EXISTS friend" + 
    	          "(_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, user_index VARCHAR,profile_img VARCHAR,user_level INTEGER)");
    	
    	db.execSQL("DROP TABLE IF EXISTS favorite");
    	db.execSQL("CREATE TABLE IF NOT EXISTS favorite" +  
   	         "(_id INTEGER PRIMARY KEY AUTOINCREMENT, favor_id VARCHAR, favor_type INTEGER, store_id VARCHAR, store_name VARCHAR, store_img VARCHAR, store_type INTEGER, promo_id VARCHAR, promo_img VARCHAR, promo_des VARCHAR)");
    
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	db.execSQL("DROP TABLE IF EXISTS addfriend");
    	db.execSQL("CREATE TABLE IF NOT EXISTS addfriend" +  
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, username VARCHAR, rawfid VARCHAR, name VARCHAR, sortKey VARCHAR, avatar VARCHAR, status INTEGER, user_level INTEGER)");
    }

}
