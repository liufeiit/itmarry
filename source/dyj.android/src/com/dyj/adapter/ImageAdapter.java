package com.dyj.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
public class ImageAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<Bitmap> imgs;

	public ImageAdapter(Context c,ArrayList<Bitmap> imgs) {
		mContext = c;
		this.imgs=imgs;
	}

	@Override
	public int getCount() {
		return imgs.size();
	}

	// ��ȡͼƬλ��
	@Override
	public Object getItem(int position) {
		return imgs.get(position);
	}

	// ��ȡͼƬID
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageview = new ImageView(mContext);
		imageview.setFocusable(true);
		imageview.setImageBitmap((Bitmap) getItem(position));
		imageview.setLayoutParams(new Gallery.LayoutParams(240, 120));		// ���ò��� ͼƬ120��120��ʾ
		imageview.setScaleType(ImageView.ScaleType.CENTER);				// ������ʾ�������ͣ������ţ�
	
		return imageview;
	}
}
