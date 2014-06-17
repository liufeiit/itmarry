package com.dyj.task;

import java.util.HashMap;

import org.phprpc.util.Cast;

import com.dyj.IndexActivity;
import com.dyj.activity.AdapterActivity;
import com.dyj.app.Global;
import com.dyj.bean.beanRwgg;
import com.dyj.db.DatabaseHelper;
import com.dyj.db.bean.Rw;
import com.dyj.rpc.RpcHandler;
import com.dyj.tabs.MyRwTab;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class TaskReDownRw extends AsyncTask<Object, String, Boolean> {

	ProgressDialog pdialog;
	private RpcHandler rpcHandler;
	private Context context;
	private beanRwgg bean;
	private DatabaseHelper dataHelper;

	public TaskReDownRw(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		// TODO Auto-generated method stub

		SharedPreferences userInfo = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		Global global = Global.getInstance(userInfo.getString("dm_user", ""));
		int user_dm = Integer.parseInt(global.read("dm_user").toString());
		int rw_dm = Integer.parseInt(params[0].toString());
		HashMap<?, ?> res = rpcHandler.reDownRw(user_dm, rw_dm);
		//Log.e("status", (String) Cast.cast(res.get("status"), String.class));
		if ("1".equals((String) Cast.cast(res.get("status"), String.class))) {
			try {
				dataHelper=getHelper();
				Dao<Rw, Integer> rwDao = dataHelper.getRwDao();
				// 修改状态为6已接收
				Rw rw = rwDao.queryForId(rw_dm);
				rw.setRwzt_dm(6);
				rwDao.update(rw);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// 更新我的任务

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pdialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pdialog = new ProgressDialog(context, 0);
		// pdialog.setMax(100);
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.show();
		this.rpcHandler = new RpcHandler(this.context);
	}

	private DatabaseHelper getHelper() {
		if (dataHelper == null) {
			dataHelper = OpenHelperManager.getHelper(this.context,
					DatabaseHelper.class);
		}
		return dataHelper;
	}
}
