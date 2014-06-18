package com.itjiehun.bootmanager;

import android.app.Application;

import com.itjiehun.bootmanager.apk.ApkCacheHelper;
import com.itjiehun.bootmanager.shell.ShellCommand;

public class BootApplication extends Application {
	private static BootApplication instance;
	private ApkCacheHelper apkCacheHelper;
	private ConfigHelper configHelper;
	private ShellCommand shellCommand;

	public static final BootApplication getInstance() {
		return instance;
	}

	public ApkCacheHelper getApkCacheHelper() {
		if (apkCacheHelper == null) {
			apkCacheHelper = new ApkCacheHelper(this);
		}
		return apkCacheHelper;
	}

	public ConfigHelper getConfigHelper() {
		if (configHelper == null) {
			configHelper = new ConfigHelper(this);
		}
		return configHelper;
	}

	public ShellCommand getShellCommand() {
		if (shellCommand == null) {
			shellCommand = new ShellCommand();
		}
		return shellCommand;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
