package com.test.baidumap.overlay;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.gaoxiaoxing.R;
import com.map.bbs.BbsActivity;

public class CloudOverlay extends ItemizedOverlay {

	List<CloudPoiInfo> mLbsPoints;
	Activity mContext;
	MapView mapView;
	PopupOverlay popup;
	private static View view;
	private Intent intent;

	public CloudOverlay(Activity context, MapView mMapView) {
		super(null, mMapView);
		mContext = context;
		this.mapView = mMapView;
		intent = new Intent();
		initPopup();
	}

	private void initPopup() {
		view = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.popup_layout, null);
		popup = new PopupOverlay(mMapView, new PopupClickListener() {
			// 监听器
			public void onClickedPopup(int index) {
			}
		});
	}

	public void setData(List<CloudPoiInfo> lbsPoints) {
		if (lbsPoints != null) {
			mLbsPoints = lbsPoints;
		}
		for (CloudPoiInfo rec : mLbsPoints) {
			GeoPoint pt = new GeoPoint((int) (rec.latitude * 1e6),
					(int) (rec.longitude * 1e6));
			OverlayItem item = new OverlayItem(pt, rec.title, rec.address);
			Drawable marker1 = this.mContext.getResources().getDrawable(
					R.drawable.icon_school);
			item.setMarker(marker1);
			addItem(item);
		}
	}

	public void showPopu(int index) {
		final CloudPoiInfo info = mLbsPoints.get(index);
		GeoPoint point = new GeoPoint((int) (info.latitude * 1e6),
				(int) (info.longitude * 1e6));
		((TextView) view.findViewById(R.id.popup_title)).setText(info.title);
		((ImageView) view.findViewById(R.id.popup_msg))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						intent.setClass(mContext, BbsActivity.class);
						intent.putExtra("Name", info.title);
						mContext.startActivity(intent);
					}
				});
		int height = this.getItem(index).getMarker().getMinimumHeight();
		mMapView.getController().animateTo(point);
		popup.showPopup(view, point, height);// 显示弹窗
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	// 响应标签点击事件
	protected boolean onTap(int index) {
		showPopu(index);
		return super.onTap(index);
	}

	// 点击其他区域
	public boolean onTap(GeoPoint arg0, MapView mapview) {
		popup.hidePop();
		return false;
	}

}