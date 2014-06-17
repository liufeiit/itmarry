package com.jingtuo.android.common;

import java.io.File;

import com.jingtuo.android.common.utils.ImageUtils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;

public class CacheManager {

	private LruCache<String, Bitmap> lruCache;
	
	private File diskCache;
	
	public CacheManager(String appName) {
		// TODO Auto-generated constructor stub
		int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);

	    // Use 1/8th of the available memory for this memory cache.
		int cacheSize = maxMemory / 8;
		lruCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return ImageUtils.getBytes(value)/1024;
			}
		};
		if(Environment.getExternalStorageDirectory()!=null){
			String path = "";
			if(appName==null||appName.equals("")){
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp";
			}else{
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + appName + "/temp";
			}
			diskCache = new File(path);
			if(!diskCache.exists()){
				diskCache.mkdirs();
			}
		}
	}
	
	/**
	 * 将Bitmap放入缓存
	 * @param key
	 * @param value
	 */
	public void put(String key, Bitmap value){
		lruCache.put(key, value);
	}
	
	/**
	 * 从缓存中读取Bitmap
	 * @param key
	 * @return
	 */
	public Bitmap get(String key){
		return lruCache.get(key);
	}
	
	
}
