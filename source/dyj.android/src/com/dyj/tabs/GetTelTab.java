package com.dyj.tabs;

import android.os.Bundle;
import android.text.InputType;

import com.dyj.activity.GetValueActivity;

public class GetTelTab extends GetValueActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void InitValue() {
		// TODO Auto-generated method stub
		this.title="���ʲ���������ϵ�绰";
		this.hint="��������ʲ���������ϵ�绰";
		this.valueName="Sg_tel";
		this.inputType=InputType.TYPE_CLASS_PHONE;
	}

}
