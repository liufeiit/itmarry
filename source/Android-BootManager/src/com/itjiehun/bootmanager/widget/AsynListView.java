package com.itjiehun.bootmanager.widget;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.itjiehun.bootmanager.R;

public abstract class AsynListView<T> extends EmptyListView {
	protected AsynListView<T>.Adapter mAdapter;
	protected AsyncListViewHelper<T> mAsyncLoadListHelper;
	protected int mCount;

	public AsynListView(Context paramContext) {
		this(paramContext, null);
	}

	public AsynListView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public AsynListView(Context paramContext, AttributeSet paramAttributeSet, int defStyle) {
		super(paramContext, paramAttributeSet, defStyle);
	}

	protected View createFooterView() {
		return View.inflate(getContext(), R.layout.list_view_footer_loading_bar, null);
	}

	public ArrayAdapter<T> getAdapter() {
		return this.mAdapter;
	}

	protected abstract View getView(int paramInt, View paramView, ViewGroup paramViewGroup, boolean paramBoolean);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void init() {
		super.init();
		this.mAdapter = initAdapter();
		this.mAsyncLoadListHelper = new AsyncListViewHelper(this.mListView, this.mLoadingPage, createFooterView()) {
			@Override
			public void notifyDataSetChanged() {
				AsynListView.this.mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLoadComplited(int paramInt1, int paramInt2, ArrayList paramArrayList) {
				AsynListView.this.onLoadComplited(paramInt1, paramInt2, paramArrayList);
			}

			@Override
			public ArrayList onLoading(int paramInt1, int paramInt2) {
				return AsynListView.this.onLoading(paramInt1, paramInt2);
			}
		};
		this.mListView.setAdapter(this.mAdapter);
	}

	protected AsynListView<T>.Adapter initAdapter() {
		return new Adapter(getContext());
	}

	public void loadPage(int paramInt1, int paramInt2) {
		this.mAsyncLoadListHelper.loadPage(paramInt1, paramInt2);
	}

	public void onLoadComplited(int paramInt1, int paramInt2, ArrayList<T> paramArrayList) {
		if (paramInt1 == 1) {
			this.mAdapter.clear();
		}
		Iterator<T> localIterator = null;
		if (paramArrayList != null)
			localIterator = paramArrayList.iterator();
		while (localIterator.hasNext()) {
			Object localObject = (Object) localIterator.next();
			this.mAdapter.add((T) localObject);
		}
	}

	protected abstract ArrayList<T> onLoading(int paramInt1, int paramInt2);

	public void reLoad() {
		this.mAsyncLoadListHelper.loadPage(1, 20);
	}

	public void refreshList() {
		reset();
		this.mAsyncLoadListHelper.loadPage(1, 20);
	}

	public void reset() {
		this.mAsyncLoadListHelper.reset();
		this.mAdapter.clear();
	}

	public void setOnBusyChangeLinstener(AsyncListViewHelper.OnBusyChangeListener paramOnBusyChangeListener) {
		this.mAsyncLoadListHelper.setOnBusyChangeListener(paramOnBusyChangeListener);
	}

	public class Adapter extends ArrayAdapter<T> {
		public Adapter(Context arg2) {
			super(arg2, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return AsynListView.this.getView(position, convertView, parent,
					AsynListView.this.mAsyncLoadListHelper.isBusy());
		}
	}
}
