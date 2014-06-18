package com.itjiehun.bootmanager.apk;

import android.graphics.drawable.Drawable;

public class ApkInfo {
	public long cacheSize;
	public boolean canMove;
	public long codeSize;
	public String dataDir;
	public long dataSize;
	public long date;
	public long fileSize;
	public Drawable icon;
	public String name;
	public boolean onSDCard;
	public String packageName;
	public String sourceDir;
	public boolean systemApp;
	public int uid;
	public String versionName;

	public ApkInfo(String paramString) {
		this.packageName = paramString;
		this.name = paramString;
	}

	public boolean equals(Object paramObject) {
		if (paramObject == null) {
			return false;
		}
		return this.packageName.equals(((ApkInfo) paramObject).packageName);
	}

	public int hashCode() {
		return this.packageName.hashCode();
	}
}