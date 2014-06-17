package com.jingtuo.android.common.app;


import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class BaseActivity extends Activity{
	
	public static final int REQUEST_CODE_TAKE_PICTURE_WITH_SYSTEM = 0;
	
	public static final String KEY_TAKE_PICTURE_PATH = "take picture path";
	
	protected SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mPreferences = getPreferences(Context.MODE_PRIVATE);
		super.onCreate(savedInstanceState);
	}

	/**
	 * 调用系统相机进行照相<br>
	 * 未指定拍摄照片的存储位置时,系统不会存储拍摄的照片,可通过getBitmap获取拍摄的照片的缩略图icon
	 * 指定拍摄照片的存储位置时,系统将拍摄的照片存储到指定的位置,可通过getBitmap获取拍摄的照片的缩略图icon
	 * @param pathName
	 */
	protected void takePicture(String pathName){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {//避免没有对返回结果进行处理导致应用异常
			takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if(pathName==null||pathName.equals("")){
				startActivityForResult(takePictureIntent,REQUEST_CODE_TAKE_PICTURE_WITH_SYSTEM);
			}else{
				File file = new File(pathName);
				if (file.exists()) {
					file.delete();
				}
				try {
					if (file.createNewFile()) {
						mPreferences.edit().putString(KEY_TAKE_PICTURE_PATH, pathName).commit();
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
						startActivityForResult(takePictureIntent,REQUEST_CODE_TAKE_PICTURE_WITH_SYSTEM);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	protected Bitmap getBitmap(Intent data) {
		Bitmap bitmap = null;
		 if(data!=null){
			 Bundle extras = data.getExtras();
			 if(extras!=null){
				 bitmap = (Bitmap) extras.get("data");
			 }
		 }
	     return bitmap;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getTakePicturePathName() {
		return mPreferences.getString(KEY_TAKE_PICTURE_PATH, "");
	}
	
	
}
