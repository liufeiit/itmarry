package com.hongkong.stiqer.adapter;


import com.hongkong.stiqer.R;
import com.hongkong.stiqer.utils.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AchievementAdapter extends BaseAdapter{
	private Context mContext;
	private String[] mps;
	int type;
	public AchievementAdapter(Context context,String achieveArray, int type) {
		mContext = context;
		mps = achieveArray.split(",");
		this.type = type;
	}

	public int getCount() { 
		return mps.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView image = new ImageView(mContext);
		image.setImageResource(achievements[Integer.parseInt(mps[position])]);
		image.setAdjustViewBounds(true);
		if(type == 1){
			image.setLayoutParams(new Gallery.LayoutParams(Util.SCREENWIDTH/9, Util.SCREENWIDTH/9));
		}else{
			image.setLayoutParams(new GridView.LayoutParams(Util.SCREENWIDTH/4, Util.SCREENWIDTH/4));
		}
		
		return image;
	}
	
	Integer[] achievements = {
		R.drawable.achieve1,R.drawable.achieve1,
		R.drawable.achieve2,R.drawable.achieve3,
		R.drawable.achieve4,R.drawable.achieve5,
		R.drawable.achieve6,R.drawable.achieve7,
		R.drawable.achieve8,R.drawable.achieve9,
		R.drawable.achieve10,R.drawable.achieve11,
		R.drawable.achieve12,R.drawable.achieve13,
		R.drawable.achieve14,R.drawable.achieve15,
		R.drawable.achieve16,
	};
	
}
