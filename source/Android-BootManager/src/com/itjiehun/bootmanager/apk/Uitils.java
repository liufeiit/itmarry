package com.itjiehun.bootmanager.apk;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.itjiehun.bootmanager.BootApplication;

public class Uitils {
	public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	public static final int APP_INSTALL_EXTERNAL = 2;
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	private static final String APP_PKG_NAME_22 = "pkg";
	public static String DATA_DIR;
	public static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
	public static final int FLAG_EXTERNAL_STORAGE = 262144;
	public static final int FLAG_FORWARD_LOCK = 1048576;
	public static final int INSTALL_LOCATION_AUTO = 0;
	public static final int INSTALL_LOCATION_PREFER_EXTERNAL = 2;
	public static final int INSTALL_LOCATION_UNSPECIFIED = -1;
	public static final int MOVE_EXTERNAL_MEDIA = 2;
	public static final int MOVE_INTERNAL = 1;
	private static final String SCHEME = "package";
	private static Object iPackageManager = null;

	public static ApkInfo createApkInfo(PackageManager paramPackageManager, PackageInfo paramPackageInfo,
			Drawable paramDrawable) {
		ApkInfo localApkInfo = new ApkInfo(paramPackageInfo.packageName);
		localApkInfo.dataDir = paramPackageInfo.applicationInfo.dataDir;
		if ((TextUtils.isEmpty(DATA_DIR)) && (!TextUtils.isEmpty(localApkInfo.dataDir))) {
			DATA_DIR = new File(localApkInfo.dataDir).getParent();
		}
		localApkInfo.versionName = paramPackageInfo.versionName;
		localApkInfo.uid = paramPackageInfo.applicationInfo.uid;
		ApkCacheHelper.ApkCache localApkCache = BootApplication.getInstance().getApkCacheHelper()
				.load(paramPackageInfo.packageName);
		if (localApkCache == null) {
			File localFile2 = new File(paramPackageInfo.applicationInfo.sourceDir);
			localApkInfo.fileSize = localFile2.length();
			localApkInfo.date = localFile2.lastModified();
			localApkInfo.sourceDir = paramPackageInfo.applicationInfo.sourceDir;
			Drawable localDrawable = paramPackageManager.getApplicationIcon(paramPackageInfo.applicationInfo);
			if (localDrawable == null) {
				localDrawable = paramDrawable;
			}
			localApkInfo.name = paramPackageManager.getApplicationLabel(paramPackageInfo.applicationInfo).toString();
			localApkInfo.icon = localDrawable;
			BootApplication.getInstance().getApkCacheHelper()
					.save(paramPackageInfo.packageName, localApkInfo.icon, localApkInfo.name);
			return localApkInfo;
		}
		localApkInfo.name = localApkCache.appName;
		localApkInfo.icon = localApkCache.icon;
		localApkInfo.versionName = paramPackageInfo.versionName;
		File localFile1 = new File(paramPackageInfo.applicationInfo.publicSourceDir);
		localApkInfo.fileSize = localFile1.length();
		localApkInfo.date = localFile1.lastModified();
		localApkInfo.sourceDir = paramPackageInfo.applicationInfo.publicSourceDir;
		return localApkInfo;
	}

	public static ApkInfo createApkInfo(PackageManager paramPackageManager, String paramString, Drawable paramDrawable) {
		try {
			ApkInfo localApkInfo = createApkInfo(paramPackageManager,
					paramPackageManager.getPackageInfo(paramString, 0), paramDrawable);
			return localApkInfo;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap drawableToBitmap(Drawable icon) {
		int i = icon.getIntrinsicWidth();
		int j = icon.getIntrinsicHeight();
		Bitmap.Config localConfig = null;
		if (icon.getOpacity() != -1) {
			localConfig = Bitmap.Config.ARGB_8888;
		} else {
			localConfig = Bitmap.Config.RGB_565;
		}
		Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
		icon.draw(localCanvas);
		return localBitmap;
	}

	public static int getInstallLocation() {
		try {
			if (iPackageManager == null) {
				Class<?> localClass = Class.forName("android.app.ActivityThread");
				iPackageManager = localClass.getMethod("getPackageManager", new Class[0]).invoke(localClass,
						new Object[0]);
			}
			int i = ((Integer) iPackageManager.getClass().getMethod("getInstallLocation", new Class[0])
					.invoke(iPackageManager, new Object[0])).intValue();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Intent getInstalledAppDetailsIntent(String paramString) {
		Intent localIntent1 = new Intent();
		int i = Build.VERSION.SDK_INT;
		if (i >= 9) {
			Intent localIntent2 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
			localIntent2.setData(Uri.fromParts("package", paramString, null));
			return localIntent2;
		}
		if (i == 8)
			;
		for (String str = "pkg";; str = "com.android.settings.ApplicationPkgName") {
			localIntent1.setAction("android.intent.action.VIEW");
			localIntent1.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			localIntent1.putExtra(str, paramString);
			return localIntent1;
		}
	}

	public static Intent getLaunchIntentForPackage(PackageManager paramPackageManager, String paramString) {
		return paramPackageManager.getLaunchIntentForPackage(paramString);
	}

	public static void installShortcut(Context paramContext, Intent paramIntent, String paramString, int paramInt) {
		Intent localIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		localIntent.putExtra("android.intent.extra.shortcut.NAME", paramString);
		localIntent.putExtra("duplicate", false);
		localIntent.putExtra("android.intent.extra.shortcut.INTENT", paramIntent);
		localIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
				Intent.ShortcutIconResource.fromContext(paramContext, paramInt));
		paramContext.sendBroadcast(localIntent);
	}

	public static void saveMyBitmap(Bitmap paramBitmap, File paramFile) throws IOException {
		if (!paramFile.getParentFile().exists()) {
			paramFile.getParentFile().mkdirs();
		}
		paramFile.createNewFile();
	}

	public static void shareFriends(Context paramContext, String paramString1, String paramString2) {
		Intent localIntent = new Intent("android.intent.action.SEND");
		localIntent.setType("text/plain");
		localIntent.putExtra("android.intent.extra.SUBJECT", paramString1);
		localIntent.putExtra("android.intent.extra.TEXT", paramString2);
		localIntent.setFlags(268435456);
	}

	public static boolean tryLaunchActivity(Context paramContext, String paramString) {
		Intent localIntent = getLaunchIntentForPackage(paramContext.getPackageManager(), paramString);
		try {
			if (localIntent != null) {
				paramContext.startActivity(localIntent);
			}
			return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public static void unInstallAppNormal(Context paramContext, String paramString) {
		Intent localIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + paramString));
		try {
			paramContext.startActivity(localIntent);
		} catch (Exception localException) {
		}
	}
}
