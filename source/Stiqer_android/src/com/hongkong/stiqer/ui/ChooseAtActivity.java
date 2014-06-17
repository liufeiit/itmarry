package com.hongkong.stiqer.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.db.FriendDao;
import com.hongkong.stiqer.entity.Friend;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class ChooseAtActivity extends Activity {
	private static final int      CHOOSE_AT_PERSON = 105;
	public static final  String   TAG = "Siqer";  
	public static final String CACHE_NAME = "friend";
    private View view;  
	private LinearLayout titleLayout;
	private RelativeLayout sectionToastLayout;
	private Button btn_queding,btnBack;
	private TextView title,alphabetButton,sectionToastText;
	private ListView contactsListView;
	private AtFriendAdapter adapter;
	private AlphabetIndexer indexer;
	private List<Friend> contacts = new ArrayList<Friend>(),atList = new ArrayList<Friend>();
	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private int lastFirstVisibleItem = -1;
	private FriendDao       friendDao;
	ProgressBar             progressBar;
	CacheHelper             cacheHelper;
    HashMap<Integer, Boolean>   isSelected;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseat);
		initData();
		initView();
		if(cacheHelper.checkCache(CACHE_NAME)){
        	ShowFromDatabase();
        }else{
        	friendDao.deleteAll();
        	progressBar.setVisibility(View.VISIBLE);
        	new GetDataTask().execute();
	    }
	}
	
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "32YTPN6M7T4TW3DH38P5");
	}
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
    
	private void initData(){
		Intent t = getIntent();
		atList = (List<Friend>) t.getSerializableExtra("atList");
		isSelected = new HashMap<Integer, Boolean>();
		for(int i =0; i<atList.size();i++){
			Log.e("Stiqer","username = "+atList.get(i).getUsername());
		}
	}
	
	private void initView() {
		adapter = new AtFriendAdapter(this, R.layout.list_item_at_friend, contacts);
		titleLayout = (LinearLayout) findViewById(R.id.at_title_layout);
		sectionToastLayout = (RelativeLayout) findViewById(R.id.at_section_toast_layout);
		title = (TextView) findViewById(R.id.at_title);
		sectionToastText = (TextView) findViewById(R.id.at_section_toast_text);
		alphabetButton = (TextView) findViewById(R.id.at_alphabetButton);
		alphabetButton.setText("#\nA\nB\nC\nD\nE\nF\nG\nH\nI\nJ\nK\nL\nM\nN\nO\nP\nQ\nR\nS\nT\nU\nV\nW\nX\nY\nZ");
		contactsListView = (ListView) findViewById(R.id.at_contacts_list_view);
		friendDao = new FriendDao(this);
		progressBar = (ProgressBar) findViewById(R.id.at_friend_progressbar);
		//contactsListView.setOnItemClickListener(new MyOnItemListener());
		btn_queding = (Button) findViewById(R.id.btn_queding);
		btnBack = (Button) findViewById(R.id.sd_back_btn);
		btnBack.setOnClickListener(new MyOkClickListener());
		btn_queding.setOnClickListener(new MyOkClickListener());
		cacheHelper = new CacheHelper(this);
		
		
	}
	
	private void ShowFromDatabase() {		
		Cursor cursor= friendDao.queryTheCursor();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(1);
				String sortKey = getSortKey(cursor.getString(2));
				Friend contact = new Friend();
				contact.setUsername(name);
				contact.setUser_index(sortKey);
				contact.setProfile_img(cursor.getString(3));
				contact.setUser_level(cursor.getInt(4));
				contacts.add(contact);
			} while (cursor.moveToNext());
		}	
		
		for(int m =0; m<contacts.size(); m++){
			isSelected.put(m, false);
		}
		
		if(atList.size()>0){
			for(int i = 0; i<atList.size(); i++){
				for(int j =0; j<contacts.size(); j++){
					if(atList.get(i).getUsername().equals(contacts.get(j).getUsername())){
						isSelected.put(j, true);
					}
				}
			}
		}
		startManagingCursor(cursor);
		indexer = new AlphabetIndexer(cursor, 2, alphabet);
		adapter.setIndexer(indexer);
		if (contacts.size() > 0) {
			setupContactsListView();
			setAlpabetListener();
		}
	}
	
	class MyOkClickListener implements OnClickListener{
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.sd_back_btn:
				finish();
				break;
				
			case R.id.btn_queding:
				Intent intent = new Intent();
				intent.putExtra("atList", (Serializable)atList);
				setResult(RESULT_OK,intent);
				finish();
			break;
			}
			
		}
	}
	
	 private class GetDataTask extends AsyncTask<Integer[],Void,List<Friend>>{
		@Override
		protected void onPostExecute(List<Friend> result) {
			//插入数据
			progressBar.setVisibility(View.GONE);
			ErrorCodeHelper errorHelper = new ErrorCodeHelper(ChooseAtActivity.this);
			if(result!=null){
				if(result.size()>0){
					if(errorHelper.CommonCode(result.get(0).getError_code())){
						friendDao.add(result);
						cacheHelper.setCache(CACHE_NAME);
					}
				}else{
					//if no friends
					Toast.makeText(ChooseAtActivity.this, "no friend now~~", Toast.LENGTH_SHORT).show();
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
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
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
	
	private String getSortKey(String sortKeyString) {
		alphabetButton.getHeight();
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}
	
	public class AtFriendAdapter extends ArrayAdapter<Friend> {

		private int resource;
		private SectionIndexer mIndexer;
		public AtFriendAdapter(Context context, int textViewResourceId, List<Friend> objects) {
			super(context, textViewResourceId, objects);
			resource = textViewResourceId;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Friend contact = getItem(position);
			LinearLayout layout = null;
			if (convertView == null) {
				layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
			} else {
				layout = (LinearLayout) convertView;
			}
			TextView name = (TextView) layout.findViewById(R.id.at_friend_name);
			ImageView avatar = (ImageView) layout.findViewById(R.id.at_friend_avatar);
			CheckBox checkBox = (CheckBox) layout.findViewById(R.id.at_checkbox);
			
			name.setText(contact.getUsername());
			Picasso.with(getContext()).load(contact.getProfile_img()).into(avatar);

			checkBox.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					if(!checkBoxUsername(contacts.get(position).getUsername())){
						//增加
						isSelected.put(position, true);
						atList.add(contacts.get(position));
					 }else{
						//减少
						 isSelected.put(position, false);
						for(int i=0,len= atList.size();  i<len; i++){   
						       Friend friend = atList.get(i);
						  	   if(friend.getUsername().equals(contacts.get(position).getUsername())){
						  		   atList.remove(i);
						  		   --len;
								   --i;
						  		   break;
						  	   }
						 }
					}
				}
			});
			
			checkBox.setChecked(isSelected.get(position));
			return layout;
		}

		public void setIndexer(SectionIndexer indexer) {
			mIndexer = indexer;
		}

	}

	public boolean checkBoxUsername(String username){
		for(Friend f : atList){
			if(f.getUsername().equals(username)){
				return true;
			}
		}
		return false;
	}

}
