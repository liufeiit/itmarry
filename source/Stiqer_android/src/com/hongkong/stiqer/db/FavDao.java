package com.hongkong.stiqer.db;

import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.entity.Fav;
import com.hongkong.stiqer.entity.Friend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavDao {
    private DBHelper        helper;
    private SQLiteDatabase  db;
    public FavDao(Context context){
    	helper = new DBHelper(context);
    	db = helper.getWritableDatabase();
    }

    public void add(List<Fav> favs) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (Fav fav : favs) {  
            	db.execSQL("INSERT INTO favorite VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{fav.getFavor_id(),fav.getFavor_type(), fav.getStore_id(), fav.getStore_name(),fav.getStore_img(),fav.getStore_type(), fav.getPromo_id(), fav.getPromo_img(), fav.getPromo_des()});  
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    } 

    public void deleteAll() {  
    	db.execSQL("DELETE FROM favorite"); 
    }
    
    public List<Fav> query(int type, String keyword) {  
        ArrayList<Fav> favs = new ArrayList<Fav>();  
        Cursor c = queryTheCursor(type,keyword);  
        while (c.moveToNext()) {  

        	Fav fav = new Fav();  
        	fav.setFavor_id(c.getString(1));
        	fav.setFavor_type(c.getInt(2));
        	fav.setStore_id(c.getString(3));
        	fav.setStore_name(c.getString(4));
        	fav.setStore_img(c.getString(5));
        	fav.setStore_type(c.getInt(6));
        	fav.setPromo_id(c.getString(7));  
        	fav.setPromo_img(c.getString(8));
        	fav.setPromo_des(c.getString(9));
            favs.add(fav);  
        }  
        c.close();  
        return favs;  
    }  
    
    public Cursor queryTheCursor(int type,String keyword) {
        Cursor c;
        if(keyword.equals("")){
        	 c = db.rawQuery("SELECT * FROM favorite where favor_type = ?", new String[]{String.valueOf(type)});
        }else{
        	String current_sql_sel = "SELECT  * FROM favorite where favor_type = "+ type +" and store_name like '%"+keyword+"%'";
        	c = db.rawQuery(current_sql_sel, null);
        }
		return c;  
    } 
    
    public void closeDB() {  
        db.close();  
    }
}
