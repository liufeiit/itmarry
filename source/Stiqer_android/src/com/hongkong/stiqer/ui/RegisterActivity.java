package com.hongkong.stiqer.ui;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	public static final int  USERNAME_EXIST = 1004;
	public static final int  EMAIL_EXIST = 1005;
	public static final int  REGISTER_SUCCESS = 1006;
    
	EditText      EditUserName;
	EditText      EditEmail;
	EditText      EditPassword;
	EditText      EditRePassword;
	Button        BtnSubmit;
	CustomDialog  progressDialog;	
	SUser          user;
 	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		EditUserName = (EditText) findViewById(R.id.register_UserName);
		EditEmail = (EditText) findViewById(R.id.register_Email);
        EditPassword = (EditText) findViewById(R.id.register_Password);
        EditRePassword = (EditText) findViewById(R.id.register_rePassword);
        BtnSubmit = (Button) findViewById(R.id.register_Submit);
        
        BtnSubmit.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(!EditPassword.getText().toString().equals(EditRePassword.getText().toString())){
					 showToast(getString(R.string.repass_mismatch));
	                 return;
				 }
				 if (TextUtils.isEmpty(EditUserName.getText().toString()))
	              {
                    showToast(getString(R.string.username_empty));
                    return;
	              }
				if (TextUtils.isEmpty(EditEmail.getText().toString()))
	             {
                    showToast(getString(R.string.email_empty));
                    return;
	             }
				if(!Util.isEmail(EditEmail.getText().toString()))
				{
					showToast(getString(R.string.email_mismatch));
                    return;
				}
				if (TextUtils.isEmpty(EditPassword.getText().toString()))
                {
                    showToast(getString(R.string.password_empty));
                    return;
                }
				
				progressDialog = CustomDialog.createProgressDialog(RegisterActivity.this,getString(R.string.register_loading));
                progressDialog.show();
				//注册过程
                
                new Thread(){
                	public void run(){
                		//user = APIManager.sharedInstance().register(EditUserName.getText().toString(),EditEmail.getText().toString(), EditPassword.getText().toString());
                		if(user.getError_code()==REGISTER_SUCCESS){
                			APIManager.user.setUsername(EditUserName.getText().toString());
                		}
                		Message msg = mHandler.obtainMessage();
                        msg.what = user.getError_code();
                        //msg.what = 1;
                        mHandler.sendMessage(msg);
                	}
                }.start();
			}
        });
	}

    private Handler mHandler = new Handler()
    {
    	public void handleMessage(Message msg)
        {
    		super.handleMessage(msg);
            progressDialog.cancel();
    		switch (msg.what)
            {
              case REGISTER_SUCCESS:
            	  SharedPreferences sharedPreferences = getSharedPreferences(Util.TAG, 0);
                  Editor editor = sharedPreferences.edit();
                  editor.putString("token", user.getToken());
                  editor.commit();
                  Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                  startActivity(intent);
                  finish();
                  overridePendingTransition(R.anim.main_show, R.anim.login_hide);
                break;
              case USERNAME_EXIST:
                 showToast(getString(R.string.reg_username_exist));
                 break;
              case EMAIL_EXIST:
                  showToast(getString(R.string.reg_email_exist));
                  break;
            }  
        }
    };
}
