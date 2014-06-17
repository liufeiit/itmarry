package com.dyj;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DispMessageTab extends Activity {

	private TextView title,content,dateline;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disp_message_tab);
		this.title=(TextView) this.findViewById(R.id.title);
		this.content=(TextView) this.findViewById(R.id.content);
		this.dateline=(TextView) this.findViewById(R.id.dateline);
		Intent intent =getIntent();
		this.title.setText(intent.getStringExtra("title"));
		this.content.setText(intent.getStringExtra("content"));
		this.dateline.setText("发布时间:"+intent.getStringExtra("dateline"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.disp_message_tab, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_close:
			//global.save(valueName, tv.getText().toString());
			
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
