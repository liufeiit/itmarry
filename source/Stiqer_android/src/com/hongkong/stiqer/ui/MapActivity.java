package com.hongkong.stiqer.ui;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.hongkong.stiqer.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


public class MapActivity extends FragmentActivity {
	private MapView mapView;  
    private AMap aMap; 
    private MarkerOptions markerOption;
    private LatLng latlng;
    private double longitude, latitude; 
    
	public void onCreate(Bundle savedInstanceState){  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_baidumap);  
		mapView = (MapView) findViewById(R.id.map);  
        mapView.onCreate(savedInstanceState);// 必须要写  
        init();  
    }
	
	/** 
     * 初始化AMap对象 
     */  
    private void init() {  
    	Intent t = getIntent();
    	longitude = t.getDoubleExtra("longitude", 0);
    	latitude = t.getDoubleExtra("latitude", 0);
	
    	latlng = new LatLng(latitude,longitude);
        if (aMap == null) {  
        	try{
               aMap = mapView.getMap();  
               aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));
               addMarkersToMap();
        	}catch(Exception e){
        		Intent st = new Intent(MapActivity.this,MapActivity.class);
				t.putExtra("longitude", longitude);
				t.putExtra("latitude", latitude);
				startActivity(st);
				this.finish();
        	}
        }  
    }
  
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onResume() {  
        super.onResume();  
        mapView.onResume();  
    }  
  
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onPause() {  
        super.onPause();  
        mapView.onPause();  
    }  
      
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
        mapView.onSaveInstanceState(outState);  
    }  
  
    /** 
     * 方法必须重写 
     */  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        mapView.onDestroy();  
    }  
    
    private void addMarkersToMap()   
    {  
    	markerOption = new MarkerOptions();  
        markerOption.position(latlng);  
        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");  
        markerOption.perspective(true);  
        markerOption.draggable(true);  
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));  
        aMap.addMarker(markerOption).showInfoWindow();
    }
    
}
