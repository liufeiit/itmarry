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
import com.hongkong.stiqer.tab.RankActivity;
import com.hongkong.stiqer.tab.StorePhotoActivity;
import com.hongkong.stiqer.ui.base.BaseActivity;


public class TabStoreActivity extends TabActivity {
	
	private TabHost tabHost;
	private TabHost.TabSpec spec;
	private Intent intent;
	private String rank_str,checkin_str,store_id;
	private Button back_btn;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabstore);
		initData();
		initView();
		initTab(); 
	}
	
	private void initView() {
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				TabStoreActivity.this.finish();
			}
		});
	}

	private void initData() {
		Intent t = getIntent();
		rank_str = t.getStringExtra("rank");
		checkin_str = t.getStringExtra("checkin");
		store_id = t.getStringExtra("store_id");
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
	
	private void initTab() {
		tabHost=this.getTabHost();
		intent=new Intent().setClass(this, StorePhotoActivity.class);
		intent.putExtra("checkin", checkin_str);
		intent.putExtra("store_id", store_id);
        spec=tabHost.newTabSpec("Photo Checkin").setIndicator("Photo Checkin").setContent(intent);
        tabHost.addTab(spec);
                
        intent=new Intent().setClass(this, RankActivity.class);
        intent.putExtra("rank", rank_str);
        intent.putExtra("store_id", store_id);
        spec=tabHost.newTabSpec("Rank").setIndicator("Rank").setContent(intent);
        tabHost.addTab(spec);
        

        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.user_tab_photo:
					tabHost.setCurrentTabByTag("Photo Checkin");
					break;
				case R.id.user_tab_rank:
					tabHost.setCurrentTabByTag("Rank");
					break;
				default:
					break;
				}
			}
		});
	}

}
