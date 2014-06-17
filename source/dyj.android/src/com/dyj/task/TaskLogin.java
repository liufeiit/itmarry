package com.dyj.task;

import java.util.HashMap;

import com.dyj.IndexActivity;
import com.dyj.MainActivity;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class TaskLogin extends AsyncTask<Object, String, HashMap>{

	private RpcHandler rpcHandler;
	public HashMap res;
	private Context context;
	private Dialog pdialog; 
	
	public TaskLogin(Context context){
		this.context=context;
		
		
		
	}
	@Override
	protected HashMap doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Log.d("user_mc",params[0].toString());
		Log.d("password",params[1].toString());
		this.rpcHandler=new RpcHandler(this.context);
		res = rpcHandler.checkLogin(params[0].toString(), params[1].toString());
		return res;
	}
	
	@Override
	protected void onPostExecute(HashMap result) {
		// TODO Auto-generated method stub
		//result=this.res;
		pdialog.dismiss();  
		
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pdialog = LoadingDialog.createLoadingDialog(this.context, "ÕýÔÚµÇÂ¼...");
		//pdialog.setMax(100);  
       
		pdialog.show();
		
		
	}

}
