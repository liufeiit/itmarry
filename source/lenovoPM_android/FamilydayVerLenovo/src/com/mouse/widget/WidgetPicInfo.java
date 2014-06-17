package com.mouse.widget;

import android.graphics.Bitmap;   

public class WidgetPicInfo {
	public String title;
	public String name;
	public String time;
	public Bitmap bm;
	public WidgetPicInfo(){
		this.title = "";
		this.name = "";
		this.time = "";
		this.bm = null;
	}
	public WidgetPicInfo(String title, String name, String time, Bitmap bm){
		this.title = title;
		this.name = name;
		this.time = time;
		this.bm = bm;
	}
}
