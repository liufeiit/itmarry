package com.hongkong.stiqer.fragment;


import java.io.File;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.hongkong.stiqer.R;

import com.hongkong.stiqer.adapter.AchievementAdapter;
import com.hongkong.stiqer.adapter.CheckInAdapter;
import com.hongkong.stiqer.adapter.ProfileActivityAdapter;
import com.hongkong.stiqer.entity.Avatar;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;

import com.hongkong.stiqer.ui.MainActivity;
import com.hongkong.stiqer.ui.MessageActivity;
import com.hongkong.stiqer.ui.PSettingActivity;
import com.hongkong.stiqer.ui.TabUserActivity;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.GetImagePath;
import com.hongkong.stiqer.utils.ImageTools;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.ExpandableHeightGridView;
import com.hongkong.stiqer.widget.ExpandableHeightListView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import eu.janmuller.android.simplecropimage.CropImage;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;


public class ProfileFragment extends Fragment {
	public static final String     TAG = "Siqer";  
	public static final int        AVATAR_UPLOAD_SUCCESS = 1000;
	
	public static final int        SEND_MESSAGE_SUCCESS = 105;
	public static final int        CONNECTION_ERROR = 0;
	public static final int        LOAD_SUCCESS = 101;
	public static final int        SHOW_BIG_AVATAR = 102;
	public static final int        CHANGE_AVATAR = 103;
    private static final int       TAKE_PICTURE = 0;
	private static final int       CHOOSE_PICTURE = 1;
	private static final int       PROFILE_PICTURE_CODE = 104;
	private static final int       PROFILE_GALLERY_CODE = 112;
	private static final int       PROFILE_CROP_PICTURE = 105;
	private static final int       PROFILE_SETTING = 111;
	private static final int       DEFAULT_RESIZE = 600;
	private static final String    PICTURE_NAME = "pre_avatar";
   
    View                           view,lv_layout;  
    ExpandableHeightGridView       gridview_hide;
    ProfileActivityAdapter         padapter;
    ExpandableHeightListView       activity_list;
    LinearLayout                   setting_btn,message_btn,friend_btn,pl_wrap;
    Gallery                        check_gallery, achieve_gallery;
    CustomDialog                   processingDialog,avatarDialog,bigImageDialog;
    DialogListener                 avatarDialogListener;
    ProgressBar                    pl_progressbar,bottom_progress;
    TextView                       total_level,pl_earn_num,next_class_txt,user_title,des_first_text,des_second_text,des_third_text,btn_more_activity,btn_more_photo;
    LayoutInflater                 mInflater;
    ImageView                      pl_avatar,des_first_dot,des_second_dot,des_third_dot,next_class_icon;
    String   filePath          =   Environment.getExternalStorageDirectory().getPath() + "/stiqer/";
    MainActivity                   mainActivity;
    String   cacheName         =   Util.MD5("profile")+".txt";
    String                         username,uid,activity_str,achieve_str,checkin_str, profile_img;
    SUser                           loginUser;
    ErrorCodeHelper                errorHelper;
    Avatar                         avatar;
    String avatarPath          =   Environment.getExternalStorageDirectory().getPath() + "/avatar.jpg";
    Context                        mContext;
    
    public static ProfileFragment newInstance(String content) {
    	ProfileFragment fragment = new ProfileFragment();  
        Bundle args = new Bundle();  
        args.putString(TAG, content);  
        fragment.setArguments(args);
        return fragment;  
    }
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
    }
    
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
        view = inflater.inflate(R.layout.fragment_profile, container, false);  
        initUser();
        initView();  
        initDialog();
        
        if(Util.profile != null){
        	showFromCache();
        }else{
        	new GetDataTask().execute();
        }
        return view;  
    }  
    
	private void initUser() {
		mContext = getActivity();
		CacheHelper cacheHelper = new CacheHelper(mContext);
		loginUser = cacheHelper.GetUser();
		uid = loginUser.getUid();
		username = loginUser.getUsername();
	}
	
	private void createCacheFile(JSONObject obj){
    	Util.profile = obj;
    }
   
    private void initView() {
    	
    	pl_progressbar = (ProgressBar) view.findViewById(R.id.pl_progressbar);
    	bottom_progress = (ProgressBar) view.findViewById(R.id.bottom_progressbar);
    	pl_avatar = (ImageView) view.findViewById(R.id.pl_avatar);
    	
    	pl_wrap = (LinearLayout) view.findViewById(R.id.pl_layout_wrap);
    	total_level = (TextView) view.findViewById(R.id.pl_total_level);
    	pl_earn_num = (TextView) view.findViewById(R.id.pl_earn_num);
    	next_class_txt = (TextView) view.findViewById(R.id.next_class_txt);
    	achieve_gallery = (Gallery) view.findViewById(R.id.pf_achieve_gallery);
    	check_gallery = (Gallery) view.findViewById(R.id.pf_check_gallery);     
    	
    	MarginLayoutParams mlp = (MarginLayoutParams) check_gallery.getLayoutParams();
        mlp.setMargins(-(Util.SCREENWIDTH/9*7), 
                       mlp.topMargin, 
                       mlp.rightMargin, 
                       mlp.bottomMargin
        );
        
    	mInflater = LayoutInflater.from(getActivity());
    	message_btn = (LinearLayout) view.findViewById(R.id.message_btn);
    	friend_btn = (LinearLayout) view.findViewById(R.id.friend_btn);
        setting_btn = (LinearLayout) view.findViewById(R.id.setting_btn);
        next_class_icon = (ImageView) view.findViewById(R.id.next_class_icon);
        activity_list = (ExpandableHeightListView) view.findViewById(R.id.pl_activity_listview);
        btn_more_activity = (TextView) view.findViewById(R.id.btn_more_activity);
    	btn_more_photo = (TextView) view.findViewById(R.id.btn_more_photo);
    	errorHelper = new ErrorCodeHelper(getActivity());
    	
    	btn_more_photo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    	btn_more_activity.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    	
        des_first_dot = (ImageView) view.findViewById(R.id.user_des_first_dot);
        des_second_dot = (ImageView) view.findViewById(R.id.user_des_second_dot);
        des_third_dot = (ImageView) view.findViewById(R.id.user_des_third_dot);
        des_first_text = (TextView) view.findViewById(R.id.user_des_first_text);
        des_second_text = (TextView) view.findViewById(R.id.user_des_second_text);
        des_third_text = (TextView) view.findViewById(R.id.user_des_third_text);
        
        mainActivity = (MainActivity) getActivity();
        
        pl_avatar.setOnClickListener(new MyOnClickListener());
        pl_avatar.setOnTouchListener(Util.TouchDark);
        setting_btn.setOnClickListener(new MyOnClickListener());
        setting_btn.setOnTouchListener(Util.TouchDark);
        message_btn.setOnClickListener(new MyOnClickListener());
        message_btn.setOnTouchListener(Util.TouchDark);
        friend_btn.setOnClickListener(new MyOnClickListener());
        btn_more_activity.setOnClickListener(new MyOnClickListener());
        btn_more_activity.setOnTouchListener(Util.TouchDark);
        btn_more_photo.setOnClickListener(new MyOnClickListener());
        btn_more_photo.setOnTouchListener(Util.TouchDark);
    }

    private void initDialog() {
         avatarDialogListener = new DialogListener(){
 			public void showDialog(Object o) {
 				String reStr = (String) o;
 				Message msg = mHandler.obtainMessage();
 				if(reStr.equals("show")){
 					msg.what = SHOW_BIG_AVATAR;
 					mHandler.sendMessage(msg);
 				}else{
 					msg.what = CHANGE_AVATAR;
 					mHandler.sendMessage(msg);
 				}
 			}
         };
    }
    
    class MyOnClickListener implements OnClickListener{
		public void onClick(View v) {
		   switch(v.getId()){
		   case R.id.pl_avatar:
			   avatarDialog = CustomDialog.createAvatarDialog(getActivity(),avatarDialogListener);
			   avatarDialog.show();
			   break;
		   case R.id.setting_btn:
			   Intent i = new Intent(getActivity(),PSettingActivity.class);
			   getActivity().startActivityForResult(i,PROFILE_SETTING);
			   break;
		   case R.id.message_btn:
			   //mainActivity.selectFromFragment(2);
			   Intent t = new Intent(getActivity(),MessageActivity.class);
			   startActivity(t);
			   break;
		   case R.id.friend_btn:
			   mainActivity.selectFromFragment(3);
			   break;
		   case R.id.btn_more_activity:
				goTab(2);
				break;
			case R.id.btn_more_photo:
				goTab(0);
				break;
			}
		 }	
    }
    
    private void goTab(int position){
		Intent t = new Intent(getActivity(),TabUserActivity.class);
		t.putExtra("activity", activity_str);
		t.putExtra("achievements", achieve_str);
		t.putExtra("checkin", checkin_str);
		t.putExtra("position", position);
		t.putExtra("to_uid", uid);
		startActivity(t);
	}
    
    public Handler mHandler = new Handler(){
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		switch(msg.what){
    		
    		case SEND_MESSAGE_SUCCESS:
    			processingDialog.dismiss();
    			Toast.makeText(getActivity(), "Send successful", Toast.LENGTH_SHORT).show();
    			break;
    			
    		case SHOW_BIG_AVATAR:
    			bigImageDialog = CustomDialog.createBigImageDialog(getActivity(),profile_img,profile_img);
    			bigImageDialog.show();
    			break;
    			
    		case CHANGE_AVATAR:
    			showPicturePicker(getActivity());
    			break;
    			
    		case AVATAR_UPLOAD_SUCCESS:
    			processingDialog.dismiss();
    			Util.profile = null;
    			Toast.makeText(getActivity(),getString(R.string.avatar_upload), Toast.LENGTH_LONG).show();
    			PicassoTools.clearCache(Picasso.with(mContext));
    			Util.isAvatarChange = true;
           	  break;
    		}
    	}
    };
    
    //GetData
    private class GetDataTask extends AsyncTask<Integer[] ,Void, JSONObject>{
    	
		protected void onPostExecute(JSONObject obj) {
			pl_progressbar.setVisibility(View.GONE);
			if(obj == null){
				errorHelper.connectError();
			}else{	
				try {
					if(errorHelper.CommonCode(obj.getInt("error_code"))){
						 createCacheFile(obj);
						 showFromCache();
					 }
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			super.onPostExecute(obj);
		}
		
  		@Override
		protected JSONObject doInBackground(Integer[]... arg0) {
			return APIManager.sharedInstance().getProfileDetail("");
		}
    }
    
    
    private void showFromCache(){
            JSONObject obj = Util.profile;
            try {
            	
				 JSONObject user_info = obj.getJSONObject("user_info");
				 total_level.setText("Level "+user_info.getInt("user_level"));
				 pl_earn_num.setText(user_info.getInt("egg_num")+" Earned");
				 profile_img = user_info.getString("profile_img");
				 
				 next_class_icon.setImageResource(next_class_ids[user_info.getInt("user_levelup")]);
				 next_class_txt.setText(next_class_array[user_info.getInt("user_levelup")]);
				 
				 if(!user_info.getString("achievements").equals("")){
				    achieve_gallery.setAdapter(new AchievementAdapter(getActivity(),user_info.getString("achievements"),1));        
				 }
				 
				 try{
					if(!profile_img.equals("")){
						Picasso.with(getActivity()).load(profile_img).skipMemoryCache().into(pl_avatar); 
					}else{
						Picasso.with(getActivity()).load(R.drawable.logo).into(pl_avatar);
					} 
				 }catch(Exception e){
					 if(getActivity() != null){
						 Picasso.with(getActivity()).load(R.drawable.logo).into(pl_avatar);
					 }else{
						 
					 }
				 }
				 
				 if(!user_info.getString("user_des").equals("")){
					des_first_dot.setVisibility(View.VISIBLE);
					des_first_text.setText(user_info.getString("user_des"));
				 }
				 if(!user_info.getString("user_des_second").equals("")){
					des_second_dot.setVisibility(View.VISIBLE);
					des_second_text.setText(user_info.getString("user_des_second"));
				 }
				 if(!obj.getString("photo_list").equals("[]")){
					check_gallery.setAdapter(new CheckInAdapter(mContext,(List<CheckIn>) Util.readJson2EntityList(obj.getString("photo_list"), new CheckIn())));
				 }
				 if(!obj.getString("activity_list").equals("[]")){
					activity_list.setAdapter(new ProfileActivityAdapter(mContext,(List<Pactivity>) Util.readJson2EntityList(obj.getString("activity_list"), new Pactivity()),R.layout.list_item_pl_activity));
				 } 
				 activity_list.setExpanded(true);
				//设置传递参数
			     activity_str = obj.getString("activity_list");
				 achieve_str = user_info.getString("achievements");
				 checkin_str = obj.getString("photo_list");
				
			} catch (JSONException e) {
				 Log.e("Stiqer","e");
				 e.printStackTrace();
			}
     
    	pl_progressbar.setVisibility(View.GONE);
		pl_wrap.setVisibility(View.VISIBLE);
    }
    
    String[] next_class_array = {
    		"0% to Next Level","25% to Next Level","50% to Next Level","75% to Next Level"
     };
    
     Integer[] next_class_ids = {
    		 R.drawable.level_bar_0,R.drawable.level_bar_25, R.drawable.level_bar_50,
    		R.drawable.level_bar_75
     };
     
    public void showPicturePicker(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setNegativeButton("Cancel", null);
		builder.setItems(new String[]{"Take a new picture","From gallery"}, new DialogInterface.OnClickListener() {
			//类型码

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				
				case TAKE_PICTURE:
					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					//删除上一次截图的临时文件
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE);
					ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
					//保存本次截图临时文件名字
					fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
					Editor editor = sharedPreferences.edit();
					editor.putString("tempName", fileName);
					editor.commit();

					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
					//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					getActivity().startActivityForResult(openCameraIntent, PROFILE_PICTURE_CODE);
					break;
					
				case CHOOSE_PICTURE:
					
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					getActivity().startActivityForResult(openAlbumIntent, PROFILE_GALLERY_CODE);
					break;
				default:
					break;
				}
			}
		});
		builder.create().show();
     }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Activity.RESULT_OK) {
    	switch (requestCode) {
    	case PROFILE_SETTING:
    		if(data != null){
    			if(!data.getExtras().getString("profile_img").equals("")){
        			Picasso.with(getActivity()).load(data.getExtras().getString("profile_img")).skipMemoryCache().into(pl_avatar);
        		}
    		}
    		break;
    		
    	case PROFILE_PICTURE_CODE:
    		
    		   String file_name = getActivity().getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
			   String files = Environment.getExternalStorageDirectory()+"/"+file_name;
			   Bitmap bitmap = ImageTools.getSmallBitmap(files,DEFAULT_RESIZE,DEFAULT_RESIZE);
		       int degree = ImageTools.readPictureDegree(files);
			   if(degree != 0){
				  bitmap = ImageTools.rotateBitmap(bitmap,degree);
			   }
			   ImageTools.savePhotoToSDCard(bitmap, Environment.getExternalStorageDirectory()+"/", PICTURE_NAME);
			   cropImage(Environment.getExternalStorageDirectory()+"/"+PICTURE_NAME+".jpg");
			   
    		break;
		case PROFILE_GALLERY_CODE:
			if (data != null) {
				Uri uri = data.getData();
				GetImagePath getImagePath= new GetImagePath(getActivity());
				String filess = getImagePath.getPath(uri);
				cropImage(filess);
			}	
			break;
		
		case PROFILE_CROP_PICTURE:
          final File f = new File(avatarPath);
          processingDialog = CustomDialog.createProgressDialog(getActivity(), "Uploading picture ...");
          processingDialog.show();
          new Thread(){
			 public void run(){
				 avatar = APIManager.sharedInstance().uploadProfileImage(f,"","");
				 Message msg = mHandler.obtainMessage();
                 msg.what = avatar.getError_code();
                 mHandler.sendMessage(msg);
			 }
		    }.start();
			Bitmap bitmaps = BitmapFactory.decodeFile(avatarPath);
			pl_avatar.setImageBitmap(bitmaps);
			break;
		default:
			break;
    	}	
    	}
    }
    
    public void cropImage(String path){
    	Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, path);
        intent.putExtra(CropImage.OUT_PATH, avatarPath);
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        getActivity().startActivityForResult(intent, PROFILE_CROP_PICTURE);
    }
}
