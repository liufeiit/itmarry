package com.dodowaterfall.widget;
import java.util.Map;

import android.content.res.AssetManager;
import android.view.View.OnClickListener;

public class FlowTag {
	private int flowId;
	private String fileName;
	public final int what = 1;
	private int pos = -1;
	private Map<String, Object> dataMap;
	private OnClickListener onClickListener;
	
	public int getFlowId() {
		return flowId;
	}
	public int getPos()
	{
		return pos;
	}
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private AssetManager assetManager;
	private int ItemWidth;
	private int ItemHeight;

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public int getItemWidth() {
		return ItemWidth;
	}

	public void setItemWidth(int itemWidth) {
		ItemWidth = itemWidth;
	}

	public int getItemHeight() {
		return ItemHeight;
	}

	public void setItemHeight(int itemHeight) {
		ItemHeight = itemHeight;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	public void setViewPos( int p_pos )
	{
		this.pos = p_pos;
	}
	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
}