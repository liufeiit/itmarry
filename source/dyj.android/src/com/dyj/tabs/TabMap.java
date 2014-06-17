package com.dyj.tabs;

import java.text.DecimalFormat;

import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.util.verify.BNKeyVerifyListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.dyj.MainActivity;
import com.dyj.R;
import com.dyj.activity.MapActivity;
import com.dyj.app.Global;
import com.dyj.bean.beanRwgg;
import com.dyj.dialog.LoadingDialog;
import com.dyj.rpc.RpcHandler;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TabMap extends MapActivity {

	private String rw_dm = null;
	private beanRwgg bean = null;
	private LocationData locData;
	private MapController mMapController;
	private Dialog pdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初始化导航

		// 初始化导航引擎
		Log.d("MAP_API_KET", this.getMAP_API_KET());

		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
				mNaviEngineInitListener, this.getMAP_API_KET(), mKeyVerifyListener);

		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.activity_tab_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		// mMapView.setSatellite(true);
		// 设置启用内置的缩放控件
		mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		locData = new LocationData();
		locData.direction = 2.0f;
		Intent intent = this.getIntent();
		if (intent.hasExtra("rw_dm")) {
			this.rw_dm = this.getIntent().getStringExtra("rw_dm");

			pdialog = LoadingDialog.createLoadingDialog(TabMap.this,
					"正在加载数据...");
			pdialog.show();
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					RpcHandler rpcHandler = new RpcHandler(TabMap.this);
					try {
						bean = rpcHandler.getRwDisp(rw_dm);
						Message message = new Message();
						message.what = 1;
						message.obj = bean;
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

			});
			if (Global.CheckNetwork(TabMap.this)) {
				t.start();
			} else {
				pdialog.dismiss();
			}
		} else if (intent.hasExtra("jd") && intent.hasExtra("wd")) {

			if (intent.getStringExtra("jd").equals("")
					|| intent.getStringExtra("jd").equals("0.0")
					|| intent.getStringExtra("jd").equals("0")) {
				Toast.makeText(TabMap.this, "当前采集点没有任何地理信息！", 1).show();
				finish();
			} else {
				locData.latitude = Double.parseDouble(intent
						.getStringExtra("wd"));
				locData.longitude = Double.parseDouble(intent
						.getStringExtra("jd"));
			}
		} else {
			locData.latitude = 25.437;
			locData.longitude = 119.054;
		}
		Log.d("tag :", "==========================");
		Log.d("jd :", locData.longitude + "");
		Log.d("wd :", locData.latitude + "");
		Log.d("tag :", "==========================");
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);

		myLocationOverlay.setData(locData);
		GeoPoint point = new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(18);// 设置地图zoom级别
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		mMapView.getController().animateTo(point);

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				beanRwgg br = (beanRwgg) msg.obj;
				Log.d("jd", br.getCjb_jd());
				Log.d("wd", br.getCjd_wd());
				if (br.getCjb_jd().equals("") || br.getCjb_jd().equals("0.0")
						|| br.getCjb_jd().equals("0")) {
					Toast.makeText(TabMap.this, "当前采集点没有任何地理信息！", 1).show();
					finish();
				} else {
					DecimalFormat df = new DecimalFormat(".###");
					locData.latitude = Double.parseDouble(df.format(Double
							.parseDouble(br.getCjd_wd())));
					locData.longitude = Double.parseDouble(df.format(Double
							.parseDouble(br.getCjb_jd())));
				}

				pdialog.dismiss();
				MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
						mMapView);

				myLocationOverlay.setData(locData);
				GeoPoint point = new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6));
				// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
				mMapController.setCenter(point);// 设置地图中心点
				mMapController.setZoom(18);// 设置地图zoom级别
				mMapView.getOverlays().add(myLocationOverlay);
				mMapView.refresh();
				mMapView.getController().animateTo(point);
				break;
			}

		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_ljgh:
			Intent ljgh_intent = new Intent(this, DaoHangTab.class);
			ljgh_intent.putExtra("wd", locData.latitude);
			ljgh_intent.putExtra("jd", locData.longitude);
			startActivity(ljgh_intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tab_map, menu);
		return true;
	}

	// ==============导航==================
	private boolean mIsEngineInitSuccess = false;

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
		}
	};

	private BNKeyVerifyListener mKeyVerifyListener = new BNKeyVerifyListener() {

		@Override
		public void onVerifySucc() {
			// TODO Auto-generated method stub
			Toast.makeText(TabMap.this, "key校验成功", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onVerifyFailed(int arg0, String arg1) {
			// TODO Auto-generated method stub
			Toast.makeText(TabMap.this, "key校验失败", Toast.LENGTH_LONG).show();
		}
	};

}
