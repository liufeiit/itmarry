package com.dyj.tabs;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import com.dyj.R;
import com.dyj.adapter.LsRwAdapter;
import com.dyj.app.Global;
import com.dyj.bean.beanLsRw;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.widget.XListView;
import com.dyj.widget.XListView.IXListViewListener;

public class LsRwTab extends Activity implements IXListViewListener {

	private XListView mListView;
	private LsRwAdapter mAdapter;
	private ArrayList<beanLsRw> alist;
	private ArrayList<beanLsRw> items = new ArrayList<beanLsRw>();
	private Handler mHandler;
	private int pageIndex = 0;
	private int size = 20;

	private Global global;
	private Dialog pdialog;
	private HashMap<String, Object> keys = new HashMap<String, Object>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences userInfo = getApplicationContext()
				.getSharedPreferences("setting", Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		setContentView(R.layout.activity_rwgg);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		mAdapter = new LsRwAdapter(this, items);
		mHandler = new Handler();
		mListView.setXListViewListener(this);
		pdialog = LoadingDialog.createLoadingDialog(LsRwTab.this, "正在加载数据...");
		pdialog.show();
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				pageIndex = 0;
				geneItems(pageIndex, size);
				// mAdapter.notifyDataSetChanged();
				Message message = new Message();  
			    message.what = 1; 
			    handler.sendMessage(message); 
				/*mAdapter = new RwggAdapter(RwggTab.this, items);
				mListView.setAdapter(mAdapter);
				onLoad();*/
			}
		});
		if (Global.CheckNetwork(LsRwTab.this)) {
			t.start();
		}
		else{
			pdialog.dismiss();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Thread t=new Thread(new Runnable() {
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
				/*mAdapter = new RwggAdapter(RwggTab.this, items);
				mListView.setAdapter(mAdapter);
				onLoad();*/
			}
		});
		if (Global.CheckNetwork(LsRwTab.this)) {
			t.start();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				pageIndex = pageIndex + 1;
				geneItems(pageIndex, size);
				// mAdapter.notifyDataSetChanged();
				Message message = new Message();  
			    message.what = 2; 
			    handler.sendMessage(message); 
				/*mAdapter = new RwggAdapter(RwggTab.this, items);
				mListView.setAdapter(mAdapter);
				onLoad();*/
			}
		});
		if (Global.CheckNetwork(LsRwTab.this)) {
			t.start();
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	private void geneItems(int pageIndex, int size) {
		Log.d("is started!",pageIndex+","+size);
		RpcHandler rpcHandler = new RpcHandler(LsRwTab.this);
		beanLsRw bean = null;
		HashMap<String, ?> map=null;
		Log.d("dm_user:",global.read("dm_user"));
		try {
			keys.put("sgry_dm", global.read("dm_user"));
			map = rpcHandler.getLsRwList(keys, pageIndex, size);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == map) {
			return;
		}
		alist = (ArrayList<beanLsRw>) map.get("list");
		for (int i = 0; i < alist.size(); i++) {
			bean = new beanLsRw();
			bean = alist.get(i);
			items.add(bean);
		}
	}

	Runnable runGetList = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

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
		}

	};
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:

				mAdapter = new LsRwAdapter(LsRwTab.this, items);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:  
            this.finish();  
            break;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
