package com.dyj.tabs;

import java.sql.SQLException;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.dyj.R;
import com.dyj.R.layout;
import com.dyj.R.menu;
import com.dyj.activity.MapActivity;
import com.dyj.db.DatabaseHelper;
import com.dyj.db.bean.Rw;
import com.dyj.dialog.LoadingDialog;
import com.dyj.push.Utils;
import com.dyj.tabs.GetLbsTab.MyLocationListenner;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LjghTab extends MapActivity {
	private MKSearch mMKSearch = null;
	private LocationData locData;
	private MapController mMapController;

	private DatabaseHelper dataHelper = null;
	private int RW_DM = 0;
	private double wd = 0;
	private double jd = 0;
	private double mywd = 0;
	private double myjd = 0;
	private LocationClient mLocClient;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	public MyLocationListenner myListener = new MyLocationListenner();

	private Dialog pdialog;
	private MyLocationOverlay myLocationOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		this.jd=intent.getDoubleExtra("jd", 0);
		this.wd=intent.getDoubleExtra("wd", 0);

		// 获取任务的坐标
		Log.d("wd", jd + "");
		Log.d("jd", wd + "");
		locData.latitude = wd;
		locData.longitude = jd;
		myLocationOverlay = new MyLocationOverlay(mMapView);

		myLocationOverlay.setData(locData);
		GeoPoint point = new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(18);// 设置地图zoom级别
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		mMapView.getController().animateTo(point);

		// 开始获取当前位置坐标
		
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		mLocClient.setAK(Utils.getMetaValue(getApplication(), "map_api_key"));
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		
		mLocClient.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ljgh_tab, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())// 得到被点击的item的itemId
		{
		case R.id.menu_begin:
			pdialog = LoadingDialog.createLoadingDialog(LjghTab.this,
					"正在规划路径...");
			pdialog.show();
			mLocClient.stop();
			mMKSearch = new MKSearch();
			mMKSearch.init(mBMapMan, new MapSearchListener());
			MKPlanNode start = new MKPlanNode();
			start.pt = new GeoPoint((int) (mywd * 1E6), (int) (myjd * 1E6));
			MKPlanNode end = new MKPlanNode();
			end.pt = new GeoPoint((int) (wd * 1E6), (int) (jd * 1E6));
			mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
			mMKSearch.drivingSearch(null, start, null, end);
			break;
		
		}
		
		return super.onOptionsItemSelected(item);
	}


	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
				return;

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			Log.d("lbs message:", sb.toString());
			mywd = location.getLatitude();
			myjd = location.getLongitude();
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
			locData.direction = location.getDerect();
			// locData.direction = 2.0f;
			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				Log.d("LocationOverlay", "receive location, animate to it");
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
				myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
			}
			// 首次定位完成
			isFirstLoc = false;
			
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			// TODO Auto-generated method stub
			if (poiLocation == null) {
				return;
			}
		}

	}
	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		//Toast.makeText(LjghTab.this, "正在定位……", Toast.LENGTH_SHORT).show();
	}

	public class MapSearchListener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int arg1) {
			// TODO Auto-generated method stub
			if (result == null) {
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(LjghTab.this, mMapView); // 此处仅展示一个方案作为示例
			routeOverlay.setData(result.getPlan(0).getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
			GeoPoint point = new GeoPoint((int) (mywd * 1e6),
					(int) (myjd * 1e6));
			mMapController.setCenter(point);
			mMapView.refresh();
			pdialog.dismiss();

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

}
