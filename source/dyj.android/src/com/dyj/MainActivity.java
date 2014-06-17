package com.dyj;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.phprpc.util.Cast;

import com.dyj.app.Global;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;
import com.dyj.tabs.RwggTab;
import com.dyj.task.TaskLogin;
import com.dyj.update.DialogHelper;
import com.dyj.update.UpdateManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btn_logon;
	private UpdateManager updateMan;
	protected ProgressDialog updateProgressDialog;
	private SharedPreferences userInfo;
	private EditText login_username, login_password;
	private Global global;

	private Dialog pdialog;

	private final static boolean NO_NETWORK = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		global = Global.getInstance();
		userInfo = getSharedPreferences("setting", Context.MODE_PRIVATE);
		// 检查更新
		if (Global.CheckNetwork(MainActivity.this)) {
		this.updateMan = new UpdateManager(MainActivity.this, appUpdateCb);
		this.updateMan.checkUpdate();
		}

		Boolean login = userInfo.getBoolean("login", true);

		// 不需要登录
		if (!login || NO_NETWORK) {
			startActivity(new Intent(MainActivity.this, IndexActivity.class));
			finish();
		}

		this.login_username = (EditText) findViewById(R.id.login_username);
		this.login_password = (EditText) findViewById(R.id.login_password);
		this.btn_logon = (Button) findViewById(R.id.btn_logon);
		this.btn_logon.setOnClickListener(new LogonButtonOnClick());
	}

	private final class LogonButtonOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String username = login_username.getText().toString();
			String password = login_password.getText().toString();

			pdialog = LoadingDialog.createLoadingDialog(MainActivity.this,
					"正在登录...");
			pdialog.show();
			if (Global.CheckNetwork(v.getContext())) {
				Thread t = new Thread(t_loggon);
				t.start();
			}
			else{
				pdialog.dismiss();
			}

		}

	}

	Runnable t_loggon = new Runnable() {

		@Override
		public void run() {

			// TODO Auto-generated method stub
			RpcHandler rpcHandler = new RpcHandler(MainActivity.this);
			HashMap res = rpcHandler.checkLogin(login_username.getText()
					.toString(), login_password.getText().toString());
			Message message = new Message();
			message.what = 1;
			message.obj = res;
			handler.sendMessage(message);
		}

	};

	/** 这里重写handleMessage方法，接受到子线程数据后更新UI **/
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				pdialog.dismiss();
				HashMap res = (HashMap) msg.obj;
				if ((Integer) res.get("errno") == 0) {
					global = Global.getInstance((String) Cast.cast(
							res.get("dm_user"), String.class));
					global.save("dm_user", (String) Cast.cast(
							res.get("dm_user"), String.class));
					global.save("user_mc", (String) Cast.cast(
							res.get("user_mc"), String.class));
					global.save("is_dl",
							(String) Cast.cast(res.get("is_dl"), String.class));
					global.save("bm_dm",
							(String) Cast.cast(res.get("bm_dm"), String.class));
					global.save("bm_mc",
							(String) Cast.cast(res.get("bm_mc"), String.class));
					global.save("gs_dm",
							(String) Cast.cast(res.get("gs_dm"), String.class));
					global.save("gs_mc",
							(String) Cast.cast(res.get("gs_mc"), String.class));
					userInfo.edit().putBoolean("login", false).commit();
					userInfo.edit()
							.putString(
									"dm_user",
									(String) Cast.cast(res.get("dm_user"),
											String.class)).commit();
					Intent intent = new Intent(MainActivity.this,
							IndexActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(
							MainActivity.this,
							(String) Cast.cast(res.get("message"), String.class),
							1).show();
				}
				break;
			}
		}
	};

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
				DialogHelper.Confirm(MainActivity.this,
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
						MainActivity.this,
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
										MainActivity.this);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			return true;
		case KeyEvent.KEYCODE_CALL:
			return true;
		case KeyEvent.KEYCODE_SYM:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_STAR:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
