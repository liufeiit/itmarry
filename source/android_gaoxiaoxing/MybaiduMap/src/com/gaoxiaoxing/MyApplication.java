package com.gaoxiaoxing;

import android.app.Application;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;

public class MyApplication extends Application {

	private static MyApplication myApp = null;
	BMapManager mapMan = null;
	private String KEY;

	@Override
	public void onCreate() {
		super.onCreate();
		KEY = getResources().getString(R.string.KEY);
		myApp = this;
		initBMapManager();
	}

	private void initBMapManager() {
		if (mapMan == null) {
			mapMan = new BMapManager(this.getApplicationContext());
		}
		if (!mapMan.init(KEY, new MKListener())) {
			Toast.makeText(this, "��ʼ��ʧ�ܣ�", Toast.LENGTH_LONG).show();
		}
	}
	

	public String getKEK() {
		return this.KEY;
	}

	public static Application getMyApp() {
		return myApp;
	}

	static class MKListener implements MKGeneralListener {
		public void onGetNetworkState(int arg0) {
			System.out.println("������ִ���");
		}

		public void onGetPermissionState(int arg0) {
			System.out.println("Ȩ�޴���");
		}

	}

}
