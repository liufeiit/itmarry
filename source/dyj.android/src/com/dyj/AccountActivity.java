package com.dyj;

import java.util.List;

import com.baidu.android.pushservice.apiproxy.PushManager;
import com.dyj.app.Global;
import com.dyj.push.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends Activity {

	private Global global;
	private SharedPreferences userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userInfo = this.getSharedPreferences("setting", Context.MODE_PRIVATE);
		global = Global.getInstance(userInfo.getString("dm_user", ""));
		setContentView(R.layout.activity_account);
		TextView user_mc = (TextView) findViewById(R.id.user_mc);
		user_mc.setText(global.read("user_mc"));
		TextView bm_mc = (TextView) findViewById(R.id.bm_mc);
		bm_mc.setText(global.read("bm_mc"));
		Button btn_logout = (Button) findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//×¢ÏúpushµÄtag
				List<String> tags = Utils.getTagsList("gs_"+global.read("gs_dm"));
				PushManager.delTags(getApplication(), tags);
				global.remove("dm_user");
				global.remove("user_mc");
				global.remove("is_dl");
				global.remove("bm_dm");
				global.remove("bm_mc");
				global.remove("gs_dm");
				global.remove("gs_mc");
				global.remove("dm_user");
				userInfo.edit().putBoolean("login", true).commit();
				userInfo.edit().remove("dm_user");
				Intent intent = new Intent(AccountActivity.this,
						MainActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}

}
