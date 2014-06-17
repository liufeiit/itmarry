package com.gaoxiaoxing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gaoxiaoxing.R;
import com.gaoxiaoxing.model.SchoolInfo;

public class SelectPoiAdapter extends BaseAdapter {

	private ArrayList<SchoolInfo> data;
	private Context context;

	public SelectPoiAdapter(Context context, ArrayList<SchoolInfo> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(context)
					.inflate(R.layout.list_poi, null);
			TextView tv = (TextView) view;
			tv.setText(data.get(position).getTitle());
			return tv;
		}
		TextView t = (TextView) view;
		t.setText(data.get(position).getTitle());
		return view;
	}
}
