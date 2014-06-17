package com.xiyili.animation;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ValueAnimatorDemoActivity extends Activity {
	
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_value_animator_demo);
		button = (Button)findViewById(R.id.button);
	}

	private void testNonSenseValueAnimator() {
		ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
		anim.setDuration(500);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = (Float)animation.getAnimatedValue();
				Log.d("onAnimationUpdate", "value="+value);
			}
		});
		anim.start();
	}

	public void onClick(View v){
		
		int b = 0;
		int c = 1;
		int a = b;
	}
	
}
