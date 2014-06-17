package com.hongkong.stiqer.tab;

import java.util.List;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.ProfileActivityAdapter;
import com.hongkong.stiqer.entity.Feed;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;

public class ActsActivity extends BaseActivity {
	
	View                       footer;
	ListView                   actListview;
	List<Pactivity>            actList;
	ProfileActivityAdapter     actAdapter;
	private final int          page_number = 16;
	private boolean            loadfinish = true;
	private int                now_page = 1;
	String                     to_uid;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity_acts);
		initData();
		initView();
		actListview.setAdapter(actAdapter);
	}
	
	private void initData() {
		Intent t = getIntent();
		to_uid = t.getStringExtra("to_uid");
		actList = (List<Pactivity>) Util.readJson2EntityList(t.getStringExtra("activity"), new Pactivity());
	}

	private void initView() {
		actListview = (ListView) findViewById(R.id.act_listview);
		actAdapter = new ProfileActivityAdapter(this,actList,R.layout.list_item_tab_activity);
		footer = getLayoutInflater().inflate(R.layout.footer_loading, null);
		actListview.setOnScrollListener(new ScrollLisenter());
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
	
	 private class GetDataTask extends AsyncTask<Integer[],Void,List<Pactivity>>{
	    @Override
		protected void onPostExecute(List<Pactivity> result) {
	    	ErrorCodeHelper errorHelper = new ErrorCodeHelper(ActsActivity.this);
			if(result!=null){
				if(result.size()>0){
					if(errorHelper.CommonCode(result.get(0).getError_code())){
						actList.addAll(result);
						actAdapter.notifyDataSetChanged();
						if(result.size() == page_number){
			            	 loadfinish = true; 
			            	 now_page++;
			             }
					}
		        }else{
		        	errorHelper.loadOver();
		        }
			 }else{
				errorHelper.connectError();
			 }
			
			 super.onPostExecute(result);
		}
		@Override
		protected List<Pactivity> doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().getActivity(to_uid, now_page*page_number, page_number);
		}
		 
	 }

	@Override
	protected void onPause() {
		super.onPause();
	}



	@Override
	protected void onResume() {
		super.onResume();
	}
}
