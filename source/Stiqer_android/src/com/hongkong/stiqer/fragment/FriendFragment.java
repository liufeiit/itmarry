package com.hongkong.stiqer.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.FriendAdapter;
import com.hongkong.stiqer.db.FriendDao;
import com.hongkong.stiqer.entity.Feed;
import com.hongkong.stiqer.entity.Friend;
import com.hongkong.stiqer.entity.FriendStatus;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.GuideActivity;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.FriendComparator;
import com.hongkong.stiqer.utils.GetFirstZimu;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.RefreshableView;
import com.hongkong.stiqer.widget.RefreshableView.PullToRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class FriendFragment extends Fragment {

	public static final String TAG = "Siqer";  
	public static final String CACHE_NAME = "friend";
	public static final int    SUCCESS = 1000;
	public static final int    NO_FRIEND_YET = 1023;
    private View  view;  
	private LinearLayout       titleLayout;
	private Context            mContext;
	private RelativeLayout     sectionToastLayout;
	private TextView           alphabetButton;
	private TextView           title;
	private TextView           sectionToastText;
	private ListView           contactsListView;
	private FriendAdapter      adapter;
	private AlphabetIndexer    indexer;
	private List<Friend>       contacts = new ArrayList<Friend>();
	private String             alphabet =  "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private int                lastFirstVisibleItem = -1;
	private FriendDao          friendDao;
	ProgressBar                progressBar;
	CustomDialog               deleteDialog;
	DialogListener             deleteListener;
	int                        delete_position = 0;
	FriendStatus               friendStatus;
	CacheHelper                cacheHelper;
	TextView                   not_friend_hint;
	boolean is_on_screen       = true;
	RefreshableView            refreshableView;
    public static FriendFragment newInstance(String content) {  
    	FriendFragment fragment = new FriendFragment();  
        Bundle args = new Bundle();  
        args.putString(TAG, content);  
        fragment.setArguments(args);  
        return fragment;  
    }  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
    }  

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
    	if (view != null)
        {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        initView();
        if(cacheHelper.checkCache(CACHE_NAME)){
      	    ShowFromDatabase();
        }else{
        	friendDao.deleteAll();
        	new GetDataTask().execute();
        }
        return view;
    }  
    
    public void initView(){
    	adapter = new FriendAdapter(getActivity(), R.layout.list_item_friend, contacts);
		titleLayout = (LinearLayout) view.findViewById(R.id.title_layout);
		sectionToastLayout = (RelativeLayout) view.findViewById(R.id.section_toast_layout);
		title = (TextView) view.findViewById(R.id.title);
		sectionToastText = (TextView) view.findViewById(R.id.section_toast_text);
		alphabetButton = (TextView) view.findViewById(R.id.alphabetButton);
		alphabetButton.setText("#\nA\nB\nC\nD\nE\nF\nG\nH\nI\nJ\nK\nL\nM\nN\nO\nP\nQ\nR\nS\nT\nU\nV\nW\nX\nY\nZ");
		contactsListView = (ListView) view.findViewById(R.id.contacts_list_view);
		friendDao = new FriendDao(getActivity());
		mContext = getActivity();
		not_friend_hint = (TextView) view.findViewById(R.id.not_friend_hint);
		
		progressBar = (ProgressBar) view.findViewById(R.id.friend_progressbar);
		deleteListener = new DialogListener(){
			public void showDialog(Object o) {
				deleteFriend();
			}
		};
		cacheHelper = new CacheHelper(getActivity());
		
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);  
        refreshableView.setOnRefreshListener(new PullToRefreshListener() {  
            @Override  
            public void onRefresh() {  
            	friendDao.deleteAll();
            	new GetDataTask().execute();
            	
                try {  
                    Thread.sleep(2500);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
                refreshableView.finishRefreshing();  
            }  
        }, 1);
    }
    
    public void deleteFriend(){
    	new Thread(){
    		public void run(){
    			friendStatus = APIManager.sharedInstance().deleteFriend(contacts.get(delete_position).getUsername());
    			Message msg = mHandler.obtainMessage();
 			    msg.what = friendStatus.getError_code();
 			    mHandler.sendMessage(msg);
    		}
    	}.start();
    }
    
	private void ShowFromDatabase() {	
		contacts.clear();
		Cursor cursor= friendDao.queryTheCursor();
		GetFirstZimu cte = new GetFirstZimu();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(1);
				String sortKey = cte.getAllFirstLetter(cursor.getString(2));
				Friend contact = new Friend();
				contact.setUsername(name);
				contact.setUser_index(sortKey);
				
				contact.setProfile_img(cursor.getString(3));
				contact.setUser_level(cursor.getInt(4));
				contacts.add(contact);
			} while (cursor.moveToNext());
		}
		Collections.sort(contacts, new FriendComparator());
		try{
			getActivity().startManagingCursor(cursor);
			indexer = new AlphabetIndexer(cursor, 2, alphabet);
			adapter.setIndexer(indexer);
			if (contacts.size() > 0) {
				setupContactsListView();
				setAlpabetListener();
			}
		}catch(Exception e){
		
		}
	}
	
	private class GetDataTask extends AsyncTask<Integer[],Void,List<Friend>>{
		@Override
		protected void onPostExecute(List<Friend> result) {
			//插入数据
			ErrorCodeHelper errorHelper = new ErrorCodeHelper(mContext);
			if(result!=null){
				if(result.size()>0){
					if(errorHelper.CommonCode(result.get(0).getError_code())){
					   try{
						  Log.e("Stiqer","result:"+result.size());
						  friendDao.add(result);
						  cacheHelper.setCache(CACHE_NAME);
					   }catch(Exception e){
					   	
					   }
					}
				}else{
					//if no friends
					not_friend_hint.setVisibility(View.VISIBLE);
				}
				
			  }else{
				errorHelper.connectError();
			  }
			  ShowFromDatabase();
			  super.onPostExecute(result);
		}
		@Override
		protected List<Friend> doInBackground(Integer[]... params) {
			return APIManager.sharedInstance().getFriendList();
		}
	}

	private void setupContactsListView() {
		contactsListView.setAdapter(adapter);
		contactsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				int section = indexer.getSectionForPosition(firstVisibleItem);
				int nextSecPosition = indexer.getPositionForSection(section + 1);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(String.valueOf(alphabet.charAt(section)));
				}
				if (nextSecPosition == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		});
		
		contactsListView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				Intent t = new Intent(getActivity(),UserActivity.class);
				t.putExtra("username", contacts.get(position).getUsername());
				getActivity().startActivity(t);
			}
		});
		
		contactsListView.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				//deleteDialog = CustomDialog.createFriendDeleteDialog(mContext,deleteListener);
				//deleteDialog.show();
				//delete_position = position;
				return false;
			}
			
		});
	}

	/**
	 * 设置字母表上的触摸事件，根据当前触摸的位置结合字母表的高度，计算出当前触摸在哪个字母上。
	 * 当手指按在字母表上时，展示弹出式分组。手指离开字母表时，将弹出式分组隐藏。
	 */

	private void setAlpabetListener() {
		
		alphabetButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float alphabetHeight = alphabetButton.getHeight();
				float y = event.getY();
				int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));
				if (sectionPosition < 0) {
					sectionPosition = 0;
				} else if (sectionPosition > 26) {
					sectionPosition = 26;
				}
				String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
				int position = indexer.getPositionForSection(sectionPosition);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					alphabetButton.setBackgroundColor(0xffc0c0c0);
					sectionToastLayout.setVisibility(View.VISIBLE);
					sectionToastText.setText(sectionLetter);
					contactsListView.setSelection(position);
					break;
				case MotionEvent.ACTION_MOVE:
					sectionToastText.setText(sectionLetter);
					contactsListView.setSelection(position);
					break;
				default:
					alphabetButton.setBackgroundColor(0xffffffff);
					sectionToastLayout.setVisibility(View.GONE);
				}
				return true;
			}
		});
	}


	private Handler mHandler = new Handler()
    {
		 
        public void handleMessage(Message msg)
        {
       	 super.handleMessage(msg);
            switch (msg.what)
            {
                case SUCCESS:
           	      Toast.makeText(mContext, "remove success", Toast.LENGTH_SHORT).show();
           	      contacts.remove(delete_position);
           	      adapter.notifyDataSetChanged();
           	      //清缓存
           	      cacheHelper.clearCache(CACHE_NAME);
           	      break;
           	      
                case NO_FRIEND_YET:
               	  Toast.makeText(mContext, "No friend yet", Toast.LENGTH_SHORT).show();
             	   break;
            }
        }
    };
}
