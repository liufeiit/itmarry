package com.itjiehun.bootmanager.widget;

import java.util.ArrayList;

import android.content.ComponentName;

import com.itjiehun.bootmanager.apk.ApkInfo;

public class Item {
	public ApkInfo apkInfo;
	public ArrayList<ComponentName> componentNames;
	public boolean enable;

	public Item(ApkInfo paramApkInfo) {
		this.apkInfo = paramApkInfo;
		this.enable = false;
		this.componentNames = new ArrayList<ComponentName>();
	}

	public void addComponent(boolean paramBoolean, ComponentName paramComponentName) {
		this.componentNames.add(paramComponentName);
		if (!enable) {
			enable = paramBoolean;
		}
	}

	public boolean equals(Object paramObject) {
		if (paramObject == null) {
			return false;
		}
		return this.apkInfo.equals(((Item) paramObject).apkInfo);
	}
}
