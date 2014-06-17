package com.hongkong.stiqer.adapter;

import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.FriendStatus;
import com.hongkong.stiqer.entity.SearchFriend;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.AddFriendListener;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFriendAdapter extends BaseAdapter{
	List<SearchFriend> mData;
	private LayoutInflater mInflater;
	private Context context;
	ViewHolder holder;
	FriendStatus friendStatus;
    AddFriendListener listener;
	
	public SearchFriendAdapter(Context context, List<SearchFriend> Sf, AddFriendListener alistener){
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = Sf;
		this.listener = alistener;
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
	        rowView = mInflater.inflate(R.layout.list_item_searchfriend, null);
	        ViewHolder holder = new ViewHolder();
	        holder.user_avatar = (ImageView) rowView.findViewById(R.id.sf_user_avater);
	        holder.user_name = (TextView) rowView.findViewById(R.id.sf_user_name);
	        holder.user_level = (TextView) rowView.findViewById(R.id.sf_user_level);
	        holder.btn_add = (Button) rowView.findViewById(R.id.sf_add_btn);
	        holder.btn_added = (Button) rowView.findViewById(R.id.sf_added_btn);
	        rowView.setTag(holder);
	    }
	
	    final ViewHolder holder = (ViewHolder) rowView.getTag();
	    holder.btn_add.setOnTouchListener(Util.TouchDark);
	    holder.btn_added.setOnTouchListener(Util.TouchDark);
	    
		holder.user_name.setText(mData.get(position).getUsername());
	    
		holder.user_level.setText(""+mData.get(position).getUser_level());
		Picasso.with(context).load(mData.get(position).getProfile_img()).into(holder.user_avatar);
		
		if(mData.get(position).getFriend_state() == -1){
			 holder.btn_add.setVisibility(View.VISIBLE);
		}else{
			 holder.btn_add.setVisibility(View.INVISIBLE);
		}
		
		if(mData.get(position).getFriend_state() == 0){
			 holder.btn_added.setVisibility(View.VISIBLE);
		}else{
			 holder.btn_added.setVisibility(View.INVISIBLE);
		}
		
		holder.user_avatar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent t = new Intent(context,UserActivity.class);
				t.putExtra("username", mData.get(position).getUsername());
				context.startActivity(t);
			}
		});
		
		holder.btn_add.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
			    new Thread(){
			    	public void run(){
			    	    friendStatus = APIManager.sharedInstance().addFriend(mData.get(position).getUsername());
			    		Message msg = mHandler.obtainMessage();
			    		Bundle bundle = new Bundle();
			    		bundle.putInt("position", position);
			            msg.setData(bundle);
			            msg.what = friendStatus.getError_code();
			            mHandler.sendMessage(msg);
			    	}
			    }.start();
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
		ImageView  user_avatar;
		TextView   user_name;
		TextView   user_level;
		Button     btn_add;
		Button     btn_added;
    } 
	
	private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
       	 super.handleMessage(msg);
            switch (msg.what)
            {
            
                case 1021:
                	Toast.makeText(context, "already sent", Toast.LENGTH_SHORT).show();
                	break;
                case 1022:
                	Toast.makeText(context, "already Friend", Toast.LENGTH_SHORT).show();
                	break;
                case 1000:
                	mData.get(msg.getData().getInt("position")).setFriend_state(0);
                	listener.sendRequest(msg.getData().getInt("position"));
                break;
                
            }
        };
    };
	
}