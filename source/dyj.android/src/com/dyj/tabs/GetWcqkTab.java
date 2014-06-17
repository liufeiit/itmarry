package com.dyj.tabs;

import com.dyj.R;
import com.dyj.R.layout;
import com.dyj.R.menu;
import com.dyj.activity.GetValueActivity;
import com.dyj.app.Global;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GetWcqkTab extends GetValueActivity {

	@Override
	protected void InitValue() {
		// TODO Auto-generated method stub
		this.title="完成情况";
		this.hint="请输入完成情况";
		this.valueName="Sg_Wcqk";
	}



	

}
