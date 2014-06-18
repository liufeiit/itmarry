package com.itjiehun.bootmanager.apk;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class DbHelper extends SQLiteOpenHelper {

	private static final String CREATE_CACHE_TABLE = "CREATE TABLE cache_table (pkg TEXT PRIMARY KEY, appName TEXT, icon BLOB);";

	public static final String DATABASE_NAME = "uninstall.db";
	public static final int DATABASE_VERSION = 2;
	public static final String TABLE_CACHE = "cache_table";
	public static final String TABLE_LOG = "log_table";

	public DbHelper(Context paramContext) {
		super(paramContext, "uninstall.db", null, 2);
	}

	public Cursor getApkCacheCursor() {
		return getWritableDatabase().query("cache_table", null, null, null, null, null, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		onUpgrade(db, 0, 2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 1) {
			db.execSQL(CREATE_CACHE_TABLE);
		}
	}

	public long saveApkCache(ApkCacheHelper.ApkCache paramApkCache) {
		ContentValues localContentValues = new ContentValues();
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		Bitmap localBitmap = Uitils.drawableToBitmap(paramApkCache.icon);
		if (localBitmap != null) {
			localBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
			localContentValues.put("icon", localByteArrayOutputStream.toByteArray());
		}
		localContentValues.put("pkg", paramApkCache.pkgName);
		localContentValues.put("appName", paramApkCache.appName);
		return getWritableDatabase().insert("cache_table", null, localContentValues);
	}
}
