package com.dyj.listener;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.dyj.IndexActivity;
import com.dyj.R;
import com.dyj.app.Global;
import com.dyj.bean.beanLsRw;
import com.dyj.bean.beanRwgg;
import com.dyj.db.DatabaseHelper;
import com.dyj.db.bean.Rw;
import com.dyj.dialog.CustomerDialog;
import com.dyj.tabs.MyRwTab;
import com.dyj.tabs.RwggTab;
import com.dyj.task.TaskReDownRw;
import com.dyj.task.TaskGetRwDisp;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LsRwListViewTitleOnClickListener implements OnClickListener {

	private int index = -1;
	private beanLsRw bean;
	private Context context;
	private static LsRwListViewTitleOnClickListener instance = null;

	private DatabaseHelper dataHelper = null;

	public LsRwListViewTitleOnClickListener(int index, beanLsRw bean,
			Context context) {
		this.setIndex(index);
		this.context = context;
		this.bean = bean;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("index is   ", this.index + "");
		Log.d("rw_dm is   ", bean.getRw_dm());
		final String rw_dm = bean.getRw_dm();
		/*
		 * View toolbar=null; toolbar=(View)
		 * this.view.findViewById(R.id.toolbar); Button btn_gps=(Button)
		 * this.view.findViewById(R.id.btn_gps);
		 * if(toolbar.getVisibility()==View.VISIBLE){
		 * toolbar.setVisibility(View.GONE); } else
		 * if(toolbar.getVisibility()==View.GONE){
		 * toolbar.setVisibility(View.VISIBLE); }
		 */
		final CustomerDialog dialog = new CustomerDialog(this.context);
		dialog.setMessage("提示：\n确认重新开始该条任务？");
		dialog.setText_btn_ok("开始");
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TaskReDownRw task = new TaskReDownRw(v.getContext());
				try {
					if(task.execute(rw_dm).get()){
						Toast  toast = Toast.makeText(context,
								"已经重新开始该任务！", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						IndexActivity.tabHost.setCurrentTabByTag("tab2");
					}else{
						Toast  toast = Toast.makeText(context,
								"不能重新开始该任务！", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				dialog.dismiss();
				
			}

		};
		dialog.setOkListener(listener);
		dialog.show();

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public beanLsRw getBean() {
		return bean;
	}

	public void setBean(beanLsRw bean) {
		this.bean = bean;
	}

}
