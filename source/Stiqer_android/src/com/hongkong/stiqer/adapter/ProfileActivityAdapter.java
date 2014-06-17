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
import com.hongkong.stiqer.adapter.FeedListAdapter.ViewHolder;
import com.hongkong.stiqer.entity.Pactivity;
import com.hongkong.stiqer.ui.StoreDetailActivity;
import com.hongkong.stiqer.utils.Util;

public class ProfileActivityAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
    private List<Pactivity>   mData;
    private int resource;
    
	public ProfileActivityAdapter(Context c, List<Pactivity> pactivity, int resource){
		this.context = c;
		this.mInflater = LayoutInflater.from(c);
		this.mData = pactivity;
		this.resource = resource;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		if(resource == R.layout.list_item_pl_activity && mData.size()>6){
			return 6;
		}

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
		if(rowView == null){
			rowView = mInflater.inflate(resource, null);
			ViewHolder holder = new ViewHolder();
			holder.store_name = (TextView) rowView.findViewById(R.id.ac_store_name);
			holder.ac_time = (TextView) rowView.findViewById(R.id.ac_time);
			holder.ac_des = (TextView) rowView.findViewById(R.id.ac_des);
			rowView.setTag(holder);
		}
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    holder.ac_time.setText(Util.transTime(mData.get(position).getTime()));
	    String act_type =  mData.get(position).getAct_type();
	    
	    holder.store_name.setText(mData.get(position).getStore_name());
	    if(act_type.equals("checkin")){
	    	holder.ac_des.setText("Was at");
	    	
	    }else if(act_type.equals("purchase")){
	    	holder.ac_des.setText("Made a purchase at");
	    	if(mData.get(position).getStore_name().length()>15){
		    	holder.store_name.setText(mData.get(position).getStore_name().substring(0, 13)+"..");
		    }
	    }else if(act_type.equals("levelup")){
	    	holder.ac_des.setText("Became level "+mData.get(position).getNew_level());
	    	
	    }else if(act_type.equals("complete mission")){
	    	holder.ac_des.setText("Redeemed a reward at");
	    	if(mData.get(position).getStore_name().length()>18){
		    	holder.store_name.setText(mData.get(position).getStore_name().substring(0, 16)+"..");
		    }
	    }else if(act_type.equals("classup")){
	    	holder.ac_des.setText("Became a "+mData.get(position).getNew_class()+" class at");
	    	if(mData.get(position).getStore_name().length()>14){
		    	holder.store_name.setText(mData.get(position).getStore_name().substring(0, 12)+"..");
		    }
	    }else{
	    	holder.ac_des.setText("Redeemed a promotion at");
	    	if(mData.get(position).getStore_name().length()>15){
		    	holder.store_name.setText(mData.get(position).getStore_name().substring(0, 13)+"..");
		    }
	    }
	    
	    
	    
	    holder.store_name.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,StoreDetailActivity.class);
				t.putExtra("store_id", mData.get(position).getStore_id());
				t.putExtra("store_name", mData.get(position).getStore_name());
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
    	TextView  store_name;
    	TextView  ac_time;
    	TextView  ac_des;
    }
	
}
