package com.gaoxiaoxing;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gaoxiaoxing.utils.ChangeSize;

public class MainActivity extends FragmentActivity {

	private DrawerLayout drawer;
	private RadioGroup rg;
	private FragmentTabHost fragmentTabHost;
	private TextView loctext;
	private LocationClient client;
	private MyApplication myapp;
	private DisplayMetrics outMetrics;
	private View mid_btn;

	private PopupWindow popup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myapp = (MyApplication) this.getApplication();
		outMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		initTabHost();

		initView();

		checkNetWork();

		initLocationClient();

	}

	private void checkNetWork() {
		ConnectivityManager con_mag = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (!hasNetworkConnected(con_mag)) {
			Toast.makeText(this, "无网络连接！请打开或检查网络。", Toast.LENGTH_LONG).show();
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		drawer = (DrawerLayout) findViewById(R.id.drawer);
		rg = (RadioGroup) findViewById(R.id.tabbar_rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rg_rb1:
					fragmentTabHost.setCurrentTabByTag("myschool");
					break;
				case R.id.rg_rb2:
					fragmentTabHost.setCurrentTabByTag("schooltrip");
				}
			}
		});
		findViewById(R.id.actionbar_user).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						if (drawer.isDrawerOpen(Gravity.RIGHT))
							drawer.closeDrawer(Gravity.RIGHT);
						else
							drawer.openDrawer(Gravity.RIGHT);
					}
				});

		loctext = (TextView) findViewById(R.id.tabbar).findViewWithTag(
				"location");
		findViewById(R.id.main_view).findViewWithTag("login_btn")
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {

					}
				});
		mid_btn = findViewById(R.id.main_mid);
		mid_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (popup == null) {
					popup = createPopupWindow();
					showPopup(popup);
					return;
				}
				if (popup.isShowing()) {
					popup.dismiss();
				} else {
					showPopup(popup);
				}
			}
		});
	}

	// popupwindow
	private PopupWindow createPopupWindow() {
		PopupWindow pop = new PopupWindow(MainActivity.this);
		View popupview = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.popup_window, null);
		popupview.findViewById(R.id.popou_edit).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								EditlRoadActivity.class);
						MainActivity.this.startActivity(intent);
						overridePendingTransition(R.anim.tranlate_in,
								R.anim.activity_anim_alpha_out);
					}
				});
		pop.setContentView(popupview);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setWidth(ChangeSize.dipToPx(250, outMetrics));
		pop.setHeight(ChangeSize.dipToPx(125, outMetrics));
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		return pop;
	}

	private void showPopup(PopupWindow pop) {
		pop.showAtLocation(mid_btn, Gravity.BOTTOM, 0, getView(R.id.tabbar_rg)
				.getHeight());
	}

	private View getView(int id) {
		return findViewById(id);
	}

	private void initTabHost() {
		// TODO Auto-generated method stub
		fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		fragmentTabHost.setup(this, getSupportFragmentManager(),
				R.id.realtabcontent);
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("myschool")
				.setIndicator("1"), MySchoolFragment.class, null);
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("schooltrip")
				.setIndicator("2"), SchoolTripFragment.class, null);
	}

	private boolean hasNetworkConnected(ConnectivityManager con_mag) {
		NetworkInfo info = con_mag.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}

	private void initLocationClient() {
		client = new LocationClient(getApplicationContext());
		client.setAK(myapp.getKEK());// 设置定位所需的KEY
		client.registerLocationListener(new BDLocationListener() {
			public void onReceivePoi(BDLocation arg0) {
			}

			public void onReceiveLocation(BDLocation res) {
				if (res.getAddrStr() == null) {
					loctext.setText("获取位置失败！");
				} else {
					loctext.setText(res.getCity() + "," + res.getDistrict()
							+ "," + res.getStreet() + ","
							+ res.getStreetNumber());
					// LocationData data=new LocationData();
					// data.latitude=res.getLatitude();
					// data.longitude=res.getLongitude();
					// data.accuracy=res.getRadius();
					// ((SchoolTripFragment)MainActivity.this.schooltrip).changeMyLocation(data);
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

	@Override
	public void onBackPressed() {
		createDialog().show();
	}

	public Dialog createDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("是否退出应用?");
		builder.setPositiveButton("是", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
			}

		});
		builder.setNegativeButton("否", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}

}
