package com.hongkong.stiqer.ui.base;

import java.util.LinkedList;

import com.hongkong.stiqer.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	public static LinkedList<Activity> sAllActivitys = new LinkedList<Activity>();

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		sAllActivitys.add(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sAllActivitys.remove(this);
	}

	public static void finishAll() {
		for (Activity activity : sAllActivitys) {
			activity.finish();
		}
		sAllActivitys.clear();
	}

	public static void exit() {
		finishAll();
		System.exit(0);
	}

	public void showToast(String message) {
		View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
		Toast toast = new Toast(getApplicationContext());
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.content);
		tv.setText("" + message);
		toast.show();
	}
}