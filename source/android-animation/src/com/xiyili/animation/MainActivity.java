package com.xiyili.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {
	public static final String ACTION_DEMO_ENTRY = "xiyili.intent.action.DEMO_ENTRY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fillDemoEntryList();
	}

	private void fillDemoEntryList() {
		Intent intent = new Intent(ACTION_DEMO_ENTRY);
//		intent.setPackage(getPackageName());
		PackageManager pm = getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
		List<Map<String,Object>> entries = new ArrayList<Map<String,Object>>();
		
		for(ResolveInfo each:infos){
			String label = each.activityInfo.name.replaceAll("Activity", "");
			Map<String,Object> entry = new HashMap<String, Object>();
			entry.put("title", label);
			entry.put("intent", getIntent(each));
			
			entries.add(entry);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(
				this, 
				entries,
				android.R.layout.simple_list_item_1, 
				new String[]{"title"},
				new int[]{android.R.id.text1});
		
		setListAdapter(adapter);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String,Object> map = (Map<String,Object>)l.getItemAtPosition(position);
		Intent intent = (Intent)map.get("intent");
		startActivity(intent);
	}

	private Object getIntent(ResolveInfo each) {
		Intent intent = new Intent();
		intent.setClassName(this, each.activityInfo.name);
		return intent;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
