package com.hongkong.stiqer.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.tab.AchievementActivity;
import com.hongkong.stiqer.tab.ActsActivity;
import com.hongkong.stiqer.tab.PhotoCheckActivity;
import com.hongkong.stiqer.ui.base.BaseActivity;


public class TabUserActivity extends TabActivity {
	
	private TabHost tabHost;
	private TabHost.TabSpec spec;
	private Intent intent;
	private String achieve_str,activity_str,checkin_str,to_uid;
	private Button back_btn;
	private int    position;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabuser);
		initData();
		initView();
		initTab(); 
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
	
	private void initView() {
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				TabUserActivity.this.finish();
			}
		});
	}

	private void initData() {
		Intent t = getIntent();
		achieve_str = t.getStringExtra("achievements");
		activity_str = t.getStringExtra("activity");
		checkin_str = t.getStringExtra("checkin");
		position = t.getIntExtra("position",0);
		to_uid = t.getStringExtra("to_uid");
	}
	
	private void initTab() {
		tabHost=this.getTabHost();
		intent=new Intent().setClass(this, PhotoCheckActivity.class);
		intent.putExtra("checkin", checkin_str);
		intent.putExtra("to_uid", to_uid);
        spec=tabHost.newTabSpec("Photo Checkin").setIndicator("Photo Checkin").setContent(intent);
        tabHost.addTab(spec);
                
        intent=new Intent().setClass(this, AchievementActivity.class);
        intent.putExtra("achievements", achieve_str);
        spec=tabHost.newTabSpec("Achievements").setIndicator("Achievements").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, ActsActivity.class);
        intent.putExtra("activity", activity_str);
        intent.putExtra("to_uid", to_uid);
        spec=tabHost.newTabSpec("Activities").setIndicator("Activities").setContent(intent);
        tabHost.addTab(spec);
        if(position==2){
        	tabHost.setCurrentTabByTag("Activities");
        }
        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.user_tab_photo:
					tabHost.setCurrentTabByTag("Photo Checkin");
					break;
				case R.id.user_tab_achieve:
					tabHost.setCurrentTabByTag("Achievements");
					break;
				case R.id.user_tab_act:
					tabHost.setCurrentTabByTag("Activities");
					break;
				default:
					break;
				}
			}
		});
	}

}
