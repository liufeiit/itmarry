package com.hongkong.stiqer.ui;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.AchievementAdapter;
import com.hongkong.stiqer.adapter.CheckInAdapter;
import com.hongkong.stiqer.adapter.ProfileActivityAdapter;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.FriendStatus;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.Defs;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.ExpandableHeightGridView;
import com.hongkong.stiqer.widget.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;

public class UserActivity extends Activity {
	public static final int        SEND_MESSAGE_SUCCESS = 105;
	public static final int        SHOW_BIG_AVATAR = 102;
	public static final int        CHANGE_AVATAR = 103;
	public static final int        CHANGE_SETTING = 104;
	public static final int        CONNECTION_ERROR = 0;
	public static final int        ACTION_SUCCESSFUL = 1000;
	public static final int        MESSAGE_SUCCESSFUL = 1040;
	public static final int        ALREADY_SEND = 1021;
	public static final int        ALREADY_FRIEND = 1022;
	public static final int        FRIEND_ACTION_SUCCESS = 1030;
	private static final int       CAN_NOT_SEND = 1033;
	private static final int       USER_NOT_FOUND = 1034;
	
	public static final String     TAG = "Siqer";  
	private static final Uri PROFILE_URI = Uri.parse(Defs.MENTIONS_SCHEMA);
    View                           view,lv_layout;  
    ExpandableHeightGridView       gridview_hide;
    ProfileActivityAdapter         padapter;
    ExpandableHeightListView       activity_list;
    LinearLayout                   setting_btn,send_message_btn,pl_wrap,friend_btn;
    Gallery                        check_gallery, achieve_gallery;
    CustomDialog                   messageDialog,processingDialog,bigImageDialog,settingDialog;
    DialogListener                 messageDialogListener,settingDialogListener;
    ProgressBar                    pl_progressbar,bottom_progress;
    TextView                       total_level,pl_earn_num,next_class_txt,user_title,des_first_text,des_second_text,des_third_text,friend_status_text,btn_more_activity,btn_more_photo;
    LayoutInflater                 mInflater;
    List<Pactivity>                load_al,first_al,second_al;
    ImageView                      pl_avatar,des_first_dot,des_second_dot,des_third_dot,next_class_icon;
    Context                        mContext;
    Button                         btnBack;
    String                         username,uid,avatar_uri,activity_str,achieve_str,checkin_str,friend_status,user_profile_img;
    int                            is_receive,is_seen;
    boolean                        is_invite_send = false;
    FriendStatus                   friendStatus;
    ErrorCodeHelper                errorHelper;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		initUser();
		initView();  
		initDialog();
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
	
	private void initUser() {
		Intent t = getIntent();
		username = t.getStringExtra("username");
		if(username == null){
			Uri uri = getIntent().getData();
			if (uri !=null&& PROFILE_URI.getScheme().equals(uri.getScheme())) {
			   username = uri.getQueryParameter(Defs.PARAM_UID);
		     }
		}
	}

	private void initView() {
    	pl_progressbar = (ProgressBar) findViewById(R.id.pl_progressbar);
    	bottom_progress = (ProgressBar) findViewById(R.id.bottom_progressbar);
    	user_title = (TextView) findViewById(R.id.user_title);
    	user_title.setText(username);
    	next_class_icon = (ImageView) findViewById(R.id.user_next_class_icon);
    	pl_avatar = (ImageView) findViewById(R.id.pl_avatar);
    	pl_wrap = (LinearLayout) findViewById(R.id.pl_layout_wrap);
    	total_level = (TextView) findViewById(R.id.pl_total_level);
    	pl_earn_num = (TextView) findViewById(R.id.pl_earn_num);
    	next_class_txt = (TextView) findViewById(R.id.user_next_class_txt);
    	achieve_gallery = (Gallery) findViewById(R.id.pf_achieve_gallery);
    	check_gallery = (Gallery) findViewById(R.id.pf_check_gallery);    
    	friend_status_text = (TextView) findViewById(R.id.friend_status_text);
    	friend_btn = (LinearLayout) findViewById(R.id.friend_btn);
    	activity_list = (ExpandableHeightListView) findViewById(R.id.pl_activity_listview);
    	btn_more_activity = (TextView) findViewById(R.id.btn_more_activity);
    	btn_more_photo = (TextView) findViewById(R.id.btn_more_photo);
    	
    	btn_more_photo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    	btn_more_activity.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    	
    	des_first_dot = (ImageView) findViewById(R.id.user_des_first_dot);
        des_second_dot = (ImageView) findViewById(R.id.user_des_second_dot);
        des_third_dot = (ImageView) findViewById(R.id.user_des_third_dot);
        des_first_text = (TextView) findViewById(R.id.user_des_first_text);
        des_second_text = (TextView) findViewById(R.id.user_des_second_text);
        des_third_text = (TextView) findViewById(R.id.user_des_third_text);
        
    	mContext = this;
    	errorHelper = new ErrorCodeHelper(mContext);
    	mInflater = LayoutInflater.from(mContext);
        setting_btn = (LinearLayout) findViewById(R.id.setting_btn);
        send_message_btn = (LinearLayout) findViewById(R.id.send_message_btn);
        btnBack = (Button) findViewById(R.id.user_back_btn);
        
		btnBack.setOnClickListener(new MyOnClickListener());
        send_message_btn.setOnClickListener(new MyOnClickListener());
        setting_btn.setOnClickListener(new MyOnClickListener());
        pl_avatar.setOnClickListener(new MyOnClickListener());
        friend_btn.setOnClickListener(new MyOnClickListener());
        btn_more_activity.setOnClickListener(new MyOnClickListener());
        btn_more_photo.setOnClickListener(new MyOnClickListener());
        
        btnBack.setOnTouchListener(Util.TouchDark);
        send_message_btn.setOnTouchListener(Util.TouchDark);
        setting_btn.setOnTouchListener(Util.TouchDark);
        pl_avatar.setOnTouchListener(Util.TouchDark);
        friend_btn.setOnTouchListener(Util.TouchDark);
        btn_more_activity.setOnTouchListener(Util.TouchDark);
        btn_more_photo.setOnTouchListener(Util.TouchDark);
     }
	
	private void initDialog(){
		messageDialogListener = new DialogListener(){
			public void showDialog(final Object o) {
				new Thread(){
					public void run(){
						int code = new APIManager().sendMessage(username,(String) o);
						Message msg = mHandler.obtainMessage();
						if(code == ACTION_SUCCESSFUL){
							code = MESSAGE_SUCCESSFUL;
						}
						msg.what = code;
						mHandler.sendMessage(msg);
					}
				}.start();
			}
        };
        
        settingDialogListener = new DialogListener(){
			public void showDialog(final Object o) {
				new Thread(){
					public void run(){
						String p[] = ((String) o).split(",");
						int code = APIManager.sharedInstance().sendUserSetting(username,Integer.parseInt(p[0]),Integer.parseInt(p[1]));
						Message msg = mHandler.obtainMessage();
						msg.what = code;
					    mHandler.sendMessage(msg);
					}
				}.start();
			}
        }; 
	}
	
	class MyOnClickListener implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.pl_avatar:
				bigImageDialog = CustomDialog.createBigImageDialog(mContext,user_profile_img,user_profile_img);
	 			bigImageDialog.show();
				break;
			case R.id.send_message_btn:
				messageDialog = CustomDialog.createMessageDialog(mContext,messageDialogListener,username,user_profile_img);
				messageDialog.show();
				break;
			case R.id.user_back_btn:
				UserActivity.this.finish();
				break;
			case R.id.setting_btn:
				settingDialog = CustomDialog.createSettingDialog(mContext,settingDialogListener,is_receive,is_seen);
				settingDialog.show();
				break;
			case R.id.friend_btn:
				if(is_invite_send && !friend_status.equals("pending")){
					Toast.makeText(mContext, "Invitation has sent", 2000).show();
				}else{
				    new Thread(){
				    	public void run(){
				    		if(friend_status.equals("friend")){
				    			//remove
				    			friendStatus = APIManager.sharedInstance().deleteFriend(username);
				    		}else{
				    			friendStatus = APIManager.sharedInstance().addFriend(username);
				    			//add friend
				    		}
				    	   	Message msg = mHandler.obtainMessage();
				    	   	if(friendStatus.getError_code() == ACTION_SUCCESSFUL){
				    	   		msg.what = FRIEND_ACTION_SUCCESS;
				    	   	}else{
				    	   		msg.what = friendStatus.getError_code();	
				    	   	}
						    mHandler.sendMessage(msg);
				    	}
				    }.start();
				}
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
		Intent t = new Intent(UserActivity.this,TabUserActivity.class);
		t.putExtra("activity", activity_str);
		t.putExtra("achievements", achieve_str);
		t.putExtra("checkin", checkin_str);
		t.putExtra("position", position);
		t.putExtra("to_uid", username);
		startActivity(t);
	}

	 private class GetDataTask extends AsyncTask<Integer[] ,Void, JSONObject>{
	    	
			protected void onPostExecute(JSONObject obj) {
				pl_progressbar.setVisibility(View.GONE);
				ErrorCodeHelper errorHelper = new ErrorCodeHelper(mContext);
				if(obj == null){
					errorHelper.connectError();
				}else{	
				    try {
				      if(errorHelper.CommonCode(obj.getInt("error_code"))){

				    	 JSONObject user_info = obj.getJSONObject("user_info");
						 total_level.setText("Level "+user_info.getInt("user_level"));
						 pl_earn_num.setText(user_info.getInt("egg_num")+" Earned");

						 next_class_icon.setImageResource(next_class_ids[user_info.getInt("user_levelup")]);
						 next_class_txt.setText(next_class_array[user_info.getInt("user_levelup")]);
						 if(!user_info.getString("achievements").equals("")){
						    achieve_gallery.setAdapter(new AchievementAdapter(mContext,user_info.getString("achievements"),1));        
						 }
						 Picasso.with(mContext).load(user_info.getString("profile_img")).into(pl_avatar);
						 user_profile_img = user_info.getString("profile_img");
						 if(!user_info.getString("user_des").equals("")){
							des_first_dot.setVisibility(View.VISIBLE);
							des_first_text.setText(user_info.getString("user_des"));
						 }
						 if(obj.getString("relation").equals("friend")){
					    	friend_status_text.setText("Remove Friend");
					     }else{
						   friend_status_text.setText("Add Friend");
				 	     }
					     friend_status = obj.getString("relation");
						
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
					    is_receive = obj.getInt("is_pulling");
					    is_seen = obj.getInt("is_pushing");
					   
					    pl_wrap.setVisibility(View.VISIBLE);
					    
				     }
				      
				} catch (JSONException e) {
					e.printStackTrace();
				}
			 }		 
				super.onPostExecute(obj);
			}
	    	
			@Override
			protected JSONObject doInBackground(Integer[]... arg0) {
				
				return APIManager.sharedInstance().getProfileDetail(username);
			}
	 }
	 
	 String[] next_class_array = {
			 "0% to Next Level","25% to Next Level","50% to Next Level","75% to Next Level"
     };
    
     Integer[] next_class_ids = {
    	R.drawable.level_bar_0,R.drawable.level_bar_25, R.drawable.level_bar_50,
		R.drawable.level_bar_75
     };
	  
	public Handler mHandler = new Handler(){
		 	public void handleMessage(Message msg){
		 		super.handleMessage(msg);
		 		errorHelper.CommonCode(msg.what);
		 		switch(msg.what){
		 		    case SEND_MESSAGE_SUCCESS:
		 			processingDialog.dismiss();
		 			Toast.makeText(mContext, "send successful", Toast.LENGTH_LONG).show();
		 			break;
		 		
		 		case CHANGE_SETTING:
		 		    Toast.makeText(mContext, "Modify successful", Toast.LENGTH_LONG).show();
		 			break;
	    			
		 		case ACTION_SUCCESSFUL:
		 			Toast.makeText(mContext, "success!", Toast.LENGTH_LONG).show();
		 			break;
		 		case FRIEND_ACTION_SUCCESS:
		 			if(friend_status.equals("friend")){
		 				//删除
		 				Toast.makeText(mContext, "remove success!", Toast.LENGTH_LONG).show();
			 			friend_status = "stranger";
			 			friend_status_text.setText("Add Friend");
		 			}else{
		 				//add
		 				if(friendStatus.getFriend_state() == 0){
		 					//pending
		 					Toast.makeText(mContext, "Request send!", Toast.LENGTH_LONG).show();
				 			friend_status = "pending";
				 			friend_status_text.setText("Add Friend");
		 				}else{
		 					Toast.makeText(mContext, "Friend add successful!", Toast.LENGTH_LONG).show();
				 			friend_status = "friend";
				 			friend_status_text.setText("Remove Friend");
		 				}
		 			}
		 			break;
		 			
		 		case ALREADY_SEND:
		 			Toast.makeText(mContext, "already send!", Toast.LENGTH_LONG).show();
		 			break;
		 			
		 		case ALREADY_FRIEND:
		 			Toast.makeText(mContext, "already friend!", Toast.LENGTH_LONG).show();
		 			break;
		 			
		 		case CAN_NOT_SEND:
		 			Toast.makeText(mContext, "can't send to this user", Toast.LENGTH_LONG).show();
		 			break;
		 			
		 		case USER_NOT_FOUND:
		 			Toast.makeText(mContext, "user not found", Toast.LENGTH_LONG).show();
		 			break;
		 			
		 		case MESSAGE_SUCCESSFUL:
		 			Toast.makeText(mContext, "message send successful", Toast.LENGTH_LONG).show();
		 			break;
		 		}
		 	}
		 };
}
