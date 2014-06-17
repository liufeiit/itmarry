package com.dyj.activity;

import com.dyj.R;
import com.dyj.app.Global;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public abstract class GetValueActivity extends Activity {

	private Global global;
	private SharedPreferences userInfo;
	private TextView tv;
	private int rw_dm;
	private SharedPreferences rwInfo;
	protected String hint = "";
	protected String valueName = null;
	protected int inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE;
	protected String title="title";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		userInfo = this.getSharedPreferences("setting", Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		setContentView(R.layout.activity_get_value_tab);
		this.tv = (TextView) findViewById(R.id.textView);
		this.rw_dm=this.getIntent().getIntExtra("rw_dm", 0);
		
		
		
		InitValue();
		this.setTitle(title);
		tv.setHint(this.hint);
		tv.setInputType(inputType);
		
		//设置SharedPreferences
		
		this.rwInfo=this.getSharedPreferences(this.rw_dm+"", Context.MODE_PRIVATE);
		if (null != this.valueName) {
			tv.setText(this.rwInfo.getString(this.valueName, ""));
		}
	}

	// 设置基本参数
	protected abstract void InitValue();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_wcqk_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_save:
			//global.save(valueName, tv.getText().toString());
			this.rwInfo.edit().putString(this.valueName, tv.getText().toString()).commit();
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 屏蔽返回键的代码
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
