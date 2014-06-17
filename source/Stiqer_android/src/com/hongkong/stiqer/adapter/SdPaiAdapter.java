package com.hongkong.stiqer.adapter;

import java.util.List;

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

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.CommentListAdapter.ViewHolder;
import com.hongkong.stiqer.entity.Rank;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.Util;
import com.squareup.picasso.Picasso;

public class SdPaiAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context        context;
	private List<Rank>     mData;

	public SdPaiAdapter(Context c,List<Rank> data){
		this.context = c;
		this.mData = data;
		this.mInflater = LayoutInflater.from(c);
	}
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
		return 0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.list_item_sd_pai, null);
	        ViewHolder holder = new ViewHolder();
	        holder.user_avatar = (ImageView) rowView.findViewById(R.id.pai_user_avatar);
	        rowView.setTag(holder);
		}
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		Picasso.with(context).load(mData.get(position).getProfile_img()).resize(Util.SCREENWIDTH/9,Util.SCREENWIDTH/9).into(holder.user_avatar);
		holder.user_avatar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,UserActivity.class);
				t.putExtra("username", mData.get(position).getUsername());
				context.startActivity(t);
			}
		});
		return rowView;
	}
	
	static class ViewHolder{ 
		ImageView  user_avatar;
    } 
	
	
}
