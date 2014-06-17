package com.dyj;

import java.util.HashMap;
import java.util.List;

import org.phprpc.util.Cast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.util.verify.BNKeyVerifyListener;
import com.dyj.app.Global;
import com.dyj.push.Utils;
import com.dyj.rpc.RpcHandler;
import com.dyj.tabs.ListMessageTab;
import com.dyj.tabs.LsRwTab;
import com.dyj.tabs.MyRwTab;
import com.dyj.tabs.RwggTab;
import com.dyj.update.DialogHelper;
import com.dyj.update.UpdateManager;
import com.dyj.widget.BadgeView;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

@SuppressLint("NewApi")
public class IndexActivity extends Activity {

	public static TabHost tabHost;
	private Global global;
	private SharedPreferences userInfo;
	private UpdateManager updateMan;
	protected ProgressDialog updateProgressDialog;
	private LinearLayout line_rwgg, line_myrw, line_lsrw, line_message;
	private BadgeView badge, badge1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
				
		userInfo = this.getSharedPreferences("setting", Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));

		Boolean login = userInfo.getBoolean("login", true);
		if (login) {
			startActivity(new Intent(IndexActivity.this, MainActivity.class));
			finish();
		}
		// 检查更新
		if (Global.CheckNetwork(IndexActivity.this)) {
			this.updateMan = new UpdateManager(IndexActivity.this, appUpdateCb);
			this.updateMan.checkUpdate();
			if (!Utils.hasBind(getApplicationContext())) {
				PushManager.startWork(getApplicationContext(),
						PushConstants.LOGIN_TYPE_API_KEY,
						Utils.getMetaValue(IndexActivity.this, "push_api_key"));

				// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
				PushManager.enableLbs(getApplicationContext());
			}
			Log.d("gs_dm:", "gs_" + global.read("gs_dm"));
			List<String> tags = Utils.getTagsList("gs_" + global.read("gs_dm"));
			PushManager.setTags(getApplicationContext(), tags);
		}
		

		setContentView(R.layout.index);
		findview();
		getTotal();
		// LayoutInflater.from(this).inflate(R.layout.activity_index,
		// tabHost.getTabContentView(), true);

	}

	public void findview() {
		line_rwgg = (LinearLayout) this.findViewById(R.id.line_rwgg);
		line_myrw = (LinearLayout) this.findViewById(R.id.line_myrw);
		line_lsrw = (LinearLayout) this.findViewById(R.id.line_lsrw);
		line_message = (LinearLayout) this.findViewById(R.id.line_message);
		line_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IndexActivity.this,
						ListMessageTab.class);
				startActivity(intent);
			}

		});
		line_rwgg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IndexActivity.this, RwggTab.class);
				startActivity(intent);
			}

		});
		line_myrw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IndexActivity.this, MyRwTab.class);
				startActivity(intent);
			}

		});
		line_lsrw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IndexActivity.this, LsRwTab.class);
				startActivity(intent);
			}

		});

		this.badge = (BadgeView) findViewById(R.id.badge);
		this.badge1 = (BadgeView) findViewById(R.id.badge1);
		// this.badge.setText("0");
	}

	// 获取统计

	public void getTotal() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				RpcHandler rpcHandler = new RpcHandler(IndexActivity.this);
				HashMap<String, ?> alist = rpcHandler.getTotalRw("8", 0,
						System.currentTimeMillis());
				Log.d("date", System.currentTimeMillis() + "");
				Log.d("unSubmitTotal",
						Cast.toString(alist.get("unSubmitTotal")));
				Log.d("noHgTotal", Cast.toString(alist.get("noHgTotal")));
				Message message = new Message();
				message.what = 1;
				message.obj = alist;
				handler.sendMessage(message);

			}

		});
		if (Global.CheckNetwork(IndexActivity.this)) {
			t.start();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			HashMap<String, ?> alist = (HashMap<String, ?>) msg.obj;
			switch (msg.what) {
			case 1:
				badge.setText(Cast.toString(alist.get("unSubmitTotal")));
				badge1.setText(Cast.toString(alist.get("noHgTotal")));
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_account:
			Intent intent = new Intent(IndexActivity.this,
					AccountActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 自动更新回调函数
	UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() {

		public void downloadProgressChanged(int progress) {
			if (updateProgressDialog != null
					&& updateProgressDialog.isShowing()) {
				updateProgressDialog.setProgress(progress);
			}

		}

		public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
			if (updateProgressDialog != null
					&& updateProgressDialog.isShowing()) {
				updateProgressDialog.dismiss();
			}
			if (sucess) {
				updateMan.update();
			} else {
				DialogHelper.Confirm(IndexActivity.this,
						R.string.dialog_error_title,
						R.string.dialog_downfailed_msg,
						R.string.dialog_downfailed_btnrep,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								updateMan.downloadPackage();

							}
						}, R.string.dialog_downfailed_btnnext, null);
			}
		}

		public void downloadCanceled() {
			// TODO Auto-generated method stub

		}

		public void checkUpdateCompleted(Boolean hasUpdate,
				CharSequence updateInfo) {
			if (hasUpdate) {
				DialogHelper.Confirm(
						IndexActivity.this,
						getText(R.string.dialog_update_title),
						getText(R.string.dialog_update_msg).toString()
								+ updateInfo
								+ getText(R.string.dialog_update_msg2)
										.toString(),
						getText(R.string.dialog_update_btnupdate),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								updateProgressDialog = new ProgressDialog(
										IndexActivity.this);
								updateProgressDialog
										.setMessage(getText(R.string.dialog_downloading_msg));
								updateProgressDialog.setIndeterminate(false);
								updateProgressDialog
										.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								updateProgressDialog.setMax(100);
								updateProgressDialog.setProgress(0);
								updateProgressDialog.show();

								updateMan.downloadPackage();
							}
						}, getText(R.string.dialog_update_btnnext), null);
			}

		}
	};
	

}
