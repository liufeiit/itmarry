package com.dyj.adapter;

import java.util.ArrayList;

import com.dyj.R;
import com.dyj.adapter.RwggAdapter.ViewHolder;
import com.dyj.db.bean.Rw;
import com.dyj.dialog.CustomerDialog;
import com.dyj.tabs.PutRwTab;
import com.dyj.tabs.TabMap;
import com.dyj.untils.GetTimeUtil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRwAdapter extends BaseAdapter {

	private ArrayList<Rw> mData;
	private LayoutInflater mInflater;
	private Context context;
	

	public MyRwAdapter(Context context, ArrayList<Rw> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.vlist_rwgg, null);
			holder.title = (TextView) convertView.findViewById(R.id.text_title);
			holder.text_cjsj = (TextView) convertView
					.findViewById(R.id.text_cjsj);
			holder.text_rw_dm = (TextView) convertView
					.findViewById(R.id.text_rw_dm);
			holder.btn_lbs = (Button) convertView.findViewById(R.id.btn_lbs);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Rw bean = mData.get(position);
		
		holder.text_rw_dm.setText(bean.getRw_dm());
		holder.text_cjsj.setText("发布时间：" + GetTimeUtil.getTime(bean.getCjsj())
				+"\n完成期限:"+bean.getWcqx());
		if (null != bean.getYhbh()) {
			holder.title.setText("用户编号：" + bean.getYhbh());
		} 
		if (null != bean.getRw_lx()&&!("".equals(bean.getRw_lx()))) {
			holder.title.setText(holder.title.getText() + "\n任务类型："
					+ bean.getRw_lx());
		}
		if (null != bean.getYhmc()&&!("".equals(bean.getYhmc()))) {
			holder.title.setText(holder.title.getText() + "\n用户名称："
					+ bean.getYhmc());
		}
		if (null != bean.getGddw()&&!("".equals(bean.getGddw()))) {
			holder.title.setText(holder.title.getText() + "\n供电单位："
					+ bean.getGddw());
		}
		if (null != bean.getAzdz()&&!("".equals(bean.getAzdz()))) {
			holder.title.setText(holder.title.getText() + "\n安装地址："
					+ bean.getAzdz());
		}
		if (null != bean.getZddz()&&!("".equals(bean.getZddz()))) {
			holder.title.setText(holder.title.getText() + "\n终端地址："
					+ bean.getZddz());
		}
		if (null != bean.getZcbh()&&!("".equals(bean.getZcbh()))) {
			holder.title.setText(holder.title.getText() + "\n资产编号："
					+ bean.getZcbh());
		}
		if (null != bean.getDbzch()&&!("".equals(bean.getDbzch()))) {
			holder.title.setText(holder.title.getText() + "\n电表资产编号："
					+ bean.getDbzch());
		}
		if (null != bean.getZbmklx()&&!("".equals(bean.getZbmklx()))) {
			holder.title.setText(holder.title.getText() + "\n载波模块类型："
					+ bean.getZbmklx());
		}
		if (null != bean.getLxr()&&!("".equals(bean.getLxr()))) {
			holder.title.setText(holder.title.getText() + "\n联系人："
					+ bean.getLxr());
		}
		if (null != bean.getDh()&&!("".equals(bean.getDh()))) {
			holder.title.setText(holder.title.getText() + "\n联系电话："
					+ bean.getDh());
		}
		if (null != bean.getDh1()&&!("".equals(bean.getDh1()))) {
			holder.title.setText(holder.title.getText() + "\n联系电话1："
					+ bean.getDh1());
		}
		if (null != bean.getGzqk()&&!("".equals(bean.getGzqk()))) {
			holder.title.setText(holder.title.getText() + "\n故障原因："
					+ bean.getGzqk());
		}
		if (null != bean.getFbr_name()&&!("".equals(bean.getFbr_name()))) {
			holder.title.setText(holder.title.getText() + "\n发布人："
					+ bean.getFbr_name());
		}
		if (null != bean.getFbr_tel()&&!("".equals(bean.getFbr_tel()))) {
			holder.title.setText(holder.title.getText() + "\n联系电话："
					+ bean.getFbr_tel());
		}
		if (null != bean.getRwzt_mc()&&!("".equals(bean.getRwzt_mc()))) {
			holder.title.setText(holder.title.getText() + "\n任务状态："
					+ bean.getRwzt_mc());
		}
		holder.btn_lbs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, TabMap.class);
				intent.putExtra("jd",  mData.get(position).getCjb_jd());
				intent.putExtra("wd",  mData.get(position).getCjd_wd());
				context.startActivity(intent);
			}
		});
		holder.title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final CustomerDialog dialog = new CustomerDialog(context);
				dialog.setMessage("提示：\n确认提交该条任务吗？");
				dialog.setText_btn_ok("继续");
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Log.d("Rw_dm is:",bean.getRw_dm());
						Intent intent = new Intent(context, PutRwTab.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 刷新
						intent.putExtra("rw_dm", bean.getRw_dm());
						context.startActivity(intent);
						dialog.dismiss();
					}
				};
				dialog.setOkListener(listener);
				dialog.show();
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView text_cjsj;
		public TextView text_rw_dm;
		public Button btn_lbs;
	}

}
