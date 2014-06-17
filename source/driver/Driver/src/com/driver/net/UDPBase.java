package com.driver.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import com.driver.data.GeneralPara;
import com.driver.data.NetPara;
import android.util.Log;

/**
 * 
 * @author Administrator
 * @UDP通信基类
 * 
 */
public class UDPBase
{
	//构造
	public UDPBase() throws SocketException, UnknownHostException
	{
	}

	/**
	 * 
	 * @发送方法
	 * @param msg[...]消息
	 * @param ip[192.168.1.116:8888]目标ip
	 * @param Timeout[2000]超时时间
	 * 
	 */
	//发送方法
	protected void send(String msg, String ip, int Timeout)
			throws SocketException, IOException
	{
		Thread thread_send = new Thread(new send_thread(msg, ip, Timeout));
		thread_send.start();
	}
	//发送线程
	public class send_thread implements Runnable
	{
		protected DatagramSocket socket_send;    // 发送socket
		protected InetAddress ipaddAddress;      // 发送目标IP地址
		protected DatagramPacket packet_send;    //发送的数据包
		protected String msg;  // 发送消息
		protected String ip;   // 发送
		protected int timeout; // 发送

		public send_thread(String msg, String ip, int timeout)
		{
			this.msg = msg;
			this.ip = ip;
			this.timeout = timeout;
		}

		@Override
		public void run()
		{
			try
			{
				String[] strings = ip.split(":");
				ipaddAddress = InetAddress.getByName(strings[0]);
				NetPara.IPADDR_CLIENT = ip;
				if (strings.length > 1) // ip地址形式    if[192.168.1.116:8888],else[192.168.1.116]
				{
					NetPara.PORT_CLIENT = Integer.parseInt(strings[1]);   //修改默认端口客户端接收端口
				}
				else {
					NetPara.IPADDR_CLIENT = ip + "" + NetPara.PORT_CLIENT;  //[192.168.1.116:8888]
				}
				if (timeout != -1) // 重新设定超时时间
					NetPara.TIMEOUT_REC = timeout;

				socket_send = new DatagramSocket();
				byte[] data = msg.getBytes();
				packet_send = new DatagramPacket(data, data.length, ipaddAddress,NetPara.PORT_CLIENT);
				socket_send.send(packet_send);
				socket_send.close();

			} catch (IOException e)
			{
				Log.v(GeneralPara.LOG_NET, "位置:" + "UDPBase/send_thread" + "  - 错误:" +"发送消息出错" + "  - 系统提示：" + e);
			}
		}
	}

	
	/**
	 * 
	 * @接收方法
	 * 
	 */
	protected void rec() throws SocketException, IOException
	{
		Thread thread = new Thread(new rec_thread());
		thread.start();
	}
	//接收线程
	public class rec_thread implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					byte[] data = new byte[NetPara.MAXBTYE_SEND];
					DatagramSocket socketrec = new DatagramSocket(NetPara.PORT_LOCAL);
					DatagramPacket packet_rec = new DatagramPacket(data,data.length);
					socketrec.receive(packet_rec);
					socketrec.close();
					Thread thread = new Thread(new rec_datahandle_thread(packet_rec));
					thread.start();
				}

			} catch (IOException e)
			{
				Log.v(GeneralPara.LOG_NET,"位置:" + "UDPBase/rec_thread" + "  - 错误:" +"接收消息出错" + "  - 系统提示：" + e);
			}
		}
	}
	//接收数据后的处理线程
	public class rec_datahandle_thread implements Runnable
	{
		protected DatagramPacket packet;
		public rec_datahandle_thread(DatagramPacket packet)
		{
			this.packet = packet;
		}
		@Override
		public void run()
		{
			rec_datahandle(packet);
		}
	}
	//接收数据处理方法      提供重载
	public void rec_datahandle(DatagramPacket packet)
	{
	}
}
