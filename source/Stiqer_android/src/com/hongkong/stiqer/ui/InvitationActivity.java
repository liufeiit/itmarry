package com.hongkong.stiqer.ui;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.Util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class InvitationActivity extends BaseActivity {
	public static final int  CONNECTION_ERROR = 999;
	Button    invite_btn;
	EditText  editText;
	SharedPreferences invitePreferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitation);
		invitePreferences = getSharedPreferences(Util.TAG, 0);
		invite_btn = (Button) findViewById(R.id.invite_btn);
		invite_btn.setOnClickListener(new MyOnClickListener());
		invite_btn.setOnTouchListener(Util.TouchDark);
		editText = (EditText) findViewById(R.id.code_txt);
	}
	
	class MyOnClickListener implements OnClickListener{
		public void onClick(View v) {
			new Thread(){
				public void run(){
				    int code = APIManager.sharedInstance().checkInvitation(editText.getText().toString());
					Message msg = mHandler.obtainMessage();
		            msg.what = code;
		            mHandler.sendMessage(msg);
				}
			}.start();
		}
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
	 private Handler mHandler = new Handler()
     {
		 
         public void handleMessage(Message msg)
         {
        	 super.handleMessage(msg);
             switch (msg.what)
             {
                 case CONNECTION_ERROR:
            	    showToast(getString(R.string.connection_error));
            	    break;
                 
                 case 2000:
                	 showToast("Invitation ok");
                     Editor editor = invitePreferences.edit();
                     editor.putString("invitation", editText.getText().toString());
                     editor.commit();
                     Intent t = new Intent(InvitationActivity.this,GuideActivity.class);
                     startActivity(t);
                     finish();
                     break;
                     
                 case 2001:
                	 showToast("Invitation code not found");
                	 break;
                	 
                 case 2002:
                	 showToast("Invitation invalid");
                	 break;
             }
         }
     };
     
     public boolean dispatchKeyEvent(KeyEvent event) {
    	 if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) 
    	 {     
    	    return true;
    	 }
         return super.dispatchKeyEvent(event);
    }
	
}
