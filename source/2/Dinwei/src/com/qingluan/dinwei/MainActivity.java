package com.qingluan.dinwei;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView time;
	Button speed;
	EditText EM;
	String[] email_Info;
	@SuppressWarnings("unused")
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 5000;
	private static int LOCATION_COUTNS = 0;
	private int Count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		time = (TextView)findViewById(R.id.display_time);
		speed= (Button)findViewById(R.id.display);
		EM = (EditText) findViewById(R.id.mail_add);
		Bundle bundle = this.getIntent().getExtras();
		email_Info = bundle.getStringArray("EMAIL");
		
		locationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);    
		option.setCoorType("bd09ll"); 
		option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
		option.setProdName("Dinwei"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setScanSpan(UPDATE_TIME);   //设置时间间隔
		locationClient.setLocOption(option);
		locationClient.setAK("LuWmWzeZsKv0BfLKVVFgGZkp");
		
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
                }
				StringBuffer sb = new StringBuffer(256);
				sb.append("Time: ");
				sb.append(location.getTime());
				sb.append("\nError code : ");
				sb.append(location.getLocType());
				sb.append("\n Latitude : ");
				sb.append(location.getLatitude());
				sb.append("\n Longitude : ");
				sb.append(location.getLongitude());
				sb.append("\nRadius : ");
				sb.append(location.getRadius());
				if (location.getLocType()  == BDLocation.TypeGpsLocation){
					sb.append("\nSpeed : ");
					sb.append(location.getSpeed());
				} else if  (location.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\nAdress : ");
					sb.append(location.getAddrStr());
				}
				time.setText(sb.toString());
				Log.d("lo", sb.toString());
				
				if(Count ==0 ){
					try{
						sendEmail(email_Info,sb.toString());
					}catch (Exception e){
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "No net found ", Toast.LENGTH_SHORT).show();
					}
				}
				Count = (Count + 1 ) %10; 
				EM.clearFocus();
			}

			@Override
			public void onReceivePoi(BDLocation location) {
				// TODO Auto-generated method stub
				
			}
		
		});
		
		speed.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "press", Toast.LENGTH_SHORT).show();
				if (locationClient == null) {
					return;
                }
				if (locationClient.isStarted()){
					speed.setText("Beginning");
					locationClient.stop();
				}else {
					speed.setText("Stop");
					locationClient.start();
					locationClient.requestLocation();
				}
			}
			
		});
			
	}

	
	public void sendEmail(String[] email_info,String mail ){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email_info[0]});
		i.putExtra(Intent.EXTRA_SUBJECT, "Info");
		i.putExtra(Intent.EXTRA_TEXT   , mail);
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
		
		//Mail mail = new Mail(email_info[0],email_info[1]);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override 
	protected void onDestroy(){
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}
	
	

	

}
