package com.dyj.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.phprpc.util.Cast;
import com.dyj.IndexActivity;
import com.dyj.R;
import com.dyj.adapter.RwggAdapter;
import com.dyj.app.Global;
import com.dyj.bean.beanRwgg;
import com.dyj.db.DatabaseHelper;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.widget.XListView;
import com.dyj.widget.XListView.IXListViewListener;
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
import android.widget.Toast;

public class RwggTab extends Activity implements IXListViewListener {
	private XListView mListView;
	private RwggAdapter mAdapter;
	private ArrayList<beanRwgg> alist;
	private ArrayList<beanRwgg> items = new ArrayList<beanRwgg>();
	private Handler mHandler;
	private int pageIndex = 0;
	private int size = 20;
	private Global global;
	private DatabaseHelper dataHelper;
	private Menu menu;
	private Dialog pdialog;
	private HashMap<String, Object> keys = new HashMap<String, Object>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rwgg);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		/*
		 * actionBar = (ActionBar) findViewById(R.id.actionbar);
		 * actionBar.setHomeLogo(R.drawable.ic_launcher);
		 * actionBar.addAction(new PldrAction(R.drawable.abc_ic_search));
		 * actionBar.addAction(new PldrAction(R.drawable.actionbar_pldr));
		 */
		SharedPreferences userInfo = getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		int tobm = Integer.parseInt(this.global.read("gs_dm"));
		keys.put("tobm", tobm + "");
		dataHelper = new DatabaseHelper(this);
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		mAdapter.setIsShowed(false);
		mListView.setAdapter(mAdapter);
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(RwggTab.this);
		mAdapter = new RwggAdapter(this, items);
		mHandler = new Handler();
		pdialog = LoadingDialog.createLoadingDialog(RwggTab.this, "正在加载数据...");
		pdialog.show();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				items.clear();
				pageIndex = 0;
				geneItems(pageIndex, size);
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
				// mAdapter.notifyDataSetChanged();
			}
		});
		if (Global.CheckNetwork(RwggTab.this)) {
			t.start();
		} else {
			pdialog.dismiss();
		}
		// geneItems(this.pageIndex, this.size);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_rwgg, menu);
		this.menu = menu;
		menu.findItem(R.id.menu_qx).setVisible(false);
		menu.findItem(R.id.menu_qxqx).setVisible(false);
		menu.findItem(R.id.menu_dr).setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_search:
			Intent intent = new Intent(RwggTab.this,
					GetSeachKeyForRwggTab.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 刷新
			startActivityForResult(intent, 100);
			break;
		case R.id.menu_pldr:
			mAdapter.setIsShowed(true);
			mAdapter.notifyDataSetChanged();
			menu.findItem(R.id.menu_pldr).setVisible(false);
			menu.findItem(R.id.menu_qx).setVisible(true);
			menu.findItem(R.id.menu_dr).setVisible(true);
			break;
		case R.id.menu_qx:
			for (int i = 0; i < items.size(); i++) {
				mAdapter.getIsSelected().put(i, true);
			}
			menu.findItem(R.id.menu_qx).setVisible(false);
			menu.findItem(R.id.menu_qxqx).setVisible(true);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.menu_qxqx:
			for (int i = 0; i < items.size(); i++) {
				mAdapter.getIsSelected().put(i, false);
			}
			mAdapter.notifyDataSetChanged();
			menu.findItem(R.id.menu_qx).setVisible(true);
			menu.findItem(R.id.menu_qxqx).setVisible(false);
			break;
		case R.id.menu_dr:
			final Iterator it = mAdapter.getListSelected().iterator();
			pdialog = LoadingDialog.createLoadingDialog(RwggTab.this,
					"正在下载数据...");
			pdialog.show();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					RpcHandler rpcHandler = new RpcHandler(RwggTab.this);
					int user_dm = Integer.parseInt(global.read("dm_user")
							.toString());
					while (it.hasNext()) {
						String RW_DM = it.next().toString();
						beanRwgg br1 = findItemByRwDm(RW_DM);
						HashMap<?, ?> res = null;
						if (Integer.parseInt(br1.getHas_rwda()) > 0) {
							res = rpcHandler.reDownRw(user_dm,
									Integer.parseInt(RW_DM));
						} else {
							res = rpcHandler.downRw(user_dm,
									Integer.parseInt(RW_DM));
						}
						if ("0".equals(res.get("status").toString())) {
							// 下载失败
							Message message = new Message();
							message.what = 1;
							message.obj = res;
							downhandler.sendMessage(message);
						} else if ("1".equals(res.get("status").toString())) {
							// 下载成功
							try {
								beanRwgg br = rpcHandler.getRwDisp(RW_DM);
								dataHelper.down_rw(br);
								Message message = new Message();
								message.what = 2;
								message.obj = br;
								downhandler.sendMessage(message);
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
					// 任务完成
					Message message = new Message();
					message.what = 3;
					downhandler.sendMessage(message);
				}
			});
			if (Global.CheckNetwork(RwggTab.this)) {
				t.start();
			} else {
				pdialog.dismiss();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 根据rw_dm查找item
	private beanRwgg findItemByRwDm(String RW_DM) {
		for (Iterator<beanRwgg> it2 = items.iterator(); it2.hasNext();) {
			beanRwgg br = it2.next();
			if (br.getRw_dm() == RW_DM) {
				return br;
			}
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// keys = new HashMap<String, Object>();
		keys.put("yh_mc", data.getStringExtra("yh_mc"));
		keys.put("gddw", data.getStringExtra("gddw"));
		keys.put("dbzch", data.getStringExtra("dbzch"));
		keys.put("zcbh", data.getStringExtra("zcbh"));
		keys.put("azdz", data.getStringExtra("azdz"));
		pdialog = LoadingDialog.createLoadingDialog(RwggTab.this, "正在加载数据...");
		pdialog.show();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				items.clear();
				pageIndex = 0;
				geneItems(pageIndex, size);
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
				// mAdapter.notifyDataSetChanged();
			}
		});
		if (Global.CheckNetwork(RwggTab.this)) {
			t.start();
		} else {
			pdialog.dismiss();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void geneItems(int pageIndex, int size) {
		RpcHandler rpcHandler = new RpcHandler(RwggTab.this);
		beanRwgg bean = null;
		try {
			HashMap<String, ?> map = rpcHandler.getNewRwList(keys, pageIndex,
					size);
			if (null == map) {
				return;
			}
			alist = (ArrayList<beanRwgg>) map.get("list");
			for (int i = 0; i < alist.size(); i++) {
				bean = new beanRwgg();
				bean = alist.get(i);
				items.add(bean);
			}
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

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				items.clear();
				pageIndex = 0;
				geneItems(pageIndex, size);
				// mAdapter.notifyDataSetChanged();
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
				/*
				 * mAdapter = new RwggAdapter(RwggTab.this, items);
				 * mListView.setAdapter(mAdapter); onLoad();
				 */
			}
		});
		if (Global.CheckNetwork(RwggTab.this)) {
			t.start();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		/*
		 * mHandler.postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { pageIndex = pageIndex + 1;
		 * geneItems(pageIndex, size); mAdapter.notifyDataSetChanged();
		 * onLoad(); } }, 2000);
		 */
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				pageIndex = pageIndex + 1;
				
				geneItems(pageIndex, size);
				for (int i = 0; i < items.size(); i++) {
					mAdapter.getIsSelected().put(i, true);
				}
				// mAdapter.notifyDataSetChanged();
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
				/*
				 * mAdapter = new RwggAdapter(RwggTab.this, items);
				 * mListView.setAdapter(mAdapter); onLoad();
				 */
			}
		});
		if (Global.CheckNetwork(RwggTab.this)) {
			t.start();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAdapter = new RwggAdapter(RwggTab.this, items);
				mListView.setAdapter(mAdapter);
				onLoad();
				pdialog.dismiss();
				break;
			case 2:
				mAdapter.notifyDataSetChanged();
				onLoad();
				break;
			case 3:
				mAdapter.notifyDataSetChanged();
				onLoad();
				break;
			}
		}
	};
	private Handler downhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				HashMap<?, ?> res = (HashMap<?, ?>) msg.obj;
				if ("0".equals(res.get("status").toString())) {
					global.save("down_error_msg",
							Cast.toString(res.get("info")));
					Toast toast = Toast.makeText(RwggTab.this,
							Cast.toString(res.get("info")), Toast.LENGTH_SHORT);
					toast.show();
				}
				break;
			case 2:
				Log.d("rwdm downed is:", ((beanRwgg) msg.obj).getRw_dm());
				Toast toast = Toast.makeText(RwggTab.this,
						((beanRwgg) msg.obj).getRw_dm() + "下载成功",
						Toast.LENGTH_SHORT);
				toast.show();
				break;
			case 3:
				mAdapter.setIsShowed(false);
				mAdapter.notifyDataSetChanged();
				menu.findItem(R.id.menu_pldr).setVisible(true);
				menu.findItem(R.id.menu_qx).setVisible(false);
				menu.findItem(R.id.menu_qxqx).setVisible(false);
				menu.findItem(R.id.menu_dr).setVisible(false);
				/*
				 * actionBar.removeAllActions(); actionBar.addAction(new
				 * PldrAction(R.drawable.abc_ic_search));
				 * actionBar.addAction(new
				 * PldrAction(R.drawable.actionbar_pldr));
				 */
				pdialog.dismiss();
				Toast toast1 = Toast.makeText(RwggTab.this, "下载成功",
						Toast.LENGTH_SHORT);
				toast1.show();
				Intent intent = new Intent(RwggTab.this, MyRwTab.class);
				startActivity(intent);
				break;
			}
		}
	};
}