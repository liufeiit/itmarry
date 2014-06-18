package com.itjiehun.bootmanager.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface IEmptyView {
	View getEmptyView();

	void onOpenSetActivity();

	void setOnRefresh(IRefresh paramIRefresh);

	void showEmptyText(Drawable paramDrawable, String paramString);

	void showLoadFail(Drawable paramDrawable);

	void showLoading(String paramString);
}
