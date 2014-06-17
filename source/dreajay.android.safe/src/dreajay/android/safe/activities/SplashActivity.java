package dreajay.android.safe.activities;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import dreajay.android.safe.R;
import dreajay.android.safe.util.HttpUtil;

public class SplashActivity extends Activity {
	public static final String TAG = "SplashActivity";

	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
	private String currentVersion;
	private String newVersion;
	private TextView tv_splash_version;
	private RelativeLayout rl_splash_root;

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				configUpdate();
			case ERROR:
				Toast.makeText(getApplicationContext(), "网络没有打开", Toast.LENGTH_SHORT).show();
				loadMain();
			default:
				loadMain();
			}
			super.handleMessage(msg);
		}

	};

	protected String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 实现全屏效果：
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 实现无标题栏（但有系统自带的任务栏）：
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号：" + getVersion());
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		playAnimation();

		//是否自动更新
		SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
//		if(sp.getBoolean("autoUpdate", false)) {
			checkUpdate();
//		} else {
//			myHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					loadMain();
//				}
//			}, 2000);
//		}
		
		
	}

	private void checkUpdate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				String updateUrl = getResources()
						.getString(R.string.update_url);
				try {
					String apkInfo = HttpUtil.executeHttpGet(updateUrl);
					Map apkInfoMap = JSONObject.parseObject(apkInfo, Map.class);
					newVersion = (String) apkInfoMap.get("version");
					String description = (String) apkInfoMap.get("description");
					apkurl = (String) apkInfoMap.get("apkurl");
					if(!newVersion.equals(currentVersion)) {
						msg.what = SUCCESS;
						myHandler.sendMessage(msg);
					} else {
						loadMain();
					}
					
				} catch (IOException e) {
//					e.printStackTrace();
					msg.what = ERROR;
					myHandler.sendMessage(msg);
				}

			}
		}).start();
	}

	private void configUpdate() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("更新提醒")
				.setMessage("安全卫士有新版本更新了，是否下载更新？")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(), "准备更新",
								Toast.LENGTH_LONG).show();
						downloadApk();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						loadMain();

					}
				}).setOnCancelListener(new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						loadMain();
					}
				});
		builder.show();
	}

	protected void loadMain() {
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		startActivity(intent);
		finish();
	}

	protected void downloadApk() {
		File downloadPath = getExternalCacheDir();
		File down = Environment.getDownloadCacheDirectory();
		Log.e(TAG, "downloadPath :" + downloadPath.getAbsolutePath());
		HttpUtils http = new HttpUtils();
		Log.e(TAG, "apkurl :" + apkurl);
		String targetPath = getExternalCacheDir()
				+ apkurl.substring(apkurl.lastIndexOf("/"));
		HttpHandler handler = http.download(apkurl, targetPath, false, false,
				new RequestCallBack<File>() {
					@Override
					public void onStart() {
						Toast.makeText(getApplicationContext(), "下载开始",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						int progress = (int) (current * 100 / total);
						Log.e(TAG, progress + "%");
						Toast.makeText(getApplicationContext(),
								"下载中：" + progress + "%", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						File file = responseInfo.result;
						installApk(file);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(getApplicationContext(), "下载失败",
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	public void installApk(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public void playAnimation() {
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(2000);
		rl_splash_root.setAnimation(aa);
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			currentVersion = packageInfo.versionName;
			return currentVersion;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
