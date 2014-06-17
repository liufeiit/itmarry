package com.hongkong.stiqer.ui;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.utils.Util;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {
    
	private final int SPLASH_DISPLAY_LENGTH = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		//get the width and height
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		Util.SCREENHEIGHT = displayMetrics.heightPixels;
		Util.SCREENWIDTH = displayMetrics.widthPixels;
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (Util.hasLogin) {
					Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					finish();
					overridePendingTransition(R.anim.main_show, R.anim.splash_hide);
				} else {
					Intent mainIntent = new Intent(SplashActivity.this, GuideActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					finish();
					overridePendingTransition(R.anim.main_show, R.anim.splash_hide);
				}
			}
		}, SPLASH_DISPLAY_LENGTH);	
	}
}
