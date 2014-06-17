package com.dyj.tabs;

import com.dyj.activity.GetValueActivity;

import android.os.Bundle;

public class GetZcbhTab extends GetValueActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void InitValue() {
		// TODO Auto-generated method stub
		this.title="新资产编号";
		this.hint="请输入新资产编号";
		this.valueName="Sg_zcbh";
	}

	

}
