package com.hongkong.stiqer.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.AppContext;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Friend;
import com.hongkong.stiqer.entity.ReturnCheckIn;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.GetImagePath;
import com.hongkong.stiqer.utils.ImageTools;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends BaseActivity {
	private static final int   CHECKIN_FIRST_SUCCESS = 1000;
	private static final int   CHECKIN_IMAGE_SUCCESS = 1100;
	private static final int   DEFAULT_RESIZE = 200;
	private static final int   TAKE_PICTURE = 0;
	private static final int   CHOOSE_PICTURE = 1;
	private static final int   TAKE_PICTURE_CODE = 102;
	private static final int   CHOOSE_PICTURE_CODE = 103;
	private static final int   DELETE_IMAGE = 104;
	private static final int   CHOOSE_AT_PERSON = 105;
	private static final int   MOST_IMAGE_NUM = 2;
	private static final int   TEXT_IS_EMPTY = 101;
	
	String           filePath = Environment.getExternalStorageDirectory().getPath() + "/stiqer/";
	LinearLayout     camera_at;
	Integer          now_image_num;
	Button           btnPost,btnBack;
	GridView         camera_gridview;
	ImageAdapter     adapter;
	EditText         checkin_msg;
	TextView         at_names;
	List<Map<String,Object>>     lo;
	int              now_position = 0, choose_position = 0;
	Context          mContext;
	CustomDialog     ShowImageDialog,progressDialog;
	DialogListener   dialogListener;
	List<Friend>     atList = new ArrayList<Friend>();
	String           atString,store_id,store_name,tempFile = "checkin.jpg";
	List<String>     photoList;
	ReturnCheckIn    returnCheckin;
	int              completeImage = 0;
	ErrorCodeHelper  errorHelper;
	ImageView        c_avatar;
	SUser            loginUser;
	GetImagePath     getImagePath;
    AppContext       application; 
	
  	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_camera);
		}catch(Exception e){
			CameraActivity.this.finish();
		}
		
		initUser();
		initView();
	    fisrt_image();
		//addDrawable();
		initPath();
	    initAdapter();
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
	
  	private void initUser() {
		CacheHelper cacheHelper = new CacheHelper(this);
	    loginUser = cacheHelper.GetUser();
	}

	private void initPath() {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private void initView() {
		application = (AppContext)getApplication();  
        application.init();  
        application.addActivity(this);  
        
		camera_gridview = (GridView) findViewById(R.id.camera_gridview);
		btnBack = (Button) findViewById(R.id.camera_back_btn);
		lo = new ArrayList<Map<String,Object>>();
		mContext = this;
		errorHelper = new ErrorCodeHelper(mContext);
		camera_at = (LinearLayout) findViewById(R.id.camera_at);
		at_names = (TextView) findViewById(R.id.at_names);
		btnPost = (Button) findViewById(R.id.btn_post);
		photoList = new ArrayList<String>();
		checkin_msg = (EditText) findViewById(R.id.checkin_msg);
		c_avatar = (ImageView) findViewById(R.id.c_avatar);
		Picasso.with(this).load(loginUser.getProfile_img_url()).into(c_avatar);
		getImagePath = new GetImagePath(mContext);
		dialogListener = new DialogListener(){
			@Override
			public void showDialog(Object o) {
				Message msg = mHandler.obtainMessage();
				msg.what = DELETE_IMAGE;
				mHandler.sendMessage(msg);
			}
		};
		btnBack.setOnTouchListener(Util.TouchDark);
		btnBack.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				CameraActivity.this.finish();
			}
		});
		camera_at.setOnTouchListener(Util.TouchDark);
		camera_at.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(CameraActivity.this,ChooseAtActivity.class);
				intent.putExtra("atList", (Serializable)atList);
				startActivityForResult(intent,CHOOSE_AT_PERSON);
			}
		});
		btnPost.setOnTouchListener(Util.TouchDark);
		btnPost.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				progressDialog = CustomDialog.createProgressDialog(mContext, "Checking in ...");
				progressDialog.show();
				new Thread(){
					public void run(){
					  returnCheckin = APIManager.sharedInstance().sendCheckin(store_name,store_id,atString,photoList.size(), checkin_msg.getText().toString());
					    Message msg = mHandler.obtainMessage();
						msg.what = returnCheckin.getError_code();
					    mHandler.sendMessage(msg);  
					}
				}.start();
			}
		});

	}
	
	private void initAdapter() {
		adapter = new ImageAdapter(this,lo);
		camera_gridview.setAdapter(adapter);
	}
	
	private void addBitmap(Bitmap bitmap){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("image",bitmap);
		map.put("type", "");
		map.put("path","");
		lo.add(map);
	}
	
	private void addDrawable(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("image", R.drawable.camera_plus_icon);
		map.put("type", "");
		map.put("path","");
		lo.add(map);
	}
	
	private Handler mHandler = new Handler(){
		
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			ErrorCodeHelper errorHelper = new ErrorCodeHelper(mContext);
			if(errorHelper.CommonCode(msg.what)){
				switch(msg.what){
				   case DELETE_IMAGE:
					 deleteFile(choose_position);
					 lo.remove(choose_position);
					 addDrawable();
					 adapter.notifyDataSetChanged();
					 break;
				   case CHECKIN_FIRST_SUCCESS:
					   if(errorHelper.CommonCode(msg.what)){
						  UploadImage();
					   }
				     break;
				   case CHECKIN_IMAGE_SUCCESS:
					   completeImage ++;
					   if(completeImage == photoList.size()){
						   progressDialog.dismiss();
						   deletePhotoList();
						   showToast("Checkin Successful!");
						   Util.profile = null;
						   finish();
					   }
					 break;
				}
			}
		}
	};
	
	private void fisrt_image() {
		Intent intent = getIntent();
		store_id = intent.getStringExtra("store_id");
		store_name = intent.getStringExtra("store_name");
		if(intent.getStringExtra("type").equals("take_picture")){
			//Bitmap bitmap = BitmapFactory.decodeFile(filePath+tempFile);
			//处理旋转图片
			Bitmap bitmap = ImageTools.getSmallBitmap(filePath+tempFile,DEFAULT_RESIZE,DEFAULT_RESIZE);
			int degree = ImageTools.readPictureDegree(filePath+tempFile);
			if(degree != 0){
				bitmap = ImageTools.rotateBitmap(bitmap,degree);
			}
			addBitmap(bitmap);
			addDrawable();
			String fp = filePath+String.valueOf(System.currentTimeMillis())+".jpg";
			ImageTools.savePhotoToSDCard(bitmap, filePath, String.valueOf(System.currentTimeMillis()));
			photoList.add(fp);
			
		}else{
			
			Uri firstUri = Uri.parse(intent.getStringExtra("image_uri"));
		    String fileName = getImagePath.getPath(firstUri);
		    
			Bitmap bitmap = ImageTools.getSmallBitmap(fileName,DEFAULT_RESIZE,DEFAULT_RESIZE);
			int degree = ImageTools.readPictureDegree(fileName);
			if(degree != 0){
				bitmap = ImageTools.rotateBitmap(bitmap,degree);
			}
			addBitmap(bitmap);
			addDrawable();
			String fp = filePath+String.valueOf(System.currentTimeMillis())+".jpg";
			ImageTools.savePhotoToSDCard(bitmap, filePath, String.valueOf(System.currentTimeMillis()));
			photoList.add(fp);
		    
		}
	 }
	
	 public void UploadImage(){
		final List<String> urlList = new ArrayList<String>();
		urlList.add(returnCheckin.getImg_url_1());
		urlList.add(returnCheckin.getImg_url_2());
		Log.e("Stiqer","returnCheckin="+returnCheckin.toString());
		Log.e("Stiqer","photoList="+photoList.toString());
	    for(int i = 0; i<photoList.size(); i++){
	    	final int position = i;
	    	new Thread(){
	    		public void run(){
	    			int code = APIManager.sharedInstance().uploadCheckinImage(urlList.get(position),photoList.get(position));
	    			Message msg = mHandler.obtainMessage();
	    			if(code == CHECKIN_FIRST_SUCCESS){
	    				msg.what = CHECKIN_IMAGE_SUCCESS;
	    			}
					mHandler.sendMessage(msg);
	    		}
	    	}.start();
	    }	
	 }

	 public void showPicturePicker(Context context){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setNegativeButton("Cancel", null);
			builder.setItems(new String[]{"Take a new picture","From gallery"}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case TAKE_PICTURE:
						Uri imageUri = null;
						Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						imageUri = Uri.fromFile(new File(filePath,tempFile));
						//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(openCameraIntent, TAKE_PICTURE_CODE);
						break;
						
					case CHOOSE_PICTURE:
						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
						openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(openAlbumIntent, CHOOSE_PICTURE_CODE);
						break;

					default:
						break;
					}
				}
			});
			builder.create().show();
		}
	 
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				case TAKE_PICTURE_CODE:
					Bitmap bitmap = ImageTools.getSmallBitmap(filePath+tempFile,DEFAULT_RESIZE,DEFAULT_RESIZE);
					int degree = ImageTools.readPictureDegree(filePath+tempFile);
					if(degree != 0){
						bitmap = ImageTools.rotateBitmap(bitmap,degree);
					}
					lo.remove(now_position);
					addBitmap(bitmap);
					if(lo.size()<MOST_IMAGE_NUM){
						addDrawable();
					}
					adapter.notifyDataSetChanged();
					String fp = filePath+String.valueOf(System.currentTimeMillis())+".jpg";
					ImageTools.savePhotoToSDCard(bitmap, filePath, String.valueOf(System.currentTimeMillis()));
					photoList.add(fp);
					break;

				case CHOOSE_PICTURE_CODE:
					Uri originalUri = data.getData(); 
					if (originalUri != null) {
				      
					  String fileName = getImagePath.getPath(originalUri);
					  Bitmap choosebitmap = ImageTools.getSmallBitmap(fileName,DEFAULT_RESIZE,DEFAULT_RESIZE);
					  int choose_degree = ImageTools.readPictureDegree(fileName);
					  if(choose_degree != 0){
						 choosebitmap = ImageTools.rotateBitmap(choosebitmap,choose_degree);
					  }
					
					  lo.remove(now_position);
					  addBitmap(choosebitmap);
					  if(lo.size()<MOST_IMAGE_NUM){
						addDrawable();
					  }
					
					  String choose_fp = filePath+String.valueOf(System.currentTimeMillis())+".jpg";
					  ImageTools.savePhotoToSDCard(choosebitmap, filePath, String.valueOf(System.currentTimeMillis()));
					  photoList.add(choose_fp);
					  adapter.notifyDataSetChanged();
					}
					break;
					
				case CHOOSE_AT_PERSON:
					atList = (List<Friend>) data.getSerializableExtra("atList");
					String str = "";
					atString = "";
					if(atList.size()>0){
						for(Friend f : atList){
							str = str+"@"+f.getUsername()+" ";
							atString =  atString + "@"+ f.getUsername();
						}
						at_names.setText(str);
					}
					
					break;
					
				default:
					break;
				}
			}
		}
	 
		public class ImageAdapter extends BaseAdapter {
		    private Context    mContext;
	        private List<Map<String,Object>>  lo;
		    public ImageAdapter(Context c, List<Map<String,Object>>  Ids) {
		        mContext = c;
		        lo = Ids;
		    }

		    public int getCount() {
		        return lo.size();
		    }

		    public Object getItem(int position) {
		        return null;
		    }

		    public long getItemId(int position) {
		        return 0;
		    }
		    
		    public View getView(final int position, View convertView, ViewGroup parent) {
		        ImageView imageView;
		        if (convertView == null) { 
		            imageView = new ImageView(mContext);
		            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
		            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		            imageView.setPadding(2, 2, 2, 2);
		        } else {
		            imageView = (ImageView) convertView;
		        }
		        
		        if(lo.get(position).get("image") instanceof Integer){
		        	imageView.setImageResource((Integer) lo.get(position).get("image"));
		        }
		        
		        if(lo.get(position).get("image") instanceof Bitmap){
		        	imageView.setImageBitmap((Bitmap) lo.get(position).get("image"));
		        }
		        
		        imageView.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						if(lo.get(position).get("image") instanceof Integer){
							now_position = position;
							showPicturePicker(mContext);
						}else{
							choose_position = position;
							ShowImageDialog = CustomDialog.createImageDialog(mContext,(Bitmap) lo.get(position).get("image"),dialogListener);
							ShowImageDialog.show();
						}
					}
		        });
		        return imageView;
		    }
	   }
		
	   private void deleteFile(int position){
		   String  imgfile = photoList.get(position);
		   Pattern pat = Pattern.compile("stiqer");
		   Matcher mat = pat.matcher(imgfile);
		   if(mat.find()){
			  File f = new File(imgfile);
			  f.delete();
		   }
		   photoList.remove(position);
		   if(photoList.size() == 0){
			   lo.remove(0);
			   adapter.notifyDataSetChanged();
		   }
		   if(photoList.size() == 0){
			   lo.remove(position);
			   addDrawable();
			   adapter.notifyDataSetChanged();
		   }
	   }
	   
	   private void deletePhotoList(){
		   Pattern pat = Pattern.compile("stiqer");
		   for(int i=0; i<photoList.size(); i++ ){
			   String  imgfile = photoList.get(i);
			   Matcher mat = pat.matcher(imgfile);
			   if(mat.find()){
					  File f = new File(imgfile);
					  f.delete();
				   }
		   }
	   }
}
