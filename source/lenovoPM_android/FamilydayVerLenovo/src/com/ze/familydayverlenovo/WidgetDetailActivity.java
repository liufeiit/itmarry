package com.ze.familydayverlenovo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class WidgetDetailActivity extends Activity {

	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_detail);
		tv = (TextView) findViewById(R.id.wd_tv);
		tv.setText(WidgetDataMrg.widgetPic.id + "和" + WidgetDataMrg.widgetPic.uid);
	}

	

}
