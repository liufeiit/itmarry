package com.hongkong.stiqer.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONException;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.FriendStatus;
import com.hongkong.stiqer.entity.Noti;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.Defs;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.hongkong.stiqer.widget.DialogListener;
import com.hongkong.stiqer.widget.MessageListener;
import com.hongkong.stiqer.widget.NotiListener;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NotiListAdapter extends BaseAdapter{
	List<Noti> mData;
	private final static int   TYPE_MESSAGE = 1;
	private final static int   TYPE_COMMENT = 2;
	private final static int   TYPE_LIKE = 3;
	private final static int   TYPE_CHECKIN = 4;
	private final static int   TYPE_STORE = 5;
	private final static int   TYPE_LEVELUP = 6;
	private final static int   TYPE_CLASSUP = 7;
	private final static int   TYPE_ACHIEVEMENT = 8;
	private final static int   TYPE_INVITATION = 9;
	private final static int   TYPE_FINISH_INVITATION = 10;
	
	private final static int   SENDER_TYPE_ADMIN = 3;
	private final static int   SENDER_TYPE_STORE = 2;
	private final static int   SENDER_TYPE_USER = 1;
	private final static int   REPLY_SUCCESS = 1000;
    
	private static final int    ALREADY_SEND = 1021;
	private static final int    ALREADY_FRIEND = 1022;
	private static final int    ADD_FRIEND_SUCCESS = 1040;
	
	private static final int    CAN_NOT_SEND = 1033;
	private static final int    USER_NOT_FOUND = 1034;
	private static final int    MESSAGE_REPLY_SUCCESS = 1041;
	private static final int    COMMENT_REPLY_SUCCESS = 1042;
	
	MessageListener          mListener;
	private LayoutInflater   mInflater;
	private Context          context;
	ViewHolder               holder;
	CustomDialog             replyDialog,bigImageDialog;
	NotiListener             replyListener;
	String                   message_reply_username;
	int                      nowPosition;
	String                   nowNotId;
	String                   comment_feed_id;
	ErrorCodeHelper          errorHelper;
	SUser                    loginUser;
	
	public NotiListAdapter(Context context, List<Noti> mNoti, MessageListener listener){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = mNoti;
		this.mListener = listener;
		this.errorHelper = new ErrorCodeHelper(context);
		replyListener = new NotiListener(){
			public void showDialog(int type, String message) {
				sendReply(type, message);
			}
		};
		CacheHelper cacherHelper = new CacheHelper(context);
		loginUser = cacherHelper.GetUser();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
	    if (rowView == null) {
	        rowView = mInflater.inflate(R.layout.list_item_noti, null);
	        ViewHolder holder = new ViewHolder();
	        holder.from_avatar = (ImageView) rowView.findViewById(R.id.noti_from_avatar);
	        holder.from_name = (TextView) rowView.findViewById(R.id.noti_from_name);
	        holder.from_level_num = (TextView) rowView.findViewById(R.id.noti_from_level_num);
	        holder.noti_at_store = (TextView) rowView.findViewById(R.id.noti_at_store);
	        
			holder.noti_message = (TextView) rowView.findViewById(R.id.noti_message);
			holder.noti_time = (TextView) rowView.findViewById(R.id.noti_time_txt);
			holder.noti_reply_comment = (Button) rowView.findViewById(R.id.noti_reply_comment);
			holder.noti_reply_message = (Button) rowView.findViewById(R.id.noti_reply_message);
			holder.from_level_icon = (ImageView) rowView.findViewById(R.id.from_level_icon);
			
			holder.noti_pic_two_wrap = (LinearLayout) rowView.findViewById(R.id.noti_pic_two_wrap);
			holder.noti_pic_two_1 = (ImageView) rowView.findViewById(R.id.noti_pic_two_1);
			holder.noti_pic_two_2 = (ImageView) rowView.findViewById(R.id.noti_pic_two_2);
			
			holder.noti_pic_three_wrap = (LinearLayout) rowView.findViewById(R.id.noti_pic_three_wrap);
			holder.noti_pic_three_1 = (ImageView) rowView.findViewById(R.id.noti_pic_three_1);
			holder.noti_pic_three_2 = (ImageView) rowView.findViewById(R.id.noti_pic_three_2);
			
			holder.noti_pic_one = (ImageView) rowView.findViewById(R.id.noti_pic_one);
			
			holder.class_icon = (ImageView) rowView.findViewById(R.id.class_icon);
			holder.class_store_img = (ImageView) rowView.findViewById(R.id.class_store_img);
			holder.class_des = (TextView) rowView.findViewById(R.id.class_des);
			holder.class_store_name = (TextView) rowView.findViewById(R.id.class_store_name);
			
			holder.btn_receive = (Button) rowView.findViewById(R.id.btn_receive);
			holder.btn_refuse = (Button) rowView.findViewById(R.id.btn_refuse);
			holder.none_message = (TextView) rowView.findViewById(R.id.none_message);
			
			holder.wrap1 = (LinearLayout) rowView.findViewById(R.id.noti_level_wrap1);
			holder.wrap2 = (LinearLayout) rowView.findViewById(R.id.noti_level_wrap2);
	        rowView.setTag(holder);
	    }

	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    final int noti_type = mData.get(position).getNoti_type();
	    final int sender_type = mData.get(position).getNoti_sender_type();
		Picasso.with(context).load(mData.get(position).getNoti_sender_img()).resize(60,60).into(holder.from_avatar);
		
		holder.from_name.setText(mData.get(position).getNoti_sender_name());	
	    int user_level = 0;
	    try {
	         user_level = mData.get(position).getNoti_extra().getInt("user_level");
	         
	    } catch (JSONException e) {
			e.printStackTrace();
		}
	    
		if(sender_type == 1){
			holder.from_level_icon.setVisibility(View.GONE);
			holder.from_level_num.setVisibility(View.GONE);
	    }else{
	    	holder.from_level_icon.setVisibility(View.GONE);
	    	holder.from_level_num.setVisibility(View.GONE);
	    }
		
    	
		if(noti_type == TYPE_INVITATION){
			holder.btn_receive.setVisibility(View.VISIBLE);
			holder.btn_refuse.setVisibility(View.VISIBLE);
		}else{
			holder.btn_receive.setVisibility(View.GONE);
			holder.btn_refuse.setVisibility(View.GONE);
		}
		
		//如果是message
	    if(noti_type == TYPE_MESSAGE){
	    	holder.noti_reply_message.setVisibility(View.VISIBLE);
	    }else{
	    	holder.noti_reply_message.setVisibility(View.GONE);
	    }
		
		holder.noti_time.setText(Util.transTime(mData.get(position).getNoti_time()));
		holder.noti_message.setText(mData.get(position).getNoti_message());
		
		if(noti_type == TYPE_CLASSUP){
			holder.noti_message.setText(mData.get(position).getNoti_message()+" "+mData.get(position).getNoti_sender_name());
		}
		
		if(noti_type == TYPE_COMMENT || noti_type == TYPE_CHECKIN || noti_type == TYPE_LIKE){
			holder.noti_message.setText(mData.get(position).getNoti_sender_name()+" "+mData.get(position).getNoti_message());
		}
		
		holder.btn_receive.setOnTouchListener(Util.TouchDark);
		holder.btn_receive.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				nowPosition = position;
				new Thread(){
					public void run(){
						FriendStatus friendStatus = APIManager.sharedInstance().addFriend(mData.get(position).getNoti_sender_name()); 
						Message msg = mHandler.obtainMessage();
						if(friendStatus.getError_code() == 1000){
							msg.what = ADD_FRIEND_SUCCESS;
						}else{
							msg.what = friendStatus.getError_code();
						}
						nowNotId = mData.get(position).getNoti_id();
						mHandler.sendMessage(msg);
					}
		    	}.start();	
			}
	    });
	    
		holder.btn_refuse.setOnTouchListener(Util.TouchDark);
	    holder.btn_refuse.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				nowPosition = position;
				mListener.deleteNode(nowPosition);
				nowNotId = mData.get(position).getNoti_id();
				deleteNode();
			}
	    });
	    
	    holder.from_avatar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				switch(sender_type){
				case SENDER_TYPE_USER:
					Intent t = new Intent(context,UserActivity.class);
					t.putExtra("username", mData.get(position).getNoti_sender_name());
					context.startActivity(t);
					break;
				case SENDER_TYPE_STORE:
					Intent t2 = new Intent(context,StoreDetailActivity.class);
					t2.putExtra("store_name", mData.get(position).getNoti_sender_name());
					t2.putExtra("store_id", mData.get(position).getNoti_sender_id());
					context.startActivity(t2);
					break;
                default:
                	break;
				}
			}
		});
	    
	    if(noti_type == TYPE_MESSAGE){
	    	holder.noti_reply_message.setVisibility(View.VISIBLE);
	    }else{
	    	holder.noti_reply_message.setVisibility(View.GONE);
	    }
	    holder.noti_reply_message.setOnTouchListener(Util.TouchDark);
	    holder.noti_reply_message.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				replyDialog = CustomDialog.createNotiReplyDialog(context,TYPE_MESSAGE,mData.get(position).getNoti_sender_name(), mData.get(position).getNoti_sender_img(), replyListener);
				replyDialog.show();
				message_reply_username = mData.get(position).getNoti_sender_name();
			}
	    });
	    
	  //如果是 class up
	    if(noti_type == TYPE_CLASSUP){
	    	holder.wrap1.setVisibility(View.VISIBLE);
	    	holder.wrap2.setVisibility(View.VISIBLE);
	    	Picasso.with(context).load(class_image_array[user_level]).into(holder.class_icon);
	    	Picasso.with(context).load(mData.get(position).getNoti_sender_img()).into(holder.class_store_img);
	    	holder.class_des.setText(class_name_array[user_level]);
	    	holder.class_store_name.setText(mData.get(position).getNoti_sender_name());
	    }else{
	    	holder.wrap1.setVisibility(View.GONE);
	    	holder.wrap2.setVisibility(View.GONE);
	    }
	    
	    
		holder.wrap2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,StoreDetailActivity.class);
				t.putExtra("store_id", mData.get(position).getNoti_sender_id());
				t.putExtra("store_name", mData.get(position).getNoti_sender_name());
				context.startActivity(t);
			}
		});
		
		 if(noti_type == TYPE_COMMENT){
		      holder.noti_reply_comment.setVisibility(View.VISIBLE);
		 }else{
		    	holder.noti_reply_comment.setVisibility(View.GONE);
		 }
		 holder.noti_reply_comment.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					nowNotId = mData.get(position).getNoti_id();
					replyDialog = CustomDialog.createNotiReplyDialog(context, TYPE_COMMENT, mData.get(position).getNoti_sender_name(), mData.get(position).getNoti_sender_img(), replyListener);
					replyDialog.show();
					try {
						comment_feed_id = mData.get(position).getNoti_extra().getString("feed_id");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				}
		    });
		 
		   if(noti_type == TYPE_LEVELUP){
			    holder.noti_pic_two_wrap.setVisibility(View.VISIBLE);
		    	Picasso.with(context).load(R.drawable.ic_level).into(holder.noti_pic_two_1);
		    	//Picasso.with(context).load(loginUser.getProfile_img_url()).into(holder.noti_pic_two_2);
		    	Picasso.with(context).load(R.drawable.logo).into(holder.noti_pic_two_2);
		    }else{
		    	holder.noti_pic_two_wrap.setVisibility(View.GONE);
		    }
           
		   
		   if(noti_type == TYPE_CHECKIN){
			   holder.noti_pic_three_wrap.setVisibility(View.VISIBLE);
			   holder.noti_at_store.setVisibility(View.VISIBLE);
			   
			   String img1 = "";
			   String img2 = "";
			   String show_store_name = "";
			   try {
				   show_store_name = mData.get(position).getNoti_extra().getString("at_store_name");
				   img1 = mData.get(position).getNoti_extra().getString("img_url_1");
				   img2 = mData.get(position).getNoti_extra().getString("img_url_2");  
			    } catch (JSONException e) {
				
				e.printStackTrace();
			   }
			   if(!show_store_name.equals("")){
				   holder.noti_at_store.setVisibility(View.VISIBLE);
				   holder.noti_at_store.setText("--@"+show_store_name);
			   }else{
				   holder.noti_at_store.setVisibility(View.GONE);
			   }
			   if(!img1.equals("")){
				   final String img1_big = img1;
				   holder.noti_pic_three_1.setVisibility(View.VISIBLE);
				   Picasso.with(context).load(img1).into(holder.noti_pic_three_1);
				   holder.noti_pic_three_1.setOnClickListener(new OnClickListener(){
					 public void onClick(View arg0) {
						 bigImageDialog = CustomDialog.createBigImageDialog(context,img1_big, Util.changeImageUrl(img1_big, 2, 3));
						 bigImageDialog.show();
					 }  
				   });
			   }else{
				   holder.noti_pic_three_1.setVisibility(View.GONE);
			   }
			   if(!img2.equals("")){
				   final String img2_big = img2;
				   holder.noti_pic_three_2.setVisibility(View.VISIBLE);
				   Picasso.with(context).load(img2).into(holder.noti_pic_three_2);
				   holder.noti_pic_three_2.setOnClickListener(new OnClickListener(){
						 public void onClick(View arg0) {
							 bigImageDialog = CustomDialog.createBigImageDialog(context,img2_big, Util.changeImageUrl(img2_big, 2, 3));
							 bigImageDialog.show();
						 }  
					   });
			   }else{
				   holder.noti_pic_three_2.setVisibility(View.GONE);
			   }
			   
			   holder.noti_at_store.setOnClickListener(new OnClickListener(){
				  public void onClick(View arg0) {
				   // TODO Auto-generated method stub
					  Intent t2 = new Intent(context,StoreDetailActivity.class);
					  String at_store_name = "";
					  String at_store_id = "";
					 try {
						at_store_id = mData.get(position).getNoti_extra().getString("at_store_id");
						at_store_name = mData.get(position).getNoti_extra().getString("at_store_name");
					 } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
					 
					 t2.putExtra("store_name", at_store_name);
					 t2.putExtra("store_id", at_store_id );
					 context.startActivity(t2);
				  }
			   });
		   }else{
			   holder.noti_pic_three_wrap.setVisibility(View.GONE);
			   holder.noti_at_store.setVisibility(View.GONE);
		   }
		/*
		//回复
	   
	    	    
	    //如果是checkin
	    //如果是store 发布
	    if(type == TYPE_CHECKIN || type == TYPE_STORE){
	    	holder.noti_pic_two_wrap.setVisibility(View.VISIBLE);
	    	if(!mData.get(position).getNoti_img_1().equals("")){
	    		Picasso.with(context).load(mData.get(position).getNoti_img_1()).into(holder.noti_pic_two_1);
	    	}
	    	if(!mData.get(position).getNoti_img_2().equals("")){
	    		Picasso.with(context).load(mData.get(position).getNoti_img_2()).into(holder.noti_pic_two_2);
	    	}
	    }
	    holder.noti_pic_two_1.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				bigImageDialog = CustomDialog.createBigImageDialog(context,Util.changeImageUrl(mData.get(position).getNoti_img_1(), 1, 3));
				bigImageDialog.show();
			}
	    });
	    holder.noti_pic_two_2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				bigImageDialog = CustomDialog.createBigImageDialog(context,Util.changeImageUrl(mData.get(position).getNoti_img_2(), 1, 3));
				bigImageDialog.show();
			}
	    });
	    
	    //如果是level up 和 获得achievement
	    if(type == TYPE_LEVELUP || type == TYPE_ACHIEVEMENT){
	    	Picasso.with(context).load(mData.get(position).getNoti_img_1()).into(holder.noti_pic_one);
	    }

		Pattern mentionsPattern = Pattern.compile("@(\\w+?)(?=\\W|$)(.)");
		String mentionScheme = String.format("%s/?%s=", Defs.MENTIONS_SCHEMA, Defs.PARAM_UID);
	    Linkify.addLinks(holder.noti_message, mentionsPattern, mentionScheme, new MatchFilter(){
			public boolean acceptMatch(CharSequence s, int start, int end) {
				return s.charAt(end-1) !='.';
			}
	    }, new TransformFilter(){
			public String transformUrl(Matcher match, String url) {
				return match.group(1);
			}
	    });
	    */
		
		return rowView;
	}
	
	@Override  
    public boolean areAllItemsEnabled() {  
        return false;  
    }  
      
    @Override
    public boolean isEnabled(int position) {  
        return false;  
    }  
    
	static class ViewHolder{ 
		ImageView  from_avatar;
		TextView   from_name;
	    TextView   from_level_num;
        TextView   noti_message;
        TextView   noti_time;
        Button   noti_reply_comment;
        Button   noti_reply_message;
        ImageView  from_level_icon;
        LinearLayout  noti_pic_two_wrap;
        LinearLayout  noti_pic_three_wrap;
        ImageView noti_pic_two_1;
        ImageView noti_pic_two_2;
        ImageView noti_pic_three_1;
        ImageView noti_pic_three_2;
        ImageView noti_pic_one;
        ImageView class_icon;
        ImageView class_store_img;
        TextView  class_des;
        TextView  class_store_name;
        LinearLayout wrap1;
        LinearLayout wrap2;
        Button      btn_receive;
        Button      btn_refuse;
        TextView    none_message;
        TextView    noti_at_store;
    } 
	
	private Handler mHandler = new Handler()
    {
		 
        public void handleMessage(Message msg)
        {
       	 super.handleMessage(msg);
            if(errorHelper.CommonCode(msg.what)){
            switch (msg.what)
             {
                case REPLY_SUCCESS:
           	        Toast.makeText(context, "reply successful~", Toast.LENGTH_SHORT).show();
           	        break;
                case ALREADY_SEND:
		 		    Toast.makeText(context, "already send!", Toast.LENGTH_SHORT).show();
		 			break;
		 		case ALREADY_FRIEND:
		 			mListener.deleteNode(nowPosition);
		 			Toast.makeText(context, "already friend!", Toast.LENGTH_SHORT).show();
		 			deleteNode();
		 			break;
		 		case ADD_FRIEND_SUCCESS:
		 			mListener.deleteNode(nowPosition);
		 			Toast.makeText(context, "Has been friend!", Toast.LENGTH_SHORT).show();
		 			deleteNode();
		 			break;
		 		case CAN_NOT_SEND:
		 			Toast.makeText(context, "can't send to this user", Toast.LENGTH_SHORT).show();
		 			break;
		 		case USER_NOT_FOUND:
		 			Toast.makeText(context, "user not found", Toast.LENGTH_SHORT).show();
		 			break;
		 		case MESSAGE_REPLY_SUCCESS:
		 			Toast.makeText(context, "message send successful", Toast.LENGTH_SHORT).show();
		 			break;
		 		case COMMENT_REPLY_SUCCESS:
		 			//delete
		 			mListener.deleteNode(nowPosition);
		 			Toast.makeText(context, "reply successful....", Toast.LENGTH_SHORT).show();
		 			deleteNode();
		 			break;
              }
           }
        }
    };
    String[] class_name_array = {
  		  "Bronze","Bronze","Sliver","Gold","Diamond","Vip","Vvip"
  	};
    Integer[] class_image_array = {
    		R.drawable.ic_class_bronze,
			R.drawable.ic_class_bronze,
			R.drawable.ic_class_silver,
			R.drawable.ic_class_gold,
			R.drawable.ic_class_diamond,
			R.drawable.ic_class_vip,
			R.drawable.ic_class_vvip,
		};
    
    public void sendReply(final int type, final String message){
		new Thread(){
			public void run(){
			   int return_code = 0;
			   if(type == TYPE_MESSAGE){
				   return_code = APIManager.sharedInstance().sendMessage(message_reply_username,message); 
				   if(return_code == 1000){
					   return_code = MESSAGE_REPLY_SUCCESS;
				   }
			   }else{
				   return_code = APIManager.sharedInstance().addComment(2,comment_feed_id,message);
				   if(return_code == 1000){
					   return_code = COMMENT_REPLY_SUCCESS;
				   }
			   }
			   Message msg = mHandler.obtainMessage();
			   msg.what = return_code;
			   mHandler.sendMessage(msg);
			}
		}.start();
	}
    
    public void deleteNode(){
    	new Thread(){
    		public void run(){
    			APIManager.sharedInstance().notiDelete(nowNotId);
    		}
    	}.start();
    }

}