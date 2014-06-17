package com.dyj.tabs;

import java.util.ArrayList;
import java.util.HashMap;

import com.dyj.DispMessageTab;
import com.dyj.R;
import com.dyj.R.id;
import com.dyj.R.layout;
import com.dyj.R.menu;
import com.dyj.adapter.RwggAdapter;
import com.dyj.adapter.messageAdapter;
import com.dyj.app.Global;
import com.dyj.bean.beanMessage;
import com.dyj.bean.beanRwgg;
import com.dyj.db.DatabaseHelper;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.untils.GetTimeUtil;
import com.dyj.widget.XListView;
import com.dyj.widget.XListView.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class ListMessageTab extends Activity implements IXListViewListener {

	private XListView mListView;
	private messageAdapter mAdapter;
	private int pageIndex = 0;
	private int size = 20;
	private Global global;
	private Dialog pdialog;
	private ArrayList<beanMessage> items = new ArrayList<beanMessage>();
	private ArrayList<beanMessage> alist;
	private String bm_dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_message_tab);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		SharedPreferences userInfo = getSharedPreferences("setting",
				Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		bm_dm = global.read("gs_dm");

		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		mListView.setAdapter(mAdapter);
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(ListMessageTab.this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				beanMessage br = items.get(arg2 - 1);
				Intent intent = new Intent(ListMessageTab.this,
						DispMessageTab.class);
				intent.putExtra("title", br.getTitle());
				intent.putExtra("content", br.getContent());
				intent.putExtra("dateline",
						GetTimeUtil.getTime(br.getDateline()));
				startActivity(intent);
			}

		});
		mAdapter = new messageAdapter(this, items);

		pdialog = LoadingDialog.createLoadingDialog(ListMessageTab.this,
				"正在加载数据...");
		pdialog.show();
		Thread t = new Thread(new Runnable() {

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

		});
		if (Global.CheckNetwork(ListMessageTab.this)) {
			t.start();
		} else {
			pdialog.dismiss();
		}

	}

	private void geneItems(int pageIndex, int size) {
		RpcHandler rpcHandler = new RpcHandler(ListMessageTab.this);
		alist = rpcHandler.getContentList(bm_dm, pageIndex, size);
		if (null != alist) {
			for (int i = 0; i < alist.size(); i++) {
				items.add(alist.get(i));
			}
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAdapter = new messageAdapter(ListMessageTab.this, items);
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

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_message_tab, menu);
		return true;
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
				Log.d("size", items.size() + "");
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
		if (Global.CheckNetwork(ListMessageTab.this)) {
			t.start();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				pageIndex = pageIndex + 1;

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
		if (Global.CheckNetwork(ListMessageTab.this)) {
			t.start();
		}
	}

}
