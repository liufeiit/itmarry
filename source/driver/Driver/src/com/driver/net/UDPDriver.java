package com.driver.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.PublicKey;

import org.xmlpull.v1.sax2.Driver;

import com.driver.data.GeneralPara;

import com.driver.main.DriverActivity;
import com.driver.main.SettingActivity;






import android.R.integer;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Administrator
 * @网络控制类
 * 
 */

public class UDPDriver extends UDPBase
{

	public UDPDriver() throws SocketException, UnknownHostException
	{
		super();
		// TODO Auto-generated constructor stub
	}
//	
//
//	public UDPDriver() throws SocketException, UnknownHostException
//	{
//		super();
//	}
//
//	
//	public void KeyClickRight()
//	{
//		
//	}
//	public void KeyClickLeft()
//	{
//		
//	}
//	/**
//	 * @发送方法
//	 * @param msg[消息]
//	 * @param ip[目标IP 192.168.1.116:12345 或者192.168.1.116  后者使用默认端口8888]
//	 * @param timeout[超时时间]
//	 * @throws SocketException
//	 * @throws IOException
//	 * @调用
//	 * @UDPMain test = new UDPMain();test.UDPMainsend("gc",computeripaddr, 2000);
//	 */
//	public void UDPMainsend(final String msg,final String ip,final int timeout) throws SocketException, IOException
//	{
//		send(msg,ip,timeout);
//	}
//
//	/**
//	 * @接收方法
//	 * @throws SocketException
//	 * @throws IOException
//	 */
//	public void UDPMainrec() throws SocketException, IOException
//	{
//		rec();
//	}
//	@Override
//	public void rec_datahandle(DatagramPacket packet)
//	{
//		byte[] buf=new byte[1024];
//		buf = packet.getData();
//		int nLen = packet.getLength();
//		String msg_rec = "";     //接收数据的内容
//		try
//		{
//		    msg_rec = new String(buf,0,nLen,"ISO-8859-1");
//		} catch (UnsupportedEncodingException e1)
//		{
//		}
//		String msg_iddr = new String(packet.getAddress().toString()); //接收包来源ip
//		String[] str = msg_rec.split(PublicData.PACKET_SPLITE_CHAR);  //获得包类型
//		 if(PublicData.PACTET_NET_ISCONNECT.equals(str[0]))    //连接成功
//		 {
//			//构建handler消息包
//				Message msg = new Message();  
//		        msg.what = GeneralPara.HADNLER_NET_ISCONNECT;    //消息类型 
//		        Bundle bundle = new Bundle();    
//		        bundle.putString("rec_msg",msg_rec);  //往Bundle中存放数据       接收的消息
//		        bundle.putString("iddr_msg",msg_iddr);  //往Bundle中存放数据     接收的来源ip地址
//		        msg.setData(bundle);//mes利用Bundle传递数据   
//		        handler.sendMessage(msg);//用activity中的handler发送消息   
//		 }
//		 else if(PublicData.PACTET_SET_SENSOR.equals(str[0]))    //设置sensor灵敏度
//		 {
//			//构建handler消息包
//			 if(str.length > 1)  //数据包类型    [消息类型][:][数据内容]
//			 {
//				 msg_rec = str[1];   //取出内容就可以了
//				 String[] strsensor = msg_rec.split(PublicData.PACKET_SPLITE_SENSOR_CHAR); 
//				 GeneralPara.SENSOR_VAULE_YUP = Integer.parseInt(strsensor[0]);                  //Y上仰
//				 GeneralPara.SENSOR_VAULE_YDOWN = Integer.parseInt(strsensor[1]);               //Y下倾
//				 GeneralPara.SENSOR_VAULE_XLEFT = Integer.parseInt(strsensor[2]);              //X左倾
//				 GeneralPara.SENSOR_VAULE_XRIGHT = Integer.parseInt(strsensor[3]);            //X右倾
//			 }
//			 
//		 }
//	}
//
//	//界面交互办法
//	private Handler handler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			super.handleMessage(msg);
//			switch (msg.what)
//			{
//			case GeneralPara.HADNLER_NET_ISCONNECT:   //连接成功
//				 Intent intent=new Intent(); 
//				 intent.setClass(SettingActivity.static_SettingActivity,DriverActivity.class); 
//		         intent.putExtra("testExtra", "test"); 
//		         SettingActivity.static_SettingActivity.startActivity(intent); 
//				break;
//			case GeneralPara.HADNLER_OTHER:   //其他类型消息包
//				String string =new String(msg.getData().getString("rec_msg"));   //取出内容
//				Toast.makeText(SettingActivity.static_SettingActivity, string, 2000).show();
//				break;
//				default:
//					Log.v("error", "位置:" + "UDPMain/handler" + "  - 错误:" +"接收到错误数据类型" + "  - 系统提示：null");
//					break;
//
//			}
//		}
//	};
//

}
