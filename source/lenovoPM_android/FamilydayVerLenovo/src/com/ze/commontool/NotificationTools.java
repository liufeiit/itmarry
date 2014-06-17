package com.ze.commontool;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;


public class NotificationTools {
	public static void sendNotification( Context mContext, int icon, String str_title, long time, String str_static_title, String str_static_say,boolean isCancel,int notifyId){
		try {
			//1.得到NotificationManager
	        NotificationManager nm=(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	        //2.实例化一个通知，指定图标、概要、时间
	        Notification nf=new Notification(icon,str_title,time);
	        //3.指定通知的标题、内容和intent
	        nf.setLatestEventInfo(mContext,str_static_title, str_static_say , null);
	        //指定声音 
	        /*nf.defaults |= Notification.DEFAULT_SOUND; 
	        nf.defaults |=  Notification.DEFAULT_VIBRATE;*/
	        nf.flags |= Notification.FLAG_AUTO_CANCEL;
	        //4.发送通知
	        nm.notify("tag",notifyId, nf);
	        if( isCancel )
	        {
	        	nm.cancel(notifyId);
	        }
		} catch (Exception e) {
			ToastUtil.show(mContext, "发送中...");
		}
    	
    }
	public static void clearNotificationById(Context mContext,int notifyId)
	{
		NotificationManager nm=(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(notifyId);
	}
}
