package com.hongkong.stiqer.adapter;


import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.CheckIn;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.CustomDialog;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CheckInAdapter extends BaseAdapter{
	private Context mContext;
	private List<CheckIn> mData;
	private CustomDialog bigImageDialog;
	
	public CheckInAdapter(Context context,List<CheckIn> list) {
		mContext = context;
		mData = list;
	}

	public int getCount() { 
		return mData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView image = new ImageView(mContext);
		Picasso.with(mContext).load(mData.get(position).getImg_url()).resize(Util.SCREENWIDTH/9,Util.SCREENWIDTH/9).into(image);
		image.setAdjustViewBounds(true);
		image.setLayoutParams(new Gallery.LayoutParams(Util.SCREENWIDTH/9, Util.SCREENWIDTH/9));
		
		image.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				bigImageDialog = CustomDialog.createBigImageDialog(mContext,mData.get(position).getImg_url(), Util.changeImageUrl(mData.get(position).getImg_url(), 2, 3));
	 			bigImageDialog.show();
			}
		});
		
		return image;
	}
}
