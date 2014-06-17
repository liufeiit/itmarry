package com.driver.data;

/**
 * 
 * @author Administrator
 * @网络设置参数数据约定
 *
 */

public class NetPara
{
	public static String IPADDR_LOCAL;    // 本地IP地址
	public static String IPADDR_CLIENT;   // 客户IP地址
	
	public static int PORT_LOCAL = 8888;  // 本地接收端口   默认8888
	public static int PORT_CLIENT = 9999; // 客户接收端口  默认9999
	
	public static int TIMEOUT_REC = 2000;                      // 接收超时
	public static int MAXBTYE_SEND = 1024;                     // 单次发送最大字节
	public static String PACKET_SPLITE_CHAR = ":";             // 数据包 - 消息类型 -分割字符   [gc:123]
	public static String PACKET_SPLITE_SENSOR_CHAR = ",";      // 数据包 - sensor灵敏度 - 分割字符 [gssensor:2,-2,-2,2]
}
