package com.hongkong.stiqer.tab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.AchievementAdapter;
import com.hongkong.stiqer.ui.base.BaseActivity;

public class AchievementActivity extends BaseActivity {
	
	String      achievements;
	GridView    achieve_gridview;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity_achieve);
		initData();
		initView();
	}

	private void initData() {
		Intent t = getIntent();
		achievements = t.getStringExtra("achievements");
	}

	private void initView() {
		achieve_gridview = (GridView) findViewById(R.id.achieve_gridview);
		if(!achievements.equals("")){
			achieve_gridview.setAdapter(new AchievementAdapter(this,achievements,2));
		}
	}
}
