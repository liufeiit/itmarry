package com.ze.familydayverlenovo;

import org.json.JSONObject;

import android.content.Context;

public class WidgetDataMrg {
	/**
	 * WIFI服务
	 */
	public final static int WIFI_SERVICE = 0;
	/**
	 * NO_WIFI服务
	 */
	public final static int NO_WIFI_SERVICE = 1;
	
	public static String M_AUTH = null;
	public static int POS_PAGE = 1;
	public static boolean FLAG_SERVICE = false;
	public static boolean FLAG_WEDGET = false;
	public static boolean FLAG_PUSH = false;
	public static JSONObject pushJSON = new JSONObject();
	public static int serviceType = 0;
	public static int error_state = 0;
	public static int posAllLocalPicList = 0;
	public static boolean flagLoadingWidget = false;
	
	/**
	 * 0表示wifi状态开启的服务   
	 */
	public static void resetData(){
		FLAG_WEDGET = false;
		POS_PAGE = 1;
		FLAG_SERVICE = false;
		FLAG_PUSH = false;
		pushJSON = new JSONObject();
		serviceType = 0;
		flag_receive = 0;
		flag_send = 0;
		posAllLocalPicList = 0;
		playAllLocal = false;
		wd_type = 1;
		flagLoadingWidget = false;
	}
	
	public static int flag_receive = 0;
	public static int flag_send = 0;
	
	public static int count = 0;
	public static boolean firstwidget = false;
	
	public static int wd_type = 1;
	/**
	 * 是否为本地全部播放
	 */
	public static boolean playAllLocal = false;
	public static class widgetPic{
		public static int id = 0;
		public static int uid = 0;
		
	}
	public static Context mContext;
	
}
