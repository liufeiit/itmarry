package com.dyj.listener;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.phprpc.util.Cast;

import com.dyj.IndexActivity;
import com.dyj.R;
import com.dyj.app.Global;
import com.dyj.bean.beanRwgg;
import com.dyj.db.DatabaseHelper;
import com.dyj.db.bean.Rw;
import com.dyj.dialog.CustomerDialog;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.tabs.MyRwTab;
import com.dyj.tabs.RwggTab;
import com.dyj.task.TaskGetRwDisp;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RwggListViewTitleOnClickListener implements OnClickListener {

	private int index = -1;
	private beanRwgg bean;
	private Context context;
	private Global global;
	private static RwggListViewTitleOnClickListener instance = null;

	private Dialog pdialog;
	private CustomerDialog dialog;

	private DatabaseHelper dataHelper = null;

	public RwggListViewTitleOnClickListener(int index, beanRwgg bean,
			Context context) {
		this.setIndex(index);
		this.context = context;
		this.bean = bean;
		SharedPreferences userInfo = context.getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		dataHelper = new DatabaseHelper(context);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
		dialog = new CustomerDialog(this.context);
		dialog.setMessage("提示：\n确认接收该条任务？");
		dialog.setText_btn_ok("下载");
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pdialog = LoadingDialog.createLoadingDialog(context,
						"正在加载数据...");
				pdialog.show();
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						RpcHandler rpcHandler = new RpcHandler(context);
						int user_dm = Integer.parseInt(global.read("dm_user")
								.toString());
						HashMap<?, ?> res=null;
						//判断是否返工任务，并用不同方式下载任务
						if (Integer.parseInt(bean.getHas_rwda()) > 0) {
							res = rpcHandler.reDownRw(user_dm,
									Integer.parseInt(rw_dm));
						} else {
							res = rpcHandler.downRw(user_dm,
									Integer.parseInt(rw_dm));
						}
						if ("0".equals(res.get("status").toString())) {
							Message message = new Message();
							message.what = 1;
							message.obj = res;
							handler.sendMessage(message);
						} else if ("1".equals(res.get("status").toString())) {
							try {
								beanRwgg br = rpcHandler.getRwDisp(bean
										.getRw_dm());
								Message message = new Message();
								message.what = 2;
								message.obj = br;
								handler.sendMessage(message);
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

						}

					}
				});
				if (Global.CheckNetwork(context)) {
					t.start();
				} else {
					pdialog.dismiss();
				}

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

	public beanRwgg getBean() {
		return bean;
	}

	public void setBean(beanRwgg bean) {
		this.bean = bean;
	}

	private DatabaseHelper getHelper() {
		if (dataHelper == null) {
			dataHelper = OpenHelperManager.getHelper(this.context,
					DatabaseHelper.class);
		}
		return dataHelper;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				HashMap<?, ?> res = (HashMap<?, ?>) msg.obj;
				if ("0".equals(res.get("status").toString())) {
					pdialog.dismiss();
					dialog.dismiss();
					global.save("down_error_msg",
							Cast.toString(res.get("info")));
					Toast toast = Toast.makeText(context,
							Cast.toString(res.get("info")), Toast.LENGTH_SHORT);
					toast.show();

				}

				break;
			case 2:
				dataHelper.down_rw((beanRwgg) msg.obj);
				/*
				 * beanRwgg br = (beanRwgg) msg.obj; try { Dao<Rw, Integer>
				 * rwDao = dataHelper.getRwDao();
				 * 
				 * //先删除旧数据
				 * 
				 * DeleteBuilder<Rw, Integer>
				 * deleteBulder=rwDao.deleteBuilder();
				 * deleteBulder.where().eq("rw_dm", br.getRw_dm());
				 * PreparedDelete<Rw> pd=deleteBulder.prepare();
				 * rwDao.delete(pd); Rw rwBean = new Rw();
				 * rwBean.setRw_dm(br.getRw_dm()); rwBean.setAzdz(br.getAzdz());
				 * rwBean.setCjb_jd(br.getCjb_jd());
				 * rwBean.setCjd_bh(br.getCjd_bh());
				 * rwBean.setCjd_wd(br.getCjd_wd());
				 * rwBean.setCjsj(br.getCjsj_i());
				 * rwBean.setDbzch(br.getDbzch()); rwBean.setDh(br.getDh());
				 * rwBean.setDh1(br.getDh1()); rwBean.setFbr_dm(br.getFbr_dm());
				 * rwBean.setFbr_mc(br.getFbr_mc());
				 * rwBean.setFbr_name(br.getFbr_name());
				 * rwBean.setFbr_tel(br.getFbr_tel());
				 * rwBean.setGddw(br.getGddw()); rwBean.setGzqk(br.getGzqk());
				 * rwBean.setHtrl(br.getHtrl()); rwBean.setLxr(br.getLxr());
				 * rwBean.setRw_lx(br.getRw_lx()); rwBean.setRwzt_dm(6);
				 * rwBean.setRwzt_mc(br.getRwzt_mc());
				 * //rwBean.setTjsj(br.getTjsj_i());
				 * rwBean.setYhbh(br.getYhbh()); rwBean.setYhmc(br.getYhmc());
				 * rwBean.setZbmklx(br.getZbmklx());
				 * rwBean.setZcbh(br.getZcbh()); rwBean.setZddz(br.getZddz());
				 * rwDao.create(rwBean); if (dataHelper != null) {
				 * OpenHelperManager.releaseHelper(); dataHelper = null; } }
				 * catch (SQLException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				pdialog.dismiss();
				dialog.dismiss();
				Toast toast = Toast.makeText(context, "下载成功",
						Toast.LENGTH_SHORT);
				toast.show();
				// IndexActivity.tabHost.setCurrentTabByTag("tab2");\
				Intent intent = new Intent(context, MyRwTab.class);
				context.startActivity(intent);
				break;
			}
		}

	};

}
