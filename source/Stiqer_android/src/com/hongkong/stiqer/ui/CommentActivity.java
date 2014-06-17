package com.hongkong.stiqer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.AdSbAdapter;
import com.hongkong.stiqer.adapter.CommentListAdapter;
import com.hongkong.stiqer.db.FriendDao;
import com.hongkong.stiqer.entity.Comment;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class CommentActivity extends Activity {
	
	private final static int      COMMENT_POST_SUCCESSFUL = 1300;
	private final static int      COMMENT_POST_FAILURE = 1014;
	private final static int      FEED_NOT_FOUND = 1019;
	private final static int      STORE_NOT_FOUND = 1013;
	private InputMethodManager mInputMethodManager ;
	View               footer;
    ListView           clistview;
    CommentListAdapter adapter;
    Context            context;
    List<Comment>      cl;
    ProgressBar        cProgressbar;
    Button             commentBtn;
    EditText           commentTxt;
    private final int  page_number = 10;
    Button             comment_back_btn;
    PopupWindow        popupWindow;
	FriendDao          friendDao;
	int                now_page = 0,type,position,comment_num;
	String             tid;
	private boolean    loadfinish = true;
	ErrorCodeHelper    errorHelper;
	CustomDialog       progressDialog;
	String             like_list;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		initData();
		initView();		
		new GetDataTask().execute();
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

	private void initData() {
		Intent t = getIntent();
		type = t.getIntExtra("type", 0);
		position = t.getIntExtra("position", 0);
		comment_num = t.getIntExtra("comment_num", 0);
		tid = t.getStringExtra("tid");
	}

	private void initView() {
		context = this;
		mInputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		errorHelper = new ErrorCodeHelper(context);
        footer = LayoutInflater.from(context).inflate(R.layout.footer_loading, null);
        cl = new ArrayList<Comment>();
        adapter = new CommentListAdapter(context, cl);
        clistview = (ListView) findViewById(R.id.comment_listview);
        cProgressbar = (ProgressBar)findViewById(R.id.comment_progressbar);
        clistview.setOnScrollListener(new ScrollLisenter());
        commentBtn = (Button) findViewById(R.id.comment_btn);
        commentTxt = (EditText) findViewById(R.id.comment_txt);
        commentTxt.addTextChangedListener(new MyTextWatcher());
        comment_back_btn = (Button) findViewById(R.id.comment_back_btn);
        comment_back_btn.setOnTouchListener(Util.TouchDark);
        comment_back_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				goFeed();
				CommentActivity.this.finish();
			}
        });
        progressDialog = CustomDialog.createProgressDialog(context, "post....");
        friendDao = new FriendDao(this);
        commentBtn.setOnClickListener(new PostOnClickListener());
        clistview.addFooterView(footer);
        clistview.setAdapter(adapter);
        clistview.removeFooterView(footer);
	}
	
	public void putLike(){
		if(!like_list.equals("")){
			LinearLayout like_wrap = (LinearLayout) findViewById(R.id.like_wrap);
			TextView like_1 = (TextView) findViewById(R.id.like_1);
			TextView like_2 = (TextView) findViewById(R.id.like_2);
			TextView like_3 = (TextView) findViewById(R.id.like_3);
			TextView like_4 = (TextView) findViewById(R.id.like_4);
			TextView like_5 = (TextView) findViewById(R.id.like_5);
			like_wrap.setVisibility(View.VISIBLE);
			like_5.setVisibility(View.VISIBLE);
			final String[] mList = like_list.split(";");
			if(mList.length>2){
			   //more than 2
				like_1.setVisibility(View.VISIBLE);
				like_2.setVisibility(View.VISIBLE);
				like_3.setVisibility(View.VISIBLE);
				like_4.setVisibility(View.VISIBLE);
				
				like_1.setText(mList[0]);
				like_2.setText(", "+mList[1]);
				int yu = mList.length - 2;
				like_4.setText(yu+" others");
				
				like_4.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						CustomDialog s = new CustomDialog(context);
                        s.createShowLikeDiaolog(mList).show();
					}
				});
			}else if(mList.length>1){
				like_1.setVisibility(View.VISIBLE);
				like_2.setVisibility(View.VISIBLE);
				like_1.setText(mList[0]);
				like_2.setText(" ,"+mList[1]);
			  // 2
			}else{
			  // 1
				like_1.setVisibility(View.VISIBLE);
				like_1.setText(mList[0]);
			}
			
			if(mList.length == 1){
				like_1.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						openUser(mList[0]);
					}
				});
			}else{
				like_1.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						openUser(mList[0]);
					}
				});
				
				like_2.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						openUser(mList[1]);
					}
				});
			}
		}

	}
	
	public void openUser(String username){
		Intent t = new Intent(context,UserActivity.class);
		t.putExtra("username", username);
		startActivity(t);
	}
	
    class MyTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			//Log.d("Stiqer", "beforeTextChanged:" + s + "-" + start + "-" + count + "-" + after);  
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			//Log.d("Stiqer", "onTextChanged:" + s + "-" + "-" + start + "-" + before + "-" + count);
			searchFriend(s);
		}
    	
    }	
    
    protected void searchFriend(CharSequence s){
    	//确定@，从后行前，如果没有碰到空格，标点，第一个@
    	int lastAtPosition = -1;
    	CharSequence searchStr="";
    	Pattern pattern = Pattern.compile("[ `~!#$^&*()=|{}':;',\\[\\].<>/?~！#￥……&*（）——|{}【】‘；：”“'。，、？]");
    	for(int i=s.length()-1; i>-1; i--){
    		if(pattern.matcher(String.valueOf(s.charAt(i))).matches()){
    			break;
    		}
    		if(String.valueOf(s.charAt(i)).equals("@")){
    			lastAtPosition = i;
    			break;
    		}
    	}
    	if(lastAtPosition>-1 && (lastAtPosition+1)<s.length()){
    		searchStr = s.subSequence(lastAtPosition+1,s.length());
    	}
    	if(!searchStr.equals("")){
    	    SearchSb(searchStr,lastAtPosition);
    	}
    }
    
	private void SearchSb(CharSequence searchStr, int atPosition) {
		Cursor cursor= friendDao.search(searchStr.toString());
		List<Map<String, Object>> atSbList = new ArrayList<Map<String,Object>>();
		if (cursor.moveToFirst()) {
			do {
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("atsb_fid", cursor.getString(1));
				item.put("atsb_name", cursor.getString(2));
				item.put("atsb_avatar", "");
				atSbList.add(item);
			} while (cursor.moveToNext());
		}
		if(atSbList.size()>0){
			showSearchPop(atSbList,atPosition);
		}
	}

	private void showSearchPop(final List<Map<String, Object>> atSbList,final int atPosition) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_atsb, null);
        ListView listView = (ListView) layout.findViewById(R.id.lv_atsb);
		listView.setAdapter(new AdSbAdapter(this,atSbList));
		popupWindow = new PopupWindow(this);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(280);
		popupWindow.setHeight(330);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		popupWindow.showAtLocation(findViewById(R.id.comment_post_wrap), Gravity.LEFT| Gravity.CENTER_VERTICAL, 10, -170);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				popupWindow.dismiss();
				String nowText = commentTxt.getText().toString();
		        commentTxt.setText(nowText.substring(0, (atPosition+1))+atSbList.get(position).get("atsb_name")+" ");
		        commentTxt.setSelection(commentTxt.getText().toString().length());
			}
		});
	}
	
	class PostOnClickListener implements OnClickListener{
		public void onClick(View v) {
			if(TextUtils.isEmpty(commentTxt.getText().toString())){
				Toast.makeText(context, "can not be empty", Toast.LENGTH_SHORT).show();
				return;
			}
			progressDialog.show();
			new Thread(){
		    	 public void run(){
		    		 int code = APIManager.sharedInstance().addComment(type,tid,commentTxt.getText().toString());
					 Message msg = mHandler.obtainMessage();
					 if(code == 1000){
						 code = COMMENT_POST_SUCCESSFUL;
					 }
			         msg.what = code;
			         mHandler.sendMessage(msg);
		    	 }
		    }.start();
		}
	}
	
	 private Handler mHandler = new Handler()
	    {
	        public void handleMessage(Message msg)
	        {
	       	 super.handleMessage(msg);
	       	 if(errorHelper.CommonCode(msg.what)){
	            switch (msg.what)
	            {
	            
	                case COMMENT_POST_SUCCESSFUL:
	                	CacheHelper cacheHelper = new CacheHelper(context);
	                	SUser myUser = cacheHelper.GetUser();
	                	Comment newComment = new Comment();
	                	newComment.setCmt_username(myUser.getUsername());
	                	newComment.setCmt_time("Just now");
	                	newComment.setCmt_user_img(myUser.getProfile_img_url());
	                	newComment.setCmt_message(commentTxt.getText().toString());
	                	newComment.setCmt_time("");
	                	cl.add(0, newComment);
	                	comment_num = comment_num+1;
	                	commentTxt.setText("");
	                	adapter.notifyDataSetChanged();
	                	progressDialog.dismiss();
	                	mInputMethodManager.hideSoftInputFromWindow(CommentActivity.this.getCurrentFocus().getWindowToken(),
	                            InputMethodManager.HIDE_NOT_ALWAYS);
	                	break;
	                	
	                case COMMENT_POST_FAILURE:
	                	Toast.makeText(context, "publish fail", Toast.LENGTH_SHORT).show();
	                	break;
	                case FEED_NOT_FOUND:
	                	Toast.makeText(context,getString(R.string.feed_not_found), Toast.LENGTH_SHORT).show();
	                	break;
	                case STORE_NOT_FOUND:
	                	Toast.makeText(context,getString(R.string.store_not_found), Toast.LENGTH_SHORT).show();
	                	break;
	              }
	            
	       	   }  
	        };
	    };
	    
	    private class GetDataTask extends AsyncTask<Integer[],Void,JSONObject>{
			@Override
			protected void onPostExecute(JSONObject result) {
				cProgressbar.setVisibility(View.GONE);
				if(result != null){
				try {
				  if(result.has("like_list")){
					   like_list = result.getString("like_list");
					   putLike();
				   }
				   List<Comment> comment_list = (List<Comment>) Util.readJson2EntityList(result.getString("comment_list"), new Comment());
					     Log.e("Stiqer","comment_list="+comment_list.size());
						 if(comment_list.size()>0){
							if(errorHelper.CommonCode(comment_list.get(0).getError_code())){
								cl.addAll(comment_list);
								adapter.notifyDataSetChanged();
								if(comment_list.size() == page_number){
									loadfinish = true;
									now_page++;
								}
							}
						}else{
				        	errorHelper.loadOver();
				        }
					   
				  } catch (JSONException e) {
						e.printStackTrace();
				  }
				}else{
					errorHelper.connectError();
				}
				if(clistview.getFooterViewsCount() > 0) clistview.removeFooterView(footer);
				super.onPostExecute(result);
			}
			@Override
			protected JSONObject doInBackground(Integer[]... params) {
				return APIManager.sharedInstance().getComment(type,tid,now_page*page_number,page_number);
			}
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
							clistview.addFooterView(footer);
							new GetDataTask().execute();
						}
					}	
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {

			}
	  }    
	  
	  @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
        	 goFeed();
         }
         return super.onKeyDown(keyCode, event);
      }
	  
	  public void goFeed(){
		  Intent resultIntent = new Intent();
   		  Bundle bundle = new Bundle();
   		  bundle.putInt("position", position);
   		  bundle.putInt("comment_num", comment_num);
   		  resultIntent.putExtras(bundle);
   		  this.setResult(RESULT_OK, resultIntent);
	  }
	  
}
