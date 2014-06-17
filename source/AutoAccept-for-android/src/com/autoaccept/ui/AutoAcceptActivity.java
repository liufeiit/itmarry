package com.autoaccept.ui;  
  
import android.app.Activity;  
import android.content.Intent;
import android.os.Bundle;  
import android.provider.Settings.System;
import android.telephony.PhoneStateListener;  
import android.telephony.TelephonyManager;  
import android.util.Log;  
import android.view.KeyEvent;
import android.widget.RadioGroup;  
import android.widget.ToggleButton;  
  
public class AutoAcceptActivity extends Activity {  
    /** Called when the activity is first created. */  
    RadioGroup rg;//来电操作单选框  
    ToggleButton tbtnRadioSwitch;//Radio开关  

    TelephonyManager telMgr;  
    CallStateListener stateListner;  
    int checkedId=0;  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
          
        telMgr= (TelephonyManager)getSystemService(TELEPHONY_SERVICE);  
        telMgr.listen(new CallStateListener(), CallStateListener.LISTEN_CALL_STATE);  
          
        PhoneUtils.printAllInform(TelephonyManager.class);  
          
        rg = (RadioGroup)findViewById(R.id.rGrpSelect);  
        rg.setOnCheckedChangeListener(new CheckEvent());  
   
    }  
      
    /** 
     * 来电时的操作 
     * @author GV 
     * 
     */  
    public class CheckEvent implements RadioGroup.OnCheckedChangeListener{  
  
        @Override  
        public void onCheckedChanged(RadioGroup group, int checkedId) {  
            AutoAcceptActivity.this.checkedId=checkedId;  
        }  
    }  
      
   
    /** 
     * 监视电话状态 
     * @author GV 
     * 
     */  
    public class CallStateListener extends PhoneStateListener {  
        @Override  
        public void onCallStateChanged(int state, String incomingNumber) {  
            if(state==TelephonyManager.CALL_STATE_IDLE)//挂断  
            {  
                Log.e("IDLE",incomingNumber);  
            }  
            else if(state==TelephonyManager.CALL_STATE_OFFHOOK)//接听  
            {  
                Log.e("OFFHOOK",incomingNumber);  
            }  
            else if(state==TelephonyManager.CALL_STATE_RINGING)//来电  
            {  
                if(AutoAcceptActivity.this.checkedId==R.id.rbtnAutoAccept)  
                {  
                    try {  
                        //需要<uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />   
//                        PhoneUtils.getITelephony(telMgr).silenceRinger();//静铃  
//                      
                    	
                  
                    	
                    	
                    	
                    	
                        
                        
                        
                    	
                    	/**
                    	 * Android 2.3(包括)以上，如果照样使用TelephoneyManager获取到的answerRingingCall方法的话，就会抛没有 android.permission.MODIFY_PHONE_STATE权限异常，其实你已经配了这个权限的了，但是不好意思，你的App不是系统 软件，没有系统签名，所以还是不能调用，除非，你root了你的手机，把你的app装到系统软件里面去，所以这里使用另外一种方法实现自动接听这个行为 了，详细如下:
                    	 */
                        
                        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,  KeyEvent.KEYCODE_HEADSETHOOK);
                        intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
                        sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");
                        intent = new  Intent("android.intent.action.MEDIA_BUTTON");
                        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                        intent.putExtra("android.intent.extra.KEY_EVENT",keyEvent);
                        sendOrderedBroadcast(intent,"android.permission.CALL_PRIVILEGED");
                        
                        
                        
                        /**
                         * 另外一种
                         */
                        
                        Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                        localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        localIntent1.putExtra("state", 1);
                        localIntent1.putExtra("microphone", 1);
                        localIntent1.putExtra("name", "Headset");
                        AutoAcceptActivity.this.sendOrderedBroadcast(localIntent1,"android.permission.CALL_PRIVILEGED");

                        Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                        KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
                        localIntent2.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent1);
                        AutoAcceptActivity.this.sendOrderedBroadcast(localIntent2,"android.permission.CALL_PRIVILEGED");

                        Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                        KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
                        localIntent3.putExtra("android.intent.extra.KEY_EVENT",localKeyEvent2);
                        AutoAcceptActivity.this.sendOrderedBroadcast(localIntent3,"android.permission.CALL_PRIVILEGED");

                        Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                        localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        localIntent4.putExtra("state", 0);
                        localIntent4.putExtra("microphone", 1);
                        localIntent4.putExtra("name", "Headset");
                        AutoAcceptActivity.this.sendOrderedBroadcast(localIntent4,"android.permission.CALL_PRIVILEGED");
                        
                        
                        
/*2.2以下*/
                    	
                    	PhoneUtils.getITelephony(telMgr).answerRingingCall();//自动接听  
                          
                    } catch (Exception e) {  
                        Log.e("error",e.getMessage());  
                    }     
                }  
                else if(AutoAcceptActivity.this.checkedId==R.id.rbtnAutoReject)  
                {  
                    try {  
                     	
                        PhoneUtils.getITelephony(telMgr).endCall();//挂断  
                     
                    } catch (Exception e) {  
                        Log.e("error",e.getMessage());    
                    }  
                }  
            }  
            super.onCallStateChanged(state, incomingNumber);  
        }  
    }  
}  