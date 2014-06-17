package com.driver.data;
/**
 * 
 * @author Administrator
 * @网络数据包约定
 *
 */
public class NetPacket
{
	/** 接收 */
	// MsgStyle
	final public static String PACTET_MSGSTYLE_NET_CONNECT = "gconnect";    // 网络获得连接反馈，表示连接成功
	final public static String PACTET_MSGSTYLE_SET_SENSOR = "gssensor"; // sensor灵敏度设置
	
	/** 发送 */
	// MsgStyle
	final public static String PACTET_MSGSTYLE_CLOSE = "gclose"; // sensor灵敏度设置
	
	// ActionStyle
	final public static String PACTET_ACTION_KEYDOWN = "kd";     //按下动作
	final public static String PACTET_ACTION_KEYUP = "ku";       //按下动作
	final public static String PACTET_ACTION_KEYCLICK = "kc";    //点击动作
	
	final public static String PACTET_ACTION1 = "action1";    //点击动作
	final public static String PACTET_ACTION2 = "action2";    //点击动作
	final public static String PACTET_ACTION3 = "action3";    //点击动作
	final public static String PACTET_ACTION4 = "action4";    //点击动作
	
	// SensorStyle 
	final public static String PACTET_SENSOR_ACTION_YUP = "yup";              //Y上仰
    final public static String PACTET_SENSOR_ACTION_YMIDDLE = "ymiddle";      //Y平视
    final public static String PACTET_SENSOR_ACTION_YDOWN = "ydown";          //Y下倾
    final public static String PACTET_SENSOR_ACTION_XLEFT = "xleft";          //X左倾
    final public static String PACTET_SENSOR_ACTION_XMIDDLE = "xmiddle";      //X平视
    final public static String PACTET_SENSOR_ACTION_XRIGHT = "xright";        //X右倾
}
