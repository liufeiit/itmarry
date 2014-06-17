package com.hongkong.stiqer.adapter;

import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.adapter.CommentListAdapter.ViewHolder;
import com.hongkong.stiqer.entity.AddFriend;
import com.hongkong.stiqer.entity.FriendStatus;
import com.hongkong.stiqer.https.APIManager;
import com.hongkong.stiqer.ui.UserActivity;
import com.hongkong.stiqer.utils.Util;
import com.hongkong.stiqer.widget.AddFriendListener;
import com.hongkong.stiqer.widget.CustomDialog;
import com.squareup.picasso.Picasso;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendAdapter extends ArrayAdapter<AddFriend> {
	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;
	/**
	 * 字母表分组工具
	 */
	private SectionIndexer mIndexer;
	private LayoutInflater mInflater;
	private Context mContext;
    CustomDialog  processingDialog;
	private AddFriendListener listener;
	private int type;
	ContentResolver resolver;
	FriendStatus friendStatus;
    
	public AddFriendAdapter(Context context, int textViewResourceId, List<AddFriend> objects, AddFriendListener Alistener,int t) {
		super(context, textViewResourceId, objects);
		resource = textViewResourceId;
		this.mInflater = LayoutInflater.from(context);
		mContext = context;
		this.listener = Alistener;
		this.type = t;
		resolver = mContext.getContentResolver(); 
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
	
		final AddFriend contact = getItem(position);
		
		View rowView = convertView;
		 if (rowView == null) {
			 rowView = mInflater.inflate(resource, null);
			 ViewHolder holder = new ViewHolder();
			 holder.icon_level = (ImageView) rowView.findViewById(R.id.icon_level);
			 holder.username = (TextView) rowView.findViewById(R.id.af_username);
			 holder.name = (TextView) rowView.findViewById(R.id.af_user_name);
			 holder.avatar = (ImageView) rowView.findViewById(R.id.af_user_avater);
			 holder.sortKeyLayout = (LinearLayout) rowView.findViewById(R.id.af_sort_key_layout);
			 holder.sortKey = (TextView) rowView.findViewById(R.id.af_sort_key);
			 holder.invite_btn = (Button) rowView.findViewById(R.id.invite_btn);
			 holder.add_btn = (Button) rowView.findViewById(R.id.addfriend_btn);
			 holder.invited_btn = (Button) rowView.findViewById(R.id.invited_btn);
			 holder.added_btn = (Button) rowView.findViewById(R.id.added_btn);
			 rowView.setTag(holder);
		 }
		 final ViewHolder holder = (ViewHolder) rowView.getTag();
		 //button问题
		 //4 没有注册，邀请   3 邀请过  -1 陌生人，添加朋友，0 为 Request sended 1 无显示  无操作
		 if(contact.getStatus() == 4){
			 holder.invite_btn.setVisibility(View.VISIBLE);
		 }else{
			 holder.invite_btn.setVisibility(View.INVISIBLE);
		 }
		 
		 if(contact.getStatus() == 3){
			 holder.invited_btn.setVisibility(View.VISIBLE);
		 }else{
			 holder.invited_btn.setVisibility(View.INVISIBLE);
		 }
		 
		 if(contact.getStatus() == -1){
			 holder.add_btn.setVisibility(View.VISIBLE);
		 }else{
			 holder.add_btn.setVisibility(View.INVISIBLE);
		 }
		 
		 if(contact.getStatus() == 0){
			 holder.added_btn.setVisibility(View.VISIBLE);
		 }else{
			 holder.added_btn.setVisibility(View.INVISIBLE);
		 }
		if(contact.getUsername() != null){
			holder.username.setText("("+contact.getUsername()+")");
			holder.username.setVisibility(View.VISIBLE);
			holder.icon_level.setVisibility(View.VISIBLE);
		}else{
			holder.username.setVisibility(View.GONE);
			holder.icon_level.setVisibility(View.GONE);
		}
		
		holder.name.setText(contact.getName());
		switch(type){
		case 1:
			Picasso.with(mContext).load(contact.getAvatar()).into(holder.avatar);
			break;
		case 2:
			Picasso.with(mContext).load(contact.getAvatar()).into(holder.avatar);
			break;	
		case 3:
			holder.avatar.setVisibility(View.GONE);
			break;	
		}
		
		holder.added_btn.setOnTouchListener(Util.TouchDark);
		holder.invite_btn.setOnTouchListener(Util.TouchDark);
		holder.invited_btn.setOnTouchListener(Util.TouchDark);
		holder.add_btn.setOnTouchListener(Util.TouchDark);
		
		holder.username.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			   Intent t = new Intent(mContext,UserActivity.class);
			   t.putExtra("username", contact.getUsername());
			   mContext.startActivity(t);
			}
		});
		
		holder.add_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
			    new Thread(){
			    	public void run(){
			    		friendStatus = APIManager.sharedInstance().addFriend(contact.getName());
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
		
		holder.invite_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			   listener.sendInvite(position,type,contact.getRawfid(),contact.getName());
			   contact.setStatus(3);
			}
		});
		holder.sortKeyLayout.setVisibility(View.GONE);
		return rowView;
	}
	/**
	 * 给当前适配器传入一个分组工具。
	 * 
	 * @param indexer
	 */
	public void setIndexer(SectionIndexer indexer) {
		mIndexer = indexer;
	}
	
	static class ViewHolder{
		TextView   username;
		TextView   name;
		ImageView  avatar;
		ImageView  icon_level;
		TextView    sortKey;
		LinearLayout sortKeyLayout;
		Button invite_btn,add_btn,invited_btn,added_btn;
	}
	
	private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
       	 super.handleMessage(msg);
            switch (msg.what)
            {
                case 1021:
                	getItem(msg.getData().getInt("position")).setStatus(0);
                	listener.sendRequest(msg.getData().getInt("position"));
                	break;
                case 1022:
                	Toast.makeText(mContext, "already friends", 1000).show();
                	break;
                case 1000:
                	getItem(msg.getData().getInt("position")).setStatus(0);
                	listener.sendRequest(msg.getData().getInt("position"));
                break;
            }
        };
    };
}
