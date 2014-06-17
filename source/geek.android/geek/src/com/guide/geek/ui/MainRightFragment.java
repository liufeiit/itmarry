package com.guide.geek.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guide.geek.R;
import com.guide.geek.app.BaseFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainRightFragment extends BaseFragment implements AdapterView.OnItemClickListener{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.my_main_right, null);
	}

	private SlidingMenu menu;
	private ListView listView;
	private MyAdapter adapter;
	
	public MainRightFragment(SlidingMenu menu){
		this.menu = menu;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.initViews();
	}
	private void initViews(){
		View parent = this.getView();
		this.adapter = new MyAdapter(context, this.getCities());
		
		this.listView = (ListView)parent.findViewById(R.id.list);
		this.listView.setAdapter(this.adapter);
//		this.listView.setOnItemClickListener(this);
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		MyHolder holder = (MyHolder)convertView.getTag();
		holder.city.selected = true;
//		holder.img.setImageResource(R.drawable.ic_select_normal);
		MyHolder oldHolder = this.adapter.getSelectedHolder();
		if(oldHolder != null){
			if(oldHolder.city.selected){
//				oldHolder.img.setImageResource(R.drawable.ic_select_empty);
				oldHolder.city.selected = false;
			}
		}
		this.adapter.setSelectedHolder(holder);
		this.adapter.notifyDataSetChanged();
		// TODO 这里还要做其它的事情
		this.menu.toggle();
	}
	
	private class City{
		private String cityName;
		private String webUrl;
		private boolean selected;
		private City(String cityName, String webUrl){
			this.cityName = cityName;
			this.webUrl = webUrl;
		}
		private City(String cityName, String webUrl, boolean selected){
			this.cityName = cityName;
			this.webUrl = webUrl;
			this.selected = selected;
		}
	}
	private class MyHolder{
		private TextView title;
		private ImageView img;
		private City city;
	}
	private class MyAdapter extends BaseAdapter{
		private MyHolder selectedHolder;
		private Context context;
		private ArrayList<City> cities;
		private LayoutInflater inflater;
		private MyAdapter(Context context, ArrayList<City> cities){
			this.context = context;
			this.cities = cities;
			if(this.cities == null){
				this.cities = new ArrayList<City>();
			}
			this.inflater = LayoutInflater.from(this.context);
		}
		@Override
		public int getCount() {
			return this.cities.size();
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder holder;
			if(convertView == null){
				convertView = this.inflater.inflate(R.layout.my_main_inner_list_for_cities, null);
				holder = new MyHolder();
				holder.title = (TextView)convertView.findViewById(R.id.inner_title);
				holder.img = (ImageView)convertView.findViewById(R.id.inner_img);
				convertView.setTag(holder);
				convertView.setOnTouchListener(onTouchListener);
				convertView.setOnClickListener(onClickListener);
			}else{
				holder = (MyHolder)convertView.getTag();
			}
			
			City city = this.cities.get(position);
			holder.title.setText(city.cityName);
			if(city.selected){
				holder.img.setImageResource(R.drawable.ic_select_normal);
				selectedHolder = holder;
			}else{
				holder.img.setImageResource(R.drawable.ic_select_empty);
			}
			holder.city = city;
			return convertView;
		}
		private void setSelectedHolder(MyHolder selectedHolder){
			this.selectedHolder = selectedHolder;
		}
		private MyHolder getSelectedHolder(){
			return this.selectedHolder;
		}
		private View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyHolder holder = (MyHolder)v.getTag();
				holder.city.selected = true;
				holder.img.setImageResource(R.drawable.ic_select_normal);
				MyHolder oldHolder = getSelectedHolder();
				if(oldHolder != null && holder != oldHolder){
					if(oldHolder.city.selected){
						oldHolder.img.setImageResource(R.drawable.ic_select_empty);
						oldHolder.city.selected = false;
					}
				}
				setSelectedHolder(holder);
//				this.notifyDataSetChanged();
				// TODO 这里还要做其它的事情
				menu.toggle();
			}};
		private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(event.getPointerCount() == 1){
							((MyHolder)v.getTag()).img.setImageResource(R.drawable.ic_select_pressed);
						}
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_MASK:
						if(event.getPointerCount() == 1){
							MyHolder holder = (MyHolder)v.getTag();
							if(holder.city.selected){
								holder.img.setImageResource(R.drawable.ic_select_normal);
							}else{
								holder.img.setImageResource(R.drawable.ic_select_empty);
							}
						}
					}
				return false;
			}};
	}
	
	public ArrayList<City> getCities(){
		ArrayList<City> list = new ArrayList<City>();
		list.add(new City("莞城区", "", true));
		list.add(new City("东城区", ""));
		list.add(new City("南城区", ""));
		list.add(new City("万江区", ""));
		list.add(new City("石碣区", ""));
		list.add(new City("石龙镇", ""));
		list.add(new City("茶山镇", ""));
		list.add(new City("石排镇", ""));
		list.add(new City("企石镇", ""));
		list.add(new City("横沥镇", ""));
		return list;
	}
}
