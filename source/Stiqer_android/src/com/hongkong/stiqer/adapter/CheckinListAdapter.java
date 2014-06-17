package com.hongkong.stiqer.adapter;


import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.Comment;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckinListAdapter extends BaseAdapter{
	List<CheckIn> mData;
	private LayoutInflater mInflater;
	private Context context;
	ViewHolder holder;
	int type; //1为usertab， 2 为store tab
	CustomDialog bigImageDialog;
	
	public CheckinListAdapter(Context context, List<CheckIn> Checkin, int type){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = Checkin;
		this.type = type;
	}
	

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
	    if (rowView == null) {
	        rowView = mInflater.inflate(R.layout.list_item_checkin, null);
	        ViewHolder holder = new ViewHolder();
	        holder.checkin_image = (ImageView) rowView.findViewById(R.id.checkin_image);
	        holder.post_image = (ImageView) rowView.findViewById(R.id.post_image);
	        holder.post_time = (TextView) rowView.findViewById(R.id.post_time);
	        rowView.setTag(holder);
	    }
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
		Picasso.with(context).load(mData.get(position).getImg_url()).into(holder.checkin_image);
		if(type==1){
			Picasso.with(context).load(mData.get(position).getImg_store_img()).into(holder.post_image);
		}else{
			Picasso.with(context).load(mData.get(position).getImg_user_img()).into(holder.post_image);
		}
		
		holder.post_time.setText(Util.transTime(mData.get(position).getImg_time()));
		
		holder.checkin_image.setOnTouchListener(Util.TouchDark);
		holder.checkin_image.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			   bigImageDialog = CustomDialog.createBigImageDialog(context,mData.get(position).getImg_url(),Util.changeImageUrl(mData.get(position).getImg_url(), 2,3));
 			   bigImageDialog.show();
			}
		});
		
		holder.post_image.setOnTouchListener(Util.TouchDark);
		holder.post_image.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
               if(type==2){
            	   Intent t = new Intent(context,UserActivity.class);
            	   t.putExtra("username",mData.get(position).getImg_username());
            	   context.startActivity(t);
               }else{
            	   Intent t = new Intent(context,StoreDetailActivity.class);
            	   t.putExtra("store_id",mData.get(position).getImg_store_id());
            	   t.putExtra("store_name",mData.get(position).getImg_store_name());
            	   context.startActivity(t);
               }
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
		ImageView  checkin_image;
		ImageView  post_image;
        TextView   post_time;
    } 
	

	
}