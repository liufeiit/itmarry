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
 * @UDPͨ�Ż���
 * 
 */
public class UDPBase
{
	//����
	public UDPBase() throws SocketException, UnknownHostException
	{
	}

	/**
	 * 
	 * @���ͷ���
	 * @param msg[...]��Ϣ
	 * @param ip[192.168.1.116:8888]Ŀ��ip
	 * @param Timeout[2000]��ʱʱ��
	 * 
	 */
	//���ͷ���
	protected void send(String msg, String ip, int Timeout)
			throws SocketException, IOException
	{
		Thread thread_send = new Thread(new send_thread(msg, ip, Timeout));
		thread_send.start();
	}
	//�����߳�
	public class send_thread implements Runnable
	{
		protected DatagramSocket socket_send;    // ����socket
		protected InetAddress ipaddAddress;      // ����Ŀ��IP��ַ
		protected DatagramPacket packet_send;    //���͵����ݰ�
		protected String msg;  // ������Ϣ
		protected String ip;   // ����
		protected int timeout; // ����

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
				if (strings.length > 1) // ip��ַ��ʽ    if[192.168.1.116:8888],else[192.168.1.116]
				{
					NetPara.PORT_CLIENT = Integer.parseInt(strings[1]);   //�޸�Ĭ�϶˿ڿͻ��˽��ն˿�
				}
				else {
					NetPara.IPADDR_CLIENT = ip + "" + NetPara.PORT_CLIENT;  //[192.168.1.116:8888]
				}
				if (timeout != -1) // �����趨��ʱʱ��
					NetPara.TIMEOUT_REC = timeout;

				socket_send = new DatagramSocket();
				byte[] data = msg.getBytes();
				packet_send = new DatagramPacket(data, data.length, ipaddAddress,NetPara.PORT_CLIENT);
				socket_send.send(packet_send);
				socket_send.close();

			} catch (IOException e)
			{
				Log.v(GeneralPara.LOG_NET, "λ��:" + "UDPBase/send_thread" + "  - ����:" +"������Ϣ����" + "  - ϵͳ��ʾ��" + e);
			}
		}
	}

	
	/**
	 * 
	 * @���շ���
	 * 
	 */
	protected void rec() throws SocketException, IOException
	{
		Thread thread = new Thread(new rec_thread());
		thread.start();
	}
	//�����߳�
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
				Log.v(GeneralPara.LOG_NET,"λ��:" + "UDPBase/rec_thread" + "  - ����:" +"������Ϣ����" + "  - ϵͳ��ʾ��" + e);
			}
		}
	}
	//�������ݺ�Ĵ����߳�
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
	//�������ݴ�����      �ṩ����
	public void rec_datahandle(DatagramPacket packet)
	{
	}
}
