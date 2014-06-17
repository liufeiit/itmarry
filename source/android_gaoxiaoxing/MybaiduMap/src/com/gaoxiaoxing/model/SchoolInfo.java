package com.gaoxiaoxing.model;

import java.io.Serializable;

import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class SchoolInfo extends BaseInfo implements Serializable {
	private int uid;
	private String title;

	public SchoolInfo(CloudPoiInfo info) {
		super(info.address,info.latitude,info.longitude);
		this.uid = info.uid;
		this.title = info.title;
	}

	public SchoolInfo(int uid, String title, String address, double latitude,
			double longitude) {
		super(address,latitude,longitude);
		this.uid = uid;
		this.title = title;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
