package com.dyj.activity;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.dyj.push.Utils;

import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity {
	protected BMapManager mBMapMan = null;
	protected MapView mMapView = null;
	private String MAP_API_KET;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setMAP_API_KET(Utils.getMetaValue(getApplication(), "map_api_key"));
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(MAP_API_KET, null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

	public String getMAP_API_KET() {
		return MAP_API_KET;
	}

	public void setMAP_API_KET(String mAP_API_KET) {
		MAP_API_KET = mAP_API_KET;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
}
