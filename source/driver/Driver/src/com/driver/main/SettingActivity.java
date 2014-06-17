package com.driver.main;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.security.auth.PrivateCredentialPermission;

import com.driver.data.NetPacket;
import com.driver.data.NetPara;
import com.driver.net.UDPMain;



import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity
{

	Vibrator vibrator;   
	//component
	public EditText et_ip;
	public Button btn_setip;
	public String computeripaddr;  //电脑IP
	//activity
	public static SettingActivity static_SettingActivity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		static_SettingActivity = this;
		FindView();
		SetClick();
		init();
		
	}

	public void FindView()
	{
		et_ip = (EditText)findViewById(R.id.et_ip);
		btn_setip = (Button)findViewById(R.id.btn_setip);
	}

	public void init()
	{
		et_ip.setText("192.168.1.116:8888");
	}


	public void SetClick()
	{
		//设置IP地址
		btn_setip.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
				long[] pattern = {5,100,10,100}; // OFF/ON/OFF/ON...    
				vibrator.vibrate(pattern, -1);
				
				Toast.makeText(SettingActivity.this, "正在连接", 50).show();
				
				computeripaddr = et_ip.getText().toString();
				NetPara.IPADDR_CLIENT = computeripaddr;
				UDPMain test;
				try
				{
					test = new UDPMain();
					test.UDPMainrec();
					test.UDPMainsend(NetPacket.PACTET_MSGSTYLE_NET_CONNECT,computeripaddr, NetPara.TIMEOUT_REC);
				} catch (SocketException e)
				{
					e.printStackTrace();
				} catch (UnknownHostException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();

	}

	@Override
	protected void onPause()
	{

		super.onPause();

	}
//	@Override    
//	protected void onStop() {    
//	if(null!=vibrator){    
//	vibrator.cancel();    
//	}    
//	super.onStop();    
//	}   
//

}