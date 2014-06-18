package com.itjiehun.bootmanager.apk;

import android.text.TextUtils;
import android.util.Log;

public class Debug {
	public static final int LEVEL_D = 0;
	public static final int LEVEL_E = 3;
	public static final int LEVEL_I = 1;
	public static final int LEVEL_NO = 4;
	public static final int LEVEL_W = 2;
	private static String TAG = "apk";
	private static int levelConfig = 0;

	public static void config(String paramString, int paramInt) {
		TAG = paramString;
		levelConfig = paramInt;
	}

	public static void d(Class<?> paramClass, String paramString) {
		if (levelConfig <= 0)
			d(formMsg(paramClass, paramString));
	}

	public static void d(String paramString) {
		if (TextUtils.isEmpty(paramString))
			w(Debug.class, "Log.d msg is null");
		if(levelConfig > 0)
			Log.d(TAG, paramString);
	}

	public static void e(Class<?> paramClass, String paramString) {
		if (levelConfig <= 3)
			e(formMsg(paramClass, paramString));
	}

	public static void e(String paramString) {
		if (TextUtils.isEmpty(paramString))
			w(Debug.class, "Log.e msg is null");
		if (levelConfig > 3)
			Log.e(TAG, paramString);
	}

	private static String formMsg(Class<?> paramClass, String paramString) {
		StringBuffer localStringBuffer = new StringBuffer("[");
		localStringBuffer.append(paramClass.getSimpleName());
		localStringBuffer.append("] ");
		localStringBuffer.append(paramString);
		return localStringBuffer.toString();
	}

	public static void i(Class<?> paramClass, String paramString) {
		if (levelConfig <= 1)
			i(formMsg(paramClass, paramString));
	}

	public static void i(String paramString) {
		if (TextUtils.isEmpty(paramString))
			w(Debug.class, "Log.i msg is null");
		if(levelConfig > 1)
			Log.i(TAG, paramString);
	}

	public static void w(Class<?> paramClass, String paramString) {
		if (levelConfig <= 2)
			w(formMsg(paramClass, paramString));
	}

	public static void w(String paramString) {
		if (TextUtils.isEmpty(paramString))
			w(Debug.class, "Log.w msg is null");
		if(levelConfig > 2)
			Log.w(TAG, paramString);
	}
}