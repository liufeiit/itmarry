package com.jingtuo.android.common.os;


/**
 * 
 * @author 28173_000
 *
 */
public class BitmapInfo {
	
	public static final String TYPE_RES = "resources";
	
	public static final String TYPE_ASSETS = "assets";

	public static final String TYPE_FILE = "file";

	public static final String TYPE_URL = "url";
	
	private String type = "";
	
	private int resId;
	
	private String path;
	
	private int dstWidth;
	
	private int dstHeight;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDstWidth() {
		return dstWidth;
	}

	public void setDstWidth(int dstWidth) {
		this.dstWidth = dstWidth;
	}

	public int getDstHeight() {
		return dstHeight;
	}

	public void setDstHeight(int dstHeight) {
		this.dstHeight = dstHeight;
	}
	
	
}
