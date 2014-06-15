package com.itjiehun.jxx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class DeviceUtil {
	private static String tag = "DeviceUtil";
	
	public static Map<String, String> getDeviceData(Context context) {
		Map<String, String> m = new HashMap<String, String>();
		String tmDevice = "", tmSerial = "", androidId = "";
		UUID deviceUuid = UUID.randomUUID();
		try {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			tmDevice = String.valueOf(tm.getDeviceId());
			tmSerial = String.valueOf(tm.getSimSerialNumber());
			androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		} catch (Exception e) {
			Log.e(tag, "TelephonyManager error.", e);
		}
		m.put("device", tmDevice);
		m.put("serial", tmSerial);
		m.put("androidId", androidId);
		m.put("deviceUuid", deviceUuid.toString());
		return m;
	}

	public static String getDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = String.valueOf(tm.getDeviceId());
		tmSerial = String.valueOf(tm.getSimSerialNumber());
		androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

	public static String getSimpleDeviceId(Context context) {
		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String result = tManager.getDeviceId();
		if (!TextUtils.isEmpty(result)) {
			return result;
		}
		try {
			result = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		} catch (Exception e) {
			Log.e(tag, "Secure error.", e);
		}
		if (!TextUtils.isEmpty(result)) {
			return result;
		}
		result = UUID.randomUUID().toString();
		return result;
	}

	public static boolean isEmulator() {
		if ("google_sdk".equals(Build.MODEL)) {
			return true;
		} else if ("sdk".equals(Build.MODEL)) {
			return true;
		}
		return false;
	}
}
