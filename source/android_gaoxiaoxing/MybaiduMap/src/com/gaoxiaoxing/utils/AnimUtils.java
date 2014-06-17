package com.gaoxiaoxing.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimUtils {
	private static float fromXValues[] = { 0.7f, 0.6f, 0.4f, 0f };
	private static float fromYValues[] = { 0f, -0.3f, -0.5f, -0.7f };
	private static float toXValue = 0;
	private static float toYValue = 0;
	private static int duration=1000;

	public static Animation makeTranslate(int index) {
		
		TranslateAnimation anim = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, fromXValues[index],
				Animation.RELATIVE_TO_SELF, toXValue,
				Animation.RELATIVE_TO_PARENT, fromYValues[index],
				Animation.RELATIVE_TO_SELF, toYValue);
		anim.setDuration(duration);
		anim.setInterpolator(new OvershootInterpolator());
		return anim;
	}
	
	public static Animation makeAlpha(){
		AlphaAnimation anim=new AlphaAnimation(0, 1);
		anim.setDuration(200);
		return anim;
	}
	
}
