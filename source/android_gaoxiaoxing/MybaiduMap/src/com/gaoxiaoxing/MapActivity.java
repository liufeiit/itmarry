package com.gaoxiaoxing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.test.baidumap.myview.ButtonGroup;
import com.test.baidumap.overlay.CloudOverlay;

public class MapActivity extends Activity {

	BMapManager mBMapMan = null;
	// MapView 地图的主控件
	MapView mMapView = null;
	// MapController用于控制地图
	MapController mMapController = null;
	// 定位服务
	LocationClient client = null;
	// 密钥
	private static String LBS_AK;

	private final int TABLE_ID = 43607;

	private Button location;// 定位按钮

	private MyLocationOverlay myLocation;// 用户位置图层

	private CloudOverlay lbsoverlay;// lbs云搜索标签图层

	private String address;// 用户所在地

	MyApplication myapp;

	private GeoPoint myLoc;// 我的位置坐标

	private SharedPreferences sp;

	private boolean hasLocation;

	private ButtonGroup g;

	private PopupWindow pop;

	WindowManager manager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获得LBS_AK
		LBS_AK = getResources().getString(R.string.LBS_AK);
		myapp = (MyApplication) this.getApplication();
		manager = MapActivity.this.getWindowManager();
		if (myapp.mapMan == null) {
			myapp.mapMan = new BMapManager(this);
			myapp.mapMan.init(myapp.getKEK(), new MyApplication.MKListener());
			System.out.println("重新初始化");
		}
		// 初始化云搜索
		initLocationClient();
		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		g = (ButtonGroup) findViewById(R.id.btn_menu);
		g.hideButton();
		g.setItemOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btns_1:
					if (address == null) {
						Toast.makeText(MapActivity.this, "没有获取到用户的位置，请重新定位。", 0)
								.show();
						break;
					}
					showPop();
					searchLocal(address);
					break;
				}
			}
		});
		mMapView.regMapViewListener(myapp.mapMan, new MKMapViewListener() {
			public void onMapMoveFinish() {
			}

			public void onMapLoadFinish() {
				System.out.println("完成加载");
			}

			public void onMapAnimationFinish() {
			}

			public void onGetCurrentMap(Bitmap arg0) {
			}

			public void onClickMapPoi(MapPoi arg0) {
			}
		});
		initOverlays();// 初始化图层
		mMapView.setBuiltInZoomControls(false);
		// 设置启用内置的缩放控件
		mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		sp = getSharedPreferences("Mylocation", this.MODE_PRIVATE);
		float MyLatitude, MyLongitude;
		MyLatitude = sp.getFloat("Latitude", 39.913164f);
		MyLongitude = sp.getFloat("Longitude", 116.41203f);
		GeoPoint geo = new GeoPoint((int) (MyLatitude * 1e6),
				(int) (MyLongitude * 1e6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(geo);// 设置地图中心点
		mMapController.setZoom(12);// 设置地图zoom级别
		hasLocation = false;
	}

	protected void showPop() {
		if (pop == null) {
			pop = new PopupWindow(MapActivity.this);
			View view = LayoutInflater.from(MapActivity.this).inflate(
					R.layout.popup_window, null);
			pop.setContentView(view);
			pop.setBackgroundDrawable(null);
			pop.setWidth(manager.getDefaultDisplay().getWidth() / 2);
			pop.setHeight(manager.getDefaultDisplay().getHeight() / 7);
		}
		if (pop != null && !pop.isShowing())
			pop.showAtLocation(mMapView, Gravity.CENTER, 0, 0);
	}

	private void initLocationClient() {
		client = new LocationClient(getApplicationContext());
		CloudManager.getInstance().init(new MyCloudSearchListener());
		client.setAK(myapp.getKEK());// 设置定位所需的KEY
		client.registerLocationListener(new MyLocationListener());// 注册监听
		LocationClientOption option = new LocationClientOption();// 设置定位参数
		option.setAddrType("all");// 返回所有信息
		option.disableCache(true);// 不缓存定位信息
		option.setCoorType("bd09ll");// 返回的坐标类型
		// option.setTimeOut(8000);设置定位超时
		client.setLocOption(option);// 传入参数
	}

	// 初始化图层
	private void initOverlays() {
		myLocation = new MyLocationOverlay(mMapView);
		lbsoverlay = new CloudOverlay(this, mMapView);
		mMapView.getOverlays().add(myLocation);
		mMapView.getOverlays().add(lbsoverlay);
	}

	// 搜索按钮事件处理
	public void search(View v) {
		// 搜索周边学校，如果还未定位则执行定位功能，再搜索用户所在地区周边的学校
		if (address == null) {
			requestLocation();
			return;
		} else
			searchLocal(address);

	}

	// 搜索周边
	private void searchLocal(String address) {
		System.out.println("搜索学校");
		LocalSearchInfo info = new LocalSearchInfo();
		info.ak = LBS_AK;
		info.geoTableId = TABLE_ID;// 索引表
		info.region = address;// 区域名称
		// 排列方式 info.sortby
		CloudManager.getInstance().localSearch(info);// 进行本地检索
	}

	// 定位按钮事件处理
	public void location(View v) {
		System.out.println("定位");
		if (location == null) {
			location = (Button) v;
		}
		v.setBackgroundResource(R.drawable.bg_list);
		v.setClickable(false);
		AnimationDrawable animdraw = (AnimationDrawable) v.getBackground();
		animdraw.start();
		Toast.makeText(this, "正在定位....", 0).show();
		requestLocation();
	}

	// 定位请求
	private void requestLocation() {
		if (client != null && !client.isStarted()) {
			client.start();
		}
		if (client != null && client.isStarted()) {
			client.requestLocation();
		}
	}

	// 缩放按钮事件处理
	public void onZoom(View v) {
		if (v.getId() == R.id.zoom_in) {
			mMapController.zoomIn();
		}
		if (v.getId() == R.id.zoom_out) {
			mMapController.zoomOut();
		}
	}

	class MyLocationListener implements BDLocationListener {
		// 定位功能的回调函数
		public void onReceiveLocation(BDLocation location) {
			if (sp != null) {
				Editor editor = sp.edit();
				editor.putFloat("Latitude", (float) location.getLatitude());
				editor.putFloat("Longitude", (float) location.getLongitude());
				if (!editor.commit()) {
					Toast.makeText(MapActivity.this, "保存位置失败", 0).show();
				}
			}
			address = location.getDistrict();
			LocationData data = new LocationData();
			data.accuracy = location.getRadius();
			data.latitude = location.getLatitude();
			data.longitude = location.getLongitude();
			myLoc = new GeoPoint((int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6));
			myLocation.setData(data);
			mMapView.refresh();
			mMapView.getController().animateTo(myLoc);
			if (MapActivity.this.location != null) {
				AnimationDrawable animdraw = (AnimationDrawable) MapActivity.this.location
						.getBackground();
				animdraw.stop();
				MapActivity.this.location
						.setBackgroundResource(R.drawable.btn_location);
				MapActivity.this.location.setClickable(true);
				if (!hasLocation) {
					g.showButton();
					hasLocation = !hasLocation;
				}
			}
			Toast.makeText(MapActivity.this,
					"你所以在位置是：" + location.getCity() + location.getDistrict(), 0)
					.show();
		}

		public void onReceivePoi(BDLocation location) {

		}

	}

	class MyCloudSearchListener implements CloudListener {
		public void onGetDetailSearchResult(DetailSearchResult result, int arg1) {
			// 详细检索调用该方法
		}

		public void onGetSearchResult(CloudSearchResult result, int arg1) {
			// 本地检索调用该方法
			if (result != null && result.poiList != null
					&& result.poiList.size() != 0) {
				lbsoverlay.setData(result.poiList);// 设置数据
				mMapView.refresh();// 地图刷新
				lbsoverlay.showPopu(0);
				if (pop != null) {
					pop.dismiss();
				}
			}

		}

	}

	// 生命周期
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	protected void onStop() {
		super.onStop();
		client.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
		mMapView.destroy();
	}

}
