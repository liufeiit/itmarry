package com.dangf.tools;

import android.os.Handler;
import android.os.Message;

public class HttpHandler extends Handler{

	@Override
	public void handleMessage(Message msg) {
		System.out.println(Thread.currentThread().getName()+"使用网络数据"+msg.obj.toString());
	}
	
	
}
