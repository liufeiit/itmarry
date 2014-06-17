package com.dyj.tabs;

import android.os.Bundle;

import com.dyj.activity.GetValueActivity;

public class getCfddTab extends GetValueActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void InitValue() {
		// TODO Auto-generated method stub
		this.title="旧资产存放地点";
		this.hint="请输入旧资产存放地点";
		this.valueName="Sg_cfdd";
	}

}
