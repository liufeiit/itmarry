package com.hongkong.stiqer.ui;

import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.SearchFriendAdapter;
import com.hongkong.stiqer.entity.SearchFriend;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.widget.AddFriendListener;
import com.hongkong.stiqer.widget.CustomDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class SearchFriendActivity extends Activity {  
	private static final int        DATA_READY = 888;
    Button  btnBack;
    ImageView search_btn;
    EditText  sf_text;
    ErrorCodeHelper errorHelper;
    CustomDialog  progressDialog;
    List<SearchFriend> ls;
    ListView sListView;
    Context mContext;
    AddFriendListener adapterListener;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchfriend); 
		initView();
	}
	
	private void initView(){
		
	    errorHelper = new ErrorCodeHelper(this);
	    mContext = this;
	    sListView = (ListView) findViewById(R.id.searchfriend_listview);
	    progressDialog = CustomDialog.createProgressDialog(this, "search....");
		sf_text = (EditText) findViewById(R.id.sf_text);
		btnBack = (Button) findViewById(R.id.sf_back_btn);
		btnBack.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				SearchFriendActivity.this.finish();
			}
		});
		
		search_btn =(ImageView) findViewById(R.id.sf_search_btn);
		search_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				progressDialog.show();
				if(sf_text.getText().toString().equals("")){
					Toast.makeText(mContext, "Username cannot be empty", Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread(){
					public void run(){
						ls = APIManager.sharedInstance().getSearchFriend(sf_text.getText().toString());
						Message msg = mHandler.obtainMessage();
						msg.what = DATA_READY;
						mHandler.sendMessage(msg);
					}
				}.start();
			}
		});
		
		adapterListener = new AddFriendListener(){
			public void sendRequest(int position) {
			  int wantedPosition = position;
		      int firstPosition = sListView.getFirstVisiblePosition() - sListView.getHeaderViewsCount(); 
		      int wantedChild = wantedPosition - firstPosition;
		      LinearLayout listview_layout = (LinearLayout) sListView.getChildAt(wantedChild);// 获得Item布局，i就是item的位置
			  Button add_btn= (Button) listview_layout.findViewById(R.id.sf_add_btn);  
			  Button added_btn= (Button) listview_layout.findViewById(R.id.sf_added_btn);  
			  add_btn.setVisibility(View.GONE);
			  added_btn.setVisibility(View.VISIBLE);
			}

			@Override
			public void sendInvite(int position, int type, String rawfid,
					String name) {
				// TODO Auto-generated method stub
			}
		};
		
		sf_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {if (actionId == EditorInfo.IME_ACTION_SEND
                         || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            	     sf_text.requestFocus();
                     return true;
                 }
                 return false;
             }
         });
			
	}
	
	private Handler mHandler = new Handler()
    {
		 
        public void handleMessage(Message msg)
        {
       	 if(progressDialog.isShowing()){
       		 progressDialog.cancel(); 
       	 }
       	 super.handleMessage(msg);
            switch (msg.what)
            {
            case DATA_READY:
            	if(ls!=null){
    				if(ls.size()>0){
    					if(errorHelper.CommonCode(ls.get(0).getError_code())){
    						sListView.setAdapter(new SearchFriendAdapter(mContext,ls,adapterListener));
    						sListView.setVisibility(View.VISIBLE);
    					}
    		        }else{
    		        	ls.clear();
    		        	sListView.setAdapter(new SearchFriendAdapter(mContext,ls,adapterListener));
    		        	Toast.makeText(mContext, "No user found..",Toast.LENGTH_SHORT).show();
    		        }
    			 }else{
    				errorHelper.connectError();
    			 }
            	break;
            }
        }
    };
	
}
