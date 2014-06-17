package com.ze.familydayverlenovo.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.ze.commontool.LoadImageMgr;
import com.ze.commontool.NetHelper;
import com.ze.commontool.PublicInfo;
import com.ze.familydayverlenovo.MainActivity;
import com.ze.familydayverlenovo.R;
import com.ze.familydayverlenovo.SearchUserActivity;
import com.ze.familydayverlenovo.R.id;
import com.ze.familydayverlenovo.userinfo.Componet;
import com.ze.familydayverlenovo.userinfo.UserInfoManager;


import android.R.raw;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyGridViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;
	public static final String flag[] = {
		"icon","label","count",
	};
	
	public static final int 		ids[] = {
		R.id.item_gridview_img,
		R.id.item_gridview_tv,
		R.id.item_gridview_count,
		R.id.main_gridview_item
	};
	private 	final int STRING_ID[] = {	
			R.string.main_pic,
			R.string.main_diary,
			R.string.main_activity,
			R.string.main_video,
			R.string.main_dialog,
			R.string.main_zone,
			R.string.main_family,
			R.string.main_send,
			R.string.main_set,
			};
	private 	final int ICON_ID[] = {	
			R.drawable.main_icon_pic,
			R.drawable.main_icon_blog,
			R.drawable.main_icon_activity,
			R.drawable.main_icon_video,
			R.drawable.main_icon_msg,
			R.drawable.main_icon_space,
			R.drawable.main_icon_family,
			R.drawable.main_icon_send,
			R.drawable.main_icon_setting,};
	private int 		layout;
//	private int      layout_diver;
	private Context mContext;
//	private final int TYPE_ITEM_0 = 0;
//	private final int TYPE_ITEM_1 = 1;
	public MyGridViewAdapter(Context context, List<Map<String, Object>>list, int mainGridviewItem) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.layout = R.layout.main_gridview_item;
//		layout_diver = R.layout.addmember_listview_diver;
		mContext = context;
	}
//	@Override
//	public int getViewTypeCount() {
//		// TODO Auto-generated method stub
//		return 2;
//	}
//	@Override
//	public int getItemViewType(int position) {
//		// TODO Auto-generated method stub
//		return (Integer)((Map<String, Object>)getItem(position)).get("item_type");
//	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 :list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder ;
		final int f_pos = position;
		if( convertView == null )
		{
			convertView = mInflater.inflate(layout, null);
			holder = new Holder();
			holder.icon = (ImageView)convertView.findViewById(ids[0]);
			holder.label = (TextView)convertView.findViewById(ids[1]);
			holder.count = (TextView)convertView.findViewById(ids[2]);
			holder.body = convertView.findViewById(ids[3]);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		int len = 0;
		if( PublicInfo.SCREEN_H < PublicInfo.SCREEN_W )
		{
			// 横屏
		len = PublicInfo.SCREEN_H;
		}else {
			// 竖屏
		 len = PublicInfo.SCREEN_W ;
		}
		
		LayoutParams params = new LayoutParams((len-80)/3,( len - 80 )/3);
		convertView.setLayoutParams(params);
		if( position % 2 ==0 )
		{
			holder.body.setBackgroundResource(R.drawable.icon_bg1);
		}else {
			holder.body.setBackgroundResource(R.drawable.icon_bg2);
		}
//		android.widget.RelativeLayout.LayoutParams itemLayoutParams = (android.widget.RelativeLayout.LayoutParams) holder.icon.getLayoutParams();
//		item
		holder.icon.setImageResource(ICON_ID[position]);
		holder.label.setText( STRING_ID[position] );
		if ( (Integer)list.get(position).get(flag[2]) > 9 )
		{
			holder.count.setText( "N" );
		}else if( (Integer)list.get(position).get(flag[2]) == 0 )
		{
			holder.count.setText( "" );
		}else
		{
			holder.count.setText( "" + (Integer)list.get(position).get(flag[2]) );
		}
		
		
		return convertView;
	}
	
	public OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	static class Holder
	{
		View 			body;
		ImageView	icon;
		TextView 	label;
		TextView 	count;
	}
}
