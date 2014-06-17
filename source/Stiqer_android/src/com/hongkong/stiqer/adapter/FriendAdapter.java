package com.hongkong.stiqer.adapter;

import java.util.List;

import com.hongkong.stiqer.R;
import com.hongkong.stiqer.entity.Friend;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 联系人列表适配器。
 * 
 * @author guolin
 */

public class FriendAdapter extends ArrayAdapter<Friend> {

	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;

	/**
	 * 字母表分组工具
	 */
	private SectionIndexer mIndexer;

	public FriendAdapter(Context context, int textViewResourceId, List<Friend> objects) {
		super(context, textViewResourceId, objects);
		resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend contact = getItem(position);
		LinearLayout layout = null;
		if (convertView == null) {
			layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		TextView friend_level = (TextView) layout.findViewById(R.id.friend_level);
		TextView name = (TextView) layout.findViewById(R.id.friend_name);
		ImageView avatar = (ImageView) layout.findViewById(R.id.friend_avatar);
		LinearLayout sortKeyLayout = (LinearLayout) layout.findViewById(R.id.sort_key_layout);
		TextView sortKey = (TextView) layout.findViewById(R.id.sort_key);
		name.setText(contact.getUsername());
		friend_level.setText(""+contact.getUser_level());
		Picasso.with(getContext()).load(contact.getProfile_img()).into(avatar);
		sortKeyLayout.setVisibility(View.GONE);
        /*
		int section = mIndexer.getSectionForPosition(position);
		if (position == mIndexer.getPositionForSection(section)) {
			sortKey.setText(contact.getUser_index());
			sortKeyLayout.setVisibility(View.VISIBLE);
		} else {
			sortKeyLayout.setVisibility(View.GONE);
		}*/
		return layout;
	}

	/**
	 * 给当前适配器传入一个分组工具。
	 * 
	 * @param indexer
	 */
	public void setIndexer(SectionIndexer indexer) {
		mIndexer = indexer;
	}

}
