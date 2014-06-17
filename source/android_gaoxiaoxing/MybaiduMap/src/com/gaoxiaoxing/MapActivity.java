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
	// MapView ��ͼ�����ؼ�
	MapView mMapView = null;
	// MapController���ڿ��Ƶ�ͼ
	MapController mMapController = null;
	// ��λ����
	LocationClient client = null;
	// ��Կ
	private static String LBS_AK;

	private final int TABLE_ID = 43607;

	private Button location;// ��λ��ť

	private MyLocationOverlay myLocation;// �û�λ��ͼ��

	private CloudOverlay lbsoverlay;// lbs��������ǩͼ��

	private String address;// �û����ڵ�

	MyApplication myapp;

	private GeoPoint myLoc;// �ҵ�λ������

	private SharedPreferences sp;

	private boolean hasLocation;

	private ButtonGroup g;

	private PopupWindow pop;

	WindowManager manager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���LBS_AK
		LBS_AK = getResources().getString(R.string.LBS_AK);
		myapp = (MyApplication) this.getApplication();
		manager = MapActivity.this.getWindowManager();
		if (myapp.mapMan == null) {
			myapp.mapMan = new BMapManager(this);
			myapp.mapMan.init(myapp.getKEK(), new MyApplication.MKListener());
			System.out.println("���³�ʼ��");
		}
		// ��ʼ��������
		initLocationClient();
		// ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		g = (ButtonGroup) findViewById(R.id.btn_menu);
		g.hideButton();
		g.setItemOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btns_1:
					if (address == null) {
						Toast.makeText(MapActivity.this, "û�л�ȡ���û���λ�ã������¶�λ��", 0)
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
				System.out.println("��ɼ���");
			}

			public void onMapAnimationFinish() {
			}

			public void onGetCurrentMap(Bitmap arg0) {
			}

			public void onClickMapPoi(MapPoi arg0) {
			}
		});
		initOverlays();// ��ʼ��ͼ��
		mMapView.setBuiltInZoomControls(false);
		// �����������õ����ſؼ�
		mMapController = mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		sp = getSharedPreferences("Mylocation", this.MODE_PRIVATE);
		float MyLatitude, MyLongitude;
		MyLatitude = sp.getFloat("Latitude", 39.913164f);
		MyLongitude = sp.getFloat("Longitude", 116.41203f);
		GeoPoint geo = new GeoPoint((int) (MyLatitude * 1e6),
				(int) (MyLongitude * 1e6));
		// �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapController.setCenter(geo);// ���õ�ͼ���ĵ�
		mMapController.setZoom(12);// ���õ�ͼzoom����
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
		client.setAK(myapp.getKEK());// ���ö�λ�����KEY
		client.registerLocationListener(new MyLocationListener());// ע�����
		LocationClientOption option = new LocationClientOption();// ���ö�λ����
		option.setAddrType("all");// ����������Ϣ
		option.disableCache(true);// �����涨λ��Ϣ
		option.setCoorType("bd09ll");// ���ص���������
		// option.setTimeOut(8000);���ö�λ��ʱ
		client.setLocOption(option);// �������
	}

	// ��ʼ��ͼ��
	private void initOverlays() {
		myLocation = new MyLocationOverlay(mMapView);
		lbsoverlay = new CloudOverlay(this, mMapView);
		mMapView.getOverlays().add(myLocation);
		mMapView.getOverlays().add(lbsoverlay);
	}

	// ������ť�¼�����
	public void search(View v) {
		// �����ܱ�ѧУ�������δ��λ��ִ�ж�λ���ܣ��������û����ڵ����ܱߵ�ѧУ
		if (address == null) {
			requestLocation();
			return;
		} else
			searchLocal(address);

	}

	// �����ܱ�
	private void searchLocal(String address) {
		System.out.println("����ѧУ");
		LocalSearchInfo info = new LocalSearchInfo();
		info.ak = LBS_AK;
		info.geoTableId = TABLE_ID;// ������
		info.region = address;// ��������
		// ���з�ʽ info.sortby
		CloudManager.getInstance().localSearch(info);// ���б��ؼ���
	}

	// ��λ��ť�¼�����
	public void location(View v) {
		System.out.println("��λ");
		if (location == null) {
			location = (Button) v;
		}
		v.setBackgroundResource(R.drawable.bg_list);
		v.setClickable(false);
		AnimationDrawable animdraw = (AnimationDrawable) v.getBackground();
		animdraw.start();
		Toast.makeText(this, "���ڶ�λ....", 0).show();
		requestLocation();
	}

	// ��λ����
	private void requestLocation() {
		if (client != null && !client.isStarted()) {
			client.start();
		}
		if (client != null && client.isStarted()) {
			client.requestLocation();
		}
	}

	// ���Ű�ť�¼�����
	public void onZoom(View v) {
		if (v.getId() == R.id.zoom_in) {
			mMapController.zoomIn();
		}
		if (v.getId() == R.id.zoom_out) {
			mMapController.zoomOut();
		}
	}

	class MyLocationListener implements BDLocationListener {
		// ��λ���ܵĻص�����
		public void onReceiveLocation(BDLocation location) {
			if (sp != null) {
				Editor editor = sp.edit();
				editor.putFloat("Latitude", (float) location.getLatitude());
				editor.putFloat("Longitude", (float) location.getLongitude());
				if (!editor.commit()) {
					Toast.makeText(MapActivity.this, "����λ��ʧ��", 0).show();
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
					"��������λ���ǣ�" + location.getCity() + location.getDistrict(), 0)
					.show();
		}

		public void onReceivePoi(BDLocation location) {

		}

	}

	class MyCloudSearchListener implements CloudListener {
		public void onGetDetailSearchResult(DetailSearchResult result, int arg1) {
			// ��ϸ�������ø÷���
		}

		public void onGetSearchResult(CloudSearchResult result, int arg1) {
			// ���ؼ������ø÷���
			if (result != null && result.poiList != null
					&& result.poiList.size() != 0) {
				lbsoverlay.setData(result.poiList);// ��������
				mMapView.refresh();// ��ͼˢ��
				lbsoverlay.showPopu(0);
				if (pop != null) {
					pop.dismiss();
				}
			}

		}

	}

	// ��������
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
