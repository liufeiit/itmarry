package com.hongkong.stiqer.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Feed;
import com.hongkong.stiqer.entity.FeedLike;
import com.hongkong.stiqer.entity.SUser;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.MainActivity;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.CacheHelper;
import com.hongkong.stiqer.utils.Defs;
import com.hongkong.stiqer.utils.ErrorCodeHelper;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.AdapterListener;
import com.hongkong.stiqer.widget.CustomDialog;
import com.squareup.picasso.Picasso;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedListAdapter extends BaseAdapter{
	public static final int    ALREADY_LIKE = 1017;
	List<Feed> mData;
	private LayoutInflater mInflater;
	private Context context;
	ViewHolder holder;
	private AdapterListener listener;
	FeedLike  feedLike;
	CustomDialog  bigImageDialog;
	String        myUsername;
	CacheHelper   cacheHelper;
	MainActivity  mainActivity;
	
	public FeedListAdapter(Context context, List<Feed> mfeed,AdapterListener listener){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = mfeed;
		this.listener = listener;
		cacheHelper = new CacheHelper(context);
		SUser loginUser = cacheHelper.GetUser();
		this.myUsername = loginUser.getUsername();
		this.mainActivity = (MainActivity) context;
	}

	@Override
	public int getCount() {
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
	    	rowView = mInflater.inflate(R.layout.list_item_feed, null);
	        ViewHolder holder = new ViewHolder();
	        holder.user_avatar = (ImageView) rowView.findViewById(R.id.feed_avatar);
			holder.username = (TextView) rowView.findViewById(R.id.feed_username);
			holder.description = (TextView) rowView.findViewById(R.id.feed_des);
			holder.store_name = (TextView) rowView.findViewById(R.id.feed_store_name);
			holder.level_image = (ImageView) rowView.findViewById(R.id.feed_level_image);
			holder.level_name = (TextView) rowView.findViewById(R.id.feed_level_name);
			holder.feed_msg = (TextView) rowView.findViewById(R.id.feed_msg);
			holder.feed_icons_wrap = (RelativeLayout) rowView.findViewById(R.id.feed_icons_wrap);
			holder.feed_at_users = (TextView) rowView.findViewById(R.id.feed_at_users);
			
			holder.stiqer_num = (TextView) rowView.findViewById(R.id.feed_stiqer_num);
			holder.egg_num = (TextView) rowView.findViewById(R.id.feed_egg_num);
			holder.like_num = (TextView) rowView.findViewById(R.id.feed_like_num);
			holder.comment_num = (TextView) rowView.findViewById(R.id.feed_comment_num);
			holder.like_btn = (ImageView) rowView.findViewById(R.id.feed_like_btn);
			holder.comment_btn = (ImageView) rowView.findViewById(R.id.feed_comment_btn);
			holder.feed_stiqer_icon = (ImageView) rowView.findViewById(R.id.feed_stiqer_icon);
			holder.feed_egg_icon = (ImageView) rowView.findViewById(R.id.feed_egg_icon);
			
			holder.image1 = (ImageView) rowView.findViewById(R.id.feed_image1);
			holder.image2 = (ImageView) rowView.findViewById(R.id.feed_image2);
			holder.image3 = (ImageView) rowView.findViewById(R.id.feed_image3);
			holder.image4 = (ImageView) rowView.findViewById(R.id.feed_image4);
		
			holder.image_wrap = (LinearLayout) rowView.findViewById(R.id.image_two_wrap);
			holder.created_time = (TextView) rowView.findViewById(R.id.feed_created_time);
			
			holder.feed_header_wrap1 = (LinearLayout) rowView.findViewById(R.id.feed_header_wrap1);
			holder.feed_header_wrap2 = (LinearLayout) rowView.findViewById(R.id.feed_header_wrap2);
			holder.feed_header_wrap3 = (LinearLayout) rowView.findViewById(R.id.feed_header_wrap3);
			
			holder.feed_des2 = (TextView) rowView.findViewById(R.id.feed_des2);
			holder.feed_username2 = (TextView) rowView.findViewById(R.id.feed_username2);
			holder.feed_store_name2 = (TextView) rowView.findViewById(R.id.feed_store_name2);
			
	      rowView.setTag(holder);
	    }
	    
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    final int feed_type = mData.get(position).getFeed_type();
	    
	    if(mData.get(position).getLevel()>0){
			//holder.level_image.setImageResource(level_image_array[mData.get(position).getLevel()]);
			//holder.level_name.setText(level_name_array[mData.get(position).getLevel()]);
		}
		
	    if(feed_type == 1 || feed_type == 3 || feed_type == 5){
	    	if(mData.get(position).getStore_name().equals("")){
		    	holder.store_name.setVisibility(View.GONE);
		    }else{
		    	holder.store_name.setVisibility(View.VISIBLE);
		    	holder.store_name.setText(mData.get(position).getStore_name());
		    }
	    	holder.username.setText(mData.get(position).getUsername());
			holder.description.setText(mData.get(position).getDescription());
			
			holder.username.setVisibility(View.VISIBLE);
			holder.description.setVisibility(View.VISIBLE);
			holder.feed_header_wrap1.setVisibility(View.VISIBLE);
			holder.feed_header_wrap2.setVisibility(View.GONE);
			holder.feed_header_wrap3.setVisibility(View.GONE);
			holder.feed_des2.setVisibility(View.GONE);
			holder.feed_username2.setVisibility(View.GONE);
			holder.feed_store_name2.setVisibility(View.GONE);
			
	    }else{
	    	holder.feed_des2.setText(mData.get(position).getDescription());
			holder.feed_username2.setText(mData.get(position).getUsername());
			holder.feed_store_name2.setText(mData.get(position).getStore_name());;
	    	
	    	holder.feed_header_wrap1.setVisibility(View.GONE);
	    	holder.feed_header_wrap2.setVisibility(View.VISIBLE);
	    	holder.feed_header_wrap3.setVisibility(View.VISIBLE);
	    	holder.feed_des2.setVisibility(View.VISIBLE);
			holder.feed_username2.setVisibility(View.VISIBLE);
			holder.feed_store_name2.setVisibility(View.VISIBLE);
			holder.store_name.setVisibility(View.GONE);
			holder.username.setVisibility(View.GONE);
			holder.description.setVisibility(View.GONE);
			
	    }
	    
		if(mData.get(position).getStiqer_num()>0){
			holder.feed_stiqer_icon.setVisibility(View.VISIBLE);
			holder.stiqer_num.setText(""+mData.get(position).getStiqer_num());
			holder.stiqer_num.setVisibility(View.VISIBLE);
		}else{
			holder.feed_stiqer_icon.setVisibility(View.GONE);
			holder.stiqer_num.setVisibility(View.GONE);
		}
		
		if(!mData.get(position).getFeed_at_users().equals("")){
			holder.feed_at_users.setVisibility(View.VISIBLE);
			String str = mData.get(position).getFeed_at_users().replaceAll("@", ", @")+"  ";
			holder.feed_at_users.setText(str.substring(2, str.length()));
			extractMention2Link(holder.feed_at_users);
			stripUnderlines(holder.feed_at_users);
		}else{
			holder.feed_at_users.setVisibility(View.GONE);
		}
		
		if(!mData.get(position).getFeed_msg().equals("") && feed_type != 5 && feed_type !=6){
			holder.feed_msg.setVisibility(View.VISIBLE);
			holder.feed_msg.setText(mData.get(position).getFeed_msg());
		}else{
			holder.feed_msg.setVisibility(View.GONE);
		}
		
		if(mData.get(position).getEgg_num()>0){
			holder.feed_egg_icon.setVisibility(View.VISIBLE);
			holder.egg_num.setVisibility(View.VISIBLE);
			holder.egg_num.setText(""+mData.get(position).getEgg_num());
		}else{
			holder.feed_egg_icon.setVisibility(View.GONE);
			holder.egg_num.setVisibility(View.GONE);
		}
		
		if(mData.get(position).getComment_num() == -1){
			holder.comment_btn.setVisibility(View.GONE);
			holder.comment_num.setVisibility(View.GONE);
		}else{
			holder.comment_btn.setVisibility(View.VISIBLE);
			holder.comment_num.setText(""+mData.get(position).getComment_num());
		}
		
		if(feed_type == 3){
			holder.feed_icons_wrap.setVisibility(View.GONE);
		}else{
			holder.feed_icons_wrap.setVisibility(View.VISIBLE);
		}
		
		if(mData.get(position).getLike_num() == -1){
			holder.like_btn.setVisibility(View.GONE);
			holder.like_num.setVisibility(View.GONE);
		}else{
			holder.like_btn.setVisibility(View.VISIBLE);
			holder.like_num.setText(""+mData.get(position).getLike_num());
		}
		
	    Picasso.with(context).load(mData.get(position).getProfile_img()).into(holder.user_avatar);

		
		holder.like_num.setText(""+mData.get(position).getLike_num());
		holder.created_time.setText(Util.transTime(mData.get(position).getTime()));
		
		if(!mData.get(position).getFeed_img_url_1().equals("") && mData.get(position).getFeed_img_url_2().equals("") && feed_type !=6){
			Picasso.with(context).load(mData.get(position).getFeed_img_url_1()).into(holder.image2);
			holder.image2.setVisibility(View.VISIBLE);
		}else{
			holder.image2.setVisibility(View.GONE);
		}
		
		if(feed_type == 1){
			holder.image1.setVisibility(View.VISIBLE);
			holder.image1.setImageResource(R.drawable.photo_checkin_feed);
		}else{
			holder.image1.setVisibility(View.GONE);
		}
		
		if(!mData.get(position).getFeed_img_url_2().equals("") || feed_type == 5 || feed_type == 6){
			if(feed_type == 1){
				Picasso.with(context).load(mData.get(position).getFeed_img_url_1()).into(holder.image3);
				Picasso.with(context).load(mData.get(position).getFeed_img_url_2()).into(holder.image4);
			}else if(feed_type == 5){
				//level up
				Picasso.with(context).load(R.drawable.ic_level).into(holder.image3);
				Picasso.with(context).load(R.drawable.logo).into(holder.image4);
			}else if(feed_type == 6){
				//class up
				Picasso.with(context).load(level_image_array[mData.get(position).getLevel()]).into(holder.image3);
				Picasso.with(context).load(mData.get(position).getFeed_img_url_1()).into(holder.image4);
			}
			holder.image_wrap.setVisibility(View.VISIBLE);
		}else{
			holder.image_wrap.setVisibility(View.GONE);
		}
		
		holder.image2.setOnTouchListener(Util.TouchDark);
		holder.image2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				bigImageDialog = CustomDialog.createBigImageDialog(context,mData.get(position).getFeed_img_url_1(), Util.changeImageUrl(mData.get(position).getFeed_img_url_1(), 1, 3));
				bigImageDialog.show();
			}
		});
		
		holder.image3.setOnTouchListener(Util.TouchDark);
		holder.image3.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(feed_type == 1){
					bigImageDialog = CustomDialog.createBigImageDialog(context,mData.get(position).getFeed_img_url_1(), Util.changeImageUrl(mData.get(position).getFeed_img_url_1(), 2, 3));
					bigImageDialog.show();
				}
				
			}
		});
		
		holder.image4.setOnTouchListener(Util.TouchDark);
		holder.image4.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(feed_type == 1){
				  bigImageDialog = CustomDialog.createBigImageDialog(context,mData.get(position).getFeed_img_url_2(), Util.changeImageUrl(mData.get(position).getFeed_img_url_2(), 2, 3));
				  bigImageDialog.show();
				}
				if(feed_type == 6){
					Intent t = new Intent(context,StoreDetailActivity.class);
					t.putExtra("store_id", mData.get(position).getStore_id());
					t.putExtra("store_name", mData.get(position).getStore_name());
					context.startActivity(t);
				}
			}
		});
		
		
		holder.like_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 new Thread(){
			    	 public void run(){
			    		//if(mData.get(position).getIs_like() == 1){
			    		//	feedLike = APIManager.sharedInstance().deleteFeedLike(mData.get(position).getFeed_id());
			    		//}else{
			    		feedLike = APIManager.sharedInstance().addFeedLike(mData.get(position).getFeed_id());
			    		//}
			    		Message msg = mHandler.obtainMessage();
			    		Bundle bundle = new Bundle();
			    		bundle.putInt("position", position);
			    		bundle.putInt("type",1);
			            msg.what = feedLike.getError_code();
			            msg.setData(bundle);
			            mHandler.sendMessage(msg);
			    	 }
			      }.start();
			}
		});
		holder.comment_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				listener.OpenComment(mData.get(position).getFeed_id(), position, mData.get(position).getComment_num());
			}
		});
		
		holder.user_avatar.setOnTouchListener(Util.TouchDark);
		holder.user_avatar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(mData.get(position).getUsername().equals(myUsername)){
					mainActivity.selectFromFragment(0);
				}else{
					Intent t = new Intent(context,UserActivity.class);
					t.putExtra("username", mData.get(position).getUsername());
					context.startActivity(t);
				}
				
			}
		});
		
		holder.username.setOnTouchListener(Util.TouchDark);
		holder.username.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
               if(mData.get(position).getUsername().equals(myUsername)){
            	   mainActivity.selectFromFragment(0);
			   }else{
				  Intent t = new Intent(context,UserActivity.class);
				  t.putExtra("username", mData.get(position).getUsername());
				  context.startActivity(t);
			   }	
		   }
		});
		
		holder.store_name.setOnTouchListener(Util.TouchDark);
		holder.store_name.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,StoreDetailActivity.class);
				t.putExtra("store_id", mData.get(position).getStore_id());
				t.putExtra("store_name", mData.get(position).getStore_name());
				context.startActivity(t);
			}
		});
		
		holder.feed_store_name2.setOnTouchListener(Util.TouchDark);
		holder.feed_store_name2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,StoreDetailActivity.class);
				t.putExtra("store_id", mData.get(position).getStore_id());
				t.putExtra("store_name", mData.get(position).getStore_name());
				context.startActivity(t);
			}
		});
		
		return rowView;
	}
	
	public void extractMention2Link(TextView  v){
		v.setAutoLinkMask(0);
		Pattern mentionsPattern = Pattern.compile("(@)([\\w\\s]+)(\\s{2}|,)");
		String mentionsScheme = String.format("%s/?%s=", Defs.MENTIONS_SCHEMA, Defs.PARAM_UID);
		Linkify.addLinks(v, mentionsPattern, mentionsScheme, new MatchFilter() {
		@Override
		public boolean acceptMatch(CharSequence s, int start, int end) {
		      return s.charAt(end-1) !='.';
		    }
		  }, new TransformFilter() {
		  @Override
		  public String transformUrl(Matcher match, String url)
		  {
		      return match.group(2); 
		   }
	    });
	}
	
	private void stripUnderlines(TextView textView) {
        Spannable s = (Spannable)textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
            StyleSpan span2 = new StyleSpan(android.graphics.Typeface.BOLD);
            s.setSpan(span2, start, end, 0);
        }
        
        textView.setText(s);
    }
	
	private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @SuppressLint("ResourceAsColor")
		@Override 
	    public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            int myColor = context.getResources().getColor(R.color.subhead);
            ds.setColor(myColor);
        }
    }

	private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
       	 super.handleMessage(msg);
       	    ErrorCodeHelper errorHelper = new ErrorCodeHelper(context);
       	    errorHelper.CommonCode(msg.what);
            switch (msg.what)
            {
                case 1000:
                	int position = msg.getData().getInt("position");
                	Log.e("Stiqer",position+";"+feedLike.getFeed_like_num()+";"+msg.getData().getInt("type"));
                	listener.sendData(position,feedLike.getFeed_like_num(),msg.getData().getInt("type"));
                	if(mData.get(position).getIs_like() == 1){
                		mData.get(position).setIs_like(0);
                	}else{
                		mData.get(position).setIs_like(1);
                	}
                	mData.get(position).setLike_num(feedLike.getFeed_like_num());
                	break;
                	
                case ALREADY_LIKE:
                	 final int positios = msg.getData().getInt("position");
                	 new Thread(){
    			    	 public void run(){
    			    		feedLike = APIManager.sharedInstance().deleteFeedLike(mData.get(positios).getFeed_id());
    			    		Message msg = mHandler.obtainMessage();
    			    		Bundle bundle = new Bundle();
    			    		bundle.putInt("position", positios);
    			    		bundle.putInt("type",1);
    			            msg.what = feedLike.getError_code();
    			            msg.setData(bundle);
    			            mHandler.sendMessage(msg);
    			    	 }
    			      }.start();
                	break;
            }
        };
    };
	@Override  
    public boolean areAllItemsEnabled() {  
        return false;  
    }  
      
    @Override
    public boolean isEnabled(int position) {  
        return false;  
    }  
    
	static class ViewHolder{ 
		ImageView  user_avatar;
        TextView   username;
        TextView   description;
        TextView   store_name;
        ImageView  level_image;
        TextView   level_name;
        TextView   stiqer_num;
        TextView   egg_num;
        TextView   like_num;
        TextView   comment_num;
        ImageView  image1; //第二行左变第一个
        ImageView  image2; //中间（如果只有一个）
        ImageView  image3; //中间第一个
        ImageView  image4; //中间第二个
        TextView   created_time;
        LinearLayout image_wrap;
        ImageView  like_btn;
        ImageView  comment_btn;
        ImageView  feed_stiqer_icon;
        ImageView  feed_egg_icon;
        TextView   feed_msg;
        TextView   feed_at_users;
        TextView   feed_username2;
        TextView   feed_store_name2;
        TextView   feed_des2;
        RelativeLayout feed_icons_wrap;
        LinearLayout feed_header_wrap1;
        LinearLayout feed_header_wrap2;
        LinearLayout feed_header_wrap3;
    } 
	
	Integer[] level_image_array = {
			R.drawable.ic_class_bronze,
			R.drawable.ic_class_bronze,
			R.drawable.ic_class_silver,
			R.drawable.ic_class_gold,
			R.drawable.ic_class_diamond,
			R.drawable.ic_class_vip,
			R.drawable.ic_class_vvip,
		};
		
		Integer[] store_type_array = {
			R.drawable.ic_store_drinks_feed,
			R.drawable.ic_store_drinks_feed,
			R.drawable.ic_store_dessert_feed,
			R.drawable.ic_store_food_feed,
		};
		
		String[] level_name_array = {
		  "Bronze","Bronze","Sliver","Gold","Diamond","Vip","Vvip"
		};
	
}