package com.hongkong.stiqer.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


public class MyLocation {
	private  Context mContext;
	double   longitude = 0.0,latitude = 0.0;
	
	public MyLocation(Context c){
		this.mContext = c;
		initGetLocation();
	}
		
	public void initGetLocation(){
		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    	if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
              Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
              if(location != null){
                 latitude = location.getLatitude();
                 longitude = location.getLongitude();
               }

	        }else{
	        	 LocationListener locationListener = new LocationListener() {
	        		 public void onStatusChanged(String provider, int status, Bundle extras) {

	        		 }
	                 // Provider被enable时触发此函数，比如GPS被打开
	                 @Override
	                 public void onProviderEnabled(String provider) {

	                 }
	                 // Provider被disable时触发此函数，比如GPS被关闭
	                 @Override
	                 public void onProviderDisabled(String provider) {
	                 }
	                 //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
	                 @Override
	                 public void onLocationChanged(Location location) {
	                     if (location != null) {  
	                         Log.e("Map", "Location changed : Lat: " 
	                         + location.getLatitude() + " Lng: " 
	                         + location.getLongitude());  
	                     }
	                 }
	        	 };
	        	 
        	   locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);  
               Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
               if(location != null){  
                  latitude = location.getLatitude(); //经度  
                  longitude = location.getLongitude(); //纬度
               }  
	    }
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
     
}
