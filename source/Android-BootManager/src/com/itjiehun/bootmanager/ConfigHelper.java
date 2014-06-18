package com.itjiehun.bootmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ConfigHelper {
	public static final String KEY_APP_LINK = "downloadLink";
	public static final String KEY_AUTO_BACK_UP = "autoBackUp";
	public static final String KEY_CHECK_CLEAR = "checkClear";
	public static final String KEY_CHECK_MOVE = "checkMove";
	public static final String KEY_DATA_DIR = "dataDir";
	public static final String KEY_OPEN_SHAKE_DETECTOR = "openShakeDetector";
	public static final String KEY_SILENT_MOVE = "silentMove";
	public static final String KEY_SORT = "sort";
	public static final String TAG_REINSTALL = "tagReinstall1.1";
	public static final String TAG_SET = "tagSet1.1";
	public static final String TAG_UPDATE = "tagUpdate";
	public static final String TAG_UPDATE_SET = "tagUpdateSet";
	private Context context;
	private boolean mBatchMode;
	private SharedPreferences mSharedPreferences;

	public ConfigHelper(Context paramContext) {
		this(paramContext, paramContext.getPackageName());
	}

	public ConfigHelper(Context paramContext, SharedPreferences paramSharedPreferences) {
		this.mSharedPreferences = paramSharedPreferences;
		this.context = paramContext;
		this.mBatchMode = false;
	}

	public ConfigHelper(Context paramContext, String paramString) {
		this(paramContext, paramContext.getSharedPreferences(paramString, 0));
	}

	private String getVersionName() {
		try {
			String str = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
			return str;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
		}
		return "0.0";
	}

	public String getAppLink() {
		return getString("downloadLink", "");
	}

	public boolean getBoolean(String paramString, boolean paramBoolean) {
		return this.mSharedPreferences.getBoolean(paramString, paramBoolean);
	}

	public int getInt(String paramString, int paramInt) {
		return this.mSharedPreferences.getInt(paramString, paramInt);
	}

	public int getSortMode() {
		return getInt("sort", 1);
	}

	public String getString(String paramString1, String paramString2) {
		return this.mSharedPreferences.getString(paramString1, paramString2);
	}

	public boolean hasNewApp() {
		return getBoolean(getVersionName(), false);
	}

	public boolean isAutoBackUp() {
		return getBoolean("autoBackUp", true);
	}

	public boolean isBatchMode() {
		return this.mBatchMode;
	}

	public boolean isCheckClear() {
		return getBoolean("checkClear", true);
	}

	public boolean isCheckMove() {
		return getBoolean("checkMove", true);
	}

	public boolean isFirstRun() {
		return getBoolean("firstRun", true);
	}

	public boolean isSilentMove() {
		return getBoolean("silentMove", true);
	}

	public boolean openShakeDetector() {
		return getBoolean("openShakeDetector", true);
	}

	public void putBoolean(String paramString, boolean paramBoolean) {
		this.mSharedPreferences.edit().putBoolean(paramString, paramBoolean).commit();
	}

	public void putInt(String paramString, int paramInt) {
		this.mSharedPreferences.edit().putInt(paramString, paramInt).commit();
	}

	public void putString(String paramString1, String paramString2) {
		this.mSharedPreferences.edit().putString(paramString1, paramString2).commit();
	}

	public void setAppLink(String paramString) {
		putString("downloadLink", paramString);
	}

	public void setAutoBackUp(boolean paramBoolean) {
		putBoolean("autoBackUp", paramBoolean);
	}

	public void setBatchMode(boolean paramBoolean) {
		this.mBatchMode = paramBoolean;
	}

	public void setCheckClear(boolean paramBoolean) {
		putBoolean("checkClear", paramBoolean);
	}

	public void setCheckMove(boolean paramBoolean) {
		putBoolean("checkMove", paramBoolean);
	}

	public void setFirstRun(boolean paramBoolean) {
		putBoolean("firstRun", paramBoolean);
	}

	public void setNewApp(boolean paramBoolean) {
		putBoolean(getVersionName(), paramBoolean);
	}

	public void setShakeDetector(boolean paramBoolean) {
		putBoolean("openShakeDetector", paramBoolean);
	}

	public void setSilentMove(boolean paramBoolean) {
		putBoolean("silentMove", paramBoolean);
	}

	public void setSortMode(int paramInt) {
		putInt("sort", paramInt);
	}
}