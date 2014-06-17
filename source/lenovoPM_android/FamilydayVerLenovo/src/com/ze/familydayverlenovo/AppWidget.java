package com.ze.familydayverlenovo;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider{
	
	public final static String family_str = "com.qlf.familyBtn";
	public final static String local_str = "com.qlf.localBtn";
	
    /** 
     * 删除一个AppWidget时调用 
     * */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) 
    {
    	Log.v("aw", "onDeleted");
    	/*Intent intent = new Intent(context, RefreshService.class);
    	context.stopService(intent);*/
        super.onDeleted(context, appWidgetIds); 
    }
 
    /** 
     * 最后一个appWidget被删除时调用 
     * */
    @Override
    public void onDisabled(Context context) 
    { 
    	Log.v("aw", "onDisabled");
    	FLAG = false;
    	if (WidgetDataMrg.FLAG_SERVICE){
    		Log.v("aw", "关闭服务");
    		WidgetDataMrg.resetData();
	    	Intent intent = new Intent(context, RefreshService.class);
	    	context.stopService(intent);
    	}
    	
    	SharedPreferences preferences = context.getSharedPreferences("widgetInfo", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("widgetOn", false);
		editor.commit();
    	
        super.onDisabled(context); 
    }
 
    
    /** 
     * AppWidget的实例第一次被创建时调用 
     * */
    @Override
    public void onEnabled(final Context context) 
    { 
    	Log.v("aw", "onEnabled");
    	WidgetDataMrg.firstwidget = true;
    	WidgetDataMrg.count = 0;
    	
    	FLAG = true;
    	WidgetDataMrg.mContext = context;
    	
    	/*SharedPreferences preferences = context.getSharedPreferences("user.config", 
    			context.MODE_WORLD_READABLE);
    	WidgetDataMrg.M_AUTH = preferences.getString("m_auth", null);
    	WidgetDataMrg.FLAG_WEDGET = true;
    	WidgetDataMrg.FLAG_SERVICE = true;//启动服务
    	//启动服务前，先判断网络状态
    	setServiceType(context);
    	Intent startIntent = new Intent(context, RefreshService.class);
    	context.startService(startIntent);*/
        super.onEnabled(context); 
        
		
		
        
    }
 
    /** 
     * 接受广播事件 
     * */
    @Override
    public void onReceive(Context context, Intent intent) 
    {
    	super.onReceive(context, intent); 
    	Log.v("aw", "onReceive");
    	//弹出选择对话框让用户选择播放本地还是family的图片
    	WidgetDataMrg.count ++;
    	if (WidgetDataMrg.firstwidget && WidgetDataMrg.count == 2){
    		
			Intent stopIntent = new Intent(context, RefreshService.class);
	    	context.stopService(stopIntent);
	    	WidgetDataMrg.flagLoadingWidget = true;
			WidgetDataMrg.wd_type = 1; //选择family播放方式
			
			SharedPreferences preferences2 = context.getSharedPreferences("user.config", 
	    			Context.MODE_WORLD_READABLE);
	    	WidgetDataMrg.M_AUTH = preferences2.getString("m_auth", null);
	    	WidgetDataMrg.FLAG_WEDGET = true;
	    	WidgetDataMrg.FLAG_SERVICE = true;//启动服务
	    	//启动服务前，先判断网络状态
	    	HttpGetAllCnnt.setServiceType(context);
	    	Intent startIntent = new Intent(context, RefreshService.class);
	    	context.startService(startIntent);
    			
    		
    	}
    	
    	if (intent.getAction().equals("com.qlf.showDt")){
    		Log.v("RefreshService", "收到重启指令");
    		//重启一次服务
			//Log.v("RefreshService", "关闭服务");
    		Intent stopIntent = new Intent(context, RefreshService.class);
        	context.stopService(stopIntent);
    		
    		setServiceType(context);
			//Log.v("RefreshService", "启动服务");
    		if (WidgetDataMrg.POS_PAGE > 2){
        		WidgetDataMrg.POS_PAGE = 1;
        	}
			//WidgetDataMrg.FLAG_SERVICE = true;
        	Intent startIntent = new Intent(context, RefreshService.class);
        	context.startService(startIntent);
        	WidgetDataMrg.flag_receive ++;
    		
    	} 
    	
    }
 
    boolean chooseFlag = true;
    /** 
     * 到达指定的更新时间或者当用户向桌面添加AppWidget时被调用 
     * */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, 
            int[] appWidgetIds) 
    {
    	Log.v("aw", "onUpdate");
    	RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
    	
    	//点击播放界面弹出选择对话框
    	Intent dialogIntent = new Intent();
    	//WidgetDataMrg.wd_type = 1;
    	dialogIntent.setClass(context, WidgetDialogActivity.class);
    	PendingIntent dpIntent = PendingIntent.getActivity(context, 0, dialogIntent, 0);
    	rViews.setOnClickPendingIntent(R.id.layout, dpIntent);
    	if (appWidgetIds != null){
    		appWidgetManager.updateAppWidget(appWidgetIds, rViews);
    	} else {
    		ComponentName cName = new ComponentName(context, AppWidget.class);
    		appWidgetManager.updateAppWidget(cName, rViews);
    	}
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
                  
    }
    
    /**
     * 判断网络是否可用
     * @param context
     * @return 
     */
    public static boolean isNetworkConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null) {  
                return mNetworkInfo.isAvailable();  
            }  
        }  
        return false;  
    }
    /**
     * 设置服务类型，WIFI or NO_WIFI
     */
    public static void setServiceType(Context context){
    	if (isNetworkConnected(context))
    		WidgetDataMrg.serviceType = WidgetDataMrg.WIFI_SERVICE;
    	else
    		WidgetDataMrg.serviceType = WidgetDataMrg.NO_WIFI_SERVICE;
    }
    
    /**
     * 延时
     * @param time
     */
    private void myDelay(long time){
    	try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static boolean FLAG = false;
}
