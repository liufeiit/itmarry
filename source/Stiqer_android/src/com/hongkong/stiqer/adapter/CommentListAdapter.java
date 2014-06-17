package com.hongkong.stiqer.adapter;


import java.util.ArrayList;
import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Comment;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.AdapterListener;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter{
	List<Comment> mData;
	private LayoutInflater mInflater;
	private Context context;
	ViewHolder holder;
	
	public CommentListAdapter(Context context, List<Comment> mComment){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = mComment;
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
	        rowView = mInflater.inflate(R.layout.list_item_comment, null);
	        ViewHolder holder = new ViewHolder();
	        holder.user_avatar = (ImageView) rowView.findViewById(R.id.comment_avatar);
	        holder.username = (TextView) rowView.findViewById(R.id.comment_from_name);
			holder.comment_con = (TextView) rowView.findViewById(R.id.comment_con);
			holder.user_level = (TextView) rowView.findViewById(R.id.comment_user_level);
			holder.post_time = (TextView) rowView.findViewById(R.id.comment_post_time);
	        rowView.setTag(holder);
	    }
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.comment_con.setText(mData.get(position).getCmt_message());
		holder.username.setText(mData.get(position).getCmt_username());
		holder.user_level.setText(""+mData.get(position).getCmt_user_level());
		Picasso.with(context).load(mData.get(position).getCmt_user_img()).resize(60,60).into(holder.user_avatar);
		if(!mData.get(position).getCmt_time().equals("")){
			holder.post_time.setText(Util.transTime(mData.get(position).getCmt_time()));
		}
		holder.user_avatar.setOnTouchListener(Util.TouchDark);
		holder.username.setOnTouchListener(Util.TouchDark);
		
		holder.user_avatar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,UserActivity.class);
				t.putExtra("username", mData.get(position).getCmt_username());
				context.startActivity(t);
			}
		});
		
		holder.username.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,UserActivity.class);
				t.putExtra("username", mData.get(position).getCmt_username());
				context.startActivity(t);
			}
		});
		
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
		ImageView  user_avatar;
        TextView   comment_con;
        TextView   user_level;
        TextView   username;
        TextView   post_time;
    } 
}