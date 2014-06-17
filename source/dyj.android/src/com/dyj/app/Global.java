package com.dyj.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.dyj.push.Utils;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Global {
	private static Global instance = null;
	public ArrayList<Cursor> cursors;
	private static SharedPreferences sharedPreferences = null;
	private static Application APP = null;

	public static Global getInstance(String user_id) {
		Log.d("user_id==============", user_id);
		if (null == instance) {
			instance = new Global();
		}
		Global.sharedPreferences = Global.APP.getSharedPreferences(user_id,
				Context.MODE_PRIVATE);
		return instance;
	}

	public static Global getInstance() {
		if (null == instance) {
			instance = new Global();

		}
		return instance;
	}

	/*
	 * public static Global getInstance() { if (null == instance) { instance =
	 * new Global();
	 * 
	 * } instance.sharedPreferences=instance.APP.getSharedPreferences("0",
	 * Context.MODE_PRIVATE); return instance; }
	 */

	// 是否本地版本开关
	public boolean isDemo() {
		return false;
	}

	public String getUserPhotoUrl(String path) {
		return path;
	}

	public void clear() {
		sharedPreferences.edit().clear().commit();
	}

	public static String getUUID() {
		String id = UUID.randomUUID().toString();
		id = id.replace("-", "");
		return id;
	}

	public static boolean CheckNetwork(Context context) {

		boolean flag = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null)
			flag = cwjManager.getActiveNetworkInfo().isAvailable();
		if (!flag) {
			Toast.makeText(context, "提示，当前网络连接不可用!", 1).show();
		}
		return flag;
	}

	public static long dateTime2Long(String dateline) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		java.util.Date d;
		try {
			d = dfs.parse(dateline);
			return d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	// 获得时间差
	public static boolean getDisTime(String time1, String time2) {
		if (time1 == null || time2 == null) {
			return false;
		}
		if (time1 == "" || time2 == "") {
			return false;
		}
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

		java.util.Date begin;
		try {
			begin = dfs.parse(time1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		java.util.Date end;
		try {
			end = dfs.parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		long l = end.getTime() - begin.getTime();

		long day = l / (24 * 60 * 60 * 1000);

		long hour = (l / (60 * 60 * 1000) - day * 24);

		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		if (min >= 20) {
			return true;
		}
		return false;
	}

	public void putCursor(Cursor cursor) {

		cursors.add(cursor);

	}

	public void closeCursors() {
		for (int i = 0; i < cursors.size(); i++) {
			if (cursors.get(i) != null && !cursors.get(i).equals(null)) {
				if (cursors instanceof Cursor) {
					cursors.get(i).close();
				}
			}
		}
	}

	public void remove(String key) {
		if (key == null) {
			return;
		} else {
			sharedPreferences.edit().remove(key).commit();
		}
	}

	public Global save(String key, String value) {
		if (key != null) {
			if (value == null) {
				sharedPreferences.edit().putString(key, "").commit();
			} else {
				sharedPreferences.edit().putString(key, value).commit();

			}
		}
		return this;
	}

	public String read(String key) {

		if (sharedPreferences.contains(key)) {
			return sharedPreferences.getString(key, "");

		}
		return "";
	}

	public ContentValues getContentValues() {
		Map<String, ?> values = sharedPreferences.getAll();
		ContentValues contentValues = new ContentValues();
		for (Map.Entry<String, ?> e : values.entrySet()) {
			if (!"user_photo".equals(e.getKey())
					&& !"userPhotoUri".equals(e.getKey())
					&& !"shiji_input".equals(e.getKey())
					&& !"zhuchenshiji_input".equals(e.getKey())
					&& !"xuexishiji_input".equals(e.getKey())
					&& !"bu_shiji_input".equals(e.getKey())) {
				contentValues.put(e.getKey(), e.getValue().toString());
			}

		}
		return contentValues;
	}

	public void onCreate(Application app) {
		cursors = new ArrayList<Cursor>();

		Global.APP = app;
		
	}

	public String getIntByStr(String str) {
		str = str.trim();
		String str2 = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		return str2;
	}

	public void setRwBeanValue() {
		Class<?> demo = null;
		Object obj = null;
		try {
			demo = Class.forName("com.test.bean");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			obj = demo.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field[] fileds = demo.getDeclaredFields();

	}

	public ArrayList<Bitmap> getStringToBitmapList(String strList) {
		ArrayList<Bitmap> zplist = new ArrayList<Bitmap>();
		ArrayList<Object> list = new ArrayList<Object>();
		Log.d("str=========", strList);
		byte[] buffer = Base64.decodeBase64(strList.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			list = (ArrayList<Object>) ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bais.close();
				if (list.isEmpty()) {
					return null;
				}
				for (int i = 0; i < list.size(); i++) {
					byte[] base64Bytes = Base64.decodeBase64(list.get(i)
							.toString().getBytes());
					bais = new ByteArrayInputStream(base64Bytes);
					zplist.add(BitmapFactory.decodeByteArray(base64Bytes, 0,
							base64Bytes.length));
				}
				return zplist;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public String getBitmapListToString(ArrayList<Bitmap> zplist) {
		List<Object> list = new ArrayList<Object>();
		ByteArrayOutputStream baos;
		for (int i = 0; i < zplist.size(); i++) {
			baos = new ByteArrayOutputStream();
			zplist.get(i).compress(CompressFormat.JPEG, 100, baos);
			String imageBase64 = new String(Base64.encodeBase64(baos
					.toByteArray()));
			list.add(imageBase64);
		}
		if (list.isEmpty()) {
			return null;
		} else {
			baos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(baos);
				oos.writeObject(list);
				String strList = new String(Base64.encodeBase64(baos
						.toByteArray()));
				return strList;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

	}
	

	

}
