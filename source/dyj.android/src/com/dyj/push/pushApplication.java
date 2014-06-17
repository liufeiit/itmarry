package com.dyj.push;

import com.baidu.frontia.FrontiaApplication;
import com.dyj.app.CrashHandler;
import com.dyj.app.Global;

public class pushApplication extends FrontiaApplication {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Global.getInstance().onCreate(this);
		/*CrashHandler crashHandler = CrashHandler.getInstance();  
		// 注册crashHandler  
        crashHandler.init(getApplicationContext());  
        // 发送以前没发送的报告(可选)  
        crashHandler.sendPreviousReportsToServer();  */
	}

}
