package com.dyj.tabs;


import com.dyj.R;
import com.dyj.R.id;
import com.dyj.R.layout;
import com.dyj.R.menu;
import com.dyj.app.Global;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class GetSeachKeyForRwggTab extends Activity {

	private EditText yh_mc,gddw,dbzch,zcbh,azdz;
	private Global global;
	private SharedPreferences userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userInfo = this.getSharedPreferences("setting", Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		setContentView(R.layout.activity_get_seach_key_for_rwgg_tab);
		yh_mc=(EditText) this.findViewById(R.id.yh_mc);
		gddw=(EditText) this.findViewById(R.id.gddw);
		dbzch=(EditText) this.findViewById(R.id.dbzch);
		zcbh=(EditText) this.findViewById(R.id.zcbh);
		azdz=(EditText) this.findViewById(R.id.azdz);
	}
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
			Intent data=new Intent();  
			
            data.putExtra("yh_mc",yh_mc.getText().toString()); 
            data.putExtra("gddw",gddw.getText().toString()); 
            data.putExtra("dbzch",dbzch.getText().toString()); 
            data.putExtra("zcbh",zcbh.getText().toString()); 
            data.putExtra("azdz",azdz.getText().toString()); 
            setResult(100, data);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		  switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	



}
