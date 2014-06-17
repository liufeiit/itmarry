package com.sumilux.apps.jabze.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ��ȡ��ϵ�ˡ�ͨ����¼������
 * 
 * @author wwj
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	private Button btnLoadContacts;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLoadContacts = (Button) findViewById(R.id.btn_load_contacts);
		btnLoadContacts.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_load_contacts: // ��ȡ��ϵ��
			intent = new Intent(this, ContactListActivity.class);
			break;
		}
		startActivity(intent);
	}

}
