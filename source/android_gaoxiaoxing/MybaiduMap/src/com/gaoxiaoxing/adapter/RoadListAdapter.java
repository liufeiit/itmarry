package com.gaoxiaoxing.adapter;

import java.util.List;

import com.gaoxiaoxing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RoadListAdapter extends BaseAdapter {
	private Context context;
	private List roadMsg;

	public RoadListAdapter(Context con,List list) {
		this.context=con;
		this.roadMsg = list;
	}

	@Override
	public int getCount() {
		return roadMsg.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if (roadMsg != null) {
			return roadMsg.get(position);
		} else
			return null;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view==null)
			view = View.inflate(context, R.layout.list_item, null);
		return view;
	}

}
