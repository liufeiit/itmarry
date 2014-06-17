package com.hongkong.stiqer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.flurry.android.FlurryAgent;
import com.google.zxing.CaptureActivity;
import com.hongkong.stiqer.AppContext;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.NotiNum;
import com.hongkong.stiqer.entity.ScanResult;
import com.hongkong.stiqer.entity.TokenResult;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.fragment.FavFragment;
import com.hongkong.stiqer.fragment.FeedFragment;
import com.hongkong.stiqer.fragment.FriendFragment;
import com.hongkong.stiqer.fragment.ProfileFragment;
import com.hongkong.stiqer.fragment.PromFragment;
import com.hongkong.stiqer.fragment.SettingFragment;
import com.hongkong.stiqer.fragment.StoreFragment;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.MyLocation;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity {
	
	private final static  int    SCAN_SUCCESS = 1000; 
	private final static  int    NOTI_ALTERNATION = 5*60*1000;
	private final static  int    TOKEN_ALTERNATION = 24*60*60*1000;
	private final static  int    SCAN_RETURN = 101;
	private final static  int    TAKE_PICTURE_RETURN= 102;
	private final static  int    CHOOSE_PICTURE_RETURN= 103;
	private static final int     PROFILE_PICTURE_CODE = 104;
	private static final int     PROFILE_CROP_PICTURE = 105;
	private final static int     REQUEST_CODE_PROMOTION = 106;
	private final static int     REQUEST_CODE_FAV = 107;
	private final static int     NOTI_SEND = 108;
	private final static int     GET_NOTI_NUM = 109;
	private final static int     TOKEN_SEND = 110;
	private final static int     REQUEST_CODE_SETTING = 111;
	private final static int     PROFILE_GALLERY_CODE = 112;
	public static final int      COMMENT_NUM_REFRESH = 113;
	private final static int     TOKEN_REFRESH_SUCCESS = 1042;
	private static final int     VERIFY_SUCCESS = 1040;
	private static final int     VERIFY_CODE_INVALID = 1033;
	private final static int     PHONE_SEND_SUCCESS = 1043;
	
	private Button          btn_drawer,btn_scan,nav_feed,nav_friend,nav_prom,nav_store,nav_fav,btn_addfriend,nav_profile,nav_set;
	private DrawerLayout    mDrawerLayout;
	private LinearLayout    mDrawerMenu;   
	private RelativeLayout  nav_noti;
	private List<Fragment>  fragments;
	private TextView        drawer_title,main_noti_num;
	private String[]        menus;
	private View            selectedView;
	private CustomDialog    progressDialog,returnDialog,classDialog,levelDialog; 
	private DialogListener  addDialogListener,phoneListener,verifyListener,classUpListener,levelUpListener;
	private Context         mContext;
	PopupWindow             popupWindow;
	MyLocation              myLocation;
	ScanResult              scanResult;
	private final Timer     notiTimer = new Timer();
	private final Timer     tokenTimer = new Timer();
	private TimerTask       notiTask,tokenTask;
	NotiNum                 notiNum = null;
	private long            exitTime = 0;
	SUser                   myUser;
	CacheHelper             cacherHelper;
	TokenResult             tokenResult;
	double   longitude = 0.0,latitude = 0.0;
	SharedPreferences notiPreference;
    Editor notiEditor;
    private AppContext      application; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTutorial();
		setContentView(R.layout.activity_main);
		initview();
		initWidth();
		selectItem(1,nav_feed,true);
		setMenuBtnListener();
		initGetUser();
		initShare();
		initNoti();
		//initToken();
		initPhoneListener();		
	}

	private void initTutorial(){
		notiPreference = getSharedPreferences(Util.TAG, 0);
		int tutorial_code = notiPreference.getInt("tutorial", 0);
		
		if(tutorial_code == 0){
			Intent t = new Intent(MainActivity.this, TutorialActivity.class);
			startActivity(t);
			this.finish();
		}
	}
	
	private void initWidth(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		Util.SCREENHEIGHT = displayMetrics.heightPixels;
		Util.SCREENWIDTH = displayMetrics.widthPixels;
	}
	
	private void initGetUser(){
		myUser = cacherHelper.GetUser();
		if(APIManager.sharedInstance().UserID.equals("")){
			APIManager.sharedInstance().UserID = myUser.getUid();
			APIManager.sharedInstance().Token = myUser.getToken();
		}
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

	private void initview() {
		//application = (AppContext)getApplication();  
      //  application.init();  
       // application.addActivity(this);  
        
		btn_drawer = (Button) findViewById(R.id.btn_drawer);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_addfriend = (Button) findViewById(R.id.btn_addfriend);
		btn_drawer.setOnTouchListener(Util.TouchDark);
		btn_scan.setOnTouchListener(Util.TouchDark);
		btn_addfriend.setOnTouchListener(Util.TouchDark);
        
		notiEditor = notiPreference.edit();
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerMenu = (LinearLayout) findViewById(R.id.left_drawer);
		drawer_title = (TextView) findViewById(R.id.drawer_title);
		menus = getResources().getStringArray(R.array.menus);
		mContext = this;

		nav_profile = (Button) findViewById(R.id.nav_profile);
		nav_feed = (Button) findViewById(R.id.nav_feed);
		nav_noti = (RelativeLayout) findViewById(R.id.nav_noti);
		
		cacherHelper = new CacheHelper(mContext);
		notiEditor.putInt("noti_num", 0);
		
		nav_friend = (Button) findViewById(R.id.nav_friend);
		nav_prom = (Button) findViewById(R.id.nav_prom);
		nav_store = (Button) findViewById(R.id.nav_store);
		nav_fav = (Button) findViewById(R.id.nav_fav);
		nav_set = (Button) findViewById(R.id.nav_set);
		selectedView = nav_feed;
		main_noti_num = (TextView) findViewById(R.id.main_noti_num);
		progressDialog = CustomDialog.createProgressDialog(this,"Scan successful. Please wait ... ");
		
		nav_profile.setOnClickListener(new navClickListener());
		nav_feed.setOnClickListener(new navClickListener());
		nav_noti.setOnClickListener(new navClickListener());
		nav_friend.setOnClickListener(new navClickListener());
		nav_prom.setOnClickListener(new navClickListener());
		nav_store.setOnClickListener(new navClickListener());
		nav_fav.setOnClickListener(new navClickListener());
		nav_set.setOnClickListener(new navClickListener());
		
		myLocation = new MyLocation(mContext);
		longitude = myLocation.getLatitude();
		latitude = myLocation.getLongitude();
			 
		fragments = new ArrayList<Fragment>();  
	    fragments.add(ProfileFragment.newInstance("Profile"));  
	    fragments.add(FeedFragment.newInstance("Feed"));  
	    fragments.add(FeedFragment.newInstance("Feed"));
	    fragments.add(FriendFragment.newInstance("Friends"));  
	    fragments.add(PromFragment.newInstance("Promotion"));  
	    fragments.add(StoreFragment.newInstance("Stores"));
	    fragments.add(FavFragment.newInstance("Favorites"));
	    fragments.add(SettingFragment.newInstance("settings"));

	}
	
    private void initPhoneListener(){
    	phoneListener = new DialogListener()
        {
            public void showDialog(final Object o)
            {
            	new Thread(){
			    	public void run(){
			    		int code = APIManager.sharedInstance().sendPhoneNumber((String) o);
			    		Message msg = mHandler.obtainMessage();
			    		if(code == 1000){
			    			code = PHONE_SEND_SUCCESS;
			    		}
			    		msg.what = code;
			    		mHandler.sendMessage(msg);
			    	}
			    }.start();
            }
        };
        
        verifyListener = new DialogListener()
        {
            public void showDialog(final Object o)
            {
            	 new Thread(){
 				    public void run(){
 				       int code = APIManager.sharedInstance().sendCheckPhoneNumber((String) o);
 	    		       Message msg = mHandler.obtainMessage();
 	    		       if(code == 1000){
 	    			       code = VERIFY_SUCCESS;
 	    		       }
 	    		       msg.what = code;
 	    		       mHandler.sendMessage(msg);
 				   }
 				}.start();
            }
        };
    	
    }

	private class navClickListener implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			  case R.id.nav_profile:
				  selectItem(0,nav_profile,false);
				 break;
			  case R.id.nav_feed:
				  selectItem(1,nav_feed,false);
			      break;
			  case R.id.nav_noti:
				  Intent m = new Intent(MainActivity.this,MessageActivity.class);
				  startActivity(m);
				  clearNotiNum();
			      break;
			  case R.id.nav_friend:
				  selectItem(3,nav_friend,false);
			      break;
			  case R.id.nav_prom:
				  selectItem(4,nav_prom,false);
			      break;
			  case R.id.nav_store:
				  selectItem(5,nav_store,false);
			      break;
			  case R.id.nav_fav:
				  selectItem(6,nav_fav,false);
			      break;
			  case R.id.nav_set:
				  selectItem(7,nav_set,false);
			      break;
			}
		}
	}
	
	private void selectItem(int i, View nav, boolean is_init) {
	   if(nav == selectedView && !is_init){
		   mDrawerLayout.closeDrawer(mDrawerMenu);  
	   }else{
		      addFragment(i);
			  nav.setSelected(true);
			  drawer_title.setText(menus[i]);
			  if(i == 3){
				  btn_addfriend.setVisibility(View.VISIBLE);
			  }
			  if(i == 1){
				  nav_noti.setVisibility(View.VISIBLE);
			  }
			  if(!is_init){
				  selectedView.setSelected(false);
				  if(selectedView == nav_friend){
					  btn_addfriend.setVisibility(View.INVISIBLE);
				  }
				  if(selectedView == nav_feed){
					  nav_noti.setVisibility(View.INVISIBLE);
				  }
				  selectedView = nav;
				  mDrawerLayout.closeDrawer(mDrawerMenu);  
			  }
	   }
  }
	
  public void selectFromFragment(int type){
	  nav_profile.setSelected(false);
	  drawer_title.setText(menus[type]);
	  if(type == 3){
		  nav_friend.setSelected(true);
		  selectedView = nav_friend;
		  btn_addfriend.setVisibility(View.VISIBLE);
	  }
	  if(type == 0){
		  selectedView.setSelected(false);
		  nav_profile.setSelected(true);
		  selectedView = nav_profile;
		  nav_noti.setVisibility(View.GONE);
	  }
	  addFragment(type);
  }
	
  private void initShare() {
	     levelUpListener  = new DialogListener(){
			 public void showDialog(Object o) {
				  Intent t = new Intent(mContext,StoreDetailActivity.class);
                  t.putExtra("store_id", scanResult.getTrans_store_id());
                  t.putExtra("store_name", scanResult.getTrans_store_name());
                  startActivity(t);
			 }
	     };
	     
	     classUpListener  = new DialogListener(){
			 public void showDialog(Object o) {
				 if(scanResult.getNew_level()>0){
					 levelDialog = CustomDialog.createLevelUpDialog(mContext,scanResult.getNew_level(),levelUpListener);
	            	 levelDialog.show();
				 }else{
					 Intent t = new Intent(mContext,StoreDetailActivity.class);
	                  t.putExtra("store_id", scanResult.getTrans_store_id());
	                  t.putExtra("store_name", scanResult.getTrans_store_name());
	                  startActivity(t); 
				 }  
			 }
	     };
	  
		addDialogListener = new DialogListener()
        {
            public void showDialog(Object o)
            {
               if(scanResult.getNew_class()>0){
                   //class升级
            	   classDialog = CustomDialog.createClassUpDialog(mContext,scanResult.getNew_class(),scanResult.getTrans_store_name(),classUpListener);
            	   classDialog.show();
               }else if(scanResult.getNew_level()>0){
            	   // level升级
            	   levelDialog = CustomDialog.createLevelUpDialog(mContext,scanResult.getNew_level(),levelUpListener);
            	   levelDialog.show();
               }else{
            	   Intent t = new Intent(mContext,StoreDetailActivity.class);
                   t.putExtra("store_id", scanResult.getTrans_store_id());
                   t.putExtra("store_name", scanResult.getTrans_store_name());
                   startActivity(t);
               }
               
            }
        };
	}
	
   private void addFragment(int position){
	   Fragment fragment = fragments.get(position);  
	   FragmentManager manager = getSupportFragmentManager(); 
       manager.beginTransaction().replace(R.id.content_frame, fragment).commit(); 
   }
	
	private void setMenuBtnListener() {
		btn_drawer.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                if (mDrawerLayout.isDrawerOpen(mDrawerMenu)) {  
                    mDrawerLayout.closeDrawer(mDrawerMenu);  
                } else {  
                    mDrawerLayout.openDrawer(mDrawerMenu);  
                }  
            }  
        }); 
		
		btn_scan.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
            	Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
				startActivityForResult(intent,SCAN_RETURN);
            }  
        }); 
		
		btn_addfriend.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
            	btn_addfriend.getTop();
            	int y = Util.SCREENHEIGHT / 8 ;
				int x = Util.SCREENWIDTH / 8 ;
				try{
					showAddFriendWindow(x, y);	
				}catch(Exception e){
					
				}
            }
        });
	}
	
	public void showAddFriendWindow(int x, int y) {

		LinearLayout layout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_addfriend, null);
		ListView listView = (ListView) layout.findViewById(R.id.lv_dialog);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> item;
		item = new HashMap<String,Object>();
		item.put("type_name","Search by Name or Username");
		item.put("type_image",R.drawable.addfriend_icon_friend);
		data.add(item);
		/*
		item = new HashMap<String,Object>();
		item.put("type_name","Add from contacts");
		item.put("type_image",R.drawable.addfriend_icon_contact);
		data.add(item);*/
		item = new HashMap<String,Object>();
		item.put("type_name","Add friends from Weibo");
		item.put("type_image",R.drawable.addfriend_icon_weibo);
		data.add(item);
		item = new HashMap<String,Object>();
		item.put("type_name","Add friends from Facebook");
		item.put("type_image",R.drawable.addfriend_icon_facebook);
		data.add(item);
		
		listView.setAdapter(new SimpleAdapter(this,data,R.layout.list_item_addfriend_dialog,new String[]{"type_name","type_image"},new int[]{R.id.tv_text,R.id.addfriend_image}));
		popupWindow = new PopupWindow(MainActivity.this);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(Util.SCREENWIDTH / 7 * 5);
		//popupWindow.setHeight(Util.SCREENHEIGHT / 7 * 2);
		popupWindow.setHeight(Util.SCREENHEIGHT / 4);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		popupWindow.showAtLocation(findViewById(R.id.activity_main), Gravity.LEFT| Gravity.TOP, x, y);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
                if(position == 0){
                	Intent t = new Intent(MainActivity.this,SearchFriendActivity.class);
                	startActivity(t);
                }
                /*else if(position == 1 && !myUser.getIs_verified().equals("yes")){
					CustomDialog phoneDialog = CustomDialog.createPhoneDialog(mContext, phoneListener);
					phoneDialog.show();
				}*/else{
					Intent t = new Intent(MainActivity.this,AddFriendActivity.class);
					t.putExtra("type", position);
					startActivity(t);
				}
				popupWindow.dismiss();
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		  if(requestCode == PROFILE_PICTURE_CODE || requestCode == PROFILE_CROP_PICTURE || requestCode == REQUEST_CODE_SETTING || requestCode == PROFILE_GALLERY_CODE){
		    	Fragment fragment = fragments.get(0);
		    	if(data == null){
		    		data = new Intent();
		    	}
	        	fragment.onActivityResult(requestCode, resultCode, data);
	        	super.onActivityResult(requestCode, resultCode, data);	
		  }
		  if(requestCode == REQUEST_CODE_PROMOTION){
			    Fragment fragment = fragments.get(4);
	        	fragment.onActivityResult(requestCode, resultCode, data);
	        	super.onActivityResult(requestCode, resultCode, data);
		  }
		  if(requestCode == REQUEST_CODE_FAV){
			    Fragment fragment = fragments.get(6);
	        	fragment.onActivityResult(requestCode, resultCode, data);
	        	super.onActivityResult(requestCode, resultCode, data); 
		  }
		  if(requestCode == COMMENT_NUM_REFRESH){
			    Fragment fragment = fragments.get(1);
	        	fragment.onActivityResult(requestCode, resultCode, data);
	        	super.onActivityResult(requestCode, resultCode, data); 
		  }
		  
	      if (resultCode == RESULT_OK) {
	        	switch (requestCode) {
	        	case SCAN_RETURN:
	        		 String result = data.getExtras().getString("result");
	 	            if (TextUtils.isEmpty(result)) {
	 	            	Toast.makeText(getApplication(), "Scan Wrong", 1000).show();
	 	                return;
	 	            }
	 	           ShowScanDialog(result);
	 	           break;
	        	case TAKE_PICTURE_RETURN:
	        		Intent intent = new Intent(MainActivity.this, CameraActivity.class);
	        		intent.putExtra("type", "take_picture");
					startActivityForResult(intent,3);
	        		break;
	        	case CHOOSE_PICTURE_RETURN: 
	        		Intent i = new Intent(MainActivity.this, CameraActivity.class);
	        		Uri originalUri = data.getData(); 
	        		i.putExtra("image_uri", originalUri.toString());
	        		i.putExtra("type", "choose_picture");
					startActivityForResult(i,3);
	        	    break;
	         }    	
        	
	    }
	 }
	 
	 private void ShowScanDialog(final String result){
		 progressDialog.show();
		 new Thread(){
	    	 public void run(){
	    		 scanResult = APIManager.sharedInstance().scanResult(result,longitude,latitude);
	    		 Message msg = mHandler.obtainMessage();
	    		 msg.what = scanResult.getError_code();
	             mHandler.sendMessage(msg);
	    	 }
	     }.start();
	 }

	 private Handler mHandler = new Handler()
     {
         public void handleMessage(Message msg)
         {
        	 if(progressDialog.isShowing()){
        		 progressDialog.cancel(); 
        	 }	
        	 ErrorCodeHelper errorHelper = new ErrorCodeHelper(mContext);
        	 errorHelper.ScanCode(msg.what);
        	 super.handleMessage(msg);
             switch (msg.what)
             {
                 case SCAN_SUCCESS:
                	returnDialog = CustomDialog.createScanDialog(mContext,addDialogListener,scanResult.getTrans_stiqer_num(),scanResult.getTrans_egg_num());
                 	returnDialog.show();
                 	Util.profile = null;
                    break;
                    
                 case NOTI_SEND:
                	 getNotiNum();
                	 break;
                 
                 case GET_NOTI_NUM:
                	 notiEditor.putInt("noti_num", notiNum.getNoti_num());
                	 main_noti_num.setText(""+notiNum.getNoti_num());
                	 break;
                	 
                 case PHONE_SEND_SUCCESS:
                	 CustomDialog phoneDialog = CustomDialog.createPhoneVerifyDialog(mContext, verifyListener);
 					 phoneDialog.show();
                	 break;
                 
                 case TOKEN_SEND:
                	 getTokenNum();
                     break;
                    
                 case TOKEN_REFRESH_SUCCESS:
                	 //设置新token
                	 APIManager.Token = tokenResult.getNew_token();
                	 SharedPreferences sharedPreferences = getSharedPreferences(Util.TAG, 0);
                     Editor editor = sharedPreferences.edit();
                     editor.putString("token", tokenResult.getNew_token());
                     editor.commit();
                	 break;
                	 
                 case VERIFY_CODE_INVALID:
              	   Toast.makeText(mContext, "Verification code invalid", Toast.LENGTH_SHORT).show();
              	   break;
              	   
                 case VERIFY_SUCCESS:
              	     SharedPreferences sharedPreferences2 = getSharedPreferences(Util.TAG, 0);
                     Editor editor2 = sharedPreferences2.edit();
                     editor2.putString("phone_verified", "yes");
                     editor2.commit();
                     myUser.setIs_verified("yes");
              	     Intent t = new Intent(mContext,AddFriendActivity.class);
     				 t.putExtra("type", 1);
     				 startActivity(t);
              	     break;
              	   
             }
         };
     };
     
     public void initNoti(){
    	 //定时更新
    	 notiTask = new TimerTask() {
    		 @Override
    		 public void run() {
    		    Message message = new Message();
    		    message.what = NOTI_SEND;
    		    mHandler.sendMessage(message);
    		 }
    	  };
    	  notiTimer.schedule(notiTask, 0, NOTI_ALTERNATION);	 
     }
     
     public void clearNotiNum(){
    	 notiEditor.putInt("noti_num", 0);
    	 main_noti_num.setText("0");
     }
     
     public void initToken(){
    	 tokenTask = new TimerTask() {
    		 @Override
    		 public void run() {
    		    Message message = new Message();
    		    message.what = TOKEN_SEND;
    		    mHandler.sendMessage(message);
    		 }
    	  };
    	  tokenTimer.schedule(tokenTask, TOKEN_ALTERNATION, TOKEN_ALTERNATION);
     }
     
     private void getNotiNum(){
    	new Thread(){
    		public void run(){
    		    notiNum = APIManager.sharedInstance().getNotiNum();
    		    if(notiNum != null){
    		    	if(notiNum.getError_code() == 1000){
        		    	Message message = new Message();
             		    message.what = GET_NOTI_NUM;
             		    mHandler.sendMessage(message);
        		    }
    		    }
    		}
    	}.start();
     }
     
     private void getTokenNum(){
    	 new Thread(){
     		public void run(){
     			//从本地存储中获得token
     			SUser loginUser = cacherHelper.GetUser();
     			tokenResult = APIManager.sharedInstance().refreshToken(loginUser.getUid(),loginUser.getToken());
     			Message message = new Message();
     			if(tokenResult.getError_code() == 1000){
     				message.what =  TOKEN_REFRESH_SUCCESS;
     			}else{
     				message.what = tokenResult.getError_code();
     			}
     		    mHandler.sendMessage(message);
     		}
    	 }.start();
     }
     
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
        	 exit();
             return false;
         }
         return super.onKeyDown(keyCode, event);
     }
     
     public void exit() {
    	 if ((System.currentTimeMillis() - exitTime) > 2000) {
             Toast.makeText(getApplicationContext(), "Press again to exit",
                     Toast.LENGTH_SHORT).show();
             exitTime = System.currentTimeMillis();
         } else {
             finish();
             System.exit(0);
         }
     }
}
