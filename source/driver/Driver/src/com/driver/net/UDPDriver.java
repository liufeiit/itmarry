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
 * @���������
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
//	 * @���ͷ���
//	 * @param msg[��Ϣ]
//	 * @param ip[Ŀ��IP 192.168.1.116:12345 ����192.168.1.116  ����ʹ��Ĭ�϶˿�8888]
//	 * @param timeout[��ʱʱ��]
//	 * @throws SocketException
//	 * @throws IOException
//	 * @����
//	 * @UDPMain test = new UDPMain();test.UDPMainsend("gc",computeripaddr, 2000);
//	 */
//	public void UDPMainsend(final String msg,final String ip,final int timeout) throws SocketException, IOException
//	{
//		send(msg,ip,timeout);
//	}
//
//	/**
//	 * @���շ���
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
//		String msg_rec = "";     //�������ݵ�����
//		try
//		{
//		    msg_rec = new String(buf,0,nLen,"ISO-8859-1");
//		} catch (UnsupportedEncodingException e1)
//		{
//		}
//		String msg_iddr = new String(packet.getAddress().toString()); //���հ���Դip
//		String[] str = msg_rec.split(PublicData.PACKET_SPLITE_CHAR);  //��ð�����
//		 if(PublicData.PACTET_NET_ISCONNECT.equals(str[0]))    //���ӳɹ�
//		 {
//			//����handler��Ϣ��
//				Message msg = new Message();  
//		        msg.what = GeneralPara.HADNLER_NET_ISCONNECT;    //��Ϣ���� 
//		        Bundle bundle = new Bundle();    
//		        bundle.putString("rec_msg",msg_rec);  //��Bundle�д������       ���յ���Ϣ
//		        bundle.putString("iddr_msg",msg_iddr);  //��Bundle�д������     ���յ���Դip��ַ
//		        msg.setData(bundle);//mes����Bundle��������   
//		        handler.sendMessage(msg);//��activity�е�handler������Ϣ   
//		 }
//		 else if(PublicData.PACTET_SET_SENSOR.equals(str[0]))    //����sensor������
//		 {
//			//����handler��Ϣ��
//			 if(str.length > 1)  //���ݰ�����    [��Ϣ����][:][��������]
//			 {
//				 msg_rec = str[1];   //ȡ�����ݾͿ�����
//				 String[] strsensor = msg_rec.split(PublicData.PACKET_SPLITE_SENSOR_CHAR); 
//				 GeneralPara.SENSOR_VAULE_YUP = Integer.parseInt(strsensor[0]);                  //Y����
//				 GeneralPara.SENSOR_VAULE_YDOWN = Integer.parseInt(strsensor[1]);               //Y����
//				 GeneralPara.SENSOR_VAULE_XLEFT = Integer.parseInt(strsensor[2]);              //X����
//				 GeneralPara.SENSOR_VAULE_XRIGHT = Integer.parseInt(strsensor[3]);            //X����
//			 }
//			 
//		 }
//	}
//
//	//���潻���취
//	private Handler handler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			super.handleMessage(msg);
//			switch (msg.what)
//			{
//			case GeneralPara.HADNLER_NET_ISCONNECT:   //���ӳɹ�
//				 Intent intent=new Intent(); 
//				 intent.setClass(SettingActivity.static_SettingActivity,DriverActivity.class); 
//		         intent.putExtra("testExtra", "test"); 
//		         SettingActivity.static_SettingActivity.startActivity(intent); 
//				break;
//			case GeneralPara.HADNLER_OTHER:   //����������Ϣ��
//				String string =new String(msg.getData().getString("rec_msg"));   //ȡ������
//				Toast.makeText(SettingActivity.static_SettingActivity, string, 2000).show();
//				break;
//				default:
//					Log.v("error", "λ��:" + "UDPMain/handler" + "  - ����:" +"���յ�������������" + "  - ϵͳ��ʾ��null");
//					break;
//
//			}
//		}
//	};
//

}
