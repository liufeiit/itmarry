package com.hongkong.stiqer.tab;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.RankAdapter;
import com.hongkong.stiqer.entity.Rank;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.Util;

public class RankActivity extends BaseActivity {
	
	List<Rank>              rankList;
	GridView                checkinGridView;
	RankAdapter             rankAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity_photocheck);
		initData();
		initView();
		checkinGridView.setAdapter(rankAdapter);
	}

	private void initView() {
		checkinGridView = (GridView)findViewById(R.id.checkin_gridview);
		rankAdapter = new RankAdapter(this,rankList);
	}
	private void initData() {
		Intent t = getIntent();
		rankList = (List<Rank>) Util.readJson2EntityList(t.getStringExtra("rank"), new Rank());
	}
}
