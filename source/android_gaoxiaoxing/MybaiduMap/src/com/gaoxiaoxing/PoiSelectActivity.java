package com.gaoxiaoxing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.gaoxiaoxing.view.CustomActionBar;

public class PoiSelectActivity extends Activity {
	private CustomActionBar actionbar;
	private MapView map;
	private EditText edit;
	private LocationClient client;
	private MyApplication app;
	private View probar,view;
	private ImageView flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMap();
		setContentView(R.layout.activity_poiselect);
		map = (MapView) findViewById(R.id.baidumap);
		actionbar = (CustomActionBar) findViewById(R.id.actionbar);
		edit = (EditText) findViewById(R.id.edit);
		probar = findViewById(R.id.probar);
		flag=(ImageView) findViewById(R.id.flag);
		view.findViewById(R.id.view);
		map.getController().setZoom(15f);
		map.regMapViewListener(app.mapMan, new MapViewListener());
		actionbar.setLeftText("返回");
		actionbar.setRightText("确定");
		actionbar.getRightView().setEnabled(false);
		actionbar.setTitle("地点编辑");
	}

	private void initLocationClient() {
		client = new LocationClient(getApplicationContext());
		MyApplication myapp = (MyApplication) this.getApplication();
		client.setAK(myapp.getKEK());// 设置定位所需的KEY
		client.registerLocationListener(new BDLocationListener() {
			public void onReceivePoi(BDLocation arg0) {
			}

			public void onReceiveLocation(BDLocation res) {
				if (res != null) {
					LocationData data = new LocationData();
					data.latitude = res.getLatitude();
					data.longitude = res.getLongitude();
					data.accuracy = res.getRadius();
					MyLocationOverlay locationOverlay = new MyLocationOverlay(
							map);
					GeoPoint poi = new GeoPoint(
							(int) (res.getLatitude() * 1e6), (int) (res
									.getLongitude() * 1e6));
					locationOverlay.setData(data);
					map.getOverlays().add(locationOverlay);
					map.refresh();
					map.getController().animateTo(poi);
					probar.setVisibility(View.GONE);
					flag.setVisibility(View.VISIBLE);
				}

			}
		});// 注册监听
		LocationClientOption option = new LocationClientOption();// 设置定位参数
		option.setAddrType("all");// 返回所有信息
		option.disableCache(true);// 不缓存定位信息
		option.setCoorType("bd09ll");// 返回的坐标类型
		option.setTimeOut(8000);// 设置定位超时
		client.setLocOption(option);// 传入参数
		client.start();
	}

	private void initMap() {
		app = (MyApplication) getApplication();
		BMapManager mapmanager = app.mapMan;
		if (mapmanager == null) {
			mapmanager = new BMapManager(getApplicationContext());
			if (mapmanager.init(app.getKEK(), new MyApplication.MKListener()))
				mapmanager.start();
		} else {
			mapmanager.start();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		map.destroy();
		map = null;
	}

	class MapViewListener implements MKMapViewListener {

		// 地图完成移动
		public void onMapMoveFinish() {
			// TODO Auto-generated method stub

		}

		// 地图加载完成
		public void onMapLoadFinish() {
			probar.setVisibility(View.VISIBLE);
			initLocationClient();
		}

		// 地图完成动画
		public void onMapAnimationFinish() {
			// TODO Auto-generated method stub

		}

		// 截图回调
		public void onGetCurrentMap(Bitmap arg0) {
			// TODO Auto-generated method stub

		}

		// 点击回调
		public void onClickMapPoi(MapPoi arg0) {
			// TODO Auto-generated method stub

		}
	}
}
