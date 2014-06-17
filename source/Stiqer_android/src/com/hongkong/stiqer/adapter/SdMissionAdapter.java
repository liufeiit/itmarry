package com.hongkong.stiqer.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.CaptureActivity;
import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Mission;
import com.squareup.picasso.Picasso;

public class SdMissionAdapter extends BaseAdapter {
	
	private final static int  REQUEST_CODE_MISSION = 201;
	private LayoutInflater mInflater;
	private Context context;
	List<Mission>   mData;
	Activity        act;
	
	public SdMissionAdapter(Context c, List<Mission> data, Activity activity){
		this.context = c;
		this.mInflater = LayoutInflater.from(c);
		this.mData = data;
		this.act = activity;
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

		rowView = mInflater.inflate(R.layout.list_item_sd_mission, null);
		ImageView mission_image = (ImageView) rowView.findViewById(R.id.mission_image);
		TextView mission_name = (TextView) rowView.findViewById(R.id.mission_name);
		TextView mission_description = (TextView) rowView.findViewById(R.id.mission_description);
		LinearLayout dot_wrap = (LinearLayout) rowView.findViewById(R.id.dot_wrap);
		
        final ImageView redeem_btn = (ImageView) rowView.findViewById(R.id.redeem_btn);
		mission_name.setText(mData.get(position).getMission_name());
		mission_description.setText(mData.get(position).getMission_des());
		Picasso.with(context).load(mData.get(position).getMission_img()).into(mission_image);
		
        final int total_dot,complete_dot; 
        complete_dot = mData.get(position).getStiqer_now();
        total_dot = mData.get(position).getStiqer_all();
        
		for(int i=0; i<complete_dot; i++){
			if(i <= total_dot){
				ImageView mImageView = new ImageView(context);  
				mImageView.setImageResource(R.drawable.green_dot_full); 
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(18, 18); 
				params.setMargins(0, 0, 6, 0); 
				mImageView.setLayoutParams(params);
				dot_wrap.addView(mImageView);
			}
			
		}
		
		for(int i=0; i<(total_dot-complete_dot); i++){
			ImageView mImageView = new ImageView(context);  
			mImageView.setImageResource(R.drawable.green_dot_empty); 
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(18, 18); 
			params.setMargins(0, 0, 6, 0);
			mImageView.setLayoutParams(params);
			dot_wrap.addView(mImageView);
		}
		
		redeem_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
		    	if(total_dot > complete_dot){
		    		Toast.makeText(context, "You are not able to redeem the reward yet." , Toast.LENGTH_SHORT).show();
		    	}else{
		    		redeem_btn.setImageResource(R.drawable.redeem_click);
					Intent intent = new Intent(context, CaptureActivity.class);
			    	intent.putExtra("pid" , mData.get(position).getMission_id());
			    	act.startActivityForResult(intent,REQUEST_CODE_MISSION);
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
}
