package com.hongkong.stiqer.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Avatar;
import com.hongkong.stiqer.entity.PSetting;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.GetImagePath;
import com.hongkong.stiqer.utils.ImageTools;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.squareup.picasso.Picasso;

import eu.janmuller.android.simplecropimage.CropImage;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


public class PSettingActivity extends BaseActivity {
	public static final int  USERNAME_EXIST = 1004;
	public static final int  AVATAR_UPLOAD_SUCCESS = 1000;
	public static final int  PROFILE_COMPLETE_SUCCESS = 1008;
	public static final int  TOKEN_EXPIRES = 1009;
	public static final String     CACHE_NAME = "profile";
	static final String[] string_year={"1956","1957","1958","1959","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969","1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014"};  
    static final String[] string_month={"MONTH","1","2","3","4","5","6","7","8","9","10","11","12"};  
    static final String[] string_day={"DAY","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};  
    static final String[] string_sex={"CHOOSE","Male","Female"};
    private static final int TAKE_PICTURE = 0;
	private static final int  CHOOSE_PICTURE = 1;
	private static final int  GALLERY_CROP = 2;
	private static final int  CROP_PICTURE = 3;
	private static final int  PICTURE_CROP = 4;//照片缩小比例
	private static final int  PROFILE_SETTING = 111;
	private static final int   DEFAULT_RESIZE = 600;
	private static final String  PICTURE_NAME = "pre_avatar";
	Avatar  avatar;
	String avatarPath          =   Environment.getExternalStorageDirectory().getPath() + "/avatar.jpg";
	Button             BtnSubmit,btnBack,btnCancel;
	CustomDialog       progressDialog;
	SUser               user;
	EditText           ps_name,ps_username;
    private Spinner reg_day,reg_month,reg_year,sex;  
    ImageView  photo_image;
    private ArrayAdapter<String> day_adapter,month_adapter,year_adapter,sex_adapter;  
    Activity mActivity;
    CacheHelper cacheHelper;
    ErrorCodeHelper errorHelper;
    ProgressBar  progressBar;
	LinearLayout ps_wrap;
	String       profile_img;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_psetting);
	    initView();
	    new GetDataTask().execute();
	}
	
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "32YTPN6M7T4TW3DH38P5");
	}
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	
    class BtnClickListener implements OnClickListener{ 
		 public void onClick(View v) {

			 if(ps_name.getText().toString().equals("")){
				 showToast("Name can not be empty");
				 return;
			 }
			 if(ps_username.getText().toString().equals("")){
				 showToast("UserName can not be empty");
				 return;
			 }
			 if(reg_day.getSelectedItemId()==0 || reg_month.getSelectedItemId()==0 || reg_year.getSelectedItemId()==0){
				 showToast("Please fill your birthday");
				 return;
			 }
			 if(sex.getSelectedItemId()==0){
				 showToast("Please choose your gender");
				 return;
			 }
			 
			 progressDialog = CustomDialog.createProgressDialog(PSettingActivity.this,"Information updating...");
             progressDialog.show();
				//注册过程   
             new Thread(){
             	public void run(){
             		
             		user = APIManager.sharedInstance().lcomplete(ps_name.getText().toString(),ps_username.getText().toString(),
           				  (int)reg_day.getSelectedItemId(),(int)reg_month.getSelectedItemId(),((int)reg_year.getSelectedItemId()+1956),
           				  (int)sex.getSelectedItemId(),"","");
               		 Message msg = mHandler.obtainMessage();
                     msg.what = user.getError_code();
                     mHandler.sendMessage(msg);
             	}
             }.start();
		}
	 }
	
	 private void initView() {
    	 reg_day=(Spinner) findViewById(R.id.sp_reg_day);   
         day_adapter=new ArrayAdapter<String>(this,R.layout.profile_spinner_item,string_day);  
         day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         reg_day.setAdapter(day_adapter);  
         
         reg_month=(Spinner) findViewById(R.id.sp_reg_month);   
         month_adapter=new ArrayAdapter<String>(this,R.layout.profile_spinner_item,string_month);  
         month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         reg_month.setAdapter(month_adapter);  
         
         reg_year=(Spinner) findViewById(R.id.sp_reg_year);   
         year_adapter=new ArrayAdapter<String>(this,R.layout.profile_spinner_item,string_year);  
         year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         reg_year.setAdapter(year_adapter); 
         
         sex=(Spinner) findViewById(R.id.sp_sex_spinner);   
         sex_adapter=new ArrayAdapter<String>(this,R.layout.profile_spinner_item,string_sex);  
         sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         sex.setAdapter(sex_adapter); 
         
         cacheHelper = new CacheHelper(PSettingActivity.this);
         errorHelper = new ErrorCodeHelper(PSettingActivity.this);
         progressBar = (ProgressBar) findViewById(R.id.ps_progressbar);
         ps_wrap = (LinearLayout) findViewById(R.id.con_wrap);
         ps_name = (EditText) findViewById(R.id.ps_name);
         ps_username = (EditText) findViewById(R.id.ps_username);

         btnBack = (Button) findViewById(R.id.ps_back_btn);
         btnBack.setOnTouchListener(Util.TouchDark);
         btnBack.setOnClickListener(new OnClickListener(){
 			public void onClick(View v) {
 				Intent intent = new Intent();
 				intent.putExtra("profile_img", profile_img);
 				PSettingActivity.this.setResult(PROFILE_SETTING, intent);
 				PSettingActivity.this.finish();
 			}
 		 });

         btnCancel = (Button) findViewById(R.id.sp_cancel_btn);
         btnCancel.setOnTouchListener(Util.TouchDark);
         btnCancel.setOnClickListener(new OnClickListener(){
  			public void onClick(View v) {
  				Intent intent = new Intent();
 				intent.putExtra("profile_img", profile_img);
 				PSettingActivity.this.setResult(PROFILE_SETTING, intent);
  				PSettingActivity.this.finish();
  			}
  		 });
         
        BtnSubmit = (Button) findViewById(R.id.sp_post_btn);
        BtnSubmit.setOnClickListener(new BtnClickListener());
        BtnSubmit.setOnTouchListener(Util.TouchDark);
        
        photo_image = (ImageView) findViewById(R.id.sp_avatar_image);
        photo_image.setOnTouchListener(Util.TouchDark);
        photo_image.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					showPicturePicker(PSettingActivity.this);
				}
		});
	}
	 
	 private class GetDataTask extends AsyncTask<Integer[],Void, PSetting>{
		ErrorCodeHelper errorHelper = new ErrorCodeHelper(PSettingActivity.this);
		@Override
		protected void onPostExecute(PSetting result) {
			if(errorHelper.CommonCode(result.getError_code())){
				Picasso.with(PSettingActivity.this).load(result.getProfile_img()).skipMemoryCache().into(photo_image);
				ps_name.setText(result.getName());
				ps_username.setText(result.getUsername());
				reg_day.setSelection(result.getBirth_day());
				reg_month.setSelection(result.getBirth_month());
				reg_year.setSelection((result.getBirth_year()-1956));
				sex.setSelection(result.getGender());
				ps_wrap.setVisibility(View.VISIBLE);
				profile_img = result.getProfile_img();
			}
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
		}

		@Override
		protected PSetting doInBackground(Integer[]... param) {
			return APIManager.sharedInstance().getProfileSetting();
		}
	 }
	 
	 public void showPicturePicker(Context context){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setNegativeButton("Cancel", null);
			builder.setItems(new String[]{"Take a new picture","From gallery"}, new DialogInterface.OnClickListener() {
				//类型码		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case TAKE_PICTURE:
						Uri imageUri = null;
						String fileName = null;
						Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						//删除上一次截图的临时文件
						SharedPreferences sharedPreferences = getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
						ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
							
						//保存本次截图临时文件名字
						fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
						Editor editor = sharedPreferences.edit();
						editor.putString("tempName", fileName);
						editor.commit();

						imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
						//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(openCameraIntent, PICTURE_CROP);
						break;
						
					case CHOOSE_PICTURE:
						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
						openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(openAlbumIntent, GALLERY_CROP);
						break;

					default:
						break;
					}
				}
			});
			builder.create().show();
	}
	 
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				
				case PICTURE_CROP:
				   String file_name = getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
				   String files = Environment.getExternalStorageDirectory()+"/"+file_name;
				   Bitmap bitmap = ImageTools.getSmallBitmap(files,DEFAULT_RESIZE,DEFAULT_RESIZE);
			       int degree = ImageTools.readPictureDegree(files);
				   if(degree != 0){
					  bitmap = ImageTools.rotateBitmap(bitmap,degree);
				   }
				   ImageTools.savePhotoToSDCard(bitmap, Environment.getExternalStorageDirectory()+"/", PICTURE_NAME);
				   cropImage(Environment.getExternalStorageDirectory()+"/"+PICTURE_NAME+".jpg");
				   break;
				case GALLERY_CROP:
					if (data != null) {
						Uri uri = data.getData();
						GetImagePath getImagePath= new GetImagePath(PSettingActivity.this);
						String filess = getImagePath.getPath(uri);
						cropImage(filess);
					}	
					break;
				
				case CROP_PICTURE:
	                final File f = new File(avatarPath);
		            progressDialog = CustomDialog.createProgressDialog(PSettingActivity.this, "Uploading picture ...");
		            progressDialog.show();
		            new Thread(){
		   			 public void run(){
		   				 avatar = APIManager.sharedInstance().uploadProfileImage(f,"","");
		   				 Message msg = mHandler.obtainMessage();
		                    msg.what = avatar.getError_code();
		                    mHandler.sendMessage(msg);
		   			 }
		   		    }.start();	
		   			Bitmap bitmaps = BitmapFactory.decodeFile(avatarPath);
					photo_image.setImageBitmap(bitmaps);
					break;
				default:
					break;
			}
		}
	 }		
	private Handler mHandler = new Handler()
    {
    	public void handleMessage(Message msg)
        {
    		super.handleMessage(msg);
            progressDialog.cancel();
            if(errorHelper.CommonCode(msg.what)){
    		 switch (msg.what)
             {
              case PROFILE_COMPLETE_SUCCESS:
            	  Util.profile = null;
            	  Toast.makeText(getApplication(), "Modify successful", Toast.LENGTH_SHORT).show();
            	  SharedPreferences sharedPreferences = getSharedPreferences(Util.TAG, 0);
                  Editor editor = sharedPreferences.edit();
                  editor.putString("token", user.getToken());
                  editor.putString("uid", user.getUid());
                  editor.putString("username", user.getUsername());
                  editor.putString("profile_image_url", user.getProfile_img_url());
                  editor.commit();
                  break;
               case USERNAME_EXIST:
                 showToast(getString(R.string.reg_username_exist));
                 break;
               case AVATAR_UPLOAD_SUCCESS:
            	  Util.profile = null;
            	  showToast(getString(R.string.avatar_upload));
            	  profile_img = avatar.getImg_uri();
            	  break;
              }  
            }
        }
    };
    
    public void cropImage(String path){
    	Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, path);
        intent.putExtra(CropImage.OUT_PATH, avatarPath);
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        startActivityForResult(intent, CROP_PICTURE);
    }

}
