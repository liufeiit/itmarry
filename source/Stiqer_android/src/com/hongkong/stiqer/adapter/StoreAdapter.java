package com.hongkong.stiqer.adapter;

import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Storelist;
import com.hongkong.stiqer.ui.MapActivity;
import com.hongkong.stiqer.utils.Util;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StoreAdapter extends BaseAdapter{
    Context   mContext;
    private LayoutInflater  mInflater;
    LinearLayout            layout_outer,layout_inner,des_first_wrap,des_second_wrap;
    List<Storelist>         mData;
    TextView                store_name,distance,des_first,des_second;
    ImageView               store_image,store_distance_icon;
        
    public StoreAdapter(Context context, List<Storelist> sl){
    	this.mContext = context;
    	this.mInflater = LayoutInflater.from(context);
    	this.mData = sl;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
    	View rowView = convertView;
    	switch(position%4){
    	case 1:
    		rowView = mInflater.inflate(R.layout.list_item_store2, null);
    		break;
    	case 2:
    		rowView = mInflater.inflate(R.layout.list_item_store3, null);
    		break;
    	case 3:
    		rowView = mInflater.inflate(R.layout.list_item_store4, null);
    		break;
    	case 0:
    		rowView = mInflater.inflate(R.layout.list_item_store1, null);
    		break;
    	}
    	
    	store_distance_icon = (ImageView) rowView.findViewById(R.id.store_distance_icon);
    	store_name  = (TextView) rowView.findViewById(R.id.store_name_list);
    	distance = (TextView) rowView.findViewById(R.id.store_distance_list);
    	store_image = (ImageView) rowView.findViewById(R.id.store_image_list);
    	des_first_wrap = (LinearLayout) rowView.findViewById(R.id.des_first_wrap);
    	des_second_wrap = (LinearLayout) rowView.findViewById(R.id.des_second_wrap);
    	des_first = (TextView) rowView.findViewById(R.id.des_first);
    	des_second = (TextView) rowView.findViewById(R.id.des_second);

    	store_name.setText(mData.get(position).getStore_name());
    	if(mData.get(position).getLatitude() >0){
    	   distance.setText(mData.get(position).getDistance());  
    	   store_distance_icon.setVisibility(View.VISIBLE);
    	}
    	  	
    	if(!TextUtils.isEmpty(mData.get(position).getStore_image_url())){
    		Picasso.with(mContext).load(mData.get(position).getStore_image_url()).into(store_image);
    	}
    	
    	if(mData.get(position).getDes_first()!= null){
    		if(!mData.get(position).getDes_first().equals("")){
    			des_first_wrap.setVisibility(View.VISIBLE);
        		des_first.setText(mData.get(position).getDes_first());
    		}
    	}
    	
    	if(mData.get(position).getDes_second()!= null){
    		if(!mData.get(position).getDes_second().equals("")){
    			des_second_wrap.setVisibility(View.VISIBLE);
        		des_second.setText(mData.get(position).getDes_second());
    		}
    	}

    	distance.setOnTouchListener(Util.TouchDark);
    	distance.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent t = new Intent(mContext,MapActivity.class);
				t.putExtra("longitude", mData.get(position).getLongitude());
				t.putExtra("latitude", mData.get(position).getLatitude());
				mContext.startActivity(t);
			}
    	});
    	store_distance_icon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent t = new Intent(mContext,MapActivity.class);
				t.putExtra("longitude", mData.get(position).getLongitude());
				t.putExtra("latitude", mData.get(position).getLatitude());
				mContext.startActivity(t);
			}
    	});
	    return rowView;
    }

}
