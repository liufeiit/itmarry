package com.gaoxiaoxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class LogoActivity extends Activity {

	private ImageView logo;
	private ImageView title;
	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		exit();
		logo = (ImageView) findViewById(R.id.logo);
		title = (ImageView) findViewById(R.id.title);
		logo.setVisibility(View.VISIBLE);
		logo.startAnimation(getAnimSet());
		handler=new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				new Thread(){
					public void run() {
						super.run();
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						exit();
					}
				}.start();
			}
		};
	}

	private AnimationSet getAnimSet() {
		AnimationSet set = new AnimationSet(false);
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		ScaleAnimation scale = new ScaleAnimation(0, 1.2f, 0, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setFillAfter(true);
		scale.setInterpolator(new OvershootInterpolator(3f));
		scale.setDuration(2000);
		scale.setFillAfter(true);
		alpha.setDuration(1000);
		alpha.setFillAfter(true);
		set.addAnimation(alpha);
		set.addAnimation(scale);
		set.setFillAfter(true);
		set.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				TranslateAnimation trans = new TranslateAnimation(
						Animation.RELATIVE_TO_PARENT, 0,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_PARENT, 0.5f,
						Animation.RELATIVE_TO_SELF, 0);
				trans.setDuration(1000);
				trans.setFillAfter(true);
				trans.setInterpolator(new AccelerateInterpolator());
				trans.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						RotateAnimation rotate = new RotateAnimation(-5f, 5f,
								Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f);
						rotate.setDuration(50);
						rotate.setRepeatCount(6);
						rotate.setRepeatMode(Animation.REVERSE);
						title.startAnimation(rotate);
						handler.sendEmptyMessage(0);
					}
				});
				title.setVisibility(View.VISIBLE);
				title.startAnimation(trans);
			}
		});
		return set;
	}

	@Override
	public void onBackPressed() {
	}
	
	public void exit(){
		Intent intent=new Intent();
		intent.setClass(LogoActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.activity_anim_alpha_in, R.anim.activity_anim_alpha_out);
	}
}
