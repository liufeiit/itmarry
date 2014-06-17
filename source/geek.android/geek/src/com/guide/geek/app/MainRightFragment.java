package com.guide.geek.app;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainRightFragment extends BaseFragment implements AdapterView.OnItemClickListener{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.my_main_right1, null);
	}

	private SlidingMenu menu;
	private ListView listView;
	private MenuAdapter adapter;
	
	public MainRightFragment(SlidingMenu menu){
		this.menu = menu;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.initViews();
	}
	private void initViews(){
		View parent = this.getView();
		this.adapter = new MenuAdapter (context, this.getMenus());		
		this.listView = (ListView) parent.findViewById (R.id.list);
		this.listView.setAdapter(this.adapter);
		this.listView.setOnItemClickListener(this);
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
		MyHolder holder = (MyHolder)convertView.getTag();
		holder.menu.selected = true;
//		holder.img.setImageResource(R.drawable.ic_select_normal);
		MyHolder oldHolder = this.adapter.getSelectedHolder();
		if(oldHolder != null){
			if(oldHolder.menu.selected){
//				oldHolder.img.setImageResource(R.drawable.ic_select_empty);
				oldHolder.menu.selected = false;
			}
		}
		this.adapter.setSelectedHolder(holder);
		this.adapter.notifyDataSetChanged();
		// TODO 这里还要做其它的事情
		this.menu.toggle();
	}
	
	private class Menu{
		private Drawable iconDrawable;
		private String title;
		private boolean selected;
		private Menu(Drawable iconDrawable, String title){
			this.iconDrawable = iconDrawable;
			this.title = title;
		}
		private Menu(Drawable iconDrawable, String title, boolean selected){
			this.iconDrawable = iconDrawable;
			this.title = title;
			this.selected = selected;
		}
	}
	private class MyHolder{
		private TextView title;
		private ImageView icon;
		private Menu menu;
	}
	private class MenuAdapter extends BaseAdapter{
		private MyHolder selectedHolder;
		private Context context;
		private ArrayList<Menu> menus;
		private LayoutInflater inflater;
		private MenuAdapter(Context context, ArrayList<Menu> menus){
			this.context = context;
			this.menus = menus;
			if(this.menus == null){
				this.menus = new ArrayList<Menu>();
			}
			this.inflater = LayoutInflater.from(this.context);
		}
		@Override
		public int getCount() {
			return this.menus.size();
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
				convertView = this.inflater.inflate(R.layout.menu_item, null);
				holder = new MyHolder();
				holder.title = (TextView)convertView.findViewById(R.id.item_title);
				holder.icon = (ImageView)convertView.findViewById(R.id.item_icon);
				convertView.setTag(holder);
				convertView.setOnTouchListener(onTouchListener);
				convertView.setOnClickListener(onClickListener);
			}else{
				holder = (MyHolder) convertView.getTag();
			}
			
			Menu menu = this.menus.get(position);
			holder.title.setText(menu.title);
			if(menu.selected){
				holder.icon.setImageResource(R.drawable.ic_select_normal);
				selectedHolder = holder;
			}else{
				holder.icon.setImageResource(R.drawable.ic_select_empty);
			}
			holder.menu = menu;
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
				holder.menu.selected = true;
				holder.icon.setImageResource(R.drawable.ic_select_normal);
				MyHolder oldHolder = getSelectedHolder();
				if(oldHolder != null && holder != oldHolder){
					if(oldHolder.menu.selected){
						oldHolder.icon.setImageResource(R.drawable.ic_select_empty);
						oldHolder.menu.selected = false;
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
							((MyHolder)v.getTag()).icon.setImageResource(R.drawable.ic_select_pressed);
						}
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_MASK:
						if(event.getPointerCount() == 1){
							MyHolder holder = (MyHolder)v.getTag();
							if(holder.menu.selected){
								holder.icon.setImageResource(R.drawable.ic_select_normal);
							}else{
								holder.icon.setImageResource(R.drawable.ic_select_empty);
							}
						}
					}
				return false;
			}};
	}
	
	public ArrayList<Menu> getMenus(){
		
		ArrayList<Menu> list = new ArrayList<Menu>();

		Resources res = this.context.getResources();
		Drawable icon = res.getDrawable(R.drawable.ic_select_normal);
		list.add(new Menu(icon, "莞城区",true));
		list.add(new Menu(icon, "东城区"));
		return list;
	}
}
