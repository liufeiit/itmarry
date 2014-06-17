package com.hongkong.stiqer.db;

import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.entity.AddFriend;
import com.hongkong.stiqer.entity.UpdateFriend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddFriendDao {
    private DBHelper        helper;
    private SQLiteDatabase  db;
    
    public AddFriendDao(Context context){
    	helper = new DBHelper(context);
    	db = helper.getWritableDatabase();
    }

    public void add(List<AddFriend> friends) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (AddFriend friend : friends) {  
                db.execSQL("INSERT INTO addfriend VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{friend.getType(), friend.getUsername(), friend.getRawfid(), friend.getName(), friend.getSortKey(), friend.getAvatar(), friend.getStatus(), friend.getUser_level()});  
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    } 
    
    public void deleteOneAddFriend(AddFriend friend) {  
        db.delete("addfriend", "rowfid = ? and type = ?", new String[]{friend.getRawfid(),String.valueOf(friend.getType())});  
    }
    
    public void deleteAll(int type) {  
    	db.execSQL("DELETE FROM addfriend where type="+type); 
    }
    
    public void update(List<UpdateFriend> updateList, int type) {
    	for (UpdateFriend udpate : updateList) {  
    		ContentValues cv = new ContentValues(); 
    		cv.put("username", udpate.getUsername());
    		cv.put("user_level", udpate.getUser_level());
  
    		cv.put("status",udpate.getFriend_state());
    		db.update("addfriend", cv, "rawfid = ? and type = ?", new String[]{udpate.getExt_uid(),String.valueOf(type)});
    	}  
	}
    
    public List<AddFriend> query(int type) {  
        ArrayList<AddFriend> addfriends = new ArrayList<AddFriend>();  
        Cursor c = queryTheCursor(type);  
        while (c.moveToNext()) {  
        	AddFriend addfriend = new AddFriend();
        	
        	addfriend.setType(c.getInt(c.getColumnIndex("type")));
        	addfriend.setUsername(c.getString(c.getColumnIndex("username")));
        	addfriend.setRawfid(c.getString(c.getColumnIndex("rawfid")));
        	addfriend.setName(c.getString(c.getColumnIndex("name")));
        	addfriend.setSortKey(c.getString(c.getColumnIndex("sortKey")));
        	addfriend.setAvatar(c.getString(c.getColumnIndex("avatar")));
        	addfriend.setStatus(c.getInt(c.getColumnIndex("status")));
        	addfriend.setUser_level(c.getInt(c.getColumnIndex("user_level")));  
        	
            addfriends.add(addfriend);  
        }  
        c.close();  
        return addfriends;  
    }  
    
    public Cursor queryTheCursor(int type) {  
        Cursor c = db.rawQuery("SELECT * FROM addfriend where type="+type, null);  
        return c;  
    } 
    
    public void closeDB() {  
        db.close();  
    }

	
}
