package com.itjiehun.bootmanager.widget;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public abstract class AsyncListViewHelper<AdapterItem> implements AbsListView.OnScrollListener {
	private boolean busy;
	private int currentPage;
	private View footerView;
	protected IEmptyView iemptyView;
	private ListView listView;
	private boolean loadMore;
	private AsyncListViewHelper<AdapterItem>.LoadTask mLoadTask;
	private boolean loading;
	private boolean noMoreData;
	protected OnBusyChangeListener onBusyChangeListener;
	private int pageSize = 20;

	public AsyncListViewHelper(ListView paramListView, IEmptyView paramIEmptyView, View paramView) {
		this.listView = paramListView;
		this.footerView = paramView;
		this.loading = false;
		this.noMoreData = false;
		this.listView.addFooterView(this.footerView);
		this.listView.setOnScrollListener(this);
		this.iemptyView = paramIEmptyView;
		this.currentPage = 0;
		this.onBusyChangeListener = null;
	}

	public boolean isBusy() {
		return this.busy;
	}

	public boolean isLoading() {
		return this.loading;
	}

	public void loadPage(int paramInt1, int paramInt2) {
		if (!this.loading) {
			this.loading = true;
			this.mLoadTask = new LoadTask(paramInt1, paramInt2);
			this.mLoadTask.execute(new Void[0]);
		}
	}

	public abstract void notifyDataSetChanged();

	public abstract void onLoadComplited(int paramInt1, int paramInt2, ArrayList<AdapterItem> paramArrayList);

	public abstract ArrayList<AdapterItem> onLoading(int paramInt1, int paramInt2);

	public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {
		if (paramInt1 + paramInt2 == paramInt3) {
			this.loadMore = true;
			return;
		}
		this.loadMore = false;
	}

	public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
		if ((this.loadMore) && (!this.noMoreData))
			loadPage(1 + this.currentPage, this.pageSize);
		switch (paramInt) {
		default:
			return;
		case 0:
			if ((this.busy) && (this.onBusyChangeListener != null))
				this.onBusyChangeListener.onBusyChange(false);
			this.busy = false;
			notifyDataSetChanged();
			return;
		case 1:
			if ((!this.busy) && (this.onBusyChangeListener != null))
				this.onBusyChangeListener.onBusyChange(true);
			this.busy = true;
			return;
		case 2:
		}
		if ((!this.busy) && (this.onBusyChangeListener != null))
			this.onBusyChangeListener.onBusyChange(true);
		this.busy = true;
	}

	public void reset() {
		this.listView.removeFooterView(this.footerView);
		this.listView.addFooterView(this.footerView);
		this.noMoreData = false;
		this.loading = false;
		if (this.mLoadTask != null) {
			this.mLoadTask.cancel(true);
			this.mLoadTask = null;
		}
	}

	public void setCurrentPage(int paramInt) {
		this.currentPage = paramInt;
	}

	public void setNoMoreData() {
		this.listView.removeFooterView(this.footerView);
		this.noMoreData = true;
	}

	public void setOnBusyChangeListener(OnBusyChangeListener paramOnBusyChangeListener) {
		this.onBusyChangeListener = paramOnBusyChangeListener;
	}

	private class LoadTask extends AsyncTask<Void, Void, ArrayList<AdapterItem>> {
		int loadingPage;
		int pageSize;

		public LoadTask(int paramInt1, int arg3) {
			this.loadingPage = paramInt1;
			this.pageSize = arg3;
		}

		protected ArrayList<AdapterItem> doInBackground(Void[] paramArrayOfVoid) {
			return AsyncListViewHelper.this.onLoading(this.loadingPage, this.pageSize);
		}

		protected void onPostExecute(ArrayList<AdapterItem> paramArrayList) {
			super.onPostExecute(paramArrayList);
			if (isCancelled())
				return;
			if (paramArrayList != null) {
				AsyncListViewHelper.this.currentPage = this.loadingPage;
				if (paramArrayList.size() < this.pageSize)
					AsyncListViewHelper.this.setNoMoreData();
				if ((paramArrayList.size() == 0) && (this.loadingPage == 1))
					AsyncListViewHelper.this.iemptyView.showEmptyText(null, "暂无数据");
			}
			AsyncListViewHelper.this.onLoadComplited(this.loadingPage, this.pageSize, paramArrayList);
			AsyncListViewHelper.this.loading = false;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			AsyncListViewHelper.this.iemptyView.showLoading("正在加载列表，请稍候！");
		}
	}

	public static abstract interface OnBusyChangeListener {
		public abstract void onBusyChange(boolean paramBoolean);
	}
}
