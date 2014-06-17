package com.driver.main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.security.auth.PrivateCredentialPermission;

import com.driver.data.GeneralPara;
import com.driver.data.NetPacket;
import com.driver.data.NetPara;

import com.driver.net.UDPMain;
import com.driver.net.UDPBase.send_thread;


import android.R.integer;
import android.app.Activity;

import android.content.Context;
import android.graphics.Color;
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
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DriverActivity extends Activity
{
	  
	//activity
	public static DriverActivity static_DriverActivity;
	//sensor
	private SensorManager manager;
	private Sensor sensor;
	private SensorEventListener listener;
	//component 
	private TextView textView;
	public EditText edittext;
	public Button btn_open ;
	public Button action1 ;
	public Button action2 ;
	public Button action3 ;
	public Button action4 ;
	//value
	private int num_Y = 0;    //最后一个保存sensor的X，Y数据
	private int num_X = 0;    //最后一个保存sensor的X，Y数据
	int count = 0;    //sensor 取值次数

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		static_DriverActivity = this;
		FindView();
		SetClick();
		Init();
	}

	public void FindView()
	{
           btn_open = (Button)findViewById(R.id.button5);    //中间的按钮  - 方向盘
           action1 = (Button)findViewById(R.id.btn_action1);    //中间的按钮  - 方向盘
           action2 = (Button)findViewById(R.id.btn_action2);    //中间的按钮  - 方向盘
           action3 = (Button)findViewById(R.id.btn_action3);    //中间的按钮  - 方向盘
           action4 = (Button)findViewById(R.id.btn_action4);    //中间的按钮  - 方向盘
           textView = (TextView) findViewById(R.id.tv_sensorchange);
   		
	}
	public void SetClick()
	{
		//中间的方向盘按钮 也是启动监听的按钮
	
		btn_open.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
                if(btn_open.getText() == "Go")
                {
                	Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
    				long[] pattern = {5,100,10,100}; // OFF/ON/OFF/ON...    
    				vibrator.vibrate(pattern, -1);
                	
                	GeneralPara.SENSOR_ISDEAL_SENSORDATA = true;
                	btn_open.setText("方向盘");
                	btn_open.setTextColor(Color.rgb(51,255,0));
                }
                else {
                	Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
    				long[] pattern = {5,1000,10,10}; // OFF/ON/OFF/ON...    
    				vibrator.vibrate(pattern, -1);
                	
                	btn_open.setText("Go");
                	btn_open.setTextColor(Color.rgb(255,0,0));
                	UDPMain test;
					try
					{
						test = new UDPMain();test.UDPMainsend(NetPacket.PACTET_MSGSTYLE_CLOSE,NetPara.IPADDR_CLIENT, 2000);   //发送一个关闭消息
					} 
					catch (SocketException e){}
					catch (UnknownHostException e){} 
					catch (IOException e){}
                	GeneralPara.SENSOR_ISDEAL_SENSORDATA = false;
				}
			}
		});
		action1.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(GeneralPara.SENSOR_ISDEAL_SENSORDATA)
				{
					if(event.getAction() == MotionEvent.ACTION_DOWN){  
						
						Thread shake_thread = new Thread(new shake_thread(1));
						shake_thread.start();
						
						UDPMain test;
						try
						{
							test = new UDPMain();
							String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION1;   //发送下倾动作  按下
							test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
						} 
						catch (SocketException e){}
						catch (UnknownHostException e){} 
						catch (IOException e){}
	                }   
	                if(event.getAction() == MotionEvent.ACTION_UP){  
	                	UDPMain test;
	    				try
	    				{
	    					test = new UDPMain();
	    					String sendmsg = NetPacket.PACTET_ACTION_KEYUP + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION1;   //发送下倾动作  按下
	    					test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
	    				} 
	    				catch (SocketException e){}
	    				catch (UnknownHostException e){} 
	    				catch (IOException e){}
	                }  
				}
				
				return false;
			}
		});
		action2.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(GeneralPara.SENSOR_ISDEAL_SENSORDATA)
				{
					if(event.getAction() == MotionEvent.ACTION_DOWN){  
						
						Thread shake_thread = new Thread(new shake_thread(1));
						shake_thread.start();
	    				
						UDPMain test;
						try
						{
							test = new UDPMain();
							String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION2;   //发送下倾动作  按下
							test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
						} 
						catch (SocketException e){}
						catch (UnknownHostException e){} 
						catch (IOException e){}
	                }   
	                if(event.getAction() == MotionEvent.ACTION_UP){  
	                	UDPMain test;
	    				try
	    				{
	    					test = new UDPMain();
	    					String sendmsg = NetPacket.PACTET_ACTION_KEYUP + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION2;   //发送下倾动作  按下
	    					test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
	    				} 
	    				catch (SocketException e){}
	    				catch (UnknownHostException e){} 
	    				catch (IOException e){}
	                }  
				}
				
				return false;
			}
		});
		action3.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(GeneralPara.SENSOR_ISDEAL_SENSORDATA)
				{
					if(event.getAction() == MotionEvent.ACTION_DOWN){

						Thread shake_thread = new Thread(new shake_thread(2));
						shake_thread.start();
	    				
						UDPMain test;
						try
						{
							test = new UDPMain();
							String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION3;   //发送下倾动作  按下
							test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
						} 
						catch (SocketException e){}
						catch (UnknownHostException e){} 
						catch (IOException e){}
	                }   
	                if(event.getAction() == MotionEvent.ACTION_UP){  
	                	UDPMain test;
	    				try
	    				{
	    					test = new UDPMain();
	    					String sendmsg = NetPacket.PACTET_ACTION_KEYUP + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION3;   //发送下倾动作  按下
	    					test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
	    				} 
	    				catch (SocketException e){}
	    				catch (UnknownHostException e){} 
	    				catch (IOException e){}
	                }  
				}
				
				return false;
			}
		});
		action4.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(GeneralPara.SENSOR_ISDEAL_SENSORDATA)
				{
				if(event.getAction() == MotionEvent.ACTION_DOWN){  

//					Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
//    				long[] pattern = {5,60,10,10}; // OFF/ON/OFF/ON...    科技
//    				vibrator.vibrate(pattern, -1);
					
					Thread shake_thread = new Thread(new shake_thread(1));
					shake_thread.start();
					
					UDPMain test;
					try
					{
						test = new UDPMain();
						String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION4;   //发送下倾动作  按下
						test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
					} 
					catch (SocketException e){}
					catch (UnknownHostException e){} 
					catch (IOException e){}
                }   
                if(event.getAction() == MotionEvent.ACTION_UP){  
                	UDPMain test;
    				try
    				{
    					test = new UDPMain();
    					String sendmsg = NetPacket.PACTET_ACTION_KEYUP + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_ACTION4;   //发送下倾动作  按下
    					test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
    				} 
    				catch (SocketException e){}
    				catch (UnknownHostException e){} 
    				catch (IOException e){}
                }  }
				return false;
				
			}
		});
	}
	public void Init()
	{
		    btn_open.setText("Go");
		    textView.setText("X=0,Y=0,Z=0");
		    manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
			sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			listener = new SensorEventListener()
			{
				boolean issendleft =  true;    //判断动作量
				boolean issendright =  true;
				boolean issendtop =  true;			
				boolean issendbottom =  true;
				@Override
				public void onSensorChanged(SensorEvent event)
				{
					count++;
					int X = (int) event.values[SensorManager.DATA_X];    //由于手机横向   所以X轴表示上下方向
					int Y = (int) event.values[SensorManager.DATA_Y];    // Y轴表示左右方向
					int Z = (int) event.values[SensorManager.DATA_Z];
					String str = "X=" + X + ",Y=" + Y + ",Z=" + Z;
					textView.setText(str + " - " + count);
					//过滤器
					if(((num_X != X ) || (num_Y != Y)) && GeneralPara.SENSOR_ISDEAL_SENSORDATA)
					{
						// <判断x方向   上下方向>
						if (X  <  GeneralPara.SENSOR_VAULE_YDOWN)     //下倾
						{

//							vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
//		    				long[] pattern = {5,10,10,10}; // OFF/ON/OFF/ON...    科技
//		    				vibrator.vibrate(pattern, -1);
		    				
							UDPMain test;
							try
							{
								if(issendbottom)
								{
									test = new UDPMain();   //   [kd][:][ydown]
									String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_SENSOR_ACTION_YDOWN;   //发送下倾动作  按下
									test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
									issendbottom = false;
								}
							} 
							catch (SocketException e){}
							catch (UnknownHostException e){} 
							catch (IOException e){}
						}

						else if (X > GeneralPara.SENSOR_VAULE_YUP) //上仰
						{

//							vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
//		    				long[] pattern = {5,10,10,10}; // OFF/ON/OFF/ON...    科技
//		    				vibrator.vibrate(pattern, -1);
		    				
							UDPMain test;
							try
							{
								if(issendtop)
		 						{
									issendtop = false;
									test = new UDPMain();    
									String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR+ NetPacket.PACTET_SENSOR_ACTION_YUP;   //发送上仰动作   按下
									test.UDPMainsend(sendmsg,NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
								}
							} 
							catch (SocketException e){}
							catch (UnknownHostException e){} 
							catch (IOException e){}
						}
						else     //平视     --   回复判断
						{

//							Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
//		    				long[] pattern = {5,10,10,10}; // OFF/ON/OFF/ON...    科技
//		    				vibrator.vibrate(pattern, -1);
		    				
							UDPMain test;
							try
							{
								if((!issendtop) || (!issendbottom))
								{
									issendbottom = true;
									issendtop = true;
									test = new UDPMain();
									String sendmsg = NetPacket.PACTET_ACTION_KEYUP + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_SENSOR_ACTION_YMIDDLE;   //发送上下轴恢复动作   弹起
									test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
								}
									
							} 
							catch (SocketException e){}
							catch (UnknownHostException e){} 
							catch (IOException e){}
						}
						 // </判断x方向   上下方向>
						 
						// <判断Y方向   左右方向>
						if (Y < GeneralPara.SENSOR_VAULE_XLEFT) // 左转
						{
//
//							vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
//		    				long[] pattern = {5,10,10,10}; // OFF/ON/OFF/ON...    科技
//		    				vibrator.vibrate(pattern, -1);
		    				
							UDPMain test;
							try
							{
								if(issendleft)
								{
									test = new UDPMain();
									String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR+ NetPacket.PACTET_SENSOR_ACTION_XLEFT;   //发送左右轴恢复动作   弹起
									test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
									issendleft = false;
								}
							}
							catch (SocketException e){}
							catch (UnknownHostException e){} 
							catch (IOException e){}
						}

						else if (Y > GeneralPara.SENSOR_VAULE_XRIGHT) // 右转
						{

//							vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
//		    				long[] pattern = {5,10,10,10}; // OFF/ON/OFF/ON...    科技
//		    				vibrator.vibrate(pattern, -1);
		    				
//							Thread shake_thread = new Thread(new shake_thread(3));
//							shake_thread.start();
							
							UDPMain test;
							try
							{
								if(issendright)
								{
									issendright = false;
									test = new UDPMain();
									String sendmsg = NetPacket.PACTET_ACTION_KEYDOWN + NetPara.PACKET_SPLITE_CHAR+ NetPacket.PACTET_SENSOR_ACTION_XRIGHT;   //发送左右轴恢复动作   弹起
									test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
								}
							
							}
							catch (SocketException e){}
							catch (UnknownHostException e){} 
							catch (IOException e){}
						}
						
						else
						{
							UDPMain test;
							try
							{
								if((!issendright) || (!issendleft))
								{
									issendright = true;
									issendleft = true;
									test = new UDPMain();
									String sendmsg = NetPacket.PACTET_ACTION_KEYUP + NetPara.PACKET_SPLITE_CHAR + NetPacket.PACTET_SENSOR_ACTION_XMIDDLE;   //发送左右轴恢复动作   弹起
									test.UDPMainsend(sendmsg, NetPara.IPADDR_CLIENT, NetPara.TIMEOUT_REC);
								}
							}
							catch (SocketException e){}
							catch (UnknownHostException e){} 
							catch (IOException e){}
						}		
					}	
					// </判断Y方向   左右方向>
//					makeSensorDelay();
				}
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy)
				{
				}
			};
			manager.registerListener(listener, sensor,SensorManager.SENSOR_DELAY_GAME);
	}
	@Override
	protected void onResume()
	{
		super.onResume();

	}

	@Override
	protected void onPause()
	{
		manager.unregisterListener(listener);
		super.onPause();

	}

	
	
	
	
	public class shake_thread implements Runnable
	{
		protected int type;   // 震动类型
		public shake_thread(int type)
		{
			this.type = type;
		}
		@Override
		public void run()
		{
		
			switch (type)
			{
			case 1:    //屏幕按钮短震型1
				Vibrator vibrator1 = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
				vibrator1.vibrate(GeneralPara.SHAKE_BTN_SHORT_ONE, -1);	
				break;
            case 2:   //屏幕按钮短震型2
            	Vibrator vibrator2 = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
            	vibrator2.vibrate(GeneralPara.SHAKE_BTN_SHORT_TWO, -1);	
				break;
            case 3:   //传感器按钮短震型2
            	Vibrator vibrator3 = (Vibrator) getSystemService(VIBRATOR_SERVICE);    
            	vibrator3.vibrate(GeneralPara.SHAKE_SENSOR_SHORT_ONE, -1);	
				break;
				
			default:
				break;
			}
					
		}
	}
	
}