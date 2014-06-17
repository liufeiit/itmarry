package com.ze.commontool;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class StringTools {
	public static String parseUrltoFallView(String temp)
	{
		int pos = temp.indexOf("!");
		if( -1 != pos )
		{
			temp = temp.substring(0 , pos);
		}
		temp = temp + "!210";
		return temp;
	}
	
	public static float getFontHeight(float fontSize)
	{
		Paint paint = new Paint();  
	    paint.setTextSize(fontSize);
	    FontMetrics fm = paint.getFontMetrics();  
	    return (float)Math.ceil(fm.descent - fm.ascent);  
	}
}
