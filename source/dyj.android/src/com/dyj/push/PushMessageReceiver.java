package com.dyj.push;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.dyj.rpc.RpcHandler;
import com.dyj.tabs.RwggTab;

public class PushMessageReceiver extends FrontiaPushMessageReceiver {

	@Override
	public void onBind(Context arg0, int arg1,  String appid,   
	        final String userId, String channelId, String requestId) {
		// TODO Auto-generated method stub
		//开始注册userid
		final Context context=arg0;
		final SharedPreferences userInfo=context.getSharedPreferences("setting", Context.MODE_PRIVATE);
		//Log.d("dm_user",userInfo.getString("dm_user", ""));
		Thread t=new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				RpcHandler rpcHandler=new RpcHandler(context);
				rpcHandler.updatePushUserId(userInfo.getString("dm_user", ""), userId);
			}
			
		});
		t.start();

	}
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
            switch(msg.what){
            case 1:
            	break;
            }
		}
	};

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(Context arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotificationClicked(Context arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		String notifyString = "通知点击  title=" + arg1 + " description=" + arg2
				+ " customContent=" + arg1;
		Log.d("com.dyj", notifyString);
		
		// 自定义内容获取方式，mykey和 myvalue对应通知推送时自定义内容中设置的键和值
		if (arg3 != null & arg3 != "") {
			
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(arg3);
			    Log.d("josn length:",customJson.length()+"");
				String myvalue = null;
				if (!customJson.isNull("Activity")) {
					Log.d("customJson",arg3);
					myvalue = customJson.getString("Activity");
					Log.d("myvalue", myvalue);
					
					Intent intent = new Intent();
					
					intent.setClass(arg0,Class.forName("com.dyj."+myvalue));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					arg0.startActivity(intent);
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		//Log.d("onSetTags",arg4);

	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

}
