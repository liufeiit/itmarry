package com.dyj.adapter;

import java.util.ArrayList;

import com.dyj.R;
import com.dyj.adapter.MyRwAdapter.ViewHolder;
import com.dyj.bean.beanMessage;
import com.dyj.db.bean.Rw;
import com.dyj.untils.GetTimeUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class messageAdapter extends BaseAdapter {

	private ArrayList<beanMessage> mData;
	private LayoutInflater mInflater;
	private Context context;

	public messageAdapter(Context context, ArrayList<beanMessage> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.mData = mData;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.message_listview_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		final beanMessage bean = mData.get(position);
		holder.title.setText(bean.getTitle());
		holder.dateline.setText("发布时间："+GetTimeUtil.getTime(bean.getDateline()));
		return convertView;
	}
	public final class ViewHolder {
		public TextView title;
		public TextView dateline;
	}
	

}
