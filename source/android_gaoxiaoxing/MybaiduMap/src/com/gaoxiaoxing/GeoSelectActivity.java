package com.gaoxiaoxing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;
import com.gaoxiaoxing.adapter.SelectPoiAdapter;
import com.gaoxiaoxing.model.SchoolInfo;
import com.gaoxiaoxing.model.cache.ModelCache;
import com.gaoxiaoxing.view.CustomActionBar;

public class GeoSelectActivity extends Activity {

	private CustomActionBar actionbar;
	private MapView map;
	private ListView poiList;
	private ProgressDialog dialog;
	private ArrayList<SchoolInfo> infos;

	private SchoolInfo choicedInfo;

	private TextView name;

	private PopupOverlay pop;
	private View popView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMap();
		initUi();

	}

	@Override
	protected void onResume() {
		super.onResume();
		dialog = ProgressDialog.show(this, "请稍等", "正在获取数据");
		dialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		LBSSearch();
	}
	

	private void LBSSearch() {
		CloudManager.getInstance().init(new SearchResult());
		LocalSearchInfo info = new LocalSearchInfo();
		info.ak = getResources().getString(R.string.LBS_AK);
		info.region = "广州市";
		info.pageSize = 50;
		info.geoTableId = 43607;
		CloudManager.getInstance().localSearch(info);
	}

	private void initMap() {
		MyApplication myapp = (MyApplication) getApplication();
		BMapManager mapmanager = myapp.mapMan;
		if (mapmanager == null) {
			mapmanager = new BMapManager(getApplicationContext());
			if (mapmanager.init(myapp.getKEK(), new MyApplication.MKListener()))
				mapmanager.start();
		} else {
			mapmanager.start();
		}

	}

	private void initUi() {
		setContentView(R.layout.activity_geoselect);
		actionbar = (CustomActionBar) findViewById(R.id.customactionbar);
		poiList = (ListView) findViewById(R.id.poi_list);
		name = (TextView) findViewById(R.id.selected);
		actionbar.setTitle("选择学校");
		actionbar.setLeftText("返回");
		actionbar.setRightText("确定");
		actionbar.setLeftOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finishWithOutResult();
			}
		});
		actionbar.setRightOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finishHaveResult(0,getIntent());
			}
		});
		actionbar.getRightView().setEnabled(false);
		map = (MapView) findViewById(R.id.baidumap);
		map.getController().setZoom(16f);// 缩放范围:3~19
		poiList.setOnItemClickListener(new OnItemClickListener() {
			private int before = -1;

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				actionbar.getRightView().setEnabled(true);
				if (position != before) {
					choicedInfo = (SchoolInfo) parent.getAdapter().getItem(
							position);
					moveMapAndShowPopup(choicedInfo);
					getIntent().putExtra("Info", choicedInfo);
					before = position;
				}
			}
		});
		pop = new PopupOverlay(map, null);
		popView = LayoutInflater.from(this).inflate(R.layout.popup_layout2,
				null);
		popView.setTag(popView.findViewById(R.id.title));
	}

	// 移动地图并且显示popup
	private void moveMapAndShowPopup(SchoolInfo info) {
		map.getController().animateTo(info.getGeoPoint());
		TextView title = (TextView) popView.getTag();
		title.setText(info.getTitle());
		pop.showPopup(popView, info.getGeoPoint(), 0);
		name.setText("位于:" + info.getTitle());
	}

	class SearchResult implements CloudListener {

		public void onGetSearchResult(CloudSearchResult result, int arg1) {
			if (result != null && result.poiList != null && result.size != 0) {
				infos = new ArrayList<SchoolInfo>();
				for (CloudPoiInfo info : result.poiList) {
					SchoolInfo sinfo = new SchoolInfo(info);
					infos.add(sinfo);
				}
				dialog.dismiss();
				poiList.setAdapter(new SelectPoiAdapter(GeoSelectActivity.this,
						infos));
			} else {
				Toast.makeText(GeoSelectActivity.this, "获取数据失败。请重试。", 1).show();
				GeoSelectActivity.this.finish();
			}
		}

		public void onGetDetailSearchResult(DetailSearchResult arg0, int arg1) {
		}
	}

	private void finishWithOutResult() {
		this.finish();
	}

	private void finishHaveResult(int resultCode,Intent result) {
		setResult(resultCode, result);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		this.finishWithOutResult();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		map.destroy();
		map = null;
	}

}
