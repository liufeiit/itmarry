package com.ze.familydayverlenovo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class WidgetLoaderBitmapMrg {
	public final static String WIDGET_PIC_CACHE_PATH = Environment.getExternalStorageDirectory() 
			+ "/familydayVerPm/widgetPic_cache/";
	/**
	 * 内存图片缓存
	 */
	 private HashMap<String, SoftReference<Bitmap>> imageCache = null;
	 private static WidgetLoaderBitmapMrg instance = null;
	 private WidgetLoaderBitmapMrg(){
		 /*File dir = new File(WIDGET_PIC_CACHE_PATH);
		 
		 if ( !dir.exists()) {
			 dir.mkdirs();
		 }*/
		 imageCache = new HashMap<String, SoftReference<Bitmap>>();
	 }
	 public static WidgetLoaderBitmapMrg getInstance(){
		 File dir = new File(WIDGET_PIC_CACHE_PATH);
		 
		 if ( !dir.exists()) {
			 dir.mkdirs();
		 }else{
			 Log.v("widgetcache", "" + dir.list().length);
			 if (dir.isDirectory() && dir.list().length > 100){
				 deleteDir(dir);
			 }
			 if (!dir.mkdirs())
			 	dir.mkdirs();
		 }
		 if (instance == null)
			 instance = new WidgetLoaderBitmapMrg();
		 return instance;
	 }
	 public Bitmap LoadBitmap(final String imgUrl){
		 Bitmap bm = null;
		 //在内存缓存中查找
		 if (imageCache.containsKey(imgUrl)){
			 SoftReference<Bitmap> reference = imageCache.get(imgUrl);
			 bm = reference.get();
			 if (bm != null){
				 Log.v("cache", "Find image in memory!");
				 return bm;
			 }
		 }
		 //在本地缓存中查找
		 String fileName = getMD5Str(imgUrl);
		 if (fileName != null){
			String filePath = WIDGET_PIC_CACHE_PATH + "/" + fileName;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
				fis = null;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (fis != null)
				bm = BitmapFactory.decodeStream(fis);
		 }
		 if (bm != null){
			 Log.v("cache", "Find image in local!");
			 return bm;
		 }
		 Log.v("cache", "No find image in cache!");
		 return null;
	 }
	 
	 public void putBitmapToCache(String picUrl, Bitmap bitmap){
		try{
			String fileName = getMD5Str(picUrl);
			String filePath = WIDGET_PIC_CACHE_PATH + "/" +fileName;
			FileOutputStream fos = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		}catch(Exception e){
			e.printStackTrace();
		}
		imageCache.put(picUrl, new SoftReference<Bitmap>(bitmap));
	 }
	 
	 /**
	  * 删除缓存
	  */
	 private static boolean deleteDir (File file){
		 boolean success = true;
		 if (file.exists()){
			 File [] list = file.listFiles();
			 if (list != null){
				 int length = list.length;
				 for (int i = 0; i < length; i ++){
					 if (list[i].isDirectory())
						 deleteDir(list[i]);
					 else{
						 boolean ret = list[i].delete();
						 if ( !ret )
							 success = false;
					 }
				 }
			 }
		 }else
			 success = false;
		 if (success)
			 file.delete();
		 return success;
	 }
	 
	 /**  
     * MD5 加密  
     */   
	 private static String getMD5Str(String str) {   
		 MessageDigest messageDigest = null;   
		 try {   
			 messageDigest = MessageDigest.getInstance("MD5");   
			 messageDigest.reset();   
			 messageDigest.update(str.getBytes("UTF-8"));   
		 } catch (NoSuchAlgorithmException e) {   
			 System.out.println("NoSuchAlgorithmException caught!");   
			 return null;
		 } catch (UnsupportedEncodingException e) {   
			 e.printStackTrace();
			 return null;
		 }   
	   
		 byte[] byteArray = messageDigest.digest();   
		 StringBuffer md5StrBuff = new StringBuffer();   
		 for (int i = 0; i < byteArray.length; i++) {               
			 if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)   
				 md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));   
			 else   
				 md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));   
		 }   
		 return md5StrBuff.toString();   
	 }
}
