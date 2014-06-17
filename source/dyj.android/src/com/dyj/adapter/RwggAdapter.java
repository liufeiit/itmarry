package com.dyj.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.dyj.R;
import com.dyj.bean.beanRwgg;
import com.dyj.listener.RwggListViewTitleOnClickListener;
import com.dyj.tabs.RwggTab;
import com.dyj.tabs.TabMap;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RwggAdapter extends BaseAdapter {
	private ArrayList<beanRwgg> mData;
	private LayoutInflater mInflater;
	private Context context;
	private beanRwgg bean = null;
	// ��������CheckBox��ѡ��״��
	private static HashMap<Integer, Boolean> isSelected;
	// ��ѡ�е�����
	private static HashSet<String> listSelected;

	public static HashSet<String> getListSelected() {
		return listSelected;
	}

	public static void setListSelected(HashSet<String> listSelected) {
		RwggAdapter.listSelected = listSelected;
	}

	// ��������CheckBox����ʾ״��
	private static boolean isShowed;

	public RwggAdapter(Context context, ArrayList<beanRwgg> mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		this.context = context;
		isSelected = new HashMap<Integer, Boolean>();
		listSelected = new HashSet<String>();
		for (int i = 0; i < mData.size(); i++) {
			getIsSelected().put(i, false);
		}
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
			holder.select = (CheckBox) convertView.findViewById(R.id.select);
			holder.text_has_rwda = (TextView) convertView
					.findViewById(R.id.text_has_rwda);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bean = mData.get(position);
		Log.d("has_rwda", bean.getHas_rwda());
		if (Integer.parseInt(bean.getHas_rwda()) > 0) {
			holder.text_has_rwda.setVisibility(View.VISIBLE);
		} else {
			holder.text_has_rwda.setVisibility(View.GONE);
		}
		holder.text_rw_dm.setText(bean.getRw_dm());
		holder.text_cjsj.setText("����ʱ�䣺" + bean.getCjsj() + "\n�������:"
				+ bean.getWcqx());
		holder.title.setText("�û���ţ�" + bean.getYhbh());
		holder.title.setText(holder.title.getText() + "\n�������ͣ�"
				+ bean.getRw_lx());
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
		if (null != bean.getDbzch()) {
			holder.title.setText(holder.title.getText() + "\n����ʲ���ţ�"
					+ bean.getDbzch());
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
		holder.btn_lbs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("rw_dm:",position+"");
				Log.d("rw_dm:",mData.get(position).getRw_dm());
				Log.d("getCjb_jd:",mData.get(position).getCjb_jd());
				Log.d("getCjd_wd:",mData.get(position).getCjd_wd());
				Intent intent = new Intent(context, TabMap.class);
				//intent.putExtra("rw_dm", bean.getRw_dm());
				intent.putExtra("jd", mData.get(position).getCjb_jd());
				intent.putExtra("wd", mData.get(position).getCjd_wd());
				context.startActivity(intent);
			}
		});
		holder.title.setOnClickListener(new RwggListViewTitleOnClickListener(
				position, bean, this.context));
		// ����select����ʾ��������
		if (this.getIsShowed()) {
			holder.select.setVisibility(View.VISIBLE);
			

		} else {
			holder.select.setVisibility(View.GONE);
		}
		// ����select��ѡ��״̬
		holder.select.setChecked(getIsSelected().get(position));
		/*if (getIsSelected().get(position)) {
			this.listSelected.add(bean.getRw_dm());
		} else {
			this.listSelected.remove(bean.getRw_dm());
		}*/

		holder.select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				
				
				if (isChecked) {
					listSelected.add(mData.get(position).getRw_dm());
				} else {
					listSelected.remove(mData.get(position).getRw_dm());
				}
				
			}
		});
		Iterator it = getListSelected().iterator();
		/*while (it.hasNext()) {
			Log.d("rw_dm========",it.next().toString());
		}*/
		return convertView;
	}

	public static boolean getIsShowed() {
		return isShowed;
	}

	public static void setIsShowed(boolean isShowed) {

		RwggAdapter.isShowed = isShowed;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		RwggAdapter.isSelected = isSelected;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView text_cjsj;
		public TextView text_rw_dm;
		public TextView text_has_rwda;
		public Button btn_lbs;
		public CheckBox select;
	}
}