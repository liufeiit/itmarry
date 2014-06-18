package com.itjiehun.bootmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class EmptyListView extends RelativeLayout implements IRefresh {

	protected ListView mListView;
	protected IEmptyView mLoadingPage;

	public EmptyListView(Context paramContext) {
		this(paramContext, null);
	}

	public EmptyListView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public EmptyListView(Context paramContext, AttributeSet paramAttributeSet, int defStyle) {
		super(paramContext, paramAttributeSet, defStyle);
		init();
		this.mListView.setCacheColorHint(0);
	}

	protected IEmptyView createEmptyView() {
		return new LoadingPage(getContext());
	}

	protected ListView createListView() {
		return new ListView(getContext());
	}

	public IEmptyView getEmptyView() {
		return this.mLoadingPage;
	}

	public ListView getListView() {
		return this.mListView;
	}

	protected void init() {
		this.mListView = createListView();
		this.mLoadingPage = createEmptyView();
		this.mLoadingPage.setOnRefresh(this);
		RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(-2, -2);
		localLayoutParams1.addRule(13);
		addView(this.mLoadingPage.getEmptyView(), localLayoutParams1);
		RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
		addView(this.mListView, localLayoutParams2);
		this.mListView.setEmptyView(this.mLoadingPage.getEmptyView());
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	public void reLoad() {
	}
}
