package com.driver.data;

/**
 * 
 * @author Administrator
 * @�������ò�������Լ��
 *
 */

public class NetPara
{
	public static String IPADDR_LOCAL;    // ����IP��ַ
	public static String IPADDR_CLIENT;   // �ͻ�IP��ַ
	
	public static int PORT_LOCAL = 8888;  // ���ؽ��ն˿�   Ĭ��8888
	public static int PORT_CLIENT = 9999; // �ͻ����ն˿�  Ĭ��9999
	
	public static int TIMEOUT_REC = 2000;                      // ���ճ�ʱ
	public static int MAXBTYE_SEND = 1024;                     // ���η�������ֽ�
	public static String PACKET_SPLITE_CHAR = ":";             // ���ݰ� - ��Ϣ���� -�ָ��ַ�   [gc:123]
	public static String PACKET_SPLITE_SENSOR_CHAR = ",";      // ���ݰ� - sensor������ - �ָ��ַ� [gssensor:2,-2,-2,2]
}
