package com.hongkong.stiqer;

import java.util.ArrayList;

import com.hongkong.stiqer.service.IntentReceiver;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import android.app.Activity;
import android.app.Application;

public class AppContext extends Application {
	 private static AppContext instance;
	 private String device_token;
	 ArrayList<Activity> list = new ArrayList<Activity>();  
	 
	    public static AppContext getInstance() {
	        return instance;
	    }

	    @Override
	    public void onCreate() {
	    	AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
	    	UAirship.takeOff(this, options);
	        PushManager.enablePush();
	        device_token = PushManager.shared().getAPID();
	        PushManager.shared().setIntentReceiver(IntentReceiver.class);
	        super.onCreate();
	        instance = this;
	    }
	    
	   public String getDevice_token(){
		   return device_token;
	   } 
	   
	   public void init(){  
	        //设置该CrashHandler为程序的默认处理器    
	        UnCeHandler catchExcep = new UnCeHandler(this);  
	        Thread.setDefaultUncaughtExceptionHandler(catchExcep);   
	    }  

	    public void removeActivity(Activity a){  
	        list.remove(a);  
	    }  
	      
	    public void addActivity(Activity a){  
	        list.add(a);  
	    }  
	       
	    public void finishActivity(){  
	        for (Activity activity : list) {    
	            if (null != activity) {    
	                activity.finish();    
	            }    
	        }  
	        //杀死该应用进程  
	       android.os.Process.killProcess(android.os.Process.myPid());    
	    } 
}
