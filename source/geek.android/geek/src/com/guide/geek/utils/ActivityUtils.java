package com.guide.geek.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import com.guide.geek.app.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * 所有界面之间的操作，都放这里
 * @author liudongqiu
 *
 */
public class ActivityUtils {

	private static ArrayList<Activity> actList = new ArrayList<Activity>();
	public static final void addAct(Activity act){
		actList.add(act);
	}
	public static final void finishAll(){
		for(int i=actList.size()-1; i>=0; i--){
			actList.get(i).finish();
		}
		actList.clear();
	}
	public static final void clearAll(){
		actList.clear();
	}
	
	public static final void toHomeAct(Context context){
		to(context, MainActivity.class);
	}
	
	public static final void to(Context context, Class<?> cls){
		to(context, cls, null);
	}
	public static final void to(Context context, Class<?> cls, Bundle data){
		Intent intent = new Intent(context, cls);
		if(data != null){
			intent.putExtras(data);
		}
		context.startActivity(intent);
	}
	public static final void gotoHome(Context context){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}
	
	/**
	 * 添加桌面快捷图标
	 * 需要权限：
	 * 	<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
	 * @param context
	 * @param cls
	 */
	/*public static void createShorcut(Context context, int logo, String name, Class<?> cls) {
	    Intent thisIntent = new Intent();
	    thisIntent.setClass(context, cls);
	    String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	    Intent addShortcut = new Intent(ACTION_ADD_SHORTCUT);

	    Parcelable icon = Intent.ShortcutIconResource.fromContext(context, logo);

	    addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	    addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
	    addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, thisIntent);
	    context.sendBroadcast(addShortcut);
	}*/
	
	
	
	public static final DecimalFormat format = new DecimalFormat("0.00");
	/**
	 * 保留两位小数
	 * @param decimal
	 * @return
	 */
	public static final String formatToTwoDecimalPlaces(String decimal){
		try {
			return format.format(format.parse(decimal));
		} catch (ParseException e) {
			return decimal;
		}
	}
	
	public static final String formatToDate(String date){
		if(date == null){
			return "";
		}else{
			int index = date.indexOf(" ");
			return (index > 0 ? date.substring(0, index) : date).replaceAll("/", "-");
		}
	}
}

