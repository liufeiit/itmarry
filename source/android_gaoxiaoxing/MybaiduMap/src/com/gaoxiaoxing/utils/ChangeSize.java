package com.gaoxiaoxing.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ChangeSize {
	public static int dipToPx(int dp,DisplayMetrics metrics){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
	}
}
