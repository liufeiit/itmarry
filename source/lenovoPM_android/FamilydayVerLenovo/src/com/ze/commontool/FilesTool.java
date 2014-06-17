/**
  * Copyright 2012 PoQoP
  * Created on 2012-2-3 下午03:56:06
  */
package com.ze.commontool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * @author huaxin
 * FilesTool.java
 */
public class FilesTool {
	/**
	 * 获取assets文件夹下的图片资源
	 * @param context， Activity的上下文变量
	 * @param fileName，文件路径名
	 * @return
	 */
	public final static String 		ROOT_PATH = Environment.getExternalStorageDirectory() + "/family/";
	public final static String 		ICON_CAPTURE = "familyicon_capture.jpg";
	public final static String 		ICON_UPLOAD = "familyicon_upload.jpg";
	public final static String 		SPACE_BACKGROUND = "familyspace_background.jpg.backup";
	
	public final static String 		SETTING_AVATAR = "setting_avatar.jpg.backup";
	
	public final static String 		CAMERA_CAPTURE_PATH = ROOT_PATH + "image/";
	
	
	public static Bitmap getImageFromAssetFile(Context context, String fileName){   
	    Bitmap image = null;   
	    try{   
	        AssetManager am = context.getAssets();   
	        InputStream is = am.open(fileName);   
	        image = BitmapFactory.decodeStream(is);   
	        is.close();   
	    }catch(Exception e){   
	           
	    }   
	    return image;   
	} 
	
	/**
	 * 缓存文件
	 * @param path
	 * @param saveDir
	 * @return
	 * @throws Exception
	 */
	public static Uri cacheFile(String path, String saveDir)throws Exception{
		Log.i("FilesTool cacheFile", path);
		File tempDirectory = new File(saveDir);
		if(!tempDirectory.exists()){
			tempDirectory.mkdir();
		}
		File file = new File(saveDir, MD5.MD5Encode(path)+ path.substring(path.lastIndexOf(".")));
		if(file.exists()){
			return Uri.fromFile(file);
		}else{
			file.createNewFile();
			FileOutputStream outStream = new FileOutputStream(file);
			HttpURLConnection conn = (HttpURLConnection)new URL(path).openConnection();
			conn.setConnectTimeout(5 * 1000); 
			conn.setRequestMethod("GET");
			if(conn.getResponseCode()==200){
				BitmapFactory.Options options = new BitmapFactory.Options(); 
				options.inJustDecodeBounds = false;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inPurgeable = true;
				options.inInputShareable = true;
				
				BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
				Bitmap bitmap = BitmapFactory.decodeStream(buf, null, options);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, baos);
				byte[] drawableArray = baos.toByteArray();
				
				outStream.write(drawableArray);
				
				/*InputStream inStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) !=-1 ){
					outStream.write(buffer, 0, len);
				}*/
				
				outStream.close();
				//inStream.close();
				baos.close();
				buf.close();
				return Uri.fromFile(file);
			}else{
				throw new Exception("文件下载失败！");
			}
		}
	}

	/**
	 * 获取服务器上的图片资源
	 * @param context， Activity的上下文变量
	 * @param fileName，文件路径名
	 * @return
	 */
	public static Bitmap getImageFromWebFile(Context context, String fileName){
		Log.i("Path", fileName);
	    Bitmap image = null;
	    try {
			Uri uri = FilesTool.cacheFile(fileName, ROOT_PATH);
			InputStream is = context.getContentResolver().openInputStream(uri);
			image = BitmapFactory.decodeStream(is);
			if(image == null){
				return null;
			}
			double ratio =  1.0 * image.getHeight() / image.getWidth();
	        Bitmap temp = Bitmap.createScaledBitmap(image, 300, (int)(300 * ratio), true);
	        is.close(); 
	        image.recycle();
	        return temp;
		} catch (Exception e) {
			e.printStackTrace();
			FilesTool.deleteCache(fileName, ROOT_PATH);
		}
	    return image;   
	}
	
	/** 
	 * 清除空的缓存文件
	 */
	public static boolean deleteCache(String path, String saveDir){
		boolean stat = false;
		if(path == null || path.trim().equals("")){
			return stat;
		}
		if(path.lastIndexOf('.') < 0){
			return stat;
		}
		File file = new File(saveDir, MD5.MD5Encode(path)+ path.substring(path.lastIndexOf('.')));
		if(file.exists()){
			stat = file.delete();
		}
		return stat;
	}
	
	/**
	 * 如题
	 * @param ctx
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapByUrlCache(Context ctx, String url){
    	Bitmap tempBMP = null;
    	try {
			Uri uri = FilesTool.cacheFile(url, ROOT_PATH);
			InputStream is = ctx.getContentResolver().openInputStream(uri);
			tempBMP = BitmapFactory.decodeStream(is);
			return tempBMP;
		} catch (Exception e) {
			e.printStackTrace();
			FilesTool.deleteCache(url, ROOT_PATH);
		}
		return tempBMP;
    }

	/**
	 * 从文件中加载config
	 */
	public static Properties load(Context ctx){
		Properties properties = new Properties();
		try {
			File file = new File("/data/data/com.huaxin.wbleague/files/config.cfg");
			if(!file.exists()){
				return null;
			}
			FileInputStream stream = ((Activity)ctx).openFileInput("config.cfg");
			properties.load(stream);
		} catch (Exception e) {
			Log.i("Err", e.toString());
		}
		return properties;
	}
	
	/**
	 * 保存配置信息，账号等
	 * @param ctx
	 * @param dataSaved
	 * @return
	 */
	public static boolean saveData(Context ctx, String userName, String password){
		Properties properties = new Properties();
		properties.put("username", userName);
		properties.put("password", password);
		try {
			FileOutputStream stream = ((Activity)ctx).openFileOutput("config.cfg", 
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "config");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

	/**
	 * 保存collectList于文件中
	 * @return 是否保存成功
	 */
	public static boolean saveData(Context ctx, List<Map<String, Object>> dataSaved){
		Properties properties = new Properties();
		JSONObject data = listToJs(dataSaved);
		properties.put("collectData", data.toString());
		try {
			FileOutputStream stream = ((Activity)ctx).openFileOutput("collect.cfg", 
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "collectList");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 从文件中加载config.cfg
	 * ctx, Activity context
	 * fileName, 文件名, 类似于 config.cfg
	 */
	public static Properties load(Context ctx, String fileName){
		Properties properties = new Properties();
		try {
			File file = new File("/data/data/com.huaxin.wbleague/files/" + fileName);
			if(!file.exists()){
				return null;
			}
			FileInputStream stream = ((Activity)ctx).openFileInput(fileName);
			properties.load(stream);
		} catch (Exception e) {
			Log.i("Err", e.toString());
		}
		return properties;
	}
	
	/**
	 * 保存配置信息，账号等
	 * @param ctx
	 * @param data, 哈希列表,
	 * @param fileName, 文件名, 类似于 config.cfg
	 * @return
	 */
	public static boolean saveData(Context ctx, Map<String, String> data, String fileName){
		Iterator<String> keys = data.keySet().iterator();
		Properties properties = new Properties();
		String key;
		while(keys.hasNext()){
			key = keys.next();
			properties.put(key, data.get(key));
		}
		try {
			FileOutputStream stream = ((Activity)ctx).openFileOutput(fileName, 
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "temp file");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	/**
	 * 将数据转化为json
	 * @param dataSaved, 要转化的列表数据
	 * @return
	 */
	public static JSONObject listToJs(List<Map<String, Object>> dataSaved){
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject temp;
		
		try{
			for(int i=0; i<dataSaved.size(); i++){
				temp = new JSONObject();
				temp.put("xiaoquName", (String)dataSaved.get(i).get("xiaoquName"));
				temp.put("xiaoquid", (Integer)dataSaved.get(i).get("xiaoquid"));
				temp.put("listImagePath", (String)dataSaved.get(i).get("listImagePath"));
				temp.put("listContent", (String)dataSaved.get(i).get("listContent"));
				temp.put("listDate", (Integer)dataSaved.get(i).get("listDate"));
				temp.put("360num", (Integer)dataSaved.get(i).get("360num"));
				temp.put("salestatus", (Integer)dataSaved.get(i).get("salestatus"));
				temp.put("lat", (Double)dataSaved.get(i).get("lat"));
				temp.put("lng", (Double)dataSaved.get(i).get("lng"));
				array.put(temp);
			}
			result.put("collectList", array);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static ArrayList<Map<String, Object>> jsToList(JSONObject data){
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> temp;
		try{
			JSONArray array = data.getJSONArray("collectList");
			JSONObject tempObj;
			if(array!=null){
				for(int i=0; i<array.length(); i++){
					temp = new HashMap<String, Object>();
					tempObj = array.getJSONObject(i);
					temp.put("xiaoquName", tempObj.getString("xiaoquName"));
					temp.put("xiaoquid", tempObj.getInt("xiaoquid"));
					temp.put("listImagePath", tempObj.getString("listImagePath"));
					temp.put("listContent", tempObj.getString("listContent"));
					temp.put("listDate", tempObj.getInt("listDate"));
					temp.put("360num", tempObj.getInt("360num"));
					temp.put("salestatus", tempObj.getInt("salestatus"));
					temp.put("lat", tempObj.getDouble("lat"));
					temp.put("lng", tempObj.getDouble("lng"));
					result.add(temp);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 保存图片文件于sd卡上
	 * @param imageId
	 * @param filePath
	 * @param ctx
	 * @return
	 */
	public static boolean saveFile(int imageId, String filePath, Context ctx){
		boolean status = false;
		
		Bitmap tempBitmap = BitmapFactory.decodeResource(ctx.getResources(), imageId);
		File tempDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/")));
		if(!tempDirectory.exists()){
			tempDirectory.mkdir();
		}
		File tempFile = new File(filePath);
		try{
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			tempBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			status = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 压缩图片
	 * @param path
	 * @return
	 */
	public static boolean cropImage(String path){
		boolean status = false;
		/*BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inSampleSize = 2;
		Bitmap temp = BitmapFactory.decodeFile(path,options);*/
		Bitmap temp = scaleImage(path);
		Bitmap bmp = Bitmap.createScaledBitmap(temp, 400, 
				400 * temp.getHeight() / temp.getWidth(), true);
		temp.recycle();
		File tempFile = new File(path);
		try{
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			bmp.recycle();
			status = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 缩小图片  防止OOM
	 * @param path
	 * @return
	 */
	public static Bitmap scaleImage(String path){
		File file = new File(path);
		if (file.exists()) {
			int size = (int)file.length()/(1024*1024);
			BitmapFactory.Options options = new BitmapFactory.Options(); 
			if (size == 2) {
				size = 3;
			}
			if ((size == 0||size == 1) && file.length() > 512*1024) {
				size = 2;
			}
			options.inSampleSize = size!=0?size:1;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			
			try {
				return BitmapFactory.decodeStream(new FileInputStream(file), 
						null, options);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			//return BitmapFactory.decodeFile(path,options);
		}
		return null;
	}
	/**
	 * 旋转图片
	 * @param path
	 * @return
	 */
	public static boolean rotateImage(String path, int degree){
		if(degree == 0){
			return true;
		}
		boolean status = false;
		Bitmap tempBitmap;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4; //1/4 of the original image

		tempBitmap=BitmapFactory.decodeFile(path, options);
		int tempW = tempBitmap.getWidth();
		int tempH = tempBitmap.getHeight();
		Log.i("WWHH", tempW + ":" + tempH);

		if (tempW > tempH) {
		    Matrix mtx = new Matrix();
		    mtx.postRotate(degree);
		    Bitmap rotatedBitmap = Bitmap.createBitmap(tempBitmap, 
		    		0,0,tempW, tempH, mtx, true);
		    tempBitmap.recycle();
		    File tempFile = new File(path);
			try{
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
				rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
				rotatedBitmap.recycle();
				status = true;
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return status;
	}
	
	/** 保存 bitmap 图片 */
	public static  boolean cacheBMP(Bitmap bmp, String path){
		boolean status = false;
		
		if (path.lastIndexOf("/") != -1) {
			File directory = new File(path.substring(0, path.lastIndexOf("/")+1));
			if(!directory.exists()){
				directory.mkdir();
			}
		}
		
		File tempFile = new File(path);
		if (!tempFile.exists()) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try{
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			status = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return status;
	}
	
	/** 保存 bitmap 图片 */
	public static  String cacheBMP(Bitmap bmp){
		//boolean status = false;
		String picPath = "";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String path = CAMERA_CAPTURE_PATH 
				+ format.format(new Date(System.currentTimeMillis())) + ".jpg";
		
		if (path.lastIndexOf("/") != -1) {
			File directory = new File(path.substring(0, path.lastIndexOf("/")+1));
			if(!directory.exists()){
				directory.mkdir();
			}
		}
		
		File tempFile = new File(path);
		if (!tempFile.exists()) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try{
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			//status = true;
			picPath = path;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return picPath;
	}
	
	
	public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e("EXIF", "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }
}
