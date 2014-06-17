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
    RadioGroup rg;//���������ѡ��  
    ToggleButton tbtnRadioSwitch;//Radio����  

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
     * ����ʱ�Ĳ��� 
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
     * ���ӵ绰״̬ 
     * @author GV 
     * 
     */  
    public class CallStateListener extends PhoneStateListener {  
        @Override  
        public void onCallStateChanged(int state, String incomingNumber) {  
            if(state==TelephonyManager.CALL_STATE_IDLE)//�Ҷ�  
            {  
                Log.e("IDLE",incomingNumber);  
            }  
            else if(state==TelephonyManager.CALL_STATE_OFFHOOK)//����  
            {  
                Log.e("OFFHOOK",incomingNumber);  
            }  
            else if(state==TelephonyManager.CALL_STATE_RINGING)//����  
            {  
                if(AutoAcceptActivity.this.checkedId==R.id.rbtnAutoAccept)  
                {  
                    try {  
                        //��Ҫ<uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />   
//                        PhoneUtils.getITelephony(telMgr).silenceRinger();//����  
//                      
                    	
                  
                    	
                    	
                    	
                    	
                        
                        
                        
                    	
                    	/**
                    	 * Android 2.3(����)���ϣ��������ʹ��TelephoneyManager��ȡ����answerRingingCall�����Ļ����ͻ���û�� android.permission.MODIFY_PHONE_STATEȨ���쳣����ʵ���Ѿ��������Ȩ�޵��ˣ����ǲ�����˼�����App����ϵͳ �����û��ϵͳǩ�������Ի��ǲ��ܵ��ã����ǣ���root������ֻ��������appװ��ϵͳ�������ȥ����������ʹ������һ�ַ���ʵ���Զ����������Ϊ �ˣ���ϸ����:
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
                         * ����һ��
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
                        
                        
                        
/*2.2����*/
                    	
                    	PhoneUtils.getITelephony(telMgr).answerRingingCall();//�Զ�����  
                          
                    } catch (Exception e) {  
                        Log.e("error",e.getMessage());  
                    }     
                }  
                else if(AutoAcceptActivity.this.checkedId==R.id.rbtnAutoReject)  
                {  
                    try {  
                     	
                        PhoneUtils.getITelephony(telMgr).endCall();//�Ҷ�  
                     
                    } catch (Exception e) {  
                        Log.e("error",e.getMessage());    
                    }  
                }  
            }  
            super.onCallStateChanged(state, incomingNumber);  
        }  
    }  
}  