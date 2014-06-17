package com.gaoxiaoxing.model;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaseInfo {
	private double latitude, longitude;
	private String address;
	
	public BaseInfo(){
	}
	
	public BaseInfo(String add,double la,double lo){
		this.address=add;
		this.latitude=la;
		this.longitude=lo;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GeoPoint getGeoPoint() {
		GeoPoint poi = new GeoPoint((int) (latitude * 1e6),
				(int) (longitude * 1e6));
		return poi;
	}
}
