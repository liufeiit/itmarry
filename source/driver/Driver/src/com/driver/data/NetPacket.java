package com.driver.data;
/**
 * 
 * @author Administrator
 * @�������ݰ�Լ��
 *
 */
public class NetPacket
{
	/** ���� */
	// MsgStyle
	final public static String PACTET_MSGSTYLE_NET_CONNECT = "gconnect";    // ���������ӷ�������ʾ���ӳɹ�
	final public static String PACTET_MSGSTYLE_SET_SENSOR = "gssensor"; // sensor����������
	
	/** ���� */
	// MsgStyle
	final public static String PACTET_MSGSTYLE_CLOSE = "gclose"; // sensor����������
	
	// ActionStyle
	final public static String PACTET_ACTION_KEYDOWN = "kd";     //���¶���
	final public static String PACTET_ACTION_KEYUP = "ku";       //���¶���
	final public static String PACTET_ACTION_KEYCLICK = "kc";    //�������
	
	final public static String PACTET_ACTION1 = "action1";    //�������
	final public static String PACTET_ACTION2 = "action2";    //�������
	final public static String PACTET_ACTION3 = "action3";    //�������
	final public static String PACTET_ACTION4 = "action4";    //�������
	
	// SensorStyle 
	final public static String PACTET_SENSOR_ACTION_YUP = "yup";              //Y����
    final public static String PACTET_SENSOR_ACTION_YMIDDLE = "ymiddle";      //Yƽ��
    final public static String PACTET_SENSOR_ACTION_YDOWN = "ydown";          //Y����
    final public static String PACTET_SENSOR_ACTION_XLEFT = "xleft";          //X����
    final public static String PACTET_SENSOR_ACTION_XMIDDLE = "xmiddle";      //Xƽ��
    final public static String PACTET_SENSOR_ACTION_XRIGHT = "xright";        //X����
}
