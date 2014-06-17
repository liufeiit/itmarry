package com.hongkong.stiqer.db;

import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.entity.Friend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FriendDao {
    private DBHelper        helper;
    private SQLiteDatabase  db;
    
    public FriendDao(Context context){
    	helper = new DBHelper(context);
    	db = helper.getWritableDatabase();
    }

    public void add(List<Friend> friends) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (Friend friend : friends) {  
                db.execSQL("INSERT INTO friend VALUES(null, ?, ?, ?, ?)", new Object[]{friend.getUsername(), friend.getUser_index(), friend.getProfile_img(), friend.getUser_level()});  
            }
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    } 
    
    public void deleteAll() {  
    	db.execSQL("DELETE FROM friend"); 
    }
    
    public List<Friend> query() {  
        ArrayList<Friend> friends = new ArrayList<Friend>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
        	Friend friend = new Friend();  
            friend.setUsername(c.getString(c.getColumnIndex("username")));
            friend.setUser_index(c.getString(c.getColumnIndex("user_index")));
            friend.setUser_level(c.getInt(c.getColumnIndex("user_level")));
            friend.setProfile_img(c.getString(c.getColumnIndex("profile_img")));
            friends.add(friend);  
        }  
        c.close();  
        return friends;
    }  
    
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM friend", null);  
        return c;  
    } 
    
    public Cursor search(String searchStr){
    	String current_sql_sel = "SELECT  * FROM friend where username like '%"+searchStr+"%'";  
        Cursor c = db.rawQuery(current_sql_sel, null); 
        return c;
    }
    
    public void closeDB() {  
        db.close();  
    }
}
