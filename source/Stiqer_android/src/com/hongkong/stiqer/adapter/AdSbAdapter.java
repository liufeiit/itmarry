package com.hongkong.stiqer.adapter;

import java.util.List;
import java.util.Map;

import com.hongkong.stiqer.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdSbAdapter extends BaseAdapter{
	List<Map<String, Object>> mData;
	private LayoutInflater mInflater;
	private Context context;
	ViewHolder holder;
	
	public AdSbAdapter(Context context, List<Map<String, Object>> data){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
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
	        rowView = mInflater.inflate(R.layout.list_item_atsb_dialog, null);
	        ViewHolder holder = new ViewHolder();
	        holder.atsb_avatar = (ImageView) rowView.findViewById(R.id.atsb_avatar);
			holder.atsb_name = (TextView) rowView.findViewById(R.id.atsb_name);
	        rowView.setTag(holder);
	    }
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.atsb_name.setText((String) mData.get(position).get("atsb_name"));
		//Picasso.with(context).load((String) mData.get(position).get("atsb_avatar")).resize(60,60).into(holder.atsb_avatar);
		return rowView;
	}
   
	static class ViewHolder{ 
		ImageView  atsb_avatar;
        TextView   atsb_name;
    } 
	

	
}