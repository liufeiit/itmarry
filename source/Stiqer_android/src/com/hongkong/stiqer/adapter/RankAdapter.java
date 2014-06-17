package com.hongkong.stiqer.adapter;


import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.entity.Comment;
import com.hongkong.stiqer.entity.Rank;
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

public class RankAdapter extends BaseAdapter{
	List<Rank> mData;
	private LayoutInflater mInflater;
	private Context context;
	ViewHolder holder;
	CustomDialog bigImageDialog;
	
	public RankAdapter(Context context, List<Rank> rank){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = rank;
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
	        rowView = mInflater.inflate(R.layout.list_item_rank, null);
	        ViewHolder holder = new ViewHolder();
	        holder.user_image = (ImageView) rowView.findViewById(R.id.user_image);
	        holder.class_image = (ImageView) rowView.findViewById(R.id.class_image);
	        holder.rank_num = (TextView) rowView.findViewById(R.id.rank_num);
	        rowView.setTag(holder);
	    }
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    
		Picasso.with(context).load(mData.get(position).getProfile_img()).into(holder.user_image);
		holder.class_image.setImageResource(level_image_array[mData.get(position).getLevel()]);
		holder.rank_num.setText(""+mData.get(position).getRank());	
		holder.class_image.setAlpha(150);
		
		holder.user_image.setOnTouchListener(Util.TouchDark);
		holder.user_image.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
        	   Intent t = new Intent(context,UserActivity.class);
        	   t.putExtra("username",mData.get(position).getUsername());
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
		ImageView  user_image;
		ImageView  class_image;
        TextView   rank_num;
    } 
	
	Integer[] level_image_array = {
			R.drawable.ic_class_bronze_feed,
			R.drawable.ic_class_bronze_feed,
			R.drawable.ic_class_sliver_feed,
			R.drawable.ic_class_gold_feed,
			R.drawable.ic_class_diamond_feed,
			R.drawable.ic_class_vip_feed,
			R.drawable.ic_class_vvip_feed,
		};
	
}