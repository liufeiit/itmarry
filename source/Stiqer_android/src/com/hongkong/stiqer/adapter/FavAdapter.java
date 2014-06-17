package com.hongkong.stiqer.adapter;

import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Fav;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavAdapter extends BaseAdapter{
    Context   mContext;
    private LayoutInflater  mInflater;
    RelativeLayout          fav_layout_wrap;
    List<Fav>               mData;
    TextView                store_name;
    ImageView               store_image;
    int                     type;
    
    public FavAdapter(Context context, List<Fav> list,int type){
    	this.mContext = context;
    	this.mInflater = LayoutInflater.from(context);
    	this.mData = list;
    	this.type = type;
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

    public View getView(int position, View convertView, ViewGroup parent) {
    	View rowView = convertView;
	    if (rowView == null) {
	        rowView = mInflater.inflate(R.layout.list_item_fav, null);
	        ViewHolder holder = new ViewHolder();
	        holder.store_image = (ImageView) rowView.findViewById(R.id.fav_image);
	        holder.store_type = (ImageView) rowView.findViewById(R.id.store_type);
			holder.store_name = (TextView) rowView.findViewById(R.id.fav_store_name);
			holder.description = (TextView) rowView.findViewById(R.id.prom_des);
			holder.fav_layout_wrap = (RelativeLayout) rowView.findViewById(R.id.fav_layout_wrap);
	        rowView.setTag(holder);
	    }
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    if(position%2 == 1){
	    	holder.fav_layout_wrap.setBackgroundColor(Color.parseColor("#FFF0F9F8"));
		}else{
			holder.fav_layout_wrap.setBackgroundColor(Color.parseColor("#FFE2F2F2"));
		}
	    if(type == 1){
	    	holder.store_type.setVisibility(View.VISIBLE);
	    	holder.store_type.setImageResource(store_type_array[mData.get(position).getStore_type()]);
	    }else{
	    	holder.description.setVisibility(View.VISIBLE);
	    	holder.description.setText(mData.get(position).getPromo_des());
	    }
		holder.store_name.setText(mData.get(position).getStore_name());
		if(!mData.get(position).getStore_img().equals("")){
			Picasso.with(mContext).load(mData.get(position).getStore_img()).into(holder.store_image);
		}
	    return rowView;
    }

    static class ViewHolder{
		ImageView  store_image;
		ImageView  store_type;
		TextView   description;
        TextView   store_name;
        RelativeLayout fav_layout_wrap;
    }

    Integer[] store_type_array = {
		R.drawable.ic_store_drinks_feed,
		R.drawable.ic_store_drinks_feed,
		R.drawable.ic_store_dessert_feed,
		R.drawable.ic_store_food_feed,
	};
}
