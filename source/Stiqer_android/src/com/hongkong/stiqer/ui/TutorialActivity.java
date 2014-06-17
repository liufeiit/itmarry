package com.hongkong.stiqer.ui;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TutorialActivity extends Activity {
	
	TextView        home_text,sidebar_text,message_text,scan_text;
	Button          btn_home,btn_sidebar,btn_message,btn_tutorial_scan;
	CustomDialog    stiqerDialog;
	DialogListener  stiqerListener;
    Context         mContext;
    RelativeLayout  home_wrap, sidebar_wrap, message_wrap, scan_wrap;
    SharedPreferences  tutorialPreferencs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toturial);
		initView();
	}
	
	private void initView(){
		mContext = this;
		home_text = (TextView) findViewById(R.id.home_text);
		btn_home = (Button) findViewById(R.id.btn_home);
		sidebar_text = (TextView) findViewById(R.id.sidebar_text);
		btn_sidebar = (Button) findViewById(R.id.btn_sidebar);
		message_text = (TextView) findViewById(R.id.message_text);
		btn_message = (Button) findViewById(R.id.btn_message);
		btn_tutorial_scan = (Button) findViewById(R.id.btn_tutorial_scan);
		scan_text = (TextView) findViewById(R.id.scan_text);
		
		home_wrap = (RelativeLayout) findViewById(R.id.home_wrap);
		sidebar_wrap = (RelativeLayout) findViewById(R.id.sidebar_wrap);
		message_wrap = (RelativeLayout) findViewById(R.id.message_wrap);
		scan_wrap = (RelativeLayout) findViewById(R.id.scan_wrap);
		
		tutorialPreferencs = getSharedPreferences(Util.TAG, 0);
		home_wrap.setVisibility(View.VISIBLE);
		
		home_text.setText("This is the home screen.\nYour home screen contains\ninformation about your\nfriends’ recent history,\nsuch as purchases,\nreward redemptions, and\nphoto check-ins.");
		sidebar_text.setText("This is the sidebar. \nAccess the most used \nfunctions--profile, friends, \nstores, and more.");
		message_text.setText("These are your messages.\nCheck back here often! ");
		scan_text.setText("This is the scan button.\nPress this to scan QR codes \nat your favorite stores!");
		
		btn_home.setOnTouchListener(Util.TouchDark);
		btn_home.setOnClickListener(new MyOnClickListener());
		btn_sidebar.setOnTouchListener(Util.TouchDark);
		btn_sidebar.setOnClickListener(new MyOnClickListener());
		btn_message.setOnTouchListener(Util.TouchDark);
		btn_message.setOnClickListener(new MyOnClickListener());
		btn_tutorial_scan.setOnTouchListener(Util.TouchDark);
		btn_tutorial_scan.setOnClickListener(new MyOnClickListener());
		
		stiqerListener = new DialogListener(){
			public void showDialog(Object o) {
				
				Editor editor = tutorialPreferencs.edit();
				editor.putInt("tutorial", 1);
				editor.commit();
				
				Intent t = new Intent(TutorialActivity.this,MainActivity.class);
				startActivity(t);
				TutorialActivity.this.finish();
			}
		};
		
		stiqerDialog = CustomDialog.createTutorialDialog(mContext,stiqerListener);
	} 
	
	class MyOnClickListener implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_home:
				home_wrap.setVisibility(View.GONE);
				sidebar_wrap.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_sidebar:
				sidebar_wrap.setVisibility(View.GONE);
				message_wrap.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_message:
				message_wrap.setVisibility(View.GONE);
				scan_wrap.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_tutorial_scan:
				stiqerDialog.show();
				scan_wrap.setVisibility(View.GONE);
				break;
			}
		}
	}
	
	//禁止后退
	@Override     
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){      
	       return true;      
	    }  
	   return super.onKeyDown(keyCode, event);      
	}  
}
