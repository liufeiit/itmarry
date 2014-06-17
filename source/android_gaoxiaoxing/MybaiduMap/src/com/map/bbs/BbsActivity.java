package com.map.bbs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gaoxiaoxing.R;

public class BbsActivity extends Activity {

	private Button write;
	private ListView list;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bbs);
		String name = getIntent().getStringExtra("Name");
		title = (TextView) findViewById(R.id.bbs_title);
		title.setText(name);
		write = (Button) findViewById(R.id.write);
		list = (ListView) findViewById(R.id.Msg_list);

		write.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BbsActivity.this, WriteAcctivity.class);
				BbsActivity.this.startActivity(intent);
			}
		});
	}

	public void exit(View v) {
		this.finish();
	}

}
