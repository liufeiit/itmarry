package com.gaoxiaoxing;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gaoxiaoxing.adapter.RoadListAdapter;
import com.gaoxiaoxing.model.SchoolInfo;
import com.gaoxiaoxing.view.CustomActionBar;
import com.gaoxiaoxing.view.FooterList;
import com.gaoxiaoxing.view.HeaderList;

public class EditlRoadActivity extends Activity {

	private Context context;
	private ListView roadlist;
	private View footer, header;
	private CustomActionBar actionbar;
	private List roadInfo;
	
	static int RETURN_START=0,RETURN_FINAL=1,RETURN_POINT=2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		roadInfo = new ArrayList();
		setContentView(R.layout.activity_editroad);
		context = this;
		initUi();
	}

	private void initUi() {
		actionbar = (CustomActionBar) findViewById(R.id.actionbar);
		actionbar.setLeftText("保存");
		actionbar.setTitle("设计线路");
		actionbar.setRightText("完成");
		roadlist = (ListView) findViewById(R.id.road_list);
		header = LayoutInflater.from(this).inflate(R.layout.list_header, null);
		footer = LayoutInflater.from(this).inflate(R.layout.list_footer, null);
		// 初始化listview的header和footer
		roadlist.setAdapter(null);
		roadlist.addHeaderView(header);
		roadlist.setAdapter(new RoadListAdapter(context, roadInfo));
		roadlist.addFooterView(footer);
		roadlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				System.out.println("pos:" + position);
				System.out.println("id:" + id);
				if (position == 0) {
					startActForRes(GeoSelectActivity.class, RETURN_START);
					return;
				}
				if (position == parent.getCount() - 1) {
					startActForRes(GeoSelectActivity.class, RETURN_FINAL);
					return;
				}
				if (position==parent.getCount()-2) {
					startActForRes(PoiSelectActivity.class, RETURN_POINT);
					return;
				}
			}
		});
	}

	private void startActForRes(Class actClas, int requestCode) {
		Intent t = getIntent().setClass(context, actClas);
		EditlRoadActivity.this.startActivityForResult(t, requestCode);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			SchoolInfo info = (SchoolInfo) data.getSerializableExtra("Info");
			switch (requestCode) {
			case 0://返回起点条目
				HeaderList hl = (HeaderList) header;
				hl.setData(info);
				break;
			case 1://返回终点条目
				FooterList fl = (FooterList) footer;
				fl.setData(info);
				break;
			case 2://返回中折点条目
				;
				
			}
		}
	}

}
