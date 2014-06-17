package com.hongkong.stiqer.widget;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.utils.ImageTools;
import com.hongkong.stiqer.utils.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;



public class ProfileDialog extends Dialog
{
    View               view;
    Context            dcontext;
    static final String[] string_year={"YEAR","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","20050","2006","2007","2008","2009","2010","2011","2012","2013"};  
    static final String[] string_month={"MONTH","1","2","3","4","5","6","7","8","9","10","11","12"};  
    static final String[] string_day={"DAY","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};  
    static final String[] string_sex={"CHOOSE","Male","Female"};
    private Spinner reg_day,reg_month,reg_year,sex;  
    ImageView  photo_image;
    private ArrayAdapter<String> day_adapter,month_adapter,year_adapter,sex_adapter;  
    Activity mActivity;
    
    private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private static final int SCALE = 5;//照片缩小比例
  
    public ProfileDialog(Context mcontext, Activity activity)
    {
        super(mcontext);
        this.dcontext = mcontext;
        this.mActivity = activity;
        this.view = LayoutInflater.from(dcontext).inflate(R.layout.activity_profile, null);
        photo_image = (ImageView) view.findViewById(R.id.photo_image);
        init_spinner();
    }

    
    private void init_spinner() {
    	 reg_day=(Spinner) view.findViewById(R.id.reg_day);   
         day_adapter=new ArrayAdapter<String>(dcontext,R.layout.profile_spinner_item,string_day);  
         day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         reg_day.setAdapter(day_adapter);  
         
         reg_month=(Spinner) view.findViewById(R.id.reg_month);   
         month_adapter=new ArrayAdapter<String>(dcontext,R.layout.profile_spinner_item,string_month);  
         month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         reg_month.setAdapter(month_adapter);  
         
         reg_year=(Spinner) view.findViewById(R.id.reg_year);   
         year_adapter=new ArrayAdapter<String>(dcontext,R.layout.profile_spinner_item,string_year);  
         year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         reg_year.setAdapter(year_adapter); 
         
         sex=(Spinner) view.findViewById(R.id.sex_spinner);   
         sex_adapter=new ArrayAdapter<String>(dcontext,R.layout.profile_spinner_item,string_sex);  
         sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         sex.setAdapter(sex_adapter); 
	}


	public ProfileDialog(Context context, int theme)
    {
        super(context, theme);
    }
    
    
    public ProfileDialog createDialog() {
		final ProfileDialog dialog = new ProfileDialog(dcontext, R.style.CustomDialog);
    	dialog.setContentView(view);
    	photo_image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showPicturePicker(dcontext);
			}
		});
		float dialogWidth = Util.SCREENWIDTH - 2 * dcontext.getResources().getDimension(R.dimen.widget_margin);
    	dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    	dialog.getWindow().getAttributes().width = (int) dialogWidth;
		return dialog;
	}
    
    public void showPicturePicker(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
			//类型码
			int REQUEST_CODE;
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
				    REQUEST_CODE = CROP;
					//删除上一次截图的临时文件
					SharedPreferences sharedPreferences = dcontext.getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
					ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
						
					//保存本次截图临时文件名字
					fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
					Editor editor = sharedPreferences.edit();
					editor.putString("tempName", fileName);
					editor.commit();

					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
					//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					mActivity.startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;
					
				case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					REQUEST_CODE = CROP;
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					mActivity.startActivityForResult(openAlbumIntent, REQUEST_CODE);
					break;

				default:
					break;
				}
			}
		});
		builder.create().show();
	}
    
    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				//将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				
				//将处理过的图片显示在界面上，并保存到本地
				photo_image.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
				
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = dcontext.getContentResolver();
				//照片的原始资源地址
				Uri originalUri = data.getData(); 
	            try {
	            	//使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
						//释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();
						
						photo_image.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
					System.out.println("Data");
				}else {
					System.out.println("File");
					String fileName = dcontext.getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
					uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
				}
				cropImage(uri, 500, 500, CROP_PICTURE);
				break;
			
			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
		                photo = (Bitmap)extra.get("data");  
		                ByteArrayOutputStream stream = new ByteArrayOutputStream();  
		                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		            }  
				}
				photo_image.setImageBitmap(photo);
				break;
			default:
				break;
			}
		}
	}
    */
  //截取图片
  	public void cropImage(Uri uri, int outputX, int outputY, int requestCode){
  		Intent intent = new Intent("com.android.camera.action.CROP");  
          intent.setDataAndType(uri, "image/*");  
          intent.putExtra("crop", "true");  
          intent.putExtra("aspectX", 1);  
          intent.putExtra("aspectY", 1);  
          intent.putExtra("outputX", outputX);   
          intent.putExtra("outputY", outputY); 
          intent.putExtra("outputFormat", "JPEG");
          intent.putExtra("noFaceDetection", true);
          intent.putExtra("return-data", true);  
  	    mActivity.startActivityForResult(intent, requestCode);
  	}
    
}

