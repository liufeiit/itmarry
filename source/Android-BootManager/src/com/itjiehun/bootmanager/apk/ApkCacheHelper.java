package com.itjiehun.bootmanager.apk;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ApkCacheHelper {
	private Context context;
	private boolean init = false;
	private HashMap<String, ApkCache> apkCacheRepo;
	private DbHelper dbHelper;

	public ApkCacheHelper(Context paramContext) {
		context = paramContext;
		init(paramContext);
	}

	private void init(Context paramContext) {
		if (init) {
			return;
		}
		init = true;
		dbHelper = new DbHelper(this.context);
		apkCacheRepo = new HashMap<String, ApkCache>();
		Cursor localCursor = dbHelper.getApkCacheCursor();
		if (localCursor != null) {
			localCursor.moveToFirst();
		}
		while (true) {
			if (localCursor.isAfterLast()) {
				localCursor.close();
				return;
			}
			ApkCache localApkCache = new ApkCache();
			localApkCache.appName = localCursor.getString(localCursor.getColumnIndex("appName"));
			localApkCache.pkgName = localCursor.getString(localCursor.getColumnIndex("pkg"));
			byte[] arrayOfByte = localCursor.getBlob(localCursor.getColumnIndex("icon"));
			if (arrayOfByte != null) {
				localApkCache.icon = new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeByteArray(
						arrayOfByte, 0, arrayOfByte.length));
			}
			apkCacheRepo.put(localApkCache.pkgName, localApkCache);
			localCursor.moveToNext();
		}
	}

	public ApkCache load(String paramString) {
		init(context);
		ApkCache localApkCache = apkCacheRepo.get(paramString);
		return localApkCache;
	}

	public boolean save(String packageName, Drawable paramDrawable, String apkInfoName) {
		init(context);
		ApkCache localApkCache = new ApkCache();
		localApkCache.appName = apkInfoName;
		localApkCache.pkgName = packageName;
		localApkCache.icon = paramDrawable;
		if (dbHelper.saveApkCache(localApkCache) != -1L) {
			apkCacheRepo.put(packageName, localApkCache);
			return true;
		}
		return false;
	}

	public static class ApkCache {
		public String appName;
		public Drawable icon;
		public String pkgName;
	}
}
