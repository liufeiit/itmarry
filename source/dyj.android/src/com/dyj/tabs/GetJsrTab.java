package com.dyj.tabs;

import android.os.Bundle;

import com.dyj.activity.GetValueActivity;

public class GetJsrTab extends GetValueActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void InitValue() {
		// TODO Auto-generated method stub
		this.title="旧资产接收人姓名";
		this.hint="请输入旧资产接收人姓名";
		this.valueName="Sg_jsr";
	}

}
