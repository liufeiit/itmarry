package com.hongkong.stiqer.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.flurry.android.FlurryAgent;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.AddFriendAdapter;
import com.hongkong.stiqer.db.AddFriendDao;
import com.hongkong.stiqer.entity.AddFriend;
import com.hongkong.stiqer.entity.Friend;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.entity.UpdateFriend;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.base.BaseActivity;
import com.hongkong.stiqer.utils.AddFriendComparator;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.FriendComparator;
import com.hongkong.stiqer.utils.GetFirstZimu;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.AddFriendListener;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class AddFriendActivity extends BaseActivity{
	
	private static final short ACTION_SINAWEIBO = 5;
	private static final short ACTION_FACEBOOK = 2;
	private static final int   TYPE_FACEBOOK = 2;
	private static final int   TYPE_WEIBO = 1;
	private static final int   TYPE_CONTACT = 3;
	
	ListView                    listView;
	List<Map<String, String>>   ll;
	int                         type;
	Platform                    weibo,facebook;
	AddFriendListener           adapterListener;
	LinearLayout                listview_layout;
	
	//分组
	private LinearLayout      titleLayout;
	private RelativeLayout    sectionToastLayout;
	private TextView          alphabetButton;
	private TextView          title,headTitle;
	private TextView          sectionToastText;
	private AddFriendAdapter  fAdapter;
	private AlphabetIndexer   indexer;
	private List<AddFriend>   contacts = new ArrayList<AddFriend>();
	private List<String>      checkRowfid = new ArrayList<String>();
	private String            alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private int               lastFirstVisibleItem = -1;
	List<AddFriend>           preContacts;
	private AddFriendDao      addFriendDao;
	private Context           mContext;
	private Button            afBack;
	ErrorCodeHelper           errorHelper;
	List<UpdateFriend>        updateList;
	ProgressBar               af_progressbar;
	SUser                     loginUser;
	Object                    post_weibo_id = null;
	Object                    post_facebook_id = null;
	String                    invite_weibo_name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriend);
		ShareSDK.initSDK(this);
		initUser();
        initView();
               
		facebook = ShareSDK.getPlatform(this, "Facebook");
		weibo = ShareSDK.getPlatform(this, "SinaWeibo");
		Intent i = getIntent();
		
		switch(i.getIntExtra("type",0)){
		case TYPE_FACEBOOK:
			type = TYPE_FACEBOOK;
			headTitle.setText("From Facebook");
			GetFromFacebook();
			break;
		case TYPE_WEIBO:
			type = TYPE_WEIBO;
			headTitle.setText("From Weibo");
			GetFromWeibo();
			break;
		case TYPE_CONTACT:
			headTitle.setText("From Contact");
			type = TYPE_CONTACT;
			GetContact();
		}
	}

	
	private void initUser(){
		CacheHelper cacheHelper = new CacheHelper(this);
		loginUser = cacheHelper.GetUser();
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
   /*
	private void showData() {
		List<AddFriend> persons = addFriendDao.query(1);  
        for (AddFriend person : persons) {  
              Log.e("Stiqer","ac="+person.getName());
        }
	}
*/
	private void initView() {
		ll = new ArrayList<Map<String,String>>();
		listView = (ListView) findViewById(R.id.addfriend_listview);
		addFriendDao = new AddFriendDao(this);
		preContacts = new ArrayList<AddFriend>();
		headTitle = (TextView) findViewById(R.id.af_head_title);
		afBack = (Button) findViewById(R.id.af_back_btn);
		af_progressbar = (ProgressBar) findViewById(R.id.af_progressbar);
		
		adapterListener = new AddFriendListener(){
			public void sendRequest(int position) {
			  int wantedPosition = position;
		      int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); 
		      int wantedChild = wantedPosition - firstPosition;
			  listview_layout = (LinearLayout)listView.getChildAt(wantedChild);// 获得Item布局，i就是item的位置
			  Button add_btn= (Button) listview_layout.findViewById(R.id.addfriend_btn);  
			  Button added_btn= (Button) listview_layout.findViewById(R.id.added_btn);  
			  add_btn.setVisibility(View.GONE);
			  added_btn.setVisibility(View.VISIBLE);
			}
			
			public void sendInvite(int position,int type, String rawfid, String name){
				Log.e("Stiqer","listview="+position+";"+type+";"+rawfid+";"+name);
				switch(type){
				case 1:
					invite_weibo_name = name;
					sendInviteByWeibo(rawfid);
					break;
				case 2:
					sendInviteByFacebook(rawfid,position);
					break;
				case 3:
					sendInviteByMsg(rawfid);
					break;
				}
				if(type ==1 || type == 3){
					changeInviteStatus(position);
				}
			}
		};
		mContext = this;
		errorHelper = new ErrorCodeHelper(mContext);
		
		titleLayout = (LinearLayout) findViewById(R.id.af_title_layout);
		sectionToastLayout = (RelativeLayout) findViewById(R.id.af_section_toast_layout);
		title = (TextView) findViewById(R.id.af_title);
		sectionToastText = (TextView) findViewById(R.id.af_section_toast_text);
		alphabetButton = (TextView) findViewById(R.id.af_alphabetButton);
		alphabetButton.setText("#\nA\nB\nC\nD\nE\nF\nG\nH\nI\nJ\nK\nL\nM\nN\nO\nP\nQ\nR\nS\nT\nU\nV\nW\nX\nY\nZ");
		afBack.setOnTouchListener(Util.TouchDark);
		afBack.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				AddFriendActivity.this.finish();
			}
		});
		
	}
	
	private void changeInviteStatus(int position){
		int wantedPosition = position;
		    int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); 
		    int wantedChild = wantedPosition - firstPosition;
		   
		listview_layout = (LinearLayout)listView.getChildAt(wantedChild);// 获得Item布局，i就是item的位置
		Button invite_btn= (Button) listview_layout.findViewById(R.id.invite_btn);  
		Button invited_btn= (Button) listview_layout.findViewById(R.id.invited_btn);  
		invite_btn.setVisibility(View.GONE);
		invited_btn.setVisibility(View.VISIBLE);
	}
	
	private void GetContact() {
        // 获取数据，打包成addFriend类
		// 接收，导入数据库
		// 展示
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Cursor cursor = getContentResolver().query(uri,new String[] { "display_name", "sort_key",Phone.NUMBER,Photo.PHOTO_ID }, null, null, "sort_key");
		if (cursor.moveToFirst()) {
			do {
				AddFriend contact = new AddFriend();
				contact.setName(cursor.getString(0));
				contact.setRawfid(cursor.getString(2));
				contact.setAvatar(cursor.getString(3));
				contact.setType(3);
				contact.setStatus(4);
				contact.setSortKey(cursor.getString(1).substring(0, 1));
				preContacts.add(contact);
				checkRowfid.add(cursor.getString(2));
			} while (cursor.moveToNext());
		}
		UpdateData(3);
		// 插入 数据库数据
	}

	private void UpdateData(final int type) {
		// TODO Auto-generated method stub
		addFriendDao.deleteAll(type);
		addFriendDao.add(preContacts);
		// 上传到服务器端
        new Thread(){
        	public void run(){
        		updateList = APIManager.sharedInstance().getAddFriendUpadte(checkRowfid,type,"fid");
        	    Message msg = mHandler.obtainMessage();
        	    Bundle bundle = new Bundle();
        	    bundle.putInt("type", type);
        	    msg.setData(bundle);
        	    msg.what = 3;
        	    mHandler.sendMessage(msg);
        	}
        }.start();
	}

	private void GetFromFacebook() {
		facebook.setPlatformActionListener(new GetListListener());
		String url = "https://graph.facebook.com/me/friends";
		String method = "GET";
		short customerAction = ACTION_FACEBOOK;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("fields", "id,name,picture");
		facebook.customerProtocol(url, method, customerAction, values, null);
	}

	private void GetFromWeibo() {
		weibo.setPlatformActionListener(new GetListListener());
		String url = "https://api.weibo.com/2/friendships/friends/bilateral.json";
		String method = "GET";
		short customerAction = ACTION_SINAWEIBO;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("count", 100);
		values.put("cursor", 1);
		weibo.customerProtocol(url, method, customerAction, values, null);
	}
	
	private void sendInviteByFacebook(final String rawfid, final int position) {
		 Session.openActiveSession(this, true, new Session.StatusCallback() {
			    // callback when session changes state
			    @Override
			    public void call(Session session, SessionState state, Exception exception) {
			    	if (session.isOpened()) {
			    		Log.e("Stiqer","open");
			    		inviteFacebookFriend(rawfid, position);
			    	}
			    }
		});
		
		/*
		if(post_facebook_id != null){
			sendFacebookComment();
		}else{
		  facebook.setPlatformActionListener(new SendFacebookInviteListener());
		  String url = "https://graph.facebook.com/me/feed";
		  String method = "POST";
		  short customerAction = ACTION_FACEBOOK;
		  HashMap<String, Object> values = new HashMap<String, Object>();
		  values.put("message", "Stiqer is a fun and useful app for redeeming mobile coupons at your favorite local stores with your friends. Get the app today! Search 'stiqer' on the app store.");
		  facebook.customerProtocol(url, method, customerAction, values, null);
		}*/
	}
	
	public class SendFacebookInviteListener implements PlatformActionListener{
		public void onCancel(Platform arg0, int arg1) {}
		public void onComplete(Platform arg0, int arg1,HashMap<String, Object> res) {
			post_facebook_id = res.get("id");
			sendFacebookComment();	
		}
		public void onError(Platform arg0, int arg1, Throwable arg2) {}
	}

	private void sendInviteByMsg(String rawfid) {
		 String content = "Your friend " + loginUser.getUsername() + " has invited you to join Stiqer.Stiqer is a fun and useful app for redeeming mobile coupons at your favorite local stores with your friends. Get the app today! Search 'stiqer' on the app store. ";
		 //if(PhoneNumberUtils.isGlobalPhoneNumber(rawfid)){ 
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+rawfid));           
            intent.putExtra("sms_body", content);           
            startActivity(intent); 
	    //} 
	}

	private void sendInviteByWeibo(String rawfid) {
		if(post_weibo_id != null){
			sendWeiboComment();
		}else{
		  String content = "Stiqer is a fun and useful app for redeeming mobile coupons at your favorite local stores with your friends. Get the app today! Search 'stiqer' on the app store.";
		  weibo.setPlatformActionListener(new SendWeiboNotiListener());
		  String url = "https://api.weibo.com/2/statuses/update.json";
		  String method = "POST";
		  short customerAction = ACTION_SINAWEIBO;
		  HashMap<String, Object> values = new HashMap<String, Object>();
		  values.put("status", content);
	 	  weibo.customerProtocol(url, method, customerAction, values, null);
		}
	}
	
	public class SendWeiboNotiListener implements PlatformActionListener{
		public void onCancel(Platform arg0, int arg1) {}
		public void onComplete(Platform arg0, int arg1,HashMap<String, Object> res) {
			post_weibo_id = res.get("id");
			sendWeiboComment();	
		}
		public void onError(Platform arg0, int arg1, Throwable arg2) {}
	}
	
	private void sendWeiboComment(){
		  String content = "@"+invite_weibo_name+" Stiqer is a fun and useful app for redeeming mobile coupons at your favorite local stores with your friends. Get the app today! Search 'stiqer' on the app store.";
		  weibo.setPlatformActionListener(new SendCommentNotiListener());
		  String url = "https://api.weibo.com/2/comments/create.json";
		  String method = "POST";
		  short customerAction = ACTION_SINAWEIBO;
		  HashMap<String, Object> values = new HashMap<String, Object>();
		  values.put("comment", content);
		  values.put("id", post_weibo_id);
	 	  weibo.customerProtocol(url, method, customerAction, values, null);
	}
	
	private void sendFacebookComment(){
		String content = "@"+invite_weibo_name+" Stiqer is a fun and useful app for redeeming mobile coupons at your favorite local stores with your friends. Get the app today! Search 'stiqer' on the app store.";
		  weibo.setPlatformActionListener(new SendCommentNotiListener());
		  String url = "https://api.weibo.com/2/comments/create.json";
		  String method = "POST";
		  short customerAction = ACTION_SINAWEIBO;
		  HashMap<String, Object> values = new HashMap<String, Object>();
		  values.put("comment", content);
		  values.put("id", post_weibo_id);
	 	  weibo.customerProtocol(url, method, customerAction, values, null);
	}
	
	public class SendCommentNotiListener implements PlatformActionListener{
		public void onCancel(Platform arg0, int arg1) {}
		public void onComplete(Platform arg0, int arg1,HashMap<String, Object> res) {
			Log.e("Stiqer","comment_res="+res.toString());
		}
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			Log.e("Stiqer","comment_error="+arg1);
		}
	}
	
	public class GetListListener implements PlatformActionListener{

		@Override
		public void onCancel(Platform arg0, int arg1) {
			Log.e("Stiqer","cancel="+arg1);
		}
		@Override
		public void onComplete(Platform arg0, int action,HashMap<String, Object> res) {
			GetFirstZimu cte = new GetFirstZimu();
			if(res.get("total_number") != null){
				//weibo
				//解析,加入数据库
	            ArrayList<Object> list = (ArrayList<Object>) res.get("users");		
				for (Object o : list) {
					AddFriend p = new AddFriend();
					HashMap<String, Object> oo = (HashMap<String, Object>)o;
					p.setAvatar(oo.get("profile_image_url").toString());
					p.setRawfid(oo.get("id").toString());
					p.setName(oo.get("name").toString());
                    p.setType(1);
                    p.setStatus(4);
                    p.setSortKey(cte.getAllFirstLetter(oo.get("name").toString()));
                    Log.e("Stiqer","frist letter = "+cte.getAllFirstLetter(oo.get("name").toString()));
                    checkRowfid.add(p.getRawfid());
                    preContacts.add(p);
				}
				UpdateData(1);
			}else{			
				ArrayList<Object> list = (ArrayList<Object>) res.get("data");
				for (Object o : list) {
					AddFriend p = new AddFriend();
					Map<String, String> map = new HashMap<String,String>();
					HashMap<String, Object> oo = (HashMap<String, Object>)o;
					HashMap<String, Object> picture = (HashMap<String, Object>) oo.get("picture");
					HashMap<String, Object> p_data = (HashMap<String, Object>) picture.get("data");
					
					p.setAvatar(p_data.get("url").toString());
					p.setRawfid(oo.get("id").toString());
					p.setName(oo.get("name").toString());
                    p.setType(2);
                    p.setStatus(4);
                    p.setSortKey(cte.getAllFirstLetter(p.getName()));
                    checkRowfid.add(p.getRawfid());
                    preContacts.add(p);
				}
				UpdateData(2);
			}
		}
		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			Log.e("Stiqer","error="+arg1+"arg2="+arg2.getMessage());
			// TODO Auto-generated method stub
		}
	}
	
	 private Handler mHandler = new Handler()
     {
         public void handleMessage(Message msg)
         {
        	 super.handleMessage(msg);
             switch (msg.what)
             {
                 case 3:
                	int type = msg.getData().getInt("type");

         			if(updateList.size()>0){
         			   if(errorHelper.CommonCode(updateList.get(0).getError_code()))
         			   {
         				  addFriendDao.update(updateList,type);
         			   }
         			}
                	 Cursor cursor = addFriendDao.queryTheCursor(type);
                	 if (cursor.moveToFirst()) {
             			do {
             				AddFriend contact = new AddFriend();
             				contact.setType(cursor.getInt(1));
             				contact.setUsername(cursor.getString(2));
             				contact.setRawfid(cursor.getString(3));
             				contact.setName(cursor.getString(4));
             				contact.setSortKey(cursor.getString(5));
             				contact.setAvatar(cursor.getString(6));
             				contact.setStatus(cursor.getInt(7));
             				contact.setUser_level(cursor.getInt(8));
             				contacts.add(contact);
             			} while (cursor.moveToNext());
             		 }
                	 startManagingCursor(cursor);
                	 Collections.sort(contacts, new AddFriendComparator());
             		 indexer = new AlphabetIndexer(cursor, 5, alphabet);
             		 fAdapter = new AddFriendAdapter(mContext, R.layout.list_item_addfriend, contacts,adapterListener,type);
             		 fAdapter.setIndexer(indexer);             	
             		 if (contacts.size() > 0) {
             			setupContactsListView();
             			setAlpabetListener();
             		 }
             		 af_progressbar.setVisibility(View.GONE);
             		 listView.setVisibility(View.VISIBLE);
                	 break;
             }
         };
     };

	
	public void onDestroy(){
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
	
	
	/**
	 * 为联系人ListView设置监听事件，根据当前的滑动状态来改变分组的显示位置，从而实现挤压动画的效果。
	 */
	private void setupContactsListView() {
		listView.setAdapter(fAdapter);

		listView.setOnScrollListener(new OnScrollListener() {
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
					listView.setSelection(position);
					break;
				case MotionEvent.ACTION_MOVE:
					sectionToastText.setText(sectionLetter);
					listView.setSelection(position);
					break;
				default:
					alphabetButton.setBackgroundColor(0xffffffff);
					sectionToastLayout.setVisibility(View.GONE);
				}
				return true;
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void inviteFacebookFriend(String uid, final int position){
		Bundle params = new Bundle();
		params.putString("title", "Send invitation");
	    params.putString("message", "Stiqer is a fun and useful app for redeeming mobile coupons at your favorite local stores with your friends. Get the app today! Search 'stiqer' on the app store");
	    params.putString("to", uid);
	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(mContext,
	            Session.getActiveSession(),params)).setOnCompleteListener(new OnCompleteListener() {
	                @Override
	                public void onComplete(Bundle values,FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(getApplicationContext(),  "Request cancelled", Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            //Toast.makeText(getApplicationContext(), "Request sent",  Toast.LENGTH_SHORT).show();
	                        	changeInviteStatus(position);
	                        } else {
	                            Toast.makeText(getApplicationContext(), "Request cancelled", Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                }

	            })
	            .build();
	    requestsDialog.show();
	}
	
}
