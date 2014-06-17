package com.guide.geek.activity;

import com.guide.geek.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LoginActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		// Request a window feature to show progress bar in the application
		// title
		requestWindowFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.user_reg);
		
	}


}
