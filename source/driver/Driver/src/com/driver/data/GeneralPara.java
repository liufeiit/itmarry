package com.driver.data;

/**
 * 
 * @author Administrator
 * @��������Լ��
 *
 */
public class GeneralPara
{
	// Sensor   ������ֵ
    public static int SENSOR_VAULE_YUP = 2;                     //Y����
    public static int SENSOR_VAULE_YDOWN = -2;                 //Y����
    public static int SENSOR_VAULE_XLEFT = -2;                //X����
    public static int SENSOR_VAULE_XRIGHT = 2;               //X����
    public static boolean SENSOR_ISDEAL_SENSORDATA = false; // �Ƿ���sensorchangge����������

    // handler
  	final public static int HADNLER_NET_ISCONNECT = 0x001;    // handler��Ϣ����   - ���ӳɹ�
  	final public static int HADNLER_SET_SENSOR = 0x002;      // handler��Ϣ����  - ����������
  	final public static int HADNLER_OTHER = 0x003;          // handler��Ϣ����  - ����
  	
    // ����
 	final public static String LOG_NET = "LOG_NET"; // LOG - �������
 	
 	
 	//������
 	final public static long[] SHAKE_BTN_SHORT_ONE = {5,50};    // ��ť������ONE
 	final public static long[] SHAKE_BTN_SHORT_TWO = {5,60,10,10};   // ��ť������TWO
 	
 	final public static long[] SHAKE_SENSOR_SHORT_ONE = {2,20,5,5};   // ������������ONE
}
