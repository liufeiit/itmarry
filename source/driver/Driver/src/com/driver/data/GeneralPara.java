package com.driver.data;

/**
 * 
 * @author Administrator
 * @基本参数约定
 *
 */
public class GeneralPara
{
	// Sensor   动作阀值
    public static int SENSOR_VAULE_YUP = 2;                     //Y上仰
    public static int SENSOR_VAULE_YDOWN = -2;                 //Y下倾
    public static int SENSOR_VAULE_XLEFT = -2;                //X左倾
    public static int SENSOR_VAULE_XRIGHT = 2;               //X右倾
    public static boolean SENSOR_ISDEAL_SENSORDATA = false; // 是否处理sensorchangge产生的数据

    // handler
  	final public static int HADNLER_NET_ISCONNECT = 0x001;    // handler消息类型   - 连接成功
  	final public static int HADNLER_SET_SENSOR = 0x002;      // handler消息类型  - 设置灵敏度
  	final public static int HADNLER_OTHER = 0x003;          // handler消息类型  - 其他
  	
    // 调试
 	final public static String LOG_NET = "LOG_NET"; // LOG - 网络测试
 	
 	
 	//震动类型
 	final public static long[] SHAKE_BTN_SHORT_ONE = {5,50};    // 按钮短震型ONE
 	final public static long[] SHAKE_BTN_SHORT_TWO = {5,60,10,10};   // 按钮短震型TWO
 	
 	final public static long[] SHAKE_SENSOR_SHORT_ONE = {2,20,5,5};   // 传感器短震型ONE
}
