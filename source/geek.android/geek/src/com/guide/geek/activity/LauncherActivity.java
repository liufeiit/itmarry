package com.guide.geek.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guide.geek.R;

public class LauncherActivity extends Activity{
	
	private WebView webView;  
	    
  
    private static final String TAG = "LauncherActivity";   
  	private int status  = 0;  
   
  	private static final int  STOPSPLASH = 0; 
  	//time in milliseconds 
  	private static final long SPLASHTIME = 1000; 
  	
  	private LinearLayout splash; 
  	private TextView tv; 
  
  	private Animation myAnimation_Alpha; 
  	private Animation animatinoGone ; 
  
  	private Handler splashHandler = new Handler() { 
	    public void handleMessage(Message msg) { 
	         switch (msg.what) { 
	         case STOPSPLASH: 
	              if( status == 1 ){                
	                splash.startAnimation(animatinoGone); 
	                splash.setVisibility(View.GONE); 
	                break; 
	              } 
	              sendEmptyMessageDelayed(STOPSPLASH, SPLASHTIME); 
	         } 
	         super.handleMessage(msg); 
	    } 
	}; 
  
	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState); 

		requestWindowFeature(Window.FEATURE_NO_TITLE); //去标题栏 
	    getWindow().requestFeature(Window.FEATURE_PROGRESS); 
	    setContentView(R.layout.splash);    
	    animatinoGone = AnimationUtils.loadAnimation(this,R.anim.alpha_gone); //动画效果 
	    myAnimation_Alpha = AnimationUtils.loadAnimation(this,R.anim.alpha_action); //动画效果 
	    
	    splash = (LinearLayout) findViewById(R.id.splash_screen); 
	    tv = (TextView) findViewById(R.id.splash_info); 
	    tv.setText("正在建立数据连接"); 
	    splash.startAnimation(myAnimation_Alpha); 
	    status = 1;
	    Message msg = new Message(); 
	    msg.what = STOPSPLASH; 
	    splashHandler.sendMessageDelayed(msg, SPLASHTIME); 
	}

}
