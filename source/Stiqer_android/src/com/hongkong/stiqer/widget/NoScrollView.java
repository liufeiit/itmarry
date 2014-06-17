package com.hongkong.stiqer.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NoScrollView extends ScrollView {

	public NoScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {  
        return false;  
    } 
}
