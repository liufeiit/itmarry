package com.map.bbs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gaoxiaoxing.R;

public class WriteAcctivity extends Activity {

	private Button pull, cancel;
	private EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);

		pull = (Button) findViewById(R.id.pull);
		cancel = (Button) findViewById(R.id.cancel);
		edit = (EditText) findViewById(R.id.edit);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WriteAcctivity.this.finish();
			}
		});
		pull.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if ("".equals(edit.getText().toString().trim())) {
					Toast.makeText(WriteAcctivity.this, "内容不能为空！", 0).show();
				}
			}
		});

	}
}
