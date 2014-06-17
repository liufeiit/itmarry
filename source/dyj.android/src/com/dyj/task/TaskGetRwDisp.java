package com.dyj.task;

import java.util.HashMap;

import org.phprpc.util.Cast;

import com.dyj.IndexActivity;
import com.dyj.activity.AdapterActivity;
import com.dyj.app.Global;
import com.dyj.bean.beanRwgg;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.tabs.MyRwTab;

import android.R.integer;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TaskGetRwDisp extends AsyncTask<Object, String, beanRwgg> {

	private Dialog pdialog; 
	private RpcHandler rpcHandler;
	private Context context;
	private beanRwgg bean = null;

	public TaskGetRwDisp(Context context) {
		this.context = context;
	}

	@Override
	protected beanRwgg doInBackground(Object... params) {
		// TODO Auto-generated method stub

		this.rpcHandler = new RpcHandler(this.context);
		SharedPreferences userInfo = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		Global global = Global.getInstance(userInfo.getString("dm_user", ""));
		int user_dm = Integer.parseInt(global.read("dm_user").toString());
		HashMap<?, ?> res = rpcHandler.downRw(user_dm,
				Integer.parseInt(params[0].toString()));

		if ("1".equals(res.get("status").toString())) {
			Log.d("status is:", res.get("status").toString());
			Log.d("info is:", res.get("info").toString());
			try {
				this.bean = this.rpcHandler.getRwDisp(params[0].toString());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("0".equals(res.get("status").toString())) {
			//global.save("down_error_msg", Cast.toString(res.get("info")));
			return null;

		}
		// 更新我的任务

		return bean;
	}

	@Override
	protected void onPostExecute(beanRwgg result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pdialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pdialog = LoadingDialog.createLoadingDialog(this.context, "正在获取数据...");
		//pdialog.setMax(100);  
       
		pdialog.show();
		
	}

}
