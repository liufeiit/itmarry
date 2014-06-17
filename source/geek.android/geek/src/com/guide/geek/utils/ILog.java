package com.guide.geek.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * 日志类
 * 
 * @author jjyo
 * 
 */
public class ILog {

	public final static String TAG = "qxlog";

	/** 调试开关 */
	public static boolean DEBUG = true;
	/** 写日志文件 */
	public static boolean WRITE_FILE = false;

	/**
	 * 输出为绿色..一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息
	 * 
	 * @param obj
	 */
	public static void i(Object... obj) {
		if (DEBUG) {
			Log.i(TAG, objects2String(obj));
			if (WRITE_FILE) {
				writeLog2File(obj);
			}
		}
	}

	/**
	 * 调试颜色为黑色的，任何消息都会输出，verbose是啰嗦的意思!
	 * 
	 * @param obj
	 */
	public static void v(Object... obj) {
		if (DEBUG) {
			Log.v(TAG, objects2String(obj));
			if (WRITE_FILE) {
				writeLog2File(obj);
			}
		}

	}

	/**
	 * 可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了。
	 * 
	 * @param obj
	 */
	public static void e(Object... obj) {
		if (DEBUG) {
			Log.e(TAG, objects2String(obj));
			writeLog2File(obj);
		}
	}

	/**
	 * 橙色信息...warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息
	 * 
	 * @param obj
	 */
	public static void w(Object... obj) {
		if (DEBUG) {
			Log.w(TAG, objects2String(obj));
			writeLog2File(obj);
		}
	}

	/**
	 * 输出颜色是蓝色的，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择
	 * 
	 * @param obj
	 */
	public static void d(Object... obj) {
		if (DEBUG) {
			Log.d(TAG, objects2String(obj));
			writeLog2File(obj);
		}
	}

	private static String objects2String(Object... obj) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < obj.length; i++) {
			sb.append(obj[i]);
		}
		return sb.toString();
	}

	public static void writeLog2File(Object... objects) {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String logPath = Environment.getExternalStorageDirectory() + "/qxin/log.txt";
				File file = new File(logPath);
				if (!file.exists()) {
					File parent = file.getParentFile();
					parent.mkdirs();
				}

				long length = file.length();

				StringBuilder sb = new StringBuilder();
				sb.append(new Date().toLocaleString());
				sb.append(":");
				for (int i = 0; i < objects.length; i++) {
					sb.append(objects[i]);
				}
				sb.append("\r\n");

				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				raf.seek(length);
				raf.write(sb.toString().getBytes("gbk"));
				raf.close();
			}
		} catch (Exception e) {
			ILog.e("writeLog2File exception:" + e.toString());
		}

	}

}
