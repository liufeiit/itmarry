package com.qingluan.tcpprint.adapter;


import com.qingluan.tcpprint.ConnectActivity;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyListViewAdapter extends BaseAdapter {
	ArrayList<String[]> Data = new ArrayList<String[]>();
	private Context context ;
	
	public MyListViewAdapter(Context c,List it){
		context = c;
	}
	public void addData(String[] date){
		boolean isSame = false;
		for(String[] some : Data){
			if (some[0].equals(date[0]) && some[1].equals(date[1])){
				isSame = true;
			}
		}
		if (! isSame){
			Data.add(date);
		}
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layoutinflate = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		//LayoutInflater inflater = (LayoutInflater)getLayoutInflater();
		//convertView = 
		return null;
	}

}
