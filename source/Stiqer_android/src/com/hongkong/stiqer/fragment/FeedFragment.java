package com.hongkong.stiqer.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.FeedListAdapter;
import com.hongkong.stiqer.entity.Feed;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.CommentActivity;
import com.hongkong.stiqer.ui.GuideActivity;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.AdapterListener;
import com.hongkong.stiqer.widget.RefreshableView;
import com.hongkong.stiqer.widget.RefreshableView.PullToRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FeedFragment extends Fragment{
	
	public static final String TAG = "Siqer";
	public static final int    REFRESH_OK = 1030;
	public static final int    COMMENT_NUM_REFRESH = 113;
    private View            view,footer;
    ListView                feed_listview;
    LinearLayout            listview_layout;
    List<Feed>              fl,load_fl;
    TextView                like_num;
    TextView                comment_num;
    private AdapterListener adapterListener;
    Context                 context;
    ProgressBar             progressbar;
    FeedListAdapter         adapter;
    private final int       page_number = 5;
    private int             total_num = 0;
    private boolean         loadfinish = false;
    ErrorCodeHelper         errorHelper;
    RefreshableView         refreshableView;
  
    
    public static FeedFragment newInstance(String content) {
    	FeedFragment fragment = new FeedFragment();  
        Bundle args = new Bundle();  
        args.putString(TAG, content);  
        fragment.setArguments(args);  
        return fragment;  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        context = getActivity(); 
    }
  
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		if(!Util.isAvatarChange){
		  if (view != null)
          {
             ((ViewGroup) view.getParent()).removeView(view);
             return view;
           }
		}else{
			total_num = 0;
			loadfinish = false;
		}
		view = inflater.inflate(R.layout.fragment_feed, container, false); 
		InitView();
        new GetDataTask().execute();   
        return view;  
    }  
    
    private void InitView() {
    	progressbar = (ProgressBar) view.findViewById(R.id.fd_progressbar);
        feed_listview = (ListView) view.findViewById(R.id.feed_listview);
        footer = getActivity().getLayoutInflater().inflate(R.layout.footer_loading, null);
        errorHelper = new ErrorCodeHelper(getActivity());
        adapterListener = new AdapterListener()
        {
    		public void sendData(int position, int num, int type) {
    		  
    		   if(type == 1)
    		   {
    			   int wantedPosition = position;
    			   int firstPosition = feed_listview.getFirstVisiblePosition() - feed_listview.getHeaderViewsCount(); 
    			   int wantedChild = wantedPosition - firstPosition;
   
    			   listview_layout = (LinearLayout) feed_listview.getChildAt(wantedChild); // 获得Item布局，i就是item的位置
    			   TextView like_num= (TextView) listview_layout.findViewById(R.id.feed_like_num);
    			   like_num.setText(""+num);
    		   }
    		}

			public void OpenComment(String fid, int position, int comment_num) {
				Intent t = new Intent(getActivity(),CommentActivity.class);
				t.putExtra("type", 2);
				t.putExtra("tid", fid);
				t.putExtra("position", position);
				t.putExtra("comment_num", comment_num);
				getActivity().startActivityForResult(t,COMMENT_NUM_REFRESH);
			}
        };
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);  
        refreshableView.setOnRefreshListener(new PullToRefreshListener() {  
            @Override  
            public void onRefresh() {  
                pullRefresh();
                try {  
                    Thread.sleep(2500);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
                refreshableView.finishRefreshing();  
            }  
        }, 1);
        
        fl = new ArrayList<Feed>();
        adapter = new FeedListAdapter(getActivity(),fl,adapterListener);
        feed_listview.setOnScrollListener(new ScrollLisenter());
        feed_listview.addFooterView(footer);
    	feed_listview.setAdapter(adapter);
    	feed_listview.removeFooterView(footer);
	}
    
    public void pullRefresh(){
    	new Thread(){
    		public void run(){
    			load_fl = APIManager.sharedInstance().getFeedRefresh(fl.get(0).getFeed_id());
    			if(load_fl == null){
    				//errorHelper.connectError();
    			}else{
    				Message msg = mHandler.obtainMessage();
		            msg.what =  REFRESH_OK;
		            mHandler.sendMessage(msg);
    			}
    		}
    	}.start();
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {

       	 super.handleMessage(msg);
            switch (msg.what)
            {
                case REFRESH_OK:
                	if(load_fl.size()>0){
    					if(errorHelper.CommonCode(load_fl.get(0).getError_code())){
    						List<Feed> trans_feed = new ArrayList<Feed>();
    						trans_feed.addAll(fl);
    						fl.clear();
    						fl.addAll(load_fl);
    						fl.addAll(trans_feed);
    						total_num = total_num + load_fl.size();
    						adapter.notifyDataSetChanged();
    					}
    				}
                	break;
            }
        };
    };

	private class ScrollLisenter implements OnScrollListener{

		@Override
		public void onScroll(AbsListView views, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			int lastItemid = views.getLastVisiblePosition();
			if((lastItemid+1) == totalItemCount){
				if(loadfinish){
				   loadfinish = false;
				   feed_listview.addFooterView(footer);
				   new GetDataTask().execute();
				}
			}
		}
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {

		}
    }
    
    private class GetDataTask extends AsyncTask<Integer[],Void,List<Feed>>{
		@Override
		protected void onPostExecute(List<Feed> result) {
			progressbar.setVisibility(View.GONE);
			if(Util.isAvatarChange){
				Util.isAvatarChange = false;
			}
			
			if(result != null){
				if(result.size()>0){
					if(errorHelper.CommonCode(result.get(0).getError_code())){
						fl.addAll(result);
						adapter.notifyDataSetChanged();
						total_num = total_num + result.size();
						if(result.size() == page_number){
							loadfinish = true;
						}
					}
				}
				if(feed_listview.getFooterViewsCount() > 0) feed_listview.removeFooterView(footer);
			}
			super.onPostExecute(result);
		}

		@Override
		protected List<Feed> doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().getFeedList(total_num,page_number);
		}
    	
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	   case COMMENT_NUM_REFRESH:
			   fl.get(data.getExtras().getInt("position")).setComment_num(data.getExtras().getInt("comment_num"));
			   adapter.notifyDataSetChanged();
    		   break;
    	}
   }
  
}
