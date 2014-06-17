package com.hongkong.stiqer.ui;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class CheckPhoneActivity extends BaseActivity {
	private static final int      VERIFY_SUCCESS = 1040;
	private static final int      VERIFY_CODE_INVALID = 1033;
	
	Button     btnPhone, btnCheck;
	EditText   phoneNumber, checkCode;
	static final String[] string_quhao={"+852","+86"};
	private ArrayAdapter<String> quhaoAdapter;
	Spinner quhao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkphone);
		initView();
	}
	
	public void initView(){
		
		btnPhone = (Button) findViewById(R.id.btn_input_phone);
		btnCheck = (Button) findViewById(R.id.btn_check_phone);
		phoneNumber = (EditText) findViewById(R.id.input_phone_number);
		checkCode = (EditText) findViewById(R.id.check_phone_number);
		
		quhao=(Spinner) findViewById(R.id.phone_quhao);   
		quhaoAdapter=new ArrayAdapter<String>(this,R.layout.profile_spinner_item,string_quhao);  
		quhaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        quhao.setAdapter(quhaoAdapter);  
        
		btnPhone.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if((int) quhao.getSelectedItemId() == 0 && phoneNumber.getText().toString().length() != 8){
					Toast.makeText(CheckPhoneActivity.this, "telphone number mismatch", Toast.LENGTH_SHORT).show();
					return;
				}
				if((int) quhao.getSelectedItemId() == 1 && phoneNumber.getText().toString().length() != 11){
					Toast.makeText(CheckPhoneActivity.this, "telphone number mismatch", Toast.LENGTH_SHORT).show();
					return;
				}
			    new Thread(){
			    	public void run(){
			    		int code = APIManager.sharedInstance().sendPhoneNumber(string_quhao[(int) quhao.getSelectedItemId()]+phoneNumber.getText().toString());
			    		Message msg = mHandler.obtainMessage();
			    		msg.what = code;
			    		mHandler.sendMessage(msg);
			    	}
			    }.start();
			}
		});
		
		btnCheck.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				 new Thread(){
				    public void run(){
				       int code = APIManager.sharedInstance().sendCheckPhoneNumber(checkCode.getText().toString());
	    		       Message msg = mHandler.obtainMessage();
	    		       if(code == 1000){
	    			       code = VERIFY_SUCCESS;
	    		       }
	    		       msg.what = code;
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
        	 ErrorCodeHelper errorHelper = new ErrorCodeHelper(CheckPhoneActivity.this);
    		 errorHelper.CommonCode(msg.what);
        	 super.handleMessage(msg);
             switch (msg.what)
             {
               case VERIFY_CODE_INVALID:
            	   Toast.makeText(CheckPhoneActivity.this, "Verification code invalid", Toast.LENGTH_SHORT).show();
            	   break;
            	   
               case VERIFY_SUCCESS:
            	   SharedPreferences sharedPreferences = getSharedPreferences(Util.TAG, 0);
                   Editor editor = sharedPreferences.edit();
                   editor.putString("phone_verified", "yes");
                   editor.commit();
            	   Intent t = new Intent(CheckPhoneActivity.this,AddFriendActivity.class);
   				   t.putExtra("type", 2);
   				   startActivity(t);
   				   CheckPhoneActivity.this.finish();
            	   break;
             }
         }
     };
}
