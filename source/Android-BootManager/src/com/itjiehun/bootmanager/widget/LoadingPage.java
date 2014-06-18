package com.itjiehun.bootmanager.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itjiehun.bootmanager.R;

public class LoadingPage extends FrameLayout implements IEmptyView {
	public View load_empty;
	public View load_error;
	public View loading_progress;
	public ImageView mEmptyImageView;
	public TextView mEmptyTextView;
	public ImageView mErrorImageView;
	public TextView mLoadingTextView;
	private IRefresh mRefresh;

	public LoadingPage(Context context) {
		this(context, null);
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View.inflate(context, R.layout.listview_loading, this);
		setVisibility(View.GONE);
		this.load_empty = findViewById(R.id.emptyLayout);
		this.loading_progress = findViewById(R.id.loadingLayout);
		this.load_error = findViewById(R.id.errorLayout);
		showView(true, false, false);
		this.mLoadingTextView = ((TextView) this.loading_progress.findViewById(R.id.loadingText));
		this.mEmptyTextView = ((TextView) this.load_empty.findViewById(R.id.emptyText));
		this.mEmptyImageView = ((ImageView) this.load_empty.findViewById(R.id.emptyImage));
		this.mErrorImageView = ((ImageView) this.load_error.findViewById(R.id.errorImage));
		this.load_error.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				LoadingPage.this.onOpenSetActivity();
			}
		});
		this.load_error.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				LoadingPage.this.showLoading();
				if (LoadingPage.this.mRefresh != null)
					LoadingPage.this.mRefresh.onRefresh();
			}
		});
	}

	private void showView(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
		setVisibility(INVISIBLE);
		View localView1 = this.loading_progress;
		View localView2 = this.load_empty;
		View localView3 = this.load_error;

		localView1.setVisibility(INVISIBLE);
		localView2.setVisibility(INVISIBLE);
		localView3.setVisibility(INVISIBLE);

		if (paramBoolean1)
			localView1.setVisibility(VISIBLE);
		if (paramBoolean2)
			localView2.setVisibility(VISIBLE);
		if (paramBoolean3)
			localView3.setVisibility(VISIBLE);
	}

	@Override
	public View getEmptyView() {
		return this;
	}

	@Override
	public void onOpenSetActivity() {
		Intent localIntent = new Intent("android.intent.action.VIEW");
		localIntent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
		try {
			getContext().startActivity(localIntent);
			return;
		} catch (Exception localException) {
		}
	}

	@Override
	public void setOnRefresh(IRefresh paramIRefresh) {
		this.mRefresh = paramIRefresh;
	}

	@Override
	public void showEmptyText(Drawable paramDrawable, String paramString) {
		showView(false, true, false);
		if (paramDrawable != null) {
			this.mEmptyImageView.setVisibility(0);
			this.mEmptyImageView.setImageDrawable(paramDrawable);
		}
		this.mEmptyTextView.setText("" + paramString);
		this.mEmptyImageView.setVisibility(8);
	}

	public void showLoadFail() {
		showLoadFail(getResources().getDrawable(R.drawable.error));
	}

	@Override
	public void showLoadFail(Drawable paramDrawable) {
		showView(false, false, true);
		if (paramDrawable != null) {
			this.mErrorImageView.setVisibility(0);
			this.mErrorImageView.setImageDrawable(paramDrawable);
			return;
		}
		this.mErrorImageView.setVisibility(8);
	}

	public void showLoading() {
		showLoading("正在加载列表，请稍候！");
	}

	@Override
	public void showLoading(String paramString) {
		showView(true, false, false);
		this.mLoadingTextView.setText(paramString);
	}
}
