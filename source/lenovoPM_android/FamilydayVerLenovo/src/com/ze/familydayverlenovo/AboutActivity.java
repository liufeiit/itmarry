package com.ze.familydayverlenovo;

import com.umeng.analytics.MobclickAgent;
import com.ze.commontool.PublicInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;



public class AboutActivity extends Activity {
	private int 			flag;
	private Button	  picImageView;
	private View 			back;
	private boolean 	isHorizontal = false;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPause(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		picImageView = (Button)findViewById(R.id.about_infopic);
		back = findViewById(R.id.about_back);
		PublicInfo.SCREEN_W = getWindowManager().getDefaultDisplay().getWidth();
		PublicInfo.SCREEN_H = getWindowManager().getDefaultDisplay().getHeight();
		if( PublicInfo.SCREEN_H > PublicInfo.SCREEN_W )
		{
			isHorizontal = false;
		}else
		{
			isHorizontal = true;
		}
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutActivity.this.finish();
			}
		});
		flag = getIntent().getIntExtra("infopic", 0);
		LayoutParams params = null;
		int height= 0;
		int width = 0;
		switch (flag) {
		case PublicInfo.INFOPIC_ABOUT:
			if( isHorizontal )
			{
				// 1280 x 800
				picImageView.setBackgroundResource(R.drawable.about_family_l);
			}else {
				picImageView.setBackgroundResource(R.drawable.about_family_p);
			}
			break;
		case PublicInfo.INFOPIC_VIP:
			if( isHorizontal )
			{
				picImageView.setBackgroundResource(R.drawable.vip_info_l);
			}else
			{
				picImageView.setBackgroundResource(R.drawable.vip_info_p);
			}
			
			break;
		case PublicInfo.INFOPIC_COIN:
			if( isHorizontal )
			{
				picImageView.setBackgroundResource(R.drawable.coin_use_l);
			}else
			{
				picImageView.setBackgroundResource(R.drawable.coin_use_p);
			}
			
			break;
		default:
			break;
		}
		if( isHorizontal )
		{
			height = (int) ((800f/1280f)*PublicInfo.SCREEN_W);
		}else{
			height = (int) ((1280f/800f)*PublicInfo.SCREEN_W);
		}
		params = new LayoutParams(PublicInfo.SCREEN_W, height);
		if( params != null )
			picImageView.setLayoutParams(params);
	}
}
