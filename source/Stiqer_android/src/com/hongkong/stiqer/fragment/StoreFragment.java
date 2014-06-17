package com.hongkong.stiqer.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.StoreAdapter;
import com.hongkong.stiqer.entity.Storelist;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.tab.PhotoCheckActivity;
import com.hongkong.stiqer.ui.GuideActivity;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.MyLocation;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.ExpandableHeightGridView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StoreFragment extends Fragment {
	public static final String TAG = "Siqer";  
	private static final int   LOAD_OK = 102;
    private   View             view; 
    Context                    mContext;
    ExpandableHeightGridView   store_gridview;
    List<Storelist>            sl,new_sl;
    TextView                   des_first,des_second;
    ProgressBar                progressbar;
    private final int          page_number = 8;
    double                     longitude = 0.0, latitude = 0.0;
    private boolean            loadfinish = false;
    StoreAdapter               adapter;
    LinearLayout               con_wrap,des_first_wrap,des_second_wrap;
    Location                   mobileLocation;
    MyLocation                 myLocation;
    ErrorCodeHelper            errorHelper;
    boolean is_on_screen       = false;

    public static StoreFragment newInstance(String content) {  
    	StoreFragment fragment = new StoreFragment();  
        Bundle args = new Bundle();  
        args.putString(TAG, content);  
        fragment.setArguments(args);  
        return fragment;  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        Bundle bundle = getArguments();
    }  
    
    public void onStart(){
    	super.onStart();
    	is_on_screen = true;
    }
    
    public void onPause(){
    	super.onPause();
    	is_on_screen = false;
    }
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (view != null)
        {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_store, container, false);  
        initView();
        return view;  
    }
  
	private void initView() {
    	store_gridview = (ExpandableHeightGridView) view.findViewById(R.id.store_gridview);
    	progressbar = (ProgressBar) view.findViewById(R.id.progressbar);

    	con_wrap = (LinearLayout) view.findViewById(R.id.con_wrap);
    	des_first_wrap = (LinearLayout) view.findViewById(R.id.des_first_wrap);
    	des_second_wrap = (LinearLayout) view.findViewById(R.id.des_second_wrap);
    	des_first = (TextView) view.findViewById(R.id.des_first);
    	des_second = (TextView) view.findViewById(R.id.des_second);
    	mContext = getActivity();
    	errorHelper = new ErrorCodeHelper(mContext);
    	myLocation = new MyLocation(mContext);
    	longitude = myLocation.getLongitude();
    	latitude = myLocation.getLatitude();
    	
    	sl = new ArrayList<Storelist>();
    	adapter = new StoreAdapter(getActivity(),sl);
    	store_gridview.setAdapter(adapter);
    	store_gridview.setExpanded(true);
    	 
    	new Thread(){
	    	 public void run(){
	    		new_sl = APIManager.sharedInstance().getStoreList(longitude,latitude,0,page_number);
	    		Message msg = mHandler.obtainMessage();
	    		msg.what = LOAD_OK;
	            mHandler.sendMessage(msg);
	    	 }
	    }.start();
	    store_gridview.setOnScrollListener(new ScrollLisenter());
	    store_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position ,long arg3) {
				openStoreDetail(sl.get(position).getStore_id(), sl.get(position).getStore_name());
			}
		}); 
    }   
    
    private void openStoreDetail(String sid, String store_name){
    	Intent t  = new Intent(getActivity(),StoreDetailActivity.class);
    	t.putExtra("store_id", sid);
    	t.putExtra("store_name", store_name);
	    getActivity().startActivity(t);
    }

    
    private class ScrollLisenter implements OnScrollListener{
		@Override
		public void onScroll(AbsListView views, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			int lastItemid = views.getLastVisiblePosition();
			if((lastItemid+1) == totalItemCount){

			if(loadfinish){
			   loadfinish = false;
			   new Thread(){
			    	 public void run(){
			    		new_sl = APIManager.sharedInstance().getStoreList(longitude,latitude,sl.size(),page_number);
			    	    Message msg = mHandler.obtainMessage();
			    		msg.what = LOAD_OK;
			            mHandler.sendMessage(msg);
			    	 }
			      }.start();
					
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

                case LOAD_OK:
                	try{
                	ErrorCodeHelper errorHelper = new ErrorCodeHelper(getActivity());
        			if(new_sl!=null){
        				if(new_sl.size()>0){
        					if(errorHelper.CommonCode(new_sl.get(0).getError_code())){
        						sl.addAll(new_sl);
        						adapter.notifyDataSetChanged();
        						if(new_sl.size() == page_number){
        			            	 loadfinish = true; 
        			             }
        					}
        		        }else{
        		        	errorHelper.loadOver();
        		        }
        			 }else{
        				errorHelper.connectError();
        			 }
        			progressbar.setVisibility(View.GONE);
        			
                	}catch(Exception e){
                		
                	}
                break;
            }
        }

    };
   
}
