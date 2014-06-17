package com.guide.geek.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.guide.geek.R;
import com.guide.geek.utils.WarnUtils;

public class MainLeftFragment extends BaseFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_left_menu1, null);
	}

	private LinearLayout layoutMortgageCalculator;
	private LinearLayout layoutFeedback;
	private LinearLayout layoutAboutUs;
	private LinearLayout layoutCheckForUpdates;
	private LinearLayout layout_out;
	private RelativeLayout left_menu;
	private ListView listviewLeftCategory;
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.initViews();
	}

	private void initViews() {
		View parent = this.getView();

		this.listviewLeftCategory = (ListView) parent.findViewById(R.id.listview_left_category);
//		this.left_menu = (RelativeLayout) parent.findViewById(R.id.left_menu);
//		this.layoutMortgageCalculator = (LinearLayout) parent.findViewById(R.id.layout_mortgage_calculator);
//		this.layoutFeedback = (LinearLayout) parent.findViewById(R.id.layout_feedback);
//		this.layoutAboutUs = (LinearLayout) parent.findViewById(R.id.layout_about_us);
//		this.layoutCheckForUpdates = (LinearLayout) parent.findViewById(R.id.layout_check_for_updates);
//
//		this.layout_out = (LinearLayout) parent.findViewById(R.id.layout_out);
//
//		this.left_menu.setOnClickListener(this);
//		this.layoutMortgageCalculator.setOnClickListener(this);
//		this.layoutFeedback.setOnClickListener(this);
//		this.layoutAboutUs.setOnClickListener(this);
//		this.layoutCheckForUpdates.setOnClickListener(this);
//		this.layout_out.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.left_menu:{
//			WarnUtils.toast(this.context, "登录");
//			break;
//		}
//		case R.id.layout_mortgage_calculator: {
//			WarnUtils.toast(this.context, "登录");
//			break;
//		}
//		case R.id.layout_feedback: {
//			WarnUtils.toast(this.context, "下载离线盘点数据");
//			break;
//		}
//		case R.id.layout_about_us: {
//			WarnUtils.toast(this.context, "系统设置");
//			break;
//		}
//		case R.id.layout_check_for_updates: {
//			WarnUtils.toast(this.context, "修改用户密码");
//			break;
//		}
//		case R.id.layout_out: {
//			WarnUtils.toast(this.context, "退出");
//			break;
//		}
		}
	}
}
