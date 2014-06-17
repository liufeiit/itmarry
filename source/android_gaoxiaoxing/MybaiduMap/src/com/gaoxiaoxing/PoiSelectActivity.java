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
		actionbar.setLeftText("����");
		actionbar.setRightText("ȷ��");
		actionbar.getRightView().setEnabled(false);
		actionbar.setTitle("�ص�༭");
	}

	private void initLocationClient() {
		client = new LocationClient(getApplicationContext());
		MyApplication myapp = (MyApplication) this.getApplication();
		client.setAK(myapp.getKEK());// ���ö�λ�����KEY
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
		});// ע�����
		LocationClientOption option = new LocationClientOption();// ���ö�λ����
		option.setAddrType("all");// ����������Ϣ
		option.disableCache(true);// �����涨λ��Ϣ
		option.setCoorType("bd09ll");// ���ص���������
		option.setTimeOut(8000);// ���ö�λ��ʱ
		client.setLocOption(option);// �������
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

		// ��ͼ����ƶ�
		public void onMapMoveFinish() {
			// TODO Auto-generated method stub

		}

		// ��ͼ�������
		public void onMapLoadFinish() {
			probar.setVisibility(View.VISIBLE);
			initLocationClient();
		}

		// ��ͼ��ɶ���
		public void onMapAnimationFinish() {
			// TODO Auto-generated method stub

		}

		// ��ͼ�ص�
		public void onGetCurrentMap(Bitmap arg0) {
			// TODO Auto-generated method stub

		}

		// ����ص�
		public void onClickMapPoi(MapPoi arg0) {
			// TODO Auto-generated method stub

		}
	}
}
