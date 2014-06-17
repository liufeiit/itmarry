package com.hongkong.stiqer.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;


import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.AppContext;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.ProfileDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;



public class GuideActivity extends BaseActivity {
	//内置
	public static final int  PASSWORD_IS_EMPTY = 202;
	public static final int  EMAIL_IS_EMPTY = 203;
	public static final int  EMAIL_MISMATCH = 204;
	public static final int  CONNECTION_ERROR = 0;
    //登陆	
	public static final int  LOGIN_SUCCESS = 1001;
	public static final int  EMAIL_NOT_EXIIT = 1002;
	public static final int  EMAIL_PASS_MISMATCH = 1003;
	public static final int  PROFILE_NOT_COMPLETE = 1007;
	//注册
	public static final int  REGISTER_SUCCESS = 1006;
	public static final int  EMAIL_EXIST = 1005;
	//第三方登陆
	public static final int  USERNAME_DUPLICATED = 1010;
	
	private ViewPager        viewPager;
	private ArrayList<View>  pageViews;
	private ImageView        imageView;
	private ImageView[]      imageViews;
	private ViewGroup        main;
	private ViewGroup        group;
	private Button           ShowLoginBtn,ShowRegBtn;
	CustomDialog             LoginDialog,RegDialog, progressDialog,thirdDialog;
	ProfileDialog            profileDialog;
	DialogListener           loginListener,profileListener;
	SUser                     user;
   //WeiboAuth                mWeiboAuth;
   //SsoHandler               mSsoHandler;
   //Oauth2AccessToken        mAccessToken;
   //UsersAPI                 mUsersAPI;
    String                   deviceToken;
    
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		Util.SCREENHEIGHT = displayMetrics.heightPixels;
		Util.SCREENWIDTH = displayMetrics.widthPixels;
		
		SharedPreferences loginPreferences = getSharedPreferences(Util.TAG, 0);

		
		/* aviation code
		if(loginPreferences.getString("invitation", "").equals("")){
			Intent t = new Intent(GuideActivity.this,InvitationActivity.class);
            startActivity(t);
            
		}else{
		*/
			if(!loginPreferences.getString("uid", "").equals("")){
				APIManager.sharedInstance().UserID = loginPreferences.getString("uid", "");
				APIManager.sharedInstance().Token = loginPreferences.getString("token", "");
				Intent t = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(t);
				this.finish();
			}
		/*
		}
		*/
		AppContext app =  (AppContext) getApplication();
	    deviceToken = app.getDevice_token();
	    if(deviceToken == null){
	    	deviceToken = "";
	    }
		ShareSDK.initSDK(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int[] img = new int[] { R.drawable.login_slide1, R.drawable.login_slide2, R.drawable.login_slide3,
				R.drawable.login_slide4};
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		for (int i = 0; i < img.length; i++) {
			LinearLayout layout = new LinearLayout(this);
			LayoutParams ltp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			final ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(img[i]);
	
			layout.addView(imageView, ltp);
			pageViews.add(layout);
		}
		thirdDialog = CustomDialog.createProgressDialog(this, "Loading...");
		
		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.activity_guide, null);
		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
		ShowLoginBtn = (Button) main.findViewById(R.id.loginbtn);
		ShowRegBtn = (Button) main.findViewById(R.id.regbtn);
		
		ShowLoginBtn.setOnClickListener(new OnclickListener());
		ShowLoginBtn.setOnTouchListener(Util.TouchDark);
		ShowRegBtn.setOnClickListener(new OnclickListener());
		ShowRegBtn.setOnTouchListener(Util.TouchDark);
		
		for (int i = 0; i < pageViews.size(); i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			margin.setMargins(10, 0, 0, 0);
			imageView = new ImageView(GuideActivity.this);
			imageView.setLayoutParams(new LayoutParams(25, 25));
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.guide_dot_full);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.guide_dot_empty);
			}
			group.addView(imageViews[i], margin);
		}
		setContentView(main);
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		loginListener = new DialogListener(){
			public void showDialog(Object o) {
				String returnStr = (String) o;
				if(returnStr.equals("weibo")){
					thirdDialog.show();
					weibo();
				}else if(returnStr.equals("facebook")){
					thirdDialog.show();
					facebook();
				}else{
					String array[] = ((String) o).split(",");
					if(array[1].trim().equals("")){
						Message msg = mHandler.obtainMessage();
			            msg.what = EMAIL_IS_EMPTY;
			            mHandler.sendMessage(msg);
					}else if(!array[0].equals("login") && !Util.isEmail(array[1].trim())){
						Message msg = mHandler.obtainMessage();
			            msg.what = EMAIL_MISMATCH;
			            mHandler.sendMessage(msg);
					}else if(array[2].trim().equals("")){
						Message msg = mHandler.obtainMessage();
			            msg.what = PASSWORD_IS_EMPTY;
			            mHandler.sendMessage(msg);
					}else{
						if(array[0].equals("login")){
							login(array[1].trim(),array[2].trim());
						}else{
						    register(array[1].trim(),array[2].trim());	
                        }
					}
				}
			}
		};

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
	
	public class OnclickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.loginbtn){
				LoginDialog = CustomDialog.createLoginDialog(GuideActivity.this,loginListener);
				LoginDialog.show();
			}
            if(v.getId() == R.id.regbtn){
            	RegDialog = CustomDialog.createRegDialog(GuideActivity.this,loginListener);
            	RegDialog.show();
			}
		}
	}
	
	private void weibo(){
		 Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		 weibo.SSOSetting(false);
		 weibo.setPlatformActionListener(new PlatformActionListener() {
			public void onCancel(Platform arg0, int arg1) {
			}
			public void onComplete(Platform plat, int arg1,HashMap<String, Object> arg2) {	
				login_third(plat.getDb().getUserId(),1,plat.getDb().getUserName(),plat.getDb().getUserIcon());
			}
			public void onError(Platform arg0, int arg1, Throwable arg2) {
			}
		});
		 weibo.authorize();
	 }
	
	 private void facebook(){
		 Platform fb = ShareSDK.getPlatform(this,Facebook.NAME);
		 fb.setPlatformActionListener(new PlatformActionListener() {
			public void onCancel(Platform arg0, int arg1) {
				 Log.e("Stiqer","onCancel="+arg1);
			}
			public void onComplete(Platform plat, int arg1,HashMap<String, Object> arg2) {	
				login_third(plat.getDb().getUserId(),2,plat.getDb().getUserName(),plat.getDb().getUserIcon());
			}

			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Log.e("Stiqer","onError="+arg1);
			}
		});
		 fb.authorize();
	 }
	 
	/*
	private void weibo(){
		mWeiboAuth = new WeiboAuth(this, "3576606494","http://www.stiqer.com", "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write");
		    mSsoHandler = new SsoHandler(GuideActivity.this, mWeiboAuth);
		    mSsoHandler.authorize(new WeiboAuthListener(){
			@Override
			public void onCancel() {
			}

			@Override
			public void onComplete(Bundle values) {
				mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            mUsersAPI = new UsersAPI(mAccessToken);
	            getWeiboInfo();
			}

			@Override
			public void onWeiboException(WeiboException arg0) {
			}
		 });
	}
	
	private void getWeiboInfo(){
		long uid = Long.parseLong(mAccessToken.getUid());
		mUsersAPI.show(uid, new RequestListener() {
			 @Override
		        public void onComplete(String response) {
		            if (!TextUtils.isEmpty(response)) {
		            	 try {
							JSONObject jt = new JSONObject(response);
							login_third(String.valueOf(jt.getInt("id")),1,jt.getString("name"),jt.getString("profile_image_url"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
		            }
		        }

		        @Override
		        public void onWeiboException(WeiboException e) {
		        }
	    });
	}
	
	 private void facebook(){
		 Session.openActiveSession(this, true, new Session.StatusCallback() {
		      // callback when session changes state
			@Override
		      public void call(Session session, SessionState state, Exception exception) {
		        if (session.isOpened()) {

		          // make request to the /me API
		          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		            // callback after Graph API response with user object
		            @Override
		            public void onCompleted(GraphUser user, Response response) {
		              if (user != null) {
		            	  login_third(user.getId(), 2 ,user.getUsername(),"");
		              }
		            }
		          });
		        }
		      }
		    });
	 }
	  */
	 
	 private void login_third(final String oauth_uid, final int type, final String username,final String profile_image_url){
		 new Thread(){
	    	 public void run(){
	
	    		 user = APIManager.sharedInstance().login_third(oauth_uid, type,username,profile_image_url,deviceToken);
	    		 if(user ==null){
	    			 Message msg = mHandler.obtainMessage();
		             msg.what = CONNECTION_ERROR;
		             mHandler.sendMessage(msg);
	    		 }else{
	    			 Message msg = mHandler.obtainMessage();
		             msg.what = user.getError_code();
		             mHandler.sendMessage(msg);
	    		 }
	    		
	    	 }
	     }.start();
	 }
	 
	 private void login(final String username, final String password){	 
		 progressDialog = CustomDialog.createProgressDialog(this, getString(R.string.login_loading));
	     progressDialog.show();
	     new Thread(){
	    	 public void run(){
	    		 user = APIManager.sharedInstance().login(username, password,deviceToken);
	    		 if(user == null){
	    			 Message msg = mHandler.obtainMessage();
		             msg.what = CONNECTION_ERROR;
		             mHandler.sendMessage(msg);
	    		 }else{
	    	        Message msg = mHandler.obtainMessage();
	                msg.what = user.getError_code();
	                mHandler.sendMessage(msg);
	    		 }
	             progressDialog.dismiss();

	    	 }
	     }.start();
	 }
	
	 private void register(final String username, final String password) {
    	 progressDialog = CustomDialog.createProgressDialog(GuideActivity.this,getString(R.string.register_loading));
         progressDialog.show();
			//注册过程
         new Thread(){
         	public void run(){
         		user = APIManager.sharedInstance().register(username,password,deviceToken);
         		if(user ==null){
	    			 Message msg = mHandler.obtainMessage();
		             msg.what = CONNECTION_ERROR;
		             mHandler.sendMessage(msg);
	    		 }else{
         		     Message msg = mHandler.obtainMessage();
                     msg.what = user.getError_code();
                     mHandler.sendMessage(msg);
	    	     }
         		 progressDialog.dismiss();
         	}
         }.start();
	 }
	 
	 private Handler mHandler = new Handler()
     {
		 
         public void handleMessage(Message msg)
         {
        	 if(thirdDialog.isShowing()){
        		 thirdDialog.dismiss();
        	 }
        	 super.handleMessage(msg);
             switch (msg.what)
             {
                 case CONNECTION_ERROR:
            	      showToast(getString(R.string.connection_error));
            	 break;
            	 
                 case EMAIL_IS_EMPTY:
                	 showToast(getString(R.string.email_username_empty));
                	 break;
            	 
                 case EMAIL_NOT_EXIIT:
                     showToast(getString(R.string.email_not_exist));
                     break;
                     
                 case EMAIL_PASS_MISMATCH:
                     showToast(getString(R.string.email_pass_mismatch));
                     break;
                     
                 case EMAIL_MISMATCH:
                     showToast(getString(R.string.email_mismatch));
                     break;
                     
                 case PROFILE_NOT_COMPLETE:
                	 Intent i = new Intent(GuideActivity.this, LCompleteActivity.class);
                	 i.putExtra("token", user.getToken());
                	 i.putExtra("uid", user.getUid());
                     startActivity(i);
                     //GuideActivity.this.finish();
                     overridePendingTransition(R.anim.main_show, R.anim.login_hide);  
                     break;
                     
                 case LOGIN_SUCCESS:
                     SharedPreferences sharedPreferences = getSharedPreferences(Util.TAG, 0);
                     Editor editor = sharedPreferences.edit();
                     editor.putString("token", user.getToken());
                     editor.putString("uid", user.getUid());
                     editor.putString("username", user.getUsername());
                     editor.putString("phone_verified", user.getIs_verified());
                     editor.putString("profile_image_url", user.getProfile_img_url());
                     editor.commit();
                     Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                     startActivity(intent);
                     GuideActivity.this.finish();
                     overridePendingTransition(R.anim.main_show, R.anim.login_hide);   
                     break;
                  
                 case PASSWORD_IS_EMPTY:
                	 showToast(getString(R.string.password_empty));
                	 break;
                	 
                 case REGISTER_SUCCESS:
               	     progressDialog.dismiss();
                     RegDialog.dismiss();
                     Intent intent1 = new Intent(GuideActivity.this, LCompleteActivity.class);
                     intent1.putExtra("token", user.getToken());
                     intent1.putExtra("uid", user.getUid());
                     startActivity(intent1);
                   break;

                 case EMAIL_EXIST:
                     showToast(getString(R.string.reg_email_exist));
                     break;
                     
                 case USERNAME_DUPLICATED:
                	showToast("Your username is "+user.getUsername()+" , you can modify on profile setting page");
                	SharedPreferences sharedPreferences2 = getSharedPreferences(Util.TAG, 0);
                    Editor editor2 = sharedPreferences2.edit();
                    editor2.putString("token", user.getToken());
                    editor2.putString("uid", user.getUid());
                    editor2.putString("username", user.getUsername());
                    editor2.putString("phone_verified", "no");
                    editor2.putString("profile_image_url", user.getProfile_img_url());
                    editor2.commit();
                    Intent intent2 = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent2);
                    GuideActivity.this.finish();
                    overridePendingTransition(R.anim.main_show, R.anim.login_hide);
                    break;
             }
         };
     };
     
     public void onDestroy(){
    	 ShareSDK.stopSDK(this);
    	 super.onDestroy();
     }
     
     
     /*
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         
         // SSO 授权回调
         // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
         if (mSsoHandler != null) {
             mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            // Log.e("Stiqer","data="+data.getExtras().toString());
         }else{
        	 super.onActivityResult(requestCode, resultCode, data);
             Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
         }
             
     }
     */
	// 指引页面数据适配器
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
		}
	}

	// 指引页面更改事件监听器
		class GuidePageChangeListener implements OnPageChangeListener {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageSelected(int arg0) {
				//遍历数组让当前选中图片下的小圆点设置颜色
				for (int i = 0; i < imageViews.length; i++) {
					imageViews[arg0].setBackgroundResource(R.drawable.guide_dot_full);

					if (arg0 != i) {
						imageViews[i].setBackgroundResource(R.drawable.guide_dot_empty);
					}
				}
			}
		}
}
