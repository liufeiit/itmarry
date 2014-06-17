package com.hongkong.stiqer.tab;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.AbsListView.OnScrollListener;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.CheckinListAdapter;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.tab.ActsActivity.ScrollLisenter;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.Util;

public class StorePhotoActivity extends BaseActivity {
	
	private static final int   CHECKIN_TYPE_STORE = 2;
	List<CheckIn>           checkinList;
	GridView                checkinGridView;
	CheckinListAdapter      checkinAdapter;
	private final int       page_number = 12;
	private int             now_page = 0;
	private boolean         loadfinish = false;
	String                  store_id;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity_photocheck);
		initData();
		initView();
		checkinGridView.setAdapter(checkinAdapter);
	}

	private void initView() {
		checkinGridView = (GridView)findViewById(R.id.checkin_gridview);
		checkinAdapter = new CheckinListAdapter(this,checkinList,2);
		checkinGridView.setOnScrollListener(new ScrollLisenter());
	}
	private void initData() {
		Intent t = getIntent();
		store_id = t.getStringExtra("store_id");
		checkinList = (List<CheckIn>) Util.readJson2EntityList(t.getStringExtra("checkin"), new CheckIn());
		if(checkinList.size()==page_number){
			loadfinish=true;
		}
	}
	
	class ScrollLisenter implements OnScrollListener{
		public void onScroll(AbsListView views, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			int lastItemid = views.getLastVisiblePosition();
			if((lastItemid+1) == totalItemCount){
				int currentpage = totalItemCount%page_number == 0 ? totalItemCount/page_number : totalItemCount/page_number+1;
				if(totalItemCount > 0 && (page_number*currentpage == totalItemCount)){
					if(loadfinish){
					   loadfinish = false;
					   new GetDataTask().execute();
					}
				}	
			}
		}
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
		}
	}
	
	private class GetDataTask extends AsyncTask<Integer[],Void,List<CheckIn>>{
	    @Override
		protected void onPostExecute(List<CheckIn> result) {
	    	if(result.size()>0){
	    		checkinList.addAll(result);
		    	checkinAdapter.notifyDataSetChanged();
	    	}
	    	if(result.size() == page_number){
	    		loadfinish = true;
	    	}
			now_page++;
			super.onPostExecute(result);
		}
		@Override
		protected List<CheckIn> doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().getStoreCheckIn(CHECKIN_TYPE_STORE,store_id,now_page*page_number,page_number);
		}
		 
	 }
}
