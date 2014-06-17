package com.hongkong.stiqer.ui;

import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.AppContext;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.NotiListAdapter;
import com.hongkong.stiqer.entity.Noti;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.MessageListener;
import com.hongkong.stiqer.widget.RefreshableView;
import com.hongkong.stiqer.widget.RefreshableView.PullToRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.app.Activity;
import android.content.SharedPreferences;

public class MessageActivity extends Activity {
	public static final String TAG = "Siqer";  
    private View               footer;
    private ListView           noti_listview;
    List<Noti>                 nl,more_nl,new_nl;
    NotiListAdapter            adapter;
    private final int          page_number = 10;
    ProgressBar                noti_progressbar;
    int                        noti_new;
    CacheHelper                cacheHelper;
    SharedPreferences          notiPreference;
    ErrorCodeHelper            errorHelper;
    Button                     btnBack;
    MessageListener            mListener;
    RefreshableView            refreshableView;
    private boolean            loadfinish = true;
    private AppContext         application; 
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initView();
        if(noti_new >0){
        	getNewList();
        }else{
        	if(Util.notiList == null){
            	getNotiList();
            }else{
            	nl.addAll(Util.notiList);
            	adapter.notifyDataSetChanged();
            }
        }
	}
	
	 private void initView() {  
		    application = (AppContext)getApplication();  
	        application.init();  
	        application.addActivity(this);  
	        
	    	notiPreference = getSharedPreferences(Util.TAG, 0);
	    	noti_new = notiPreference.getInt("noti_num", 0);
	    	footer = getLayoutInflater().inflate(R.layout.footer_loading, null);
	    	noti_progressbar = (ProgressBar) findViewById(R.id.noti_progressbar);
	    	nl = new ArrayList<Noti>();
	    	cacheHelper = new CacheHelper(this);
	    	errorHelper = new ErrorCodeHelper(this);
	    	btnBack = (Button) findViewById(R.id.m_back_btn);
	    	btnBack.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					MessageActivity.this.finish();
				}
	    	});
	    	
	    	mListener = new MessageListener(){
				public void deleteNode(int position) {
					nl.remove(position);
					Util.notiList = nl;
					adapter.notifyDataSetChanged();
				}
	    	};
	    	
	    	adapter = new NotiListAdapter(this,nl,mListener);
	    	
	    	noti_listview = (ListView) findViewById(R.id.noti_listview);
	    	noti_listview.setOnScrollListener(new ScrollLisenter());
	    	noti_listview.addFooterView(footer);
	    	noti_listview.setAdapter(adapter);
	    	noti_listview.removeFooterView(footer);
	    	
	    	refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);  
	        refreshableView.setOnRefreshListener(new PullToRefreshListener() {  
	            @Override  
	            public void onRefresh() {  
	                getNewList();
	                try {  
	                    Thread.sleep(2500);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }
	                refreshableView.finishRefreshing();  
	            }  
	        }, 1);
	    }
	 
	 public void getNotiList(){
	    	new Thread(){
	    		public void run(){
	    			
	    			more_nl = APIManager.sharedInstance().getNotiList(nl.size(),page_number);
	            	Message msg = mHandler.obtainMessage();
	                msg.what = 1500;
	                mHandler.sendMessage(msg);
	    		}
	    	}.start();    		
	 }
	 
	 public void getNewList(){
	    	new Thread(){
	    		public void run(){
	    	       new_nl = APIManager.sharedInstance().getNewNoti();
	    	       Message msg = mHandler.obtainMessage();
	               msg.what = 1501;
	               mHandler.sendMessage(msg);
	    		}
	    	}.start();
	 }
	 
	 
	    private class ScrollLisenter implements OnScrollListener{
			@Override
			public void onScroll(AbsListView views, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItemid = views.getLastVisiblePosition();
				if((lastItemid+1) == totalItemCount){
					int currentpage = totalItemCount%page_number == 0 ? totalItemCount/page_number : totalItemCount/page_number+1;
					if(totalItemCount > 0 && (page_number*currentpage == totalItemCount)){
						if(loadfinish){
							loadfinish = false;
							noti_listview.addFooterView(footer);
							getNotiList();
						}
					}
				}
			}
			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
	           
			}
	    }
	    
	    private Handler mHandler = new Handler()
	    {
	        public void handleMessage(Message msg)
	        {
	       	 super.handleMessage(msg);
	            switch (msg.what)
	            {
	            case 1500:
	            	if(more_nl!=null){
	    				if(more_nl.size()>0){
	    					if(errorHelper.CommonCode(more_nl.get(0).getError_code())){
	    						nl.addAll(more_nl);
	    						adapter.notifyDataSetChanged();
	    						Util.notiList = nl;
	    						if(more_nl.size() == page_number){
	    			            	 loadfinish = true; 
	    			            }
	    					}
	    		        }else{
	    		        	errorHelper.loadOver();
	    		        }
	    			 }else{
	    				errorHelper.connectError();
	    			 }
	            	if(noti_listview.getFooterViewsCount() > 0) noti_listview.removeFooterView(footer);
	            	break;
	            	
	            case 1501:
	            	if(new_nl!=null){
	    				if(new_nl.size()>0){
	    					if(errorHelper.CommonCode(new_nl.get(0).getError_code())){
	    						List<Noti> trans_nl = new ArrayList<Noti>();
	    						
	    						if(Util.notiList != null){
	    							trans_nl.addAll(nl);
	        						nl.clear();
	        						nl.addAll(new_nl);
	        						nl.addAll(trans_nl);
	        						Util.notiList = nl;
	        						adapter.notifyDataSetChanged();
	    						}else{
	    							if(new_nl.size() < page_number){
	    								getNewList();
	    							}
	    						}
	    						
	    					}
	    		        }else{
	    		        	errorHelper.loadOver();
	    		        }
	    			 }else{
	    				errorHelper.connectError();
	    			 }
	            	break;
	            }
	        }
	    };      
}
