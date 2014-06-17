package com.dyj.adapter;

import java.util.ArrayList;

import com.dyj.R;
import com.dyj.adapter.RwggAdapter.ViewHolder;
import com.dyj.bean.beanLsRw;
import com.dyj.bean.beanRwgg;
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
import android.widget.Toast;

public class LsRwAdapter extends BaseAdapter {

	private ArrayList<beanLsRw> mData;
	private LayoutInflater mInflater;
	private Context context;
	private beanLsRw bean = null;

	public LsRwAdapter(Context context, ArrayList<beanLsRw> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
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
			convertView = mInflater.inflate(R.layout.vlist_lsrw, null);
			holder.title = (TextView) convertView.findViewById(R.id.text_title);
			holder.text_cjsj = (TextView) convertView
					.findViewById(R.id.text_cjsj);
			holder.text_rw_dm = (TextView) convertView
					.findViewById(R.id.text_rw_dm);
			holder.text_pjjg = (TextView) convertView
					.findViewById(R.id.text_pjjg);
			holder.btn_lbs = (Button) convertView.findViewById(R.id.btn_lbs);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		bean = mData.get(position);
		
		holder.text_rw_dm.setText(bean.getRw_dm());
		holder.text_cjsj.setText("�ύʱ�䣺"
				+ GetTimeUtil.getTime(Integer.parseInt(bean.getScsj())));
		holder.text_pjjg.setVisibility(View.VISIBLE);
		holder.text_pjjg.setText("���۽����"+bean.getIs_hg());
		holder.title.setText("");
		if (null != bean.getRw_lx()) {
			holder.title.setText(holder.title.getText() + "\n�������ͣ�"
					+ bean.getRw_lx());
		}
		if (null != bean.getYhmc()) {
			holder.title.setText(holder.title.getText() + "\n�û����ƣ�"
					+ bean.getYhmc());
		}
		if (null != bean.getGddw()) {
			holder.title.setText(holder.title.getText() + "\n���絥λ��"
					+ bean.getGddw());
		}
		if (null != bean.getAzdz()) {
			holder.title.setText(holder.title.getText() + "\n��װ��ַ��"
					+ bean.getAzdz());
		}
		if (null != bean.getZddz()) {
			holder.title.setText(holder.title.getText() + "\n�ն˵�ַ��"
					+ bean.getZddz());
		}
		if (null != bean.getZcbh()) {
			holder.title.setText(holder.title.getText() + "\n�ʲ���ţ�"
					+ bean.getZcbh());
		}
		if(null!=bean.getDbzch()){
			holder.title.setText(holder.title.getText() + "\n����ʲ���ţ�"
					+ bean.getDbzch());
		}
		if (null != bean.getZcbh()) {
			holder.title.setText(holder.title.getText() + "\n�ʲ���ţ�"
					+ bean.getZcbh());
		}
		
		if (null != bean.getZbmklx()) {
			holder.title.setText(holder.title.getText() + "\n�ز�ģ�����ͣ�"
					+ bean.getZbmklx());
		}
		if (null != bean.getLxr()) {
			holder.title.setText(holder.title.getText() + "\n��ϵ�ˣ�"
					+ bean.getLxr());
		}
		if (null != bean.getDh()) {
			holder.title.setText(holder.title.getText() + "\n��ϵ�绰��"
					+ bean.getDh());
		}
		if (null != bean.getDh1()) {
			holder.title.setText(holder.title.getText() + "\n��ϵ�绰1��"
					+ bean.getDh1());
		}
		if (null != bean.getGzqk()) {
			holder.title.setText(holder.title.getText() + "\n����ԭ��"
					+ bean.getGzqk());
		}
		if (null != bean.getFbr_name()) {
			holder.title.setText(holder.title.getText() + "\n�����ˣ�"
					+ bean.getFbr_name());
		}
		if (null != bean.getFbr_tel()) {
			holder.title.setText(holder.title.getText() + "\n��ϵ�绰��"
					+ bean.getFbr_tel());
		}
		if (null != bean.getRwzt_mc()) {
			holder.title.setText(holder.title.getText() + "\n����״̬��"
					+ bean.getRwzt_mc());
		}
		if (null != bean.getNew_zcbh()) {
			holder.title.setText(holder.title.getText() + "\n���ʲ���ţ�"
					+ bean.getNew_zcbh());
		}
		if (null != bean.getOld_jsr()) {
			holder.title.setText(holder.title.getText() + "\n���ʲ������ˣ�"
					+ bean.getOld_jsr());
		}
		if (null != bean.getOld_tel()) {
			holder.title.setText(holder.title.getText() + "\n���ʲ������˵绰��"
					+ bean.getOld_tel());
		}
		if (null != bean.getOld_cfdd()) {
			holder.title.setText(holder.title.getText() + "\n���ʲ���ŵص㣺"
					+ bean.getOld_cfdd());
		}
		if (null != bean.getPjry_mc()) {
			holder.title.setText(holder.title.getText() + "\n�����ˣ�"
					+ bean.getPjry_mc());
		}

		if (null != bean.getPjry_bm_mc()) {
			holder.title.setText(holder.title.getText() + "\n�����˲��ţ�"
					+ bean.getPjry_bm_mc());
		}
		
		holder.btn_lbs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(context, TabMap.class);
				intent.putExtra("jd", mData.get(position).getSg_jd());
				intent.putExtra("wd",  mData.get(position).getSg_wd());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView text_cjsj;
		public TextView text_rw_dm;
		public TextView text_pjjg;
		public Button btn_lbs;
	}

}
