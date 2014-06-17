package com.dyj.tabs;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.phprpc.util.AssocArray;
import org.phprpc.util.Cast;

import com.dyj.R;
import com.dyj.adapter.MyRwAdapter;
import com.dyj.app.Global;
import com.dyj.bean.beanRwgg;
import com.dyj.db.DatabaseHelper;
import com.dyj.db.bean.Rw;
import com.dyj.dialog.CustomerDialog;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.widget.XListView;
import com.dyj.widget.XListView.IXListViewListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

@SuppressLint("NewApi")
public class MyRwTab extends Activity implements IXListViewListener {

	private int pageIndex = 0;
	private int size = 20;
	private DatabaseHelper dataHelper = null;
	private Dao<Rw, Integer> rwDao;

	private XListView mListView;
	public MyRwAdapter mAdapter;
	private ArrayList<Rw> alist;
	private ArrayList<beanRwgg> rwlist;
	private ArrayList<Rw> items = new ArrayList<Rw>();
	private Handler mHandler;
	private String yh_mc_key = "";
	private String gddw_key = "";
	private String dbzch_key = "";
	private String zcbh_key = "";
	private String azdz_key = "";

	private Dialog pdialog;
	private Global global;
	private HashMap<String, Object> keys = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_rw_tab);
		SharedPreferences userInfo = getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		keys.put("sgry_dm", global.read("dm_user"));

		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		if (dataHelper == null) {
			dataHelper = OpenHelperManager
					.getHelper(this, DatabaseHelper.class);
		}
		// 开始同步服务器的数据开始

		pdialog = LoadingDialog.createLoadingDialog(this, "正在与服务器同步...");
		pdialog.show();

		if (Global.CheckNetwork(this)) {
			new Thread(runSysnc).start();
		} else {
			final CustomerDialog dialog = new CustomerDialog(this);
			dialog.setMessage("无法连接到网络，这样您将无法得到服务器数据，可能将导致无法提交任务，是否继续？");
			dialog.setText_btn_ok("继续");
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					geneItems(pageIndex, size);
					mListView = (XListView) findViewById(R.id.xListView);
					mListView.setPullLoadEnable(true);
					mAdapter = new MyRwAdapter(MyRwTab.this, items);
					mListView.setAdapter(mAdapter);
					// mListView.setPullLoadEnable(false);
					// mListView.setPullRefreshEnable(false);
					mListView.setXListViewListener(MyRwTab.this);
					mHandler = new Handler();
					dialog.dismiss();
				}

			};
			dialog.setOkListener(listener);
			dialog.show();
			pdialog.dismiss();
		}
		// 开始同步服务器的数据结束

	}

	Runnable runSysnc = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			RpcHandler rpcHandler = new RpcHandler(MyRwTab.this);

			HashMap<String, ?> map;
			Message message = new Message();
			rwlist = new ArrayList<beanRwgg>();
			try {
				map = rpcHandler.getMyRwList(keys, 0, 1000);
				if (null == map) {
					return;
				} else {
					AssocArray List = (AssocArray) map.get("list");
					AssocArray data;
					beanRwgg br = null;
					if (null != List) {
						for (int i = 0; i < List.size(); i++) {
							data = (AssocArray) List.toArrayList().get(i);

							String rw_dm = Cast.toString(data.toHashMap().get(
									"rw_dm"));
							br = rpcHandler.getRwDisp(rw_dm);

							rwlist.add(br);
						}
					}

					message.what = 2;
					message.obj = rwlist;
					downhandler.sendMessage(message);

				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	};

	private Handler downhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 2:
				ArrayList<beanRwgg> list = (ArrayList<beanRwgg>) msg.obj;

				beanRwgg br = null;

				try {
					rwDao = dataHelper.getRwDao();
					DeleteBuilder<Rw, Integer> deleteBulder = rwDao
							.deleteBuilder();
					deleteBulder.where().gt("rw_dm", 0);
					// deleteBulder.where().gt(columnName, value)
					PreparedDelete<Rw> pd = deleteBulder.prepare();
					rwDao.delete(pd);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// 先删除旧数据

				for (int i = 0; i < list.size(); i++) {
					br = list.get(i);

					try {
						Log.d("msg", br.getRw_dm() + "写入数据库开始");

						// 先删除旧数据

						Rw rwBean = new Rw();
						rwBean.setRw_dm(br.getRw_dm());
						rwBean.setAzdz(br.getAzdz());
						rwBean.setCjb_jd(br.getCjb_jd());
						rwBean.setCjd_bh(br.getCjd_bh());
						rwBean.setCjd_wd(br.getCjd_wd());
						rwBean.setCjsj(br.getCjsj_i());
						rwBean.setDbzch(br.getDbzch());
						rwBean.setDh(br.getDh());
						rwBean.setDh1(br.getDh1());
						rwBean.setFbr_dm(br.getFbr_dm());
						rwBean.setFbr_mc(br.getFbr_mc());
						rwBean.setFbr_name(br.getFbr_name());
						rwBean.setFbr_tel(br.getFbr_tel());
						rwBean.setGddw(br.getGddw());
						rwBean.setGzqk(br.getGzqk());
						rwBean.setHtrl(br.getHtrl());
						rwBean.setLxr(br.getLxr());
						rwBean.setRw_lx(br.getRw_lx());
						rwBean.setRwzt_dm(6);
						rwBean.setRwzt_mc(br.getRwzt_mc());
						// rwBean.setTjsj(br.getTjsj_i());
						rwBean.setYhbh(br.getYhbh());
						rwBean.setYhmc(br.getYhmc());
						rwBean.setZbmklx(br.getZbmklx());
						rwBean.setZcbh(br.getZcbh());
						rwBean.setZddz(br.getZddz());
						rwBean.setWcqx(br.getWcqx());
						Log.d("rw_lx", rwBean.getRw_lx());

						Log.d("sql", rwDao.create(rwBean) + "");
						Log.d("size", rwDao.queryForAll().size() + "");
						Log.d("msg", br.getRw_dm() + "写入数据库结束");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						Log.e("error", e.getMessage());
						e.printStackTrace();
					}
				}
				geneItems(pageIndex, size);
				mListView = (XListView) findViewById(R.id.xListView);
				mListView.setPullLoadEnable(true);
				mAdapter = new MyRwAdapter(MyRwTab.this, items);
				mListView.setAdapter(mAdapter);
				// mListView.setPullLoadEnable(false);
				// mListView.setPullRefreshEnable(false);
				mListView.setXListViewListener(MyRwTab.this);
				mHandler = new Handler();
				pdialog.dismiss();

			}

		}
	};

	private void geneItems(int pageIndex, int size) {
		try {
			rwDao = dataHelper.getRwDao();
			QueryBuilder<Rw, Integer> qb = rwDao.queryBuilder();
			qb.offset(pageIndex * size);
			qb.limit(size);

			qb.where().eq("rwzt_dm", 6).and()
					.like("Gddw", "%" + gddw_key + "%")
					.and().like("yhmc", "%" + yh_mc_key + "%")
					.and().like("Dbzch", "%" + dbzch_key + "%")
					.and().like("zcbh", "%" + zcbh_key + "%")
					.and().like("azdz", "%" + azdz_key + "%");

			Log.d("sql:", qb.prepareStatementString());
			PreparedQuery<Rw> preparedQuery = qb.prepare();
			items.addAll(rwDao.query(preparedQuery));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				items.clear();
				pageIndex = 0;
				geneItems(pageIndex, size);
				// mAdapter.notifyDataSetChanged();
				mAdapter = new MyRwAdapter(MyRwTab.this, items);
				mListView.setAdapter(mAdapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageIndex = pageIndex + 1;
				geneItems(pageIndex, size);
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dataHelper != null) {
			OpenHelperManager.releaseHelper();
			dataHelper = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.my_rw_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_sysnc:
			pdialog = LoadingDialog.createLoadingDialog(this, "正在与服务器同步...");
			pdialog.show();
			if (Global.CheckNetwork(this)) {
				new Thread(runSysnc).start();
			} else {
				pdialog.dismiss();
			}
			break;
		case R.id.menu_search:
			Intent intent = new Intent(MyRwTab.this,
					GetSeachKeyForRwggTab.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 刷新
			startActivityForResult(intent, 100);
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		this.yh_mc_key= data.getStringExtra("yh_mc");
		this.gddw_key = data.getStringExtra("gddw");
		this.dbzch_key = data.getStringExtra("dbzch");
		this.zcbh_key = data.getStringExtra("zcbh");
		this.azdz_key = data.getStringExtra("azdz");
		items.clear();
		pageIndex = 0;
		geneItems(pageIndex, size);
		// mAdapter.notifyDataSetChanged();
		mAdapter = new MyRwAdapter(MyRwTab.this, items);
		mListView.setAdapter(mAdapter);
	}

}
